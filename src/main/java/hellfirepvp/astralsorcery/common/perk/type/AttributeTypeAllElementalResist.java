package hellfirepvp.astralsorcery.common.perk.type;

import java.util.Locale;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeAllElementalResist extends PerkAttributeType
{
    public AttributeTypeAllElementalResist() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_ALL_ELEMENTAL_RESIST, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onDamageTaken);
    }
    
    private void onDamageTaken(final LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntityLiving();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {
            return;
        }
        final DamageSource ds = event.getSource();
        if (this.isMaybeElementalDamage(ds)) {
            float multiplier = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 1.0f);
            --multiplier;
            multiplier = AttributeEvent.postProcessModded(player, this, multiplier);
            multiplier = 1.0f - Mth.func_76131_a(multiplier, 0.0f, 1.0f);
            event.setAmount(event.getAmount() * multiplier);
        }
    }
    
    private boolean isMaybeElementalDamage(final DamageSource source) {
        if (source.func_76347_k() || source.func_82725_o()) {
            return true;
        }
        String key = source.func_76355_l();
        if (key == null) {
            return false;
        }
        key = key.toLowerCase(Locale.ROOT);
        return key.contains("fire") || key.contains("heat") || key.contains("lightning") || key.contains("cold") || key.contains("freez") || key.contains("discharg") || key.contains("electr") || key.contains("froze") || key.contains("ice");
    }
}
