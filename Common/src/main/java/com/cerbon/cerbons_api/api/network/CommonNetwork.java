package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

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

    public static <T extends CustomPacketPayload> IPacketRegistrar registerPacket(ResourceLocation packetIdentifier, StreamCodec<RegistryFriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler) {
        if (INSTANCE != null)
            return INSTANCE.packetRegistration.registerPacket(packetIdentifier, codec, handler);

        return getDelayedHandler().registerPacket(packetIdentifier, codec, handler);
    }
}
