package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.util.time.TimeStopController;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.time.TimeStopZone;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.entity.player.Player;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectHorologium extends MantleEffect
{
    public static HorologiumConfig CONFIG;
    
    public MantleEffectHorologium() {
        super(ConstellationsAS.horologium);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener((Consumer)this::onHurt);
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        if (!player.func_184811_cZ().func_185141_a((Item)ItemsAS.MANTLE)) {
            this.playCapeSparkles(player, 0.4f);
        }
        else {
            this.playCapeSparkles(player, 0.2f);
        }
    }
    
    private void onHurt(final LivingHurtEvent event) {
        if (ItemMantle.getEffect(event.getEntityLiving(), ConstellationsAS.horologium) != null && event.getEntityLiving() instanceof Player && !event.getEntityLiving().func_130014_f_().func_201670_d() && !event.getSource().func_76347_k()) {
            final Player player = (Player)event.getEntityLiving();
            if (!player.func_184811_cZ().func_185141_a((Item)ItemsAS.MANTLE) && AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectHorologium.CONFIG.chargeCostPerFreeze.get())) {
                TimeStopController.freezeWorldAt(TimeStopZone.EntityTargetController.allExcept((Entity)player), player.func_130014_f_(), player.func_233580_cy_(), ((Double)MantleEffectHorologium.CONFIG.effectRange.get()).floatValue(), (int)MantleEffectHorologium.CONFIG.effectDuration.get());
                player.func_184811_cZ().func_185145_a((Item)ItemsAS.MANTLE, (int)MantleEffectHorologium.CONFIG.cooldown.get());
                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectHorologium.CONFIG.chargeCostPerFreeze.get(), false);
            }
        }
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectHorologium.CONFIG;
    }
    
    static {
        MantleEffectHorologium.CONFIG = new HorologiumConfig();
    }
    
    public static class HorologiumConfig extends Config
    {
        private final double defaultEffectRange = 20.0;
        private final int defaultEffectDuration = 180;
        private final int defaultCooldown = 1000;
        private final int defaultChargeCostPerFreeze = 400;
        public ForgeConfigSpec.DoubleValue effectRange;
        public ForgeConfigSpec.IntValue effectDuration;
        public ForgeConfigSpec.IntValue cooldown;
        public ForgeConfigSpec.IntValue chargeCostPerFreeze;
        
        public HorologiumConfig() {
            super("horologium");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the range of the time-freeze bubble.").translation(this.translationKey("effectRange"));
            final String s = "effectRange";
            this.getClass();
            this.effectRange = translation.defineInRange(s, 20.0, 4.0, 64.0);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Defines the duration of the time-freeze bubble.").translation(this.translationKey("effectDuration"));
            final String s2 = "effectDuration";
            this.getClass();
            this.effectDuration = translation2.defineInRange(s2, 180, 40, 1000);
            final ForgeConfigSpec.Builder translation3 = cfgBuilder.comment("Defines the cooldown for the time-freeze effect after it triggered (should be longer than duration maybe)").translation(this.translationKey("cooldown"));
            final String s3 = "cooldown";
            this.getClass();
            this.cooldown = translation3.defineInRange(s3, 1000, 40, 20000);
            final ForgeConfigSpec.Builder translation4 = cfgBuilder.comment("Set the amount alignment charge consumed per created time stop zone").translation(this.translationKey("chargeCostPerFreeze"));
            final String s4 = "chargeCostPerFreeze";
            this.getClass();
            this.chargeCostPerFreeze = translation4.defineInRange(s4, 400, 0, 1000);
        }
    }
}
