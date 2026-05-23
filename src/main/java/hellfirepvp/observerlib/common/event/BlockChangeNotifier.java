package hellfirepvp.observerlib.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockChangeNotifier {
    private static final List<Listener> listeners = new ArrayList<>();

    public static synchronized void addListener(Listener listener) { listeners.add(listener); }

    public static void onBlockChange(Level world, @Nullable LevelChunk chunk, BlockPos pos, BlockState oldS, BlockState newS) {
        if (chunk == null) chunk = world.getChunkAt(pos);
        for (Listener listener : listeners) listener.onChange(world, chunk, pos, oldS, newS);
    }

    public interface Listener {
        void onChange(Level world, LevelChunk chunk, BlockPos pos, BlockState oldState, BlockState newState);
    }
}
