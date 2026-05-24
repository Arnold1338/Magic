package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.tick.PlayerTickPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyMending extends KeyPerk implements PlayerTickPerk
{
    private static final int defaultChanceToRepair = 800;
    private static final int defaultChargeCost = 60;
    public static final Config CONFIG;
    
    public KeyMending(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    @Override
    public void onPlayerTick(final Player player, final LogicalSide side) {
        if (side.isServer()) {
            int repairChance = (int)KeyMending.CONFIG.chanceToRepair.get();
            repairChance /= (int)PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, ResearchHelper.getProgress(player, side), PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
            repairChance = Math.max(repairChance, 1);
            for (final ItemStack armor : player.func_184193_aE()) {
                if (KeyMending.rand.nextInt(repairChance) != 0) {
                    continue;
                }
                if (armor.isEmpty() || !armor.func_77984_f() || !armor.func_77951_h() || !AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeyMending.CONFIG.chargeCost.get(), false)) {
                    continue;
                }
                armor.setDamageValue(armor.getDamageValue() - 1);
            }
        }
    }
    
    static {
        CONFIG = new Config("key.mending");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.IntValue chanceToRepair;
        private ForgeConfigSpec.IntValue chargeCost;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.chanceToRepair = cfgBuilder.comment("Sets the chance (Random.nextInt(chance) == 0) to try to see if a piece of armor on the player that is damageable and damaged can be repaired; the lower the more likely.").translation(this.translationKey("chanceToRepair")).defineInRange("chanceToRepair", 800, 5, Integer.MAX_VALUE);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per restored durability point.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 60, 1, 500);
        }
    }
}
