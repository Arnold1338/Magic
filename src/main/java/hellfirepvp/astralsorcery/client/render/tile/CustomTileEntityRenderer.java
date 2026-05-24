package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class CustomTileEntityRenderer<T extends BlockEntity> extends BlockEntityRenderer<T>
{
    public CustomTileEntityRenderer(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public abstract void func_225616_a_(final T p0, final float p1, final PoseStack p2, final MultiBufferSource p3, final int p4, final int p5);
}
