package com.cerbon.cerbons_api.api.static_utilities;

import com.cerbon.cerbons_api.api.general.particle.ClientParticleBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ParticleUtils {

    public static void spawnParticle(ServerLevel level, ParticleOptions particleType, Vec3 pos, Vec3 velOrOffset, int count, double speed) {
        level.sendParticles(particleType, pos.x, pos.y, pos.z, count, velOrOffset.x, velOrOffset.y, velOrOffset.z, speed);
    }

    public static void spawnRotatingParticles(RotatingParticles particleParams) {
        int startingRotation = new Random().nextInt(360);
        double randomRadius = RandomUtils.range(particleParams.minRadius(), particleParams.maxRadius());
        double rotationSpeed = RandomUtils.range(particleParams.minSpeed(), particleParams.maxSpeed());
        ClientParticleBuilder particleBuilder = particleParams.particleBuilder();
        particleBuilder
                .continuousPosition(simpleParticle -> rotateAroundPos(particleParams.pos(), simpleParticle.getAge(), startingRotation, randomRadius, rotationSpeed))
                .build(rotateAroundPos(particleParams.pos(), 0, startingRotation, randomRadius, rotationSpeed), Vec3.ZERO);
    }

    private static Vec3 rotateAroundPos(Vec3 pos, int age, int startingRotation, double radius, double rotationSpeed) {
        Vec3 xzOffset = VecUtils.xAxis.yRot((float)Math.toRadians(age * rotationSpeed + startingRotation));
        return pos.add(xzOffset.scale(radius));
    }

    public record RotatingParticles(Vec3 pos, ClientParticleBuilder particleBuilder, double minRadius, double maxRadius, double minSpeed, double maxSpeed) {}
}