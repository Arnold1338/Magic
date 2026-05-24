package hellfirepvp.astralsorcery.common.util.tile;

import java.util.Iterator;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.common.capabilities.Capability;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.HashSet;
import java.util.Collection;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import net.minecraft.core.Direction;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import net.minecraft.world.level.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TileInventory extends ItemStackHandler implements Iterable<ItemStack>
{
    protected final TileEntitySynchronized tile;
    protected final Consumer<Integer> changeListener;
    protected final Supplier<Integer> slotCountProvider;
    protected Set<Direction> applicableSides;
    protected BiFunction<Integer, ItemStack, Integer> stackSizeLimiter;
    
    public TileInventory(@Nonnull final TileEntitySynchronized tile, @Nonnull final Supplier<Integer> slotCountProvider, final Direction... applicableSides) {
        this(tile, slotCountProvider, (Consumer<Integer>)null, applicableSides);
    }
    
    public TileInventory(@Nonnull final TileEntitySynchronized tile, @Nonnull final Supplier<Integer> slotCountProvider, @Nullable final Consumer<Integer> changeListener, final Direction... applicableSides) {
        this(tile, slotCountProvider, changeListener, (Collection<Direction>)Arrays.asList(applicableSides), (slot, stack) -> stack.func_77976_d());
    }
    
    protected TileInventory(@Nonnull final TileEntitySynchronized tile, @Nonnull final Supplier<Integer> slotCountProvider, @Nullable final Consumer<Integer> changeListener, @Nonnull final Collection<Direction> applicableSides, @Nonnull final BiFunction<Integer, ItemStack, Integer> stackSizeLimiter) {
        super((int)slotCountProvider.get());
        this.applicableSides = new HashSet<Direction>();
        this.tile = tile;
        this.changeListener = changeListener;
        this.slotCountProvider = slotCountProvider;
        this.applicableSides.addAll(applicableSides);
        this.stackSizeLimiter = stackSizeLimiter;
    }
    
    public TileInventory filterMaxStackSize(final BiFunction<Integer, ItemStack, Integer> stackSizeLimiter) {
        this.stackSizeLimiter = stackSizeLimiter;
        return this;
    }
    
    protected TileInventory makeNewInstance() {
        return new TileInventory(this.tile, this.slotCountProvider, this.changeListener, MiscUtils.copySet(this.applicableSides), this.stackSizeLimiter);
    }
    
    @Nonnull
    public TileInventory deserialize(final CompoundTag tag) {
        this.deserializeNBT(tag);
        if (this.getSlots() != this.slotCountProvider.get()) {
            final TileInventory newInv = this.makeNewInstance();
            for (int i = 0; i < Math.min(this.getSlots(), newInv.getSlots()); ++i) {
                ItemStack old = this.getStackInSlot(i);
                old = ItemUtils.copyStackWithSize(old, old.func_190916_E());
                newInv.setStackInSlot(i, old);
            }
            return newInv;
        }
        return this;
    }
    
    @Nonnull
    public CompoundTag serialize() {
        return this.serializeNBT();
    }
    
    @Nonnull
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
        final int insertable = this.stackSizeLimiter.apply(slot, stack);
        final int leftOver = stack.func_190916_E() - insertable;
        final ItemStack toInsert = ItemUtils.copyStackWithSize(stack, insertable);
        final ItemStack notInserted = super.insertItem(slot, toInsert, simulate);
        return ItemUtils.copyStackWithSize(toInsert, leftOver + notInserted.func_190916_E());
    }
    
    private boolean hasHandlerForSide(@Nullable final Direction facing) {
        return facing == null || this.applicableSides.contains(facing);
    }
    
    public boolean hasCapability(final Capability<?> capability, @Nullable final Direction facing) {
        return this.hasHandlerForSide(facing) && CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability;
    }
    
    public LazyOptional<TileInventory> getCapability() {
        return (LazyOptional<TileInventory>)LazyOptional.of(() -> this);
    }
    
    protected void onContentsChanged(final int slot) {
        super.onContentsChanged(slot);
        if (this.changeListener != null) {
            this.changeListener.accept(slot);
        }
        this.tile.markForUpdate();
    }
    
    public void clearInventory() {
        for (int i = 0; i < this.getSlots(); ++i) {
            this.setStackInSlot(i, ItemStack.field_190927_a);
            this.onContentsChanged(i);
        }
    }
    
    @Nonnull
    public Iterator<ItemStack> iterator() {
        return this.stacks.iterator();
    }
}
