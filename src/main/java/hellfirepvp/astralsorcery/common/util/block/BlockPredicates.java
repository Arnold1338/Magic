package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

public class BlockPredicates
{
    public static BlockPredicate isInTag(final ITag<Block> blockTag) {
        return (world, pos, state) -> state.func_235714_a_(blockTag);
    }
    
    public static BlockPredicate isBlock(final Block... blocks) {
        final Set<Block> applicable = new HashSet<Block>(Arrays.asList(blocks));
        return (world, pos, state) -> applicable.contains(state.getBlock());
    }
    
    public static BlockPredicate isState(final BlockState... states) {
        final Set<BlockState> applicable = new HashSet<BlockState>(Arrays.asList(states));
        return (world, pos, state) -> applicable.contains(state);
    }
    
    public static <T extends BlockEntity> BlockPredicate doesTileExist(final T tile, final boolean loadTileWorldAndChunk) {
        final RegistryKey<Level> dim = (RegistryKey<Level>)tile.getLevel().dimension();
        final BlockEntityType<?> tileType = (BlockEntityType<?>)tile.getType();
        final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        return (world, pos, state) -> {
            if (loadTileWorldAndChunk || srv.forgeGetWorldMap().containsKey(dim)) {
                final Level foundWorld = (Level)srv.getLevel(dim);
                if (foundWorld == null) {
                    return !loadTileWorldAndChunk;
                }
                else {
                    if (!loadTileWorldAndChunk) {
                        if (!foundWorld.getChunkSource().func_222865_a(new ChunkPos(pos))) {
                            return true;
                        }
                    }
                    final BlockEntity te = MiscUtils.getTileAt((IBlockReader)foundWorld, pos, BlockEntity.class, true);
                    return te != null && te.getType().equals(tileType);
                }
            }
            else {
                return true;
            }
        };
    }
}
