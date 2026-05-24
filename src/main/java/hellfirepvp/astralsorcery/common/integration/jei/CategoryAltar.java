package hellfirepvp.astralsorcery.common.integration.jei;

import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import net.minecraft.util.Mth;
import mezz.jei.api.gui.IRecipeLayout;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import mezz.jei.api.constants.VanillaTypes;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import java.util.Collections;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import java.util.List;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.AstralSorcery;
import mezz.jei.api.helpers.IGuiHelper;
import hellfirepvp.astralsorcery.common.block.tile.BlockAltar;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import mezz.jei.api.gui.drawable.IDrawable;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;

public class CategoryAltar extends JEICategory<SimpleAltarRecipe>
{
    private final IDrawable background;
    private final IDrawable icon;
    private final AltarType altarType;
    
    public CategoryAltar(final ResourceLocation id, final String textureRef, final BlockAltar altarRef, final IGuiHelper guiHelper) {
        super(id);
        this.background = (IDrawable)guiHelper.createDrawable(AstralSorcery.key(String.format("textures/gui/jei/%s.png", textureRef)), 0, 0, 116, 162);
        this.icon = guiHelper.createDrawableIngredient((Object)new ItemStack((ItemLike)altarRef));
        this.altarType = altarRef.getAltarType();
    }
    
    public Class<? extends SimpleAltarRecipe> getRecipeClass() {
        return SimpleAltarRecipe.class;
    }
    
    public IDrawable getBackground() {
        return this.background;
    }
    
    public IDrawable getIcon() {
        return this.icon;
    }
    
    public AltarType getAltarType() {
        return this.altarType;
    }
    
    @Override
    public List<SimpleAltarRecipe> getRecipes() {
        return RecipeTypesAS.TYPE_ALTAR.getRecipes(recipe -> recipe.getAltarType().equals(this.getAltarType()));
    }
    
    public void draw(final SimpleAltarRecipe recipe, final PoseStack matrixStack, final double mouseX, final double mouseY) {
        if (recipe.getFocusConstellation() != null) {
            RenderSystem.enableBlend();
            Blending.DEFAULT.apply();
            final IConstellation cst = recipe.getFocusConstellation();
            RenderingConstellationUtils.renderConstellationIntoGUI(Color.BLACK, cst, matrixStack, 0.0f, 0.0f, 0.0f, 50.0f, 50.0f, 1.2000000476837158, () -> 0.9f, true, false);
            RenderSystem.disableBlend();
        }
    }
    
    public void setIngredients(final SimpleAltarRecipe altarRecipe, final IIngredients ingredients) {
        final ImmutableList.Builder<List<ItemStack>> itemInputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        final ImmutableList.Builder<List<ItemStack>> itemOutputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        itemOutputs.add((Object)Collections.singletonList(altarRecipe.getOutputForRender((Iterable<ItemStack>)Collections.emptyList())));
        final AltarRecipeGrid grid = altarRecipe.getInputs();
        for (int slot = 0; slot < 25; ++slot) {
            itemInputs.add((Object)JEICategory.ingredientStacks(grid.getIngredient(slot)));
        }
        for (final WrappedIngredient relayInput : altarRecipe.getRelayInputs()) {
            itemInputs.add((Object)JEICategory.ingredientStacks(relayInput.getIngredient()));
        }
        ingredients.setInputLists(VanillaTypes.ITEM, (List)itemInputs.build());
        ingredients.setOutputLists(VanillaTypes.ITEM, (List)itemOutputs.build());
    }
    
    public void setRecipe(final IRecipeLayout recipeLayout, final SimpleAltarRecipe altarRecipe, final IIngredients ingredients) {
        final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        final int step = 19;
        final int xOffset = 11;
        final int yOffset = 57;
        for (int yy = 0; yy < 5; ++yy) {
            for (int xx = 0; xx < 5; ++xx) {
                final int slot = xx + yy * 5;
                itemStacks.init(slot, true, xOffset + step * xx, yOffset + step * yy);
            }
        }
        final int centerX = 49;
        final int centerY = 95;
        for (int additional = altarRecipe.getRelayInputs().size(), i = 0; i < additional; ++i) {
            double part = i / (double)additional * 2.0 * 3.141592653589793;
            part = Mth.func_151237_a(part, 0.0, 6.283185307179586);
            part += 3.141592653589793;
            final double xAdd = Math.sin(part) * 60.0;
            final double yAdd = Math.cos(part) * 60.0;
            itemStacks.init(25 + i, true, Mth.func_76128_c(centerX + xAdd), Mth.func_76128_c(centerY + yAdd));
        }
        itemStacks.init(itemStacks.getGuiIngredients().size(), false, 48, 18);
        itemStacks.set(ingredients);
    }
}
