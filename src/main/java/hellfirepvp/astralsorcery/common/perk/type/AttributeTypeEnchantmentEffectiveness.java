package hellfirepvp.astralsorcery.common.perk.type;

import java.util.Iterator;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantment;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.event.DynamicEnchantmentEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeEnchantmentEffectiveness extends PerkAttributeType
{
    public AttributeTypeEnchantmentEffectiveness() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_ENCH_EFFECT, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onModify);
    }
    
    private void onModify(final DynamicEnchantmentEvent.Modify event) {
        final Player player = event.getResolvedPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {

        }
        final float inc = PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, ResearchHelper.getProgress(player, side), this);
        for (final DynamicEnchantment ench : event.getEnchantmentsToApply()) {
            float lvl = (float)ench.getLevelAddition();
            lvl *= inc;
            final float post = AttributeEvent.postProcessModded(player, this, lvl);
            ench.setLevelAddition(Math.round(post));
        }
    }
}
