package hellfirepvp.astralsorcery.common.integration.crt;

import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.level.item.crafting.RecipeType;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipe;
import com.blamejared.crafttweaker.impl_native.blocks.ExpandBlockState;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import net.minecraft.world.level.item.crafting.Recipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;
import java.util.function.Consumer;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.util.block.BlockMatchInformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.level.block.Block;
import com.blamejared.crafttweaker.impl.tag.MCTag;
import net.minecraft.world.level.level.block.state.BlockState;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;

@ZenRegister
@ZenCodeType.Name("mods.astralsorcery.BlockTransmutationManager")
public class BlockTransmutationManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(final String name, final BlockState outState, final MCTag<Block> input, final double starlight, @ZenCodeType.Optional("null") final ResourceLocation constellationKey) {
        this.addTransmutation(name, outState, starlight, constellationKey, transmutation -> transmutation.addInputOption(new BlockMatchInformation((ITag<Block>)input.getInternal())));
    }
    
    @ZenCodeType.Method
    public void addRecipe(final String name, final BlockState outState, final BlockState input, final boolean exact, final double starlight, @ZenCodeType.Optional("null") final ResourceLocation constellationKey) {
        this.addTransmutation(name, outState, starlight, constellationKey, transmutation -> transmutation.addInputOption(new BlockMatchInformation(input, exact)));
    }
    
    private void addTransmutation(String name, final BlockState outState, final double starlight, @ZenCodeType.Optional("null") final ResourceLocation constellationKey, final Consumer<BlockTransmutation> addInputRequirements) {
        name = this.fixRecipeName(name);
        IWeakConstellation weakConstellation = null;
        if (constellationKey != null) {
            if (!RegistriesAS.REGISTRY_CONSTELLATIONS.containsKey(constellationKey)) {
                throw new IllegalArgumentException("Invalid constellation key: \"" + constellationKey + "\"");
            }
            final IConstellation constellation = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(constellationKey);
            if (!(constellation instanceof IWeakConstellation)) {
                throw new IllegalArgumentException("Constellation: \"" + constellationKey + "\" is not a weak constellation!");
            }
            weakConstellation = (IWeakConstellation)constellation;
        }
        final BlockTransmutation transmutation = new BlockTransmutation(new ResourceLocation(name), outState, starlight, weakConstellation);
        CraftTweakerAPI.apply((IAction)new ActionAddRecipe((IRecipeManager)this, (Recipe)transmutation));
    }
    
    @ZenCodeType.Method
    public void removeRecipe(final BlockState outputState) {
        this.removeRecipe(outputState, false);
    }
    
    @ZenCodeType.Method
    public void removeRecipe(final BlockState outputState, final boolean exact) {
        final BlockMatchInformation matcher = new BlockMatchInformation(outputState, exact);
        CraftTweakerAPI.apply((IAction)new ActionRemoveRecipe((IRecipeManager)this, iRecipe -> {
            if (iRecipe instanceof BlockTransmutation) {
                final BlockTransmutation recipe = (BlockTransmutation)iRecipe;
                return matcher.test(recipe.getOutput());
            }
            else {
                return false;
            }
        }, action -> "Removing Block Transmutation recipes that output " + ExpandBlockState.getCommandString(outputState)));
    }
    
    public void removeRecipe(final IItemStack output) {
        throw new UnsupportedOperationException("Cannot remove Astral Sorcery Block Transmutation recipes by IItemStacks, use the BlockState method instead!");
    }
    
    public RecipeType<BlockTransmutation> getRecipeType() {
        return RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION.getType();
    }
}
