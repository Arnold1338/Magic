package hellfirepvp.astralsorcery.common.crafting.recipe.interaction.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResult;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import com.google.common.collect.Lists;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.ResultDropItem;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import mezz.jei.api.gui.IRecipeLayout;

public class JEIHandlerDropItem extends JEIInteractionResultHandler
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addToRecipeLayout(final IRecipeLayout recipeLayout, final LiquidInteraction recipe, final IIngredients ingredients) {
        final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(2, false, 47, 18);
        itemStacks.set(ingredients);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addToRecipeIngredients(final LiquidInteraction recipe, final IIngredients ingredients) {
        final ImmutableList.Builder<List<ItemStack>> itemOutputs = (ImmutableList.Builder<List<ItemStack>>)ImmutableList.builder();
        final InteractionResult result = recipe.getResult();
        if (result instanceof ResultDropItem) {
            itemOutputs.add((Object)Lists.newArrayList((Object[])new ItemStack[] { ((ResultDropItem)result).getOutput() }));
        }
        ingredients.setOutputLists(VanillaTypes.ITEM, (List)itemOutputs.build());
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawRecipe(final LiquidInteraction recipe, final PoseStack renderStack, final double mouseX, final double mouseY) {
    }
}
