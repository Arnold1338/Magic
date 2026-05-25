package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.world.item.crafting.IRecipeSerializer;
import org.apache.logging.log4j.Level;
import hellfirepvp.astralsorcery.AstralSorcery;
import com.google.gson.JsonObject;
import java.util.HashMap;
import javax.annotation.Nonnull;
import net.minecraft.core.Registry;
import java.util.HashSet;
import javax.annotation.Nullable;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import net.minecraft.world.item.crafting.RecipeType;
import java.util.Map;

public abstract class CustomRecipeBuilder<R extends CustomMatcherRecipe>
{
    private static final Map<RecipeType<?>, Set<ResourceLocation>> builtRecipes;
    
    public void build(final Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, null);
    }
    
    public void build(final Consumer<IFinishedRecipe> consumerIn, @Nullable final String directory) {
        final R recipe = this.validateAndGet();
        String saveId = recipe.func_199560_c().addTransientModifier();
        if (directory != null) {
            saveId = directory + "/" + saveId;
        }
        saveId = this.getSerializer().getRegistryName().addTransientModifier() + "/" + saveId;
        final ResourceLocation id = new ResourceLocation(recipe.func_199560_c().func_110624_b(), saveId);
        if (!CustomRecipeBuilder.builtRecipes.computeIfAbsent((RecipeType<?>)recipe.func_222127_g(), type -> new HashSet()).add(id)) {
            throw new IllegalArgumentException("Tried to register recipe with id " + id + " twice for type " + Registry.field_218367_H.func_177774_c((Object)recipe.func_222127_g()));
        }
        consumerIn.accept((IFinishedRecipe)new WrappedCustomRecipe((CustomMatcherRecipe)recipe, id));
    }
    
    @Nonnull
    protected abstract R validateAndGet();
    
    protected abstract CustomRecipeSerializer<R> getSerializer();
    
    static {
        builtRecipes = new HashMap<RecipeType<?>, Set<ResourceLocation>>();
    }
    
    private class WrappedCustomRecipe implements IFinishedRecipe
    {
        private final R recipe;
        private final ResourceLocation id;
        
        private WrappedCustomRecipe(final R recipe, final ResourceLocation id) {
            this.recipe = recipe;
            this.id = id;
        }
        
        public void func_218610_a(final JsonObject json) {
            AstralSorcery.log.log(Level.INFO, this.id.toString());
            CustomRecipeBuilder.this.getSerializer().write(json, this.recipe);
        }
        
        public ResourceLocation func_200442_b() {
            return this.id;
        }
        
        public IRecipeSerializer<?> func_218609_c() {
            return (IRecipeSerializer<?>)this.recipe.getSerializer();
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
