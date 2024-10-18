package com.cerbon.cerbons_api.packet;

import com.cerbon.cerbons_api.api.network.Network;
import com.cerbon.cerbons_api.packet.custom.MultipartEntityInteractionC2SPacket;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.resources.ResourceLocation;

public class CerbonsApiPacketHandler {
    public static final ResourceLocation MULTIPART_ENTITY_INTERACTION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "multipart_entity_interaction");

    public void register() {
        Network.registerPacket(MULTIPART_ENTITY_INTERACTION,
                        MultipartEntityInteractionC2SPacket.CODEC,
                        MultipartEntityInteractionC2SPacket::handle
        );
    }
}
