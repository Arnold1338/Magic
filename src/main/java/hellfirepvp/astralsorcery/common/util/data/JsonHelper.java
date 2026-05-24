package hellfirepvp.astralsorcery.common.util.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.awt.Color;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.world.level.level.material.Fluids;
import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import java.util.function.Function;
import java.util.function.Predicate;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import java.util.function.Consumer;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

public class JsonHelper
{
    private static final Gson GSON;
    
    public static void parseMultipleStrings(final JsonObject root, final String key, final Consumer<String> consumer) {
        consumeJsonListConfiguration(root, key, "String", "Strings", JsonElement::isJsonPrimitive, JsonElement::getAsString, consumer);
    }
    
    public static void parseMultipleJsonPrimitives(final JsonObject root, final String key, final String singular, final String plural, final Consumer<JsonPrimitive> consumer) {
        consumeJsonListConfiguration(root, key, singular, plural, JsonElement::isJsonPrimitive, JsonElement::getAsJsonPrimitive, consumer);
    }
    
    public static void parseMultipleJsonObjects(final JsonObject root, final String key, final Consumer<JsonObject> consumer) {
        consumeJsonListConfiguration(root, key, "JsonObject", "JsonObjects", JsonElement::isJsonObject, JsonElement::getAsJsonObject, consumer);
    }
    
    private static <T> void consumeJsonListConfiguration(final JsonObject root, final String key, final String singular, final String plural, final Predicate<JsonElement> verifier, final Function<JsonElement, T> consumerTransformer, final Consumer<T> consumer) {
        if (!root.has(key)) {
            throw new JsonSyntaxException(String.format("Expected '%s' to be a %s or an array of %s!", key, singular, plural));
        }
        final JsonElement el = root.get(key);
        if (verifier.test(el)) {
            consumer.accept(consumerTransformer.apply(el));
        }
        else {
            if (!el.isJsonArray()) {
                throw new JsonSyntaxException(String.format("Expected '%s' to be a %s or an array of %s!", key, singular, plural));
            }
            final JsonArray objectArray = el.getAsJsonArray();
            for (final JsonElement arrayEl : objectArray) {
                if (!verifier.test(arrayEl)) {
                    throw new JsonSyntaxException(String.format("Expected '%s' to be an array of %s!", key, plural));
                }
                consumer.accept(consumerTransformer.apply(arrayEl));
            }
        }
    }
    
    @Nonnull
    public static FluidStack getFluidStack(final JsonElement fluidElement, final String infoKey) {
        FluidStack fluidStack;
        if (fluidElement.isJsonPrimitive() && ((JsonPrimitive)fluidElement).isString()) {
            final String strKey = fluidElement.getAsString();
            final ResourceLocation fluidKey = new ResourceLocation(strKey);
            fluidStack = new FluidStack((Fluid)ForgeRegistries.FLUIDS.getValue(fluidKey), 1000);
        }
        else {
            if (!fluidElement.isJsonObject()) {
                throw new JsonSyntaxException("Missing " + infoKey + ", expected to find a string or object");
            }
            fluidStack = getFluidStack(fluidElement.getAsJsonObject(), true);
        }
        return fluidStack;
    }
    
    @Nonnull
    public static FluidStack getFluidStack(final JsonObject json, final boolean readNBT) {
        final String fluidName = JSONUtils.func_151200_h(json, "fluid");
        final Fluid fluid = (Fluid)ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
        if (fluid == null || fluid == Fluids.field_204541_a) {
            return FluidStack.EMPTY;
        }
        if (readNBT && json.has("nbt")) {
            try {
                final JsonElement element = json.get("nbt");
                CompoundTag nbt;
                if (element.isJsonObject()) {
                    nbt = JsonToNBT.func_180713_a(JsonHelper.GSON.toJson(element));
                }
                else {
                    nbt = JsonToNBT.func_180713_a(JSONUtils.func_151206_a(element, "nbt"));
                }
                final CompoundTag tempRead = new CompoundTag();
                tempRead.put("Tag", (Tag)nbt);
                tempRead.putString("FluidName", fluidName);
                tempRead.putInt("Amount", JSONUtils.func_151208_a(json, "amount", 1000));
                return FluidStack.loadFluidStackFromNBT(tempRead);
            }
            catch (final CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
            }
        }
        return new FluidStack(fluid, JSONUtils.func_151208_a(json, "amount", 1000));
    }
    
    @Nonnull
    public static ItemStack getItemStack(final JsonElement itemElement, final String infoKey) {
        ItemStack itemstack;
        if (itemElement.isJsonPrimitive() && ((JsonPrimitive)itemElement).isString()) {
            final String strKey = itemElement.getAsString();
            final ResourceLocation itemKey = new ResourceLocation(strKey);
            itemstack = new ItemStack((ItemLike)ForgeRegistries.ITEMS.getValue(itemKey));
        }
        else {
            if (!itemElement.isJsonObject()) {
                throw new JsonSyntaxException("Missing " + infoKey + ", expected to find a string or object");
            }
            itemstack = CraftingHelper.getItemStack(itemElement.getAsJsonObject(), true);
        }
        return itemstack;
    }
    
    @Nonnull
    public static ItemStack getItemStack(final JsonObject root, final String key) {
        if (!JSONUtils.func_151204_g(root, key)) {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a string or object");
        }
        ItemStack itemstack;
        if (root.get(key).isJsonObject()) {
            itemstack = CraftingHelper.getItemStack(JSONUtils.func_152754_s(root, key), true);
        }
        else {
            final String strKey = JSONUtils.func_151200_h(root, key);
            final ResourceLocation itemKey = new ResourceLocation(strKey);
            itemstack = new ItemStack((ItemLike)ForgeRegistries.ITEMS.getValue(itemKey));
        }
        return itemstack;
    }
    
    @Nonnull
    public static JsonObject serializeItemStack(final ItemStack stack) {
        final JsonObject object = new JsonObject();
        object.addProperty("item", stack.getItem().getRegistryName().toString());
        object.addProperty("count", (Number)stack.func_190916_E());
        if (stack.hasTag()) {
            object.addProperty("nbt", stack.getTag().toString());
        }
        return object;
    }
    
    public static Color getColor(final JsonObject object, final String key) {
        final String value = JSONUtils.func_151200_h(object, key);
        if (value.startsWith("0x")) {
            final String hexNbr = value.substring(2);
            try {
                return new Color(Integer.parseInt(hexNbr, 16), true);
            }
            catch (final NumberFormatException exc) {
                throw new JsonParseException("Expected " + hexNbr + " to be a hexadecimal string!", (Throwable)exc);
            }
        }
        try {
            return new Color(Integer.parseInt(value), true);
        }
        catch (final NumberFormatException exc2) {
            try {
                return new Color(Integer.parseInt(value, 16), true);
            }
            catch (final NumberFormatException e) {
                throw new JsonParseException("Expected " + value + " to be a int or hexadecimal-number!", (Throwable)e);
            }
        }
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }
}
