package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.effect.EffectType;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.PotionEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypePotionDuration extends PerkAttributeType
{
    public AttributeTypePotionDuration() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_POTION_DURATION, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onEffect);
    }
    
    private void onEffect(final PotionEvent.PotionAddedEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            if (event.getOldPotionEffect() == null) {
                this.modifyPotionDuration((Player)event.getEntityLiving(), event.getPotionEffect(), event.getPotionEffect());
            }
            else if (new MobEffectInstance(event.getOldPotionEffect()).func_199308_a(event.getPotionEffect())) {
                this.modifyPotionDuration((Player)event.getEntityLiving(), event.getPotionEffect(), event.getOldPotionEffect());
            }
        }
    }
    
    private void modifyPotionDuration(final Player player, final MobEffectInstance newSetEffect, final MobEffectInstance existingEffect) {
        if (player.level() || newSetEffect.func_188419_a().func_220303_e().equals((Object)EffectType.HARMFUL) || existingEffect.func_76458_c() < newSetEffect.func_76458_c()) {
            return;
        }
        float newDuration = (float)existingEffect.getAmplifier();
        newDuration = PerkAttributeHelper.getOrCreateMap(player, LogicalSide.SERVER).modifyValue(player, ResearchHelper.getProgress(player, LogicalSide.SERVER), this, newDuration);
        newDuration = AttributeEvent.postProcessModded(player, this, newDuration);
        if (newSetEffect.getAmplifier() < newDuration) {
            newSetEffect.field_76460_b = Mth.func_76141_d(newDuration);
        }
    }
}
