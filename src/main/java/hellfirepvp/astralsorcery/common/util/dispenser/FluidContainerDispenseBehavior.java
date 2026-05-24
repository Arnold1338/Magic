package hellfirepvp.astralsorcery.common.util.dispenser;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nonnull;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.state.Property;
import net.minecraft.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;

public class FluidContainerDispenseBehavior extends DefaultDispenseItemBehavior
{
    private static final FluidContainerDispenseBehavior INSTANCE;
    private final DefaultDispenseItemBehavior defaultBehavior;
    
    private FluidContainerDispenseBehavior() {
        this.defaultBehavior = new DefaultDispenseItemBehavior();
    }
    
    public static FluidContainerDispenseBehavior getInstance() {
        return FluidContainerDispenseBehavior.INSTANCE;
    }
    
    protected ItemStack func_82487_b(final IBlockSource source, final ItemStack stack) {
        if (FluidUtil.getFluidContained(stack).isPresent()) {
            return this.dumpContainer(source, stack);
        }
        return this.fillContainer(source, stack);
    }
    
    @Nonnull
    private ItemStack fillContainer(final IBlockSource source, final ItemStack stack) {
        final World world = (World)source.func_197524_h();
        final Direction dispenserFacing = (Direction)source.func_189992_e().getValue((Property)DispenserBlock.field_176441_a);
        final BlockPos blockpos = source.func_180699_d().func_177972_a(dispenserFacing);
        final FluidActionResult actionResult = FluidUtil.tryPickUpFluid(stack, (Player)null, world, blockpos, dispenserFacing.func_176734_d());
        final ItemStack resultStack = actionResult.getResult();
        if (!actionResult.isSuccess() || resultStack.isEmpty()) {
            return super.func_82487_b(source, stack);
        }
        if (stack.func_190916_E() == 1) {
            return resultStack;
        }
        if (((DispenserTileEntity)source.func_150835_j()).func_146019_a(resultStack) < 0) {
            this.defaultBehavior.dispense(source, resultStack);
        }
        final ItemStack stackCopy = stack.copy();
        stackCopy.shrink(1);
        return stackCopy;
    }
    
    @Nonnull
    private ItemStack dumpContainer(final IBlockSource source, @Nonnull final ItemStack stack) {
        final ServerLevel world = source.func_197524_h();
        final ItemStack singleStack = stack.copy();
        singleStack.func_190920_e(1);
        final LazyOptional<IFluidHandlerItem> itemFluidHandler = (LazyOptional<IFluidHandlerItem>)FluidUtil.getFluidHandler(singleStack);
        if (!itemFluidHandler.isPresent()) {
            return super.func_82487_b(source, stack);
        }
        final FluidStack drained = itemFluidHandler.map(handler -> handler.drain(1000, IFluidHandler.FluidAction.EXECUTE)).orElse(FluidStack.EMPTY);
        final Direction dispenserFacing = (Direction)source.func_189992_e().getValue((Property)DispenserBlock.field_176441_a);
        final BlockPos pos = source.func_180699_d().func_177972_a(dispenserFacing);
        final Player player = (Player)AstralSorcery.getProxy().getASFakePlayerServer(world);
        final FluidActionResult result = FluidUtil.tryPlaceFluid(player, (World)source.func_197524_h(), InteractionHand.MAIN_HAND, pos, stack, drained);
        if (!result.isSuccess()) {
            return this.defaultBehavior.dispense(source, stack);
        }
        final ItemStack drainedStack = result.getResult();
        if (drainedStack.func_190916_E() == 1) {
            return drainedStack;
        }
        if (!drainedStack.isEmpty() && ((DispenserTileEntity)source.func_150835_j()).func_146019_a(drainedStack) < 0) {
            this.defaultBehavior.dispense(source, drainedStack);
        }
        final ItemStack stackCopy = drainedStack.copy();
        stackCopy.shrink(1);
        return stackCopy;
    }
    
    static {
        INSTANCE = new FluidContainerDispenseBehavior();
    }
}
