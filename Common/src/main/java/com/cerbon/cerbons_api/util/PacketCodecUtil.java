package com.cerbon.cerbons_api.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PacketCodecUtil {
    public static <E extends Enum<E>> StreamCodec<FriendlyByteBuf, E> getEnumCodec(Class<E> enumClass) {
        return StreamCodec.of(FriendlyByteBuf::writeEnum, (buf) -> buf.readEnum(enumClass));
    }
}
