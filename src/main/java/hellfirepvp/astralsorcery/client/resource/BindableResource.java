package hellfirepvp.astralsorcery.client.resource;

import net.minecraft.client.renderer.RenderState;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BindableResource extends Full implements ReloadableResource
{
    private Texture resource;
    private String path;
    
    protected BindableResource(final ResourceLocation key) {
        super(key);
        this.resource = null;
        this.path = null;
    }
    
    BindableResource(final String path) {
        this(AstralSorcery.key(path.replaceAll("[^a-zA-Z0-9\\.\\-]", "_")));
        this.path = path;
        this.allocateGlId();
    }
    
    public String getPath() {
        return this.path;
    }
    
    public SpriteSheetResource asSpriteSheet(final int rows, final int columns) {
        return new SpriteSheetResource(this, rows, columns);
    }
    
    @Override
    public void invalidateAndReload() {
        Minecraft.getInstance().func_110434_K().func_147645_c(this.getKey());
        this.resource = null;
    }
    
    protected Texture allocateGlId() {
        if (AssetLibrary.isReloading()) {
            return null;
        }
        final TextureManager mgr = Minecraft.getInstance().func_110434_K();
        final Texture resource = mgr.func_229267_b_(this.getKey());
        if (resource != null) {
            return resource;
        }
        mgr.func_229263_a_(this.getKey(), (Texture)new SimpleTexture(new ResourceLocation(this.getPath())));
        return mgr.func_229267_b_(this.getKey());
    }
    
    @Override
    public void bindTexture() {
        if (AssetLibrary.isReloading()) {
            return;
        }
        if (this.resource == null) {
            this.resource = this.allocateGlId();
        }
        if (this.resource == null) {
            return;
        }
        RenderSystem.bindTexture(this.resource.func_110552_b());
    }
    
    @Override
    public RenderState.TextureState asState() {
        return new RenderState.TextureState(this.getKey(), false, false) {
            public void func_228547_a_() {
                RenderSystem.enableTexture();
                BindableResource.this.bindTexture();
                BindableResource.this.resource.setBlurMipmap(false, false);
            }
        };
    }
}
