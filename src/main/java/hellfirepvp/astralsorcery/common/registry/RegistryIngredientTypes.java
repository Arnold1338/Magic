package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredient;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.FluidIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredientSerializer;
import hellfirepvp.astralsorcery.common.lib.IngredientSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.FluidIngredientSerializer;

public class RegistryIngredientTypes
{
    private RegistryIngredientTypes() {
    }
    
    public static void init() {
        IngredientSerializersAS.FLUID_SERIALIZER = (IIngredientSerializer<FluidIngredient>)new FluidIngredientSerializer();
        IngredientSerializersAS.CRYSTAL_SERIALIZER = (IIngredientSerializer<CrystalIngredient>)new CrystalIngredientSerializer();
        CraftingHelper.register(AstralSorcery.key("fluid"), (IIngredientSerializer)IngredientSerializersAS.FLUID_SERIALIZER);
        CraftingHelper.register(AstralSorcery.key("crystal"), (IIngredientSerializer)IngredientSerializersAS.CRYSTAL_SERIALIZER);
    }
}
