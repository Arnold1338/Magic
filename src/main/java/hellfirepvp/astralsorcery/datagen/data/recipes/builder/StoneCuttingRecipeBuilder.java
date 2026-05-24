package hellfirepvp.astralsorcery.datagen.data.recipes.builder;

import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.IRecipeSerializer;
import com.google.gson.JsonObject;
import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;

public class StoneCuttingRecipeBuilder
{
    private final Ingredient input;
    private final ItemLike output;
    private final int count;
    
    private StoneCuttingRecipeBuilder(final Ingredient input, final ItemLike output, final int count) {
        this.input = input;
        this.output = output;
        this.count = count;
    }
    
    public static StoneCuttingRecipeBuilder stoneCuttingRecipe(final Ingredient input, final ItemLike output) {
        return stoneCuttingRecipe(input, output, 1);
    }
    
    public static StoneCuttingRecipeBuilder stoneCuttingRecipe(final Ingredient input, final ItemLike output, final int count) {
        return new StoneCuttingRecipeBuilder(input, output, count);
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey((IForgeRegistryEntry)this.output.func_199767_j()));
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        id = NameUtil.prefixPath(id, "stonecutting/");
        consumerIn.accept((IFinishedRecipe)new Result(id, this.input, this.output.func_199767_j(), this.count));
    }
    
    public static class Result implements IFinishedRecipe
    {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Item result;
        private final int count;
        
        public Result(final ResourceLocation id, final Ingredient input, final Item output, final int count) {
            this.id = id;
            this.ingredient = input;
            this.result = output;
            this.count = count;
        }
        
        public void func_218610_a(final JsonObject jsonObject) {
            jsonObject.add("ingredient", this.ingredient.func_200304_c());
            jsonObject.addProperty("result", ForgeRegistries.ITEMS.getKey((IForgeRegistryEntry)this.result).toString());
            jsonObject.addProperty("count", (Number)this.count);
        }
        
        public ResourceLocation func_200442_b() {
            return this.id;
        }
        
        public IRecipeSerializer<?> func_218609_c() {
            return (IRecipeSerializer<?>)IRecipeSerializer.field_222175_s;
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
