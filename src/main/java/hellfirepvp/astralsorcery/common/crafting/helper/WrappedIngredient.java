package hellfirepvp.astralsorcery.common.crafting.helper;

import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import net.minecraftforge.common.crafting.CraftingHelper;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.IngredientHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class WrappedIngredient
{
    private final Ingredient ingredient;
    
    public WrappedIngredient(final Ingredient ingredient) {
        this.ingredient = ingredient;
    }
    
    public Ingredient getIngredient() {
        return this.ingredient;
    }
    
    public ItemStack getRandomMatchingStack(final long tick) {
        return IngredientHelper.getRandomVisibleStack(this.getIngredient(), tick);
    }
    
    @Nullable
    public static WrappedIngredient deserialize(final CompoundTag tag) {
        if (!tag.contains("ingredient")) {
            return null;
        }
        final JsonElement jsonElement = new JsonParser().parse(tag.getString("ingredient"));
        return new WrappedIngredient(CraftingHelper.getIngredient(jsonElement));
    }
    
    public CompoundTag serialize() {
        final CompoundTag tag = new CompoundTag();
        tag.putString("ingredient", this.ingredient.func_200304_c().toString());
        return tag;
    }
}
