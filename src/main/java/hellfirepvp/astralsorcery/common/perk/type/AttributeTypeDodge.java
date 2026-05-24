package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierDodge;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeDodge extends PerkAttributeType
{
    public static final Config CONFIG;
    
    public AttributeTypeDodge() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_DODGE);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener(EventPriority.HIGH, (Consumer)this::onDamageTaken);
    }
    
    @Nonnull
    @Override
    public PerkAttributeModifier createModifier(final float modifier, final ModifierType mode) {
        return new AttributeModifierDodge(this, mode, modifier);
    }
    
    private void onDamageTaken(final LivingDamageEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntityLiving();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {
            return;
        }
        float chance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 0.0f);
        chance /= 100.0f;
        chance = AttributeEvent.postProcessModded(player, this, chance);
        if (chance >= AttributeTypeDodge.rand.nextFloat() && AlignmentChargeHandler.INSTANCE.drainCharge(player, side, (float)(int)AttributeTypeDodge.CONFIG.chargeCost.get(), false)) {
            event.setCanceled(true);
        }
    }
    
    static {
        CONFIG = new Config("type." + PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_DODGE.func_110623_a());
    }
    
    private static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.IntValue chargeCost;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per dodged damage.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 80, 1, 500);
        }
    }
}
