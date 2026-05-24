package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.block.entity.BlockEntityType;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.level.ChunkPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.level.block.state.BlockState;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import net.minecraft.world.level.level.block.Block;
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
        final RegistryKey<World> dim = (RegistryKey<World>)tile.func_145831_w().dimension();
        final BlockEntityType<?> tileType = (BlockEntityType<?>)tile.func_200662_C();
        final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        return (world, pos, state) -> {
            if (loadTileWorldAndChunk || srv.forgeGetWorldMap().containsKey(dim)) {
                final World foundWorld = (World)srv.func_71218_a(dim);
                if (foundWorld == null) {
                    return !loadTileWorldAndChunk;
                }
                else {
                    if (!loadTileWorldAndChunk) {
                        if (!foundWorld.func_72863_F().func_222865_a(new ChunkPos(pos))) {
                            return true;
                        }
                    }
                    final BlockEntity te = MiscUtils.getTileAt((IBlockReader)foundWorld, pos, BlockEntity.class, true);
                    return te != null && te.func_200662_C().equals(tileType);
                }
            }
            else {
                return true;
            }
        };
    }
}
