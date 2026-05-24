package hellfirepvp.astralsorcery.common.util.nbt;

import net.minecraft.world.level.phys.AABB;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import java.util.UUID;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ObjectUtils;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.IForgeRegistryEntry;
import java.util.function.Consumer;
import java.util.Optional;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraft.world.level.level.block.state.BlockState;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.Iterator;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.LongArrayNBT;
import java.util.Collection;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.item.ItemStack;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.entity.Entity;

public class NBTHelper
{
    @Nonnull
    public static CompoundTag getPersistentData(final Entity entity) {
        return getPersistentData(entity.getPersistentData());
    }
    
    @Nonnull
    public static CompoundTag getPersistentData(final ItemStack item) {
        return getPersistentData(getData(item));
    }
    
    @Nonnull
    public static CompoundTag getPersistentData(final CompoundTag base) {
        CompoundTag compound;
        if (hasPersistentData(base)) {
            compound = base.func_74775_l("astralsorcery");
        }
        else {
            compound = new CompoundTag();
            base.put("astralsorcery", (Tag)compound);
        }
        return compound;
    }
    
    public static boolean hasPersistentData(final Entity entity) {
        return hasPersistentData(entity.getPersistentData());
    }
    
    public static boolean hasPersistentData(final ItemStack item) {
        return item.hasTag() && hasPersistentData(item.getTag());
    }
    
    public static boolean hasPersistentData(final CompoundTag base) {
        return base.contains("astralsorcery") && base.func_74781_a("astralsorcery") instanceof CompoundTag;
    }
    
    public static void removePersistentData(final Entity entity) {
        removePersistentData(entity.getPersistentData());
    }
    
    public static void removePersistentData(final ItemStack item) {
        if (item.hasTag()) {
            removePersistentData(item.getTag());
        }
    }
    
    public static void removePersistentData(final CompoundTag base) {
        base.func_82580_o("astralsorcery");
    }
    
    public static void deepMerge(final CompoundTag dst, final CompoundTag src, final boolean uniqueArrayEntries) {
        for (final String s : src.func_150296_c()) {
            final Tag nbtElement = src.func_74781_a(s);
            if (nbtElement.func_74732_a() == 10) {
                if (dst.func_150297_b(s, 10)) {
                    deepMerge(dst.func_74775_l(s), (CompoundTag)nbtElement, uniqueArrayEntries);
                }
                else {
                    dst.put(s, nbtElement.func_74737_b());
                }
            }
            else if (nbtElement.func_74732_a() == 9) {
                if (dst.func_150297_b(s, 9)) {
                    final ListTag dstList = (ListTag)dst.func_74781_a(s);
                    final ListTag srcList = (ListTag)nbtElement;
                    if (dstList.func_230528_d__() == srcList.func_230528_d__()) {
                        deepMergeList(dstList, srcList);
                    }
                    else {
                        dst.put(s, (Tag)srcList.func_74737_b());
                    }
                }
                else {
                    dst.put(s, nbtElement.func_74737_b());
                }
            }
            else if (nbtElement.func_74732_a() == 11) {
                if (dst.func_150297_b(s, 11)) {
                    final IntArrayNBT dstArr = (IntArrayNBT)dst.func_74781_a(s);
                    final IntArrayNBT srcArr = (IntArrayNBT)nbtElement;
                    if (uniqueArrayEntries) {
                        for (final IntTag element : srcArr) {
                            if (!dstArr.contains((Object)element)) {
                                dstArr.add((Object)element);
                            }
                        }
                    }
                    else {
                        dstArr.addAll((Collection)srcArr);
                    }
                }
                else {
                    dst.put(s, nbtElement.func_74737_b());
                }
            }
            else if (nbtElement.func_74732_a() == 12) {
                if (dst.func_150297_b(s, 12)) {
                    final LongArrayNBT dstArr2 = (LongArrayNBT)dst.func_74781_a(s);
                    final LongArrayNBT srcArr2 = (LongArrayNBT)nbtElement;
                    if (uniqueArrayEntries) {
                        for (final LongNBT element2 : srcArr2) {
                            if (!dstArr2.contains((Object)element2)) {
                                dstArr2.add((Object)element2);
                            }
                        }
                    }
                    else {
                        dstArr2.addAll((Collection)srcArr2);
                    }
                }
                else {
                    dst.put(s, nbtElement.func_74737_b());
                }
            }
            else if (nbtElement.func_74732_a() == 7) {
                if (dst.func_150297_b(s, 7)) {
                    final ByteArrayNBT dstArr3 = (ByteArrayNBT)dst.func_74781_a(s);
                    final ByteArrayNBT srcArr3 = (ByteArrayNBT)nbtElement;
                    if (uniqueArrayEntries) {
                        for (final ByteTag element3 : srcArr3) {
                            if (!dstArr3.contains((Object)element3)) {
                                dstArr3.add((Object)element3);
                            }
                        }
                    }
                    else {
                        dstArr3.addAll((Collection)srcArr3);
                    }
                }
                else {
                    dst.put(s, nbtElement.func_74737_b());
                }
            }
            else {
                dst.put(s, nbtElement.func_74737_b());
            }
        }
    }
    
