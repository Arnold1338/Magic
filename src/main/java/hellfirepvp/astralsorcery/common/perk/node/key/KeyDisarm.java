package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.math.MathHelper;
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

public class KeyDisarm extends KeyPerk
{
    private static final float defaultDropChance = 0.05f;
    public static final Config CONFIG;
    
    public KeyDisarm(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.HIGH, (Consumer)this::onAttack);
    }
    
    private void onAttack(final LivingHurtEvent event) {
        final DamageSource source = event.getSource();
        if (source.func_76346_g() != null && source.func_76346_g() instanceof Player) {
            final Player player = (Player)source.func_76346_g();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                final float chance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ((Double)KeyDisarm.CONFIG.dropChance.get()).floatValue());
                final float currentChance = MathHelper.func_76131_a(chance, 0.0f, 1.0f);
                for (final EquipmentSlot slot : EquipmentSlot.values()) {
                    if (KeyDisarm.rand.nextFloat() < currentChance) {
                        final LivingEntity attacked = event.getEntityLiving();
                        final ItemStack stack = attacked.getItemBySlot(slot);
                        if (!stack.isEmpty()) {
                            attacked.func_184201_a(slot, ItemStack.field_190927_a);
                            ItemUtils.dropItemNaturally(attacked.field_70170_p, attacked.func_226277_ct_(), attacked.func_226278_cu_(), attacked.func_226281_cx_(), stack);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.disarm");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue dropChance;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.dropChance = cfgBuilder.comment("Defines the chance (in percent) per hit to make the attacked entity drop its armor.").translation(this.translationKey("dropChance")).defineInRange("dropChance", 0.05000000074505806, 0.0, 1.0);
        }
    }
}
