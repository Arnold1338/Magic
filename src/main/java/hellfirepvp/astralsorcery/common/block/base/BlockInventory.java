package hellfirepvp.astralsorcery.common.block.base;

import net.minecraftforge.common.util.LazyOptional;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class BlockInventory extends BlockCrystalContainer
{
    protected BlockInventory(final AbstractBlock.Properties builder) {
        super(builder);
    }
    
    public void func_196243_a(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        final BlockEntity te = MiscUtils.getTileAt((IBlockReader)worldIn, pos, BlockEntity.class, true);
        if (te != null && !worldIn.isClientSide) {
            final LazyOptional<IItemHandler> opt = (LazyOptional<IItemHandler>)te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            if (opt.isPresent()) {
                ItemUtils.dropInventory((IItemHandler)opt.orElse((Object)ItemUtils.EMPTY_INVENTORY), worldIn, pos);
            }
        }
        super.func_196243_a(state, worldIn, pos, newState, isMoving);
    }
}
