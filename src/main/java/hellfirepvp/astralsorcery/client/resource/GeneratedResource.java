package hellfirepvp.astralsorcery.client.resource;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.io.IOException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import net.minecraft.resources.ResourceLocation;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public class GeneratedResource extends BindableResource implements ReloadableResource
{
    private final Supplier<BufferedImage> imageGen;
    private final boolean blur;
    private final boolean clamp;
    
    GeneratedResource(final ResourceLocation key, final Supplier<BufferedImage> imageGenerator, final boolean blur, final boolean clamp) {
        super(NameUtil.prefixPath(key, "dynamic_"));
        this.imageGen = imageGenerator;
        this.blur = blur;
        this.clamp = clamp;
    }
    
    @Override
    protected AbstractTexture allocateGlId() {
        if (AssetLibrary.isReloading()) {
            return null;
        }
        final TextureManager mgr = Minecraft.getInstance().func_110434_K();
        final AbstractTexture resource = mgr.func_229267_b_(this.getKey());
        if (resource != null) {
            return resource;
        }
        final InMemoryTexture texture = new InMemoryTexture((Supplier)this.imageGen, this.blur, this.clamp);
        mgr.func_229263_a_(this.getKey(), (Texture)texture);
        return mgr.func_229267_b_(this.getKey());
    }
    
    private static class InMemoryTexture extends Texture
    {
        private final Supplier<BufferedImage> imageGen;
        private final boolean blur;
        private final boolean clamp;
        
        private InMemoryTexture(final Supplier<BufferedImage> imageGen, final boolean blur, final boolean clamp) {
            this.imageGen = imageGen;
            this.blur = blur;
            this.clamp = clamp;
        }
        
        public void func_195413_a(final ResourceManager manager) throws IOException {
            final NativeImage image = NativeImage.func_211679_a(NativeImage.PixelFormat.RGBA, this.createMemInput());
            if (!RenderSystem.isOnRenderThreadOrInit()) {
                RenderSystem.recordRenderCall(() -> this.loadImage(image, this.blur, this.clamp));
            }
            else {
                this.loadImage(image, this.blur, this.clamp);
            }
        }
        
        private void loadImage(final NativeImage imageIn, final boolean blurIn, final boolean clampIn) {
            TextureUtil.func_225681_a_(this.func_110552_b(), 0, imageIn.func_195702_a(), imageIn.func_195714_b());
            imageIn.func_227789_a_(0, 0, 0, 0, 0, imageIn.func_195702_a(), imageIn.func_195714_b(), blurIn, clampIn, false, true);
        }
        
        private InputStream createMemInput() throws IOException {
            final BufferedImage bufferedImage = this.imageGen.get();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.close();
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }
}
