package com.cerbon.cerbons_api.api.network.data;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record CommonPacketWrapper<T>(PacketContainer<T> container, T packet) implements CustomPacketPayload {

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return container.type();
    }
}
