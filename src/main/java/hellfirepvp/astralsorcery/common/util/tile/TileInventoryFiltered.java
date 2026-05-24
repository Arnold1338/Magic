package hellfirepvp.astralsorcery.common.util.tile;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.item.ItemStack;
import java.util.function.BiFunction;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import net.minecraft.core.Direction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;

public class TileInventoryFiltered extends TileInventory
{
    private InputFilter inputFilter;
    private ExtractFilter extractFilter;
    
    public TileInventoryFiltered(@Nonnull final TileEntitySynchronized tile, @Nonnull final Supplier<Integer> slotCountProvider, final Direction... applicableSides) {
        super(tile, slotCountProvider, applicableSides);
        this.inputFilter = null;
        this.extractFilter = null;
    }
    
    public TileInventoryFiltered(@Nonnull final TileEntitySynchronized tile, @Nonnull final Supplier<Integer> slotCountProvider, @Nullable final Consumer<Integer> changeListener, final Direction... applicableSides) {
        super(tile, slotCountProvider, changeListener, applicableSides);
        this.inputFilter = null;
        this.extractFilter = null;
    }
    
    protected TileInventoryFiltered(@Nonnull final TileEntitySynchronized tile, @Nonnull final Supplier<Integer> slotCountProvider, @Nullable final Consumer<Integer> changeListener, @Nonnull final Collection<Direction> applicableSides, @Nonnull final BiFunction<Integer, ItemStack, Integer> stackSizeLimiter) {
        super(tile, slotCountProvider, changeListener, applicableSides, stackSizeLimiter);
        this.inputFilter = null;
        this.extractFilter = null;
    }
    
    public TileInventoryFiltered canInsert(final InputFilter filter) {
        this.inputFilter = filter;
        return this;
    }
    
    public TileInventoryFiltered canExtract(final ExtractFilter filter) {
        this.extractFilter = filter;
        return this;
    }
    
    @Override
    protected TileInventoryFiltered makeNewInstance() {
        final TileInventoryFiltered inv = new TileInventoryFiltered(this.tile, this.slotCountProvider, this.changeListener, MiscUtils.copySet(this.applicableSides), this.stackSizeLimiter);
        inv.canInsert(this.inputFilter);
        inv.canExtract(this.extractFilter);
        return inv;
    }
    
    @Nonnull
    @Override
    public TileInventoryFiltered deserialize(final CompoundTag tag) {
        return (TileInventoryFiltered)super.deserialize(tag);
    }
    
    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
        if (!this.canInsertItem(slot, stack, this.getStackInSlot(slot))) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }
    
    @Nonnull
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
        if (!this.canExtractItem(slot, amount, this.getStackInSlot(slot))) {
            return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
    }
    
    public boolean canInsertItem(final int slot, final ItemStack toAdd) {
        return this.canInsertItem(slot, toAdd, this.getStackInSlot(slot));
    }
    
    private boolean canInsertItem(final int slot, final ItemStack toAdd, @Nonnull final ItemStack existing) {
        return this.inputFilter == null || this.inputFilter.canInsert(slot, toAdd, existing);
    }
    
    public boolean canExtractItem(final int slot, final int amount) {
        return this.canExtractItem(slot, amount, this.getStackInSlot(slot));
    }
    
    private boolean canExtractItem(final int slot, final int amount, @Nonnull final ItemStack existing) {
        return this.extractFilter == null || this.extractFilter.canExtract(slot, amount, existing);
    }
    
    public interface ExtractFilter
    {
        boolean canExtract(final int p0, final int p1, @Nonnull final ItemStack p2);
    }
    
    public interface InputFilter
    {
        boolean canInsert(final int p0, final ItemStack p1, @Nonnull final ItemStack p2);
    }
}
