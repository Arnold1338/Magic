package hellfirepvp.astralsorcery.client.effect;

import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import java.awt.Color;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.function.VFXPositionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXScaleFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXRenderOffsetFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

public abstract class EntityVisualFX extends EntityComplexFX
{
    private Vector3 oldPos;
    private Vector3 motion;
    private float gravityY;
    private float alphaMultiplier;
    private float scaleMultiplier;
    private VFXAlphaFunction alphaFunction;
    private VFXRenderOffsetFunction renderOffsetFunction;
    private VFXScaleFunction scaleFunction;
    private VFXColorFunction colorFunction;
    private VFXMotionController motionController;
    private VFXPositionController positionController;
    
    public EntityVisualFX(final Vector3 pos) {
        super(pos);
        this.motion = new Vector3();
        this.gravityY = 0.0f;
        this.alphaMultiplier = 1.0f;
        this.scaleMultiplier = 1.0f;
        this.alphaFunction = VFXAlphaFunction.CONSTANT;
        this.renderOffsetFunction = VFXRenderOffsetFunction.IDENTITY;
        this.scaleFunction = VFXScaleFunction.IDENTITY;
        this.colorFunction = VFXColorFunction.WHITE;
        this.motionController = VFXMotionController.IDENTITY;
        this.positionController = VFXPositionController.CONSTANT;
        this.oldPos = this.pos.clone();
    }
    
    public <T extends EntityVisualFX> T setAlphaMultiplier(final float alphaMultiplier) {
        this.alphaMultiplier = alphaMultiplier;
        return (T)this;
    }
    
    public <T extends EntityVisualFX> T setScaleMultiplier(final float scaleMultiplier) {
        this.scaleMultiplier = scaleMultiplier;
        return (T)this;
    }
    
    public float getAlphaMultiplier() {
        return this.alphaMultiplier;
    }
    
    public float getScaleMultiplier() {
        return this.scaleMultiplier;
    }
    
    public Vector3 getOldPosition() {
        return this.oldPos.clone();
    }
    
    @Override
    public <T extends EntityComplexFX> T setPosition(final Vector3 pos) {
        super.setPosition(pos);
        this.oldPos = pos.clone();
        return (T)this;
    }
    
    public Vector3 getMotion() {
        return this.motion.clone();
    }
    
    public <T extends EntityVisualFX> T setMotion(final Vector3 motion) {
        this.motion = motion.clone();
        return (T)this;
    }
    
    public <T extends EntityVisualFX> T setGravityStrength(final float grav) {
        this.gravityY = grav;
        return (T)this;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.motion = this.motionController.updateMotion(this, this.getMotion().addY(-this.gravityY));
        final Vector3 newPos = this.positionController.updatePosition(this, this.getPosition(), this.getMotion());
        this.oldPos = this.pos.clone();
        this.pos = newPos;
    }
    
    public abstract <T extends EntityVisualFX> void render(final BatchRenderContext<T> p0, final PoseStack p1, final IVertexBuilder p2, final float p3);
    
    public int getAlpha(final float pTicks) {
        return (int)(this.alphaFunction.getAlpha(this, this.getAlphaMultiplier(), pTicks) * 255.0f);
    }
    
    public float getScale(final float pTicks) {
        return this.scaleFunction.getScale(this, this.getScaleMultiplier(), pTicks);
    }
    
    public Color getColor(final float pTicks) {
        return this.colorFunction.getColor(this, pTicks);
    }
    
    public Vector3 getRenderPosition(final float pTicks) {
        return this.renderOffsetFunction.changeRenderPosition(this, RenderingVectorUtils.interpolate(this.getOldPosition(), this.getPosition(), pTicks), pTicks);
    }
    
    public <T extends EntityVisualFX> T color(final VFXColorFunction<?> colorFunction) {
        this.colorFunction = colorFunction;
        return (T)this;
    }
    
    public <T extends EntityVisualFX> T alpha(final VFXAlphaFunction<?> alphaFunction) {
        this.alphaFunction = alphaFunction;
        return (T)this;
    }
    
    public <T extends EntityVisualFX> T renderOffset(final VFXRenderOffsetFunction<?> renderOffsetController) {
        this.renderOffsetFunction = renderOffsetController;
        return (T)this;
    }
    
    public <T extends EntityVisualFX> T scale(final VFXScaleFunction<?> scaleFunction) {
        this.scaleFunction = scaleFunction;
        return (T)this;
    }
    
    public <T extends EntityVisualFX> T motion(final VFXMotionController<?> motionController) {
        this.motionController = motionController;
        return (T)this;
    }
    
    public <T extends EntityVisualFX> T position(final VFXPositionController<?> positionController) {
        this.positionController = positionController;
        return (T)this;
    }
}
