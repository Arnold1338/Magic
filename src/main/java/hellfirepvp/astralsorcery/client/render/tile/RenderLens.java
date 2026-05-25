package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import java.awt.Color;
import java.util.List;
import org.joml.Vector3f;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.client.model.builtin.ModelLensColored;
import hellfirepvp.astralsorcery.client.model.builtin.ModelLens;
import hellfirepvp.astralsorcery.common.tile.TileLens;

public class RenderLens extends CustomTileEntityRenderer<TileLens>
{
    private static final ModelLens MODEL_LENS;
    private static final ModelLensColored MODEL_LENS_COLORED;
    
    public RenderLens(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileLens tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final List<BlockPos> linked = tile.getLinkedPositions();
        float degYaw = 0.0f;
        float degPitch = 0.0f;
        renderStack.popPose();
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
                renderStack.translate(0.5, 1.5, 0.5);
                renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).getMultiBufferSource()180.0f));
                renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()degYaw % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.popPose();
                    renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), -degPitch);
                    renderStack.popPose();
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
                renderStack.translate(0.5, -0.5, 0.5);
                renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()(-degYaw + 180.0f) % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.popPose();
                    renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), degPitch);
                    renderStack.popPose();
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
                renderStack.translate(0.5, 0.5, 1.5);
                renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).getMultiBufferSource()270.0f));
                renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()(-degYaw + 180.0f) % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.popPose();
                    renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), -degPitch);
                    renderStack.popPose();
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
                renderStack.translate(0.5, 0.5, -0.5);
                renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).getMultiBufferSource()90.0f));
                renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()degYaw % 360.0f));
                if (tile.getColorType() != null) {
                    renderStack.popPose();
                    renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), degPitch);
                    renderStack.popPose();
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
                renderStack.translate(1.5, 0.5, 0.5);
                renderStack.mulPose(new org.joml.Vector3f(0, 0, 1).getMultiBufferSource()90.0f));
                renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()degYaw + 270.0f));
                if (tile.getColorType() != null) {
                    renderStack.popPose();
                    renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), -degPitch);
                    renderStack.popPose();
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
                renderStack.translate(-0.5, 0.5, 0.5);
                renderStack.mulPose(new org.joml.Vector3f(0, 0, 1).getMultiBufferSource()270.0f));
                renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()-degYaw + 90.0f));
                if (tile.getColorType() != null) {
                    renderStack.popPose();
                    renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()180.0f));
                    this.renderLensColored(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, tile.getColorType().getColor(), degPitch);
                    renderStack.popPose();
                }
                this.renderLens(renderStack, renderTypeBuffer, combinedLight, combinedOverlay, -degPitch);
                break;
            }
        }
        renderStack.popPose();
    }
    
    private void renderLensColored(final PoseStack renderStack, final MultiBufferSource buffer, final int combinedLight, final int combinedOverlay, final Color c, final float pitch) {
        RenderLens.MODEL_LENS_COLORED.glass.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.fitting1.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.fitting2.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.detail1_1.field_78795_f = pitch * 0.017453292f;
        RenderLens.MODEL_LENS_COLORED.detail1.field_78795_f = pitch * 0.017453292f;
        final VertexConsumer vb = buffer.getBuffer(RenderTypesAS.MODEL_LENS_COLORED_GLASS);
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
