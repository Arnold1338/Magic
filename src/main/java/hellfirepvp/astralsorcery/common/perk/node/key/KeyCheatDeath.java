package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.effect.MobEffect;
import net.minecraft.world.level.effect.MobEffectInstance;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.perk.PerkCooldownHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.CooldownPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyCheatDeath extends KeyPerk implements CooldownPerk
{
    private static final int defaultCooldownPotionApplication = 600;
    private static final int defaultPotionDuration = 500;
    private static final int defaultPotionAmplifier = 0;
    private static final int defaultChargeCost = 350;
    public static final Config CONFIG;
    
    public KeyCheatDeath(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.HIGHEST, (Consumer)this::onDeath);
    }
    
    private void onDeath(final LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            final Player player = (Player)event.getEntityLiving();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress progress = ResearchHelper.getProgress(player, side);
            if (side.isServer() && progress.getPerkData().hasPerkEffect(this) && !PerkCooldownHelper.isCooldownActiveForPlayer(player, this) && AlignmentChargeHandler.INSTANCE.drainCharge(player, side, (float)(int)KeyCheatDeath.CONFIG.chargeCost.get(), false)) {
                PerkCooldownHelper.setCooldownActiveForPlayer(player, this, (int)KeyCheatDeath.CONFIG.cooldownPotionApplication.get());
                player.func_195064_c(new MobEffectInstance((Effect)EffectsAS.EFFECT_CHEAT_DEATH, (int)KeyCheatDeath.CONFIG.potionDuration.get(), (int)KeyCheatDeath.CONFIG.potionAmplifier.get(), true, false, true));
            }
        }
    }
    
    @Override
    public void onCooldownTimeout(final Player player) {
    }
    
    static {
        CONFIG = new Config("key.cheat_death");
    }
    
    private static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.IntValue cooldownPotionApplication;
        private ForgeConfigSpec.IntValue potionDuration;
        private ForgeConfigSpec.IntValue potionAmplifier;
        private ForgeConfigSpec.IntValue chargeCost;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.cooldownPotionApplication = cfgBuilder.comment("Once the potion effect gets applied, it'll take at least this amount of ticks or a server restart until it can be re-applied by this perk.").translation(this.translationKey("cooldownPotionApplication")).defineInRange("cooldownPotionApplication", 600, 1, Integer.MAX_VALUE);
            this.potionDuration = cfgBuilder.comment("Once the potion effect gets applied by any of the triggers, this will be used as tick-duration of the potion effect.").translation(this.translationKey("potionDuration")).defineInRange("potionDuration", 500, 1, Integer.MAX_VALUE);
            this.potionAmplifier = cfgBuilder.comment("Once the potion effect gets applied by any of the triggers, this will be used as amplifier of the potion effect.").translation(this.translationKey("potionAmplifier")).defineInRange("potionAmplifier", 0, 0, 4);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per death-prevention.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 350, 1, 500);
        }
    }
}
