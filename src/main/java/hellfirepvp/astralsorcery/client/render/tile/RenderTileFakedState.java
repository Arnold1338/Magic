package hellfirepvp.astralsorcery.client.render.tile;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.awt.Color;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import hellfirepvp.observerlib.client.util.RenderTypeDecorator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.block.AirBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import hellfirepvp.astralsorcery.common.tile.base.TileFakedState;

public class RenderTileFakedState extends CustomTileEntityRenderer<TileFakedState>
{
    public RenderTileFakedState(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileFakedState tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final BlockState fakedState = tile.getFakedState();
        if (fakedState.getBlock() instanceof AirBlock) {
            return;
        }
        final Color blendColor = tile.getOverlayColor();
        final int[] color = { blendColor.getRed(), blendColor.getGreen(), blendColor.getBlue(), 128 };
        final RenderType type = RenderTypeLookup.func_239221_b_(fakedState);
        final RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(type, () -> {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            return;
        }, () -> {
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            return;
        });
        final BufferDecoratorBuilder decorator = BufferDecoratorBuilder.withColor((r, g, b, a) -> color);
        final IVertexBuilder buf = renderTypeBuffer.getBuffer((RenderType)decorated);
        RenderingUtils.renderSimpleBlockModel(fakedState, renderStack, decorator.decorate(buf), tile.func_174877_v(), tile, true);
    }
}
