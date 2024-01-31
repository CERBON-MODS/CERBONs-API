package com.cerbon.cerbons_api.packet;

import com.cerbon.cerbons_api.api.network.Network;
import com.cerbon.cerbons_api.packet.custom.MultipartEntityInteractionC2SPacket;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.resources.ResourceLocation;

public class CerbonsApiPacketHandler {
    private final ResourceLocation CHANNEL = new ResourceLocation(Constants.MOD_ID, "packets");

    public void register() {
        Network.registerPacket(CHANNEL, MultipartEntityInteractionC2SPacket.class,
                        MultipartEntityInteractionC2SPacket::new,
                        MultipartEntityInteractionC2SPacket::write,
                        MultipartEntityInteractionC2SPacket::handle
        );
    }
}
