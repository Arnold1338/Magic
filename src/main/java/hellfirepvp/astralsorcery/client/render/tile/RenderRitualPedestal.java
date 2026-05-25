package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.Blending;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedCelestialCrystal;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedRockCrystal;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;

public class RenderRitualPedestal extends CustomTileEntityRenderer<TileRitualPedestal>
{
    public RenderRitualPedestal(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileRitualPedestal tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final ItemStack stack = tile.getCurrentCrystal();
        if (stack.isEmpty()) {
            return;
        }
        ItemStack display = stack;
        if (display.getItem() instanceof ItemAttunedRockCrystal) {
            display = new ItemStack((ItemLike)BlocksAS.ROCK_COLLECTOR_CRYSTAL);
        }
        if (display.getItem() instanceof ItemAttunedCelestialCrystal) {
            display = new ItemStack((ItemLike)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL);
        }
        renderStack.popPose();
        renderStack.func_227861_a_(0.5, 0.8999999761581421, 0.5);
        renderStack.translate(2.0f, 2.0f, 2.0f);
        RenderingUtils.renderTranslucentItemStackModelGround(display, renderStack, Color.WHITE, Blending.DEFAULT, 255);
        renderStack.scale();
        final IWeakConstellation ritualConstellation = tile.getRitualConstellation();
        if (ritualConstellation != null) {
            final long seed = RenderingUtils.getPositionSeed(tile.getBlockState());
            final int scales = tile.isWorking() ? 24 : 12;
            final int count = tile.isWorking() ? 16 : 12;
            renderStack.popPose();
            renderStack.func_227861_a_(0.5, 1.2000000476837158, 0.5);
            RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, ritualConstellation.getConstellationColor(), seed, scales, (float)scales, count);
            renderStack.scale();
        }
    }
}
