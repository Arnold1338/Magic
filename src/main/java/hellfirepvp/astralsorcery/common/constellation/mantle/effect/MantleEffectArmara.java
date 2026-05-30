package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectArmara extends MantleEffect
{
    public static ArmaraConfig CONFIG;
    
    public MantleEffectArmara() {
        super(ConstellationsAS.armara);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener(EventPriority.HIGH, (Consumer)this::onHurt);
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @Override
    protected void tickServer(final Player player) {
        super.tickServer(player);
        if (this.getCurrentImmunityStacks((LivingEntity)player) >= (int)MantleEffectArmara.CONFIG.immunityStacks.get()) {
            this.setCurrentImmunityRechargeTick((LivingEntity)player, (int)MantleEffectArmara.CONFIG.immunityRechargeTicks.get());

        }
        int tick = this.getCurrentImmunityRechargeTick((LivingEntity)player);
        if (--tick <= 0) {
            if (AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectArmara.CONFIG.chargeCostPerStack.get())) {
                this.setCurrentImmunityRechargeTick((LivingEntity)player, (int)MantleEffectArmara.CONFIG.immunityRechargeTicks.get());
                this.setCurrentImmunityStacks((LivingEntity)player, this.getCurrentImmunityStacks((LivingEntity)player) + 1);
                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectArmara.CONFIG.chargeCostPerStack.get(), false);
            }
        }
        else {
            this.setCurrentImmunityRechargeTick((LivingEntity)player, tick);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        this.playCapeSparkles(player, 0.15f);
        final Vector3 at = Vector3.atEntityCorner((Entity)player);
        at.addY(player.func_213302_cg() / 3.0f * 2.0f);
        final Vector3 lookVec = new Vector3(player.func_70040_Z()).normalize();
        final int stacks = this.getCurrentImmunityStacks((LivingEntity)player);
        if (stacks > 0) {
            final Random sRand = new Random(player.getUUID().hashCode());
            for (int i = 0; i < stacks; ++i) {
                final Vector3 axis = Vector3.random(sRand);
                axis.setX(axis.getX() * 0.3499999940395355);
                axis.setZ(axis.getZ() * 0.3499999940395355);
                final Vector3 perpEffect = axis.clone().perpendicular();
                final float scale = MantleEffectArmara.rand.nextFloat() * 0.2f + 0.2f;
                final int ticksPerCircle = 80 + sRand.nextInt(50);
                final int tick = player.field_70173_aa % ticksPerCircle;
                final Vector3 anglePlayer = perpEffect.normalize().rotate(Math.toRadians(360.0f * (tick / (float)ticksPerCircle)), axis).normalize();
                final Vector3 pos = anglePlayer.clone().multiply(sRand.nextFloat() * 0.4f + 0.9f).add(at);
                float alpha = 0.8f;
                if (Minecraft.getInstance().options.func_243230_g().func_243192_a()) {
                    final float deg = (float)Math.toDegrees(lookVec.angle(anglePlayer));
                    if (deg < 70.0f) {
                        final float tansparentDegree = 40.0f;
                        alpha *= Mth.canEnchant((deg - tansparentDegree) / (80.0f - tansparentDegree), 0.0f, 1.0f);
                    }
                }
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.FADE_OUT).setAlphaMultiplier(alpha).color(VFXColorFunction.constant(ColorsAS.MANTLE_ARMARA_STACKS)).setScaleMultiplier(scale).setMaxAge(20 + MantleEffectArmara.rand.nextInt(20));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.FADE_OUT).setAlphaMultiplier(alpha).color(VFXColorFunction.WHITE).setScaleMultiplier(scale * 0.4f).setMaxAge(10 + MantleEffectArmara.rand.nextInt(10));
            }
        }
    }
    
    private void onHurt(final LivingHurtEvent event) {
        final Level world = event.getEntity().level();
        final LivingEntity hurt = event.getEntityLiving();
        if (world.level()) {

        }
        final MantleEffectArmara armara = ItemMantle.getEffect(hurt, ConstellationsAS.armara);
        if (armara != null && this.shouldPreventDamage(hurt, event.getSource(), false)) {
            event.setCanceled(true);
        }
    }
    
    private boolean shouldPreventDamage(final LivingEntity hurt, final DamageSource source, final boolean simulate) {
        if (source.func_76357_e()) {
            return false;
        }
        int stacks = this.getCurrentImmunityStacks(hurt);
        if (stacks <= 0) {
            return false;
        }
        if (!simulate) {
            --stacks;
            this.setCurrentImmunityStacks(hurt, stacks);
        }
        return true;
    }
    
    private int getCurrentImmunityRechargeTick(final LivingEntity entity) {
        return this.getData(entity).getInt("ITick");
    }
    
    private void setCurrentImmunityRechargeTick(final LivingEntity entity, final int tick) {
        this.getData(entity).putInt("ITick", tick);
    }
    
    private int getCurrentImmunityStacks(final LivingEntity entity) {
        return this.getData(entity).getInt("IStacks");
    }
    
    private void setCurrentImmunityStacks(final LivingEntity entity, final int stacks) {
        this.getData(entity).putInt("IStacks", stacks);
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectArmara.CONFIG;
    }
    
    static {
        MantleEffectArmara.CONFIG = new ArmaraConfig();
    }
    
    public static class ArmaraConfig extends Config
    {
        private final int defaultImmunityStacks = 3;
        private final int defaultImmunityRechargeTicks = 300;
        private final int defaultChargeCostPerStack = 750;
        public ForgeConfigSpec.IntValue immunityStacks;
        public ForgeConfigSpec.IntValue immunityRechargeTicks;
        public ForgeConfigSpec.IntValue chargeCostPerStack;
        
        public ArmaraConfig() {
            super("armara");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set the max amount of immunity stacks.").translation(this.translationKey("immunityStacks"));
            final String s = "immunityStacks";
            this.getClass();
            this.immunityStacks = translation.defineInRange(s, 3, 0, 10);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Sets the amount of ticks between immunity stack recharges.").translation(this.translationKey("immunityRechargeTicks"));
            final String s2 = "immunityRechargeTicks";
            this.getClass();
            this.immunityRechargeTicks = translation2.defineInRange(s2, 300, 20, 1000000);
            final ForgeConfigSpec.Builder translation3 = cfgBuilder.comment("Set the amount alignment charge consumed per created immunity stack").translation(this.translationKey("chargeCostPerStack"));
            final String s3 = "chargeCostPerStack";
            this.getClass();
            this.chargeCostPerStack = translation3.defineInRange(s3, 750, 0, 1000);
        }
    }
}
