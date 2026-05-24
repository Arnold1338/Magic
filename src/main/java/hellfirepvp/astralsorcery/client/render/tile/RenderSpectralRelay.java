package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.tile.TileSpectralRelay;

public class RenderSpectralRelay extends CustomTileEntityRenderer<TileSpectralRelay>
{
    public RenderSpectralRelay(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileSpectralRelay tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final ItemStack stack = tile.getInventory().getStackInSlot(0);
        if (!stack.isEmpty()) {
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(0.5, 0.10000000149011612, 0.5);
            RenderingUtils.renderItemAsEntity(stack, renderStack, renderTypeBuffer, 0.0, 0.0, 0.0, combinedLight, pTicks, tile.getTicksExisted());
            renderStack.func_227865_b_();
        }
    }
}
