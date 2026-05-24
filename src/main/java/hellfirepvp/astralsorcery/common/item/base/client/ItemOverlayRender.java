package hellfirepvp.astralsorcery.common.item.base.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.item.ItemStack;
import com.mojang.blaze3d.vertex.PoseStack;

public interface ItemOverlayRender
{
    @OnlyIn(Dist.CLIENT)
    boolean renderOverlay(final PoseStack p0, final ItemStack p1, final float p2);
}
