package com.cerbon.cerbons_api.neoforge.network;

import com.cerbon.cerbons_api.api.network.Network;
import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NeoForgeNetworkHandler extends PacketRegistrationHandler {
    private final Map<ResourceLocation, NeoForgePacketContainer<?>> PACKETS = new HashMap<>();

    public NeoForgeNetworkHandler(Side side) {
        super(side);
    }

    @SubscribeEvent
    public void register(final RegisterPayloadHandlersEvent event) {
        if (!PACKETS.isEmpty()) {
            PACKETS.forEach((type, container) -> {
                final PayloadRegistrar registrar = event.registrar(container.packetIdentifier().getNamespace());
                registrar.commonBidirectional(
                        Network.getType(container.packetIdentifier()),
                        (StreamCodec<? super FriendlyByteBuf, CustomPacketPayload>) (Object) container.codec(),
                        (IPayloadHandler<CustomPacketPayload>) container.handler());
            });
        }
    }

    protected <T extends CustomPacketPayload> void registerPacket(PacketContainer<T> container) {
        if (PACKETS.get(container.packetIdentifier()) == null) {
            var packetContainer = new NeoForgePacketContainer<T>(
                    container.packetIdentifier(),
                    container.codec(),
                    buildHandler(container.handler())
            );

            PACKETS.put(container.packetIdentifier(), packetContainer);
        }
    }

    public <T extends CustomPacketPayload> void sendToServer(T packet) {
        this.sendToServer(packet, false);
    }

    public <T extends CustomPacketPayload> void sendToServer(T packet, boolean ignoreCheck) {
        try {
            PacketDistributor.sendToServer(packet);
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the client, this is needed.", packet.getClass(), t);
        }
    }

    public <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player) {
        try {
            if (NetworkRegistry.hasChannel(player.connection, packet.type().id()))
                PacketDistributor.sendToPlayer(player, packet);
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the server, this is needed.", packet.getClass(), t);
        }
    }

    private <T extends CustomPacketPayload> IPayloadHandler<T> buildHandler(Consumer<PacketContext<T>> handler) {
        return (payload, ctx) -> {
            try {
                Side side = ctx.flow().getReceptionSide().equals(LogicalSide.SERVER) ? Side.SERVER : Side.CLIENT;
                Player player = ctx.player();
                handler.accept(new PacketContext<>(player instanceof ServerPlayer serverPlayer ? serverPlayer : null, payload, side));
            }
            catch (Throwable t) {
                Constants.LOGGER.error("Error handling packet: {} -> ", payload.type().id(), t);
            }
        };
    }
}
