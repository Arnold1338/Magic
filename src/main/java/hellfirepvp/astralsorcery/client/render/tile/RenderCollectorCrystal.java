package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;

public class RenderCollectorCrystal extends CustomTileEntityRenderer<TileCollectorCrystal>
{
    public RenderCollectorCrystal(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileCollectorCrystal tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        if (!tile.doesSeeSky()) {

        }
        Color color = tile.getCollectorType().getDisplayColor();
        long seed = RenderingUtils.getPositionSeed(tile.getBlockState());
        renderStack.popPose();
        renderStack.translate(0.5, 0.5, 0.5);
        RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, color, seed, 24, 24.0f, 12);
        seed ^= 0x54FF129A4B11C382L;
        if (tile.isEnhanced() && tile.getAttunedConstellation() != null) {
            color = tile.getAttunedConstellation().getConstellationColor();
        }
        RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, color, seed, 24, 24.0f, 12);
        renderStack.popPose();
    }
}
