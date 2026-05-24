package hellfirepvp.astralsorcery.common.crafting.recipe.interaction.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import mezz.jei.api.ingredients.IIngredients;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import mezz.jei.api.gui.IRecipeLayout;

public abstract class JEIInteractionResultHandler
{
    @OnlyIn(Dist.CLIENT)
    public abstract void addToRecipeLayout(final IRecipeLayout p0, final LiquidInteraction p1, final IIngredients p2);
    
    @OnlyIn(Dist.CLIENT)
    public abstract void addToRecipeIngredients(final LiquidInteraction p0, final IIngredients p1);
    
    @OnlyIn(Dist.CLIENT)
    public abstract void drawRecipe(final LiquidInteraction p0, final PoseStack p1, final double p2, final double p3);
}
