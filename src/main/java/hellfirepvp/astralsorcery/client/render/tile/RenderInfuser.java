package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;

public class RenderInfuser extends CustomTileEntityRenderer<TileInfuser>
{
    public RenderInfuser(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileInfuser tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final ItemStack stack = tile.getItemInput();
        if (!stack.isEmpty()) {
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(0.5, 0.75, 0.5);
            RenderingUtils.renderItemAsEntity(stack, renderStack, renderTypeBuffer, 0.0, 0.0, 0.0, combinedLight, pTicks, tile.getTicksExisted());
            renderStack.func_227865_b_();
        }
    }
}
