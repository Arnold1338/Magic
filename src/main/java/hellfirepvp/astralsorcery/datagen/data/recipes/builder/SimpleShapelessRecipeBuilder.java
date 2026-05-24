package hellfirepvp.astralsorcery.datagen.data.recipes.builder;

import javax.annotation.Nullable;
import net.minecraft.item.crafting.IRecipeSerializer;
import java.util.Iterator;
import net.minecraft.core.Registry;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;
import net.minecraft.tags.TagKey;
import com.google.common.collect.Lists;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import net.minecraft.world.item.Item;

public class SimpleShapelessRecipeBuilder
{
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients;
    private String subDirectory;
    
    public SimpleShapelessRecipeBuilder(final ItemLike result, final int count) {
        this.ingredients = Lists.newArrayList();
        this.subDirectory = null;
        this.result = result.func_199767_j();
        this.count = count;
    }
    
    public static SimpleShapelessRecipeBuilder shapelessRecipe(final ItemLike result) {
        return shapelessRecipe(result, 1);
    }
    
    public static SimpleShapelessRecipeBuilder shapelessRecipe(final ItemLike result, final int count) {
        return new SimpleShapelessRecipeBuilder(result, count);
    }
    
    public SimpleShapelessRecipeBuilder addIngredient(final ITag<Item> tagIn) {
        return this.addIngredient(Ingredient.func_199805_a((ITag)tagIn));
    }
    
    public SimpleShapelessRecipeBuilder addIngredient(final ItemLike itemIn) {
        return this.addIngredient(itemIn, 1);
    }
    
    public SimpleShapelessRecipeBuilder addIngredient(final ItemLike itemIn, final int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.addIngredient(Ingredient.func_199804_a(new ItemLike[] { itemIn }));
        }
        return this;
    }
    
    public SimpleShapelessRecipeBuilder addIngredient(final Ingredient ingredientIn) {
        return this.addIngredient(ingredientIn, 1);
    }
    
    public SimpleShapelessRecipeBuilder addIngredient(final Ingredient ingredientIn, final int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.ingredients.add(ingredientIn);
        }
        return this;
    }
    
    public SimpleShapelessRecipeBuilder subDirectory(final String dir) {
        this.subDirectory = dir;
        return this;
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey((IForgeRegistryEntry)this.result.getItem()));
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        String path = id.func_110623_a();
        if (this.subDirectory != null && !this.subDirectory.isEmpty()) {
            path = this.subDirectory + "/" + path;
        }
        id = new ResourceLocation(id.func_110624_b(), "shapeless/" + path);
        consumerIn.accept((IFinishedRecipe)new Result(id, this.result, this.count, this.ingredients));
    }
    
    public static class Result implements IFinishedRecipe
    {
        private final ResourceLocation key;
        private final Item result;
        private final int count;
        private final List<Ingredient> ingredients;
        
        public Result(final ResourceLocation key, final Item result, final int resultCount, final List<Ingredient> ingredients) {
            this.key = key;
            this.result = result;
            this.count = resultCount;
            this.ingredients = ingredients;
        }
        
        public void func_218610_a(final JsonObject json) {
            final JsonArray inputs = new JsonArray();
            for (final Ingredient ingredient : this.ingredients) {
                inputs.add(ingredient.func_200304_c());
            }
            json.add("ingredients", (JsonElement)inputs);
            final JsonObject result = new JsonObject();
            result.addProperty("item", Registry.field_212630_s.func_177774_c((Object)this.result).toString());
            if (this.count > 1) {
                result.addProperty("count", (Number)this.count);
            }
            json.add("result", (JsonElement)result);
        }
        
        public IRecipeSerializer<?> func_218609_c() {
            return (IRecipeSerializer<?>)IRecipeSerializer.field_222158_b;
        }
        
        public ResourceLocation func_200442_b() {
            return this.key;
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
