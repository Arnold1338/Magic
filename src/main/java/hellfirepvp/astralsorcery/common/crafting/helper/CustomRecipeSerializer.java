package hellfirepvp.astralsorcery.common.crafting.helper;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;


public abstract class CustomRecipeSerializer<T extends CustomMatcherRecipe> > implements RecipeSerializer<T>
{
    public CustomRecipeSerializer(final ResourceLocation name) {
        this;
    }
    
    public abstract void write(final JsonObject p0, final T p1);
}
