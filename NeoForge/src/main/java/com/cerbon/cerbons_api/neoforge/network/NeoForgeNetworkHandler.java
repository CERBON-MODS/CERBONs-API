package com.cerbon.cerbons_api.neoforge.network;

import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class NeoForgeNetworkHandler extends PacketRegistrationHandler {
    private final Map<Class<?>, NeoForgePacketContainer> PACKETS = new HashMap<>();

    public NeoForgeNetworkHandler(Side side) {
        super(side);
    }

    @SubscribeEvent
    public void register(final RegisterPayloadHandlerEvent event) {
        if (!PACKETS.isEmpty()) {
            PACKETS.forEach((type, container) -> {
                final IPayloadRegistrar registrar = event.registrar(container.packetIdentifier().getNamespace());
                registrar.common(
                        container.packetIdentifier(),
                        container.decoder(),
                        container.handler());
            });
        }
    }

    protected <T> void registerPacket(PacketContainer<T> container) {
        if (PACKETS.get(container.messageType()) == null) {
            var packetContainer = new NeoForgePacketContainer<>(
                    container.messageType(),
                    container.packetIdentifier(),
                    container.encoder(),
                    decoder(container.decoder()),
                    buildHandler(container.handler())
            );

            PACKETS.put(container.messageType(), packetContainer);
        }
    }

    private <T> FriendlyByteBuf.Reader<NeoForgePacket<T>> decoder(Function<FriendlyByteBuf, T> decoder) {
        return (buf -> {
            T packet = decoder.apply(buf);
            return new NeoForgePacket<T>(PACKETS.get(packet.getClass()), packet);
        });
    }

    public <T> void sendToServer(T packet) {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck) {
        NeoForgePacketContainer<T> container = PACKETS.get(packet.getClass());
        try {
            PacketDistributor.SERVER.noArg().send(new NeoForgePacket<>(container, packet));
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the client, this is needed.", packet.getClass(), t);
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player) {
        NeoForgePacketContainer<T> container = PACKETS.get(packet.getClass());
        try {
            if (player.connection.isConnected(container.packetIdentifier()))
                PacketDistributor.PLAYER.with(player).send(new NeoForgePacket<>(container, packet));
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the server, this is needed.", packet.getClass(), t);
        }
    }

    private <T, K extends NeoForgePacket<T>> IPayloadHandler<K> buildHandler(Consumer<PacketContext<T>> handler) {
        return (payload, ctx) -> {
            try {
                Side side = ctx.flow().getReceptionSide().equals(LogicalSide.SERVER) ? Side.SERVER : Side.CLIENT;
                Player player = ctx.player().orElse(null);
                handler.accept(new PacketContext<>(player instanceof ServerPlayer serverPlayer ? serverPlayer : null, payload.packet(), side));
            }
            catch (Throwable t) {
                Constants.LOGGER.error("Error handling packet: {} -> ", payload.packet().getClass(), t);
            }
        };
    }
}
