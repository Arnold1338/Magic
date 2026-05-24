package hellfirepvp.astralsorcery.common.storage;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.item.ItemComparator;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.item.ItemStack;

public class StoredItemStack
{
    private final ItemStack stack;
    private int amount;
    
    StoredItemStack(final ItemStack stack) {
        this(stack, stack.func_190916_E());
    }
    
    private StoredItemStack(final ItemStack stack, final int amount) {
        this.stack = ItemUtils.copyStackWithSize(stack, 1);
        this.amount = amount;
    }
    
    public ItemStack getTemplateStack() {
        return ItemUtils.copyStackWithSize(this.stack, Math.min(this.stack.func_77976_d(), this.amount));
    }
    
    public boolean removeAmount(final int amount) {
        if (this.amount - amount < 0) {
            return false;
        }
        this.amount -= amount;
        return true;
    }
    
    public boolean isEmpty() {
        return this.amount <= 0;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public boolean combineIntoThis(final StoredItemStack other) {
        if (ItemComparator.compare(this.stack, other.stack, ItemComparator.Clause.Sets.ITEMSTACK_STRICT_NOAMOUNT)) {
            this.amount += other.amount;
            return true;
        }
        return false;
    }
    
    public boolean combineIntoThis(final ItemStack other) {
        if (ItemComparator.compare(this.stack, other, ItemComparator.Clause.Sets.ITEMSTACK_STRICT_NOAMOUNT)) {
            this.amount += other.func_190916_E();
            return true;
        }
        return false;
    }
    
    @Nonnull
    public CompoundTag serialize() {
        final CompoundTag tag = new CompoundTag();
        tag.put("item", (Tag)this.stack.serializeNBT());
        tag.putInt("amount", this.amount);
        return tag;
    }
    
    @Nullable
    public static StoredItemStack deserialize(final CompoundTag cmp) {
        final ItemStack stack = ItemStack.func_199557_a(cmp.func_74775_l("item"));
        if (stack.isEmpty()) {
            return null;
        }
        final int amount = cmp.getInt("amount");
        return new StoredItemStack(stack, amount);
    }
}
