package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredient;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.FluidIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class IngredientSerializersAS
{
    public static IIngredientSerializer<FluidIngredient> FLUID_SERIALIZER;
    public static IIngredientSerializer<CrystalIngredient> CRYSTAL_SERIALIZER;
    
    private IngredientSerializersAS() {
    }
}
