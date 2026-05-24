package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import java.awt.Color;
import java.util.List;
import com.mojang.math.Vector3f;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import hellfirepvp.astralsorcery.client.model.builtin.ModelLensColored;
import hellfirepvp.astralsorcery.client.model.builtin.ModelLens;
import hellfirepvp.astralsorcery.common.tile.TileLens;

public class RenderLens extends CustomTileEntityRenderer<TileLens>
{
    private static final ModelLens MODEL_LENS;
    private static final ModelLensColored MODEL_LENS_COLORED;
    
    public RenderLens(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileLens tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final List<BlockPos> linked = tile.getLinkedPositions();
        float degYaw = 0.0f;
        float degPitch = 0.0f;
        renderStack.func_227860_a_();
        switch (tile.getPlacedAgainst()) {
            case DOWN: {
                if (!linked.isEmpty() && linked.size() == 1) {
                    final BlockPos to = linked.get(0);
                    final BlockPos from = tile.getTrPos();
                    final Vector3 dir = new Vector3((Vector3i)to).subtract(new Vector3((Vector3i)from));
                    degPitch = (float)Math.atan2(dir.getY(), Math.sqrt(dir.getX() * dir.getX() + dir.getZ() * dir.getZ()));
                    degYaw = (float)Math.atan2(dir.getX(), dir.getZ());
                    degYaw = 180.0f + (float)Math.toDegrees(-degYaw);
                    degPitch = (float)Math.toDegrees(degPitch);
                }
                renderStack.func_227861_a_(0.5, 1.5, 0.5);
                renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0f));
                renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(degYaw % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.func_227860_a_();
                    renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), -degPitch);
                    renderStack.func_227865_b_();
                }
                this.renderLens(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, degPitch);
                break;
            }
            case UP: {
                if (!linked.isEmpty() && linked.size() == 1) {
                    final BlockPos to = linked.get(0);
                    final BlockPos from = tile.getTrPos();
                    final Vector3 dir = new Vector3((Vector3i)to).subtract(new Vector3((Vector3i)from));
                    degPitch = (float)Math.atan2(dir.getY(), Math.sqrt(dir.getX() * dir.getX() + dir.getZ() * dir.getZ()));
                    degYaw = (float)Math.atan2(dir.getX(), dir.getZ());
                    degYaw = 180.0f + (float)Math.toDegrees(-degYaw);
                    degPitch = (float)Math.toDegrees(degPitch);
                }
                renderStack.func_227861_a_(0.5, -0.5, 0.5);
                renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((-degYaw + 180.0f) % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.func_227860_a_();
                    renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), degPitch);
                    renderStack.func_227865_b_();
                }
                this.renderLens(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, -degPitch);
                break;
            }
            case NORTH: {
                if (!linked.isEmpty() && linked.size() == 1) {
                    final BlockPos to = linked.get(0);
                    final BlockPos from = tile.getTrPos();
                    final Vector3 dir = new Vector3((Vector3i)to).subtract(new Vector3((Vector3i)from));
                    degPitch = (float)Math.atan2(dir.getZ(), Math.sqrt(dir.getX() * dir.getX() + dir.getY() * dir.getY()));
                    degYaw = (float)Math.atan2(dir.getX(), dir.getY());
                    degYaw = 180.0f + (float)Math.toDegrees(-degYaw);
                    degPitch = (float)Math.toDegrees(degPitch);
                }
                renderStack.func_227861_a_(0.5, 0.5, 1.5);
                renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(270.0f));
                renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((-degYaw + 180.0f) % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.func_227860_a_();
                    renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), -degPitch);
                    renderStack.func_227865_b_();
                }
                this.renderLens(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, degPitch);
                break;
            }
            case SOUTH: {
                if (!linked.isEmpty() && linked.size() == 1) {
                    final BlockPos to = linked.get(0);
                    final BlockPos from = tile.getTrPos();
                    final Vector3 dir = new Vector3((Vector3i)to).subtract(new Vector3((Vector3i)from));
                    degPitch = (float)Math.atan2(dir.getZ(), Math.sqrt(dir.getX() * dir.getX() + dir.getY() * dir.getY()));
                    degYaw = (float)Math.atan2(dir.getX(), dir.getY());
                    degYaw = 180.0f + (float)Math.toDegrees(-degYaw);
                    degPitch = (float)Math.toDegrees(degPitch);
                }
                renderStack.func_227861_a_(0.5, 0.5, -0.5);
                renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(90.0f));
                renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(degYaw % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.func_227860_a_();
                    renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), degPitch);
                    renderStack.func_227865_b_();
                }
                this.renderLens(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, -degPitch);
                break;
            }
            case WEST: {
                if (!linked.isEmpty() && linked.size() == 1) {
                    final BlockPos to = linked.get(0);
                    final BlockPos from = tile.getTrPos();
                    final Vector3 dir = new Vector3((Vector3i)to).subtract(new Vector3((Vector3i)from));
                    degPitch = (float)Math.atan2(dir.getX(), Math.sqrt(dir.getZ() * dir.getZ() + dir.getY() * dir.getY()));
                    degYaw = (float)Math.atan2(dir.getZ(), dir.getY());
                    degYaw = 180.0f + (float)Math.toDegrees(-degYaw);
                    degPitch = (float)Math.toDegrees(degPitch);
                }
                renderStack.func_227861_a_(1.5, 0.5, 0.5);
                renderStack.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_(90.0f));
                renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(degYaw + 270.0f));
                if (tile.getColorType() != null) {
                    renderStack.func_227860_a_();
                    renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), -degPitch);
                    renderStack.func_227865_b_();
                }
                this.renderLens(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, degPitch);
                break;
            }
            case EAST: {
                if (!linked.isEmpty() && linked.size() == 1) {
                    final BlockPos to = linked.get(0);
                    final BlockPos from = tile.getTrPos();
                    final Vector3 dir = new Vector3((Vector3i)to).subtract(new Vector3((Vector3i)from));
                    degPitch = (float)Math.atan2(dir.getX(), Math.sqrt(dir.getZ() * dir.getZ() + dir.getY() * dir.getY()));
                    degYaw = (float)Math.atan2(dir.getZ(), dir.getY());
                    degYaw = 180.0f + (float)Math.toDegrees(-degYaw);
                    degPitch = (float)Math.toDegrees(degPitch);
                }
                renderStack.func_227861_a_(-0.5, 0.5, 0.5);
                renderStack.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_(270.0f));
                renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(-degYaw + 90.0f));
                if (tile.getColorType() != null) {
                    renderStack.func_227860_a_();
                    renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), degPitch);
                    renderStack.func_227865_b_();
                }
                this.renderLens(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, -degPitch);
                break;
            }
        }
        renderStack.func_227865_b_();
    }
    
    private void renderLensColored(final PoseStack renderStack, final MultiBufferSource buffer, final int combinedLight, final int combinedOverlay, final Color c, final float pitch) {
        RenderLens.MODEL_LENS_COLORED.glass.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.fitting1.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.fitting2.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.detail1_1.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.detail1.field_78795_f = pitch * 0.017453292f;
        final IVertexBuilder vb = buffer.getBuffer(RenderTypesAS.MODEL_LENS_COLORED_GLASS);
        RenderLens.MODEL_LENS_COLORED.renderGlass(renderStack, vb, combinedLight, combinedOverlay, c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 1.0f);
        RenderingUtils.refreshDrawing(vb, RenderTypesAS.MODEL_LENS_COLORED_GLASS);
        RenderLens.MODEL_LENS_COLORED.render(renderStack, buffer, combinedLight, combinedOverlay);
    }
    
    private void renderLens(final PoseStack renderStack, final MultiBufferSource buffer, final int combinedLight, final int combinedOverlay, final float pitch) {
        RenderLens.MODEL_LENS.lens.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS.render(renderStack, buffer, combinedLight, combinedOverlay);
    }
    
    static {
        MODEL_LENS = new ModelLens();
        MODEL_LENS_COLORED = new ModelLensColored();
    }
}
