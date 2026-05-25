package hellfirepvp.astralsorcery.common.data.journal;

import net.minecraft.world.item.crafting.Recipe;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageText;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageBlockTransmutation;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;
import java.util.function.Supplier;

public class JournalPageBlockTransmutation implements JournalPage
{
    private final Supplier<BlockTransmutation> recipeProvider;
    
    private JournalPageBlockTransmutation(final Supplier<BlockTransmutation> recipeProvider) {
        this.recipeProvider = recipeProvider;
    }
    
    public static JournalPageBlockTransmutation fromOutput(final Predicate<ItemStack> outputTest) {
        return new JournalPageBlockTransmutation(() -> {
            final RecipeManager mgr = RecipeHelper.getRecipeManager();
            if (mgr == null) {
                throw new IllegalStateException("Not connected to a server, but calling GUI code?");
            }
            else {
                return (BlockTransmutation)mgr.getRecipeFor((RecipeType)RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION.getType()).values().stream().map(r -> (BlockTransmutation)r).filter(r -> outputTest.test(r.getOutputDisplay())).findFirst().orElse(null);
            }
        });
    }
    
    @Override
    public RenderablePage buildRenderPage(final ResearchNode node, final int page) {
        final BlockTransmutation recipe = this.recipeProvider.get();
        if (recipe != null) {
            return new RenderPageBlockTransmutation(node, page, recipe);
        }
        return new RenderPageText("astralsorcery.journal.recipe.removalinfo");
    }
}
