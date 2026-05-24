package hellfirepvp.astralsorcery.common.auxiliary;

import net.minecraft.world.level.block.state.BlockState;
import java.util.Collection;
import java.util.Optional;
import java.util.Iterator;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.tile.TileChalice;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import hellfirepvp.astralsorcery.common.block.tile.BlockFountain;
import hellfirepvp.astralsorcery.common.block.tile.BlockChalice;
import net.minecraft.util.math.MathHelper;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ChaliceHelper
{
    @Nonnull
    public static List<BlockPos> findNearbyChalices(final World world, final BlockPos origin, final int distance) {
        final Vector3 thisVector = new Vector3((Vector3i)origin).add(0.5, 1.5, 0.5);
        final List<BlockPos> foundChalices = BlockDiscoverer.searchForBlocksAround(world, origin, MathHelper.func_76125_a(distance, 0, 16), (w, pos, state) -> !pos.equals((Object)origin) && state.getBlock() instanceof BlockChalice && !w.func_175640_z(pos) && !(w.getBlockState(pos.func_177977_b()).getBlock() instanceof BlockFountain));
        foundChalices.removeIf(pos -> {
            final Vector3 chaliceVector = new Vector3((Vector3i)pos).add(0.5, 1.5, 0.5);
            final RaytraceAssist assist = new RaytraceAssist(thisVector, chaliceVector);
            return !assist.isClear(world);
        });
        return foundChalices;
    }
    
    @Nonnull
    public static List<TileChalice> findNearbyChalicesContaining(final World world, final BlockPos origin, final FluidStack expected, final int distance) {
        final List<TileChalice> out = new LinkedList<TileChalice>();
        for (final BlockPos chalicePos : findNearbyChalices(world, origin, distance)) {
            final TileChalice chalice = MiscUtils.getTileAt((IBlockReader)world, chalicePos, TileChalice.class, true);
            if (chalice != null && chalice.getTank().drain(expected, IFluidHandler.FluidAction.SIMULATE).getAmount() >= expected.getAmount()) {
                out.add(chalice);
            }
        }
        return out;
    }
    
    @Nonnull
    public static Optional<List<TileChalice>> findNearbyChalicesCombined(final World world, final BlockPos origin, final FluidStack expected, final int distance) {
        final FluidStack required = expected.copy();
        final List<TileChalice> out = new LinkedList<TileChalice>();
        for (final BlockPos chalicePos : findNearbyChalices(world, origin, distance)) {
            final TileChalice chalice = MiscUtils.getTileAt((IBlockReader)world, chalicePos, TileChalice.class, true);
            if (chalice != null) {
                final FluidStack drained = chalice.getTank().drain(expected, IFluidHandler.FluidAction.SIMULATE);
                if (drained.isEmpty()) {
                    continue;
                }
                required.shrink(drained.getAmount());
                out.add(chalice);
            }
        }
        if (required.isEmpty()) {
            return Optional.of(out);
        }
        return Optional.empty();
    }
    
    public static boolean doChalicesContainCombined(final World world, final Collection<BlockPos> chalicePositions, final FluidStack expected) {
        final FluidStack required = expected.copy();
        for (final BlockPos pos : chalicePositions) {
            final TileChalice chalice = MiscUtils.getTileAt((IBlockReader)world, pos, TileChalice.class, true);
            if (chalice != null) {
                final FluidStack drained = chalice.getTank().drain(expected, IFluidHandler.FluidAction.SIMULATE);
                if (drained.isEmpty()) {
                    continue;
                }
                required.shrink(drained.getAmount());
            }
        }
        return required.isEmpty();
    }
}
