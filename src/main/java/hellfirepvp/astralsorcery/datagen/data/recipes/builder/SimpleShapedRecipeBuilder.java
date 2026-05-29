package hellfirepvp.astralsorcery.datagen.data.recipes.builder;

import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.RecipeSerializer;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.Map;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public class SimpleShapedRecipeBuilder
{
    private final ItemStack result;
    private final List<String> pattern;
    private final Map<Character, Ingredient> key;
    private String subDirectory;
    
    private SimpleShapedRecipeBuilder(final ItemLike result, final int count) {
        this(new ItemStack((ItemLike)result.func_199767_j(), count));
    }
    
    private SimpleShapedRecipeBuilder(final ItemStack result) {
        this.pattern = Lists.newArrayList();
        this.key = Maps.newLinkedHashMap();
        this.subDirectory = null;
        this.result = result.copy();
    }
    
    public static SimpleShapedRecipeBuilder shapedRecipe(final ItemLike result) {
        return shapedRecipe(result, 1);
    }
    
    public static SimpleShapedRecipeBuilder shapedRecipe(final ItemLike result, final int count) {
        return new SimpleShapedRecipeBuilder(result, count);
    }
    
    public SimpleShapedRecipeBuilder key(final Character symbol, final ITag.INamedTag<Item> tag) {
        return this.key(symbol, Ingredient.func_199805_a((ITag)tag));
    }
    
    public SimpleShapedRecipeBuilder key(final Character symbol, final ItemLike item) {
        return this.key(symbol, Ingredient.func_199804_a(new ItemLike[] { item }));
    }
    
    public SimpleShapedRecipeBuilder key(final Character symbol, final Ingredient ingredientIn) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        }
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.key.put(symbol, ingredientIn);
        return this;
    }
    
    public SimpleShapedRecipeBuilder patternLine(final String patternIn) {
        if (!this.pattern.isEmpty() && patternIn.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        this.pattern.add(patternIn);
        return this;
    }
    
    public SimpleShapedRecipeBuilder subDirectory(final String dir) {
        this.subDirectory = dir;
        return this;
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey((Object)this.result.getItem()));
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        String path = id.addTransientModifier();
        if (this.subDirectory != null && !this.subDirectory.isEmpty()) {
            path = this.subDirectory + "/" + path;
        }
        id = new ResourceLocation(id.func_110624_b(), "shaped/" + path);
        consumerIn.accept((IFinishedRecipe)new Result(id, this.result, this.pattern, this.key));
    }
    
    private void validate(final ResourceLocation id) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
        }
        final Set<Character> set = Sets.newHashSet((Iterable)this.key.keySet());
        set.remove(' ');
        for (final String s : this.pattern) {
            for (int i = 0; i < s.length(); ++i) {
                final char c0 = s.charAt(i);
                if (!this.key.containsKey(c0) && c0 != ' ') {
                    throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");
                }
                set.remove(c0);
            }
        }
        if (!set.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
        }
        if (this.pattern.size() == 1 && this.pattern.get(0).length() == 1) {
            throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
        }
    }
    
    public static class Result implements IFinishedRecipe
    {
        private final ResourceLocation id;
        private final ItemStack result;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        
        public Result(final ResourceLocation idIn, final ItemStack resultIn, final List<String> patternIn, final Map<Character, Ingredient> keyIn) {
            this.id = idIn;
            this.result = resultIn;
            this.pattern = patternIn;
            this.key = keyIn;
        }
        
        public void func_218610_a(final JsonObject json) {
            final JsonArray jsonarray = new JsonArray();
            for (final String s : this.pattern) {
                jsonarray.add(s);
            }
            json.add("pattern", (JsonElement)jsonarray);
            final JsonObject keys = new JsonObject();
            for (final Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                keys.add(String.valueOf(entry.getKey()), entry.getValue().func_200304_c());
            }
            json.add("key", (JsonElement)keys);
            json.add("result", (JsonElement)JsonHelper.serializeItemStack(this.result));
        }
        
        public RecipeSerializer<?> func_218609_c() {
            return (RecipeSerializer<?>)RecipeSerializer.field_222157_a;
        }
        
        public ResourceLocation func_200442_b() {
            return this.id;
        }
        
        @Nullable
        public JsonObject func_200440_c() {
            return null;
        }
        
        @Nullable
        public ResourceLocation func_200443_d() {
            return new ResourceLocation("");
        }
    }
}
