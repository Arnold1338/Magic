package hellfirepvp.astralsorcery.datagen.data.recipes.interaction;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.FluidsAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResult;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.ResultDropItem;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.item.Items;
import net.minecraft.world.level.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.level.level.material.Fluids;
import hellfirepvp.astralsorcery.common.crafting.builder.LiquidInteractionBuilder;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;

public class InteractionRecipeProvider
{
    public static void registerLiquidInteractionRecipes(final Consumer<IFinishedRecipe> registrar) {
        LiquidInteractionBuilder.builder(AstralSorcery.key("water_lava_cobblestone")).setReactant1(new FluidStack((Fluid)Fluids.field_204546_a, 10)).setChanceConsumeReactant1(0.0f).setReactant2(new FluidStack((Fluid)Fluids.field_204547_b, 10)).setChanceConsumeReactant2(0.0f).setWeight(6).setResult(ResultDropItem.dropItem(new ItemStack((ItemLike)Items.field_221585_m))).build(registrar);
        LiquidInteractionBuilder.builder(AstralSorcery.key("water_lava_stone")).setReactant1(new FluidStack((Fluid)Fluids.field_204546_a, 10)).setChanceConsumeReactant1(0.1f).setReactant2(new FluidStack((Fluid)Fluids.field_204547_b, 10)).setChanceConsumeReactant2(0.1f).setWeight(3).setResult(ResultDropItem.dropItem(new ItemStack((ItemLike)Items.field_221574_b))).build(registrar);
        LiquidInteractionBuilder.builder(AstralSorcery.key("water_lava_obsidian")).setReactant1(new FluidStack((Fluid)Fluids.field_204546_a, 100)).setChanceConsumeReactant1(0.5f).setReactant2(new FluidStack((Fluid)Fluids.field_204547_b, 100)).setChanceConsumeReactant2(0.5f).setWeight(1).setResult(ResultDropItem.dropItem(new ItemStack((ItemLike)Items.field_221655_bP))).build(registrar);
        LiquidInteractionBuilder.builder(AstralSorcery.key("liquidstarlight_water_ice")).setReactant1(new FluidStack((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE, 10)).setReactant2(new FluidStack((Fluid)Fluids.field_204546_a, 10)).setResult(ResultDropItem.dropItem(new ItemStack((ItemLike)Items.field_221770_cu))).build(registrar);
        LiquidInteractionBuilder.builder(AstralSorcery.key("liquidstarlight_lava_sand")).setReactant1(new FluidStack((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE, 10)).setChanceConsumeReactant1(0.15f).setReactant2(new FluidStack((Fluid)Fluids.field_204547_b, 10)).setChanceConsumeReactant2(0.15f).setWeight(49).setResult(ResultDropItem.dropItem(new ItemStack((ItemLike)Items.field_221548_A))).build(registrar);
        LiquidInteractionBuilder.builder(AstralSorcery.key("liquidstarlight_lava_aquamarine")).setReactant1(new FluidStack((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE, 10)).setChanceConsumeReactant1(0.5f).setReactant2(new FluidStack((Fluid)Fluids.field_204547_b, 10)).setChanceConsumeReactant2(0.5f).setWeight(1).setResult(ResultDropItem.dropItem(new ItemStack((ItemLike)BlocksAS.AQUAMARINE_SAND_ORE))).build(registrar);
    }
}
