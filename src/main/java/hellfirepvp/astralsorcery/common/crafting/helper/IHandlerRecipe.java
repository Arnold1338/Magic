package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.IItemHandler;

public interface IHandlerRecipe<I extends IItemHandler> extends Recipe<Container>
{
    boolean matches(final I p0, final World p1);
    
    default boolean func_77569_a(final Container inv, final World worldIn) {
        return false;
    }
}
