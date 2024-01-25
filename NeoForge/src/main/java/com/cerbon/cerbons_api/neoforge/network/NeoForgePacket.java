package com.cerbon.cerbons_api.neoforge.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record NeoForgePacket<T>(NeoForgePacketContainer<T> container, T packet) implements CustomPacketPayload {

    @Override
    public void write(@NotNull FriendlyByteBuf buff) {
        container().encoder().accept(packet(), buff);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return container().packetIdentifier();
    }
}