package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyDamageEffects extends KeyPerk
{
    private static final float defaultApplicationChance = 0.08f;
    public static final Config CONFIG;
    
    public KeyDamageEffects(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onDamageResult);
    }
    
    private void onDamageResult(final LivingDamageEvent event) {
        final DamageSource source = event.getSource();
        if (source.getEnchantments( != null && source.getDirectEntity() instanceof Player) {
            final Player player = (Player)source.getDirectEntity();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                final LivingEntity attacked = event.getEntityLiving();
                final float chance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ((Double)KeyDamageEffects.CONFIG.applicationChance.get()).floatValue());
                if (KeyDamageEffects.rand.nextFloat() < chance) {
                    switch (KeyDamageEffects.rand.nextInt(3)) {
                        case 0: {
                            attacked.func_195064_c(new MobEffectInstance(Effects.field_82731_v, 200, 1, false, false, true));
                            break;
                        }
                        case 1: {
                            attacked.func_195064_c(new MobEffectInstance(Effects.field_76436_u, 200, 1, false, false, true));
                            break;
                        }
                        case 2: {
                            attacked.func_195064_c(new MobEffectInstance(Effects.field_76421_d, 200, 1, false, false, true));
                            attacked.func_195064_c(new MobEffectInstance(Effects.field_76437_t, 200, 1, false, false, true));
                            break;
                        }
                    }
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.damage_effects");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue applicationChance;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.applicationChance = cfgBuilder.comment("Defines the chance per hit to apply additional effects.").translation(this.translationKey("applicationChance")).defineInRange("applicationChance", 0.07999999821186066, 0.009999999776482582, 0.20000000298023224);
        }
    }
}
