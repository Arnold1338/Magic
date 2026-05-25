package hellfirepvp.astralsorcery.common.storage;

import java.util.ArrayList;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.IItemHandler;
import java.util.Collection;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.Lists;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class StorageCache
{
    private final Map<StorageKey, List<StoredItemStack>> content;
    
    public StorageCache() {
        this.content = Maps.newHashMap();
    }
    
    public int getTotalItemCount() {
        int i = 0;
        for (final List<StoredItemStack> stacks : this.content.values()) {
            for (final StoredItemStack stack : stacks) {
                i += stack.getAmount();
            }
        }
        return i;
    }
    
    public int getItemCount(final StorageKey key) {
        int i = 0;
        for (final StoredItemStack s : this.content.getOrDefault(key, Lists.newArrayList())) {
            i += s.getAmount();
        }
        return i;
    }
    
    public boolean add(final ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        final StorageKey key = StorageKey.from(stack);
        for (final StoredItemStack s : this.content.computeIfAbsent(key, stKey -> Lists.newArrayList())) {
            if (s.combineIntoThis(stack)) {
                return true;
            }
        }
        this.content.get(key).add(new StoredItemStack(stack));
        return true;
    }
    
    private void mergeIntoThis(final StorageCache otherStorage) {
        for (final StorageKey otherKey : otherStorage.content.keySet()) {
            final List<StoredItemStack> thisStorage = this.content.computeIfAbsent(otherKey, stKey -> Lists.newArrayList());
            final List<StoredItemStack> notMerged = Lists.newArrayList();
        Label_0079:
            for (final StoredItemStack toMerge : otherStorage.content.get(otherKey)) {
                for (final StoredItemStack thisItem : thisStorage) {
                    if (thisItem.combineIntoThis(toMerge)) {
                        continue Label_0079;
                    }
                }
                notMerged.add(toMerge);
            }
            thisStorage.addAll(notMerged);
        }
    }
    
    public boolean attemptTransferInto(final StorageKey key, final IItemHandler inv, final int slot, final boolean simulate) {
        if (this.content.isEmpty()) {
            return false;
        }
        final List<StoredItemStack> stacks = this.content.get(key);
        if (stacks == null || stacks.isEmpty()) {
            return false;
        }
        for (final StoredItemStack stack : stacks) {
            final ItemStack sample = stack.getTemplateStack();
            final int amountToRemove = sample.getCount();
            final ItemStack notInserted = inv.insertItem(slot, sample, simulate);
            final int addedCount = amountToRemove - notInserted.getCount();
            if (addedCount > 0) {
                if (!simulate) {
                    if (!stack.removeAmount(addedCount)) {
                        return false;
                    }
                    if (stack.isEmpty()) {
                        stacks.remove(stack);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean attemptTransferInto(final StorageKey key, final IItemHandler inv, final boolean simulate) {
        if (this.content.isEmpty()) {
            return false;
        }
        final List<StoredItemStack> stacks = this.content.get(key);
        if (stacks == null || stacks.isEmpty()) {
            return false;
        }
        boolean change = false;
        for (int i = 0; i < inv.getSlots(); ++i) {
            for (final StoredItemStack stack : stacks) {
                final ItemStack sample = stack.getTemplateStack();
                final int amountToRemove = sample.getCount();
                final ItemStack notInserted = inv.insertItem(i, sample, simulate);
                final int addedCount = amountToRemove - notInserted.getCount();
                if (addedCount > 0) {
                    change = true;
                }
                if (!simulate) {
                    if (!stack.removeAmount(addedCount)) {
                        return false;
                    }
                    if (stack.isEmpty()) {
                        stacks.remove(stack);
                        break;
                    }
                    continue;
                }
            }
        }
        return change;
    }
    
    public void writeToNBT(final CompoundTag tag) {
        final ListTag content = new ListTag();
        for (final StorageKey key : this.content.keySet()) {
            final CompoundTag itemStorage = new CompoundTag();
            itemStorage.put("storageKey", (Tag)key.serialize());
            final ListTag items = new ListTag();
            for (final StoredItemStack stack : this.content.get(key)) {
                items.add((Object)stack.serialize());
            }
            itemStorage.put("items", (Tag)items);
        }
        tag.put("content", (Tag)content);
    }
    
    public void readFromNBT(final CompoundTag tag) {
        this.content.clear();
        final ListTag content = tag.getList("content", 10);
        for (int i = 0; i < content.size(); ++i) {
            final CompoundTag itemStorage = content.getCompound(i);
            final StorageKey key = StorageKey.deserialize(itemStorage.func_74775_l("storageKey"));
            if (key != null) {
                final ListTag items = itemStorage.getList("items", 10);
                final List<StoredItemStack> stacks = new ArrayList<StoredItemStack>(items.size());
                for (int j = 0; j < items.size(); ++j) {
                    final StoredItemStack stack = StoredItemStack.deserialize(items.getCompound(j));
                    if (stack != null) {
                        stacks.add(stack);
                    }
                }
                this.content.put(key, stacks);
            }
        }
    }
}
