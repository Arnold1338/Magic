package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.client.model.builtin.ModelTelescope;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;

public class RenderTelescope extends CustomTileEntityRenderer<TileTelescope>
{
    private static final ModelTelescope MODEL_TELESCOPE;
    
    public RenderTelescope(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileTelescope tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        renderStack.popPose();
        renderStack.translate(0.5, 1.5, 0.5);
        renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).getMultiBufferSource()180.0f));
        renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()180.0f + tile.getRotation().ordinal() * 45.0f));
        RenderTelescope.MODEL_TELESCOPE.render(renderStack, renderTypeBuffer, combinedLight, combinedOverlay);
        renderStack.popPose();
    }
    
    static {
        MODEL_TELESCOPE = new ModelTelescope();
    }
}
