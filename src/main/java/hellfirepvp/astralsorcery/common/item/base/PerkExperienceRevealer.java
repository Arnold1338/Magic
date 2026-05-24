package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.world.item.ItemStack;

public interface PerkExperienceRevealer
{
    default boolean shouldReveal(final ItemStack stack) {
        return true;
    }
}
