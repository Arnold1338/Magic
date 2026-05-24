package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class CustomTileEntityRenderer<T extends BlockEntity> extends TileEntityRenderer<T>
{
    public CustomTileEntityRenderer(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public abstract void func_225616_a_(final T p0, final float p1, final PoseStack p2, final MultiBufferSource p3, final int p4, final int p5);
}
