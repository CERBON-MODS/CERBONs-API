package com.cerbon.cerbons_api.fabric.network;

import com.cerbon.cerbons_api.api.network.Network;
import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FabricNetworkHandler extends PacketRegistrationHandler {
    private static final Set<ResourceLocation> REGISTERED_CHANNELS = new HashSet<>();
    private final Map<ResourceLocation, CustomPacketPayload.Type<?>> CHANNELS = new HashMap<>();

    public FabricNetworkHandler(Side side) {
        super(side);
    }

    @Override
    protected <T extends CustomPacketPayload> void registerPacket(PacketContainer<T> container) {
        if (!CHANNELS.containsKey(container.packetIdentifier())) {
            CustomPacketPayload.Type<T> type = Network.getType(container.packetIdentifier());
            CHANNELS.put(container.packetIdentifier(), type);

            if (!REGISTERED_CHANNELS.contains(container.packetIdentifier())) {
                PayloadTypeRegistry.playS2C().register(type, container.codec());
                PayloadTypeRegistry.playC2S().register(type, container.codec());
                REGISTERED_CHANNELS.add(container.packetIdentifier());
            }

            if (Side.CLIENT.equals(this.side)) {
                Constants.LOGGER.debug("Registering packet {} on the: {}", container.packetIdentifier(), Side.CLIENT);

                ClientPlayNetworking.registerGlobalReceiver(type, ((message, ctx) -> {
                    ctx.client().execute(() -> container.handler().accept(new PacketContext<>(message, Side.CLIENT)));
                }));
            }
            else {
                Constants.LOGGER.debug("Registering packet {} on the: {}", container.packetIdentifier(), Side.SERVER);

                ServerPlayNetworking.registerGlobalReceiver(type, ((message, ctx) -> {
                    ctx.server().execute(() -> container.handler().accept(new PacketContext<>(ctx.player(), message, Side.SERVER)));
                }));
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T packet) {
        this.sendToServer(packet, false);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T packet, boolean ignoreCheck) {
        if (ClientPlayNetworking.canSend(packet.type().id()) || ignoreCheck) {
            ClientPlayNetworking.send(packet);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player) {
        if (ServerPlayNetworking.canSend(player, packet.type().id())) {
            ServerPlayNetworking.send(player, packet);
        }
    }
}
