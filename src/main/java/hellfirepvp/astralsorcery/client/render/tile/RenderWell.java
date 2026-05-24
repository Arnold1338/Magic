package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.util.tile.PrecisionSingleFluidTank;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import java.awt.Color;
import net.minecraft.world.level.IBlockDisplayReader;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.tile.TileWell;

public class RenderWell extends CustomTileEntityRenderer<TileWell>
{
    public RenderWell(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileWell tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final PrecisionSingleFluidTank tank = tile.getTank();
        if (!tank.getFluid().isEmpty() && tank.getFluidAmount() > 0) {
            final FluidStack contained = tank.getFluid();
            final TextureAtlasSprite tas = RenderingUtils.getParticleTexture(contained);
            final Color fluidColor = new Color(contained.getFluid().getAttributes().getColor((IBlockDisplayReader)tile.func_145831_w(), tile.func_174877_v()));
            final VertexConsumer buf = renderTypeBuffer.getBuffer(RenderTypesAS.TER_WELL_LIQUID);
            final Vector3 offset = new Vector3(0.5, 0.32, 0.5).addY(tank.getPercentageFilled() * 0.6);
            RenderingDrawUtils.renderAngleRotatedTexturedRectVB(buf, renderStack, offset, Vector3.RotAxis.Y_AXIS, (float)Math.toRadians(45.0), 0.54f, tas.func_94209_e(), tas.func_94206_g(), tas.func_94212_f() - tas.func_94209_e(), tas.func_94210_h() - tas.func_94206_g(), fluidColor.getRed(), fluidColor.getGreen(), fluidColor.getBlue(), 255);
        }
        final ItemStack catalyst = tile.getInventory().getStackInSlot(0);
        if (!catalyst.isEmpty()) {
            RenderingUtils.renderItemAsEntity(catalyst, renderStack, renderTypeBuffer, 0.5, 0.75, 0.5, combinedLight, pTicks, tile.getTicksExisted());
        }
    }
}
