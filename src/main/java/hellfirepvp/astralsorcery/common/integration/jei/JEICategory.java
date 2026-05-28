package hellfirepvp.astralsorcery.common.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import java.util.Arrays;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.crafting.Recipe;

public abstract class JEICategory<T extends Recipe<?>> implements IRecipeCategory<T>
{
    private final String locTitle;
    private final ResourceLocation uid;
    
    public JEICategory(final ResourceLocation categoryId) {
        this(category(categoryId), categoryId);
    }
    
    public JEICategory(final String unlocTitle, final ResourceLocation uid) {
        this.locTitle = I18n.func_135052_a(unlocTitle, new Object[0]);
        this.uid = uid;
    }
    
    protected static String category(final ResourceLocation categoryId) {
        return String.format("jei.category.%s.%s", categoryId.func_110624_b(), categoryId.addTransientModifier());
    }
    
    protected static List<ItemStack> ingredientStacks(final Ingredient ingredient) {
        return Arrays.asList(ingredient.func_193365_a());
    }
    
    protected static void initFluidInput(final IGuiFluidStackGroup group, final int index, final int x, final int y) {
        group.init(index, true, x + 1, y + 1, 16, 16, 1000, false, (IDrawable)null);
    }
    
    protected static void initFluidOutput(final IGuiFluidStackGroup group, final int index, final int x, final int y) {
        group.init(index, false, x + 1, y + 1, 16, 16, 1000, false, (IDrawable)null);
    }
    
    public abstract List<T> getRecipes();
    
    public ResourceLocation getUid() {
        return this.uid;
    }
    
    public String getTitle() {
        return this.locTitle;
    }
}
