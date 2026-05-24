package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import net.minecraft.world.effect.MobEffect;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperSpawnDeny;
import hellfirepvp.astralsorcery.common.util.tick.TickTokenMap;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.entity.projectile.Projectile;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedCrystalBase;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.source.orbital.FXOrbitalArmara;
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

public class CEffectArmara extends ConstellationEffectEntityCollect<LivingEntity>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static ArmaraConfig CONFIG;
    private int rememberedTimeout;
    
    public CEffectArmara(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.armara, LivingEntity.class, e -> e.isAlive() && TechnicalEntityRegistry.INSTANCE.canAffect((Entity)e));
        this.rememberedTimeout = 0;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final World world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        if (pedestal.getTicksExisted() % 20 == 0) {
            EffectHelper.spawnSource(new FXOrbitalArmara(new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5)).setOrbitRadius(0.8 + CEffectArmara.rand.nextFloat() * 0.7).setOrbitAxis(Vector3.RotAxis.Y_AXIS).setTicksPerRotation(20 + CEffectArmara.rand.nextInt(20)));
        }
        final ConstellationEffectProperties prop = this.createProperties(pedestal.getMirrorCount());
        final ItemStack socket = pedestal.getCurrentCrystal();
        if (!socket.isEmpty() && socket.getItem() instanceof ItemAttunedCrystalBase) {
            final IMinorConstellation trait = ((ItemAttunedCrystalBase)socket.getItem()).getTraitConstellation(socket);
            if (trait != null) {
                trait.affectConstellationEffect(prop);
                if (prop.isCorrupted()) {
                    return;
                }
            }
        }
        final List<Entity> projectiles = world.func_217357_a((Class)Entity.class, CEffectArmara.BOX.func_186670_a(pos).func_186662_g(prop.getSize()));
        if (!projectiles.isEmpty()) {
            for (final Entity e : projectiles) {
                if (e.isAlive() && TechnicalEntityRegistry.INSTANCE.canAffect(e)) {
                    if (e instanceof ProjectileEntity) {
                        final double xRatio = pos.getX() + 0.5 - e.func_226277_ct_();
                        final double zRatio = pos.getZ() + 0.5 - e.func_226281_cx_();
                        final float f = MathHelper.func_76133_a(xRatio * xRatio + zRatio * zRatio);
                        final Vector3 motion = new Vector3(e.func_213322_ci());
                        motion.multiply(new Vector3(0.5, 1.0, 0.5));
                        motion.subtract(xRatio / f * 0.4, 0.0, zRatio / f * 0.4);
                        ((ProjectileEntity)e).func_70186_c(motion.getX(), motion.getY(), motion.getZ(), 1.5f, 0.0f);
                    }
                    else {
                        if (!(e instanceof MobEntity)) {
                            continue;
                        }
                        ((LivingEntity)e).func_233627_a_(0.4f, pos.getX() + 0.5 - e.func_226277_ct_(), pos.getZ() + 0.5 - e.func_226281_cx_());
                    }
                }
            }
        }
    }
    
    @Override
    public boolean playEffect(final World world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        final int toAdd = 2 + CEffectArmara.rand.nextInt(5);
        final WorldBlockPos at = WorldBlockPos.wrapServer(world, pos);
        final TickTokenMap.SimpleTickToken<Double> token = EventHelperSpawnDeny.spawnDenyRegions.get(at);
        if (token != null) {
            int next = token.getRemainingTimeout() + toAdd;
            if (next > 400) {
                next = 400;
            }
            token.setTimeout(next);
            this.rememberedTimeout = next;
        }
        else {
            this.rememberedTimeout = Math.min(400, this.rememberedTimeout + toAdd);
            EventHelperSpawnDeny.spawnDenyRegions.put(at, new TickTokenMap.SimpleTickToken<Double>(properties.getSize(), this.rememberedTimeout));
        }
        if (!properties.isCorrupted()) {
            final List<Entity> projectiles = world.func_217357_a((Class)Entity.class, CEffectArmara.BOX.func_186670_a(pos).func_186662_g(properties.getSize()));
            if (!projectiles.isEmpty()) {
                for (final Entity e : projectiles) {
                    if (e.isAlive() && TechnicalEntityRegistry.INSTANCE.canAffect(e)) {
                        if (e instanceof ProjectileEntity) {
                            final double xRatio = pos.getX() + 0.5 - e.func_226277_ct_();
                            final double zRatio = pos.getZ() + 0.5 - e.func_226281_cx_();
                            final float f = MathHelper.func_76133_a(xRatio * xRatio + zRatio * zRatio);
                            final Vector3 motion = new Vector3(e.func_213322_ci());
                            motion.multiply(new Vector3(0.5, 1.0, 0.5));
                            motion.subtract(xRatio / f * 0.4, 0.0, zRatio / f * 0.4);
                            ((ProjectileEntity)e).func_70186_c(motion.getX(), motion.getY(), motion.getZ(), 1.5f, 0.0f);
                        }
                        else {
                            if (!(e instanceof MobEntity)) {
                                continue;
                            }
                            ((LivingEntity)e).func_233627_a_(0.4f, pos.getX() + 0.5 - e.func_226277_ct_(), pos.getZ() + 0.5 - e.func_226281_cx_());
                        }
                    }
                }
            }
        }
        final int potionAmplifier = (int)CEffectArmara.CONFIG.potionAmplifier.get();
        final List<LivingEntity> entities = this.collectEntities(world, pos, properties);
        for (final LivingEntity entity : entities) {
            if (entity.isAlive() && (entity instanceof MobEntity || entity instanceof Player)) {
                if (properties.isCorrupted()) {
                    if (entity instanceof Player) {
                        continue;
                    }
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76424_c, 100, potionAmplifier + 4));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76428_l, 100, potionAmplifier + 4));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76429_m, 100, potionAmplifier + 2));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76420_g, 100, potionAmplifier + 4));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76427_o, 100, potionAmplifier + 4));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76422_e, 100, potionAmplifier + 4));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance((Effect)EffectsAS.EFFECT_DROP_MODIFIER, 100, 5));
                }
                else {
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76429_m, 30, Math.min(potionAmplifier, 3), true, true));
                    if (entity instanceof Player) {
                        EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76444_x, 30, potionAmplifier, true, false));
                    }
                }
                if (!(entity instanceof Player)) {
                    continue;
                }
                this.markPlayerAffected((Player)entity);
            }
        }
        return true;
    }
    
    @Override
    public Config getConfig() {
        return CEffectArmara.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectArmara.FLAG;
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
        CEffectArmara.FLAG = ConstellationEffect.makeAffectionFlag("armara");
        CEffectArmara.CONFIG = new ArmaraConfig();
    }
    
    private static class ArmaraConfig extends Config
    {
        private final int defaultPotionAmplifier = 1;
        public ForgeConfigSpec.IntValue potionAmplifier;
        
        public ArmaraConfig() {
            super("armara", 16.0, 2.0);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set the amplifier for the potion effects this ritual provides.").translation(this.translationKey("potionAmplifier"));
            final String s = "potionAmplifier";
            this.getClass();
            this.potionAmplifier = translation.defineInRange(s, 1, 0, 10);
        }
    }
}
