package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crafting.custom.RecipeDyeableChangeColor;
import hellfirepvp.astralsorcery.common.crafting.serializer.LiquidInteractionSerializer;
import hellfirepvp.astralsorcery.common.crafting.serializer.SimpleAltarRecipeSerializer;
import hellfirepvp.astralsorcery.common.crafting.serializer.BlockTransmutationSerializer;
import hellfirepvp.astralsorcery.common.crafting.serializer.LiquidInfusionSerializer;
import hellfirepvp.astralsorcery.common.crafting.serializer.WellRecipeSerializer;
import net.minecraft.resources.ResourceLocation;

public class RecipeSerializersAS
{
    public static final ResourceLocation WELL_LIQUEFACTION;
    public static final ResourceLocation LIQUID_INFUSION;
    public static final ResourceLocation BLOCK_TRANSMUTATION;
    public static final ResourceLocation SIMPLE_ALTAR_CRAFTING;
    public static final ResourceLocation LIQUID_INTERACTION;
    public static final ResourceLocation CUSTOM_CHANGE_WAND_COLOR;
    public static final ResourceLocation CUSTOM_CHANGE_GATEWAY_COLOR;
    public static WellRecipeSerializer WELL_LIQUEFACTION_SERIALIZER;
    public static LiquidInfusionSerializer LIQUID_INFUSION_SERIALIZER;
    public static BlockTransmutationSerializer BLOCK_TRANSMUTATION_SERIALIZER;
    public static SimpleAltarRecipeSerializer ALTAR_RECIPE_SERIALIZER;
    public static LiquidInteractionSerializer LIQUID_INTERACTION_SERIALIZER;
    public static RecipeDyeableChangeColor.IlluminationWandColorSerializer CUSTOM_CHANGE_WAND_COLOR_SERIALIZER;
    public static RecipeDyeableChangeColor.CelestialGatewayColorSerializer CUSTOM_CHANGE_GATEWAY_COLOR_SERIALIZER;
    
    private RecipeSerializersAS() {
    }
    
    static {
        WELL_LIQUEFACTION = AstralSorcery.key("lightwell");
        LIQUID_INFUSION = AstralSorcery.key("infuser");
        BLOCK_TRANSMUTATION = AstralSorcery.key("block_transmutation");
        SIMPLE_ALTAR_CRAFTING = AstralSorcery.key("altar");
        LIQUID_INTERACTION = AstralSorcery.key("liquid_interaction");
        CUSTOM_CHANGE_WAND_COLOR = AstralSorcery.key("change_wand_color");
        CUSTOM_CHANGE_GATEWAY_COLOR = AstralSorcery.key("change_gateway_color");
    }
}
