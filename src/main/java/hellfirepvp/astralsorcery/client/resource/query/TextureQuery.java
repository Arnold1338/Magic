package hellfirepvp.astralsorcery.client.resource.query;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.resource.AssetLibrary;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;

public class TextureQuery
{
    private final AssetLoader.TextureLocation location;
    private final String[] path;
    private Supplier<?> resource;
    
    public TextureQuery(final AssetLoader.TextureLocation location, final String... path) {
        this.location = location;
        this.path = path;
    }
    
    @OnlyIn(Dist.CLIENT)
    public AbstractRenderableTexture resolve() {
        if (this.resource == null) {
            this.resource = AssetLibrary.loadReference(this.location, this.path);
        }
        return (AbstractRenderableTexture)this.resource.get();
    }
}
