package com.cerbon.cerbons_api.api.network;

import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class PacketRegistrationHandler implements INetworkHandler, IPacketRegistrar {
    final Map<ResourceLocation, PacketContainer<?>> PACKET_MAP = new HashMap<>();

    protected final Side side;

    /**
     * Handles packet registration
     *
     * @param side - The side
     */
    public PacketRegistrationHandler(Side side) {
        this.side = side;
    }

    public <T extends CustomPacketPayload> IPacketRegistrar registerPacket(ResourceLocation packetIdentifier, StreamCodec<RegistryFriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler) {
        PacketContainer<T> container = new PacketContainer<>(packetIdentifier, codec, handler);
        PACKET_MAP.put(packetIdentifier, container);
        registerPacket(container);
        return this;
    }

    public Side getSide() {
        return side;
    }

    protected abstract <T extends CustomPacketPayload> void registerPacket(PacketContainer<T> container);
}
