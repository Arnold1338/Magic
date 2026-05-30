package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.EventPriority;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectDiscidia extends MantleEffect
{
    public static DiscidiaConfig CONFIG;
    
    public MantleEffectDiscidia() {
        super(ConstellationsAS.discidia);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener((Consumer)this::onAttack);
        bus.addListener(EventPriority.HIGHEST, (Consumer)this::onHurt);
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        float effChance = 0.1f;
        if (this.getLastAttackDamage((LivingEntity)player) > 0.0f) {
            effChance = 0.2f;
        }
        this.playCapeSparkles(player, effChance);
    }
    
    private void onAttack(final LivingAttackEvent event) {
        final LivingEntity attacked = event.getEntityLiving();
        final Level world = attacked.level();
        final DamageSource source = event.getSource();
        final Entity attacker = source.getDirectEntity();
        if (world.level()) {

        }
        if (attacker instanceof Player) {
            if (attacked instanceof ServerPlayer && MiscUtils.isPlayerFakeMP((ServerPlayer)attacked)) {

            }
            final Player player = (Player)attacker;
            final MantleEffectDiscidia eff = ItemMantle.getEffect((LivingEntity)player, ConstellationsAS.discidia);
            if (eff != null) {
                EventFlags.MANTLE_DISCIDIA_ADDED.executeWithFlag(() -> {
                    final float added = this.getLastAttackDamage((LivingEntity)player);
                    if (added > 0.1f && AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectDiscidia.CONFIG.chargeCostPerAttack.get())) {
                        DamageUtil.shotgunAttack(attacked, entity -> DamageUtil.attackEntityFrom((Entity)entity, CommonProxy.DAMAGE_SOURCE_STELLAR, added / 2.0f));
                        DamageUtil.shotgunAttack(attacked, entity -> DamageUtil.attackEntityFrom((Entity)entity, DamageSource.func_76365_a(player), added / 2.0f, (Entity)player));
                        AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectDiscidia.CONFIG.chargeCostPerAttack.get(), false);
                    }
                });
            }
        }
    }
    
    private void onHurt(final LivingHurtEvent event) {
        final Level world = event.getEntity().level();
        final LivingEntity hurt = event.getEntityLiving();
        if (world.level()) {

        }
        final MantleEffectDiscidia armara = ItemMantle.getEffect(hurt, ConstellationsAS.discidia);
        if (armara != null) {
            this.writeLastAttackDamage(hurt, event.getAmount());
        }
    }
    
    public void writeLastAttackDamage(final LivingEntity entity, final float dmgIn) {
        this.getData(entity).func_74776_a("lastAttack", dmgIn);
    }
    
    public float getLastAttackDamage(final LivingEntity entity) {
        return this.getData(entity).getFloat("lastAttack") * ((Double)MantleEffectDiscidia.CONFIG.damageMultiplier.get()).floatValue();
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectDiscidia.CONFIG;
    }
    
    static {
        MantleEffectDiscidia.CONFIG = new DiscidiaConfig();
    }
    
    public static class DiscidiaConfig extends Config
    {
        private final double defaultDamageMultiplier = 1.5;
        private final int defaultChargeCostPerAttack = 100;
        public ForgeConfigSpec.DoubleValue damageMultiplier;
        public ForgeConfigSpec.IntValue chargeCostPerAttack;
        
        public DiscidiaConfig() {
            super("discidia");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Sets the multiplier for how much of the received damage is converted into additional damage.").translation(this.translationKey("damageMultiplier"));
            final String s = "damageMultiplier";
            this.getClass();
            this.damageMultiplier = translation.defineInRange(s, 1.5, 0.0, 100.0);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Set the amount alignment charge consumed per attack enhanced by the mantle").translation(this.translationKey("chargeCostPerAttack"));
            final String s2 = "chargeCostPerAttack";
            this.getClass();
            this.chargeCostPerAttack = translation2.defineInRange(s2, 100, 0, 1000);
        }
    }
}
