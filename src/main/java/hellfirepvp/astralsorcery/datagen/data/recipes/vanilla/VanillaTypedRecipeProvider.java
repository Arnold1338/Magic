package hellfirepvp.astralsorcery.datagen.data.recipes.vanilla;

import net.minecraft.world.item.crafting.SpecialRecipeSerializer;
import net.minecraft.data.CustomRecipeBuilder;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.datagen.data.recipes.builder.ResultCookingRecipeBuilder;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.datagen.data.recipes.builder.SimpleShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraft.world.level.item.Items;
import hellfirepvp.astralsorcery.datagen.data.recipes.builder.SimpleShapedRecipeBuilder;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.datagen.data.recipes.builder.StoneCuttingRecipeBuilder;
import net.minecraft.world.level.item.crafting.Ingredient;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;

public class VanillaTypedRecipeProvider
{
    public static void registerStoneCutterRecipes(final Consumer<IFinishedRecipe> registrar) {
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_ARCH).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_BRICKS).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_CHISELED).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_ENGRAVED).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_PILLAR).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_RUNED).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_SLAB, 2).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW }), (ItemLike)BlocksAS.MARBLE_STAIRS).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_BRICKS }), (ItemLike)BlocksAS.MARBLE_SLAB, 2).build(registrar, AstralSorcery.key("marble_slab_from_bricks"));
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_BRICKS }), (ItemLike)BlocksAS.MARBLE_STAIRS).build(registrar, AstralSorcery.key("marble_stairs_from_bricks"));
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_ARCH).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_BRICKS).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_CHISELED).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_ENGRAVED).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_PILLAR).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_RUNED).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_SLAB, 2).build(registrar);
        StoneCuttingRecipeBuilder.stoneCuttingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW }), (ItemLike)BlocksAS.BLACK_MARBLE_STAIRS).build(registrar);
    }
    
    public static void registerShapedRecipes(final Consumer<IFinishedRecipe> registrar) {
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)ItemsAS.TOME).patternLine(" P ").patternLine("ABA").patternLine(" A ").key('A', (ItemLike)ItemsAS.AQUAMARINE).key('B', (ItemLike)Items.field_151122_aG).key('P', (ItemLike)ItemsAS.PARCHMENT).build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)ItemsAS.PARCHMENT, 4).patternLine(" P ").patternLine("PAP").patternLine(" P ").key('A', (ItemLike)ItemsAS.AQUAMARINE).key('P', (ItemLike)Items.field_151121_aF).build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)ItemsAS.WAND).patternLine(" AE").patternLine(" MA").patternLine("M  ").key('A', (ItemLike)ItemsAS.AQUAMARINE).key('M', (ItemLike)BlocksAS.MARBLE_RAW).key('E', (ITag.INamedTag<Item>)Tags.Items.ENDER_PEARLS).build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_ARCH, 2).patternLine("MM").key('M', (ItemLike)BlocksAS.MARBLE_RAW).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_BRICKS, 4).patternLine("MM").patternLine("MM").key('M', (ItemLike)BlocksAS.MARBLE_RAW).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_CHISELED, 4).patternLine(" M ").patternLine("M M").patternLine(" M ").key('M', (ItemLike)BlocksAS.MARBLE_RAW).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_ENGRAVED, 5).patternLine(" M ").patternLine("MMM").patternLine(" M ").key('M', (ItemLike)BlocksAS.MARBLE_RAW).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_PILLAR, 2).patternLine("M").patternLine("M").key('M', (ItemLike)BlocksAS.MARBLE_RAW).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_RUNED, 3).patternLine("MCM").key('M', (ItemLike)BlocksAS.MARBLE_RAW).key('C', (ItemLike)BlocksAS.MARBLE_CHISELED).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_SLAB, 6).patternLine("MMM").key('M', (ItemLike)BlocksAS.MARBLE_RAW).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_SLAB, 6).patternLine("MMM").key('M', (ItemLike)BlocksAS.MARBLE_BRICKS).subDirectory("marble").build(registrar, AstralSorcery.key("marble_slab_from_bricks"));
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_STAIRS, 8).patternLine("M  ").patternLine("MM ").patternLine("MMM").key('M', (ItemLike)BlocksAS.MARBLE_RAW).subDirectory("marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.MARBLE_STAIRS, 8).patternLine("M  ").patternLine("MM ").patternLine("MMM").key('M', (ItemLike)BlocksAS.MARBLE_BRICKS).subDirectory("marble").build(registrar, AstralSorcery.key("marble_stairs_from_bricks"));
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_RAW, 8).patternLine("MMM").patternLine("MCM").patternLine("MMM").key('M', (ItemLike)BlocksAS.MARBLE_RAW).key('C', (ITag.INamedTag<Item>)ItemTags.field_219775_L).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_ARCH, 2).patternLine("MM").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_BRICKS, 4).patternLine("MM").patternLine("MM").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_CHISELED, 4).patternLine(" M ").patternLine("M M").patternLine(" M ").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_ENGRAVED, 5).patternLine(" M ").patternLine("MMM").patternLine(" M ").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_PILLAR, 2).patternLine("M").patternLine("M").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_RUNED, 3).patternLine("MCM").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).key('C', (ItemLike)BlocksAS.BLACK_MARBLE_CHISELED).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_SLAB, 6).patternLine("MMM").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.BLACK_MARBLE_STAIRS, 8).patternLine("M  ").patternLine("MM ").patternLine("MMM").key('M', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).subDirectory("black_marble").build(registrar);
        SimpleShapedRecipeBuilder.shapedRecipe((ItemLike)BlocksAS.STARMETAL).patternLine("III").patternLine("III").patternLine("III").key('I', (ItemLike)ItemsAS.STARMETAL_INGOT).build(registrar);
        SimpleShapelessRecipeBuilder.shapelessRecipe((ItemLike)ItemsAS.STARMETAL_INGOT, 9).addIngredient((ItemLike)BlocksAS.STARMETAL).build(registrar);
    }
    
    public static void registerCookingRecipes(final Consumer<IFinishedRecipe> registrar) {
        ResultCookingRecipeBuilder.smeltingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.STARMETAL_ORE }), new ItemStack((ItemLike)ItemsAS.STARMETAL_INGOT), 1.8f, 200).build(registrar);
        ResultCookingRecipeBuilder.smeltingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.AQUAMARINE_SAND_ORE }), new ItemStack((ItemLike)ItemsAS.AQUAMARINE, 4), 1.8f, 200).build(registrar);
        ResultCookingRecipeBuilder.blastingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.STARMETAL_ORE }), new ItemStack((ItemLike)ItemsAS.STARMETAL_INGOT), 2.5f, 80).build(registrar);
        ResultCookingRecipeBuilder.blastingRecipe(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.AQUAMARINE_SAND_ORE }), new ItemStack((ItemLike)ItemsAS.AQUAMARINE, 3), 0.2f, 40).build(registrar);
    }
    
    public static void registerCustomRecipes(final Consumer<IFinishedRecipe> registrar) {
        CustomRecipeBuilder.func_218656_a((SpecialRecipeSerializer)RecipeSerializersAS.CUSTOM_CHANGE_WAND_COLOR_SERIALIZER).func_200499_a((Consumer)registrar, RecipeSerializersAS.CUSTOM_CHANGE_WAND_COLOR.toString());
        CustomRecipeBuilder.func_218656_a((SpecialRecipeSerializer)RecipeSerializersAS.CUSTOM_CHANGE_GATEWAY_COLOR_SERIALIZER).func_200499_a((Consumer)registrar, RecipeSerializersAS.CUSTOM_CHANGE_GATEWAY_COLOR.toString());
    }
}
