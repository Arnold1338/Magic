package hellfirepvp.astralsorcery.common.item.base.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.item.ItemStack;

public interface ItemHeldRender
{
    @OnlyIn(Dist.CLIENT)
    boolean renderInHand(final ItemStack p0, final PoseStack p1, final float p2);
}
