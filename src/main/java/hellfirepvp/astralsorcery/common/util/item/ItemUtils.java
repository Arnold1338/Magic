package hellfirepvp.astralsorcery.common.util.item;

import net.minecraftforge.items.ItemStackHandler;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import net.minecraft.core.Direction;
import net.minecraftforge.items.IItemHandlerModifiable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.integration.IntegrationBotania;
import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraftforge.items.CapabilityItemHandler;
import java.util.LinkedList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.tags.TagKey;
import java.util.Collections;
import net.minecraft.tags.ItemTags;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.item.Item;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.entity.EquipmentSlot;
import java.util.function.Supplier;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.level.entity.item.ItemEntity;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.level.Level;
import java.util.Random;
import net.minecraftforge.items.IItemHandler;

public class ItemUtils
{
    public static final IItemHandler EMPTY_INVENTORY;
    private static final Random rand;
    
    public static ItemEntity dropItem(final World world, final double x, final double y, final double z, final ItemStack stack) {
        if (world.isClientSide) {
            return null;
        }
        final ItemEntity ei = new ItemEntity(world, x, y, z, stack);
        ei.func_213317_d(new Vec3(0.0, 0.0, 0.0));
        world.func_217376_c((Entity)ei);
        ei.func_174867_a(20);
        return ei;
    }
    
    public static ItemEntity dropItemNaturally(final World world, final double x, final double y, final double z, final ItemStack stack) {
        if (world.isClientSide) {
            return null;
        }
        final ItemEntity ei = new ItemEntity(world, x, y, z, stack);
        applyRandomDropOffset(ei);
        world.func_217376_c((Entity)ei);
        ei.func_174867_a(20);
        return ei;
    }
    
    public static void decrementItem(final TileInventory inventory, final int slot, final Consumer<ItemStack> handleExcess) {
        decrementItem(() -> inventory.getStackInSlot(slot), stack -> inventory.setStackInSlot(slot, stack), handleExcess);
    }
    
    public static void decrementItem(final Supplier<ItemStack> getFromInventory, final Consumer<ItemStack> setIntoInventory, final Consumer<ItemStack> handleExcess) {
        ItemStack toConsume = getFromInventory.get();
        toConsume = copyStackWithSize(toConsume, toConsume.func_190916_E());
        ItemStack toReplaceWith = ItemStack.field_190927_a;
        if (toConsume.hasContainerItem()) {
            toReplaceWith = toConsume.getContainerItem();
        }
        toConsume.shrink(1);
        if (!toReplaceWith.isEmpty()) {
            if (toConsume.isEmpty()) {
                setIntoInventory.accept(toReplaceWith);
            }
            else if (ItemComparator.compare(toConsume, toReplaceWith, ItemComparator.Clause.Sets.ITEMSTACK_STRICT_NOAMOUNT)) {
                toReplaceWith.grow(toConsume.func_190916_E());
                if (toReplaceWith.func_190916_E() > toReplaceWith.func_77976_d()) {
                    final int overcapped = toReplaceWith.func_190916_E() - toReplaceWith.func_77976_d();
                    setIntoInventory.accept(copyStackWithSize(toReplaceWith, toReplaceWith.func_77976_d()));
                    handleExcess.accept(copyStackWithSize(toReplaceWith, overcapped));
                }
                else {
                    setIntoInventory.accept(toReplaceWith);
                }
            }
            else {
                handleExcess.accept(toReplaceWith);
            }
        }
        else {
            setIntoInventory.accept(toConsume);
        }
    }
    
    public static boolean isEquippableArmor(final Entity entity, final ItemStack stack) {
        for (final EquipmentSlot type : EquipmentSlot.values()) {
            if (type.func_188453_a() == EquipmentSlot.Group.ARMOR && stack.canEquip(type, entity)) {
                return true;
            }
        }
        return false;
    }
    
