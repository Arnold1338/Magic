package hellfirepvp.astralsorcery.datagen.data.recipes.builder;

import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;

public class ResultCookingRecipeBuilder
{
    private final ItemStack result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final CookingRecipeSerializer<?> recipeSerializer;
    
    private ResultCookingRecipeBuilder(final ItemStack result, final Ingredient ingredientIn, final float experienceIn, final int cookingTimeIn, final CookingRecipeSerializer<?> serializer) {
        this.result = result.copy();
        this.ingredient = ingredientIn;
        this.experience = experienceIn;
        this.cookingTime = cookingTimeIn;
        this.recipeSerializer = serializer;
    }
    
    public static ResultCookingRecipeBuilder cookingRecipe(final Ingredient ingredientIn, final ItemStack result, final float experienceIn, final int cookingTimeIn, final CookingRecipeSerializer<?> serializer) {
        return new ResultCookingRecipeBuilder(result, ingredientIn, experienceIn, cookingTimeIn, serializer);
    }
    
    public static ResultCookingRecipeBuilder blastingRecipe(final Ingredient ingredientIn, final ItemStack result, final float experienceIn, final int cookingTimeIn) {
        return cookingRecipe(ingredientIn, result, experienceIn, cookingTimeIn, (CookingRecipeSerializer<?>)IRecipeSerializer.field_222172_p);
    }
    
    public static ResultCookingRecipeBuilder smeltingRecipe(final Ingredient ingredientIn, final ItemStack result, final float experienceIn, final int cookingTimeIn) {
        return cookingRecipe(ingredientIn, result, experienceIn, cookingTimeIn, (CookingRecipeSerializer<?>)IRecipeSerializer.field_222171_o);
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey((IForgeRegistryEntry)this.result.getItem()));
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn, final String save) {
        final ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey((IForgeRegistryEntry)this.result.getItem());
        final ResourceLocation saveNameKey = new ResourceLocation(save);
        if (saveNameKey.equals((Object)itemKey)) {
            throw new IllegalStateException("Recipe " + saveNameKey + " should remove its 'save' argument");
        }
        this.build(consumerIn, saveNameKey);
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        id = new ResourceLocation(id.func_110624_b(), this.recipeSerializer.getRegistryName().func_110623_a() + "/" + id.func_110623_a());
        consumerIn.accept((IFinishedRecipe)new Result(id, this.ingredient, this.result, this.experience, this.cookingTime, (IRecipeSerializer<? extends AbstractCookingRecipe>)this.recipeSerializer));
    }
    
    public static class Result implements IFinishedRecipe
    {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final ItemStack result;
        private final float experience;
        private final int cookingTime;
        private final IRecipeSerializer<? extends AbstractCookingRecipe> serializer;
        
        public Result(final ResourceLocation idIn, final Ingredient ingredientIn, final ItemStack resultIn, final float experienceIn, final int cookingTimeIn, final IRecipeSerializer<? extends AbstractCookingRecipe> serializerIn) {
            this.id = idIn;
            this.ingredient = ingredientIn;
            this.result = resultIn;
            this.experience = experienceIn;
            this.cookingTime = cookingTimeIn;
            this.serializer = serializerIn;
        }
        
        public void func_218610_a(final JsonObject json) {
            final JsonObject itemResult = new JsonObject();
            itemResult.addProperty("item", this.result.getItem().getRegistryName().toString());
            itemResult.addProperty("count", (Number)this.result.func_190916_E());
            json.add("ingredient", this.ingredient.func_200304_c());
            json.add("result", (JsonElement)itemResult);
            json.addProperty("experience", (Number)this.experience);
            json.addProperty("cookingtime", (Number)this.cookingTime);
        }
        
        public IRecipeSerializer<?> func_218609_c() {
            return this.serializer;
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
