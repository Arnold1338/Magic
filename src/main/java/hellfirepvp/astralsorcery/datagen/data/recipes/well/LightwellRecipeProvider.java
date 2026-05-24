package hellfirepvp.astralsorcery.datagen.data.recipes.well;

import net.minecraft.world.level.level.material.Fluids;
import net.minecraft.world.level.item.Items;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import net.minecraft.world.level.level.material.Fluid;
import hellfirepvp.astralsorcery.common.lib.FluidsAS;
import net.minecraft.world.level.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.crafting.builder.WellRecipeBuilder;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;

public class LightwellRecipeProvider
{
    public static void registerLightwellRecipes(final Consumer<IFinishedRecipe> registrar) {
        WellRecipeBuilder.builder(AstralSorcery.key("starlight_aquamarine")).setItemInput((ItemLike)ItemsAS.AQUAMARINE).setLiquidOutput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).color(new Color(43007)).productionMultiplier(0.45f).shatterMultiplier(9.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("starlight_resonating_gem")).setItemInput((ItemLike)ItemsAS.RESONATING_GEM).setLiquidOutput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).color(new Color(43007)).productionMultiplier(0.65f).shatterMultiplier(15.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("starlight_rock_crystal")).setItemInput((ItemLike)ItemsAS.ROCK_CRYSTAL).setLiquidOutput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).color(ColorsAS.ROCK_CRYSTAL).productionMultiplier(0.12f).shatterMultiplier(3.5f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("starlight_attuned_rock_crystal")).setItemInput((ItemLike)ItemsAS.ATTUNED_ROCK_CRYSTAL).setLiquidOutput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).color(ColorsAS.ROCK_CRYSTAL).productionMultiplier(0.14f).shatterMultiplier(5.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("starlight_celestial_crystal")).setItemInput((ItemLike)ItemsAS.CELESTIAL_CRYSTAL).setLiquidOutput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).color(ColorsAS.CELESTIAL_CRYSTAL).productionMultiplier(0.18f).shatterMultiplier(4.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("starlight_attuned_celestial_crystal")).setItemInput((ItemLike)ItemsAS.ATTUNED_CELESTIAL_CRYSTAL).setLiquidOutput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).color(ColorsAS.CELESTIAL_CRYSTAL).productionMultiplier(0.2f).shatterMultiplier(8.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("lava_magma_block")).setItemInput((ItemLike)Items.field_221958_gk).setLiquidOutput((Fluid)Fluids.field_204547_b).color(new Color(16725260)).productionMultiplier(0.7f).shatterMultiplier(40.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("lava_netherrack")).setItemInput((ItemLike)Items.field_221691_cH).setLiquidOutput((Fluid)Fluids.field_204547_b).color(new Color(16725260)).productionMultiplier(0.5f).shatterMultiplier(10.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("water_ice")).setItemInput((ItemLike)Items.field_221770_cu).setLiquidOutput((Fluid)Fluids.field_204546_a).color(new Color(5466623)).productionMultiplier(1.0f).shatterMultiplier(30.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("water_packed_ice")).setItemInput((ItemLike)Items.field_221898_fg).setLiquidOutput((Fluid)Fluids.field_204546_a).color(new Color(5466623)).productionMultiplier(0.4f).shatterMultiplier(60.0f).build(registrar);
        WellRecipeBuilder.builder(AstralSorcery.key("water_snow")).setItemInput((ItemLike)Items.field_221772_cv).setLiquidOutput((Fluid)Fluids.field_204546_a).color(new Color(5466623)).productionMultiplier(1.5f).shatterMultiplier(20.0f).build(registrar);
    }
}
