package com.cerbon.cerbons_api.api.network.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public record PacketContainer<T>(CustomPacketPayload.Type<? extends CustomPacketPayload> type,
                                 Class<T> classType,
                                 BiConsumer<T, FriendlyByteBuf> encoder,
                                 Function<FriendlyByteBuf, T> decoder,
                                 StreamCodec<? super FriendlyByteBuf, T> codec,
                                 Consumer<PacketContext<T>> handler) {

    @SuppressWarnings("unchecked")
    public <B extends FriendlyByteBuf> PacketContainer(CustomPacketPayload.Type<? extends CustomPacketPayload> type,
                                                       Class<T> classType,
                                                       StreamCodec<? super B, T> codec,
                                                       Consumer<PacketContext<T>> handle) {
        this(type, classType, null, null, (StreamCodec<? super FriendlyByteBuf, T>) codec, handle);
    }

    @SuppressWarnings("unchecked")
    public <K extends CustomPacketPayload> CustomPacketPayload.Type<K> getType() {
        return (CustomPacketPayload.Type<K>) type();
    }

    //TODO: Removing for mc 1.21.2 or 1.22, will also be able to remove the wrapping of the decoder.
    @SuppressWarnings("unchecked")
    public <K extends FriendlyByteBuf> StreamCodec<K, CommonPacketWrapper> getCodec() {
        if (this.codec() == null) {
            // builds a codec from the supplied encoder and decoder.
            return CustomPacketPayload.codec(
                    (packet, buf) -> this.encoder().accept((T) packet.packet(), buf),
                    (buf) -> new CommonPacketWrapper<>(this, this.decoder().apply(buf)));
        }
        else {
            return CustomPacketPayload.codec(
                    (packet, buf) -> this.codec().encode(buf, (T) packet.packet()),
                    (buf) -> new CommonPacketWrapper<>(this, this.codec().decode(buf)));
        }
    }
}
