package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.level.effect.MobEffect;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyBleed extends KeyPerk
{
    private static final int defaultBleedDuration = 40;
    private static final float defaultBleedChance = 0.25f;
    public static final Config CONFIG;
    
    public KeyBleed(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener((Consumer)this::onAttack);
    }
    
    private void onAttack(final LivingHurtEvent event) {
        final DamageSource source = event.getSource();
        if (source.getDirectEntity() != null && source.getDirectEntity() instanceof Player) {
            final Player player = (Player)source.getDirectEntity();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                final LivingEntity target = event.getEntityLiving();
                double chance = (double)KeyBleed.CONFIG.bleedChance.get();
                chance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_BLEED_CHANCE, (float)chance);
                if (KeyBleed.rand.nextFloat() < chance) {
                    int stackCap = 3;
                    stackCap = Math.round(PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_BLEED_STACKS, (float)stackCap));
                    int duration = (int)KeyBleed.CONFIG.bleedDuration.get();
                    duration = Math.round(PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_BLEED_DURATION, (float)duration));
                    int setAmplifier = 0;
                    if (target.hasEffect((MobEffect)EffectsAS.EFFECT_BLEED)) {
                        final MobEffectInstance pe = target.func_70660_b((MobEffect)EffectsAS.EFFECT_BLEED);
                        if (pe != null) {
                            setAmplifier = Math.min(pe.func_76458_c() + 1, stackCap - 1);
                        }
                    }
                    target.func_195064_c(new MobEffectInstance((MobEffect)EffectsAS.EFFECT_BLEED, duration, setAmplifier, false, true));
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.bleed");
    }
    
    private static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.IntValue bleedDuration;
        private ForgeConfigSpec.DoubleValue bleedChance;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.bleedDuration = cfgBuilder.comment("Defines the duration of the bleeding effect when applied. Refreshes this duration when a it is applied again").translation(this.translationKey("bleedDuration")).defineInRange("bleedDuration", 40, 5, 400);
            this.bleedChance = cfgBuilder.comment("Defines the base chance a bleed can/is applied when an entity is being hit by this entity").translation(this.translationKey("bleedChance")).defineInRange("bleedChance", 0.25, 0.01, 1.0);
        }
    }
}
