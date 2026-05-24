package hellfirepvp.astralsorcery.common.base;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.resource.AssetLibrary;
import java.util.Locale;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.LevelAccessor;

public enum MoonPhase
{
    FULL, 
    WANING_3_4, 
    WANING_1_2, 
    WANING_1_4, 
    NEW, 
    WAXING_1_4, 
    WAXING_1_2, 
    WAXING_3_4;
    
    public static MoonPhase fromWorld(final IWorld world) {
        return MiscUtils.getEnumEntry(MoonPhase.class, world.dimensionType().func_236035_c_(world.func_241851_ab()));
    }
    
    @OnlyIn(Dist.CLIENT)
    public AbstractRenderableTexture getTexture() {
        return AssetLibrary.loadTexture(AssetLoader.TextureLocation.MISC, "moon_" + this.name().toLowerCase(Locale.ROOT));
    }
}
