package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraft.world.entity.Pose;
import java.util.List;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.phys.AABB;
import hellfirepvp.astralsorcery.common.util.collision.CustomCollisionHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectAevitas extends MantleEffect
{
    public static AevitasConfig CONFIG;
    
    public MantleEffectAevitas() {
        super(ConstellationsAS.aevitas);
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @Override
    protected void tickServer(final Player player) {
        super.tickServer(player);
        if (isStandingOnAir((Entity)player)) {
            AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, ((Double)MantleEffectAevitas.CONFIG.chargeCostPerTravelTick.get()).floatValue(), false);
        }
        final int healChance = (int)MantleEffectAevitas.CONFIG.healChance.get();
        final int foodChance = (int)MantleEffectAevitas.CONFIG.feedChance.get();
        if (healChance > 0 && MantleEffectAevitas.rand.nextInt(healChance) == 0) {
            player.heal(((Double)MantleEffectAevitas.CONFIG.healthPerCycle.get()).floatValue());
        }
        if (foodChance > 0 && MantleEffectAevitas.rand.nextInt(foodChance) == 0) {
            final FoodStats stats = player.func_71024_bL();
            if ((stats.setFoodLevel() < 20 || stats.func_75115_e() < 5.0f) && AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectAevitas.CONFIG.chargeCostPerFood.get())) {
                stats.func_75122_a(((Double)MantleEffectAevitas.CONFIG.foodPerCycle.get()).intValue(), 0.5f);
                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectAevitas.CONFIG.chargeCostPerFood.get(), false);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        this.playCapeSparkles(player, 0.1f);
        if (isStandingOnAir((Entity)player)) {
            final Vector3 center = Vector3.atEntityCorner((Entity)player).addY(0.15000000596046448);
            for (int i = 0; i < 5; ++i) {
                final Vector3 offset = Vector3.random().setY(0).normalize().multiply(MantleEffectAevitas.rand.nextFloat() * 5.0f).addY(MantleEffectAevitas.rand.nextFloat() * -0.4f).add(center);
                final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setScaleMultiplier(0.25f + MantleEffectAevitas.rand.nextFloat() * 0.15f).alpha(VFXAlphaFunction.PYRAMID).setMotion(Vector3.random().normalize().multiply(0.004f)).setMaxAge(45 + MantleEffectAevitas.rand.nextInt(20));
                if (MantleEffectAevitas.rand.nextInt(3) == 0) {
                    p.color(VFXColorFunction.WHITE);
                }
                else {
                    p.color(VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_AEVITAS));
                }
            }
        }
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectAevitas.CONFIG;
    }
    
    public static boolean canSupportEffect(final Player player) {
        final LogicalSide side = player.level() ? LogicalSide.CLIENT : LogicalSide.SERVER;
        final PlayerProgress progress = ResearchHelper.getProgress(player, side);
        return progress.doPerkAbilities() && progress.hasConstellationDiscovered(ConstellationsAS.aevitas) && AlignmentChargeHandler.INSTANCE.hasCharge(player, side, ((Double)MantleEffectAevitas.CONFIG.chargeCostPerTravelTick.get()).floatValue());
    }
    
    public static boolean isStandingOnAir(final Entity entity) {
        if (entity.func_233570_aj_()) {
            final Level world = entity.level();
            final BlockPos at = entity.func_233580_cy_().renderItem();
            return world.getBlockState(at).isAir((IBlockReader)world, at);
        }
        return false;
    }
    
    static {
        MantleEffectAevitas.CONFIG = new AevitasConfig();
    }
    
    public static class AevitasConfig extends Config
    {
        private final int defaultHealChance = 80;
        private final int defaultFeedChance = 80;
        private final double defaultHealthPerCycle = 0.5;
        private final double defaultFoodPerCycle = 1.0;
        private final double defaultChargeCostPerTravelTick = 2.5;
        private final int defaultChargeCostPerHeal = 100;
        private final int defaultChargeCostPerFood = 100;
        public ForgeConfigSpec.IntValue healChance;
        public ForgeConfigSpec.IntValue feedChance;
        public ForgeConfigSpec.DoubleValue healthPerCycle;
        public ForgeConfigSpec.DoubleValue foodPerCycle;
        public ForgeConfigSpec.DoubleValue chargeCostPerTravelTick;
        public ForgeConfigSpec.IntValue chargeCostPerHeal;
        public ForgeConfigSpec.IntValue chargeCostPerFood;
        
        public AevitasConfig() {
            super("aevitas");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set the chance of '1 in <this value>' per tick to do 1 heal cycle. Amount healed per cycle is determined by 'healthPerCycle' config option. Set to 0 to disable.").translation(this.translationKey("healChance"));
            final String s = "healChance";
            this.getClass();
            this.healChance = translation.defineInRange(s, 80, 0, Integer.MAX_VALUE);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Set the chance of '1 in <this value>' per tick to do 1 food cycle. Amount fed per cycle is determined by 'foodPerCycle' config option. Set to 0 to disable.").translation(this.translationKey("feedChance"));
            final String s2 = "feedChance";
            this.getClass();
            this.feedChance = translation2.defineInRange(s2, 80, 0, Integer.MAX_VALUE);
            final ForgeConfigSpec.Builder translation3 = cfgBuilder.comment("Set the amount of health recovered by health cycle.").translation(this.translationKey("healthPerCycle"));
            final String s3 = "healthPerCycle";
            this.getClass();
            this.healthPerCycle = translation3.defineInRange(s3, 0.5, 0.0, 100.0);
            final ForgeConfigSpec.Builder translation4 = cfgBuilder.comment("Set the amount of food recovered by food cycle.").translation(this.translationKey("foodPerCycle"));
            final String s4 = "foodPerCycle";
            this.getClass();
            this.foodPerCycle = translation4.defineInRange(s4, 1.0, 0.0, 100.0);
            final ForgeConfigSpec.Builder translation5 = cfgBuilder.comment("Set the amount alignment charge consumed per tick when walking/standing in the air").translation(this.translationKey("chargeCostPerTravelTick"));
            final String s5 = "chargeCostPerTravelTick";
            this.getClass();
            this.chargeCostPerTravelTick = translation5.defineInRange(s5, 2.5, 0.0, 100.0);
            final ForgeConfigSpec.Builder translation6 = cfgBuilder.comment("Set the amount alignment charge consumed per feed-cycle").translation(this.translationKey("chargeCostPerFood"));
            final String s6 = "chargeCostPerFood";
            this.getClass();
            this.chargeCostPerFood = translation6.defineInRange(s6, 100, 0, 1000);
            final ForgeConfigSpec.Builder translation7 = cfgBuilder.comment("Set the amount alignment charge consumed per heal-cycle").translation(this.translationKey("chargeCostPerHeal"));
            final String s7 = "chargeCostPerHeal";
            this.getClass();
            this.chargeCostPerHeal = translation7.defineInRange(s7, 100, 0, 1000);
        }
    }
    
    public static class PlayerWalkableAir implements CustomCollisionHandler
    {
        private static final AABB FULL_BOX;
        
        @Override
        public boolean shouldAddCollisionFor(final Entity entity) {
            return entity instanceof Player && !((Player)entity).field_71075_bZ.field_75100_b && ItemMantle.getEffect((LivingEntity)entity, ConstellationsAS.aevitas) != null && MantleEffectAevitas.canSupportEffect((Player)entity);
        }
        
        @Override
        public void addCollision(final Entity entity, final AABB testBox, final List<AABB> additionalCollision) {
            int yOffset = 1;
            if (entity.func_213283_Z() == Pose.CROUCHING && MantleEffectAevitas.isStandingOnAir(entity)) {
                yOffset = 2;
            }
            additionalCollision.add(PlayerWalkableAir.FULL_BOX.func_72317_d(entity.getX(), Math.floor(entity.getY()) - yOffset, entity.getZ()));
        }
        
        static {
            FULL_BOX = new AABB(BlockPos.field_177992_a);
        }
    }
}
