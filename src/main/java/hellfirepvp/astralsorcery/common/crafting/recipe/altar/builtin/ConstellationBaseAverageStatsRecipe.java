package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import java.util.Iterator;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import java.util.List;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.resources.ResourceLocation;

public class ConstellationBaseAverageStatsRecipe extends ConstellationBaseItemRecipe
{
    public ConstellationBaseAverageStatsRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
    }
    
    public static ConstellationBaseAverageStatsRecipe convertToThis(final SimpleAltarRecipe other) {
        return new ConstellationBaseAverageStatsRecipe(other.func_199560_c(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }
    
    @Nonnull
    @Override
    public ItemStack getOutputForRender(final Iterable<ItemStack> inventoryContents) {
        final ItemStack out = super.getOutputForRender(inventoryContents);
        this.setStats(out, inventoryContents);
        return out;
    }
    
    @Nonnull
    @Override
    public List<ItemStack> getOutputs(final TileAltar altar) {
        final List<ItemStack> out = super.getOutputs(altar);
        out.forEach(stack -> this.setStats(stack, altar.getInventory()));
        return out;
    }
    
    private void setStats(final ItemStack out, final Iterable<ItemStack> inventoryContents) {
        if (!(out.getItem() instanceof CrystalAttributeItem)) {
            return;
        }
        int count = 0;
        final CrystalAttributes.Builder builder = CrystalAttributes.Builder.newBuilder(true);
        for (final ItemStack stack : inventoryContents) {
            if (stack.getItem() instanceof CrystalAttributeItem) {
                final CrystalAttributes attr = ((CrystalAttributeItem)stack.getItem()).getAttributes(stack);
                if (attr == null) {
                    continue;
                }
                builder.addAll(attr);
                ++count;
            }
        }
        final CrystalAttributes attr2 = builder.buildAverage(count);
        if (!attr2.isEmpty()) {
            ((CrystalAttributeItem)out.getItem()).setAttributes(out, attr2);
        }
    }
}
