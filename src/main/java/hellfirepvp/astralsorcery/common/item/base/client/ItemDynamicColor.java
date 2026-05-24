package hellfirepvp.astralsorcery.common.item.base.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.ItemStack;

public interface ItemDynamicColor
{
    @OnlyIn(Dist.CLIENT)
    int getColor(final ItemStack p0, final int p1);
}
