package hellfirepvp.observerlib.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.structure.Structure;
import hellfirepvp.observerlib.api.tile.MatchableTile;
import hellfirepvp.observerlib.client.preview.RenderStructurePreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class StructureRenderer {
    public static void renderStructure(Structure structure, BlockPos center,
                                       PoseStack poseStack, MultiBufferSource bufferSource,
                                       boolean forceRender, float partialTick) {
        if (structure == null) return;
        RenderStructurePreview.renderStructure(structure, center, poseStack, bufferSource, forceRender, partialTick);
    }
}
