package hellfirepvp.astralsorcery.client.util.obj;

public class TextureCoordinate
{
    public float u;
    public float v;
    public float w;
    
    public TextureCoordinate(final float u, final float v) {
        this(u, v, 0.0f);
    }
    
    public TextureCoordinate(final float u, final float v, final float w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }
}
