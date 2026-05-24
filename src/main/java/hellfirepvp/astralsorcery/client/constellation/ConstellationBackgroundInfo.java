package hellfirepvp.astralsorcery.client.constellation;

import com.google.common.base.Preconditions;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.client.renderer.RenderType;

public class ConstellationBackgroundInfo
{
    private final RenderType renderType;
    private final AbstractRenderableTexture texture;
    
    ConstellationBackgroundInfo(final RenderType renderType, final AbstractRenderableTexture texture) {
        Preconditions.checkNotNull((Object)renderType);
        Preconditions.checkNotNull((Object)texture);
        this.renderType = renderType;
        this.texture = texture;
    }
    
    public RenderType getRenderType() {
        return this.renderType;
    }
    
    public AbstractRenderableTexture getBackgroundTexture() {
        return this.texture;
    }
}
