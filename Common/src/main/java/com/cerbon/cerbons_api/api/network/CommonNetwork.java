package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static <T> IPacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler) {
        if (INSTANCE != null)
            return INSTANCE.packetRegistration.registerPacket(packetIdentifier, messageType, encoder, decoder, handler);

        return getDelayedHandler().registerPacket(packetIdentifier, messageType, encoder, decoder, handler);
    }
}
