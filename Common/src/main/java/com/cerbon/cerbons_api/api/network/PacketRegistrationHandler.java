package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class PacketRegistrationHandler implements INetworkHandler, IPacketRegistrar {
    protected final Map<Class<?>, PacketContainer<?>> PACKET_MAP = new HashMap<>();

    protected final Side side;

    /**
     * Handles packet registration
     *
     * @param side - The side
     */
    public PacketRegistrationHandler(Side side) {
        this.side = side;
    }

    @Override
    public <T> IPacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler) {
        PacketContainer<T> container = new PacketContainer<>(type, packetClass, codec, handler);
        PACKET_MAP.put(packetClass, container);
        registerPacket(container);
        return this;
    }

    public Side getSide() {
        return side;
    }

    protected abstract <T> void registerPacket(PacketContainer<T> container);
}
