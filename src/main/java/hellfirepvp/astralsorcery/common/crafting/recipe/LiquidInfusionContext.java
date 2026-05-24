package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.RecipeCraftingContext;

public class LiquidInfusionContext extends RecipeCraftingContext<LiquidInfusion, IItemHandler>
{
    private final TileInfuser infuser;
    private final Player crafter;
    private final LogicalSide side;
    
    public LiquidInfusionContext(final TileInfuser infuser, final Player crafter, final LogicalSide side) {
        this.infuser = infuser;
        this.crafter = crafter;
        this.side = side;
    }
    
    public TileInfuser getInfuser() {
        return this.infuser;
    }
    
    public Player getCrafter() {
        return this.crafter;
    }
    
    public LogicalSide getSide() {
        return this.side;
    }
}
