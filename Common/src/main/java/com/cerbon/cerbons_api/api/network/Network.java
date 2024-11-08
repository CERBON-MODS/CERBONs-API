package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public class Network {

    /**
     * Packet Registration
     *
     * @param type        - The packet type.
     * @param packetClass - The class of the packet.
     * @param codec       - The StreamCodec.
     * @param handler     - The handler method.
     * @param <T>         - The class type
     * @return The registrar for chaining registrations.
     */
    public static <T> IPacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler) {
        return CommonNetwork.registerPacket(type, packetClass, codec, handler);
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
