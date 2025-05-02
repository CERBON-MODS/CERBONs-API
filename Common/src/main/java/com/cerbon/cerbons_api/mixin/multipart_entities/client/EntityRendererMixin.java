package com.cerbon.cerbons_api.mixin.multipart_entities.client;

import com.cerbon.cerbons_api.api.multipart_entities.util.CompoundOrientedBox;
import com.cerbon.cerbons_api.api.multipart_entities.util.OrientedBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    //TODO: I have no idea if this will work. Probably not. I will find out soon
    @Inject(method = "extractAdditionalHitboxes", at = @At("TAIL"))
    private <T extends Entity> void createOrientedBoxesRenderStates(T entity, ImmutableList.Builder<HitboxRenderState> hitboxes, float partialTick, CallbackInfo ci) {
        AABB box = entity.getBoundingBox();
        if (!(box instanceof CompoundOrientedBox compound)) return;

        for (OrientedBox orientedBox : compound) {
            Vec3 center   = orientedBox.getCenter();
            Vec3 halfSize = orientedBox.getHalfExtents();

            double minX = center.x - halfSize.x;
            double minY = center.y - halfSize.y;
            double minZ = center.z - halfSize.z;
            double maxX = center.x + halfSize.x;
            double maxY = center.y + halfSize.y;
            double maxZ = center.z + halfSize.z;

            float x0 = (float) (minX - entity.getX());
            float y0 = (float) (minY - entity.getY());
            float z0 = (float) (minZ - entity.getZ());
            float x1 = (float) (maxX - entity.getX());
            float y1 = (float) (maxY - entity.getY());
            float z1 = (float) (maxZ - entity.getZ());

            hitboxes.add(new HitboxRenderState(
                    x0, y0, z0,
                    x1, y1, z1,
                    0f, 0f, 0f,
                    0f, 0f, 1f
            ));
        }

        compound.toVoxelShape().forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            float x0 = (float) (minX - entity.getX());
            float y0 = (float) (minY - entity.getY());
            float z0 = (float) (minZ - entity.getZ());
            float x1 = (float) (maxX - entity.getX());
            float y1 = (float) (maxY - entity.getY());
            float z1 = (float) (maxZ - entity.getZ());

            hitboxes.add(new HitboxRenderState(
                    x0, y0, z0,
                    x1, y1, z1,
                    0f, 0f, 0f,
                    0f, 1f, 0f
            ));
        });
    }

//    @Inject(method = "renderHitbox", at = @At("RETURN"))
//    private static void drawOrientedBoxes(PoseStack poseStack, VertexConsumer buffer, HitboxRenderState hitbox, CallbackInfo ci) {
//        final AABB box = entity.getBoundingBox();
//
//        if (box instanceof final CompoundOrientedBox compoundOrientedBox) {
//            poseStack.pushPose();
//            poseStack.translate(-entity.getX(), -entity.getY(), -entity.getZ());
//
//            for (final OrientedBox orientedBox : compoundOrientedBox) {
//                poseStack.pushPose();
//                final Vec3 center = orientedBox.getCenter();
//                poseStack.translate(center.x, center.y, center.z);
//                poseStack.mulPose(orientedBox.getRotation().toFloatQuat());
//                ShapeRenderer.renderLineBox(poseStack, buffer, orientedBox.getExtents(), 0, 0, 1, 1);
//                poseStack.popPose();
//            }
//
//            compoundOrientedBox.toVoxelShape().forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> ShapeRenderer.renderLineBox(poseStack, buffer, minX, minY, minZ, maxX, maxY, maxZ, 0, 1, 0, 1f));
//            poseStack.popPose();
//        }
//    }
}
