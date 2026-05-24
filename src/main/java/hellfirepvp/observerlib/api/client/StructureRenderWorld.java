package hellfirepvp.observerlib.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.structure.Structure;
import hellfirepvp.observerlib.api.tile.MatchableTile;
import hellfirepvp.observerlib.client.util.ClientTickHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Stack;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class StructureRenderWorld implements BlockAndTintGetter {
    private static final int MAX_LIGHT = 15;
    private final Holder<Biome> globalBiome;
    private final Structure structure;
    private final Stack<Predicate<BlockPos>> blockFilter = new Stack<>();

    public StructureRenderWorld(Structure structure, Holder<Biome> globalBiome) {
        this.structure = structure;
        this.globalBiome = globalBiome;
    }

    public void pushContentFilter(@Nonnull Predicate<BlockPos> blockFilter) { this.blockFilter.push(blockFilter); }
    public void popContentFilter() { if (!this.blockFilter.isEmpty()) this.blockFilter.pop(); }

    private boolean allowAccess(BlockPos pos) {
        for (Predicate<BlockPos> filter : blockFilter) if (!filter.test(pos)) return false;
        return true;
    }

    @Nullable @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        if (!structure.hasBlockAt(pos) || !allowAccess(pos)) return null;
        MatchableState state = structure.getBlockStateAt(pos);
        BlockEntity tile = state.createBlockEntity(this, ClientTickHelper.getClientTick());
        if (tile == null) return null;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) tile.setLevel(mc.level);
        MatchableTile<?> tileMatch = structure.getTileEntityAt(pos);
        if (tileMatch == null) return tile;
        CompoundTag tag = new CompoundTag();
        ((MatchableTile) tileMatch).writeDisplayData(tile, ClientTickHelper.getClientTick(), tag);
        ((MatchableTile) tileMatch).postPlacement(tile, this, pos);
        return tile;
    }

    @Nonnull @Override
    public BlockState getBlockState(BlockPos pos) {
        if (!structure.hasBlockAt(pos) || !allowAccess(pos)) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        MatchableState state = structure.getContents().get(pos);
        return (state == null) ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()
            : state.getDescriptiveState(ClientTickHelper.getClientTick());
    }

    @Nonnull @Override
    public FluidState getFluidState(BlockPos pos) { return getBlockState(pos).getFluidState(); }

    @Override public float getShade(Direction direction, boolean b) { return 1.0f; }

    @Override
    public LevelLightEngine getLightEngine() {
        return Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getLightEngine() : null;
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver resolver) {
        return resolver.getColor(globalBiome.value(), pos.getX(), pos.getZ());
    }

    @Override public int getBrightness(LightLayer lightLayer, BlockPos pos) { return MAX_LIGHT; }
    @Override public int getRawBrightness(BlockPos pos, int amount) { return MAX_LIGHT; }
    @Override public int getHeight() { return 384; }
    @Override public int getMinBuildHeight() { return -64; }
}
