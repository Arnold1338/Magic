package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import java.util.List;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JSONUtils;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.resources.ResourceLocation;

public class ConstellationCopyStatsRecipe extends ConstellationBaseAverageStatsRecipe
{
    private static final String KEY_CONSTELLATION_SLOT = "constellationSlot";
    private int constellationSlot;
    
    public ConstellationCopyStatsRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
        this.constellationSlot = -1;
    }
    
    public static ConstellationCopyStatsRecipe convertToThis(final SimpleAltarRecipe other) {
        return new ConstellationCopyStatsRecipe(other.func_199560_c(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }
    
    public void setConstellationSlot(final int constellationSlot) {
        this.constellationSlot = constellationSlot;
    }
    
    public int getConstellationSlot() {
        return this.constellationSlot;
    }
    
    @Override
    public void deserializeAdditionalJson(final JsonObject recipeObject) throws JsonSyntaxException {
        super.deserializeAdditionalJson(recipeObject);
        if (JSONUtils.func_151204_g(recipeObject, "constellationSlot")) {
            this.constellationSlot = JSONUtils.func_151203_m(recipeObject, "constellationSlot");
        }
    }
    
    @Override
    public void serializeAdditionalJson(final JsonObject recipeObject) {
        super.serializeAdditionalJson(recipeObject);
        if (this.constellationSlot != -1) {
            recipeObject.addProperty("constellationSlot", (Number)this.constellationSlot);
        }
    }
    
    @Nonnull
    @Override
    public ItemStack getOutputForRender(final Iterable<ItemStack> inventoryContents) {
        final ItemStack out = super.getOutputForRender(inventoryContents);
        this.copyConstellation(out, inventoryContents);
        return out;
    }
    
    @Nonnull
    @Override
    public List<ItemStack> getOutputs(final TileAltar altar) {
        final List<ItemStack> out = super.getOutputs(altar);
        out.forEach(stack -> this.copyConstellation(stack, altar.getInventory()));
        return out;
    }
    
    private void copyConstellation(final ItemStack out, Iterable<ItemStack> inventoryContents) {
        if (out.getItem() instanceof ConstellationItem) {
            final ConstellationItem iOut = (ConstellationItem)out.getItem();
            if (iOut.getAttunedConstellation(out) == null || iOut.getTraitConstellation(out) == null) {
                if (this.constellationSlot >= 0) {
                    inventoryContents = Iterables.concat((Iterable)Lists.newArrayList((Object[])new ItemStack[] { (ItemStack)Iterables.get((Iterable)inventoryContents, this.constellationSlot, (Object)ItemStack.field_190927_a) }), (Iterable)inventoryContents);
                }
                for (final ItemStack stack : inventoryContents) {
                    if (stack.getItem() instanceof ConstellationItem) {
                        if (iOut.getAttunedConstellation(out) == null) {
                            final IWeakConstellation c = ((ConstellationItem)stack.getItem()).getAttunedConstellation(stack);
                            if (c != null) {
                                iOut.setAttunedConstellation(out, c);
                            }
                        }
                        if (iOut.getTraitConstellation(out) != null) {
                            continue;
                        }
                        final IMinorConstellation c2 = ((ConstellationItem)stack.getItem()).getTraitConstellation(stack);
                        if (c2 == null) {
                            continue;
                        }
                        iOut.setTraitConstellation(out, c2);
                    }
                }
            }
        }
    }
}
