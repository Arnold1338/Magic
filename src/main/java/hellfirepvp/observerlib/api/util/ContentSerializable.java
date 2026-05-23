package hellfirepvp.observerlib.api.util;

import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.client.util.ClientTickHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ContentSerializable {
    default List<ItemStack> getAsStacks(BlockGetter world, Player player) {
        List<ItemStack> out = new LinkedList<>();
        if (!(this instanceof BlockArray)) return out;
        BlockArray thisArray = (BlockArray) this;
        long tick = ClientTickHelper.getClientTick();
        for (Map.Entry<BlockPos, MatchableState> entry : thisArray.getContents().entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState sample = entry.getValue().getDescriptiveState(tick);
            ItemStack stack = ItemStack.EMPTY;
            if (!sample.getFluidState().isEmpty() && sample.getFluidState().isSource()) {
                stack = FluidUtil.getFilledBucket(new FluidStack(sample.getFluidState().getType(), 1000));
            }
            if (stack.isEmpty()) {
                try { stack = new ItemStack(sample.getBlock()); } catch (Exception ex) {}
            }
            if (!stack.isEmpty()) {
                ResourceLocation needle = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem());
                ItemStack existing = ItemStack.EMPTY;
                for (ItemStack i : out) {
                    if (net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(i.getItem()) != null
                        && net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(i.getItem()).equals(needle)) {
                        existing = i; break;
                    }
                }
                if (existing.isEmpty()) out.add(stack);
                else existing.setCount(existing.getCount() + stack.getCount());
            }
        }
        return out;
    }
}
