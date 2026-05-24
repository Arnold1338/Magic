package hellfirepvp.astralsorcery.client.resource.query;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.Tuple;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;

public class TextureSubQuery extends TextureQuery
{
    private final float uOffset;
    private final float vOffset;
    private final float uLength;
    private final float vLength;
    
    public TextureSubQuery(final AssetLoader.TextureLocation location, final String name, final float uOffset, final float vOffset, final float uLength, final float vLength) {
        super(location, new String[] { name });
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.uLength = uLength;
        this.vLength = vLength;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public AbstractRenderableTexture resolve() {
        final AbstractRenderableTexture res = super.resolve();
        return new AbstractRenderableTexture(res.getKey()) {
            @Override
            public void bindTexture() {
                res.bindTexture();
            }
            
            @Override
            public RenderState.TextureState asState() {
                return res.asState();
            }
            
            @Override
            public Tuple<Float, Float> getUVOffset() {
                return (Tuple<Float, Float>)new Tuple((Object)TextureSubQuery.this.uOffset, (Object)TextureSubQuery.this.vOffset);
            }
            
            @Override
            public float getUWidth() {
                return TextureSubQuery.this.uLength;
            }
            
            @Override
            public float getVWidth() {
                return TextureSubQuery.this.vLength;
            }
            
            @Override
            public boolean equals(final Object obj) {
                return res.equals(obj);
            }
            
            @Override
            public int hashCode() {
                return res.hashCode();
            }
        };
    }
}
