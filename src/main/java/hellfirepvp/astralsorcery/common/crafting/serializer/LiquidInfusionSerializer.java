package hellfirepvp.astralsorcery.common.crafting.serializer;

import net.minecraft.world.item.crafting.Recipe;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import com.google.gson.JsonSyntaxException;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.util.JSONUtils;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;

public class LiquidInfusionSerializer extends CustomRecipeSerializer<LiquidInfusion>
{
    public LiquidInfusionSerializer() {
        super(RecipeSerializersAS.LIQUID_INFUSION);
    }
    
    public LiquidInfusion read(final ResourceLocation recipeId, final JsonObject json) {
        final ResourceLocation fluidKey = new ResourceLocation(JSONUtils.func_151200_h(json, "fluidInput"));
        final Fluid fluidInput = (Fluid)ForgeRegistries.FLUIDS.getValue(fluidKey);
        if (fluidInput == null || fluidInput == Fluids.field_204541_a) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidKey);
        }
        final Ingredient input = CraftingHelper.getIngredient(json.get("input"));
        final ItemStack output = JsonHelper.getItemStack(json.get("output"), "output");
        final float consumptionChance = JSONUtils.func_151217_k(json, "consumptionChance");
        final int duration = JSONUtils.func_151203_m(json, "duration");
        final boolean consumeMultipleFluids = JSONUtils.func_151209_a(json, "consumeMultipleFluids", false);
        final boolean acceptChaliceInput = JSONUtils.func_151209_a(json, "acceptChaliceInput", true);
        final boolean copyNBTToOutputs = JSONUtils.func_151209_a(json, "copyNBTToOutputs", false);
        return new LiquidInfusion(recipeId, duration, fluidInput, input, output, consumptionChance, consumeMultipleFluids, acceptChaliceInput, copyNBTToOutputs);
    }
    
    public LiquidInfusion read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        return LiquidInfusion.read(recipeId, buffer);
    }
    
    @Override
    public void write(final JsonObject object, final LiquidInfusion recipe) {
        recipe.write(object);
    }
    
    public void write(final FriendlyByteBuf buffer, final LiquidInfusion recipe) {
        recipe.write(buffer);
    }
}
