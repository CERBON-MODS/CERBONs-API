package com.cerbon.cerbons_api.forge.network;

import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgeNetworkHandler extends PacketRegistrationHandler {
    private final Map<Class<?>, SimpleChannel> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler(Side side) {
        super(side);
    }

    @Override
    protected <T> void registerPacket(PacketContainer<T> container) {
        if (CHANNELS.get(container.messageType()) == null) {
            SimpleChannel channel = NetworkRegistry.ChannelBuilder
                    .named(container.packetIdentifier())
                    .clientAcceptedVersions((s) -> true)
                    .serverAcceptedVersions((s) -> true)
                    .networkProtocolVersion(() -> "1")
                    .simpleChannel();
            channel.registerMessage(0, container.messageType(), container.encoder(), container.decoder(), buildHandler(container.handler()));
            Constants.LOGGER.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), this.side);
            CHANNELS.put(container.messageType(), channel);
        }
    }

    @Override
    public <T> void sendToServer(T packet) {
        this.sendToServer(packet, false);
    }

    @Override
    public <T> void sendToServer(T packet, boolean ignoreCheck) {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (channel.isRemotePresent(connection) || ignoreCheck)
            channel.sendToServer(packet);
    }

    @Override
    public <T> void sendToClient(T packet, ServerPlayer player) {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = player.connection.connection;
        if (channel.isRemotePresent(connection))
            channel.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    private <T> BiConsumer<T, Supplier<NetworkEvent.Context>> buildHandler(Consumer<PacketContext<T>> handler) {
        return (message, ctx) -> {
            ctx.get().enqueueWork(() -> {
                Side side = ctx.get().getDirection().getReceptionSide().isServer() ? Side.SERVER : Side.CLIENT;
                ServerPlayer player = ctx.get().getSender();
                handler.accept(new PacketContext<>(player, message, side));
            });
            ctx.get().setPacketHandled(true);
        };
    }
}
