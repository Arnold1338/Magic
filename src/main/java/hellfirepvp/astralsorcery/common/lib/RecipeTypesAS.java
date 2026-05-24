package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteractionContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipeContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutationContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusionContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefactionContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.ResolvingRecipeType;

public class RecipeTypesAS
{
    public static ResolvingRecipeType<IItemHandler, WellLiquefaction, WellLiquefactionContext> TYPE_WELL;
    public static ResolvingRecipeType<IItemHandler, LiquidInfusion, LiquidInfusionContext> TYPE_INFUSION;
    public static ResolvingRecipeType<IItemHandler, BlockTransmutation, BlockTransmutationContext> TYPE_BLOCK_TRANSMUTATION;
    public static ResolvingRecipeType<IItemHandler, SimpleAltarRecipe, SimpleAltarRecipeContext> TYPE_ALTAR;
    public static ResolvingRecipeType<IItemHandler, LiquidInteraction, LiquidInteractionContext> TYPE_LIQUID_INTERACTION;
    
    private RecipeTypesAS() {
    }
}
