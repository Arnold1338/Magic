package hellfirepvp.astralsorcery.common.integration.crt;

import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.RecipeType;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipe;
import com.blamejared.crafttweaker.impl_native.fluid.ExpandFluid;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import net.minecraft.world.item.crafting.Recipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;
import java.awt.Color;
import net.minecraft.resources.ResourceLocation;
import com.blamejared.crafttweaker.api.item.IIngredient;
import net.minecraft.world.level.material.Fluid;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;

@ZenRegister
@ZenCodeType.Name("mods.astralsorcery.WellManager")
public class WellManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(String name, final Fluid output, final IIngredient input, final float productionMultiplier, final float shatterMultiplier, @ZenCodeType.OptionalInt(16733695) final int color) {
        name = this.fixRecipeName(name);
        final ResourceLocation recipeId = new ResourceLocation(name);
        final WellLiquefaction recipe = new WellLiquefaction(recipeId, input.asVanillaIngredient(), output, new Color(color, true), productionMultiplier, shatterMultiplier);
        CraftTweakerAPI.apply((IAction)new ActionAddRecipe((IRecipeManager)this, (Recipe)recipe));
    }
    
    public void removeRecipe(final IItemStack output) {
        throw new UnsupportedOperationException("Cannot remove Astral Sorcery Well Liquefaction recipes by IItemStacks, use the Fluid method instead!");
    }
    
    @ZenCodeType.Method
    public void removeRecipe(final Fluid output) {
        CraftTweakerAPI.apply((IAction)new ActionRemoveRecipe((IRecipeManager)this, iRecipe -> {
            if (iRecipe instanceof WellLiquefaction) {
                final WellLiquefaction recipe = (WellLiquefaction)iRecipe;
                return output == recipe.getFluidOutput();
            }
            else {
                return false;
            }
        }, action -> "Removing Well Liquefaction recipes that output " + ExpandFluid.getCommandString(output)));
    }
    
    public RecipeType<WellLiquefaction> getRecipeType() {
        return RecipeTypesAS.TYPE_WELL.getType();
    }
}
