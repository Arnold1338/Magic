package hellfirepvp.astralsorcery.client.resource;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.client.util.obj.WavefrontObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AssetLoader
{
    private AssetLoader() {
    }
    
    @OnlyIn(Dist.CLIENT)
    protected static BindableResource load(final AssetLocation location, final SubLocation subLocation, final String name, final String suffix) {
        return new BindableResource(buildResourceString(location, subLocation, name, suffix));
    }
    
    @OnlyIn(Dist.CLIENT)
    private static String buildResourceString(final AssetLocation location, final SubLocation subLocation, String name, final String suffix) {
        if (name.endsWith(suffix)) {
            name = name.substring(0, name.length() - suffix.length());
        }
        final StringBuilder builder = new StringBuilder();
        builder.append("astralsorcery").append(':').append(location.location).append("/");
        if (subLocation != null) {
            builder.append(subLocation.getLocation()).append("/");
        }
        builder.append(name).append(suffix);
        return builder.toString();
    }
    
    @OnlyIn(Dist.CLIENT)
    protected static BindableResource loadTexture(final TextureLocation location, final String name) {
        return load(AssetLocation.TEXTURES, location, name, ".png");
    }
    
    @OnlyIn(Dist.CLIENT)
    public static WavefrontObject loadObjModel(final ModelLocation location, final String name) {
        return new WavefrontObject(new ResourceLocation(buildResourceString(AssetLocation.MODELS, location, name, ".obj")));
    }
    
    public enum ModelLocation implements SubLocation
    {
        OBJ("obj");
        
        private final String location;
        
        private ModelLocation(final String location) {
            this.location = location;
        }
        
        @Override
        public String getLocation() {
            return this.location;
        }
    }
    
    public enum TextureLocation implements SubLocation
    {
        ITEMS("item"), 
        BLOCKS("block"), 
        GUI("gui"), 
        MISC("misc"), 
        MODEL("model"), 
        EFFECT("effect"), 
        ENVIRONMENT("environment"), 
        CONSTELLATION("constellation");
        
        private final String location;
        
        private TextureLocation(final String location) {
            this.location = location;
        }
        
        @Override
        public String getLocation() {
            return this.location;
        }
    }
    
    public enum AssetLocation
    {
        MODELS("models"), 
        TEXTURES("textures");
        
        private final String location;
        
        private AssetLocation(final String location) {
            this.location = location;
        }
    }
    
    public interface SubLocation
    {
        String getLocation();
    }
}
