package hellfirepvp.astralsorcery.client.effect.vfx;

import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXBlock extends EntityVisualFX
{
    private BlockState blockState;
    private Vector3 rotationDegreeAxis;
    private Vector3 prevRotationDegreeAxis;
    private Vector3 rotationChange;
    
    public FXBlock(final Vector3 pos) {
        super(pos);
        this.blockState = null;
        this.rotationDegreeAxis = new Vector3();
        this.prevRotationDegreeAxis = new Vector3();
        this.rotationChange = new Vector3();
    }
    
    public FXBlock setBlockState(final BlockState blockState) {
        this.blockState = blockState;
        return this;
    }
    
    public FXBlock tumble() {
        this.rotationDegreeAxis = Vector3.positiveYRandom().multiply(360);
        this.rotationChange = Vector3.random().multiply(12);
        return this;
    }
    
    public Vector3 getInterpolatedRotation(final float percent) {
        return new Vector3(RenderingVectorUtils.interpolate(this.prevRotationDegreeAxis.getX(), this.rotationDegreeAxis.getX(), percent), RenderingVectorUtils.interpolate(this.prevRotationDegreeAxis.getY(), this.rotationDegreeAxis.getY(), percent), RenderingVectorUtils.interpolate(this.prevRotationDegreeAxis.getZ(), this.rotationDegreeAxis.getZ(), percent));
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.rotationChange.lengthSquared() > 0.0) {
            this.prevRotationDegreeAxis = this.rotationDegreeAxis.clone();
            this.rotationDegreeAxis.add(this.rotationChange);
        }
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final VertexConsumer vb, final float pTicks) {
        if (this.blockState == null) {

        }
        final int alpha = this.getAlpha(pTicks);
        final Color c = this.getColor(pTicks);
        final int[] colorOverride = { c.getRed(), c.getGreen(), c.getBlue(), alpha };
        final Vector3 translate = this.getRenderPosition(pTicks).subtract(RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks));
        final Vector3 rotation = this.getInterpolatedRotation(pTicks);
        final float scale = this.getScale(pTicks);
        renderStack.popPose();
        renderStack.translate(translate.getX(), translate.getY(), translate.getZ());
        renderStack.translate(0.5, 0.5, 0.5);
        renderStack.translate(scale, scale, scale);
        renderStack.mulPose(new org.joml.Quaternionf().rotateX((float)Math.toRadians((float)rotation.getX())));
        renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians((float)rotation.getY())));
        renderStack.mulPose(new org.joml.Quaternionf().rotateZ((float)Math.toRadians((float)rotation.getZ())));
        renderStack.translate(-0.5, -0.5, -0.5);
        new BufferDecoratorBuilder().setColorDecorator((r, g, b, a) -> colorOverride).decorate(vb, decorated -> RenderingUtils.renderSimpleBlockModel(this.blockState, renderStack, decorated));
        renderStack.popPose();
    }
}
