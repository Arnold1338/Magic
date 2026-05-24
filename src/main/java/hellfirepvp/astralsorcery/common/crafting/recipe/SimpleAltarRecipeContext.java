package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.RecipeCraftingContext;

public class SimpleAltarRecipeContext extends RecipeCraftingContext<SimpleAltarRecipe, IItemHandler>
{
    private final TileAltar altar;
    private final Player crafter;
    private final LogicalSide side;
    private boolean ignoreStarlightRequirement;
    
    public SimpleAltarRecipeContext(final Player crafter, final LogicalSide side, final TileAltar altar) {
        this.ignoreStarlightRequirement = false;
        this.altar = altar;
        this.crafter = crafter;
        this.side = side;
    }
    
    public SimpleAltarRecipeContext setIgnoreStarlightRequirement(final boolean ignoreStarlightRequirement) {
        this.ignoreStarlightRequirement = ignoreStarlightRequirement;
        return this;
    }
    
    public LogicalSide getSide() {
        return this.side;
    }
    
    public Player getCrafter() {
        return this.crafter;
    }
    
    public boolean ignoreStarlightRequirement() {
        return this.ignoreStarlightRequirement;
    }
    
    public TileAltar getAltar() {
        return this.altar;
    }
}
