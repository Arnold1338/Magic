package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import hellfirepvp.astralsorcery.client.model.builtin.ModelAttunementAltar;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;

public class RenderAttunementAltar extends CustomTileEntityRenderer<TileAttunementAltar>
{
    private static final ModelAttunementAltar MODEL_ATTUNEMENT_ALTAR;
    
    public RenderAttunementAltar(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileAttunementAltar tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(0.5, 0.5, 0.5);
        renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0f));
        RenderAttunementAltar.MODEL_ATTUNEMENT_ALTAR.render(renderStack, renderTypeBuffer, combinedLight, combinedOverlay);
        renderStack.func_227865_b_();
        final float spinDur = 100.0f;
        final float spinStart = 60.0f;
        final float startY = -1.2f;
        final float endY = -0.5f;
        final float tickPartY = (endY - startY) / spinStart;
        final float prevPosY = endY + tile.prevActivationTick * tickPartY;
        final float posY = endY + tile.activationTick * tickPartY;
        final float framePosY = RenderingVectorUtils.interpolate(prevPosY, posY, pTicks);
        double generalAnimationTick = (ClientScheduler.getClientTick() + pTicks) / 4.0;
        if (tile.animate) {
            if (tile.tesrLocked) {
                tile.tesrLocked = false;
            }
        }
        else if (tile.tesrLocked) {
            generalAnimationTick = 7.25;
        }
        else if (Math.abs(generalAnimationTick % spinDur - 7.25) <= 0.3125) {
            generalAnimationTick = 7.25;
            tile.tesrLocked = true;
        }
        for (int i = 1; i < 9; ++i) {
            final float incrementer = spinDur / 8.0f * i;
            final double aFrame = generalAnimationTick + incrementer;
            final double prevAFrame = generalAnimationTick + incrementer - 1.0;
            final double renderFrame = RenderingVectorUtils.interpolate(prevAFrame, aFrame, 0.0f);
            final double partRenderFrame = renderFrame % spinDur / spinDur;
            final float normalized = (float)(partRenderFrame * 2.0 * 3.141592653589793);
            final float xOffset = MathHelper.func_76134_b(normalized);
            final float zOffset = MathHelper.func_76126_a(normalized);
            final float rotation = RenderingVectorUtils.interpolate(tile.prevActivationTick / spinStart, tile.activationTick / spinStart, pTicks);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(0.5, (double)framePosY, 0.5);
            renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0f));
            RenderAttunementAltar.MODEL_ATTUNEMENT_ALTAR.renderHovering(renderStack, renderTypeBuffer.getBuffer(RenderAttunementAltar.MODEL_ATTUNEMENT_ALTAR.getGeneralType()), combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f, xOffset, zOffset, rotation);
            renderStack.func_227865_b_();
        }
    }
    
    static {
        MODEL_ATTUNEMENT_ALTAR = new ModelAttunementAltar();
    }
}
