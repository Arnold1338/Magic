package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.player.Player;
import com.mojang.math.Vector3f;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.common.entity.technical.EntityObservatoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import hellfirepvp.astralsorcery.client.model.builtin.ModelObservatory;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;

public class RenderObservatory extends CustomTileEntityRenderer<TileObservatory>
{
    private static final ModelObservatory MODEL_OBSERVATORY;
    
    public RenderObservatory(final BlockEntityRenderDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileObservatory tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
        final Entity ridden;
        if (player != null && (ridden = Minecraft.func_71410_x().field_71439_g.getVehicle()) != null && ridden instanceof EntityObservatoryHelper && ((EntityObservatoryHelper)ridden).getAssociatedObservatory() != null) {
            ((EntityObservatoryHelper)ridden).applyObservatoryRotationsFrom(tile, player, false);
        }
        final float prevYaw = tile.prevObservatoryYaw;
        final float yaw = tile.observatoryYaw;
        final float prevPitch = tile.prevObservatoryPitch;
        final float pitch = tile.observatoryPitch;
        final float iYawDegree = RenderingVectorUtils.interpolateRotation(prevYaw + 180.0f, yaw + 180.0f, pTicks);
        final float iPitchDegree = RenderingVectorUtils.interpolateRotation(prevPitch, pitch, pTicks);
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(0.5, 1.5, 0.5);
        renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0f));
        renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
        RenderObservatory.MODEL_OBSERVATORY.setupRotations(iYawDegree, iPitchDegree);
        RenderObservatory.MODEL_OBSERVATORY.render(renderStack, renderTypeBuffer, combinedLight, combinedOverlay);
        renderStack.func_227865_b_();
    }
    
    static {
        MODEL_OBSERVATORY = new ModelObservatory();
    }
}
