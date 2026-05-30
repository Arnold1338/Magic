package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Mob;
import hellfirepvp.astralsorcery.common.util.DamageSourceUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.data.config.registry.TechnicalEntityRegistry;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ConstellationEffectEntityCollect;

public class CEffectDiscidia extends ConstellationEffectEntityCollect<LivingEntity>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static DiscidiaConfig CONFIG;
    
    public CEffectDiscidia(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.discidia, LivingEntity.class, entity -> entity.isAlive() && TechnicalEntityRegistry.INSTANCE.canAffect((Entity)entity));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final Level world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        final Vector3 playAt = new Vector3((Vec3i)pos).add(0.5, 0.5, 0.5);
        if (pos.equals((Object)pedestal.getBlockState())) {
            playAt.add(CEffectDiscidia.rand.nextFloat() * 0.1 * (CEffectDiscidia.rand.nextBoolean() ? 1 : -1), CEffectDiscidia.rand.nextFloat() * 5.0f, CEffectDiscidia.rand.nextFloat() * 0.1 * (CEffectDiscidia.rand.nextBoolean() ? 1 : -1));
        }
        final Vector3 motion = Vector3.random().setY(0).multiply(0.05);
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(playAt).alpha(VFXAlphaFunction.FADE_OUT).motion(VFXMotionController.decelerate(() -> motion)).color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_DISCIDIA)).setScaleMultiplier(0.4f).setMaxAge(35);
    }
    
    @Override
    public boolean playEffect(final Level world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        boolean didEffect = false;
        final float damage = ((Double)CEffectDiscidia.CONFIG.damage.get()).floatValue();
        final Player owner = this.getOwningPlayerInWorld(world, pos);
        final DamageSource src = (owner == null) ? CommonProxy.DAMAGE_SOURCE_STELLAR : DamageSourceUtil.withEntityDirect(CommonProxy.DAMAGE_SOURCE_STELLAR, (Entity)owner);
        final List<LivingEntity> entities = this.collectEntities(world, pos, properties);
        for (final LivingEntity entity : entities) {
            if (CEffectDiscidia.rand.nextInt(6) != 0) {

            }
            if (properties.isCorrupted() && entity instanceof MobEntity && entity.getClassification(false) == MobCategory.MONSTER) {
                entity.heal(damage);
                entity.func_195064_c(new MobEffectInstance(Effects.field_76429_m, 30, 1));
            }
            else {
                if (entity instanceof Player) {

                }
                if (entity.equals((Object)owner)) {

                }
                DamageUtil.shotgunAttack(entity, e -> DamageUtil.attackEntityFrom((Entity)entity, src, damage));
            }
            if (entity instanceof Player) {
                this.markPlayerAffected((Player)entity);
            }
            didEffect = true;
        }
        return didEffect;
    }
    
    @Override
    public Config getConfig() {
        return CEffectDiscidia.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectDiscidia.FLAG;
    }
    
    static {
        CEffectDiscidia.FLAG = ConstellationEffect.makeAffectionFlag("discidia");
        CEffectDiscidia.CONFIG = new DiscidiaConfig();
    }
    
    private static class DiscidiaConfig extends Config
    {
        private final double defaultDamage = 3.0;
        public ForgeConfigSpec.DoubleValue damage;
        
        public DiscidiaConfig() {
            super("discidia", 10.0, 2.0);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the max. possible damage dealt per damage tick.").translation(this.translationKey("damage"));
            final String s = "damage";
            this.getClass();
            this.damage = translation.defineInRange(s, 3.0, 0.1, 128.0);
        }
    }
}
