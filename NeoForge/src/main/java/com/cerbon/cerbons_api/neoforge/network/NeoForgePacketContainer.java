package com.cerbon.cerbons_api.neoforge.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record NeoForgePacketContainer<T extends CustomPacketPayload>(ResourceLocation packetIdentifier,
                                                                     StreamCodec<RegistryFriendlyByteBuf, T> codec,
                                                                     IPayloadHandler<T> handler)
{
}
