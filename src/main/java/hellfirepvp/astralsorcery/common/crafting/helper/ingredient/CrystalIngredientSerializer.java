package hellfirepvp.astralsorcery.common.crafting.helper.ingredient;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class CrystalIngredientSerializer implements IIngredientSerializer<CrystalIngredient>
{
    public CrystalIngredient parse(final JsonObject json) {
        final boolean hasToBeAttuned = JSONUtils.func_151209_a(json, "hasToBeAttuned", false);
        final boolean hasToBeCelestial = JSONUtils.func_151209_a(json, "hasToBeCelestial", false);
        final boolean canBeAttuned = JSONUtils.func_151209_a(json, "canBeAttuned", true);
        final boolean canBeCelestialCrystal = JSONUtils.func_151209_a(json, "canBeCelestialCrystal", true);
        return new CrystalIngredient(hasToBeAttuned, hasToBeCelestial, canBeAttuned, canBeCelestialCrystal);
    }
    
    public CrystalIngredient parse(final FriendlyByteBuf buffer) {
        return new CrystalIngredient(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }
    
    public void write(final FriendlyByteBuf buffer, final CrystalIngredient ingredient) {
        buffer.writeBoolean(ingredient.hasToBeAttuned());
        buffer.writeBoolean(ingredient.hasToBeCelestial());
        buffer.writeBoolean(ingredient.canBeAttuned());
        buffer.writeBoolean(ingredient.canBeCelestialCrystal());
    }
}
