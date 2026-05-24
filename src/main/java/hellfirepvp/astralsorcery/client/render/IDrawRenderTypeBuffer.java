package hellfirepvp.astralsorcery.client.render;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

public interface IDrawRenderTypeBuffer extends MultiBufferSource
{
    void draw();
    
    void draw(final RenderType p0);
    
    default IDrawRenderTypeBuffer defaultBuffer() {
        return of(MultiBufferSource.func_228455_a_(Tessellator.func_178181_a().func_178180_c()));
    }
    
    default IDrawRenderTypeBuffer of(final MultiBufferSource.Impl drawBuffer) {
        return new IDrawRenderTypeBuffer() {
            @Override
            public void draw() {
                drawBuffer.func_228461_a_();
            }
            
            @Override
            public void draw(final RenderType type) {
                drawBuffer.func_228462_a_(type);
            }
            
            public IVertexBuilder getBuffer(final RenderType renderType) {
                return drawBuffer.getBuffer(renderType);
            }
        };
    }
}
