package hellfirepvp.astralsorcery.common.crafting.helper.ingredient;

import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import hellfirepvp.astralsorcery.common.lib.IngredientSerializersAS;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.List;
import java.util.Collection;
import net.minecraftforge.common.crafting.StackList;
import net.minecraft.world.level.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.level.item.ItemStack;
import java.util.ArrayList;
import java.util.stream.Stream;
import net.minecraft.world.level.item.crafting.Ingredient;

public class CrystalIngredient extends Ingredient
{
    private final boolean hasToBeAttuned;
    private final boolean hasToBeCelestial;
    private final boolean canBeAttuned;
    private final boolean canBeCelestialCrystal;
    
    public CrystalIngredient(final boolean hasToBeAttuned, final boolean hasToBeCelestial) {
        this(hasToBeAttuned, hasToBeCelestial, true, true);
    }
    
    public CrystalIngredient(final boolean hasToBeAttuned, final boolean hasToBeCelestial, final boolean canBeAttuned, final boolean canBeCelestialCrystal) {
        super((Stream)getItems(hasToBeAttuned, hasToBeCelestial, canBeAttuned, canBeCelestialCrystal));
        this.hasToBeAttuned = hasToBeAttuned;
        this.hasToBeCelestial = hasToBeCelestial;
        this.canBeAttuned = canBeAttuned;
        this.canBeCelestialCrystal = canBeCelestialCrystal;
    }
    
    private static Stream<Ingredient.IItemList> getItems(final boolean hasToBeAttuned, final boolean hasToBeCelestial, boolean canBeAttuned, boolean canBeCelestialCrystal) {
        if (hasToBeAttuned) {
            canBeAttuned = true;
        }
        if (hasToBeCelestial) {
            canBeCelestialCrystal = true;
        }
        final List<ItemStack> stacks = new ArrayList<ItemStack>();
        if (hasToBeAttuned) {
            if (hasToBeCelestial) {
                stacks.add(new ItemStack((ItemLike)ItemsAS.ATTUNED_CELESTIAL_CRYSTAL));
            }
            else {
                stacks.add(new ItemStack((ItemLike)ItemsAS.ATTUNED_ROCK_CRYSTAL));
                if (canBeCelestialCrystal) {
                    stacks.add(new ItemStack((ItemLike)ItemsAS.ATTUNED_CELESTIAL_CRYSTAL));
                }
            }
        }
        else if (hasToBeCelestial) {
            stacks.add(new ItemStack((ItemLike)ItemsAS.CELESTIAL_CRYSTAL));
            if (canBeAttuned) {
                stacks.add(new ItemStack((ItemLike)ItemsAS.ATTUNED_CELESTIAL_CRYSTAL));
            }
        }
        else {
            stacks.add(new ItemStack((ItemLike)ItemsAS.ROCK_CRYSTAL));
            if (canBeCelestialCrystal) {
                stacks.add(new ItemStack((ItemLike)ItemsAS.CELESTIAL_CRYSTAL));
            }
            if (canBeAttuned) {
                stacks.add(new ItemStack((ItemLike)ItemsAS.ATTUNED_ROCK_CRYSTAL));
                stacks.add(new ItemStack((ItemLike)ItemsAS.ATTUNED_CELESTIAL_CRYSTAL));
            }
        }
        return Stream.of((Ingredient.IItemList)new StackList((Collection)stacks));
    }
    
    public boolean hasToBeAttuned() {
        return this.hasToBeAttuned;
    }
    
    public boolean hasToBeCelestial() {
        return this.hasToBeCelestial;
    }
    
    public boolean canBeAttuned() {
        return this.canBeAttuned;
    }
    
    public boolean canBeCelestialCrystal() {
        return this.canBeCelestialCrystal;
    }
    
    public JsonElement func_200304_c() {
        final JsonObject object = new JsonObject();
        object.addProperty("type", CraftingHelper.getID((IIngredientSerializer)IngredientSerializersAS.CRYSTAL_SERIALIZER).toString());
        object.addProperty("hasToBeAttuned", Boolean.valueOf(this.hasToBeAttuned()));
        object.addProperty("hasToBeCelestial", Boolean.valueOf(this.hasToBeCelestial()));
        object.addProperty("canBeAttuned", Boolean.valueOf(this.canBeAttuned()));
        object.addProperty("canBeCelestialCrystal", Boolean.valueOf(this.canBeCelestialCrystal()));
        return (JsonElement)object;
    }
    
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return IngredientSerializersAS.CRYSTAL_SERIALIZER;
    }
}
