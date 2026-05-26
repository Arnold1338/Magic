package hellfirepvp.astralsorcery.common.integration.jei;

import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.jei.JEIInteractionResultHandler;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.constants.VanillaTypes;
import java.util.Collections;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.Font;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteractionContext;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.jei.JEIInteractionResultRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import java.util.List;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.integration.IntegrationJEI;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import java.text.DecimalFormat;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;

public class CategoryLiquidInteraction extends JEICategory<LiquidInteraction>
{
    private static final DecimalFormat FORMAT_CHANCE;
    private final IDrawable background;
    private final IDrawable icon;
    
    public CategoryLiquidInteraction(final IGuiHelper guiHelper) {
        super(IntegrationJEI.CATEGORY_LIQUID_INTERACTION);
        this.background = (IDrawable)guiHelper.createDrawable(AstralSorcery.key("textures/gui/jei/interaction.png"), 0, 0, 112, 54);
        this.icon = guiHelper.createDrawableIngredient((Object)new ItemStack((ItemLike)BlocksAS.CHALICE));
    }
    
    public Class<? extends LiquidInteraction> getRecipeClass() {
        return LiquidInteraction.class;
    }
    
    public IDrawable getBackground() {
        return this.background;
    }
    
    public IDrawable getIcon() {
        return this.icon;
    }
    
    @Override
    public List<LiquidInteraction> getRecipes() {
        return RecipeTypesAS.TYPE_LIQUID_INTERACTION.getAllRecipes();
    }
    
    public void draw(final LiquidInteraction recipe, final PoseStack renderStack, final double mouseX, final double mouseY) {
        this.icon.draw(renderStack, 3, 36);
        this.icon.draw(renderStack, 93, 36);
        JEIInteractionResultRegistry.get(recipe.getResult().getId()).ifPresent(handler -> handler.drawRecipe(recipe, renderStack, mouseX, mouseY));
        final FluidStack testMatch1 = new FluidStack(recipe.getReactant1(), 1000);
        final FluidStack testMatch2 = new FluidStack(recipe.getReactant2(), 1000);
        final LiquidInteractionContext ctx = new LiquidInteractionContext(testMatch1, testMatch2);
        final Collection<LiquidInteraction> sameInteractions = RecipeTypesAS.TYPE_LIQUID_INTERACTION.findMatchingRecipes(ctx);
        if (!sameInteractions.isEmpty()) {
            final int totalWeight = sameInteractions.stream().mapToInt(LiquidInteraction::getWeight).sum();
            final float perc = recipe.getWeight() / (float)totalWeight * 100.0f;
            final Font fr = Minecraft.getInstance().font;
            final MutableComponent txt = (MutableComponent)Component.translatable("jei.astralsorcery.tip.chance");
            final int width = fr.func_238414_a_((FormattedCharSequence)txt);
            fr.func_243248_b(renderStack, (Component)txt, (float)(74 - width), 44.0f, 3355443);
        }
    }
    
    public void setIngredients(final LiquidInteraction recipe, final IIngredients ingredients) {
        final ImmutableList.Builder<List<FluidStack>> fluidInputs = (ImmutableList.Builder<List<FluidStack>>)ImmutableList.builder();
        fluidInputs.add((Object)Collections.singletonList(recipe.getReactant1()));
        fluidInputs.add((Object)Collections.singletonList(recipe.getReactant2()));
        ingredients.setInputLists(VanillaTypes.FLUID, (List)fluidInputs.build());
        JEIInteractionResultRegistry.get(recipe.getResult().getId()).ifPresent(handler -> handler.addToRecipeIngredients(recipe, ingredients));
    }
    
    public void setRecipe(final IRecipeLayout recipeLayout, final LiquidInteraction recipe, final IIngredients ingredients) {
        final IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        fluidStacks.init(0, true, 3, 19, 16, 16, recipe.getReactant1().getAmount(), false, (IDrawable)null);
        fluidStacks.init(1, true, 93, 19, 16, 16, recipe.getReactant2().getAmount(), false, (IDrawable)null);
        fluidStacks.set(ingredients);
        JEIInteractionResultRegistry.get(recipe.getResult().getId()).ifPresent(handler -> handler.addToRecipeLayout(recipeLayout, recipe, ingredients));
    }
    
    static {
        FORMAT_CHANCE = new DecimalFormat("0.00");
    }
}
