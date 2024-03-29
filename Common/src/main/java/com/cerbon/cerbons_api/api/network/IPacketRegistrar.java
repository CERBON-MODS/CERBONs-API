package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IPacketRegistrar {

    /**
     * @return the side
     */
    Side getSide();

    /**
     * Packet Registration
     *
     * @param packetIdentifier The unique {@link ResourceLocation} packet id.
     * @param messageType      The class of the packet.
     * @param encoder          The encoder method.
     * @param decoder          The decoder method.
     * @param handler          The handler method.
     * @param <T>              The class type
     * @return The registrar for chaining registrations.
     */
    <T> IPacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler);
}
