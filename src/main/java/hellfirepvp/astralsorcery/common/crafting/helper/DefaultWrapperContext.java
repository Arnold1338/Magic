package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.world.level.level.Level;
import net.minecraftforge.items.IItemHandler;

public class DefaultWrapperContext extends RecipeCraftingContext<IHandlerRecipe, IItemHandler>
{
    private final IItemHandler handler;
    private final World world;
    
    public DefaultWrapperContext(final IItemHandler handler, final World world) {
        this.handler = handler;
        this.world = world;
    }
    
    public IItemHandler getHandler() {
        return this.handler;
    }
    
    public World getWorld() {
        return this.world;
    }
}
