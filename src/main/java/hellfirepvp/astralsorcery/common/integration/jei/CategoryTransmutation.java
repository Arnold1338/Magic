package hellfirepvp.astralsorcery.common.integration.jei;

import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.constants.VanillaTypes;
import java.util.Collections;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import java.util.List;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.integration.IntegrationJEI;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;

public class CategoryTransmutation extends JEICategory<BlockTransmutation>
{
    private final IDrawable background;
    private final IDrawable icon;
    
    public CategoryTransmutation(final IGuiHelper guiHelper) {
        super(IntegrationJEI.CATEGORY_TRANSMUTATION);
        this.background = (IDrawable)guiHelper.createDrawable(AstralSorcery.key("textures/gui/jei/transmutation.png"), 0, 0, 116, 54);
        this.icon = guiHelper.createDrawableIngredient((Object)new ItemStack((ItemLike)BlocksAS.LENS));
    }
    
    public Class<? extends BlockTransmutation> getRecipeClass() {
        return BlockTransmutation.class;
    }
    
    public IDrawable getBackground() {
        return this.background;
    }
    
    public IDrawable getIcon() {
        return this.icon;
    }
    
    @Override
    public List<BlockTransmutation> getRecipes() {
        return RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION.getAllRecipes();
    }
    
    public void setIngredients(final BlockTransmutation transmutation, final IIngredients ingredients) {
        final ImmutableList.Builder<List<ItemStack>> itemInputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        final ImmutableList.Builder<List<ItemStack>> itemOutputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        itemInputs.add((Object)transmutation.getInputDisplay());
        itemOutputs.add((Object)Collections.singletonList(transmutation.getOutputDisplay()));
        ingredients.setInputLists(VanillaTypes.ITEM, (List)itemInputs.build());
        ingredients.setOutputLists(VanillaTypes.ITEM, (List)itemOutputs.build());
    }
    
    public void setRecipe(final IRecipeLayout recipeLayout, final BlockTransmutation transmutation, final IIngredients ingredients) {
        final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 22, 17);
        itemStacks.init(1, false, 94, 18);
        itemStacks.set(ingredients);
    }
}
