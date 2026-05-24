package hellfirepvp.astralsorcery.common.integration.crt;

import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.RecipeType;
import java.util.Iterator;
import java.util.Map;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import net.minecraft.world.item.crafting.Recipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import java.util.function.Function;
import java.util.Arrays;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;

@ZenRegister
@ZenCodeType.Name("mods.astralsorcery.AltarManager")
public class AltarManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(String name, final String altarType, final IItemStack output, final IIngredient[][] ingredients, final int duration, final int starlightRequired) {
        name = this.fixRecipeName(name);
        if (Arrays.stream(AltarType.values()).map((Function<? super AltarType, ?>)Enum::name).noneMatch(s -> s.equalsIgnoreCase(altarType))) {
            throw new IllegalArgumentException("Unknown Astral Sorcery Altar Type: " + altarType);
        }
        if (ingredients.length != 5) {
            throw new IllegalArgumentException("Astral Sorcery Altar ingredients needs to be a 5x5 array with all values filled. Use <item:minecraft:air> to pad it out!");
        }
        for (final IIngredient[] ingredient : ingredients) {
            if (ingredient.length != 5) {
                throw new IllegalArgumentException("Astral Sorcery Altar ingredients needs to be a 5x5 array with all values filled. Use <item:minecraft:air> to pad it out!");
            }
        }
        final AltarRecipeGrid.Builder builder = AltarRecipeGrid.builder();
        builder.patternLine("ABCDE").patternLine("FGHIJ").patternLine("KLMNO").patternLine("PQRST").patternLine("UVWXY");
        int index = 65;
        for (final IIngredient[] array : ingredients) {
            final IIngredient[] ingredient2 = array;
            for (final IIngredient iIngredient : array) {
                builder.key((char)index, iIngredient.asVanillaIngredient());
                ++index;
            }
        }
        final SimpleAltarRecipe recipe = new SimpleAltarRecipe(new ResourceLocation(name), AltarType.valueOf(altarType.toUpperCase()), duration, starlightRequired, builder.build());
        recipe.addOutput(output.getInternal());
        CraftTweakerAPI.apply((IAction)new ActionAddRecipe((IRecipeManager)this, (Recipe)recipe));
    }
    
    @ZenCodeType.Method
    public void addRecipe(String name, final String altarType, final IItemStack output, final String[] pattern, final Map<String, IIngredient> ingredients, final int duration, final int starlightRequired) {
        name = this.fixRecipeName(name);
        if (Arrays.stream(AltarType.values()).map((Function<? super AltarType, ?>)Enum::name).noneMatch(s -> s.equalsIgnoreCase(altarType))) {
            throw new IllegalArgumentException("Unknown Astral Sorcery Altar Type: " + altarType);
        }
        if (pattern.length != 5) {
            throw new IllegalArgumentException("Astral Sorcery Altar ingredients needs to be a 5x5 array with all values filled.");
        }
        if (ingredients.keySet().stream().anyMatch(s -> s.length() != 1)) {
            throw new IllegalArgumentException("Cannot have multiple characters as pattern key!");
        }
        final AltarRecipeGrid.Builder builder = AltarRecipeGrid.builder();
        for (final String strings : pattern) {
            builder.patternLine(strings);
        }
        for (final String character : ingredients.keySet()) {
            builder.key(character.charAt(0), ingredients.get(character).asVanillaIngredient());
        }
        final SimpleAltarRecipe recipe = new SimpleAltarRecipe(new ResourceLocation(name), AltarType.valueOf(altarType.toUpperCase()), duration, starlightRequired, builder.build());
        recipe.addOutput(output.getInternal());
        CraftTweakerAPI.apply((IAction)new ActionAddRecipe((IRecipeManager)this, (Recipe)recipe));
    }
    
    public void removeRecipe(final IItemStack output) {
        throw new UnsupportedOperationException("Cannot remove Altar recipes by their output, remove them by their name instead!");
    }
    
    public RecipeType<SimpleAltarRecipe> getRecipeType() {
        return RecipeTypesAS.TYPE_ALTAR.getType();
    }
}
