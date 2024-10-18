package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface IPacketRegistrar {

    /**
     * @return the side
     */
    Side getSide();

    /**
     * Packet Registration
     *
     * @param packetIdentifier The unique {@link ResourceLocation} packet id.
     * @param codec            The codec of the packet.
     * @param <T>              The class type
     * @param handler          The handler method.
     * @return The registrar for chaining registrations.
     */
    <T extends CustomPacketPayload> IPacketRegistrar registerPacket(ResourceLocation packetIdentifier, StreamCodec<RegistryFriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler);
}
