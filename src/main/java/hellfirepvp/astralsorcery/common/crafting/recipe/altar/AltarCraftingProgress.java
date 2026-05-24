package hellfirepvp.astralsorcery.common.crafting.recipe.altar;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public interface AltarCraftingProgress
{
    boolean tryProcess(final TileAltar p0, final ActiveSimpleAltarRecipe p1, final CompoundTag p2, final int p3, final int p4);
}
