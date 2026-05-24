package hellfirepvp.astralsorcery.client.screen.journal.page;

import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.NonNullList;
import java.util.HashMap;
import net.minecraft.world.item.crafting.Recipe;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.Map;

public class RenderPageRecipe extends RenderPageRecipeTemplate
{
    private final Map<Integer, Ingredient> inputs;
    private final ItemStack output;
    private final ResourceLocation recipeId;
    
    private RenderPageRecipe(@Nullable final ResearchNode node, final int nodePage, final Map<Integer, Ingredient> inputs, final ItemStack output, final ResourceLocation recipeId) {
        super(node, nodePage);
        this.inputs = inputs;
        this.output = output;
        this.recipeId = recipeId;
    }
    
    public static RenderPageRecipe fromRecipe(@Nullable final ResearchNode node, final int nodePage, final Recipe<?> recipe) {
        final NonNullList<Ingredient> ingredients = (NonNullList<Ingredient>)recipe.func_192400_c();
        final Map<Integer, Ingredient> inputs = new HashMap<Integer, Ingredient>();
        for (int i = 0; i < 9; ++i) {
            inputs.put(i, Ingredient.field_193370_a);
        }
        final boolean offsetDiagonal = ingredients.size() == 1;
        for (int xx = 0; xx < 3; ++xx) {
            for (int zz = 0; zz < 3; ++zz) {
                int indexSlot;
                final int slot = indexSlot = xx + zz * 3;
                if (offsetDiagonal) {
                    indexSlot -= 4;
                }
                if (indexSlot >= 0 && indexSlot < ingredients.size()) {
                    inputs.put(slot, (Ingredient)ingredients.get(indexSlot));
                }
            }
        }
        return new RenderPageRecipe(node, nodePage, inputs, recipe.func_77571_b(), recipe.func_199560_c());
    }
    
    @Override
    public void render(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.clearFrameRectangles();
        this.renderRecipeGrid(renderStack, x, y, z, TexturesAS.TEX_GUI_BOOK_GRID_T1);
        this.renderExpectedItemStackOutput(renderStack, x + 78.0f, y + 25.0f, z, 1.4f, this.output);
        final float recipeX = x + 55.0f;
        final float recipeY = y + 103.0f;
        for (int xx = 0; xx < 3; ++xx) {
            for (int yy = 0; yy < 3; ++yy) {
                final int slot = xx + yy * 3;
                final float renderX = recipeX + 25 * xx;
                final float renderY = recipeY + 25 * yy;
                this.renderExpectedIngredientInput(renderStack, renderX, renderY, z, 1.1f, slot * 20, this.inputs.get(slot));
            }
        }
    }
    
    @Override
    public boolean propagateMouseClick(final double mouseX, final double mouseZ) {
        return this.handleBookLookupClick(mouseX, mouseZ);
    }
    
    @Override
    public void postRender(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.renderHoverTooltips(renderStack, mouseX, mouseY, z, this.recipeId);
    }
}
