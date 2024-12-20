package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public record CommonNetwork(PacketRegistrationHandler packetRegistration) {
    private static DelayedPacketRegistrationHandler delayedHandler;
    public static CommonNetwork INSTANCE;

    public CommonNetwork(PacketRegistrationHandler packetRegistration) {
        INSTANCE = this;
        this.packetRegistration = packetRegistration;
        getDelayedHandler().registerQueuedPackets(packetRegistration);
    }

    /**
     * Fabric does not enforce load order, so we may have to delay packet registrations.
     *
     * @return the handler;
     */
    private static DelayedPacketRegistrationHandler getDelayedHandler() {
        if (delayedHandler == null)
            delayedHandler = new DelayedPacketRegistrationHandler();

        return delayedHandler;
    }

    public static <T> IPacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler) {
        if (INSTANCE != null) {
            return INSTANCE.packetRegistration.registerPacket(type, packetClass, codec, handler);
        }
        else
            return getDelayedHandler().registerPacket(type, packetClass, codec, handler);
    }
}
