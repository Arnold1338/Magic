package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.world.item.crafting.IRecipeSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Container;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public abstract class CustomMatcherRecipe extends BaseHandlerRecipe<IItemHandler>
{
    protected CustomMatcherRecipe(final ResourceLocation recipeId) {
        super(recipeId);
    }
    
    public final boolean func_194133_a(final int width, final int height) {
        return false;
    }
    
    @Override
    public final boolean matches(final IItemHandler handler, final Level world) {
        return false;
    }
    
    public final ItemStack func_77572_b(final Container inv) {
        return this.func_77571_b();
    }
    
    public final ItemStack func_77571_b() {
        return ItemStack.EMPTY;
    }
    
    public abstract CustomRecipeSerializer<?> getSerializer();
}
