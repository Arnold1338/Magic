package hellfirepvp.astralsorcery.common.crafting.serializer;

import net.minecraft.world.item.crafting.Recipe;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.item.crafting.Ingredient;
import com.google.gson.JsonSyntaxException;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeTypeHandler;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;

public class SimpleAltarRecipeSerializer extends CustomRecipeSerializer<SimpleAltarRecipe>
{
    public SimpleAltarRecipeSerializer() {
        super(RecipeSerializersAS.SIMPLE_ALTAR_CRAFTING);
    }
    
    public SimpleAltarRecipe read(final ResourceLocation recipeId, final JsonObject json) {
        final int typeId = JSONUtils.func_151203_m(json, "altar_type");
        final AltarType type = MiscUtils.getEnumEntry(AltarType.class, typeId);
        final int duration = JSONUtils.func_151203_m(json, "duration");
        final int starlightRequirement = JSONUtils.func_151203_m(json, "starlight");
        final AltarRecipeGrid grid = AltarRecipeGrid.deserialize(type, json);
        grid.validate(type);
        SimpleAltarRecipe recipe = new SimpleAltarRecipe(recipeId, type, duration, starlightRequirement, grid);
        if (JSONUtils.func_151204_g(json, "recipe_class")) {
            final ResourceLocation key = new ResourceLocation(JSONUtils.func_151200_h(json, "recipe_class"));
            recipe = AltarRecipeTypeHandler.convert(recipe, key);
            recipe.setCustomRecipeType(key);
        }
        if (JSONUtils.func_151202_d(json, "output")) {
            final JsonArray outputArray = JSONUtils.func_151214_t(json, "output");
            for (int i = 0; i < outputArray.size(); ++i) {
                recipe.addOutput(JsonHelper.getItemStack(outputArray.get(i), String.format("output[%s]", i)));
            }
        }
        else {
            recipe.addOutput(JsonHelper.getItemStack(json, "output"));
        }
        JsonObject recipeOptions = new JsonObject();
        if (JSONUtils.func_151204_g(json, "options")) {
            recipeOptions = JSONUtils.func_152754_s(json, "options");
        }
        recipe.deserializeAdditionalJson(recipeOptions);
        if (JSONUtils.func_151204_g(json, "focus_constellation")) {
            final ResourceLocation key2 = new ResourceLocation(JSONUtils.func_151200_h(json, "focus_constellation"));
            final IConstellation cst = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(key2);
            if (cst == null) {
                throw new JsonSyntaxException("Unknown constellation " + key2.toString());
            }
            recipe.setFocusConstellation(cst);
        }
        if (JSONUtils.func_151204_g(json, "relay_inputs")) {
            final JsonArray relayIngredients = JSONUtils.func_151214_t(json, "relay_inputs");
            for (int j = 0; j < relayIngredients.size(); ++j) {
                final JsonElement element = relayIngredients.get(j);
                final Ingredient ingredient = Ingredient.func_199802_a(element);
                if (!ingredient.func_203189_d()) {
                    recipe.addRelayInput(ingredient);
                }
                else {
                    AstralSorcery.log.warn("Skipping relay_inputs[" + j + "] for recipe " + recipeId + " as the ingredient has no matching items!");
                    AstralSorcery.log.warn("Ingredient skipped: " + JSONUtils.func_151222_d(element));
                }
            }
        }
        if (JSONUtils.func_151204_g(json, "effects")) {
            final JsonArray effectNames = JSONUtils.func_151214_t(json, "effects");
            for (int j = 0; j < effectNames.size(); ++j) {
                final JsonElement element = effectNames.get(j);
                final ResourceLocation effectKey = new ResourceLocation(JSONUtils.func_151206_a(element, "effects[" + j + "]"));
                final AltarRecipeEffect effect = (AltarRecipeEffect)RegistriesAS.REGISTRY_ALTAR_EFFECTS.getValue(effectKey);
                if (effect == null) {
                    throw new JsonSyntaxException("No altar effect for name " + effectKey + "! (Found at: effects[" + j + "])");
                }
                recipe.addAltarEffect(effect);
            }
        }
        return recipe;
    }
    
    public SimpleAltarRecipe read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        return SimpleAltarRecipe.read(recipeId, buffer);
    }
    
    @Override
    public void write(final JsonObject object, final SimpleAltarRecipe recipe) {
        recipe.write(object);
    }
    
    public void write(final FriendlyByteBuf buffer, final SimpleAltarRecipe recipe) {
        recipe.write(buffer);
    }
}
