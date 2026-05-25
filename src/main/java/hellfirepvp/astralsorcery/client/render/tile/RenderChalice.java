package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import org.joml.Vector3f;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.tile.TileChalice;

public class RenderChalice extends CustomTileEntityRenderer<TileChalice>
{
    public RenderChalice(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileChalice tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final FluidStack stack = tile.getTank().getFluid();
        if (stack.isEmpty()) {
            return;
        }
        final TextureAtlasSprite tas = RenderingUtils.getParticleTexture(stack);
        if (tas == null) {
            return;
        }
        final Vector3 rotation = RenderingVectorUtils.interpolate(tile.getPrevRotation(), tile.getRotation(), pTicks);
        final Color color = new Color(ColorUtils.getOverlayColor(stack));
        final float percSize = 0.125f + tile.getTank().getPercentageFilled() * 0.375f;
        final float ulength = tas.func_94212_f() - tas.func_94209_e();
        final float vlength = tas.func_94210_h() - tas.func_94206_g();
        final float uPart = ulength * percSize;
        final float vPart = vlength * percSize;
        final float uOffset = tas.func_94209_e() + ulength / 2.0f - uPart / 2.0f;
        final float vOffset = tas.func_94206_g() + vlength / 2.0f - vPart / 2.0f;
        renderStack.popPose();
        renderStack.translate(0.5, 1.399999976158142, 0.5);
        renderStack.mulPose(new org.joml.Quaternionf().rotateX((float)Math.toRadians((float)rotation.getX())));
        renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians((float)rotation.getY())));
        renderStack.mulPose(new org.joml.Quaternionf().rotateZ((float)Math.toRadians((float)rotation.getZ())));
        renderStack.translate(percSize, percSize, percSize);
        final VertexConsumer buf = renderTypeBuffer.getBuffer(RenderTypesAS.TER_CHALICE_LIQUID);
        RenderingDrawUtils.renderTexturedCubeCentralColorNormal(renderStack, buf, uOffset, vOffset, uPart, vPart, color.getRed(), color.getGreen(), color.getBlue(), 255, renderStack.last().func_227872_b_());
        renderStack.popPose();
    }
}
