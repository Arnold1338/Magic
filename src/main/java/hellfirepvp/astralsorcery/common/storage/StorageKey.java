package hellfirepvp.astralsorcery.common.storage;

import javax.annotation.Nullable;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.item.Item;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.ItemStack;

public class StorageKey
{
    @Nonnull
    private final ItemStack stack;
    
    private StorageKey(@Nonnull final ItemStack stack) {
        this.stack = stack;
    }
    
    public static StorageKey from(final ItemStack stack) {
        return new StorageKey(stack);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StorageKey that = (StorageKey)o;
        final Item thisItem = this.stack.getItem();
        final Item thatItem = that.stack.getItem();
        return Objects.equals(thisItem.getRegistryName(), thatItem.getRegistryName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.stack.getItem().getRegistryName());
    }
    
    @Nonnull
    public CompoundTag serialize() {
        final CompoundTag keyTag = new CompoundTag();
        keyTag.putString("name", this.stack.getItem().getRegistryName().toString());
        return keyTag;
    }
    
    @Nullable
    public static StorageKey deserialize(final CompoundTag nbt) {
        final ResourceLocation rl = new ResourceLocation(nbt.getString("name"));
        final Item i = (Item)ForgeRegistries.ITEMS.getValue(rl);
        if (i == null || i == Items.field_190931_a) {
            return null;
        }
        return new StorageKey(new ItemStack((ItemLike)i));
    }
}