    private static void deepMergeList(final ListTag dst, final ListTag src) {
        for (int j = 0; j < src.size(); ++j) {
            final Tag toAdd = src.get(j);
            boolean found = false;
            for (int i = 0; i < dst.size(); ++i) {
                final Tag existing = dst.get(i);
                if (existing.equals(toAdd)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                dst.add((Object)toAdd.func_74737_b());
            }
        }
    }
    
    @Nonnull
    public static <E, N extends Tag> List<E> readList(final CompoundTag nbt, final String key, final int type, final Function<N, E> deserializer) {
        if (!nbt.func_150297_b(key, 9)) {
            return new ArrayList<E>();
        }
        return readList(nbt.getList(key, type), deserializer);
    }
    
    @Nonnull
    public static <E, N extends Tag> List<E> readList(final ListTag nbt, final Function<N, E> deserializer) {
        return nbt.stream().map(n -> deserializer.apply(n)).collect((Collector<? super Object, ?, List<E>>)Collectors.toList());
    }
    
    @Nonnull
    public static <E, N extends Tag> Set<E> readSet(final CompoundTag nbt, final String key, final int type, final Function<N, E> deserializer) {
        if (!nbt.func_150297_b(key, 9)) {
            return new HashSet<E>();
        }
        return readSet(nbt.getList(key, type), deserializer);
    }
    
    @Nonnull
    public static <E, N extends Tag> Set<E> readSet(final ListTag nbt, final Function<N, E> deserializer) {
        return nbt.stream().map(n -> deserializer.apply(n)).collect((Collector<? super Object, ?, Set<E>>)Collectors.toSet());
    }
    
    public static <E> void writeList(final CompoundTag tag, final String key, final Collection<E> collection, final Function<E, Tag> serializer) {
        tag.put(key, (Tag)writeList(collection, serializer));
    }
    
    public static <E> ListTag writeList(final Collection<E> collection, final Function<E, Tag> serializer) {
        final ListTag nbt = new ListTag();
        nbt.addAll((Collection)collection.stream().map((Function<? super E, ?>)serializer).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return nbt;
    }
    
    public static CompoundTag getData(final ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound == null) {
            compound = new CompoundTag();
            stack.setTag(compound);
        }
        return compound;
    }
    
    public static <T> void writeOptional(final CompoundTag nbt, final String key, @Nullable final T object, final BiConsumer<CompoundTag, T> writer) {
        nbt.putBoolean(key + "_present", object != null);
        if (object != null) {
            final CompoundTag write = new CompoundTag();
            writer.accept(write, object);
            nbt.put(key, (Tag)write);
        }
    }
    
    @Nullable
    public static <T> T readOptional(final CompoundTag nbt, final String key, final Function<CompoundTag, T> reader) {
        return readOptional(nbt, key, reader, (T)null);
    }
    
    @Nullable
    public static <T> T readOptional(final CompoundTag nbt, final String key, final Function<CompoundTag, T> reader, final T _default) {
        if (nbt.getBoolean(key + "_present")) {
            final CompoundTag read = nbt.func_74775_l(key);
            return reader.apply(read);
        }
        return _default;
    }
    
    public static <T extends Enum<T>> void writeEnum(final CompoundTag nbt, final String key, final T enumValue) {
        nbt.putInt(key, enumValue.ordinal());
    }
    
    public static <T extends Enum<T>> T readEnum(final CompoundTag nbt, final String key, final Class<T> enumClazz) {
        if (!enumClazz.isEnum()) {
            throw new IllegalArgumentException("Passed class is not an enum!");
        }
        return enumClazz.getEnumConstants()[nbt.getInt(key)];
    }
    
    public static void setBlockState(final CompoundTag cmp, final String key, final BlockState state) {
        final CompoundTag serialized = getBlockStateNBTTag(state);
        cmp.put(key, (Tag)serialized);
    }
    
    @Nullable
    public static BlockState getBlockState(final CompoundTag cmp, final String key) {
        return getBlockStateFromTag(cmp.func_74775_l(key));
    }
    
    @Nonnull
    public static CompoundTag getBlockStateNBTTag(BlockState state) {
        if (state.getBlock().getRegistryName() == null) {
            state = Blocks.field_150350_a.defaultBlockState();
        }
        final CompoundTag tag = new CompoundTag();
        tag.putString("registryName", state.getBlock().getRegistryName().toString());
        final ListTag properties = new ListTag();
        for (final Property property : state.func_235904_r_()) {
            final CompoundTag propTag = new CompoundTag();
            try {
                propTag.putString("value", property.func_177702_a(state.getValue(property)));
            }
            catch (final Exception exc) {
                continue;
            }
            propTag.putString("property", property.func_177701_a());
            properties.add((Object)propTag);
        }
        tag.put("properties", (Tag)properties);
        return tag;
    }
    
    @Nullable
    public static BlockState getBlockStateFromTag(final CompoundTag cmp) {
        return getBlockStateFromTag(cmp, null);
    }
    
    @Nullable
    public static <T extends Comparable<T>> BlockState getBlockStateFromTag(final CompoundTag cmp, final BlockState _default) {
        final ResourceLocation key = new ResourceLocation(cmp.getString("registryName"));
        final Block block = (Block)ForgeRegistries.BLOCKS.getValue(key);
        if (block == null || block == Blocks.field_150350_a) {
            return _default;
        }
        BlockState state = block.defaultBlockState();
        final Collection<Property<?>> properties = state.func_235904_r_();
        final ListTag list = cmp.getList("properties", 10);
        for (int i = 0; i < list.size(); ++i) {
            final CompoundTag propertyTag = list.getCompound(i);
            final String valueStr = propertyTag.getString("value");
            final String propertyStr = propertyTag.getString("property");
            final Property<T> match = (Property<T>)MiscUtils.iterativeSearch(properties, prop -> prop.func_177701_a().equalsIgnoreCase(propertyStr));
            if (match != null) {
                try {
                    final Optional<T> opt = match.func_185929_b(valueStr);
                    if (opt.isPresent()) {
                        state = (BlockState)state.func_206870_a((Property)match, (Comparable)opt.get());
                    }
                }
                catch (final Throwable t) {}
            }
        }
        return state;
    }
    
    public static void setAsSubTag(final CompoundTag compound, final String tag, final Consumer<CompoundTag> applyFct) {
        final CompoundTag newTag = new CompoundTag();
        applyFct.accept(newTag);
        compound.put(tag, (Tag)newTag);
    }
    
    @Nullable
    public static <T> T readFromSubTag(final CompoundTag compound, final String tag, final Function<CompoundTag, T> readFct) {
        if (compound.func_150297_b(tag, 10)) {
            return readFct.apply(compound.func_74775_l(tag));
        }
        return null;
    }
    
    public static <T extends IForgeRegistryEntry<T>> void setRegistryEntry(final CompoundTag compoundNBT, final String tag, final T entry) {
        setResourceLocation(compoundNBT, tag + "_registry", RegistryManager.ACTIVE.getRegistry(entry.getRegistryType()).getRegistryName());
        setResourceLocation(compoundNBT, tag, entry.getRegistryName());
    }
    
    @Nullable
    public static <T extends IForgeRegistryEntry<T>> T getRegistryEntry(final CompoundTag compoundNBT, final String tag) {
        final ResourceLocation registryName = getResourceLocation(compoundNBT, tag + "_registry");
        if (registryName != null) {
            final ForgeRegistry<T> registry = (ForgeRegistry<T>)RegistryManager.ACTIVE.getRegistry(registryName);
            if (registry != null) {
                final ResourceLocation key = getResourceLocation(compoundNBT, tag);
                if (key != null) {
                    return (T)registry.getValue(key);
                }
            }
        }
        return null;
    }
    
    public static void setResourceLocation(final CompoundTag compoundNBT, final String tag, final ResourceLocation key) {
        compoundNBT.putString(tag, key.toString());
    }
    
    @Nullable
    public static ResourceLocation getResourceLocation(final CompoundTag compoundNBT, final String tag) {
        if (compoundNBT.contains(tag)) {
            return new ResourceLocation(compoundNBT.getString(tag));
        }
        return null;
    }
    
    public static void setStack(final CompoundTag compound, final String tag, final ItemStack stack) {
        setAsSubTag(compound, tag, stack::func_77955_b);
    }
    
    public static ItemStack getStack(final CompoundTag compound, final String tag) {
        return (ItemStack)ObjectUtils.firstNonNull((Object[])new ItemStack[] { readFromSubTag(compound, tag, ItemStack::func_199557_a), ItemStack.field_190927_a });
    }
    
    public static void setFluid(final CompoundTag compound, final String tag, final FluidStack stack) {
        setAsSubTag(compound, tag, stack::writeToNBT);
    }
    
    public static FluidStack getFluid(final CompoundTag compound, final String tag) {
        return (FluidStack)ObjectUtils.firstNonNull((Object[])new FluidStack[] { readFromSubTag(compound, tag, FluidStack::loadFluidStackFromNBT), FluidStack.EMPTY });
    }
    
    public static void removeUUID(final CompoundTag compound, final String key) {
        compound.func_82580_o(key);
    }
    
    public static UUID getUUID(final CompoundTag compoundNBT, final String key, final UUID _default) {
        if (compoundNBT.hasUUID(key)) {
            return compoundNBT.getUUID(key);
        }
        return _default;
    }
    
    public static CompoundTag writeBlockPosToNBT(final BlockPos pos, final CompoundTag compound) {
        compound.putInt("bposX", pos.getX());
        compound.putInt("bposY", pos.getY());
        compound.putInt("bposZ", pos.getZ());
        return compound;
    }
    
    public static BlockPos readBlockPosFromNBT(final CompoundTag compound) {
        final int x = compound.getInt("bposX");
        final int y = compound.getInt("bposY");
        final int z = compound.getInt("bposZ");
        return new BlockPos(x, y, z);
    }
    
    public static CompoundTag writeVector3(final Vector3 v) {
        final CompoundTag cmp = new CompoundTag();
        writeVector3(v, cmp);
        return cmp;
    }
    
    public static CompoundTag writeVector3(final Vector3 v, final CompoundTag compound) {
        compound.func_74780_a("vecPosX", v.getX());
        compound.func_74780_a("vecPosY", v.getY());
        compound.func_74780_a("vecPosZ", v.getZ());
        return compound;
    }
    
    public static Vector3 readVector3(final CompoundTag compound) {
        return new Vector3(compound.putDouble("vecPosX"), compound.putDouble("vecPosY"), compound.putDouble("vecPosZ"));
    }
    
    public static CompoundTag writeBoundingBox(final AABB box, final CompoundTag tag) {
        tag.func_74780_a("boxMinX", box.field_72340_a);
        tag.func_74780_a("boxMinY", box.field_72338_b);
        tag.func_74780_a("boxMinZ", box.field_72339_c);
        tag.func_74780_a("boxMaxX", box.field_72336_d);
        tag.func_74780_a("boxMaxY", box.field_72337_e);
        tag.func_74780_a("boxMaxZ", box.field_72334_f);
        return tag;
    }
    
    public static AABB readBoundingBox(final CompoundTag tag) {
        return new AABB(tag.putDouble("boxMinX"), tag.putDouble("boxMinY"), tag.putDouble("boxMinZ"), tag.putDouble("boxMaxX"), tag.putDouble("boxMaxY"), tag.putDouble("boxMaxZ"));
    }
}
