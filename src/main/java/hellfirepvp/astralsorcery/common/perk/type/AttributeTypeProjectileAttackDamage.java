package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeProjectileAttackDamage extends PerkAttributeType
{
    public AttributeTypeProjectileAttackDamage() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_PROJ_DAMAGE, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener(EventPriority.LOW, (Consumer)this::onProjectileDamage);
    }
    
    private void onProjectileDamage(final LivingHurtEvent event) {
        if (event.getSource().func_76352_a()) {
            final DamageSource source = event.getSource();
            if (source.func_76346_g() != null && source.func_76346_g() instanceof Player) {
                final Player player = (Player)source.func_76346_g();
                final LogicalSide side = this.getSide((Entity)player);
                if (!this.hasTypeApplied(player, side)) {
                    return;
                }
                float amt = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, event.getAmount());
                amt = AttributeEvent.postProcessModded(player, this, amt);
                event.setAmount(amt);
            }
        }
    }
}
