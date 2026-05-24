package hellfirepvp.astralsorcery.client.util;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class LightmapUtil
{
    public static int getPackedFullbrightCoords() {
        return 15728880;
    }
    
    public static int getPackedLightCoords(final int lightValue) {
        return getPackedLightCoords(lightValue, lightValue);
    }
    
    public static int getPackedLightCoords(final int skyLight, final int blockLight) {
        return skyLight << 20 | blockLight << 4;
    }
    
    public static int getPackedLightCoords(final IBlockDisplayReader world, final BlockPos at) {
        return WorldRenderer.func_228421_a_(world, at);
    }
}
