package hellfirepvp.astralsorcery.common.event.handler;

import net.minecraft.world.level.level.block.Block;
import hellfirepvp.astralsorcery.common.block.tile.BlockAltar;
import net.minecraft.world.level.level.block.Blocks;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import net.minecraft.world.level.level.chunk.ChunkStatus;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.chunk.LevelChunk;
import net.minecraft.world.level.level.Level;
import hellfirepvp.observerlib.common.event.BlockChangeNotifier;

public class EventHandlerAutoLink implements BlockChangeNotifier.Listener
{
    public void onChange(final World world, final Chunk chunk, final BlockPos pos, final BlockState oldState, final BlockState newState) {
        if (world.func_201670_d() || !chunk.func_201589_g().func_209003_a(ChunkStatus.field_222617_m)) {
            return;
        }
        final Block oldB = oldState.getBlock();
        final Block newB = newState.getBlock();
        if (oldB != newB) {
            final WorldNetworkHandler handle = WorldNetworkHandler.getNetworkHandler(world);
            handle.informBlockChange(pos);
            if (oldB == Blocks.field_150462_ai) {
                handle.removeAutoLinkTo(pos);
            }
            if (newB == Blocks.field_150462_ai) {
                handle.attemptAutoLinkTo(pos);
            }
            if (oldB instanceof BlockAltar) {
                handle.removeAutoLinkTo(pos);
            }
            if (newB instanceof BlockAltar) {
                handle.attemptAutoLinkTo(pos);
            }
        }
    }
}