    public static ItemStack dropItemToPlayer(final Player player, final ItemStack stack) {
        final World world = player.func_130014_f_();
        if (world.func_201670_d() || stack.isEmpty()) {
            return stack;
        }
        final ItemEntity item = new ItemEntity(world, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), stack);
        if (item.func_92059_d().isEmpty()) {
            return stack;
        }
        item.func_174868_q();
        try {
            item.func_70100_b_(player);
        }
        catch (final Exception ex) {}
        if (item.isAlive()) {
            return item.func_92059_d().copy();
        }
        return ItemStack.field_190927_a;
    }
    
    private static void applyRandomDropOffset(final ItemEntity item) {
        item.func_213293_j(ItemUtils.rand.nextFloat() * 0.3f - 0.15, ItemUtils.rand.nextFloat() * 0.3f - 0.05, ItemUtils.rand.nextFloat() * 0.3f - 0.15);
    }
    
    @Nonnull
    public static ItemStack changeItem(@Nonnull final ItemStack stack, @Nonnull final Item item) {
        final CompoundTag nbt = stack.func_77955_b(new CompoundTag());
        nbt.putString("id", item.getRegistryName().toString());
        return ItemStack.func_199557_a(nbt);
    }
    
    @Nonnull
    public static ItemStack createBlockStack(final BlockState state) {
        return new ItemStack((ItemLike)state.getBlock());
    }
    
    @Nullable
    public static BlockState createBlockState(final ItemStack stack) {
        final Block b = Block.func_149634_a(stack.getItem());
        if (b == Blocks.field_150350_a) {
            return null;
        }
        return b.defaultBlockState();
    }
    
    @Nonnull
    public static List<ItemStack> getItemsOfTag(final ResourceLocation key) {
        final ITag<Item> tag = (ITag<Item>)ItemTags.func_199903_a().func_199910_a(key);
        return (tag == null) ? Collections.emptyList() : getItemsOfTag(tag);
    }
    
    @Nonnull
    public static List<ItemStack> getItemsOfTag(final ITag<Item> itemTag) {
        return (List)itemTag.func_230236_b_().stream().map(ItemStack::new).collect(Collectors.toList());
    }
    
    public static Collection<ItemStack> scanInventoryFor(final IItemHandler handler, final Item i) {
        final List<ItemStack> out = new LinkedList<ItemStack>();
        for (int j = 0; j < handler.getSlots(); ++j) {
            final ItemStack s = handler.getStackInSlot(j);
            if (!s.isEmpty() && s.getItem() == i) {
                out.add(copyStackWithSize(s, s.func_190916_E()));
            }
        }
        return out;
    }
    
    public static Collection<ItemStack> scanInventoryForMatching(final IItemHandler handler, final ItemStack match, final boolean strict) {
        return findItemsInInventory(handler, match, strict);
    }
    
    public static Collection<ItemStack> findItemsInPlayerInventory(final Player player, final ItemStack match, final boolean strict) {
        final IItemHandler handler = (IItemHandler)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse((Object)ItemUtils.EMPTY_INVENTORY);
        final Collection<ItemStack> results = findItemsInInventory(handler, match, strict);
        if (Mods.BOTANIA.isPresent()) {
            results.addAll(IntegrationBotania.findProvidersProvidingItems(player, match));
        }
        return results;
    }
    
    public static Collection<ItemStack> findItemsInInventory(final IItemHandler handler, final ItemStack match, final boolean strict) {
        final List<ItemStack> stacksOut = new LinkedList<ItemStack>();
        for (int j = 0; j < handler.getSlots(); ++j) {
            final ItemStack s = handler.getStackInSlot(j);
            if (strict) {
                if (!ItemComparator.compare(s, match, ItemComparator.Clause.ITEM, ItemComparator.Clause.NBT_STRICT, ItemComparator.Clause.CAPABILITIES_COMPATIBLE)) {
                    continue;
                }
            }
            else if (!ItemComparator.compare(s, match, ItemComparator.Clause.ITEM)) {
                continue;
            }
            stacksOut.add(copyStackWithSize(s, s.func_190916_E()));
        }
        return stacksOut;
    }
    
    public static Map<Integer, ItemStack> findItemsIndexedInPlayerInventory(final Player player, final Predicate<ItemStack> match) {
        return findItemsIndexedInInventory((IItemHandler)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse((Object)ItemUtils.EMPTY_INVENTORY), match);
    }
    
    public static Map<Integer, ItemStack> findItemsIndexedInInventory(final IItemHandler handler, final ItemStack match, final boolean strict) {
        return findItemsIndexedInInventory(handler, s -> {
            boolean b;
            if (strict) {
                b = ItemComparator.compare(s, match, ItemComparator.Clause.ITEM, ItemComparator.Clause.NBT_STRICT, ItemComparator.Clause.CAPABILITIES_COMPATIBLE);
            }
            else {
                b = ItemComparator.compare(s, match, ItemComparator.Clause.ITEM);
            }
            return b;
        });
    }
    
    public static Map<Integer, ItemStack> findItemsIndexedInInventory(final IItemHandler handler, final Predicate<ItemStack> match) {
        final Map<Integer, ItemStack> stacksOut = new HashMap<Integer, ItemStack>();
        for (int j = 0; j < handler.getSlots(); ++j) {
            final ItemStack s = handler.getStackInSlot(j);
            if (match.test(s)) {
                stacksOut.put(j, copyStackWithSize(s, s.func_190916_E()));
            }
        }
        return stacksOut;
    }
    
    public static boolean consumeFromPlayerInventory(final Player player, final ItemStack requestingItemStack, final ItemStack toConsume, final boolean simulate) {
        final int consumed = 0;
        final ItemStack tryConsume = copyStackWithSize(toConsume, toConsume.func_190916_E() - consumed);
        if (tryConsume.isEmpty()) {
            return true;
        }
        final IItemHandlerModifiable handler = (IItemHandlerModifiable)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (Direction)null).orElse((Object)ItemUtils.EMPTY_INVENTORY);
        return consumeFromInventory(handler, tryConsume, simulate) || (Mods.BOTANIA.isPresent() && IntegrationBotania.consumeFromPlayerInventory(player, requestingItemStack, toConsume, simulate));
    }
    
    public static boolean tryConsumeFromInventory(final IItemHandler handler, final ItemStack toConsume, final boolean simulate) {
        return handler instanceof IItemHandlerModifiable && consumeFromInventory((IItemHandlerModifiable)handler, toConsume, simulate);
    }
    
    public static boolean consumeFromInventory(final IItemHandlerModifiable handler, final ItemStack toConsume, final boolean simulate) {
        final Map<Integer, ItemStack> contents = findItemsIndexedInInventory((IItemHandler)handler, toConsume, false);
        if (contents.isEmpty()) {
            return false;
        }
        int cAmt = toConsume.func_190916_E();
        for (final int slot : contents.keySet()) {
            final ItemStack inSlot = contents.get(slot);
            final int toRemove = (cAmt > inSlot.func_190916_E()) ? inSlot.func_190916_E() : cAmt;
            cAmt -= toRemove;
            if (!simulate) {
                handler.setStackInSlot(slot, copyStackWithSize(inSlot, inSlot.func_190916_E() - toRemove));
            }
            if (cAmt <= 0) {
                break;
            }
        }
        return cAmt <= 0;
    }
    
    public static void dropInventory(final IItemHandler handle, final World worldIn, final BlockPos pos) {
        if (worldIn.isClientSide) {
            return;
        }
        for (int i = 0; i < handle.getSlots(); ++i) {
            final ItemStack stack = handle.getStackInSlot(i);
            if (!stack.isEmpty()) {
                dropItemNaturally(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            }
        }
    }
    
    public static void decrStackInInventory(final ItemStackHandler handler, final int slot) {
        if (slot < 0 || slot >= handler.getSlots()) {
            return;
        }
        final ItemStack st = handler.getStackInSlot(slot);
        if (st.isEmpty()) {
            return;
        }
        st.func_190920_e(st.func_190916_E() - 1);
        if (st.func_190916_E() <= 0) {
            handler.setStackInSlot(slot, ItemStack.field_190927_a);
        }
    }
    
    public static boolean tryPlaceItemInInventory(@Nonnull final ItemStack stack, final IItemHandler handler) {
        return tryPlaceItemInInventory(stack, handler, 0, handler.getSlots());
    }
    
    public static boolean tryPlaceItemInInventory(@Nonnull final ItemStack stack, final IItemHandler handler, final int start, final int end) {
        final ItemStack toAdd = stack.copy();
        if (!hasInventorySpace(toAdd, handler, start, end)) {
            return false;
        }
        final int max = stack.func_77976_d();
        for (int i = start; i < end; ++i) {
            final ItemStack in = handler.getStackInSlot(i);
            if (in.isEmpty()) {
                final int added = Math.min(stack.func_190916_E(), max);
                stack.func_190920_e(stack.func_190916_E() - added);
                handler.insertItem(i, copyStackWithSize(stack, added), false);
                return true;
            }
            if (ItemComparator.compare(stack, in, ItemComparator.Clause.ITEM, ItemComparator.Clause.NBT_STRICT, ItemComparator.Clause.CAPABILITIES_COMPATIBLE)) {
                final int space = max - in.func_190916_E();
                final int added2 = Math.min(stack.func_190916_E(), space);
                stack.func_190920_e(stack.func_190916_E() - added2);
                handler.getStackInSlot(i).func_190920_e(handler.getStackInSlot(i).func_190916_E() + added2);
                if (stack.func_190916_E() <= 0) {
                    return true;
                }
            }
        }
        return stack.func_190916_E() == 0;
    }
    
    public static boolean hasInventorySpace(@Nonnull final ItemStack stack, final IItemHandler handler, final int rangeMin, final int rangeMax) {
        int size = stack.func_190916_E();
        final int max = stack.func_77976_d();
        for (int i = rangeMin; i < rangeMax && size > 0; ++i) {
            final ItemStack in = handler.getStackInSlot(i);
            if (in.isEmpty()) {
                size -= max;
            }
            else if (ItemComparator.compare(stack, in, ItemComparator.Clause.ITEM, ItemComparator.Clause.NBT_STRICT, ItemComparator.Clause.CAPABILITIES_COMPATIBLE)) {
                final int space = max - in.func_190916_E();
                size -= space;
            }
        }
        return size <= 0;
    }
    
    public static ItemStack copyStackWithSize(@Nonnull final ItemStack stack, final int amount) {
        if (stack.isEmpty() || amount <= 0) {
            return ItemStack.field_190927_a;
        }
        final ItemStack s = stack.copy();
        s.func_190920_e(amount);
        return s;
    }
    
    static {
        EMPTY_INVENTORY = (IItemHandler)new ItemHandlerEmpty();
        rand = new Random();
    }
    
    private static class ItemHandlerEmpty implements IItemHandlerModifiable
    {
        public int getSlots() {
            return 0;
        }
        
        @Nonnull
        public ItemStack getStackInSlot(final int slot) {
            return ItemStack.field_190927_a;
        }
        
        @Nonnull
        public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
            return stack;
        }
        
        @Nonnull
        public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
            return ItemStack.field_190927_a;
        }
        
        public int getSlotLimit(final int slot) {
            return 64;
        }
        
        public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
            return false;
        }
        
        public void setStackInSlot(final int slot, @Nonnull final ItemStack stack) {
        }
    }
}
