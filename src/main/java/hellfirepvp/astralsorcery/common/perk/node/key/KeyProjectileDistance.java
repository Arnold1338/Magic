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

public class KeyProjectileDistance extends KeyPerk
{
    private static final float defaultCapDistance = 6400.0f;
    private static final float defaultMaxAdditionalMultiplier = 0.75f;
    public static final Config CONFIG;
    
    public KeyProjectileDistance(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.HIGH, (Consumer)this::onProjDamage);
    }
    
    private void onProjDamage(final LivingHurtEvent event) {
        if (event.getSource().func_76352_a()) {
            final DamageSource source = event.getSource();
            if (source.getEnchantments( != null && source.getDirectEntity() instanceof Player) {
                final Player player = (Player)source.getDirectEntity();
                final LogicalSide side = this.getSide((Entity)player);
                final PlayerProgress prog = ResearchHelper.getProgress(player, side);
                if (prog.getPerkData().hasPerkEffect(this)) {
                    float added = ((Double)KeyProjectileDistance.CONFIG.maxAdditionalMultiplier.get()).floatValue();
                    added *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
                    final float capDstSq = ((Double)KeyProjectileDistance.CONFIG.capDistance.get()).floatValue();
                    final float mul = (float)player.func_70068_e((Entity)event.getEntityLiving()) / capDstSq;
                    added *= ((mul > 1.0f) ? 1.0f : mul);
                    float amt = event.getAmount();
                    amt *= 1.0f + added;
                    event.setAmount(amt);
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.proj_distance");
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
            this.capDistance = cfgBuilder.comment("Defines the distance that must be reached to achieve the maximum damage multiplier").translation(this.translationKey("capDistance")).defineInRange("capDistance", 6400.0, 100.0, 65536.0);
            this.maxAdditionalMultiplier = cfgBuilder.comment("Defines the maximum multiplier that can be reached if the 'capDistance' is reached or surpassed when hitting something").translation(this.translationKey("maxAdditionalMultiplier")).defineInRange("maxAdditionalMultiplier", 0.75, 0.05000000074505806, 5.0);
        }
    }
}
