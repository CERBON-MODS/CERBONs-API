package com.cerbon.cerbons_api.api.general.particle;

import net.minecraft.client.Camera;
import org.joml.Vector3f;

@FunctionalInterface
public interface IParticleGeometry {
    Vector3f[] getGeometry(
            Camera camera,
            float tickDelta,
            double prevPosX,
            double prevPosY,
            double prevPosZ,
            double x,
            double y,
            double z,
            float scale,
            float rotation
    );
}