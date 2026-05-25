package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

public class DefaultWrapperContext extends RecipeCraftingContext<IHandlerRecipe, IItemHandler>
{
    private final IItemHandler handler;
    private final Level world;
    
    public DefaultWrapperContext(final IItemHandler handler, final Level world) {
        this.handler = handler;
        this.world = world;
    }
    
    public IItemHandler getHandler() {
        return this.handler;
    }
    
    public Level getWorld() {
        return this.world;
    }
}
