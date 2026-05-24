package hellfirepvp.astralsorcery.client.resource;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.AstralSorcery;

public class BlockAtlasTexture extends Full
{
    private static final BlockAtlasTexture INSTANCE;
    
    private BlockAtlasTexture() {
        super(AstralSorcery.key("block_atlas_reference"));
    }
    
    public static BlockAtlasTexture getInstance() {
        return BlockAtlasTexture.INSTANCE;
    }
    
    @Override
    public void bindTexture() {
        final TextureManager mgr = Minecraft.getInstance().func_110434_K();
        mgr.func_110577_a(AtlasTexture.field_110575_b);
    }
    
    @Override
    public RenderState.TextureState asState() {
        return new RenderState.TextureState(AtlasTexture.field_110575_b, false, false);
    }
    
    static {
        INSTANCE = new BlockAtlasTexture();
    }
}
