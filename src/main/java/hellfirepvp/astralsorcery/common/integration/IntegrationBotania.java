package hellfirepvp.astralsorcery.common.integration;

import net.minecraft.world.level.item.Item;
import net.minecraft.world.level.level.block.Block;
import java.util.List;
import vazkii.botania.api.item.IBlockProvider;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.world.level.item.BlockItem;
import java.util.LinkedList;
import java.util.Collection;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.entity.player.Player;

public class IntegrationBotania
{
    public static Collection<ItemStack> findProvidersProvidingItems(final Player player, final ItemStack match) {
        final List<ItemStack> stacksOut = new LinkedList<ItemStack>();
        if (!(match.getItem() instanceof BlockItem)) {
            return stacksOut;
        }
        final Block matchBlock = ((BlockItem)match.getItem()).func_179223_d();
        final IItemHandler handler = (IItemHandler)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse((Object)ItemUtils.EMPTY_INVENTORY);
        for (int j = 0; j < handler.getSlots(); ++j) {
            final ItemStack s = handler.getStackInSlot(j);
            final Item sItem = s.getItem();
            if (sItem instanceof IBlockProvider) {
                final IBlockProvider provider = (IBlockProvider)sItem;
                int blockCount = provider.getBlockCount(player, ItemStack.field_190927_a, s, matchBlock);
                if (blockCount == -1) {
                    blockCount = 9001;
                }
                if (blockCount > 0) {
                    stacksOut.add(ItemUtils.copyStackWithSize(s, blockCount));
                }
            }
        }
        return stacksOut;
    }
    
    public static boolean consumeFromPlayerInventory(final Player player, final ItemStack requestingItemStack, final ItemStack toConsume, final boolean simulate) {
        if (!(toConsume.getItem() instanceof BlockItem)) {
            return false;
        }
        final Block consumeBlock = ((BlockItem)toConsume.getItem()).func_179223_d();
        final IItemHandler handler = (IItemHandler)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse((Object)ItemUtils.EMPTY_INVENTORY);
        for (int j = 0; j < handler.getSlots(); ++j) {
            final ItemStack s = handler.getStackInSlot(j);
            final Item sItem = s.getItem();
            if (sItem instanceof IBlockProvider) {
                final IBlockProvider provider = (IBlockProvider)sItem;
                final int blockCount = provider.getBlockCount(player, requestingItemStack, s, consumeBlock);
                if (blockCount == -1 || blockCount > toConsume.func_190916_E()) {
                    for (int i = 0; i < toConsume.func_190916_E(); ++i) {
                        if (!provider.provideBlock(player, requestingItemStack, s, consumeBlock, !simulate)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
