package hellfirepvp.astralsorcery.common.crafting.recipe;

import hellfirepvp.astralsorcery.common.tile.TileWell;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.RecipeCraftingContext;

public class WellLiquefactionContext extends RecipeCraftingContext<WellLiquefaction, IItemHandler>
{
    private final ItemStack input;
    
    public WellLiquefactionContext(final TileWell well) {
        this(well.getCatalyst());
    }
    
    public WellLiquefactionContext(final ItemStack input) {
        this.input = input;
    }
    
    public ItemStack getInput() {
        return this.input;
    }
}
