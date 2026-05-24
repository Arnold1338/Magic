package hellfirepvp.observerlib.client.preview;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.client.StructureRenderWorld;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import hellfirepvp.observerlib.api.util.StructureUtil;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import hellfirepvp.observerlib.client.util.ClientTickHelper;
import hellfirepvp.observerlib.client.util.RenderTypeDecorator;
import hellfirepvp.observerlib.client.util.SimpleBossInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

@OnlyIn(Dist.CLIENT)
public class StructurePreview {
    private static final RandomSource rand = RandomSource.create();
    private final ResourceKey<Level> dimension;
    private final BlockPos origin;
    private final StructureSnapshot snapshot;
    private double minimumDisplayDistanceSq = 64.0;
    private double displayDistanceMultiplier = 1.75;
    private BiPredicate<Level, BlockPos> persistenceTest = (world, pos) -> true;
    private Component barText = null;
    private SimpleBossInfo bossInfo = null;

    private StructurePreview(ResourceKey<Level> dimension, BlockPos origin, StructureSnapshot snapshot) {
        this.dimension = dimension;
        this.origin = origin;
        this.snapshot = snapshot;
    }

    public static Builder newBuilder(ResourceKey<Level> dimension, BlockPos source, MatchableStructure structure) {
        return newBuilder(dimension, source, structure, ClientTickHelper.getClientTick());
    }

    public static Builder newBuilder(ResourceKey<Level> dimension, BlockPos source, MatchableStructure structure, long tick) {
        return new Builder(dimension, source, structure, tick);
    }

    private boolean isInRenderDistance(BlockPos position) {
        double distanceSq = Math.max(minimumDisplayDistanceSq,
            (double)(snapshot.getStructure().getMaximumOffset().getX() - snapshot.getStructure().getMinimumOffset().getX())
            * (snapshot.getStructure().getMaximumOffset().getZ() - snapshot.getStructure().getMinimumOffset().getZ()));
        distanceSq *= Math.max(1.0, displayDistanceMultiplier);
        return origin.distSqr(position) <= distanceSq;
    }

    boolean canRender(Level renderWorld, BlockPos renderPosition) {
        return dimension.equals(renderWorld.dimension()) && isInRenderDistance(renderPosition);
    }

    boolean canPersist(Level renderWorld, BlockPos position) {
        return persistenceTest.test(renderWorld, position);
    }

    public void tick(Level renderWorld, BlockPos position) {
        if (barText != null) {
            if (dimension.equals(renderWorld.dimension()) && isInRenderDistance(position)) {
                if (bossInfo == null) {
                    (bossInfo = SimpleBossInfo.newBuilder(barText, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS).build()).displayInfo();
                }
                float percFinished = StructureUtil.getMismatches(snapshot.getStructure(), renderWorld, origin).size()
                    / (float) snapshot.getStructure().getContents().size();
                bossInfo.setProgress(1.0f - percFinished);
            } else if (bossInfo != null) {
                bossInfo.removeInfo(); bossInfo = null;
            }
        }
    }

    void onRemove() { if (bossInfo != null) bossInfo.removeInfo(); }

    void render(Level renderWorld, PoseStack poseStack, Vec3 playerPos) {
        Optional<Integer> displaySlice = StructureUtil.getLowestMismatchingSlice(snapshot.getStructure(), renderWorld, origin);
        if (!displaySlice.isPresent()) return;

        Holder<Biome> biome = renderWorld.getBiome(origin);
        StructureRenderWorld drawWorld = new StructureRenderWorld(snapshot.getStructure(), biome);
        drawWorld.pushContentFilter(pos -> pos.getY() == displaySlice.get());

        final int[] fullBright = {15, 15};
        final boolean[] isMismatch = {false};

        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

        BufferDecoratorBuilder decorator = new BufferDecoratorBuilder()
            .setLightmapDecorator((sky, block) -> fullBright)
            .setColorDecorator((r, g, b, a) -> isMismatch[0] ? new int[]{255, 0, 0, 128} : new int[]{r, g, b, 128});

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
        poseStack.translate(-playerPos.x, -playerPos.y, -playerPos.z);

        List<Tuple<BlockPos, ? extends MatchableState>> structureSlice = snapshot.getStructure().getStructureSlice(displaySlice.get());
        structureSlice.sort(Comparator.comparingDouble(tpl -> tpl.getA().distSqr(
            new BlockPos((int) playerPos.x, (int) playerPos.y, (int) playerPos.z))));
        java.util.Collections.reverse(structureSlice);

        for (Tuple<BlockPos, ? extends MatchableState> expectedBlock : structureSlice) {
            BlockPos at = expectedBlock.getA().offset(origin);
            BlockState actual = renderWorld.getBlockState(at);
            if (snapshot.getStructure().matchesSingleBlock(renderWorld, origin, expectedBlock.getA(), actual, renderWorld.getBlockEntity(at))) continue;

            BlockState renderState = expectedBlock.getB() == MatchableState.REQUIRES_AIR
                ? Blocks.GLASS.defaultBlockState()
                : expectedBlock.getB().getDescriptiveState(snapshot.getSnapshotTick());

            BlockEntity renderTile = expectedBlock.getB().createBlockEntity(drawWorld, snapshot.getSnapshotTick());
            ModelData modelData = renderTile != null
                ? renderTile != null ? renderTile.getModelData() : ModelData.EMPTY
                : ModelData.EMPTY;

            poseStack.pushPose();
            poseStack.translate(at.getX() + 0.2, at.getY() + 0.2, at.getZ() + 0.2);
            poseStack.scale(0.6f, 0.6f, 0.6f);

            if (!actual.isAir()) isMismatch[0] = true;

            drawWorld.pushContentFilter(pos -> pos.equals(expectedBlock.getA()));

            RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(RenderType.translucent(), transparentSetup, transparentClean);
            VertexConsumer buf = decorator.decorate(buffers.getBuffer(decorated));
            brd.renderBatched(renderState, BlockPos.ZERO, drawWorld, poseStack, buf, true, rand, modelData, decorated);

            buffers.endBatch();
            drawWorld.popContentFilter();
            isMismatch[0] = false;
            poseStack.popPose();
        }

        drawWorld.popContentFilter();
        poseStack.popPose();
    }

    public static class Builder {
        private final StructurePreview preview;
        private Builder(ResourceKey<Level> dimension, BlockPos origin, MatchableStructure structure, long tick) {
            this.preview = new StructurePreview(dimension, origin, new StructureSnapshot(structure, tick));
        }
        public Builder setMinimumDisplayDistance(double d) { preview.minimumDisplayDistanceSq = d * d; return this; }
        public Builder setDisplayDistanceMultiplier(double d) { preview.displayDistanceMultiplier = d; return this; }
        public Builder removeIfOutOfRenderDistance() { preview.persistenceTest = preview.persistenceTest.and((w, p) -> preview.isInRenderDistance(p)); return this; }
        public Builder removeIfOutInDifferentWorld() { preview.persistenceTest = preview.persistenceTest.and((w, p) -> preview.dimension.equals(w.dimension())); return this; }
        public Builder andPersistOnlyIf(BiPredicate<Level, BlockPos> test) { preview.persistenceTest = preview.persistenceTest.and(test); return this; }
        public Builder showBar(Component headline) { preview.barText = headline; return this; }
        public StructurePreview buildAndSet() { StructurePreviewHandler.getInstance().setStructurePreview(preview); return preview; }
    }
}
