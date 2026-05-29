package hellfirepvp.astralsorcery.common.util.data;

import java.io.DataInput;
import java.io.InputStream;
import java.io.DataInputStream;
import io.netty.buffer.ByteBufInputStream;
import java.io.DataOutput;
import net.minecraft.nbt.NbtIo;
import java.io.OutputStream;
import java.io.DataOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraftforge.fluids.FluidStack;
import java.util.Optional;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSourceProvider;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryManager;

import java.nio.charset.StandardCharsets;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;

public class ByteBufUtils
{
    @Nullable
    public static <T> T readOptional(final FriendlyByteBuf buf, final Function<FriendlyByteBuf, T> readFct) {
        if (buf.readBoolean()) {
            return readFct.apply(buf);
        }
        return null;
    }
    
    public static <T> void writeOptional(final FriendlyByteBuf buf, @Nullable final T object, final BiConsumer<FriendlyByteBuf, T> applyFct) {
        writeOptional(buf, object, Function.identity(), (BiConsumer<FriendlyByteBuf, Object>)applyFct);
    }
    
    public static <T, R> void writeOptional(final FriendlyByteBuf buf, @Nullable final T object, final Function<T, R> converter, final BiConsumer<FriendlyByteBuf, R> applyFct) {
        buf.writeBoolean(object != null);
        if (object != null) {
            applyFct.accept(buf, converter.apply(object));
        }
    }
    
    public static void writeUUID(final FriendlyByteBuf buf, final UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }
    
    public static UUID readUUID(final FriendlyByteBuf buf) {
        return new UUID(buf.readLong(), buf.readLong());
    }
    
    public static <T> void writeCollection(final FriendlyByteBuf buf, @Nullable final Collection<T> list, final BiConsumer<FriendlyByteBuf, T> iterationFct) {
        if (list != null) {
            buf.writeInt(list.size());
            list.forEach(e -> iterationFct.accept(buf, e));
        }
        else {
            buf.writeInt(-1);
        }
    }
    
    @Nullable
    public static <T> List<T> readList(final FriendlyByteBuf buf, final Function<FriendlyByteBuf, T> readFct) {
        return readCollection(buf, ArrayList::new, List::add, readFct);
    }
    
    @Nullable
    public static <T> Set<T> readSet(final FriendlyByteBuf buf, final Function<FriendlyByteBuf, T> readFct) {
        return readCollection(buf, HashSet::new, Set::add, readFct);
    }
    
    @Nullable
    public static <T, C extends Collection<T>> C readCollection(final FriendlyByteBuf buf, final Supplier<C> newCollection, final BiConsumer<C, T> addFn, final Function<FriendlyByteBuf, T> readFct) {
        final int size = buf.readInt();
        if (size == -1) {
            return null;
        }
        final C collection = newCollection.get();
        for (int i = 0; i < size; ++i) {
            addFn.accept(collection, readFct.apply(buf));
        }
        return collection;
    }
    
    public static <K, V> void writeMap(final FriendlyByteBuf buf, @Nullable final Map<K, V> map, final BiConsumer<FriendlyByteBuf, K> keySerializer, final BiConsumer<FriendlyByteBuf, V> valueSerializer) {
        if (map != null) {
            buf.writeInt(map.size());
            for (final Map.Entry<K, V> entry : map.entrySet()) {
                keySerializer.accept(buf, entry.getKey());
                valueSerializer.accept(buf, entry.getValue());
            }
        }
        else {
            buf.writeInt(-1);
        }
    }
    
    @Nullable
    public static <K, V> Map<K, V> readMap(final FriendlyByteBuf buf, final Function<FriendlyByteBuf, K> readKey, final Function<FriendlyByteBuf, V> readValue) {
        final int size = buf.readInt();
        if (size == -1) {
            return null;
        }
        final Map<K, V> map = new HashMap<K, V>(size);
        for (int i = 0; i < size; ++i) {
            map.put(readKey.apply(buf), readValue.apply(buf));
        }
        return map;
    }
    
