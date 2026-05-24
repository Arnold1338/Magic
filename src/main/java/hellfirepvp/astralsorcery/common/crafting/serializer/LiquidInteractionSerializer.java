package hellfirepvp.astralsorcery.common.crafting.serializer;

import net.minecraft.world.item.crafting.Recipe;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;

public class LiquidInteractionSerializer extends CustomRecipeSerializer<LiquidInteraction>
{
    public LiquidInteractionSerializer() {
        super(RecipeSerializersAS.LIQUID_INTERACTION);
    }
    
    public LiquidInteraction read(final ResourceLocation recipeId, final JsonObject json) {
        return LiquidInteraction.read(recipeId, json);
    }
    
    @Override
    public void write(final JsonObject object, final LiquidInteraction recipe) {
        recipe.write(object);
    }
    
    @Nullable
    public LiquidInteraction read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        return LiquidInteraction.read(recipeId, buffer);
    }
    
    public void write(final FriendlyByteBuf buffer, final LiquidInteraction recipe) {
        recipe.write(buffer);
    }
}
