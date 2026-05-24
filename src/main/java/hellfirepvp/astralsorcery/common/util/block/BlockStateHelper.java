package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.world.level.block.AirBlock;
import com.google.gson.JsonObject;
import java.util.Optional;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Locale;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Collection;
import net.minecraft.world.level.block.state.Property;
import java.util.ArrayList;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nonnull;
import net.minecraft.world.level.block.Block;
import com.google.common.base.Splitter;

public class BlockStateHelper
{
    private static final Splitter PROP_SPLITTER;
    private static final Splitter PROP_ELEMENT_SPLITTER;
    
    @Nonnull
    public static String serialize(@Nonnull final Block block) {
        return block.getRegistryName().toString();
    }
    
    @Nonnull
    public static <V extends Comparable<V>> String serialize(@Nonnull final BlockState state) {
        final StringBuilder name = new StringBuilder(state.getBlock().getRegistryName().toString());
        final List<Property<?>> props = new ArrayList<Property<?>>(state.func_235904_r_());
        if (!props.isEmpty()) {
            name.append('[');
            for (int i = 0; i < props.size(); ++i) {
                final Property<V> prop = (Property<V>)props.get(i);
                if (i > 0) {
                    name.append(',');
                }
                name.append(prop.func_177701_a());
                name.append('=');
                name.append(prop.func_177702_a(state.getValue((Property)prop)));
            }
            name.append(']');
        }
        return name.toString();
    }
    
    @Nonnull
    public static <V extends Comparable<V>> JsonObject serializeObject(final BlockState state, final boolean serializeProperties) {
        final JsonObject object = new JsonObject();
        serializeObject(object, state, serializeProperties);
        return object;
    }
    
    public static <V extends Comparable<V>> void serializeObject(final JsonObject out, final BlockState state, final boolean serializeProperties) {
        out.addProperty("block", state.getBlock().getRegistryName().toString());
        if (serializeProperties && !state.func_235904_r_().isEmpty()) {
            final JsonArray properties = new JsonArray();
            for (final Property<V> prop : state.func_235904_r_()) {
                final Property<?> property = prop;
                final JsonObject objProperty = new JsonObject();
                objProperty.addProperty("name", prop.func_177701_a());
                objProperty.addProperty("value", prop.func_177702_a(state.getValue((Property)prop)));
                properties.add((JsonElement)objProperty);
            }
            out.add("properties", (JsonElement)properties);
        }
    }
    
    @Nonnull
    public static Block deserializeBlock(@Nonnull final String serialized) {
        final Block block = (Block)ForgeRegistries.BLOCKS.getValue(new ResourceLocation(serialized));
        return (block == null) ? Blocks.field_150350_a : block;
    }
    
    @Nonnull
    public static <T extends Comparable<T>> BlockState deserialize(@Nonnull final String serialized) {
        final int propIndex = serialized.indexOf(91);
        final boolean hasProperties = !isMissingStateInformation(serialized);
        ResourceLocation key;
        if (hasProperties) {
            key = new ResourceLocation(serialized.substring(0, propIndex).toLowerCase(Locale.ROOT));
        }
        else {
            key = new ResourceLocation(serialized.toLowerCase(Locale.ROOT));
        }
        final Block block = (Block)ForgeRegistries.BLOCKS.getValue(key);
        BlockState state = block.defaultBlockState();
        if (!block.equals(Blocks.field_150350_a) && hasProperties) {
            final List<String> strProps = BlockStateHelper.PROP_SPLITTER.splitToList((CharSequence)serialized.substring(propIndex, serialized.length() - 1));
            for (final String serializedProperty : strProps) {
                final List<String> propertyValues = BlockStateHelper.PROP_ELEMENT_SPLITTER.splitToList((CharSequence)serializedProperty);
                final String name = propertyValues.get(0);
                final String strValue = propertyValues.get(1);
                final Property<T> property = (Property<T>)MiscUtils.iterativeSearch(state.func_235904_r_(), prop -> prop.func_177701_a().equalsIgnoreCase(name));
                if (property != null) {
                    final Optional<T> value = property.func_185929_b(strValue);
                    if (!value.isPresent()) {
                        continue;
                    }
                    state = (BlockState)state.func_206870_a((Property)property, (Comparable)value.get());
                }
            }
        }
        return state;
    }
    
    @Nonnull
    public static <T extends Comparable<T>> BlockState deserializeObject(final JsonObject object) {
        final String key = JSONUtils.func_151200_h(object, "block");
        final Block b = (Block)ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key));
        if (b == null || b instanceof AirBlock) {
            return Blocks.field_150350_a.defaultBlockState();
        }
        BlockState state = b.defaultBlockState();
        if (isMissingStateInformation(object)) {
            return state;
        }
        if (JSONUtils.func_151204_g(object, "properties")) {
            final JsonArray properties = JSONUtils.func_151214_t(object, "properties");
            for (final JsonElement elemProperty : properties) {
                final JsonObject objProperty = JSONUtils.func_151210_l(elemProperty, "properties[?]");
                final String propName = JSONUtils.func_151200_h(objProperty, "name");
                final Property<T> property = (Property<T>)MiscUtils.iterativeSearch(state.func_235904_r_(), prop -> prop.func_177701_a().equalsIgnoreCase(propName));
                if (property != null) {
                    final String propValue = JSONUtils.func_151200_h(objProperty, "value");
                    final Optional<T> value = property.func_185929_b(propValue);
                    if (!value.isPresent()) {
                        continue;
                    }
                    state = (BlockState)state.func_206870_a((Property)property, (Comparable)value.get());
                }
            }
        }
        return state;
    }
    
    public static boolean isMissingStateInformation(@Nonnull final JsonObject serialized) {
        return serialized.has("properties");
    }
    
    public static boolean isMissingStateInformation(@Nonnull final String serialized) {
        return serialized.indexOf(91) == -1;
    }
    
    static {
        PROP_SPLITTER = Splitter.on(',');
        PROP_ELEMENT_SPLITTER = Splitter.on('=');
    }
}
