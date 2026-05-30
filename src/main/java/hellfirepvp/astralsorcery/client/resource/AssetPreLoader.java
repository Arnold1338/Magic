package hellfirepvp.astralsorcery.client.resource;

import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPerkTree;
import hellfirepvp.astralsorcery.client.registry.RegistryEffectTypes;
import hellfirepvp.astralsorcery.client.registry.RegistryEffectTemplates;
import hellfirepvp.astralsorcery.client.registry.RegistryRenderTypes;
import hellfirepvp.astralsorcery.client.registry.RegistrySprites;
import hellfirepvp.astralsorcery.client.registry.RegistryTextures;
import net.minecraftforge.resource.VanillaResourceType;
import net.minecraftforge.resource.IResourceType;
import java.util.function.Predicate;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

public class AssetPreLoader implements ISelectiveResourceReloadListener
{
    public static final AssetPreLoader INSTANCE;
    private boolean initialized;
    
    private AssetPreLoader() {
        this.initialized = false;
    }
    
    public void onResourceManagerReload(final ResourceManager resourceManager, final Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test((IResourceType)VanillaResourceType.TEXTURES)) {
            if (this.initialized) {

            }
            RegistryTextures.loadTextures();
            RegistrySprites.loadSprites();
            RegistryRenderTypes.init();
            RegistryEffectTemplates.init();
            RegistryEffectTypes.init();
            ScreenJournalPerkTree.refreshDrawBuffer();
            this.initialized = true;
        }
    }
    
    static {
        INSTANCE = new AssetPreLoader();
    }
}