    public static void writeTextComponent(final FriendlyByteBuf buf, final Component cmp) {
        writeString(buf, Component.Serializer.func_150696_a(cmp));
    }
    
    public static MutableComponent readTextComponent(final FriendlyByteBuf buf) {
        return Component.Serializer.func_240643_a_(readString(buf));
    }
    
    public static void writeString(final FriendlyByteBuf buf, final String toWrite) {
        final byte[] str = toWrite.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(str.length);
        buf.writeBytes(str);
    }
    
    public static String readString(final FriendlyByteBuf buf) {
        final int length = buf.readInt();
        final byte[] strBytes = new byte[length];
        buf.readBytes(strBytes, 0, length);
        return new String(strBytes, StandardCharsets.UTF_8);
    }
    
    public static <T> void writeRegistryEntry(final FriendlyByteBuf buf, final Object<T> entry) {
        writeResourceLocation(buf, entry.getRegistryName());
        writeResourceLocation(buf, RegistryManager.ACTIVE.getRegistry(entry.getRegistryType()).getRegistryName());
    }
    
    public static <T> T readRegistryEntry(final FriendlyByteBuf buf) {
        final ResourceLocation entryName = readResourceLocation(buf);
        final ResourceLocation registryName = readResourceLocation(buf);
        return (T)RegistryManager.ACTIVE.getRegistry(registryName).getValue(entryName);
    }
    
    public static void writeVanillaRegistryEntry(final FriendlyByteBuf buf, final ResourceKey<?> key) {
        writeResourceLocation(buf, key.getRegistryName());
        writeResourceLocation(buf, key.func_240901_a_());
    }
    
    public static <T> ResourceKey<T> readVanillaRegistryEntry(final FriendlyByteBuf buf) {
        final ResourceLocation registryName = readResourceLocation(buf);
        return (ResourceKey<T>)ResourceKey.func_240903_a_(ResourceKey.create(registryName), readResourceLocation(buf));
    }
    
    public static void writeResourceLocation(final FriendlyByteBuf buf, final ResourceLocation key) {
        writeString(buf, key.toString());
    }
    
    public static ResourceLocation readResourceLocation(final FriendlyByteBuf buf) {
        return new ResourceLocation(readString(buf));
    }
    
    public static <T extends Enum<T>> void writeEnumValue(final FriendlyByteBuf buf, final T value) {
        buf.writeInt(value.ordinal());
    }
    
    public static <T extends Enum<T>> T readEnumValue(final FriendlyByteBuf buf, final Class<T> enumClazz) {
        if (!enumClazz.isEnum()) {
            throw new IllegalArgumentException("Passed class is not an enum!");
        }
        return enumClazz.getEnumConstants()[buf.readInt()];
    }
    
    public static void writeJsonObject(final FriendlyByteBuf buf, final JsonObject object) {
        writeString(buf, object.getString());
    }
    
    public static JsonObject readJsonObject(final FriendlyByteBuf buf) {
        return new JsonParser().parse(readString(buf)).getAsJsonObject();
    }
    
    public static void writeModifierSource(final FriendlyByteBuf buf, final ModifierSource source) {
        final ResourceLocation providerName = source.getProviderName();
        writeResourceLocation(buf, providerName);
        final ModifierSourceProvider provider = ModifierManager.getProvider(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown provider: " + providerName);
        }
        provider.serialize(source, buf);
    }
    
    public static ModifierSource readModifierSource(final FriendlyByteBuf buf) {
        final ResourceLocation providerName = readResourceLocation(buf);
        final ModifierSourceProvider<?> provider = ModifierManager.getProvider(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown provider: " + providerName);
        }
        return (ModifierSource)provider.deserialize(buf);
    }
    
