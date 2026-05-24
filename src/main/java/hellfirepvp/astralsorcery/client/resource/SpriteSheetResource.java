package hellfirepvp.astralsorcery.client.resource;

import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import net.minecraft.util.Tuple;
import net.minecraft.client.renderer.RenderState;
import hellfirepvp.astralsorcery.common.util.NameUtil;

public class SpriteSheetResource extends AbstractRenderableTexture
{
    protected float uPart;
    protected float vPart;
    protected int frameCount;
    protected int rows;
    protected int columns;
    private final AbstractRenderableTexture resource;
    
    public SpriteSheetResource(final AbstractRenderableTexture resource) {
        this(resource, 1, 1);
    }
    
    public SpriteSheetResource(final AbstractRenderableTexture resource, final int rows, final int columns) {
        super(NameUtil.suffixPath(resource.getKey(), "_sprite"));
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Can't instantiate a sprite sheet without any rows or columns!");
        }
        this.frameCount = rows * columns;
        this.rows = rows;
        this.columns = columns;
        this.resource = resource;
        this.uPart = 1.0f / columns;
        this.vPart = 1.0f / rows;
    }
    
    @Override
    public void bindTexture() {
        this.resource.bindTexture();
    }
    
    @Override
    public RenderState.TextureState asState() {
        return this.resource.asState();
    }
    
    @Override
    public Tuple<Float, Float> getUVOffset() {
        final long timer = ClientScheduler.getClientTick();
        return this.getUVOffset(timer);
    }
    
    @Override
    public float getUWidth() {
        return this.getULength();
    }
    
    @Override
    public float getVWidth() {
        return this.getVLength();
    }
    
    public AbstractRenderableTexture getResource() {
        return this.resource;
    }
    
    public float getULength() {
        return this.uPart;
    }
    
    public float getVLength() {
        return this.vPart;
    }
    
    public Tuple<Float, Float> getUVOffset(final long frameTimer) {
        final int frame = (int)(frameTimer % this.frameCount);
        return (Tuple<Float, Float>)new Tuple((Object)(frame % this.columns * this.uPart), (Object)(frame / this.columns * this.vPart));
    }
    
    public Tuple<Float, Float> getUVOffset(final EntityComplexFX fx, final float pTicks, final float spriteDisplayFactor) {
        final float agePart = fx.getAge() * spriteDisplayFactor + pTicks;
        final float perc = agePart / fx.getMaxAge();
        final long timer = MathHelper.func_76141_d(this.getFrameCount() * perc);
        return this.getUVOffset(timer);
    }
    
    public int getFrameCount() {
        return this.frameCount;
    }
    
    public int getRows() {
        return this.rows;
    }
    
    public int getColumns() {
        return this.columns;
    }
}
