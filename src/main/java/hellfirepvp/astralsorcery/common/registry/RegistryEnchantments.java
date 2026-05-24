package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.enchantment.EnchantmentScorchingHeat;
import hellfirepvp.astralsorcery.common.lib.EnchantmentsAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.enchantment.EnchantmentNightVision;
import net.minecraft.world.item.enchantment.Enchantment;

public class RegistryEnchantments
{
    private RegistryEnchantments() {
    }
    
    public static void init() {
        EnchantmentsAS.NIGHT_VISION = (Enchantment)register(new EnchantmentNightVision()).setRegistryName(AstralSorcery.key("night_vision"));
        EnchantmentsAS.SCORCHING_HEAT = (Enchantment)register(new EnchantmentScorchingHeat()).setRegistryName(AstralSorcery.key("scorching_heat"));
    }
    
    private static <T extends Enchantment> T register(final T effect) {
        AstralSorcery.getProxy().getRegistryPrimer().register(effect);
        return effect;
    }
}
