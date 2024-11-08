package com.cerbon.cerbons_api.fabric.network;

import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.CommonPacketWrapper;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.api.network.exceptions.RegistrationException;
import com.cerbon.cerbons_api.util.Constants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHandler extends PacketRegistrationHandler {

    public FabricNetworkHandler(Side side) {
        super(side);
    }

    @SuppressWarnings("unchecked")
    protected <T> void registerPacket(PacketContainer<T> container) {
        try {
            PayloadTypeRegistry.playC2S().register(container.getType(), container.getCodec());
            PayloadTypeRegistry.playS2C().register(container.getType(), container.getCodec());
        }
        catch (IllegalArgumentException e) {
            // do nothing
        }

        if (Side.CLIENT.equals(this.side)) {
            Constants.LOGGER.info("Registering packet {} : {} on the: {}", container.type().id(), container.classType(), Side.CLIENT);

            ClientPlayNetworking.registerGlobalReceiver(container.getType(),
                    (ClientPlayNetworking.PlayPayloadHandler<CommonPacketWrapper<T>>) (payload, context) -> context.client().execute(() ->
                            container.handler().accept(
                                    new PacketContext<>(payload.packet(), Side.CLIENT))));
        }

        Constants.LOGGER.info("Registering packet {} : {} on the: {}", container.type().id(), container.classType(), Side.SERVER);
        ServerPlayNetworking.registerGlobalReceiver(container.getType(),
                (ServerPlayNetworking.PlayPayloadHandler<CommonPacketWrapper<T>>) (payload, context) -> context.player().server.execute(() ->
                        container.handler().accept(
                                new PacketContext<>(context.player(), payload.packet(), Side.SERVER))));

    }

    @SuppressWarnings("unchecked")
    public <T> void sendToServer(T packet, boolean ignoreCheck) {
        PacketContainer<T> container = (PacketContainer<T>) PACKET_MAP.get(packet.getClass());
        if (container != null) {
            if (ignoreCheck || ClientPlayNetworking.canSend(container.type().id()))
                ClientPlayNetworking.send(new CommonPacketWrapper<>(container, packet));
        }
        else
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the client, packets need to be registered on both sides!");

    }

    @SuppressWarnings("unchecked")
    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck) {
        PacketContainer<T> container = (PacketContainer<T>) PACKET_MAP.get(packet.getClass());
        if (container != null) {
            if (ignoreCheck || ServerPlayNetworking.canSend(player, container.type().id()))
                ServerPlayNetworking.send(player, new CommonPacketWrapper<>(container, packet));
        }
        else
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the server, packets need to be registered on both sides!");
    }
}
