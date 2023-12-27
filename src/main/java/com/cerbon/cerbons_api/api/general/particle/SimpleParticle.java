package com.cerbon.cerbons_api.api.general.particle;

import com.cerbon.cerbons_api.api.static_utilities.RandomUtils;
import com.cerbon.cerbons_api.api.static_utilities.VecUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Function;

public class SimpleParticle extends TextureSheetParticle {
    private final ParticleContext particleContext;
    private final IParticleGeometry particleGeometry;
    private final boolean cycleSprites;

    private Function<SimpleParticle, Float> rotationOverride;
    private Function<SimpleParticle, Vec3> velocityOverride;
    private Function<SimpleParticle, Vec3> positionOverride;
    private Function<Float, Float> scaleOverride;
    private Function<Float, Vec3> colorOverride;
    private Function<Float, Integer> brightnessOverride;
    private Vec3 colorVariation = Vec3.ZERO;

    private float prevRotation = 0f;
    private float rotation = 0f;
    public float ageRatio = 1f;

    public SimpleParticle(ParticleContext particleContext, int particleAge, IParticleGeometry particleGeometry, boolean cycleSprites, boolean doCollision) {
        super(particleContext.level(), particleContext.pos().x(), particleContext.pos().y(), particleContext.pos().z());
        this.particleContext = particleContext;
        this.lifetime = particleAge;
        this.particleGeometry = particleGeometry;
        this.cycleSprites = cycleSprites;
        this.hasPhysics = doCollision;
        this.xd = particleContext.vel().x();
        this.yd = particleContext.vel().y();
        this.zd = particleContext.vel().z();

        if (cycleSprites) setSpriteFromAge(particleContext.spriteSet());
        else setSprite(particleContext.spriteSet().get(this.random));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public @NotNull Vec3 getPos() {
        return new Vec3(x, y, z);
    }

    public int getAge() {
        return age;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isAlive()) return;

        if (cycleSprites) setSpriteFromAge(particleContext.spriteSet());
        ageRatio = (float) age / lifetime;
        setColorFromOverride(colorOverride, ageRatio);
        setScaleFromOverride(scaleOverride, ageRatio);
        setVelocityFromOverride(velocityOverride);
        setPositionFromOverride(positionOverride);
        setRotationFromOverride(rotationOverride);

    }

    private void setRotationFromOverride(Function<SimpleParticle, Float> rotationOverride) {
        if (rotationOverride == null) return;

        float rot = rotationOverride.apply(this);
        prevRotation = rotation;
        rotation = rot;
    }

    private void setVelocityFromOverride(Function<SimpleParticle, Vec3> velocityOverride) {
        if (velocityOverride == null) return;

        Vec3 velocity = velocityOverride.apply(this);
        xd = velocity.x();
        yd = velocity.y();
        zd = velocity.z();
    }

    private void setPositionFromOverride(Function<SimpleParticle, Vec3> positionOverride) {
        if (positionOverride == null) return;

        Vec3 pos = positionOverride.apply(this);
        setPos(pos.x(), pos.y(), pos.z());
    }

    private void setScaleFromOverride(Function<Float, Float> scaleOverride, float ageRatio) {
        if (scaleOverride == null) return;

        quadSize = scaleOverride.apply(ageRatio);
        setSize(0.2f * quadSize, 0.2f * quadSize);
    }

    private void setColorFromOverride(Function<Float, Vec3> colorOverride, float ageRatio) {
        if (colorOverride == null) return;

        Vec3 color = colorOverride.apply(ageRatio);
        Vec3 variedColor = VecUtils.coerceAtMost(VecUtils.coerceAtLeast(color.add(colorVariation), Vec3.ZERO), VecUtils.unit);
        setColor((float) variedColor.x(), (float) variedColor.y(), (float) variedColor.z());
    }

    public void setBrightnessOverride(Function<Float, Integer> override) {
        brightnessOverride = override;
    }

    public void setColorOverride(Function<Float, Vec3> override) {
        colorOverride = override;
        setColorFromOverride(override, 0f);
    }

    public void setScaleOverride(Function<Float, Float> override) {
        scaleOverride = override;
        setScaleFromOverride(override, 0f);
    }

    public void setColorVariation(double variation) {
        colorVariation = RandomUtils.randVec().scale(variation);
        setColorFromOverride(colorOverride, 0f);
    }

    public void setVelocityOverride(Function<SimpleParticle, Vec3> override) {
        velocityOverride = override;
    }

    public void setPositionOverride(Function<SimpleParticle, Vec3> override) {
        positionOverride = override;
    }

    public void setRotationOverride(Function<SimpleParticle, Float> override) {
        rotationOverride = override;

        if (rotationOverride != null) {
            rotation = rotationOverride.apply(this);
            prevRotation = rotationOverride.apply(this);
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        return brightnessOverride != null ? brightnessOverride.apply(ageRatio) : super.getLightColor(partialTick);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, @NotNull Camera camera, float partialTicks) {
        Vector3f[] vector3fs = particleGeometry.getGeometry(
                camera,
                partialTicks,
                xo, yo, zo,
                x, y, z,
                getQuadSize(partialTicks),
                Mth.lerp(partialTicks, prevRotation, rotation)
        );

        float l = this.getU0();
        float m = this.getU1();
        float n = this.getV0();
        float o = this.getV1();
        float p = getLightColor(partialTicks);

        vertexConsumer.vertex(
                vector3fs[0].x(), vector3fs[0].y(),
                vector3fs[0].z()
        ).uv(m, o).color(rCol, gCol, bCol, alpha).uv2((int) p).endVertex();

        vertexConsumer.vertex(
                vector3fs[1].x(), vector3fs[1].y(),
                vector3fs[1].z()
        ).uv(m, n).color(rCol, gCol, bCol, alpha).uv2((int) p).endVertex();

        vertexConsumer.vertex(
                vector3fs[2].x(), vector3fs[2].y(),
                vector3fs[2].z()
        ).uv(l, n).color(rCol, gCol, bCol, alpha).uv2((int) p).endVertex();

        vertexConsumer.vertex(
                vector3fs[3].x(), vector3fs[3].y(),
                vector3fs[3].z()
        ).uv(l, o).color(rCol, gCol, bCol, alpha).uv2((int) p).endVertex();
    }
}