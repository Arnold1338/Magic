package hellfirepvp.astralsorcery.common.data.journal;

import net.minecraft.world.item.crafting.Recipe;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageText;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageLiquidInfusion;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import java.util.function.Supplier;

public class JournalPageLiquidInfusion implements JournalPage
{
    private final Supplier<LiquidInfusion> recipeProvider;
    
    private JournalPageLiquidInfusion(final Supplier<LiquidInfusion> recipeProvider) {
        this.recipeProvider = recipeProvider;
    }
    
    public static JournalPageLiquidInfusion fromOutput(final Predicate<ItemStack> outputTest) {
        return new JournalPageLiquidInfusion(() -> {
            final RecipeManager mgr = RecipeHelper.getRecipeManager();
            if (mgr == null) {
                throw new IllegalStateException("Not connected to a server, but calling GUI code?");
            }
            else {
                return (LiquidInfusion)mgr.func_215366_a((RecipeType)RecipeTypesAS.TYPE_INFUSION.getType()).values().stream().map(r -> (LiquidInfusion)r).filter(r -> outputTest.test(r.getOutput(ItemStack.EMPTY))).findFirst().orElse(null);
            }
        });
    }
    
    @Override
    public RenderablePage buildRenderPage(final ResearchNode node, final int page) {
        final LiquidInfusion recipe = this.recipeProvider.get();
        if (recipe != null) {
            return new RenderPageLiquidInfusion(node, page, recipe);
        }
        return new RenderPageText("astralsorcery.journal.recipe.removalinfo");
    }
}
