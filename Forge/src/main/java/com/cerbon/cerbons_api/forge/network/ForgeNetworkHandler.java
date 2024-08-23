package com.cerbon.cerbons_api.forge.network;

import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgeNetworkHandler extends PacketRegistrationHandler {
    private final Map<Class<?>, SimpleChannel> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler(Side side) {
        super(side);
    }

    protected <T> void registerPacket(PacketContainer<T> container) {
        if (CHANNELS.get(container.messageType()) == null) {

            SimpleChannel channel = ChannelBuilder
                    .named(container.packetIdentifier())
                    .clientAcceptedVersions((s, v) -> true)
                    .serverAcceptedVersions((s, v) -> true)
                    .networkProtocolVersion(1)
                    .simpleChannel();

            channel.messageBuilder(container.messageType())
                    .decoder(container.decoder())
                    .encoder(container.encoder())
                    .consumerNetworkThread(buildHandler(container.handler()))
                    .add();

            Constants.LOGGER.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), this.side);
            CHANNELS.put(container.messageType(), channel);
        }
    }

    public <T> void sendToServer(T packet) {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck) {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        try {
            if (ignoreCheck || channel.isRemotePresent(connection))
                channel.send(packet, PacketDistributor.SERVER.noArg());
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the client, this is needed for fabric.", packet.getClass(), t);
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player) {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = player.connection.getConnection();
        try {
            if (channel.isRemotePresent(connection))
                channel.send(packet, PacketDistributor.PLAYER.with(player));
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the server, this is needed for fabric.", packet.getClass(), t);
        }
    }

    private <T> BiConsumer<T, CustomPayloadEvent.Context> buildHandler(Consumer<PacketContext<T>> handler) {
        return (message, ctx) -> {
            ctx.enqueueWork(() -> {
                Side side = ctx.isServerSide() ? Side.SERVER : Side.CLIENT;
                ServerPlayer player = ctx.getSender();
                handler.accept(new PacketContext<>(player, message, side));
            });
            ctx.setPacketHandled(true);
        };
    }
}
