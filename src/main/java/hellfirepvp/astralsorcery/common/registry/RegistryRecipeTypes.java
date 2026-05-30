package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.core.Registry;
import hellfirepvp.astralsorcery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.astralsorcery.common.crafting.helper.IHandlerRecipe;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectVortexPlane;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectUpgradeAltar;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectPillarSparkle;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectPillarLightbeams;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectLuminescenceFlare;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectLiquidBurst;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectLargeDustSwirl;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectGatewayEdge;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectFocusEdge;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectFocusDustSwirl;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectAltarRandomSparkle;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectAltarFocusSparkle;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectAltarDefaultSparkle;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.EffectAltarDefaultLightbeams;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.BuiltInEffectTraitRelayHighlight;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.BuiltInEffectTraitFocusCircle;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.BuiltInEffectDiscoveryCentralBeam;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.BuiltInEffectConstellationFinish;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.BuiltInEffectConstellationLines;
import hellfirepvp.astralsorcery.common.lib.AltarRecipeEffectsAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.BuiltInEffectAttunementSparkle;
import hellfirepvp.astralsorcery.common.crafting.nojson.WorldFreezingRegistry;
import hellfirepvp.astralsorcery.common.crafting.nojson.WorldMeltableRegistry;
import hellfirepvp.astralsorcery.common.crafting.nojson.AttunementCraftingRegistry;
import hellfirepvp.astralsorcery.common.crafting.nojson.LiquidStarlightCraftingRegistry;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteractionContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipeContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutationContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusionContext;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefactionContext;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.ResolvingRecipeType;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;

public class RegistryRecipeTypes
{
    private RegistryRecipeTypes() {
    }
    
    public static void init() {
        RecipeTypesAS.TYPE_WELL = new ResolvingRecipeType<IItemHandler, WellLiquefaction, WellLiquefactionContext>("well", WellLiquefaction.class, (recipe, context) -> recipe.matches(context.getInput()));
        RecipeTypesAS.TYPE_INFUSION = new ResolvingRecipeType<IItemHandler, LiquidInfusion, LiquidInfusionContext>("infusion", LiquidInfusion.class, (recipe, context) -> recipe.matches(context.getInfuser(), context.getCrafter(), context.getSide()));
        RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION = new ResolvingRecipeType<IItemHandler, BlockTransmutation, BlockTransmutationContext>("block_transmutation", BlockTransmutation.class, (recipe, context) -> recipe.matches(context.getWorld(), context.getPos(), context.getState(), context.getConstellation()));
        RecipeTypesAS.TYPE_ALTAR = new ResolvingRecipeType<IItemHandler, SimpleAltarRecipe, SimpleAltarRecipeContext>("altar", SimpleAltarRecipe.class, (recipe, context) -> recipe.matches(context.getSide(), context.getCrafter(), context.getAltar(), context.ignoreStarlightRequirement()));
        RecipeTypesAS.TYPE_LIQUID_INTERACTION = new ResolvingRecipeType<IItemHandler, LiquidInteraction, LiquidInteractionContext>("liquid_interaction", LiquidInteraction.class, (recipe, context) -> recipe.matches(context.getContentTank1(), context.getContentTank2()));
        LiquidStarlightCraftingRegistry.INSTANCE.init();
        AttunementCraftingRegistry.INSTANCE.init();
        WorldMeltableRegistry.INSTANCE.init();
        WorldFreezingRegistry.INSTANCE.init();
    }
    
    public static void initAltarEffects() {
        AltarRecipeEffectsAS.BUILTIN_ATTUNEMENT_SPARKLE = registerEffect(new BuiltInEffectAttunementSparkle());
        AltarRecipeEffectsAS.BUILTIN_CONSTELLATION_LINES = registerEffect(new BuiltInEffectConstellationLines());
        AltarRecipeEffectsAS.BUILTIN_CONSTELLATION_FINISH = registerEffect(new BuiltInEffectConstellationFinish());
        AltarRecipeEffectsAS.BUILTIN_DISCOVERY_CENTRAL_BEAM = registerEffect(new BuiltInEffectDiscoveryCentralBeam());
        AltarRecipeEffectsAS.BUILTIN_TRAIT_FOCUS_CIRCLE = registerEffect(new BuiltInEffectTraitFocusCircle());
        AltarRecipeEffectsAS.BUILTIN_TRAIT_RELAY_HIGHLIGHT = registerEffect(new BuiltInEffectTraitRelayHighlight());
        AltarRecipeEffectsAS.ALTAR_DEFAULT_LIGHTBEAM = registerEffect(new EffectAltarDefaultLightbeams());
        AltarRecipeEffectsAS.ALTAR_DEFAULT_SPARKLE = registerEffect(new EffectAltarDefaultSparkle());
        AltarRecipeEffectsAS.ALTAR_FOCUS_SPARKLE = registerEffect(new EffectAltarFocusSparkle());
        AltarRecipeEffectsAS.ALTAR_RANDOM_SPARKLE = registerEffect(new EffectAltarRandomSparkle());
        AltarRecipeEffectsAS.FOCUS_DUST_SWIRL = registerEffect(new EffectFocusDustSwirl());
        AltarRecipeEffectsAS.FOCUS_EDGE = registerEffect(new EffectFocusEdge());
        AltarRecipeEffectsAS.GATEWAY_EDGE = registerEffect(new EffectGatewayEdge());
        AltarRecipeEffectsAS.LARGE_DUST_SWIRL = registerEffect(new EffectLargeDustSwirl());
        AltarRecipeEffectsAS.LIQUID_BURST = registerEffect(new EffectLiquidBurst());
        AltarRecipeEffectsAS.LUMINESCENCE_FLARE = registerEffect(new EffectLuminescenceFlare());
        AltarRecipeEffectsAS.PILLAR_LIGHTBEAMS = registerEffect(new EffectPillarLightbeams());
        AltarRecipeEffectsAS.PILLAR_SPARKLE = registerEffect(new EffectPillarSparkle());
        AltarRecipeEffectsAS.UPGRADE_ALTAR = registerEffect(new EffectUpgradeAltar());
        AltarRecipeEffectsAS.VORTEX_PLANE = registerEffect(new EffectVortexPlane());
    }
    
    private static <T extends AltarRecipeEffect> T registerEffect(final T recipeEffect) {

        AstralSorcery.getProxy().getRegistryPrimer().register(recipeEffect);
        return recipeEffect;
    }
    
    private static <C extends IItemHandler, T extends IHandlerRecipe<C>, R extends RecipeCraftingContext<T, C>, S extends ResolvingRecipeType<C, T, R>> S register(final S recipeType) {
        Registry.func_218322_a(Registry.field_218367_H, recipeType.getRegistryName(), (Object)recipeType.getType());
        return recipeType;
    }
}
