package hellfirepvp.astralsorcery.client.effect.context.base;

import net.minecraft.world.phys.Vec3;
import hellfirepvp.astralsorcery.client.util.draw.RenderInfo;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.observerlib.client.util.RenderTypeDecorator;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.effect.EntityDynamicFX;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHandler;
import java.util.List;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.function.BiFunction;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.common.util.order.OrderSortable;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class BatchRenderContext<T extends EntityVisualFX> extends OrderSortable
{
    private static int counter;
    private final int id;
    private final SpriteSheetResource sprite;
    private boolean drawWithTexture;
    protected RenderType renderType;
    protected BiFunction<BatchRenderContext<T>, Vector3, T> particleCreator;
    
    public BatchRenderContext(final RenderType renderType, final BiFunction<BatchRenderContext<T>, Vector3, T> particleCreator) {
        this(new SpriteSheetResource(BlockAtlasTexture.getInstance()), renderType, particleCreator);
    }
    
    public BatchRenderContext(final AbstractRenderableTexture texture, final RenderType renderType, final BiFunction<BatchRenderContext<T>, Vector3, T> particleCreator) {
        this(new SpriteSheetResource(texture), renderType, particleCreator);
    }
    
    public BatchRenderContext(final SpriteSheetResource sprite, final RenderType renderType, final BiFunction<BatchRenderContext<T>, Vector3, T> particleCreator) {
        this.drawWithTexture = true;
        this.id = BatchRenderContext.counter++;
        this.sprite = sprite;
        this.renderType = renderType;
        this.particleCreator = particleCreator.andThen(fx -> {
            final int frames = this.sprite.getFrameCount();
            if (frames > 1) {
                fx.setMaxAge(frames);
            }
            return fx;
        });
    }
    
    public T makeParticle(final Vector3 pos) {
        return this.particleCreator.apply(this, pos);
    }
    
    public BatchRenderContext<T> setDrawWithTexture(final boolean drawWithTexture) {
        this.drawWithTexture = drawWithTexture;
        return this;
    }
    
    public SpriteSheetResource getSprite() {
        return this.sprite;
    }
    
    public void renderAll(final List<EffectHandler.PendingEffect> effects, final PoseStack renderStack, final IDrawRenderTypeBuffer drawBuffer, final float pTicks) {
        final BatchRenderContext blankCtx = this;
        effects.stream().filter(effect -> effect.getEffect() instanceof EntityDynamicFX).forEach(effect -> ((EntityDynamicFX)effect.getEffect()).renderNow((BatchRenderContext<EntityVisualFX>)blankCtx, renderStack, drawBuffer, pTicks));
        RenderType drawType = this.getRenderType();
        if (this.drawWithTexture) {
            drawType = (RenderType)RenderTypeDecorator.wrapSetup(this.getRenderType(), () -> {
                RenderSystem.enableTexture();
                this.getSprite().bindTexture();
                return;
            }, () -> {
                BlockAtlasTexture.getInstance().bindTexture();
                RenderSystem.disableTexture();
                return;
            });
        }
        final IVertexBuilder buf = drawBuffer.getBuffer(drawType);
        effects.forEach(effect -> effect.getEffect().render((BatchRenderContext<EntityVisualFX>)this, renderStack, buf, pTicks));
        this.drawBatched(buf, drawBuffer);
    }
    
    private void drawBatched(final IVertexBuilder buf, final IDrawRenderTypeBuffer renderTypeBuffer) {
        if (buf instanceof BufferBuilder && this.getRenderType().func_228664_q_() == 7) {
            final Vec3 view = RenderInfo.getInstance().getARI().func_216785_c();
            ((BufferBuilder)buf).func_181674_a((float)view.field_72450_a, (float)view.field_72448_b, (float)view.field_72449_c);
        }
        renderTypeBuffer.draw();
    }
    
    public RenderType getRenderType() {
        return this.renderType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BatchRenderContext that = (BatchRenderContext)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
    
    static {
        BatchRenderContext.counter = 0;
    }
}
