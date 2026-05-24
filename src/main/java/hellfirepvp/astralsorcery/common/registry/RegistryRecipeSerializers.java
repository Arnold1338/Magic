package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.Container;
import hellfirepvp.astralsorcery.common.crafting.custom.RecipeDyeableChangeColor;
import hellfirepvp.astralsorcery.common.crafting.serializer.LiquidInteractionSerializer;
import hellfirepvp.astralsorcery.common.crafting.serializer.SimpleAltarRecipeSerializer;
import hellfirepvp.astralsorcery.common.crafting.serializer.BlockTransmutationSerializer;
import hellfirepvp.astralsorcery.common.crafting.serializer.LiquidInfusionSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.serializer.WellRecipeSerializer;

public class RegistryRecipeSerializers
{
    private RegistryRecipeSerializers() {
    }
    
    public static void init() {
        RecipeSerializersAS.WELL_LIQUEFACTION_SERIALIZER = register(new WellRecipeSerializer());
        RecipeSerializersAS.LIQUID_INFUSION_SERIALIZER = register(new LiquidInfusionSerializer());
        RecipeSerializersAS.BLOCK_TRANSMUTATION_SERIALIZER = register(new BlockTransmutationSerializer());
        RecipeSerializersAS.ALTAR_RECIPE_SERIALIZER = register(new SimpleAltarRecipeSerializer());
        RecipeSerializersAS.LIQUID_INTERACTION_SERIALIZER = register(new LiquidInteractionSerializer());
        RecipeSerializersAS.CUSTOM_CHANGE_WAND_COLOR_SERIALIZER = register(new RecipeDyeableChangeColor.IlluminationWandColorSerializer());
        RecipeSerializersAS.CUSTOM_CHANGE_GATEWAY_COLOR_SERIALIZER = register(new RecipeDyeableChangeColor.CelestialGatewayColorSerializer());
    }
    
    private static <C extends IInventory, R extends Recipe<C>, T extends IRecipeSerializer<R>> T register(final T serializer) {
        AstralSorcery.getProxy().getRegistryPrimer().register(serializer);
        return serializer;
    }
}
