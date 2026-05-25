package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperTemporaryFlight;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectStatus;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;

public class CEffectVicio extends ConstellationEffect implements ConstellationEffectStatus
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static VicioConfig CONFIG;
    
    public CEffectVicio(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.vicio);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final Level world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        if (CEffectVicio.rand.nextInt(3) == 0) {
            final Vector3 r = new Vector3(pos.getX() + CEffectVicio.rand.nextFloat() * 4.0f * (CEffectVicio.rand.nextBoolean() ? 1 : -1) + 0.5, pos.getY() + CEffectVicio.rand.nextFloat() * 2.0f + 0.5, pos.getZ() + CEffectVicio.rand.nextFloat() * 4.0f * (CEffectVicio.rand.nextBoolean() ? 1 : -1) + 0.5);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(r).setMotion(Vector3.random().setY(0).multiply(0.03f)).setScaleMultiplier(0.45f).color(VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_VICIO)).setGravityStrength(-0.002f).setMaxAge(40);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(r).setMotion(new Vector3(0.0f, CEffectVicio.rand.nextFloat() * 0.03f, 0.0f)).setScaleMultiplier(0.45f).color(VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_VICIO)).setGravityStrength(-0.002f).setMaxAge(40);
        }
    }
    
    @Override
    public boolean playEffect(final Level world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        return false;
    }
    
    @Override
    public boolean runStatusEffect(final Level world, final BlockPos pos, final int mirrorAmount, final ConstellationEffectProperties modified, @Nullable final IMinorConstellation possibleTraitEffect) {
        boolean foundPlayer = false;
        final double range = modified.getSize();
        if (modified.isCorrupted()) {
            final List<LivingEntity> entities = world.func_217357_a((Class)LivingEntity.class, CEffectVicio.BOX.func_186670_a(pos).func_186662_g(range));
            for (final LivingEntity entity : entities) {
                if (entity instanceof ServerPlayer) {
                    final ServerPlayer pl = (ServerPlayer)entity;
                    if (pl.field_71134_c.func_73081_b().func_77144_e()) {
                        final boolean prev = pl.field_71075_bZ.field_75101_c;
                        pl.field_71075_bZ.field_75101_c = false;
                        pl.field_71075_bZ.field_75100_b = false;
                        if (prev) {
                            pl.func_71016_p();
                        }
                    }
                    this.markPlayerAffected((Player)pl);
                }
                foundPlayer = true;
                entity.func_195064_c(new MobEffectInstance(Effects.field_76421_d, 200, 9));
                entity.func_195064_c(new MobEffectInstance(Effects.field_76419_f, 200, 9));
            }
        }
        else {
            final List<ServerPlayer> entities2 = world.func_217357_a((Class)ServerPlayer.class, CEffectVicio.BOX.func_186670_a(pos).func_186662_g(range));
            for (final ServerPlayer pl2 : entities2) {
                if (EventHelperTemporaryFlight.allowFlight((Player)pl2)) {
                    final boolean prev2 = pl2.field_71075_bZ.field_75101_c;
                    pl2.field_71075_bZ.field_75101_c = true;
                    foundPlayer = true;
                    if (!prev2) {
                        pl2.func_71016_p();
                    }
                }
                this.markPlayerAffected((Player)pl2);
            }
        }
        return foundPlayer;
    }
    
    @Override
    public Config getConfig() {
        return CEffectVicio.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectVicio.FLAG;
    }
    
    static {
        CEffectVicio.FLAG = ConstellationEffect.makeAffectionFlag("vicio");
        CEffectVicio.CONFIG = new VicioConfig();
    }
    
    private static class VicioConfig extends Config
    {
        private VicioConfig() {
            super("vicio", 24.0, 16.0);
        }
    }
}
