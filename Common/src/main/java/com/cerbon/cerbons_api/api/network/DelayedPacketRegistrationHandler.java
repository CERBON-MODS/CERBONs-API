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

public class DelayedPacketRegistrationHandler implements IPacketRegistrar {
    private static final Map<Class<?>, PacketContainer<?>> QUEUED_PACKET_MAP = new HashMap<>();

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public <T> IPacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler) {
        PacketContainer<T> container = new PacketContainer<>(type, packetClass, codec, handler);
        QUEUED_PACKET_MAP.put(packetClass, container);
        return this;
    }


    public void registerQueuedPackets(PacketRegistrationHandler packetRegistration) {
        if (!QUEUED_PACKET_MAP.isEmpty()) {
            packetRegistration.PACKET_MAP.putAll(QUEUED_PACKET_MAP);
            QUEUED_PACKET_MAP.forEach((aClass, container) -> packetRegistration.registerPacket(container));
        }
    }
}
