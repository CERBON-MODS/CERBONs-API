package com.cerbon.cerbons_api.packet;

import com.cerbon.cerbons_api.api.network.Network;
import com.cerbon.cerbons_api.packet.custom.MultipartEntityInteractionC2SPacket;

public class CerbonsApiPacketHandler {

    public void register() {
        Network.registerPacket(
                MultipartEntityInteractionC2SPacket.type(),
                MultipartEntityInteractionC2SPacket.class,
                MultipartEntityInteractionC2SPacket.STREAM_CODEC,
                MultipartEntityInteractionC2SPacket::handle
        );
    }
}
