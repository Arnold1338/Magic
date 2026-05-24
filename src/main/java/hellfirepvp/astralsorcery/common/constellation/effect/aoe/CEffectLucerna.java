package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperSpawnDeny;
import hellfirepvp.astralsorcery.common.util.tick.TickTokenMap;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.source.orbital.FXOrbitalLucerna;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectStatus;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;

public class CEffectLucerna extends ConstellationEffect implements ConstellationEffectStatus
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static LucernaConfig CONFIG;
    private int rememberedTimeout;
    
    public CEffectLucerna(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.lucerna);
        this.rememberedTimeout = 0;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final World world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        if (ClientScheduler.getClientTick() % 20L == 0L) {
            EffectHelper.spawnSource(new FXOrbitalLucerna(new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5)).setOrbitAxis(Vector3.RotAxis.Y_AXIS).setOrbitRadius(0.8 + CEffectLucerna.rand.nextFloat() * 0.7).setTicksPerRotation(20 + CEffectLucerna.rand.nextInt(20)));
        }
    }
    
    @Override
    public boolean runStatusEffect(final World world, final BlockPos pos, final int mirrorAmount, final ConstellationEffectProperties modified, @Nullable final IMinorConstellation possibleTraitEffect) {
        if (modified.isCorrupted()) {
            if (world instanceof ServerLevel && DayTimeHelper.isNight(world) && CEffectLucerna.rand.nextBoolean()) {
                SkyHandler.getInstance().revertWorldTimeTick((ServerLevel)world);
            }
            return true;
        }
        final WorldBlockPos at = WorldBlockPos.wrapServer(world, pos);
        final TickTokenMap.SimpleTickToken<Double> token = EventHelperSpawnDeny.spawnDenyRegions.get(at);
        if (token != null && Math.abs(token.getValue() - modified.getSize()) < 0.001) {
            int next = token.getRemainingTimeout() + 80;
            if (next > 400) {
                next = 400;
            }
            token.setTimeout(next);
            this.rememberedTimeout = next;
        }
        else {
            if (token != null) {
                token.setTimeout(0);
            }
            this.rememberedTimeout = Math.min(400, this.rememberedTimeout + 80);
            EventHelperSpawnDeny.spawnDenyRegions.put(at, new TickTokenMap.SimpleTickToken<Double>(modified.getSize(), this.rememberedTimeout));
        }
        return true;
    }
    
    @Override
    public Config getConfig() {
        return CEffectLucerna.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectLucerna.FLAG;
    }
    
    @Override
    public boolean playEffect(final World world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        return false;
    }
    
    @Override
    public void readFromNBT(final CompoundTag cmp) {
        super.readFromNBT(cmp);
        this.rememberedTimeout = cmp.getInt("rememberedTimeout");
    }
    
    @Override
    public void writeToNBT(final CompoundTag cmp) {
        super.writeToNBT(cmp);
        cmp.putInt("rememberedTimeout", this.rememberedTimeout);
    }
    
    static {
        CEffectLucerna.FLAG = ConstellationEffect.makeAffectionFlag("lucerna");
        CEffectLucerna.CONFIG = new LucernaConfig();
    }
    
    private static class LucernaConfig extends Config
    {
        public LucernaConfig() {
            super("lucerna", 32.0, 64.0);
        }
    }
}
