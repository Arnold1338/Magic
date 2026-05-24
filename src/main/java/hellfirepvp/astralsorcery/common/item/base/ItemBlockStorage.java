package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.HashMap;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.item.ItemStack;
import java.util.Random;

public interface ItemBlockStorage
{
    public static final Random random = new Random();
    
    default boolean storeBlockState(final ItemStack stack, final World world, final BlockPos pos) {
        if (MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, true) != null) {
            return false;
        }
        final BlockState state = world.getBlockState(pos);
        if (state.isAir((IBlockReader)world, pos) || state.func_185887_b((IBlockReader)world, pos) == -1.0f || ItemUtils.createBlockStack(state).isEmpty()) {
            return false;
        }
        final CompoundTag persistent = NBTHelper.getPersistentData(stack);
        final ListTag stored = persistent.getList("storedStates", 10);
        stored.add((Object)NBTHelper.getBlockStateNBTTag(state));
        persistent.put("storedStates", (Tag)stored);
        return true;
    }
    
    default void clearContainerFor(final Player player) {
        final Tuple<Hand, ItemStack> held = MiscUtils.getMainOrOffHand((LivingEntity)player, stack -> stack.getItem() instanceof ItemBlockStorage);
        if (held != null) {
            NBTHelper.getPersistentData((ItemStack)held.func_76340_b()).func_82580_o("storedStates");
        }
    }
    
    @Nonnull
    default List<Tuple<ItemStack, Integer>> getInventoryMatchingItemStacks(final Player player, final ItemStack referenceContainer) {
        final Map<BlockState, Tuple<ItemStack, Integer>> storedStates = getInventoryMatching(player, referenceContainer);
        final List<Tuple<ItemStack, Integer>> foundStacks = new ArrayList<Tuple<ItemStack, Integer>>(storedStates.values());
        foundStacks.sort(Comparator.comparing(tpl -> ((ItemStack)tpl.func_76341_a()).getItem().getRegistryName()));
        return foundStacks;
    }
    
    @Nonnull
    default Map<BlockState, Tuple<ItemStack, Integer>> getInventoryMatching(final Player player, final ItemStack referenceContainer) {
        final Map<BlockState, ItemStack> mappedStacks = getMappedStoredStates(referenceContainer);
        final Map<BlockState, Tuple<ItemStack, Integer>> foundContents = new HashMap<BlockState, Tuple<ItemStack, Integer>>();
        for (final BlockState state : mappedStacks.keySet()) {
            final ItemStack stored = mappedStacks.get(state);
            int countDisplay = 0;
            final Collection<ItemStack> stacks = ItemUtils.findItemsInPlayerInventory(player, stored, true);
            for (final ItemStack found : stacks) {
                countDisplay += found.func_190916_E();
            }
            foundContents.put(state, (Tuple<ItemStack, Integer>)new Tuple((Object)stored.copy(), (Object)countDisplay));
        }
        return foundContents;
    }
    
    @Nonnull
    default Map<BlockState, ItemStack> getMappedStoredStates(final ItemStack referenceContainer) {
        final List<BlockState> blockStates = (List<BlockState>)getStoredStates(referenceContainer);
        final Map<BlockState, ItemStack> map = new LinkedHashMap<BlockState, ItemStack>();
        for (final BlockState state : blockStates) {
            final ItemStack stack = ItemUtils.createBlockStack(state);
            if (!stack.isEmpty()) {
                map.put(state, stack);
            }
        }
        return map;
    }
    
    @Nonnull
    default NonNullList<BlockState> getStoredStates(final ItemStack referenceContainer) {
        final NonNullList<BlockState> states = (NonNullList<BlockState>)NonNullList.func_191196_a();
        if (!referenceContainer.isEmpty() && referenceContainer.getItem() instanceof ItemBlockStorage) {
            final CompoundTag persistent = NBTHelper.getPersistentData(referenceContainer);
            final ListTag stored = persistent.getList("storedStates", 10);
            for (int i = 0; i < stored.size(); ++i) {
                final BlockState state = NBTHelper.getBlockStateFromTag(stored.getCompound(i));
                if (state != null) {
                    states.add((Object)state);
                }
            }
        }
        return states;
    }
    
    default Random getPreviewRandomFromWorld(final World world) {
        long tempSeed = 7508891506429673237L;
        tempSeed *= world.getDayTime() / 40L << 8;
        return new Random(tempSeed);
    }
}
