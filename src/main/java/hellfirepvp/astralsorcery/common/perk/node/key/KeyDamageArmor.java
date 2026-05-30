package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.item.ItemStack;
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

public class KeyDamageArmor extends KeyPerk
{
    private static final float defaultDamagePerArmor = 0.05f;
    public static final Config CONFIG;
    
    public KeyDamageArmor(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOW, (Consumer)this::onDamage);
    }
    
    private void onDamage(final LivingHurtEvent event) {
        final LivingEntity attacked = event.getEntityLiving();
        if (attacked instanceof Player) {
            final Player player = (Player)attacked;
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                int armorPieces = 0;
                for (final ItemStack armor : player.func_184193_aE()) {
                    if (!armor.isEmpty()) {
                        ++armorPieces;
                    }
                }
                if (armorPieces == 0) {

                }
                final double dmgArmor = (double)KeyDamageArmor.CONFIG.damagePerArmor.get();
                float dmg = event.getAmount();
                dmg *= (float)(dmgArmor * armorPieces * PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT));
                event.setAmount(Math.max(event.getAmount() - dmg, 0.0f));
                final int armorDmg = Mth.func_76123_f(dmg * 1.3f);
                for (final ItemStack stack : player.func_184193_aE()) {
                    stack.func_222118_a(armorDmg, (LivingEntity)player, pl -> pl.func_213361_c(EquipmentSlot.MAINHAND));
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.damage_armor");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue damagePerArmor;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.damagePerArmor = cfgBuilder.comment("Defines how much damage is dealt additionally to armor. This value gets multiplied by the amount of armorpieces the entity you're attacking wears.").translation(this.translationKey("damagePerArmor")).defineInRange("damagePerArmor", 0.05000000074505806, 0.009999999776482582, 0.20000000298023224);
        }
    }
}
