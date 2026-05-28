package hellfirepvp.astralsorcery.common.crafting.serializer;

import net.minecraft.world.item.crafting.Recipe;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;

import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.material.Fluid;
import com.google.gson.JsonElement;
import net.minecraft.world.item.crafting.Ingredient;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;

public class WellRecipeSerializer extends CustomRecipeSerializer<WellLiquefaction>
{
    public WellRecipeSerializer() {
        super(RecipeSerializersAS.WELL_LIQUEFACTION);
    }
    
    public WellLiquefaction read(final ResourceLocation recipeId, final JsonObject json) {
        final Ingredient input = Ingredient.func_199802_a((JsonElement)JSONUtils.func_152754_s(json, "input"));
        final String fluidKey = JSONUtils.func_151200_h(json, "output");
        final Fluid fluid = (Fluid)ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidKey));
        if (fluid == null) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidKey);
        }
        final float productionMultiplier = JSONUtils.func_151217_k(json, "productionMultiplier");
        final float shatterMultiplier = JSONUtils.func_151217_k(json, "shatterMultiplier");
        Color color = null;
        if (json.has("color")) {
            color = JsonHelper.getColor(json, "color");
        }
        return new WellLiquefaction(recipeId, input, fluid, color, productionMultiplier, shatterMultiplier);
    }
    
    public WellLiquefaction read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        final Ingredient input = Ingredient.func_199566_b(buffer);
        final Fluid fluid = ByteBufUtils.readRegistryEntry(buffer);
        final float shatter = buffer.readFloat();
        final float production = buffer.readFloat();
        final Color color = ByteBufUtils.readOptional(buffer, buf -> new Color(buf.readInt(), true));
        return new WellLiquefaction(recipeId, input, fluid, color, production, shatter);
    }
    
    public void write(final FriendlyByteBuf buffer, final WellLiquefaction recipe) {
        recipe.getInput().func_199564_a(buffer);
        ByteBufUtils.writeRegistryEntry(buffer, (net.minecraftforge.registries.IForgeRegistryEntry<Object>)recipe.getFluidOutput());
        buffer.writeFloat(recipe.getShatterMultiplier());
        buffer.writeFloat(recipe.getProductionMultiplier());
        ByteBufUtils.writeOptional(buffer, recipe.getCatalystColor(), (buf, color) -> buf.writeInt(color.getRGB()));
    }
    
    @Override
    public void write(final JsonObject object, final WellLiquefaction recipe) {
        object.add("input", recipe.getInput().func_200304_c());
        object.addProperty("output", recipe.getFluidOutput().getRegistryName().toString());
        object.addProperty("productionMultiplier", (Number)recipe.getProductionMultiplier());
        object.addProperty("shatterMultiplier", (Number)recipe.getShatterMultiplier());
        object.addProperty("color", (Number)((recipe.getCatalystColor() == null) ? Color.WHITE.getRGB() : recipe.getCatalystColor().getRGB()));
    }
}
