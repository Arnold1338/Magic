package hellfirepvp.astralsorcery.datagen.data.recipes;

import hellfirepvp.astralsorcery.datagen.data.recipes.vanilla.VanillaTypedRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.interaction.InteractionRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.transmutation.BlockTransmutationRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.well.LightwellRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.infuser.InfuserRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.altar.RadianceAltarRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.altar.CelestialAltarRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.altar.AttunementAltarRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.altar.DiscoveryAltarRecipeProvider;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.RecipeProvider;

public class AstralRecipeProvider extends RecipeProvider
{
    public AstralRecipeProvider(final DataGenerator generatorIn) {
        super(generatorIn);
    }
    
    protected void func_200404_a(final Consumer<IFinishedRecipe> registrar) {
        DiscoveryAltarRecipeProvider.registerAltarRecipes(registrar);
        AttunementAltarRecipeProvider.registerAltarRecipes(registrar);
        CelestialAltarRecipeProvider.registerAltarRecipes(registrar);
        RadianceAltarRecipeProvider.registerAltarRecipes(registrar);
        InfuserRecipeProvider.registerInfuserRecipes(registrar);
        LightwellRecipeProvider.registerLightwellRecipes(registrar);
        BlockTransmutationRecipeProvider.registerTransmutationRecipes(registrar);
        InteractionRecipeProvider.registerLiquidInteractionRecipes(registrar);
        VanillaTypedRecipeProvider.registerStoneCutterRecipes(registrar);
        VanillaTypedRecipeProvider.registerShapedRecipes(registrar);
        VanillaTypedRecipeProvider.registerCookingRecipes(registrar);
        VanillaTypedRecipeProvider.registerCustomRecipes(registrar);
    }
}
