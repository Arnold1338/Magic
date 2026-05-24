package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.world.level.item.ItemStack;

public interface PerkExperienceRevealer
{
    default boolean shouldReveal(final ItemStack stack) {
        return true;
    }
}
