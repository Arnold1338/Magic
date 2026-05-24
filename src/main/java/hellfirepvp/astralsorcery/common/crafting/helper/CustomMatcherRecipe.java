package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
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
    public final boolean matches(final IItemHandler handler, final World world) {
        return false;
    }
    
    public final ItemStack func_77572_b(final IInventory inv) {
        return this.func_77571_b();
    }
    
    public final ItemStack func_77571_b() {
        return ItemStack.field_190927_a;
    }
    
    public abstract CustomRecipeSerializer<?> getSerializer();
}
