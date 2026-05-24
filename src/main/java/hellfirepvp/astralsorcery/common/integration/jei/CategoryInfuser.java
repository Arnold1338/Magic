package hellfirepvp.astralsorcery.common.integration.jei;

import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.constants.VanillaTypes;
import net.minecraftforge.fluids.FluidStack;
import java.util.Collections;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import java.util.List;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.integration.IntegrationJEI;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;

public class CategoryInfuser extends JEICategory<LiquidInfusion>
{
    private final IDrawable background;
    private final IDrawable icon;
    
    public CategoryInfuser(final IGuiHelper guiHelper) {
        super(IntegrationJEI.CATEGORY_INFUSER);
        this.background = (IDrawable)guiHelper.createDrawable(AstralSorcery.key("textures/gui/jei/infuser.png"), 0, 0, 116, 162);
        this.icon = guiHelper.createDrawableIngredient((Object)new ItemStack((ItemLike)BlocksAS.INFUSER));
    }
    
    public Class<? extends LiquidInfusion> getRecipeClass() {
        return LiquidInfusion.class;
    }
    
    public IDrawable getBackground() {
        return this.background;
    }
    
    public IDrawable getIcon() {
        return this.icon;
    }
    
    @Override
    public List<LiquidInfusion> getRecipes() {
        return RecipeTypesAS.TYPE_INFUSION.getAllRecipes();
    }
    
    public void setIngredients(final LiquidInfusion liquidInfusion, final IIngredients ingredients) {
        final ImmutableList.Builder<List<FluidStack>> fluidInputs = (ImmutableList.Builder<List<FluidStack>>)ImmutableList.builder();
        final ImmutableList.Builder<List<ItemStack>> itemInputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        final ImmutableList.Builder<List<ItemStack>> itemOutputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        itemInputs.add((Object)Arrays.asList(liquidInfusion.getItemInput().func_193365_a()));
        itemOutputs.add((Object)Collections.singletonList(liquidInfusion.getOutputForRender((Iterable<ItemStack>)Collections.emptyList())));
        final FluidStack fInput = new FluidStack(liquidInfusion.getLiquidInput(), 1000);
        for (int i = 0; i < 12; ++i) {
            fluidInputs.add((Object)Collections.singletonList(fInput.copy()));
        }
        ingredients.setInputLists(VanillaTypes.FLUID, (List)fluidInputs.build());
        ingredients.setInputLists(VanillaTypes.ITEM, (List)itemInputs.build());
        ingredients.setOutputLists(VanillaTypes.ITEM, (List)itemOutputs.build());
    }
    
    public void setRecipe(final IRecipeLayout recipeLayout, final LiquidInfusion liquidInfusion, final IIngredients ingredients) {
        final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        final IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        itemStacks.init(0, true, 49, 95);
        JEICategory.initFluidInput(fluidStacks, 1, 30, 57);
        JEICategory.initFluidInput(fluidStacks, 2, 49, 57);
        JEICategory.initFluidInput(fluidStacks, 3, 68, 57);
        JEICategory.initFluidInput(fluidStacks, 4, 11, 76);
        JEICategory.initFluidInput(fluidStacks, 5, 87, 76);
        JEICategory.initFluidInput(fluidStacks, 6, 11, 95);
        JEICategory.initFluidInput(fluidStacks, 7, 87, 95);
        JEICategory.initFluidInput(fluidStacks, 8, 11, 114);
        JEICategory.initFluidInput(fluidStacks, 9, 87, 114);
        JEICategory.initFluidInput(fluidStacks, 10, 30, 133);
        JEICategory.initFluidInput(fluidStacks, 11, 49, 133);
        JEICategory.initFluidInput(fluidStacks, 12, 68, 133);
        itemStacks.init(13, false, 48, 18);
        itemStacks.set(ingredients);
        fluidStacks.set(ingredients);
    }
}
