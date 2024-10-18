package com.cerbon.cerbons_api.api.network.data;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public record PacketContainer<T extends CustomPacketPayload>(ResourceLocation packetIdentifier,
                                                             StreamCodec<RegistryFriendlyByteBuf, T> codec,
                                                             Consumer<PacketContext<T>> handler) {
}
