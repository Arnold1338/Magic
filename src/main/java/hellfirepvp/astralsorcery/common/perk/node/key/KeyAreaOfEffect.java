package hellfirepvp.astralsorcery.common.perk.node.key;

import java.util.Iterator;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.SweepingEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.projectile.TridentEntity;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.IndirectDamageSource;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.resources.ResourceLocation;

public class KeyAreaOfEffect extends KeyAddEnchantment
{
    public KeyAreaOfEffect(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.addEnchantment(Enchantments.field_191530_r, 2);
    }
    
    @Override
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.HIGH, (Consumer)this::onDamage);
    }
    
    private void onDamage(final LivingHurtEvent event) {
        if (EventFlags.SWEEP_ATTACK.isSet()) {
            return;
        }
        final DamageSource source = event.getSource();
        if (source instanceof IndirectDamageSource && source.getDirectEntity() != null && source.getDirectEntity() instanceof Player) {
            final Player player = (Player)source.getDirectEntity();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                final LivingEntity attacked = event.getEntityLiving();
                final Entity indirectSource = source.getDirectEntity();
                float sweepingPercentage;
                if (indirectSource instanceof TridentEntity) {
                    final ItemStack tridentStack = ((TridentEntity)indirectSource).field_203054_h;
                    final int sweepLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.field_191530_r, tridentStack);
                    sweepingPercentage = ((sweepLevel > 0) ? SweepingEnchantment.func_191526_e(sweepLevel) : 0.0f);
                }
                else {
                    sweepingPercentage = EnchantmentHelper.func_191527_a((LivingEntity)player);
                }
                if (sweepingPercentage > 0.0f) {
                    sweepingPercentage = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, sweepingPercentage);
                    final float toApply = event.getAmount() * sweepingPercentage;
                    final float range = 2.5f * PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
                    EventFlags.SWEEP_ATTACK.executeWithFlag(() -> {
                        attacked.level().func_217357_a((Class)LivingEntity.class, attacked.func_174813_aQ().func_72314_b((double)range, (double)(range / 2.0f), (double)range)).iterator();
                        final Iterator iterator;
                        while (iterator.hasNext()) {
                            final LivingEntity target = iterator.next();
                            if (MiscUtils.canPlayerAttackServer((LivingEntity)player, target) && !player.equals((Object)target)) {
                                DamageUtil.attackEntityFrom((Entity)target, source, toApply);
                            }
                        }
                    });
                }
            }
        }
    }
}
