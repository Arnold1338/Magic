package hellfirepvp.astralsorcery.common.integration.jei;

import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.constants.VanillaTypes;
import java.util.Collections;
import net.minecraftforge.fluids.FluidStack;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.integration.IntegrationJEI;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;

public class CategoryWell extends JEICategory<WellLiquefaction>
{
    private final IDrawable background;
    private final IDrawable icon;
    
    public CategoryWell(final IGuiHelper guiHelper) {
        super(IntegrationJEI.CATEGORY_WELL);
        this.background = (IDrawable)guiHelper.createDrawable(AstralSorcery.key("textures/gui/jei/lightwell.png"), 0, 0, 116, 54);
        this.icon = guiHelper.createDrawableIngredient((Object)new ItemStack((ItemLike)BlocksAS.WELL));
    }
    
    public Class<? extends WellLiquefaction> getRecipeClass() {
        return WellLiquefaction.class;
    }
    
    public IDrawable getBackground() {
        return this.background;
    }
    
    public IDrawable getIcon() {
        return this.icon;
    }
    
    public void draw(final WellLiquefaction recipe, final PoseStack renderStack, final double mouseX, final double mouseY) {
        this.icon.draw(renderStack, 46, 20);
    }
    
    @Override
    public List<WellLiquefaction> getRecipes() {
        return RecipeTypesAS.TYPE_WELL.getAllRecipes();
    }
    
    public void setIngredients(final WellLiquefaction wellLiquefaction, final IIngredients ingredients) {
        final ImmutableList.Builder<List<FluidStack>> fluidOutputs = (ImmutableList.Builder<List<FluidStack>>)ImmutableList.builder();
        final ImmutableList.Builder<List<ItemStack>> itemInputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        itemInputs.add((Object)JEICategory.ingredientStacks(wellLiquefaction.getInput()));
        fluidOutputs.add((Object)Collections.singletonList(new FluidStack(wellLiquefaction.getFluidOutput(), 1000)));
        ingredients.setInputLists(VanillaTypes.ITEM, (List)itemInputs.build());
        ingredients.setOutputLists(VanillaTypes.FLUID, (List)fluidOutputs.build());
    }
    
    public void setRecipe(final IRecipeLayout recipeLayout, final WellLiquefaction wellLiquefaction, final IIngredients ingredients) {
        final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        final IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        itemStacks.init(0, true, 2, 18);
        JEICategory.initFluidOutput(fluidStacks, 1, 94, 18);
        itemStacks.set(ingredients);
        fluidStacks.set(ingredients);
    }
}
