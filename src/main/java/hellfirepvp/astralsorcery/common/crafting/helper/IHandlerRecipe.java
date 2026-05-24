package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.world.level.Level;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.IItemHandler;

public interface IHandlerRecipe<I extends IItemHandler> extends Recipe<IInventory>
{
    boolean matches(final I p0, final World p1);
    
    default boolean func_77569_a(final IInventory inv, final World worldIn) {
        return false;
    }
}
