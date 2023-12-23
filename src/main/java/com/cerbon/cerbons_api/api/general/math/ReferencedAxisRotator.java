package com.cerbon.cerbons_api.api.general.math;

import com.cerbon.cerbons_api.api.static_utilities.VecUtils;
import net.minecraft.world.phys.Vec3;

public class ReferencedAxisRotator {
    private final double angleBetween;
    private final Vec3 rotationAxis;

    public ReferencedAxisRotator(Vec3 originalAxis, Vec3 newAxis) {
        this.angleBetween = VecUtils.unsignedAngle(originalAxis, newAxis);
        this.rotationAxis = originalAxis.cross(newAxis);
    }

    public Vec3 rotate(Vec3 vec) {
        return VecUtils.rotateVector(vec, rotationAxis, angleBetween);
    }
}
