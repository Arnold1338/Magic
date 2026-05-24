package hellfirepvp.astralsorcery.common.crafting.nojson.fountain;

import net.minecraft.nbt.CompoundTag;
import java.util.Objects;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.Vec3i;
import javax.annotation.Nullable;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.tile.TileFountain;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrime;
import net.minecraft.resources.ResourceLocation;
import java.util.Random;

public abstract class FountainEffect<E extends EffectContext>
{
    protected static final Random rand;
    private final ResourceLocation id;
    
    protected FountainEffect(final ResourceLocation id) {
        this.id = id;
    }
    
    public final ResourceLocation getId() {
        return this.id;
    }
    
    @Nonnull
    public abstract BlockFountainPrime getAssociatedPrime();
    
    @Nonnull
    public abstract E createContext(final TileFountain p0);
    
    public abstract void tick(final TileFountain p0, final E p1, final int p2, final LogicalSide p3, final OperationSegment p4);
    
    public abstract void transition(final TileFountain p0, final E p1, final LogicalSide p2, final OperationSegment p3, final OperationSegment p4);
    
    public abstract void onReplace(final TileFountain p0, final E p1, @Nullable final FountainEffect<?> p2, final LogicalSide p3);
    
    @OnlyIn(Dist.CLIENT)
    protected void playFountainVortexParticles(final Vector3i pos, final float chance) {
        final Vector3 at = new Vector3(pos).add(0.5, 0.5, 0.5);
        for (int i = 0; i < 18; ++i) {
            if (FountainEffect.rand.nextFloat() < chance) {
                final Vector3 particlePos = new Vector3(pos.getX() - 3 + FountainEffect.rand.nextFloat() * 7.0f, pos.getY() + FountainEffect.rand.nextFloat(), pos.getZ() - 3 + FountainEffect.rand.nextFloat() * 7.0f);
                final Vector3 motion = particlePos.clone().vectorFromHereTo(at).normalize().divide(30.0);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(particlePos).setMotion(motion).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + FountainEffect.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE).setMaxAge(20 + FountainEffect.rand.nextInt(40));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void playFountainArcs(final Vector3i pos, final float chance) {
        if (FountainEffect.rand.nextFloat() < chance && FountainEffect.rand.nextInt(8) == 0) {
            final Vector3 at = new Vector3(pos).add(0.5, 0.5, 0.5);
            final Vector3 pos2 = Vector3.random().setY(0).normalize().multiply(4).add(at);
            final Vector3 pos3 = Vector3.random().setY(0).normalize().multiply(4).add(at);
            EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(pos2).makeDefault(pos3);
        }
    }
    
    public final boolean isInTick(final OperationSegment segment, final int operationTick) {
        final int start = this.getSegmentStartTick(segment);
        return operationTick > start && operationTick <= start + this.getSegmentDuration(segment);
    }
    
    public final int getSegmentStartTick(final OperationSegment segment) {
        int ticks = 0;
        for (int i = 0; i < segment.ordinal(); ++i) {
            final int duration = this.getSegmentDuration(OperationSegment.values()[i]);
            if (duration != -1) {
                ticks += duration;
            }
        }
        return ticks;
    }
    
    public final float getSegmentPercent(final OperationSegment segment, int operationTick) {
        final int start = this.getSegmentStartTick(segment);
        operationTick -= start;
        if (operationTick <= 0) {
            return 0.0f;
        }
        final float duration = (float)this.getSegmentDuration(segment);
        return Math.min(1.0f, operationTick / duration);
    }
    
    public int getSegmentDuration(final OperationSegment segment) {
        return segment.getDuration();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FountainEffect<?> effect = (FountainEffect<?>)o;
        return Objects.equals(this.id, effect.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    
    static {
        rand = new Random();
    }
    
    public abstract static class EffectContext
    {
        public abstract void readFromNBT(final CompoundTag p0);
        
        public abstract void writeToNBT(final CompoundTag p0);
    }
    
    public enum OperationSegment
    {
        INACTIVE(-1), 
        STARTUP(60), 
        PREPARATION(200), 
        RUNNING(-1);
        
        private final int duration;
        
        private OperationSegment(final int duration) {
            this.duration = duration;
        }
        
        public int getDuration() {
            return this.duration;
        }
        
        public boolean isLaterThan(final OperationSegment other) {
            return this.ordinal() > other.ordinal();
        }
        
        public boolean isLaterOrEqualTo(final OperationSegment other) {
            return this.ordinal() >= other.ordinal();
        }
    }
}
