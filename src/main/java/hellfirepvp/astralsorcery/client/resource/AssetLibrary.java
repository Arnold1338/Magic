package hellfirepvp.astralsorcery.client.resource;

import java.util.ArrayList;
import hellfirepvp.astralsorcery.client.sky.astral.AstralSkyRenderer;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.resource.VanillaResourceType;
import net.minecraftforge.resource.IResourceType;
import java.util.function.Predicate;
import net.minecraft.server.packs.resources.ResourceManager;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.util.object.CacheReference;
import java.util.function.Supplier;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

public class AssetLibrary implements ISelectiveResourceReloadListener
{
    public static AssetLibrary INSTANCE;
    private static boolean reloading;
    private static final Map<AssetLoader.SubLocation, Map<String, AbstractRenderableTexture>> loadedTextures;
    private static final Map<ResourceLocation, GeneratedResource> dynamicTextures;
    private static final List<ReloadableResource> reloadableResources;
    
    private AssetLibrary() {
    }
    
    public static Supplier<AbstractRenderableTexture> loadReference(final AssetLoader.TextureLocation location, final String... path) {
        return new CacheReference<AbstractRenderableTexture>(() -> loadTexture(location, path));
    }
    
    public static AbstractRenderableTexture loadTexture(final AssetLoader.TextureLocation location, final String... path) {
        final String name = String.join("/", (CharSequence[])path);
        if (name.endsWith(".png")) {
            throw new IllegalArgumentException("Tried to loadTexture with appended .png from the AssetLibrary!");
        }
        final AbstractRenderableTexture resource = AssetLibrary.loadedTextures.computeIfAbsent(location, l -> new HashMap()).computeIfAbsent(name, str -> AssetLoader.loadTexture(location, str));
        AssetLibrary.reloadableResources.add((ReloadableResource)resource);
        return resource;
    }
    
    public static GeneratedResource loadGeneratedResource(final ResourceLocation key, final Supplier<BufferedImage> imageGenerator, final boolean blur) {
        return loadGeneratedResource(key, imageGenerator, blur, false);
    }
    
    public static GeneratedResource loadGeneratedResource(final ResourceLocation key, final Supplier<BufferedImage> imageGenerator, final boolean blur, final boolean clamp) {
        if (AssetLibrary.dynamicTextures.containsKey(key)) {
            return AssetLibrary.dynamicTextures.get(key);
        }
        final GeneratedResource resource = new GeneratedResource(key, imageGenerator, blur, clamp);
        AssetLibrary.reloadableResources.add(resource);
        AssetLibrary.dynamicTextures.put(key, resource);
        return resource;
    }
    
    public static boolean isReloading() {
        return AssetLibrary.reloading;
    }
    
    public void onResourceManagerReload(final ResourceManager resourceManager, final Predicate<IResourceType> resourcePredicate) {
        if (AssetLibrary.reloading || !resourcePredicate.test((IResourceType)VanillaResourceType.TEXTURES)) {
            return;
        }
        AssetLibrary.reloading = true;
        AstralSorcery.log.info("[AssetLibrary] Refreshing and Invalidating Resources");
        AssetLibrary.reloadableResources.forEach(ReloadableResource::invalidateAndReload);
        AssetLibrary.reloading = false;
        AstralSkyRenderer.INSTANCE.reset();
        AstralSorcery.log.info("[AssetLibrary] Successfully reloaded library.");
    }
    
    static {
        AssetLibrary.INSTANCE = new AssetLibrary();
        AssetLibrary.reloading = false;
        loadedTextures = new HashMap<AssetLoader.SubLocation, Map<String, AbstractRenderableTexture>>();
        dynamicTextures = new HashMap<ResourceLocation, GeneratedResource>();
        reloadableResources = new ArrayList<ReloadableResource>();
    }
}
