package com.cerbon.cerbons_api.packet;

import com.cerbon.cerbons_api.CerbonsApi;
import com.cerbon.cerbons_api.packet.custom.multipart_entities.MultipartEntityInteractionC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CerbonsApiPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(CerbonsApi.MOD_ID, "packets"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(MultipartEntityInteractionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MultipartEntityInteractionC2SPacket::new)
                .encoder(MultipartEntityInteractionC2SPacket::write)
                .consumerMainThread(MultipartEntityInteractionC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
