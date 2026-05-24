package hellfirepvp.astralsorcery.client.util.draw;

import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;

public class BufferContext extends BufferBuilder
{
    private boolean inDrawing;
    
    BufferContext(final int size) {
        super(size);
        this.inDrawing = false;
    }
    
    public void func_181668_a(final int mode, final VertexFormat format) {
        super.func_181668_a(mode, format);
        this.inDrawing = true;
    }
    
    public void func_181674_a(final float x, final float y, final float z) {
        if (this.inDrawing) {
            super.func_181674_a(x, y, z);
        }
    }
    
    public void draw() {
        if (this.inDrawing) {
            RenderingUtils.finishDrawing(this);
            this.inDrawing = false;
        }
    }
}
