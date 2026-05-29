package hellfirepvp.astralsorcery.datagen.data.recipes.altar;

import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.NBTCopyRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationBaseItemRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationBaseNBTCopyRecipe;
import java.util.List;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;

import hellfirepvp.astralsorcery.common.base.Mods;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.lib.AltarRecipeEffectsAS;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.item.ItemResonator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import hellfirepvp.astralsorcery.common.lib.FluidsAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeTypeHandler;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import hellfirepvp.astralsorcery.common.lib.TagsAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredient;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;

import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.crafting.builder.SimpleAltarRecipeBuilder;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;

public class RadianceAltarRecipeProvider
{
    public static void registerAltarRecipes(final Consumer<IFinishedRecipe> registrar) {
        registerRecipes(registrar);
        registerConstellationRecipes(registrar);
    }
    
    private static void registerRecipes(final Consumer<IFinishedRecipe> registrar) {
        SimpleAltarRecipeBuilder.builder().createRecipe((Object<?>)ItemsAS.MANTLE, AltarType.RADIANCE).setStarlightRequirement(0.6f).setInputs(AltarRecipeGrid.builder().patternLine("     ").patternLine("R C R").patternLine("RIAIR").patternLine("SI IS").patternLine("S   S").key('C', new CrystalIngredient(false, false)).key('A', (ItemLike)Items.field_151027_R).key('I', (ItemLike)ItemsAS.ILLUMINATION_POWDER).key('R', (ItemLike)ItemsAS.RESONATING_GEM).key('S', TagsAS.Items.DUSTS_STARDUST)).addRelayInput(TagsAS.Items.INGOTS_STARMETAL).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addRelayInput((ITag.INamedTag<Item>)Tags.Items.FEATHERS).addRelayInput((ITag.INamedTag<Item>)Tags.Items.ENDER_PEARLS).addOutput((ItemLike)ItemsAS.MANTLE).build(registrar);
        SimpleAltarRecipeBuilder.ofType(AltarRecipeTypeHandler.NBT_COPY).createRecipe(NameUtil.suffixPath(ItemsAS.RESONATOR.getRegistryName(), "_upgrade_ichosic"), AltarType.RADIANCE).modify(recipe -> recipe.addNBTCopyMatchIngredient((ItemLike)ItemsAS.RESONATOR)).setFocusConstellation(ConstellationsAS.octans).setStarlightRequirement(0.8f).setInputs(AltarRecipeGrid.builder().patternLine("  I  ").patternLine("S R S").patternLine(" SLS ").patternLine("  S  ").patternLine(" GGG ").key('R', (ItemLike)ItemsAS.RESONATOR).key('L', (Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE).key('S', TagsAS.Items.DUSTS_STARDUST).key('I', (ItemLike)ItemsAS.ILLUMINATION_POWDER).key('G', (ItemLike)ItemsAS.RESONATING_GEM)).addOutput(ItemResonator.setCurrentUpgradeUnsafe(ItemResonator.setUpgradeUnlocked(new ItemStack((ItemLike)ItemsAS.RESONATOR), ItemResonator.ResonatorUpgrade.STARLIGHT, ItemResonator.ResonatorUpgrade.FLUID_FIELDS), ItemResonator.ResonatorUpgrade.FLUID_FIELDS)).build(registrar);
        SimpleAltarRecipeBuilder.builder().createRecipe((Object<?>)BlocksAS.FOUNTAIN, AltarType.RADIANCE).setStarlightRequirement(0.5f).setInputs(AltarRecipeGrid.builder().patternLine("WSSSW").patternLine("WMCMW").patternLine("WGRGW").patternLine(" MRM ").patternLine("     ").key('C', new CrystalIngredient(false, false)).key('G', (ITag.INamedTag<Item>)Tags.Items.INGOTS_GOLD).key('W', (ItemLike)BlocksAS.INFUSED_WOOD_PLANKS).key('S', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).key('M', TagsAS.Items.INGOTS_STARMETAL).key('R', (ItemLike)ItemsAS.RESONATING_GEM)).addOutput((ItemLike)BlocksAS.FOUNTAIN).addRelayInput(TagsAS.Items.FORGE_GEM_AQUAMARINE).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addRelayInput(TagsAS.Items.FORGE_GEM_AQUAMARINE).addRelayInput(TagsAS.Items.DUSTS_STARDUST).build(registrar);
        SimpleAltarRecipeBuilder.builder().createRecipe((Object<?>)BlocksAS.FOUNTAIN_PRIME_LIQUID, AltarType.RADIANCE).setStarlightRequirement(0.7f).setFocusConstellation(ConstellationsAS.octans).setInputs(AltarRecipeGrid.builder().patternLine("GSSSG").patternLine("RG GR").patternLine(" MLM ").patternLine(" R R ").patternLine(" RLR ").key('R', (ItemLike)ItemsAS.RESONATING_GEM).key('G', (ITag.INamedTag<Item>)Tags.Items.INGOTS_GOLD).key('M', TagsAS.Items.INGOTS_STARMETAL).key('L', (ItemLike)ItemsAS.GLASS_LENS).key('S', (ItemLike)BlocksAS.BLACK_MARBLE_RAW)).addOutput((ItemLike)BlocksAS.FOUNTAIN_PRIME_LIQUID).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addAltarEffect(AltarRecipeEffectsAS.LIQUID_BURST).build(registrar);
        SimpleAltarRecipeBuilder.builder().createRecipe((Object<?>)BlocksAS.FOUNTAIN_PRIME_VORTEX, AltarType.RADIANCE).setStarlightRequirement(0.7f).setFocusConstellation(ConstellationsAS.vicio).setInputs(AltarRecipeGrid.builder().patternLine("GSSSG").patternLine(" GRG ").patternLine("M L M").patternLine("M   M").patternLine(" M M ").key('R', (ItemLike)ItemsAS.RESONATING_GEM).key('G', (ITag.INamedTag<Item>)Tags.Items.INGOTS_GOLD).key('M', TagsAS.Items.INGOTS_STARMETAL).key('L', (ItemLike)ItemsAS.GLASS_LENS).key('S', (ItemLike)BlocksAS.BLACK_MARBLE_RAW)).addOutput((ItemLike)BlocksAS.FOUNTAIN_PRIME_VORTEX).addRelayInput((ItemLike)ItemsAS.NOCTURNAL_POWDER).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addRelayInput((ItemLike)ItemsAS.NOCTURNAL_POWDER).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addAltarEffect(AltarRecipeEffectsAS.VORTEX_PLANE).build(registrar);
        SimpleAltarRecipeBuilder.builder().createRecipe((Object<?>)BlocksAS.CHALICE, AltarType.RADIANCE).setStarlightRequirement(0.5f).setInputs(AltarRecipeGrid.builder().patternLine("     ").patternLine("R   R").patternLine("RGSGR").patternLine(" MSM ").patternLine(" GSG ").key('R', (ItemLike)ItemsAS.RESONATING_GEM).key('G', (ITag.INamedTag<Item>)Tags.Items.INGOTS_GOLD).key('S', (ItemLike)BlocksAS.BLACK_MARBLE_RAW).key('M', TagsAS.Items.INGOTS_STARMETAL)).addOutput((ItemLike)BlocksAS.CHALICE).addRelayInput((ItemLike)ItemsAS.AQUAMARINE).addRelayInput((ItemLike)ItemsAS.AQUAMARINE).addRelayInput((ItemLike)ItemsAS.AQUAMARINE).addRelayInput((ItemLike)ItemsAS.AQUAMARINE).addRelayInput((ItemLike)ItemsAS.AQUAMARINE).build(registrar);
        SimpleAltarRecipeBuilder.builder().createRecipe((Object<?>)BlocksAS.OBSERVATORY, AltarType.RADIANCE).setFocusConstellation(ConstellationsAS.lucerna).setStarlightRequirement(0.4f).setInputs(AltarRecipeGrid.builder().patternLine("  NRL").patternLine(" NRIR").patternLine("NRLRN").patternLine("G RN ").patternLine("GGG  ").key('N', (ITag.INamedTag<Item>)Tags.Items.NUGGETS_GOLD).key('I', (ItemLike)ItemsAS.INFUSED_GLASS).key('G', (ITag.INamedTag<Item>)Tags.Items.INGOTS_GOLD).key('R', (ItemLike)BlocksAS.MARBLE_RUNED).key('L', (ItemLike)ItemsAS.GLASS_LENS)).addOutput((ItemLike)BlocksAS.OBSERVATORY).addRelayInput((ItemLike)ItemsAS.ILLUMINATION_POWDER).addRelayInput((ItemLike)ItemsAS.NOCTURNAL_POWDER).addRelayInput((ItemLike)ItemsAS.ILLUMINATION_POWDER).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addRelayInput((ItemLike)ItemsAS.ILLUMINATION_POWDER).addRelayInput((ItemLike)ItemsAS.NOCTURNAL_POWDER).addRelayInput((ItemLike)ItemsAS.ILLUMINATION_POWDER).addRelayInput(TagsAS.Items.DUSTS_STARDUST).build(registrar);
        registerShiftingStarRecipe(registrar, ConstellationsAS.aevitas, ItemsAS.SHIFTING_STAR_AEVITAS);
        registerShiftingStarRecipe(registrar, ConstellationsAS.armara, ItemsAS.SHIFTING_STAR_ARMARA);
        registerShiftingStarRecipe(registrar, ConstellationsAS.discidia, ItemsAS.SHIFTING_STAR_DISCIDIA);
        registerShiftingStarRecipe(registrar, ConstellationsAS.evorsio, ItemsAS.SHIFTING_STAR_EVORSIO);
        registerShiftingStarRecipe(registrar, ConstellationsAS.vicio, ItemsAS.SHIFTING_STAR_VICIO);
    }
    
    private static void registerShiftingStarRecipe(final Consumer<IFinishedRecipe> registrar, final IMajorConstellation constellation, final Item shiftingStarItem) {
        final Ingredient signature = constellation.getConstellationSignatureItems().get(0);
        SimpleAltarRecipeBuilder.builder().createRecipe((Object<?>)shiftingStarItem, AltarType.RADIANCE).setFocusConstellation(constellation).setStarlightRequirement(0.6f).setInputs(AltarRecipeGrid.builder().patternLine("  S  ").patternLine("  C  ").patternLine("SIBIS").patternLine("  C  ").patternLine("  S  ").key('B', (ItemLike)ItemsAS.SHIFTING_STAR).key('I', TagsAS.Items.INGOTS_STARMETAL).key('S', TagsAS.Items.DUSTS_STARDUST).key('C', signature)).addOutput((ItemLike)shiftingStarItem).addRelayInput(signature).addRelayInput((ItemLike)ItemsAS.ILLUMINATION_POWDER).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addRelayInput(signature).addRelayInput((ItemLike)ItemsAS.ILLUMINATION_POWDER).addRelayInput(TagsAS.Items.DUSTS_STARDUST).addAltarEffect(AltarRecipeEffectsAS.FOCUS_DUST_SWIRL).addAltarEffect(AltarRecipeEffectsAS.FOCUS_EDGE).addAltarEffect(AltarRecipeEffectsAS.ALTAR_FOCUS_SPARKLE).build(registrar);
    }
    
    private static void registerConstellationRecipes(final Consumer<IFinishedRecipe> registrar) {
        RegistriesAS.REGISTRY_CONSTELLATIONS.forEach(cst -> {
            if (Mods.ASTRAL_SORCERY.owns((Object<?>)cst)) {
                registerConstellationPaperRecipe(registrar, cst);
                if (cst instanceof IWeakConstellation) {
                    registerMantleRecipe(registrar, (IWeakConstellation)cst);
                }
            }
        });
    }
    
    private static void registerMantleRecipe(final Consumer<IFinishedRecipe> registrar, final IWeakConstellation constellation) {
        final List<Ingredient> signature = constellation.getConstellationSignatureItems();
        if (signature.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a mantle recipe for constellation without signature items: " + constellation.getRegistryName());
        }
        final Ingredient center = signature.get(0);
        final SimpleAltarRecipeBuilder<ConstellationBaseNBTCopyRecipe> builder = SimpleAltarRecipeBuilder.ofType(AltarRecipeTypeHandler.CONSTELLATION_BASE_NBT_COPY).createRecipe(AstralSorcery.key("mantle_" + constellation.getSimpleName()), AltarType.RADIANCE).modify(recipe -> recipe.setConstellation(constellation).addNBTCopyMatchIngredient((ItemLike)ItemsAS.MANTLE)).setFocusConstellation(constellation).multiplyDuration(1.2f).setStarlightRequirement(0.8f).setInputs(AltarRecipeGrid.builder().patternLine("  S  ").patternLine("  L  ").patternLine("SLMLS").patternLine("  L  ").patternLine("  S  ").key('L', center).key('M', (ItemLike)ItemsAS.MANTLE).key('S', TagsAS.Items.DUSTS_STARDUST)).addOutput((ItemLike)ItemsAS.MANTLE);
        signature.forEach(builder::addRelayInput);
        builder.build(registrar);
    }
    
    private static void registerConstellationPaperRecipe(final Consumer<IFinishedRecipe> registrar, final IConstellation constellation) {
        final List<Ingredient> signature = constellation.getConstellationSignatureItems();
        if (signature.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a constellation paper recipe for constellation without signature items: " + constellation.getRegistryName());
        }
        final Ingredient center = signature.get(0);
        final SimpleAltarRecipeBuilder<ConstellationBaseItemRecipe> builder = SimpleAltarRecipeBuilder.ofType(AltarRecipeTypeHandler.CONSTELLATION_ITEM_BASE).createRecipe(AstralSorcery.key("constellation_paper_" + constellation.getSimpleName()), AltarType.RADIANCE).modify(recipe -> recipe.setConstellation(constellation)).multiplyDuration(0.7f).setStarlightRequirement(0.4f).setInputs(AltarRecipeGrid.builder().patternLine("  L  ").patternLine("  F  ").patternLine("LSPSL").patternLine("  B  ").patternLine("  L  ").key('L', center).key('B', (ITag.INamedTag<Item>)Tags.Items.DYES_BLACK).key('P', (ItemLike)ItemsAS.PARCHMENT).key('F', (ITag.INamedTag<Item>)Tags.Items.FEATHERS).key('S', TagsAS.Items.DUSTS_STARDUST)).addOutput((ItemLike)ItemsAS.CONSTELLATION_PAPER);
        signature.forEach(builder::addRelayInput);
        builder.build(registrar);
    }
}
