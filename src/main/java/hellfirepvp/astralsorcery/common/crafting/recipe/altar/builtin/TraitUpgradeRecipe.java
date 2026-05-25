package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import java.util.Collections;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.lib.AltarRecipeEffectsAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;

public class TraitUpgradeRecipe extends SimpleAltarRecipe
{
    public TraitUpgradeRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
        this.addAltarEffect(AltarRecipeEffectsAS.UPGRADE_ALTAR);
    }
    
    public static TraitUpgradeRecipe convertToThis(final SimpleAltarRecipe other) {
        return new TraitUpgradeRecipe(other.func_199560_c(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }
    
    @Nonnull
    @Override
    public List<ItemStack> getOutputs(final TileAltar altar) {
        return Collections.emptyList();
    }
    
    @Override
    public boolean matches(final LogicalSide side, final Player crafter, final TileAltar altar, final boolean ignoreStarlightRequirement) {
        return altar.getAltarType() == AltarType.CONSTELLATION && super.matches(side, crafter, altar, ignoreStarlightRequirement);
    }
    
    @Override
    public void onRecipeCompletion(final TileAltar altar, final ActiveSimpleAltarRecipe activeRecipe) {
        super.onRecipeCompletion(altar, activeRecipe);
        ResearchManager.informCraftedAltar(altar, activeRecipe, new ItemStack((ItemLike)BlocksAS.ALTAR_RADIANCE));
        altar.getLevel().func_180501_a(altar.getBlockState(), BlocksAS.ALTAR_RADIANCE.defaultBlockState(), 3);
    }
}
