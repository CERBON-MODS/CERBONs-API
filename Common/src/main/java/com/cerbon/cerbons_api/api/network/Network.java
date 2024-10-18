package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Network {
    private static final Map<ResourceLocation, CustomPacketPayload.Type<?>> TYPES = new HashMap<>();

    /**
     * Packet Registration
     *
     * @param packetIdentifier The unique {@link ResourceLocation} packet id.
     * @param codec            The codec.
     * @param handler          The handler method.
     * @param <T>              The type
     * @return The registrar for chaining registrations.
     */
    public static <T extends CustomPacketPayload> IPacketRegistrar registerPacket(ResourceLocation packetIdentifier, StreamCodec<RegistryFriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler) {
        return CommonNetwork.registerPacket(packetIdentifier, codec, handler);
    }

    /**
     * Gets the Network handler for use to send packets.
     *
     * @return - The network handler
     */
    public static INetworkHandler getNetworkHandler() {
        return CommonNetwork.INSTANCE.packetRegistration();
    }

    /**
     * Returns the type of the network packet.
     *
     * @param packetIdentifier The unique {@link ResourceLocation} packet id.
     * @param <T>              The class type.
     * @return The payload type.
     */
    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType(ResourceLocation packetIdentifier) {
        return (CustomPacketPayload.Type<T>) TYPES.computeIfAbsent(packetIdentifier, id -> new CustomPacketPayload.Type<>(id));
    }
}
