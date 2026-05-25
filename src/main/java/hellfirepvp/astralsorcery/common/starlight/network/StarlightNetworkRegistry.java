package hellfirepvp.astralsorcery.common.starlight.network;

import java.util.Random;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.starlight.network.handler.BlockTransmutationHandler;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightRecipient;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.List;

public class StarlightNetworkRegistry
{
    private static final List<IStarlightBlockHandler> blockHandlers;
    
    @Nullable
    public static IStarlightBlockHandler getStarlightHandler(final Level world, final BlockPos pos, final BlockState state, final IWeakConstellation cst) {
        final Block b = state.getBlock();
        if (b instanceof BlockStarlightRecipient) {
            return null;
        }
        for (final IStarlightBlockHandler handler : StarlightNetworkRegistry.blockHandlers) {
            if (handler.isApplicable(world, pos, state, cst)) {
                return handler;
            }
        }
        return null;
    }
    
    public static void registerBlockHandler(final IStarlightBlockHandler handler) {
        StarlightNetworkRegistry.blockHandlers.add(handler);
    }
    
    public static void setupRegistry() {
        registerBlockHandler(new BlockTransmutationHandler());
    }
    
    static {
        blockHandlers = new LinkedList<IStarlightBlockHandler>();
    }
    
    public interface IStarlightBlockHandler
    {
        boolean isApplicable(final Level p0, final BlockPos p1, final BlockState p2, final IWeakConstellation p3);
        
        void receiveStarlight(final Level p0, final Random p1, final BlockPos p2, final BlockState p3, final IWeakConstellation p4, final double p5);
    }
}
