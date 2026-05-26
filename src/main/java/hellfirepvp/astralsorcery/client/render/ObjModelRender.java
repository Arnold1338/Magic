package hellfirepvp.astralsorcery.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import hellfirepvp.astralsorcery.client.util.obj.WavefrontObject;

public class ObjModelRender
{
    private static WavefrontObject crystalModel;
    private static WavefrontObject celestialWingsModel;
    private static VertexBuffer vboCelestialWings;
    private static WavefrontObject wraithWingsModel;
    private static VertexBuffer wraithWingsBones;
    private static VertexBuffer wraithWingsWing;
    
    public static void renderCrystal(final PoseStack renderStack, final VertexConsumer buf, final Runnable drawFn) {
        if (ObjModelRender.crystalModel == null) {
            ObjModelRender.crystalModel = AssetLoader.loadObjModel(AssetLoader.ModelLocation.OBJ, "crystal");
        }
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(renderStack.last().pose());
        ObjModelRender.crystalModel.render(buf);
        drawFn.run();
        RenderSystem.popMatrix();
    }
    
    public static void renderCelestialWings(final PoseStack renderStack) {
        if (ObjModelRender.celestialWingsModel == null) {
            ObjModelRender.celestialWingsModel = AssetLoader.loadObjModel(AssetLoader.ModelLocation.OBJ, "celestial_wings");
        }
        if (ObjModelRender.vboCelestialWings == null) {
            final int[] lightGray = { 178, 178, 178, 255 };
            BufferDecoratorBuilder.withColor((r, g, b, a) -> lightGray).decorate(Tessellator.func_178181_a().func_178180_c(), decorated -> ObjModelRender.vboCelestialWings = ObjModelRender.celestialWingsModel.batch(decorated));
        }
        ObjModelRender.vboCelestialWings.func_177359_a();
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227892_a_(0L);
        ObjModelRender.vboCelestialWings.func_227874_a_(renderStack.last().pose(), ObjModelRender.celestialWingsModel.getGLDrawingMode());
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227895_d_();
        VertexBuffer.func_177361_b();
    }
    
    public static void renderWraithWings(final PoseStack renderStack) {
        if (ObjModelRender.wraithWingsModel == null) {
            ObjModelRender.wraithWingsModel = AssetLoader.loadObjModel(AssetLoader.ModelLocation.OBJ, "wraith_wings");
        }
        if (ObjModelRender.wraithWingsBones == null) {
            final int[] gray = { 77, 77, 77, 255 };
            BufferDecoratorBuilder.withColor((r, g, b, a) -> gray).decorate(Tessellator.func_178181_a().func_178180_c(), decorated -> ObjModelRender.wraithWingsBones = ObjModelRender.wraithWingsModel.batchOnly(decorated, "Bones"));
        }
        if (ObjModelRender.wraithWingsWing == null) {
            final int[] black = { 0, 0, 0, 255 };
            BufferDecoratorBuilder.withColor((r, g, b, a) -> black).decorate(Tessellator.func_178181_a().func_178180_c(), decorated -> ObjModelRender.wraithWingsWing = ObjModelRender.wraithWingsModel.batchOnly(decorated, "Wing"));
        }
        ObjModelRender.wraithWingsBones.func_177359_a();
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227892_a_(0L);
        ObjModelRender.wraithWingsBones.func_227874_a_(renderStack.last().pose(), ObjModelRender.wraithWingsModel.getGLDrawingMode());
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227895_d_();
        VertexBuffer.func_177361_b();
        ObjModelRender.wraithWingsWing.func_177359_a();
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227892_a_(0L);
        ObjModelRender.wraithWingsWing.func_227874_a_(renderStack.last().pose(), ObjModelRender.wraithWingsModel.getGLDrawingMode());
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227895_d_();
        VertexBuffer.func_177361_b();
    }
}
