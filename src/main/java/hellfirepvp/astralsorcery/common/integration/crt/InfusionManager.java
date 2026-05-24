package hellfirepvp.astralsorcery.common.integration.crt;

import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.RecipeType;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import net.minecraft.world.item.ItemStack;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import net.minecraft.world.item.crafting.Recipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;

@ZenRegister
@ZenCodeType.Name("mods.astralsorcery.InfusionManager")
public class InfusionManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(String name, final IItemStack itemOutput, final IIngredient itemInput, final Fluid liquidInput, final int craftingTickTime, final float consumptionChance, final boolean consumeMultipleFluids, final boolean acceptChaliceInput, final boolean copyNBTToOutputs) {
        name = this.fixRecipeName(name);
        final ResourceLocation recipeId = new ResourceLocation(name);
        final LiquidInfusion recipe = new LiquidInfusion(recipeId, craftingTickTime, liquidInput, itemInput.asVanillaIngredient(), itemOutput.getInternal(), consumptionChance, consumeMultipleFluids, acceptChaliceInput, copyNBTToOutputs);
        CraftTweakerAPI.apply((IAction)new ActionAddRecipe((IRecipeManager)this, (Recipe)recipe));
    }
    
    public void removeRecipe(final IItemStack output) {
        CraftTweakerAPI.apply((IAction)new ActionRemoveRecipe((IRecipeManager)this, iRecipe -> {
            if (iRecipe instanceof LiquidInfusion) {
                final LiquidInfusion recipe = (LiquidInfusion)iRecipe;
                return output.matches((IItemStack)new MCItemStackMutable(recipe.getOutput(ItemStack.EMPTY)));
            }
            else {
                return false;
            }
        }, action -> "Removing \"" + action.getRecipeTypeName() + "\" recipes with output: " + output + "\""));
    }
    
    public RecipeType<LiquidInfusion> getRecipeType() {
        return RecipeTypesAS.TYPE_INFUSION.getType();
    }
}
