package hellfirepvp.astralsorcery.client.effect.source;

import net.minecraft.client.Minecraft;
import java.util.function.Function;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public abstract class FXSourceOrbital<E extends EntityVisualFX, T extends BatchRenderContext<E>> extends FXSource<E, T>
{
    private double orbitRadius;
    private int tickOffset;
    private int branches;
    private Vector3 orbitAxis;
    private Vector3 offset;
    
    public FXSourceOrbital(final Vector3 pos, final T template) {
        super(pos, template);
        this.orbitRadius = 1.0;
        this.tickOffset = 0;
        this.branches = 1;
        this.orbitAxis = Vector3.RotAxis.Y_AXIS;
        this.offset = new Vector3();
    }
    
    public FXSourceOrbital setTicksPerRotation(final int ticks) {
        this.maxAge = ticks;
        return this;
    }
    
    public FXSourceOrbital setBranches(final int branches) {
        this.branches = branches;
        return this;
    }
    
    public FXSourceOrbital setOrbitRadius(final double orbitRadius) {
        this.orbitRadius = orbitRadius;
        return this;
    }
    
    public FXSourceOrbital setOrbitAxis(@Nonnull final Vector3 orbitAxis) {
        this.orbitAxis = orbitAxis.clone().normalize();
        return this;
    }
    
    public FXSourceOrbital setTickOffset(final int tickOffset) {
        this.tickOffset = tickOffset;
        return this;
    }
    
    public FXSourceOrbital setOffset(@Nonnull final Vector3 offset) {
        this.offset = offset;
        return this;
    }
    
    @Nonnull
    public Vector3 getOffset() {
        return this.offset.clone();
    }
    
    @Nonnull
    public Vector3 getOrbitAxis() {
        return this.orbitAxis.clone();
    }
    
    @Override
    public void tickSpawnFX(final Function<Vector3, E> effectRegistrar) {
        if (Minecraft.func_71410_x().func_147113_T()) {
            return;
        }
        for (int branch = 0; branch < this.branches; ++branch) {
            final Vector3 point = this.orbitAxis.clone().perpendicular().normalize().multiply(this.orbitRadius).rotate(Math.toRadians(this.getRotationDegree(branch)), this.orbitAxis).add(this.offset).add(this.getPosition());
            this.spawnOrbitalParticle(point, effectRegistrar);
        }
    }
    
    public abstract void spawnOrbitalParticle(final Vector3 p0, final Function<Vector3, E> p1);
    
    private double getRotationDegree(final int branch) {
        final double perc = (this.age + this.tickOffset) % this.maxAge / (double)this.maxAge;
        return 360.0f / this.branches * branch + 360.0 * perc;
    }
}
