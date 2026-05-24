package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import java.util.function.Consumer;
import java.util.List;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.resources.ResourceLocation;

public class CrystalCountRecipe extends ConstellationBaseAverageStatsRecipe
{
    public CrystalCountRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
    }
    
    public static CrystalCountRecipe convertToThis(final SimpleAltarRecipe other) {
        return new CrystalCountRecipe(other.func_199560_c(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }
    
    @Nonnull
    @Override
    public ItemStack getOutputForRender(final Iterable<ItemStack> inventoryContents) {
        final ItemStack out = super.getOutputForRender(inventoryContents);
        this.setAmount(out);
        return out;
    }
    
    @Nonnull
    @Override
    public List<ItemStack> getOutputs(final TileAltar altar) {
        final List<ItemStack> out = super.getOutputs(altar);
        out.forEach(this::setAmount);
        return out;
    }
    
    private void setAmount(final ItemStack out) {
        if (out.getItem() instanceof CrystalAttributeItem) {
            final CrystalAttributes attr = ((CrystalAttributeItem)out.getItem()).getAttributes(out);
            if (attr != null && !attr.isEmpty()) {
                out.func_190920_e(CrystalCalculations.getSizeCraftingAmount(attr));
            }
        }
    }
}
