package hellfirepvp.astralsorcery.client.screen.journal.page;

import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.world.item.ItemStack;
import java.util.Collections;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;

public class RenderPageAltarRecipe extends RenderPageRecipeTemplate
{
    private final SimpleAltarRecipe recipe;
    private final AbstractRenderableTexture gridTexture;
    
    public RenderPageAltarRecipe(@Nullable final ResearchNode node, final int nodePage, final SimpleAltarRecipe recipe) {
        super(node, nodePage);
        this.recipe = recipe;
        this.gridTexture = this.getGridTexture(recipe);
    }
    
    private AbstractRenderableTexture getGridTexture(final SimpleAltarRecipe recipe) {
        switch (recipe.getAltarType()) {
            case DISCOVERY: {
                return TexturesAS.TEX_GUI_BOOK_GRID_T1;
            }
            case ATTUNEMENT: {
                return TexturesAS.TEX_GUI_BOOK_GRID_T2;
            }
            case CONSTELLATION: {
                return TexturesAS.TEX_GUI_BOOK_GRID_T3;
            }
            case RADIANCE: {
                return TexturesAS.TEX_GUI_BOOK_GRID_T4;
            }
            default: {
                return TexturesAS.TEX_GUI_BOOK_GRID_T4;
            }
        }
    }
    
    @Override
    public void render(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.clearFrameRectangles();
        this.renderRecipeGrid(renderStack, x, y, z, this.gridTexture);
        this.renderExpectedItemStackOutput(renderStack, x + 78.0f, y + 25.0f, z, 1.4f, this.recipe.getOutputForRender((Iterable<ItemStack>)Collections.emptyList()));
        this.renderInfoStar(renderStack, x, y, z, pTicks);
        this.renderRequiredConstellation(renderStack, x, y, z, this.recipe.getFocusConstellation());
        final int widthShift = (5 - this.recipe.getInputs().getWidth()) / 2;
        final int heightShift = (5 - this.recipe.getInputs().getHeight()) / 2;
        final AltarType type = this.recipe.getAltarType();
        final float recipeX = x + 30.0f;
        final float recipeY = y + 78.0f;
        for (int xx = 0; xx < 5; ++xx) {
            for (int yy = 0; yy < 5; ++yy) {
                final int slot = xx + yy * 5;
                if (type.hasSlot(slot)) {
                    final int recipeIndex = xx - widthShift + (yy - heightShift) * 5;
                    if (recipeIndex >= 0 && recipeIndex < 25) {
                        final float renderX = recipeX + 25 * xx;
                        final float renderY = recipeY + 25 * yy;
                        this.renderExpectedIngredientInput(renderStack, renderX, renderY, z, 1.1f, recipeIndex * 20, this.recipe.getInputs().getIngredient(recipeIndex));
                    }
                }
            }
        }
        this.renderExpectedRelayInputs(renderStack, x, y, z + 150.0f, this.recipe);
    }
    
    @Override
    public boolean propagateMouseClick(final double mouseX, final double mouseZ) {
        return this.handleRecipeNameCopyClick(mouseX, mouseZ, this.recipe) || this.handleBookLookupClick(mouseX, mouseZ);
    }
    
    @Override
    public void postRender(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.renderHoverTooltips(renderStack, mouseX, mouseY, z, this.recipe.func_199560_c());
        this.renderInfoStarTooltips(renderStack, x, y, z, mouseX, mouseY, toolTip -> {
            this.addAltarRecipeTooltip(this.recipe, toolTip);
            this.addConstellationInfoTooltip(this.recipe.getFocusConstellation(), toolTip);
            return;
        });
        super.postRender(renderStack, x, y, z, pTicks, mouseX, mouseY);
    }
}
