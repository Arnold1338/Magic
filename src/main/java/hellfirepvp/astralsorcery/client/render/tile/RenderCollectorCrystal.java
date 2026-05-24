package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;

public class RenderCollectorCrystal extends CustomTileEntityRenderer<TileCollectorCrystal>
{
    public RenderCollectorCrystal(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileCollectorCrystal tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        if (!tile.doesSeeSky()) {
            return;
        }
        Color color = tile.getCollectorType().getDisplayColor();
        long seed = RenderingUtils.getPositionSeed(tile.func_174877_v());
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(0.5, 0.5, 0.5);
        RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, color, seed, 24, 24.0f, 12);
        seed ^= 0x54FF129A4B11C382L;
        if (tile.isEnhanced() && tile.getAttunedConstellation() != null) {
            color = tile.getAttunedConstellation().getConstellationColor();
        }
        RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, color, seed, 24, 24.0f, 12);
        renderStack.func_227865_b_();
    }
}
