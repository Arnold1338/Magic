package hellfirepvp.astralsorcery.common.crafting.recipe;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.RecipeCraftingContext;

public class BlockTransmutationContext extends RecipeCraftingContext<BlockTransmutation, IItemHandler>
{
    private final IWorld world;
    private final BlockPos pos;
    private final BlockState state;
    private final IWeakConstellation constellation;
    
    public BlockTransmutationContext(final IWorld world, final BlockPos pos, final BlockState state, final IWeakConstellation constellation) {
        this.world = world;
        this.pos = pos;
        this.state = state;
        this.constellation = constellation;
    }
    
    public IWorld getWorld() {
        return this.world;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public BlockState getState() {
        return this.state;
    }
    
    public IWeakConstellation getConstellation() {
        return this.constellation;
    }
}
