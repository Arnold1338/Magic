package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.RecipeCraftingContext;

public class LiquidInteractionContext extends RecipeCraftingContext<LiquidInteraction, IItemHandler>
{
    private final FluidStack contentTank1;
    private final FluidStack contentTank2;
    
    public LiquidInteractionContext(final FluidStack contentTank1, final FluidStack contentTank2) {
        this.contentTank1 = contentTank1;
        this.contentTank2 = contentTank2;
    }
    
    public FluidStack getContentTank1() {
        return this.contentTank1;
    }
    
    public FluidStack getContentTank2() {
        return this.contentTank2;
    }
}
