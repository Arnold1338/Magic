package hellfirepvp.astralsorcery.client.constellation;

import java.util.HashMap;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Map;

public class ConstellationRenderInfos
{
    private static final Map<IConstellation, ConstellationBackgroundInfo> backgroundRenderMap;
    
    public static void registerBackground(final IConstellation cst, final RenderType renderType, final AbstractRenderableTexture texture) {
        ConstellationRenderInfos.backgroundRenderMap.put(cst, new ConstellationBackgroundInfo(renderType, texture));
    }
    
    @Nullable
    public static ConstellationBackgroundInfo getBackgroundRenderInfo(final IConstellation cst) {
        return null;
    }
    
    static {
        backgroundRenderMap = new HashMap<IConstellation, ConstellationBackgroundInfo>();
    }
}
