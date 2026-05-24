package hellfirepvp.astralsorcery.common.crafting.nojson;

import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public abstract class CustomRecipeRegistry<R extends CustomRecipe>
{
    private Map<ResourceLocation, R> recipes;
    
    public CustomRecipeRegistry() {
        this.recipes = new HashMap<ResourceLocation, R>();
    }
    
    public abstract void init();
    
    public void register(@Nonnull final R recipe) {
        this.recipes.put(recipe.getKey(), recipe);
    }
    
    @Nullable
    public R getRecipe(final ResourceLocation key) {
        return this.recipes.get(key);
    }
    
    @Nonnull
    public Collection<R> getRecipes() {
        return this.recipes.values();
    }
}
