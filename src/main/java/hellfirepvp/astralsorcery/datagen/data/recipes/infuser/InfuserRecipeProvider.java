package hellfirepvp.astralsorcery.datagen.data.recipes.infuser;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import hellfirepvp.astralsorcery.common.lib.FluidsAS;

import hellfirepvp.astralsorcery.common.crafting.builder.LiquidInfusionBuilder;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;

public class InfuserRecipeProvider
{
    public static void registerInfuserRecipes(final Consumer<IFinishedRecipe> registrar) {
        LiquidInfusionBuilder.builder((Object<?>)ItemsAS.AQUAMARINE).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)ItemsAS.AQUAMARINE).setOutput((ItemLike)ItemsAS.RESONATING_GEM).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221792_df).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.GLASS_PANES).setOutput((ItemLike)ItemsAS.GLASS_LENS).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Blocks.field_235334_I_).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)Blocks.field_235334_I_).setOutput(new ItemStack((ItemLike)Items.field_151074_bl, 21)).multiplyDuration(0.6f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Blocks.field_235398_nh_).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)Blocks.field_235398_nh_).setOutput(new ItemStack((ItemLike)Items.field_234760_kn_, 2)).multiplyDuration(0.6f).setFluidConsumptionChance(0.2f).setConsumeMultipleFluids(true).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221548_A).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.SAND).setOutput((ItemLike)Items.field_221776_cx).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Blocks.field_150346_d).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)Blocks.field_150346_d).setOutput((ItemLike)Items.field_221581_i).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151016_H).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.GUNPOWDER).setOutput((ItemLike)Items.field_151114_aO).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151079_bi).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.ENDER_PEARLS).setOutput((ItemLike)Items.field_151061_bv).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151137_ax).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.DUSTS_REDSTONE).setOutput((ItemLike)Items.field_151016_H).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151103_aS).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.BONES).setOutput(new ItemStack((ItemLike)Items.field_196106_bc, 4)).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151072_bj).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.RODS_BLAZE).setOutput(new ItemStack((ItemLike)Items.field_151065_br, 4)).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151123_aH).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.SLIMEBALLS).setOutput((ItemLike)Items.field_151064_bs).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151172_bF).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.CROPS_CARROT).setOutput((ItemLike)Items.field_151150_bK).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_151127_ba).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)Items.field_151127_ba).setOutput((ItemLike)Items.field_151060_bw).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221552_E).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.ORES_IRON).setOutput(new ItemStack((ItemLike)Items.field_151042_j, 3)).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221551_D).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)Blocks.field_150352_o).setOutput(new ItemStack((ItemLike)Items.field_151043_k, 3)).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221652_an).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.ORES_LAPIS).setOutput((ItemLike)Items.field_221654_ao).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221762_cq).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.ORES_REDSTONE).setOutput((ItemLike)Items.field_221858_em).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221730_ca).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.ORES_DIAMOND).setOutput(new ItemStack((ItemLike)Items.field_151045_i, 5)).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221733_dC).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.ORES_EMERALD).setOutput(new ItemStack((ItemLike)Items.field_151166_bC, 5)).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)Items.field_221650_am).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ITag.INamedTag<Item>)Tags.Items.GLASS).setOutput((ItemLike)Items.field_221770_cu).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)BlocksAS.INFUSED_WOOD).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)BlocksAS.INFUSED_WOOD).setOutput((ItemLike)BlocksAS.INFUSED_WOOD_INFUSED).multiplyDuration(0.5f).setFluidConsumptionChance(0.1f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)ItemsAS.CRYSTAL_AXE).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)ItemsAS.CRYSTAL_AXE).setOutput((ItemLike)ItemsAS.INFUSED_CRYSTAL_AXE).setConsumeMultipleFluids(true).setAcceptChaliceInput(false).setCopyNBTToOutputs(true).setFluidConsumptionChance(1.0f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)ItemsAS.CRYSTAL_PICKAXE).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)ItemsAS.CRYSTAL_PICKAXE).setOutput((ItemLike)ItemsAS.INFUSED_CRYSTAL_PICKAXE).setConsumeMultipleFluids(true).setAcceptChaliceInput(false).setCopyNBTToOutputs(true).setFluidConsumptionChance(1.0f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)ItemsAS.CRYSTAL_SHOVEL).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)ItemsAS.CRYSTAL_SHOVEL).setOutput((ItemLike)ItemsAS.INFUSED_CRYSTAL_SHOVEL).setConsumeMultipleFluids(true).setAcceptChaliceInput(false).setCopyNBTToOutputs(true).setFluidConsumptionChance(1.0f).build(registrar);
        LiquidInfusionBuilder.builder((Object<?>)ItemsAS.CRYSTAL_SWORD).setLiquidInput((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).setItemInput((ItemLike)ItemsAS.CRYSTAL_SWORD).setOutput((ItemLike)ItemsAS.INFUSED_CRYSTAL_SWORD).setConsumeMultipleFluids(true).setAcceptChaliceInput(false).setCopyNBTToOutputs(true).setFluidConsumptionChance(1.0f).build(registrar);
    }
}
