package com.cerbon.cerbons_api.packet.custom;

import com.cerbon.cerbons_api.api.multipart_entities.client.PlayerInteractMultipartEntity;
import com.cerbon.cerbons_api.api.multipart_entities.entity.MultipartAwareEntity;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

public class MultipartEntityInteractionC2SPacket {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "multipart_entity_interaction");
    public static final StreamCodec<FriendlyByteBuf, MultipartEntityInteractionC2SPacket> STREAM_CODEC = StreamCodec.ofMember(MultipartEntityInteractionC2SPacket::write, MultipartEntityInteractionC2SPacket::new);

    private final int entityId;
    private final String part;
    private final InteractionHand hand;
    private final boolean isSneaking;
    private final PlayerInteractMultipartEntity.InteractionType interactionType;

    public MultipartEntityInteractionC2SPacket(int entityId, String part, InteractionHand hand, boolean isSneaking, PlayerInteractMultipartEntity.InteractionType interactionType) {
        this.entityId = entityId;
        this.part = part;
        this.hand = hand;
        this.isSneaking = isSneaking;
        this.interactionType = interactionType;
    }

    public MultipartEntityInteractionC2SPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.part = buf.readUtf(32767);
        this.hand = buf.readEnum(InteractionHand.class);
        this.isSneaking = buf.readBoolean();
        this.interactionType = buf.readEnum(PlayerInteractMultipartEntity.InteractionType.class);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUtf(part);
        buf.writeEnum(hand);
        buf.writeBoolean(isSneaking);
        buf.writeEnum(interactionType);
    }

    public static void handle(PacketContext<MultipartEntityInteractionC2SPacket> ctx) {
        if (ctx.side().equals(Side.CLIENT) || ctx.sender() == null) return;

        ServerPlayer serverPlayer = ctx.sender();
        MultipartEntityInteractionC2SPacket packet = ctx.message();

        ServerLevel serverLevel = serverPlayer.serverLevel();

        serverPlayer.setShiftKeyDown(packet.isSneaking);
        Entity entity = serverLevel.getEntity(packet.entityId);
        if (entity == null) return;

        switch (packet.interactionType) {
            case INTERACT -> entity.interact(serverPlayer, packet.hand);
            case ATTACK -> setNextDamagedPart(serverPlayer, entity, packet.part);
            default -> throw new RuntimeException();
        }
    }

    private static void setNextDamagedPart(ServerPlayer serverPlayer, Entity entity, String part) {
        if (entity instanceof MultipartAwareEntity multipartAwareEntity)
            multipartAwareEntity.setNextDamagedPart(part);

        serverPlayer.attack(entity);
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }
}
