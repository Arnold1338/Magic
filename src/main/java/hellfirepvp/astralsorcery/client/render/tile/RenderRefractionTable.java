package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.math.Vector3f;
import net.minecraft.client.resources.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.client.model.builtin.ModelRefractionTable;
import hellfirepvp.astralsorcery.common.tile.TileRefractionTable;

public class RenderRefractionTable extends CustomTileEntityRenderer<TileRefractionTable>
{
    private static final ModelRefractionTable MODEL_REFRACTION_TABLE;
    
    public RenderRefractionTable(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileRefractionTable tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        if (!tile.hasParchment() && !tile.getInputStack().isEmpty()) {
            final ItemStack input = tile.getInputStack();
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(0.5, 0.8500000238418579, 0.5);
            renderStack.func_227862_a_(0.625f, 0.625f, 0.625f);
            Minecraft.func_71410_x().func_175599_af().func_229110_a_(input, ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, renderStack, renderTypeBuffer);
            renderStack.func_227865_b_();
        }
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(0.5, 1.5, 0.5);
        renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0f));
        RenderType type = RenderRefractionTable.MODEL_REFRACTION_TABLE.getGeneralType();
        VertexConsumer vb = renderTypeBuffer.getBuffer(type);
        RenderRefractionTable.MODEL_REFRACTION_TABLE.renderFrame(renderStack, vb, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f, tile.hasParchment());
        RenderingUtils.refreshDrawing(vb, type);
        if (!tile.getGlassStack().isEmpty()) {
            type = RenderTypesAS.MODEL_REFRACTION_TABLE_GLASS;
            vb = renderTypeBuffer.getBuffer(type);
            RenderRefractionTable.MODEL_REFRACTION_TABLE.renderGlass(renderStack, vb, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
            RenderingUtils.refreshDrawing(vb, type);
        }
        renderStack.func_227865_b_();
    }
    
    static {
        MODEL_REFRACTION_TABLE = new ModelRefractionTable();
    }
}
