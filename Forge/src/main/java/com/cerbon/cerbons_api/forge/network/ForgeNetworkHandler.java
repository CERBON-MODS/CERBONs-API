package com.cerbon.cerbons_api.forge.network;

import com.cerbon.cerbons_api.api.network.Network;
import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.payload.PayloadConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgeNetworkHandler extends PacketRegistrationHandler {
    private final Map<ResourceLocation, Channel<CustomPacketPayload>> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler(Side side) {
        super(side);
    }

    protected <T extends CustomPacketPayload> void registerPacket(PacketContainer<T> container) {
        if (CHANNELS.get(container.packetIdentifier()) == null) {

            PayloadConnection<CustomPacketPayload> builder = ChannelBuilder
                    .named(container.packetIdentifier())
                    .clientAcceptedVersions((s, v) -> true)
                    .serverAcceptedVersions((s, v) -> true)
                    .networkProtocolVersion(1)
                .payloadChannel();

            var channel = builder.play()
                .bidirectional()
                .add(Network.getType(container.packetIdentifier()), container.codec(), buildHandler(container.handler()))
                .build();

            Constants.LOGGER.debug("Registering packet {} on the: {}", container.packetIdentifier(), this.side);
            CHANNELS.put(container.packetIdentifier(), channel);
        }
    }

    public <T extends CustomPacketPayload> void sendToServer(T packet) {
        this.sendToServer(packet, false);
    }

    public <T extends CustomPacketPayload> void sendToServer(T packet, boolean ignoreCheck) {
        Channel<CustomPacketPayload> channel = CHANNELS.get(packet.type().id());
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        try {
            if (ignoreCheck || channel.isRemotePresent(connection))
                channel.send(packet, PacketDistributor.SERVER.noArg());
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the client, this is needed for fabric.", packet.getClass(), t);
        }
    }

    public <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player) {
        Channel<CustomPacketPayload> channel = CHANNELS.get(packet.type().id());
        Connection connection = player.connection.getConnection();
        try {
            if (channel.isRemotePresent(connection))
                channel.send(packet, PacketDistributor.PLAYER.with(player));
        }
        catch (Throwable t) {
            Constants.LOGGER.error("{} packet not registered on the server, this is needed for fabric.", packet.getClass(), t);
        }
    }

    private <T extends CustomPacketPayload> BiConsumer<T, CustomPayloadEvent.Context> buildHandler(Consumer<PacketContext<T>> handler) {
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
