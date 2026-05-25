package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.level.effect.MobEffects;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
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
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyRampage extends KeyPerk
{
    private static final float defaultRampageChance = 1.0f;
    private static final int defaultRampageDuration = 100;
    public static final Config CONFIG;
    
    public KeyRampage(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onEntityDeath);
    }
    
    private void onEntityDeath(final LivingDeathEvent event) {
        final DamageSource source = event.getSource();
        if (source.getEnchantments( != null && source.getDirectEntity() instanceof Player) {
            final Player player = (Player)source.getDirectEntity();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (side.isServer() && prog.getPerkData().hasPerkEffect(this)) {
                float ch = ((Double)KeyRampage.CONFIG.rampageChance.get()).floatValue();
                ch = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ch);
                if (KeyRampage.rand.nextFloat() < ch) {
                    int dur = (int)KeyRampage.CONFIG.rampageDuration.get();
                    dur = Math.round(PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_RAMPAGE_DURATION, (float)dur));
                    dur = Math.round(PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, (float)dur));
                    if (dur > 0) {
                        player.func_195064_c(new MobEffectInstance(Effects.field_76424_c, dur, 1, false, false, true));
                        player.func_195064_c(new MobEffectInstance(Effects.field_76422_e, dur, 1, false, false, true));
                        player.func_195064_c(new MobEffectInstance(Effects.field_76420_g, dur, 1, false, false, true));
                    }
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.rampage");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue rampageChance;
        private ForgeConfigSpec.IntValue rampageDuration;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.rampageChance = cfgBuilder.comment("Defines the chance to gain rampage buffs when killing a mob").translation(this.translationKey("rampageChance")).defineInRange("rampageChance", 1.0, 0.05000000074505806, 1.0);
            this.rampageDuration = cfgBuilder.comment("Defines the duration of the rampage in ticks").translation(this.translationKey("rampageDuration")).defineInRange("rampageDuration", 100, 10, 100000);
        }
    }
}
