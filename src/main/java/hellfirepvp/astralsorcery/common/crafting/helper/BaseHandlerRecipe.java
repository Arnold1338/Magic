package hellfirepvp.astralsorcery.common.crafting.helper;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public abstract class BaseHandlerRecipe<I extends IItemHandler> implements IHandlerRecipe<I>
{
    private final ResourceLocation recipeId;
    private String group;
    
    protected BaseHandlerRecipe(final ResourceLocation recipeId) {
        this.group = "";
        this.recipeId = recipeId;
    }
    
    public void setGroup(final String group) {
        this.group = group;
    }
    
    public String func_193358_e() {
        return this.group;
    }
    
    public boolean func_192399_d() {
        return true;
    }
    
    public final ResourceLocation func_199560_c() {
        return this.recipeId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BaseHandlerRecipe<?> that = (BaseHandlerRecipe<?>)o;
        return this.recipeId.equals((Object)that.recipeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.recipeId);
    }
}
