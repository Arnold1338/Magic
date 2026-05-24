package hellfirepvp.astralsorcery.common.data.journal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageText;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageRecipe;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageAltarRecipe;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.world.level.item.ItemStack;
import java.util.function.Predicate;
import net.minecraft.world.level.item.crafting.RecipeManager;
import net.minecraft.world.level.item.crafting.RecipeType;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.item.crafting.Recipe;
import java.util.function.Supplier;

public class JournalPageRecipe implements JournalPage
{
    private final Supplier<Recipe<?>> recipeProvider;
    
    private JournalPageRecipe(final Supplier<Recipe<?>> recipeProvider) {
        this.recipeProvider = recipeProvider;
    }
    
    public static JournalPageRecipe fromName(final ResourceLocation recipeId) {
        return new JournalPageRecipe(() -> {
            final RecipeManager mgr = RecipeHelper.getRecipeManager();
            if (mgr == null) {
                throw new IllegalStateException("Not connected to a server, but calling GUI code?");
            }
            else {
                final Recipe recipe = mgr.func_215366_a((RecipeType)RecipeTypesAS.TYPE_ALTAR.getType()).get(recipeId);
                if (recipe != null) {
                    return recipe;
                }
                else {
                    final Recipe recipe2 = mgr.func_215366_a(RecipeType.field_222149_a).get(recipeId);
                    if (recipe2 != null) {
                        return recipe2;
                    }
                    else {
                        return null;
                    }
                }
            }
        });
    }
    
    public static JournalPageRecipe fromOutputPreferAltarRecipes(final Predicate<ItemStack> outputTest) {
        return new JournalPageRecipe(() -> {
            final RecipeManager mgr = RecipeHelper.getRecipeManager();
            if (mgr == null) {
                throw new IllegalStateException("Not connected to a server, but calling GUI code?");
            }
            else {
                final Recipe<?> recipe = (Recipe<?>)mgr.func_215366_a((RecipeType)RecipeTypesAS.TYPE_ALTAR.getType()).values().stream().map(r -> (SimpleAltarRecipe)r).filter(r -> outputTest.test(r.getOutputForRender((Iterable<ItemStack>)Collections.emptyList()))).findFirst().orElse(null);
                if (recipe != null) {
                    return recipe;
                }
                else {
                    final Recipe<?> recipe2 = (Recipe<?>)mgr.func_215366_a(RecipeType.field_222149_a).values().stream().filter(r -> outputTest.test(r.func_77571_b())).findFirst().orElse(null);
                    if (recipe2 != null) {
                        return recipe2;
                    }
                    else {
                        return null;
                    }
                }
            }
        });
    }
    
    public static JournalPageRecipe fromOutputPreferVanillaRecipes(final Predicate<ItemStack> outputTest) {
        return new JournalPageRecipe(() -> {
            final RecipeManager mgr = RecipeHelper.getRecipeManager();
            if (mgr == null) {
                throw new IllegalStateException("Not connected to a server, but calling GUI code?");
            }
            else {
                final Recipe<?> recipe = (Recipe<?>)mgr.func_215366_a(RecipeType.field_222149_a).values().stream().filter(r -> outputTest.test(r.func_77571_b())).findFirst().orElse(null);
                if (recipe != null) {
                    return recipe;
                }
                else {
                    final Recipe<?> recipe2 = (Recipe<?>)mgr.func_215366_a((RecipeType)RecipeTypesAS.TYPE_ALTAR.getType()).values().stream().map(r -> (SimpleAltarRecipe)r).filter(r -> outputTest.test(r.getOutputForRender((Iterable<ItemStack>)Collections.emptyList()))).findFirst().orElse(null);
                    if (recipe2 != null) {
                        return recipe2;
                    }
                    else {
                        return null;
                    }
                }
            }
        });
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderablePage buildRenderPage(final ResearchNode node, final int nodePage) {
        final Recipe<?> recipe = this.recipeProvider.get();
        if (recipe instanceof SimpleAltarRecipe) {
            return new RenderPageAltarRecipe(node, nodePage, (SimpleAltarRecipe)recipe);
        }
        if (recipe != null) {
            return RenderPageRecipe.fromRecipe(node, nodePage, recipe);
        }
        return new RenderPageText("astralsorcery.journal.recipe.removalinfo");
    }
}
