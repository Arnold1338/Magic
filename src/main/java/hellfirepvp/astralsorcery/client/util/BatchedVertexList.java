package hellfirepvp.astralsorcery.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.function.Consumer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class BatchedVertexList
{
    private final VertexFormat vFormat;
    private VertexBuffer vbo;
    private boolean initialized;
    
    public BatchedVertexList(final VertexFormat vFormat) {
        this.vbo = null;
        this.initialized = false;
        this.vFormat = vFormat;
    }
    
    public void batch(final Consumer<BufferBuilder> batchFn) {
        if (this.initialized) {
            return;
        }
        final BufferBuilder buf = Tessellator.func_178181_a().func_178180_c();
        this.vbo = new VertexBuffer(this.vFormat);
        batchFn.accept(buf);
        buf.func_178977_d();
        this.vbo.func_227875_a_(buf);
        this.initialized = true;
    }
    
    public void render(final PoseStack renderStack) {
        if (!this.initialized) {
            return;
        }
        this.vbo.func_177359_a();
        this.vFormat.func_227892_a_(0L);
        this.vbo.func_227874_a_(renderStack.func_227866_c_().func_227870_a_(), 7);
        this.vFormat.func_227895_d_();
        VertexBuffer.func_177361_b();
    }
    
    public void reset() {
        if (this.vbo != null) {
            this.vbo.close();
        }
        this.initialized = false;
    }
}
