package hellfirepvp.observerlib.client.preview;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.client.StructureRenderWorld;
import hellfirepvp.observerlib.api.structure.Structure;
import hellfirepvp.observerlib.api.tile.MatchableTile;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import hellfirepvp.observerlib.client.util.ClientTickHelper;
import hellfirepvp.observerlib.client.util.RenderTypeDecorator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderStructurePreview {

    public static void renderStructure(Structure structure, BlockPos center,
                                       PoseStack poseStack, MultiBufferSource bufferSource,
                                       boolean forceRender, float partialTick) {
        if (structure == null || Minecraft.getInstance().level == null) return;

        Holder<net.minecraft.world.level.biome.Biome> biome =
            Minecraft.getInstance().level.getBiome(center);
        StructureRenderWorld renderWorld = new StructureRenderWorld(structure, biome);

        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        long tick = ClientTickHelper.getClientTick();
        RandomSource rand = RandomSource.create();

        final int[] fullBright = {15, 15};
        BufferDecoratorBuilder decorator = new BufferDecoratorBuilder()
            .setLightmapDecorator((sky, block) -> fullBright);

        Runnable transparentSetup = () -> {
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        };
        Runnable transparentClean = () -> {
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        };

        poseStack.pushPose();

        for (BlockPos offset : structure.getContents().keySet()) {
            BlockPos at = center.offset(offset);
            MatchableState matchable = structure.getBlockStateAt(offset);
            BlockState renderState = matchable.getDescriptiveState(tick);

            if (renderState.isAir()) continue;
            if (!forceRender && Minecraft.getInstance().level.getBlockState(at).equals(renderState)) continue;

            BlockEntity renderTile = matchable.createBlockEntity(renderWorld, tick);

            poseStack.pushPose();
            poseStack.translate(at.getX() + 0.2, at.getY() + 0.2, at.getZ() + 0.2);
            poseStack.scale(0.6f, 0.6f, 0.6f);

            RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(
                RenderType.translucent(), transparentSetup, transparentClean);
            VertexConsumer buf = decorator.decorate(bufferSource.getBuffer(decorated));

            brd.renderBatched(renderState, BlockPos.ZERO, renderWorld, poseStack, buf, true, rand,
                renderTile != null ? net.minecraftforge.client.extensions.common.IClientBlockExtensions.of(renderTile).getRenderData(renderTile) : ModelData.EMPTY,
                decorated);

            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
