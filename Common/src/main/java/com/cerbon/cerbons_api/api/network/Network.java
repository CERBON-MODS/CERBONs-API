package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Network {

    /**
     * Packet Registration
     *
     * @param packetIdentifier The unique {@link ResourceLocation} packet id.
     * @param messageType      The class of the packet.
     * @param encoder          The encoder method.
     * @param decoder          The decoder method.
     * @param handler          The handler method.
     * @param <T>              The type
     * @return The registrar for chaining registrations.
     */
    public static <T> IPacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, FriendlyByteBuf> encoder, Consumer<PacketContext<T>> handler) {
        return CommonNetwork.registerPacket(packetIdentifier, messageType, encoder, decoder, handler);
    }

    /**
     * Gets the Network handler for use to send packets.
     *
     * @return - The network handler
     */
    public static INetworkHandler getNetworkHandler() {
        return CommonNetwork.INSTANCE.packetRegistration();
    }
}
