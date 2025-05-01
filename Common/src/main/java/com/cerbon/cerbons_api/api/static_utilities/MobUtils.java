package com.cerbon.cerbons_api.api.static_utilities;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MobUtils {
    public static void setPos(Entity entity, Vec3 vec) {
        entity.absSnapTo(vec.x, vec.y, vec.z);
    }

    public static Vec3 eyePos(Entity entity) {
        return entity.getEyePosition(1.0f);
    }

    public static Vec3 lastRenderPos(Entity entity) {
        return new Vec3(entity.xOld, entity.yOld, entity.zOld);
    }

    public static void preventDespawnExceptPeaceful(LivingEntity mobEntity, Level level) {
        if (level.getDifficulty() == Difficulty.PEACEFUL) mobEntity.discard();
        else mobEntity.setNoActionTime(0);
    }
}
