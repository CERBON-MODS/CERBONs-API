package com.cerbon.cerbons_api.mixin.multipart_entities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    //TODO: Find workaround
    @Inject(method = "renderHitbox", at = @At("RETURN"))
    private static void drawOrientedBoxes(PoseStack poseStack, VertexConsumer buffer, HitboxRenderState hitbox, CallbackInfo ci) {
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
    }
}
