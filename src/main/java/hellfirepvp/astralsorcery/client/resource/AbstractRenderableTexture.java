package hellfirepvp.astralsorcery.client.resource;

import java.util.Objects;
import net.minecraft.util.Tuple;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractRenderableTexture
{
    private final ResourceLocation key;
    
    protected AbstractRenderableTexture(final ResourceLocation key) {
        this.key = key;
    }
    
    public final ResourceLocation getKey() {
        return this.key;
    }
    
    public abstract void bindTexture();
    
    public abstract RenderStateShard.TextureStateShard asState();
    
    public abstract Tuple<Float, Float> getUVOffset();
    
    public abstract float getUWidth();
    
    public abstract float getVWidth();
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AbstractRenderableTexture that = (AbstractRenderableTexture)o;
        return Objects.equals(this.getKey(), that.getKey());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getKey());
    }
    
    public abstract static class Full extends AbstractRenderableTexture
    {
        public Full(final ResourceLocation key) {
            super(key);
        }
        
        @Override
        public Tuple<Float, Float> getUVOffset() {
            return (Tuple<Float, Float>)new Tuple((Object)0.0f, (Object)0.0f);
        }
        
        @Override
        public final float getUWidth() {
            return 1.0f;
        }
        
        @Override
        public final float getVWidth() {
            return 1.0f;
        }
    }
}