    public static void writePos(final FriendlyByteBuf buf, final BlockPos pos) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }
    
    public static BlockPos readPos(final FriendlyByteBuf buf) {
        final int x = buf.readInt();
        final int y = buf.readInt();
        final int z = buf.readInt();
        return new BlockPos(x, y, z);
    }
    
    public static void writeVector(final FriendlyByteBuf buf, final Vector3 vec) {
        buf.writeDouble(vec.getX());
        buf.writeDouble(vec.getY());
        buf.writeDouble(vec.getZ());
    }
    
    public static Vector3 readVector(final FriendlyByteBuf buf) {
        final double x = buf.readDouble();
        final double y = buf.readDouble();
        final double z = buf.readDouble();
        return new Vector3(x, y, z);
    }
    
    public static void writeItemStack(final FriendlyByteBuf byteBuf, @Nonnull final ItemStack stack) {
        final boolean defined = !stack.isEmpty();
        byteBuf.writeBoolean(defined);
        if (defined) {
            final CompoundTag tag = new CompoundTag();
            stack.func_77955_b(tag);
            writeNBTTag(byteBuf, tag);
        }
    }
    
    @Nonnull
    public static ItemStack readItemStack(final FriendlyByteBuf byteBuf) {
        final boolean defined = byteBuf.readBoolean();
        if (defined) {
            return ItemStack.func_199557_a(readNBTTag(byteBuf));
        }
        return ItemStack.EMPTY;
    }
    
    public static void writeBlockState(final FriendlyByteBuf byteBuf, @Nonnull final BlockState state) {
        writeRegistryEntry(byteBuf, (net.minecraftforge.registries.Object<Object>)state.getBlock());
        final Collection<Property<?>> properties = state.func_235904_r_();
        byteBuf.writeInt(properties.size());
        for (final Property prop : properties) {
            writeString(byteBuf, prop.func_177701_a());
            writeString(byteBuf, prop.func_177702_a(state.getValue(prop)));
        }
    }
    
    public static <T extends Comparable<T>> BlockState readBlockState(final FriendlyByteBuf byteBuf) {
        final Block block = readRegistryEntry(byteBuf);
        BlockState state = block.defaultBlockState();
        for (int properties = byteBuf.readInt(), i = 0; i < properties; ++i) {
            final String propName = readString(byteBuf);
            final String valueStr = readString(byteBuf);
            final Property<T> property = (Property<T>)MiscUtils.iterativeSearch(state.func_235904_r_(), prop -> prop.func_177701_a().equalsIgnoreCase(propName));
            if (property != null) {
                final Optional<T> value = property.func_185929_b(valueStr);
                if (value.isPresent()) {
                    state = (BlockState)state.setValue((Property)property, (Comparable)value.get());
                }
            }
        }
        return state;
    }
    
    public static void writeFluidStack(final FriendlyByteBuf byteBuf, @Nonnull final FluidStack stack) {
        stack.writeToPacket(byteBuf);
    }
    
    @Nonnull
    public static FluidStack readFluidStack(final FriendlyByteBuf byteBuf) {
        return FluidStack.readFromPacket(byteBuf);
    }
    
    public static void writeNBTTag(final FriendlyByteBuf byteBuf, @Nonnull final CompoundTag tag) {
        try (final DataOutputStream dos = new DataOutputStream((OutputStream)new ByteBufOutputStream((ByteBuf)byteBuf))) {
            NbtIo.func_74800_a(tag, (DataOutput)dos);
        }
        catch (final Exception ex) {}
    }
    
    @Nonnull
    public static CompoundTag readNBTTag(final FriendlyByteBuf byteBuf) {
        try (final DataInputStream dis = new DataInputStream((InputStream)new ByteBufInputStream((ByteBuf)byteBuf))) {
            return NbtIo.func_74794_a((DataInput)dis);
        }
        catch (final Exception ex) {
            throw new IllegalStateException("Could not load NBT Tag from incoming byte buffer!");
        }
    }
}
