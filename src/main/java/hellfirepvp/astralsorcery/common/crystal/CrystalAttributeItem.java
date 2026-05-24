package hellfirepvp.astralsorcery.common.crystal;

import javax.annotation.Nullable;
import net.minecraft.world.level.item.ItemStack;

public interface CrystalAttributeItem
{
    @Nullable
    CrystalAttributes getAttributes(final ItemStack p0);
    
    void setAttributes(final ItemStack p0, @Nullable final CrystalAttributes p1);
}
