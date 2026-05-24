package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyProjectileProximity extends KeyPerk
{
    private static final float defaultCapDistance = 100.0f;
    private static final float defaultMaxAdditionalMultiplier = 0.75f;
    public static final Config CONFIG;
    
    public KeyProjectileProximity(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.HIGH, (Consumer)this::onProjDamage);
    }
    
    private void onProjDamage(final LivingHurtEvent event) {
        if (event.getSource().func_76352_a()) {
            final DamageSource source = event.getSource();
            if (source.func_76346_g() != null && source.func_76346_g() instanceof Player) {
                final Player player = (Player)source.func_76346_g();
                final LogicalSide side = this.getSide((Entity)player);
                final PlayerProgress prog = ResearchHelper.getProgress(player, side);
                if (prog.getPerkData().hasPerkEffect(this)) {
                    float added = ((Double)KeyProjectileProximity.CONFIG.maxAdditionalMultiplier.get()).floatValue();
                    added *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
                    final float capDstSq = ((Double)KeyProjectileProximity.CONFIG.capDistance.get()).floatValue();
                    float dst = -((float)player.func_70068_e((Entity)event.getEntityLiving()) - capDstSq);
                    dst /= capDstSq;
                    if (dst < 0.0f) {
                        dst /= 10.0f;
                    }
                    dst = Math.max(0.0f, 1.0f + dst);
                    added *= dst;
                    float amt = event.getAmount();
                    amt *= Math.max(0.0f, added);
                    event.setAmount(amt);
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.proj_proximity");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue capDistance;
        private ForgeConfigSpec.DoubleValue maxAdditionalMultiplier;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.capDistance = cfgBuilder.comment("Defines the distance at which no additional damage is awarded.").translation(this.translationKey("capDistance")).defineInRange("capDistance", 100.0, 4.0, 65536.0);
            this.maxAdditionalMultiplier = cfgBuilder.comment("Defines the maximum multiplier that can be reached if the distance when hitting something with projectiles is basically nothing.").translation(this.translationKey("maxAdditionalMultiplier")).defineInRange("maxAdditionalMultiplier", 0.75, 0.05000000074505806, 5.0);
        }
    }
}
