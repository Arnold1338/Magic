package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.world.effect.EffectType;
import net.minecraft.world.level.effect.MobEffectInstance;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyCleanseBadPotions extends KeyPerk
{
    public KeyCleanseBadPotions(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOW, (Consumer)this::onHeal);
    }
    
    private void onHeal(final LivingHealEvent event) {
        final LivingEntity entity = event.getEntityLiving();
        if (entity instanceof Player && !entity.level()) {
            final Player player = (Player)entity;
            final List<MobEffectInstance> badEffects = player.func_70651_bq().stream().filter(p -> p.func_188419_a().func_220303_e() == EffectType.HARMFUL).collect((Collector<? super Object, ?, List<MobEffectInstance>>)Collectors.toList());
            if (badEffects.isEmpty()) {
                return;
            }
            final MobEffectInstance effect = badEffects.get(KeyCleanseBadPotions.rand.nextInt(badEffects.size()));
            final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
            if (prog.getPerkData().hasPerkEffect(this)) {
                float inclChance = 0.1f;
                inclChance = PerkAttributeHelper.getOrCreateMap(player, LogicalSide.SERVER).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, inclChance);
                final float chance = this.getChance(event.getAmount()) * inclChance;
                if (KeyCleanseBadPotions.rand.nextFloat() < chance) {
                    player.func_195063_d(effect.func_188419_a());
                }
            }
        }
    }
    
    private float getChance(final float healed) {
        if (healed <= 0.0f) {
            return 0.0f;
        }
        final float chance = (3.0f / (healed * -0.6666667f) + 5.0f) / 5.0f;
        return Mth.canEnchant(chance, 0.0f, 1.0f);
    }
}
