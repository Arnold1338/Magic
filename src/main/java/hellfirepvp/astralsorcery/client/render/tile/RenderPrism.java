package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.tile.TilePrism;

public class RenderPrism extends CustomTileEntityRenderer<TilePrism>
{
    public RenderPrism(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TilePrism tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
    }
}
