package com.cerbon.cerbons_api.packet.custom;

import com.cerbon.cerbons_api.api.multipart_entities.client.PlayerInteractMultipartEntity;
import com.cerbon.cerbons_api.api.multipart_entities.entity.MultipartAwareEntity;
import com.cerbon.cerbons_api.api.network.Network;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.packet.CerbonsApiPacketHandler;
import com.cerbon.cerbons_api.util.PacketCodecUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

public class MultipartEntityInteractionC2SPacket implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, MultipartEntityInteractionC2SPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, e -> e.entityId,
        ByteBufCodecs.STRING_UTF8, e -> e.part,
        PacketCodecUtil.getEnumCodec(InteractionHand.class), e -> e.hand,
        ByteBufCodecs.BOOL, e -> e.isSneaking,
        PacketCodecUtil.getEnumCodec(PlayerInteractMultipartEntity.InteractionType.class), e -> e.interactionType,
        MultipartEntityInteractionC2SPacket::new
    );

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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Network.getType(CerbonsApiPacketHandler.MULTIPART_ENTITY_INTERACTION);
    }
}
