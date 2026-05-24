package hellfirepvp.astralsorcery.common.integration.crt;

import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.level.item.crafting.RecipeType;
import com.blamejared.crafttweaker.api.brackets.CommandStringDisplayable;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import net.minecraft.world.level.entity.EntityType;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.ResultSpawnEntity;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import net.minecraft.world.level.item.crafting.Recipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResult;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.ResultDropItem;
import net.minecraft.resources.ResourceLocation;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IItemStack;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;

@ZenRegister
@ZenCodeType.Name("mods.astralsorcery,LiquidInteractionManager")
public class LiquidInteractionManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(String name, final IItemStack output, final IFluidStack reactant1, final float chanceConsumeReactant1, final IFluidStack reactant2, final float chanceConsumeReactant2, final int weight) {
        name = this.fixRecipeName(name);
        final ResourceLocation recipeId = new ResourceLocation(name);
        final LiquidInteraction recipe = new LiquidInteraction(recipeId, reactant1.getInternal(), chanceConsumeReactant1, reactant2.getInternal(), chanceConsumeReactant2, weight, ResultDropItem.dropItem(output.getInternal()));
        CraftTweakerAPI.apply((IAction)new ActionAddRecipe((IRecipeManager)this, (Recipe)recipe, "Drop Item"));
    }
    
    @ZenCodeType.Method
    public void addRecipe(String name, final MCEntityType output, final IFluidStack reactant1, final float chanceConsumeReactant1, final IFluidStack reactant2, final float chanceConsumeReactant2, final int weight) {
        name = this.fixRecipeName(name);
        final ResourceLocation recipeId = new ResourceLocation(name);
        final LiquidInteraction recipe = new LiquidInteraction(recipeId, reactant1.getInternal(), chanceConsumeReactant1, reactant2.getInternal(), chanceConsumeReactant2, weight, ResultSpawnEntity.spawnEntity((EntityType<?>)output.getInternal()));
        CraftTweakerAPI.apply((IAction)new ActionAddRecipe((IRecipeManager)this, (Recipe)recipe, "Spawn Entity"));
    }
    
    public void removeRecipe(final IItemStack output) {
        CraftTweakerAPI.apply((IAction)new ActionRemoveRecipe((IRecipeManager)this, iRecipe -> {
            final LiquidInteraction recipe = (LiquidInteraction)iRecipe;
            final InteractionResult result = recipe.getResult();
            if (result instanceof ResultDropItem) {
                final ResultDropItem resultDropItem = (ResultDropItem)result;
                return output.matches((IItemStack)new MCItemStackMutable(resultDropItem.getOutput()));
            }
            else {
                return false;
            }
        }).describeDefaultRemoval((CommandStringDisplayable)output));
    }
    
    @ZenCodeType.Method
    public void removeRecipe(final MCEntityType entityType) {
        CraftTweakerAPI.apply((IAction)new ActionRemoveRecipe((IRecipeManager)this, iRecipe -> {
            final LiquidInteraction recipe = (LiquidInteraction)iRecipe;
            final InteractionResult result = recipe.getResult();
            if (result instanceof ResultSpawnEntity) {
                final ResultSpawnEntity resultSpawnEntity = (ResultSpawnEntity)result;
                return entityType.getInternal() == resultSpawnEntity.getEntityType();
            }
            else {
                return false;
            }
        }).describeDefaultRemoval((CommandStringDisplayable)entityType));
    }
    
    public RecipeType<LiquidInteraction> getRecipeType() {
        return RecipeTypesAS.TYPE_LIQUID_INTERACTION.getType();
    }
}
