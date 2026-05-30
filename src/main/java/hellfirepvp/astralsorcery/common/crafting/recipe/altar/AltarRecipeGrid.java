package hellfirepvp.astralsorcery.common.crafting.recipe.altar;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.FluidIngredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.LinkedList;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.world.item.ItemStack;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.Map;
import java.util.regex.Pattern;

public class AltarRecipeGrid
{
    public static final int GRID_SIZE = 5;
    public static final int MAX_INVENTORY_SIZE = 25;
    public static final AltarRecipeGrid EMPTY;
    private static final Pattern SKIP_CHARS;
    private final Map<Integer, Ingredient> gridParts;
    private final int width;
    private final int height;
    
    private AltarRecipeGrid(final Map<Integer, Ingredient> gridParts) {
        this(new AltarRecipeGrid(gridParts, 5, 5));
    }
    
    private AltarRecipeGrid(final AltarRecipeGrid other) {
        this(other.gridParts, other.width, other.height);
    }
    
    private AltarRecipeGrid(final Map<Integer, Ingredient> gridParts, final int width, final int height) {
        this.gridParts = gridParts;
        this.width = width;
        this.height = height;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public boolean containsInputs(final IItemHandlerModifiable itemHandler, final boolean testMirrored) {
        for (int xx = 0; xx <= 5 - this.width; ++xx) {
            for (int zz = 0; zz <= 5 - this.height; ++zz) {
                if (this.matches(itemHandler, xx, zz, false)) {
                    return true;
                }
                if (testMirrored && this.matches(itemHandler, xx, zz, true)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Ingredient getIngredient(final int index) {
        return this.gridParts.getOrDefault(index, Ingredient.field_193370_a);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    private boolean matches(final IItemHandlerModifiable itemHandler, final int xOffset, final int zOffset, final boolean mirrored) {
        final Set<Integer> matchedItems = new HashSet<Integer>();
        final int totalOffset = zOffset * 5 + xOffset;
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.height; ++z) {
                int index = x + z * 5;
                if (mirrored) {
                    index = this.width - x - 1 + z * 5;
                }
                final Ingredient expected = this.getIngredient(index);
                final int slot = index + totalOffset;
                final ItemStack contained = itemHandler.getStackInSlot(slot);
                if (!expected.test(contained)) {
                    return false;
                }
                matchedItems.add(slot);
            }
        }
        return this.isGridEmpty(itemHandler, matchedItems);
    }
    
    private boolean isGridEmpty(final IItemHandlerModifiable inventory, final Collection<Integer> skipSlots) {
        for (int x = 0; x < 5; ++x) {
            for (int z = 0; z < 5; ++z) {
                final int slot = x + z * 5;
                if (!skipSlots.contains(slot) && !inventory.getStackInSlot(slot).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void validate(final AltarType type) {
        if (this.gridParts.isEmpty()) {
            throw new IllegalArgumentException("Altar recipe grid cannot be empty!");
        }
        for (final Integer index : this.gridParts.keySet()) {
            if (!type.hasSlot(index)) {
                throw new IllegalArgumentException("Altar type " + type.name() + " has no slot at " + index);
            }
            final Ingredient input = this.gridParts.get(index);
            if (input.func_203189_d()) {
                throw new IllegalArgumentException("Input at " + index + " has no matching items!");
            }
        }
    }
    
    public void write(final FriendlyByteBuf buffer) {
        buffer.writeInt(this.width);
        buffer.writeInt(this.height);
        buffer.writeInt(this.gridParts.size());
        this.gridParts.forEach((key, value) -> {
            buffer.writeInt((int)key);
            value.func_199564_a(buffer);
        });
    }
    
    public static AltarRecipeGrid read(final FriendlyByteBuf buffer) {
        final int width = buffer.readInt();
        final int height = buffer.readInt();
        final int gridParts = buffer.readInt();
        final Map<Integer, Ingredient> ingredientMap = new HashMap<Integer, Ingredient>();
        for (int i = 0; i < gridParts; ++i) {
            final int slot = buffer.readInt();
            final Ingredient ingredient = Ingredient.func_199566_b(buffer);
            ingredientMap.put(slot, ingredient);
        }
        return new AltarRecipeGrid(ingredientMap, width, height);
    }
    
    public void serialize(final JsonObject object) {
        final JsonArray pattern = new JsonArray();
        final JsonObject keys = new JsonObject();
        final Map<JsonElement, String> revMap = new HashMap<JsonElement, String>();
        final Map<String, JsonElement> ingredientMap = new HashMap<String, JsonElement>();
        final Map<Integer, String> patternMap = new HashMap<Integer, String>();
        char c = 'A';
        for (final Map.Entry<Integer, Ingredient> entry : this.gridParts.entrySet()) {
            final Integer slotIndex = entry.getKey();
            final Ingredient value = entry.getValue();
            final JsonElement jsonIngredient = value.func_200304_c();
            if (!revMap.containsKey(jsonIngredient)) {
                final String strKey = String.valueOf(c);
                revMap.put(jsonIngredient, strKey);
                patternMap.put(slotIndex, strKey);
                ingredientMap.put(strKey, jsonIngredient);
                ++c;
            }
            else {
                patternMap.put(slotIndex, revMap.get(jsonIngredient));
            }
        }
        for (int xx = 0; xx < 5; ++xx) {
            final StringBuilder line = new StringBuilder();
            for (int zz = 0; zz < 5; ++zz) {
                final int slotIndex2 = xx * 5 + zz;
                line.append(patternMap.getOrDefault(slotIndex2, "_"));
            }
            pattern.add(line.toString());
        }
        object.add("pattern", (JsonElement)pattern);
        ingredientMap.forEach((key, ingredient) -> keys.add(String.valueOf(key), ingredient));
        object.add("key", (JsonElement)keys);
    }
    
    public static AltarRecipeGrid deserialize(final AltarType type, final JsonObject json) throws JsonSyntaxException {
        final JsonArray pattern = JSONUtils.func_151214_t(json, "pattern");
        final JsonObject keys = JSONUtils.func_152754_s(json, "key");
        final Map<Integer, Character> patternMap = new HashMap<Integer, Character>();
        final Set<Character> usedChars = new HashSet<Character>();
        for (int i = 0; i < 25; ++i) {
            patternMap.put(i, '_');
        }
        for (int i = 0; i < Math.min(pattern.size(), 5); ++i) {
            final String str = JSONUtils.func_151206_a(pattern.get(i), String.format("pattern[%s]", i));
            if (str.length() > 5) {
                throw new JsonSyntaxException("Invalid pattern: too many columns, 5 is maximum");
            }
            final char[] charArray = str.toCharArray();
            for (int j = 0; j < charArray.length; ++j) {
                final char c = charArray[j];
                final String strChar = String.valueOf(c);
                if (!AltarRecipeGrid.SKIP_CHARS.matcher(strChar).matches()) {
                    usedChars.add(c);
                    patternMap.put(i * 5 + j, c);
                }
            }
        }
        final Map<Integer, Ingredient> mappedIngredients = new HashMap<Integer, Ingredient>();
        for (final Map.Entry<String, JsonElement> jEntry : keys.entrySet()) {
            final String key = jEntry.getKey();
            if (key.length() != 1) {
                throw new JsonSyntaxException("Invalid Key: '" + key + "'! Keys must only be a single character!");
            }
            final char c = key.charAt(0);
            if (AltarRecipeGrid.SKIP_CHARS.matcher(String.valueOf(c)).matches()) {

            }
            if (!usedChars.contains(c)) {
                throw new JsonSyntaxException("Invalid Key: '" + key + "'! Not used in the pattern map!");
            }
            final Ingredient k = Ingredient.func_199802_a((JsonElement)jEntry.getValue());
            for (int index = 0; index < 25; ++index) {
                if (patternMap.get(index) == c) {
                    mappedIngredients.put(index, k);
                }
            }
            usedChars.remove(c);
        }
        if (!usedChars.isEmpty()) {
            throw new JsonSyntaxException("The following keys are used in the pattern but don't have a key associated with them: " + usedChars);
        }
        if (mappedIngredients.isEmpty()) {
            throw new JsonSyntaxException("Empty recipe found. At least one input must be specified!");
        }
        for (final Integer slot : mappedIngredients.keySet()) {
            if (!type.hasSlot(slot)) {
                throw new JsonSyntaxException("Slot " + slot + " has an ingredient but cannot be used in altar type " + type.name());
            }
        }
        return new AltarRecipeGrid(mappedIngredients);
    }
    
    static {
        EMPTY = new AltarRecipeGrid(new HashMap<Integer, Ingredient>(), 0, 0);
        SKIP_CHARS = Pattern.compile("^\\s|_|#$");
    }
    
    public static class Builder
    {
        private final LinkedList<String> pattern;
        private final Map<Character, Ingredient> inputMapping;
        
        public Builder() {
            this.pattern = Lists.newLinkedList();
            this.inputMapping = Maps.newHashMap();
        }
        
        public Builder patternLine(final String line) {
            if (line.length() > 5) {
                throw new IllegalArgumentException("Altar recipe pattern line must not be more than 5 characters long! Passed line '" + line + "'");
            }
            if (this.pattern.size() >= 5) {
                throw new IllegalArgumentException("Altar recipe pattern must not have more than 5 lines total!");
            }
            this.pattern.add(line);
            return this;
        }
        
        public Builder key(final Character key, final ITag.INamedTag<Item> tagIn) {
            return this.key(key, Ingredient.func_199805_a((ITag)tagIn));
        }
        
        public Builder key(final Character key, final ItemLike itemIn) {
            return this.key(key, Ingredient.func_199804_a(new ItemLike[] { itemIn }));
        }
        
        public Builder key(final Character key, final Fluid fluid) {
            return this.key(key, new FluidIngredient(new FluidStack[] { new FluidStack(fluid, 1000) }));
        }
        
        public Builder key(final Character key, final Ingredient input) {
            if (this.inputMapping.containsKey(key)) {
                throw new IllegalArgumentException("Character '" + key + "' is already defined!");
            }
            if (key.equals(' ') || key.equals('_')) {
                throw new IllegalArgumentException("Character ' ' (whitespace) or '_' (underscore) is reserved and cannot be defined!");
            }
            this.inputMapping.put(key, input);
            return this;
        }
        
        public AltarRecipeGrid build() {
            final int mostWidth = this.pattern.stream().map((Function<? super Object, ? extends Integer>)String::length).max(Integer::compareTo).orElseThrow(() -> new IllegalArgumentException("No pattern is defined for altar recipe!"));
            final int mostHeight = (int)this.pattern.stream().filter(s -> !s.isEmpty()).count();
            if (mostHeight == 0 || mostWidth == 0) {
                throw new IllegalArgumentException("Altar recipe grid pattern is empty!");
            }
            final int shiftZ = (5 - mostHeight) / 2;
            for (int i = 0; i < shiftZ; ++i) {
                this.pattern.addFirst(StringUtils.repeat('_', 5));
            }
            for (int i = 0; i < 5 - mostHeight - shiftZ; ++i) {
                this.pattern.add(StringUtils.repeat('_', 5));
            }
            final List<String> patternLines = new LinkedList<String>();
            final int shiftX = (5 - mostWidth) / 2;
            for (final String line : this.pattern) {
                final String newLine = StringUtils.repeat("_", shiftX) + line + StringUtils.repeat("_", 5 - mostWidth - shiftX);
                patternLines.add(newLine);
            }
            final HashSet<Character> foundCharacters = new HashSet<Character>();
            for (final String line2 : patternLines) {
                for (final char c : line2.toCharArray()) {
                    if (!AltarRecipeGrid.SKIP_CHARS.matcher(String.valueOf(c)).matches()) {
                        foundCharacters.add(c);
                    }
                }
            }
            if (!this.inputMapping.keySet().containsAll(foundCharacters)) {
                final String missingCharacters = foundCharacters.stream().filter(key -> !this.inputMapping.containsKey(key)).map((Function<? super Object, ?>)String::valueOf).collect((Collector<? super Object, ?, String>)Collectors.joining(", "));
                throw new IllegalArgumentException("No matching input found for characters " + missingCharacters);
            }
            final Map<Integer, Ingredient> ingredientMap = new HashMap<Integer, Ingredient>();
            for (int lineIndex = 0; lineIndex < patternLines.size(); ++lineIndex) {
                final char[] charArray = patternLines.get(lineIndex).toCharArray();
                for (int cIndex = 0; cIndex < charArray.length; ++cIndex) {
                    final Character c2 = charArray[cIndex];
                    if (!AltarRecipeGrid.SKIP_CHARS.matcher(String.valueOf(c2)).matches()) {
                        final int slotIndex = lineIndex * 5 + cIndex;
                        ingredientMap.put(slotIndex, this.inputMapping.get(c2));
                    }
                }
            }
            return new AltarRecipeGrid(ingredientMap, null);
        }
    }
}
