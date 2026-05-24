package hellfirepvp.observerlib.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NBTHelper {
    @Nonnull
    public static CompoundTag getBlockStateNBTTag(BlockState state) {
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (rl == null) state = Blocks.AIR.defaultBlockState();
        final CompoundTag tag = new CompoundTag();
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        tag.putString("registryName", key != null ? key.toString() : "minecraft:air");
        final ListTag properties = new ListTag();
        for (Property<?> property : state.getProperties()) {
            final CompoundTag propTag = new CompoundTag();
            try { propTag.putString("value", getName(property, state)); }
            catch (Exception exc) { continue; }
            propTag.putString("property", property.getName());
            properties.add(propTag);
        }
        tag.put("properties", properties);
        return tag;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getName(Property<T> property, BlockState state) {
        return property.getName(state.getValue(property));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> BlockState getBlockStateFromTag(final CompoundTag cmp, final BlockState _default) {
        final ResourceLocation key = ResourceLocation.tryParse(cmp.getString("registryName"));
        if (key == null) return _default;
        final Block block = ForgeRegistries.BLOCKS.getValue(key);
        if (block == null || block == Blocks.AIR) return _default;
        BlockState state = block.defaultBlockState();
        final Collection<Property<?>> properties = state.getProperties();
        final ListTag list = cmp.getList("properties", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            final CompoundTag propertyTag = list.getCompound(i);
            final String valueStr = propertyTag.getString("value");
            final String propertyStr = propertyTag.getString("property");
            final Property<T> match = (Property<T>) iterativeSearch(properties, prop -> prop.getName().equalsIgnoreCase(propertyStr));
            if (match != null) {
                try {
                    final Optional<T> opt = match.getValue(valueStr);
                    if (opt.isPresent()) state = state.setValue(match, opt.get());
                } catch (Throwable t) {}
            }
        }
        return state;
    }

    @Nullable
    private static <T> T iterativeSearch(Collection<T> collection, Predicate<T> matchingFct) {
        for (T element : collection) { if (matchingFct.test(element)) return element; }
        return null;
    }

    public static void setAsSubTag(CompoundTag compound, String tag, Consumer<CompoundTag> applyFct) {
        final CompoundTag newTag = new CompoundTag();
        applyFct.accept(newTag);
        compound.put(tag, newTag);
    }

    public static void writeBlockPosToNBT(BlockPos pos, CompoundTag compound) {
        compound.putInt("bposX", pos.getX());
        compound.putInt("bposY", pos.getY());
        compound.putInt("bposZ", pos.getZ());
    }

    public static BlockPos readBlockPosFromNBT(CompoundTag compound) {
        return new BlockPos(compound.getInt("bposX"), compound.getInt("bposY"), compound.getInt("bposZ"));
    }
}
