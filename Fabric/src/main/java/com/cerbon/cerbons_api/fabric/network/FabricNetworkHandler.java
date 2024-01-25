package com.cerbon.cerbons_api.fabric.network;

import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.PacketContainer;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FabricNetworkHandler extends PacketRegistrationHandler {
    private final Map<Class<?>, Message<?>> CHANNELS = new HashMap<>();

    public FabricNetworkHandler(Side side) {
        super(side);
    }

    @Override
    protected <T> void registerPacket(PacketContainer<T> container) {
        if (CHANNELS.get(container.messageType()) == null) {
            CHANNELS.put(container.messageType(), new Message<>(container.packetIdentifier(), container.encoder()));
            if (Side.CLIENT.equals(this.side)) {
                Constants.LOGGER.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), Side.CLIENT);

                ClientPlayNetworking.registerGlobalReceiver(container.packetIdentifier(), ((client, listener, buf, responseSender) -> {
                    buf.readByte(); // handle forge discriminator
                    T message = container.decoder().apply(buf);
                    client.execute(() -> container.handler().accept(new PacketContext<>(message, Side.CLIENT)));
                }));
            }
            else {
                Constants.LOGGER.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), Side.SERVER);

                ServerPlayNetworking.registerGlobalReceiver(container.packetIdentifier(), ((server, player, listener, buf, responseSender) -> {
                    buf.readByte(); // handle forge discriminator
                    T message = container.decoder().apply(buf);
                    server.execute(() -> container.handler().accept(new PacketContext<>(player, message, Side.SERVER)));
                }));
            }
        }
    }

    @Override
    public <T> void sendToServer(T packet) {
        this.sendToServer(packet, false);
    }

    @Override
    public <T> void sendToServer(T packet, boolean ignoreCheck) {
        Message<T> message = (Message<T>) CHANNELS.get(packet.getClass());
        if (ClientPlayNetworking.canSend(message.id()) || ignoreCheck) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeByte(0); // handle forge discriminator
            message.encoder().accept(packet, buf);
            ClientPlayNetworking.send(message.id(), buf);
        }
    }

    @Override
    public <T> void sendToClient(T packet, ServerPlayer player) {
        Message<T> message = (Message<T>) CHANNELS.get(packet.getClass());
        if (ServerPlayNetworking.canSend(player, message.id())) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeByte(0); // handle forge discriminator
            message.encoder().accept(packet, buf);
            ServerPlayNetworking.send(player, message.id(), buf);
        }
    }

    public record Message<T>(ResourceLocation id, BiConsumer<T, FriendlyByteBuf> encoder) {}
}
