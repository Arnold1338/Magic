package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import hellfirepvp.astralsorcery.client.model.builtin.ModelTelescope;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;

public class RenderTelescope extends CustomTileEntityRenderer<TileTelescope>
{
    private static final ModelTelescope MODEL_TELESCOPE;
    
    public RenderTelescope(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileTelescope tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(0.5, 1.5, 0.5);
        renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0f));
        renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f + tile.getRotation().ordinal() * 45.0f));
        RenderTelescope.MODEL_TELESCOPE.render(renderStack, renderTypeBuffer, combinedLight, combinedOverlay);
        renderStack.func_227865_b_();
    }
    
    static {
        MODEL_TELESCOPE = new ModelTelescope();
    }
}
