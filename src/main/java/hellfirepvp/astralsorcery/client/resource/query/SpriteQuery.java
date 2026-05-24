package hellfirepvp.astralsorcery.client.resource.query;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;

public class SpriteQuery extends TextureQuery
{
    private final int rows;
    private final int columns;
    private Object spriteResource;
    
    public SpriteQuery(final AssetLoader.TextureLocation location, final int rows, final int columns, final String... path) {
        super(location, path);
        this.rows = rows;
        this.columns = columns;
    }
    
    private SpriteQuery(final Object spriteResource, final int rows, final int columns) {
        super(null, new String[] { "" });
        this.spriteResource = spriteResource;
        this.rows = rows;
        this.columns = columns;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static SpriteQuery of(final SpriteSheetResource res) {
        return new SpriteQuery(res, res.getRows(), res.getColumns());
    }
    
    public int getRows() {
        return this.rows;
    }
    
    public int getColumns() {
        return this.columns;
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public SpriteSheetResource resolveSprite() {
        if (this.spriteResource == null) {
            final AbstractRenderableTexture res = this.resolve();
            this.spriteResource = new SpriteSheetResource(res, this.getRows(), this.getColumns());
        }
        return (SpriteSheetResource)this.spriteResource;
    }
}
