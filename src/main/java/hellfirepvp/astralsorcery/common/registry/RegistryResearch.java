package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.data.journal.JournalPageText;
import java.util.Locale;
import hellfirepvp.astralsorcery.common.data.journal.JournalPageStructure;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.data.journal.JournalPageLiquidInfusion;
import hellfirepvp.astralsorcery.common.data.journal.JournalPageBlockTransmutation;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import net.minecraft.world.level.level.block.Blocks;
import hellfirepvp.astralsorcery.common.data.journal.JournalPage;
import hellfirepvp.astralsorcery.common.data.journal.JournalPageRecipe;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.item.ItemResonator;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import net.minecraft.world.level.level.ItemLike;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;

public class RegistryResearch
{
    public static void init() {
        registerDiscovery();
        registerCrafting();
        registerAttunement();
        registerConstellation();
        registerRadiance();
    }
    
    private static void registerRadiance() {
        final ResearchNode resAttuneCrystalTrait = new ResearchNode((ItemLike)ItemsAS.ROCK_CRYSTAL, "ATT_TRAIT", 0.0f, 0.0f).addPage(text("ATT_TRAIT.1")).addPage(text("ATT_TRAIT.2")).register(ResearchProgression.RADIANCE);
        final ResearchNode resObservatory = new ResearchNode((ItemLike)BlocksAS.OBSERVATORY, "OBSERVATORY", 1.5f, 0.25f).addPage(text("OBSERVATORY.1")).addPage(recipe((ItemLike)BlocksAS.OBSERVATORY)).addTomeLookup((ItemLike)BlocksAS.OBSERVATORY, 1, ResearchProgression.RADIANCE).addPage(text("OBSERVATORY.3")).register(ResearchProgression.RADIANCE);
        final ResearchNode resIrradiantStars = new ResearchNode(new ItemLike[] { (ItemLike)ItemsAS.SHIFTING_STAR_AEVITAS, (ItemLike)ItemsAS.SHIFTING_STAR_ARMARA, (ItemLike)ItemsAS.SHIFTING_STAR_DISCIDIA, (ItemLike)ItemsAS.SHIFTING_STAR_EVORSIO, (ItemLike)ItemsAS.SHIFTING_STAR_VICIO }, "ENH_SHIFTING_STAR", 3.25f, 0.5f).addPage(text("ENH_SHIFTING_STAR.1")).addPage(recipe((ItemLike)ItemsAS.SHIFTING_STAR_AEVITAS)).addTomeLookup((ItemLike)ItemsAS.SHIFTING_STAR_AEVITAS, 1, ResearchProgression.RADIANCE).addPage(recipe((ItemLike)ItemsAS.SHIFTING_STAR_ARMARA)).addTomeLookup((ItemLike)ItemsAS.SHIFTING_STAR_ARMARA, 2, ResearchProgression.RADIANCE).addPage(recipe((ItemLike)ItemsAS.SHIFTING_STAR_DISCIDIA)).addTomeLookup((ItemLike)ItemsAS.SHIFTING_STAR_DISCIDIA, 3, ResearchProgression.RADIANCE).addPage(recipe((ItemLike)ItemsAS.SHIFTING_STAR_EVORSIO)).addTomeLookup((ItemLike)ItemsAS.SHIFTING_STAR_EVORSIO, 4, ResearchProgression.RADIANCE).addPage(recipe((ItemLike)ItemsAS.SHIFTING_STAR_VICIO)).addTomeLookup((ItemLike)ItemsAS.SHIFTING_STAR_VICIO, 5, ResearchProgression.RADIANCE).register(ResearchProgression.RADIANCE);
        final ResearchNode resRelayCrafting = new ResearchNode((ItemLike)BlocksAS.ALTAR_RADIANCE, "CRAFTING_FOCUS_HINT", 2.5f, 2.0f).addPage(text("CRAFTING_FOCUS_HINT.1")).addPage(text("CRAFTING_FOCUS_HINT.2")).addPage(text("CRAFTING_FOCUS_HINT.3")).register(ResearchProgression.RADIANCE);
        final ResearchNode resMantle = new ResearchNode((ItemLike)ItemsAS.MANTLE, "ATT_CAPE", 1.25f, 2.5f).addPage(text("ATT_CAPE.1")).addPage(recipe(stack -> ItemsAS.MANTLE.equals(stack.getItem()) && ItemsAS.MANTLE.getConstellation(stack) == null)).addTomeLookup((ItemLike)ItemsAS.MANTLE, 1, ResearchProgression.RADIANCE).addPage(text("ATT_CAPE.3")).addPage(text("ATT_CAPE.4")).register(ResearchProgression.RADIANCE);
        final ResearchNode resChalice = new ResearchNode((ItemLike)BlocksAS.CHALICE, "C_CHALICE", 3.0f, 3.0f).addPage(text("C_CHALICE.1")).addPage(recipe((ItemLike)BlocksAS.CHALICE)).addTomeLookup((ItemLike)BlocksAS.CHALICE, 1, ResearchProgression.RADIANCE).addPage(text("C_CHALICE.3")).addPage(text("C_CHALICE.4")).register(ResearchProgression.RADIANCE);
        final ResearchNode resFountain = new ResearchNode((ItemLike)BlocksAS.FOUNTAIN, "BORE_CORE", 2.25f, 4.0f).addPage(text("BORE_CORE.1")).addPage(recipe((ItemLike)BlocksAS.FOUNTAIN)).addTomeLookup((ItemLike)BlocksAS.FOUNTAIN, 1, ResearchProgression.RADIANCE).addPage(text("BORE_CORE.3")).addPage(structure(StructureTypesAS.PTYPE_FOUNTAIN)).register(ResearchProgression.RADIANCE);
        final ResearchNode resLiquidBore = new ResearchNode((ItemLike)BlocksAS.FOUNTAIN_PRIME_LIQUID, "BORE_HEAD_LIQUID", 1.5f, 5.0f).addPage(text("BORE_HEAD_LIQUID.1")).addPage(recipe((ItemLike)BlocksAS.FOUNTAIN_PRIME_LIQUID)).addTomeLookup((ItemLike)BlocksAS.FOUNTAIN_PRIME_LIQUID, 1, ResearchProgression.RADIANCE).addPage(text("BORE_HEAD_LIQUID.3")).addPage(text("BORE_HEAD_LIQUID.4")).register(ResearchProgression.RADIANCE);
        final ItemStack liquidResonator = ItemResonator.setUpgradeUnlocked(new ItemStack((ItemLike)ItemsAS.RESONATOR), ItemResonator.ResonatorUpgrade.STARLIGHT, ItemResonator.ResonatorUpgrade.FLUID_FIELDS);
        ItemResonator.setCurrentUpgradeUnsafe(liquidResonator, ItemResonator.ResonatorUpgrade.FLUID_FIELDS);
        final ResearchNode resLiquidResonator = new ResearchNode(liquidResonator, "ICHOSIC", 0.0f, 4.5f).addPage(text("ICHOSIC.1")).addPage(recipe(stack -> ItemsAS.RESONATOR.equals(stack.getItem()) && ItemResonator.getCurrentUpgrade(null, stack) == ItemResonator.ResonatorUpgrade.FLUID_FIELDS)).register(ResearchProgression.RADIANCE);
        final ResearchNode resVortexBore = new ResearchNode((ItemLike)BlocksAS.FOUNTAIN_PRIME_VORTEX, "BORE_HEAD_VORTEX", 3.5f, 4.75f).addPage(text("BORE_HEAD_VORTEX.1")).addPage(recipe((ItemLike)BlocksAS.FOUNTAIN_PRIME_VORTEX)).addTomeLookup((ItemLike)BlocksAS.FOUNTAIN_PRIME_VORTEX, 1, ResearchProgression.RADIANCE).addPage(text("BORE_HEAD_VORTEX.3")).register(ResearchProgression.RADIANCE);
        resIrradiantStars.addSourceConnectionFrom(resRelayCrafting);
        resMantle.addSourceConnectionFrom(resRelayCrafting);
        resObservatory.addSourceConnectionFrom(resRelayCrafting);
        resAttuneCrystalTrait.addSourceConnectionFrom(resObservatory);
        resChalice.addSourceConnectionFrom(resRelayCrafting);
        resFountain.addSourceConnectionFrom(resChalice);
        resVortexBore.addSourceConnectionFrom(resFountain);
        resLiquidBore.addSourceConnectionFrom(resFountain);
        resLiquidResonator.addSourceConnectionFrom(resLiquidBore);
    }
    
    private static void registerConstellation() {
        final ResearchNode resLenses = new ResearchNode((ItemLike)ItemsAS.GLASS_LENS, "LENSES_EFFECTS", 6.25f, 1.75f).addPage(text("LENSES_EFFECTS.1")).addPage(text("LENSES_EFFECTS.2")).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resColoredLensFire = new ResearchNode((ItemLike)ItemsAS.COLORED_LENS_FIRE, "IGNITION_LENS", 5.5f, 0.5f).addPage(text("IGNITION_LENS.1")).addPage(recipe((ItemLike)ItemsAS.COLORED_LENS_FIRE)).addTomeLookup((ItemLike)ItemsAS.COLORED_LENS_FIRE, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resColoredLensBreak = new ResearchNode((ItemLike)ItemsAS.COLORED_LENS_BREAK, "BREAK_LENS", 6.75f, 0.25f).addPage(text("BREAK_LENS.1")).addPage(recipe((ItemLike)ItemsAS.COLORED_LENS_BREAK)).addTomeLookup((ItemLike)ItemsAS.COLORED_LENS_BREAK, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resColoredLensDamage = new ResearchNode((ItemLike)ItemsAS.COLORED_LENS_DAMAGE, "DAMAGE_LENS", 7.5f, 1.25f).addPage(text("DAMAGE_LENS.1")).addPage(recipe((ItemLike)ItemsAS.COLORED_LENS_DAMAGE)).addTomeLookup((ItemLike)ItemsAS.COLORED_LENS_DAMAGE, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resColoredLensPush = new ResearchNode((ItemLike)ItemsAS.COLORED_LENS_PUSH, "PUSH_LENS", 7.25f, 2.25f).addPage(text("PUSH_LENS.1")).addPage(recipe((ItemLike)ItemsAS.COLORED_LENS_PUSH)).addTomeLookup((ItemLike)ItemsAS.COLORED_LENS_PUSH, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resColoredLensRegeneration = new ResearchNode((ItemLike)ItemsAS.COLORED_LENS_REGENERATION, "REGENERATION_LENS", 6.75f, 3.0f).addPage(text("REGENERATION_LENS.1")).addPage(recipe((ItemLike)ItemsAS.COLORED_LENS_REGENERATION)).addTomeLookup((ItemLike)ItemsAS.COLORED_LENS_REGENERATION, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resColoredLensGrowth = new ResearchNode((ItemLike)ItemsAS.COLORED_LENS_GROWTH, "GROWTH_LENS", 5.75f, 2.75f).addPage(text("GROWTH_LENS.1")).addPage(recipe((ItemLike)ItemsAS.COLORED_LENS_GROWTH)).addTomeLookup((ItemLike)ItemsAS.COLORED_LENS_GROWTH, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resColoredLensSpectral = new ResearchNode((ItemLike)ItemsAS.COLORED_LENS_SPECTRAL, "SPECTRAL_LENS", 4.75f, 2.0f).addPage(text("SPECTRAL_LENS.1")).addPage(recipe((ItemLike)ItemsAS.COLORED_LENS_SPECTRAL)).addTomeLookup((ItemLike)ItemsAS.COLORED_LENS_SPECTRAL, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resInfuser = new ResearchNode((ItemLike)BlocksAS.INFUSER, "INFUSER", 2.0f, 1.75f).addPage(text("INFUSER.1")).addPage(recipe((ItemLike)BlocksAS.INFUSER)).addTomeLookup((ItemLike)BlocksAS.INFUSER, 1, ResearchProgression.CONSTELLATION).addPage(text("INFUSER.3")).addPage(structure(StructureTypesAS.PTYPE_INFUSER)).addPage(recipeInfusion((ItemLike)ItemsAS.RESONATING_GEM)).addTomeLookup((ItemLike)ItemsAS.RESONATING_GEM, 4, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resEnchantmentAmulet = new ResearchNode((ItemLike)ItemsAS.ENCHANTMENT_AMULET, "ENCHANTMENT_AMULET", 1.75f, 3.25f).addPage(text("ENCHANTMENT_AMULET.1")).addPage(JournalPageRecipe.fromName(AstralSorcery.key("altar/enchantment_amulet_init"))).addTomeLookup((ItemLike)ItemsAS.ENCHANTMENT_AMULET, 1, ResearchProgression.CONSTELLATION).addPage(text("ENCHANTMENT_AMULET.3")).addPage(JournalPageRecipe.fromName(AstralSorcery.key("altar/enchantment_amulet_reroll"))).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resRitualLink = new ResearchNode((ItemLike)BlocksAS.RITUAL_LINK, "RITUAL_LINK", 0.5f, 3.5f).addPage(text("RITUAL_LINK.1")).addPage(recipe((ItemLike)BlocksAS.RITUAL_LINK)).addTomeLookup((ItemLike)BlocksAS.RITUAL_LINK, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resIlluminationWand = new ResearchNode((ItemLike)ItemsAS.ILLUMINATION_WAND, "ILLUMINATION_WAND", 0.25f, 2.5f).addPage(text("ILLUMINATION_WAND.1")).addPage(recipe((ItemLike)ItemsAS.ILLUMINATION_WAND)).addTomeLookup((ItemLike)ItemsAS.ILLUMINATION_WAND, 1, ResearchProgression.CONSTELLATION).addPage(text("ILLUMINATION_WAND.3")).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resInfusedTools = new ResearchNode(new ItemLike[] { (ItemLike)ItemsAS.INFUSED_CRYSTAL_SWORD, (ItemLike)ItemsAS.INFUSED_CRYSTAL_PICKAXE, (ItemLike)ItemsAS.INFUSED_CRYSTAL_AXE, (ItemLike)ItemsAS.INFUSED_CRYSTAL_SHOVEL }, "CHARGED_TOOLS", 0.25f, 1.25f).addPage(text("CHARGED_TOOLS.1")).addPage(text("CHARGED_TOOLS.2")).addPage(recipeInfusion((ItemLike)ItemsAS.INFUSED_CRYSTAL_SWORD)).addTomeLookup((ItemLike)ItemsAS.INFUSED_CRYSTAL_SWORD, 2, ResearchProgression.CONSTELLATION).addPage(recipeInfusion((ItemLike)ItemsAS.INFUSED_CRYSTAL_PICKAXE)).addTomeLookup((ItemLike)ItemsAS.INFUSED_CRYSTAL_PICKAXE, 3, ResearchProgression.CONSTELLATION).addPage(recipeInfusion((ItemLike)ItemsAS.INFUSED_CRYSTAL_AXE)).addTomeLookup((ItemLike)ItemsAS.INFUSED_CRYSTAL_AXE, 4, ResearchProgression.CONSTELLATION).addPage(recipeInfusion((ItemLike)ItemsAS.INFUSED_CRYSTAL_SHOVEL)).addTomeLookup((ItemLike)ItemsAS.INFUSED_CRYSTAL_SHOVEL, 5, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resTreeBeacon = new ResearchNode((ItemLike)BlocksAS.TREE_BEACON, "TREEBEACON", 1.25f, 0.5f).addPage(text("TREEBEACON.1")).addPage(recipe((ItemLike)BlocksAS.TREE_BEACON)).addTomeLookup((ItemLike)BlocksAS.TREE_BEACON, 1, ResearchProgression.CONSTELLATION).addPage(text("TREEBEACON.3")).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resPrism = new ResearchNode((ItemLike)BlocksAS.PRISM, "PRISM", 2.75f, 0.0f).addPage(text("PRISM.1")).addPage(recipe((ItemLike)BlocksAS.PRISM)).addTomeLookup((ItemLike)BlocksAS.PRISM, 1, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resEngravingTable = new ResearchNode((ItemLike)BlocksAS.REFRACTION_TABLE, "DRAWING_TABLE", 3.5f, 1.0f).addPage(text("DRAWING_TABLE.1")).addPage(recipe((ItemLike)BlocksAS.REFRACTION_TABLE)).addTomeLookup((ItemLike)BlocksAS.REFRACTION_TABLE, 1, ResearchProgression.CONSTELLATION).addPage(text("DRAWING_TABLE.3")).addPage(text("DRAWING_TABLE.4")).addPage(recipe((ItemLike)ItemsAS.INFUSED_GLASS)).addTomeLookup((ItemLike)ItemsAS.INFUSED_GLASS, 4, ResearchProgression.CONSTELLATION).addPage(text("DRAWING_TABLE.6")).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resAltar4 = new ResearchNode((ItemLike)BlocksAS.ALTAR_RADIANCE, "ALTAR4", 3.5f, 3.0f).addPage(text("ALTAR4.1")).addPage(recipe((ItemLike)BlocksAS.ALTAR_RADIANCE)).addTomeLookup((ItemLike)BlocksAS.ALTAR_RADIANCE, 1, ResearchProgression.CONSTELLATION).addPage(structure(StructureTypesAS.PTYPE_ALTAR_TRAIT)).addPage(text("ALTAR4.4")).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resCollectorCrystal = new ResearchNode((ItemLike)BlocksAS.ROCK_COLLECTOR_CRYSTAL, "COLL_CRYSTAL", 2.75f, 3.75f).addPage(text("COLL_CRYSTAL.1")).addPage(recipe((ItemLike)BlocksAS.ROCK_COLLECTOR_CRYSTAL)).addTomeLookup((ItemLike)BlocksAS.ROCK_COLLECTOR_CRYSTAL, 1, ResearchProgression.CONSTELLATION).addTomeLookup((ItemLike)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL, 1, ResearchProgression.CONSTELLATION).addPage(text("COLL_CRYSTAL.3")).addPage(text("COLL_CRYSTAL.4")).register(ResearchProgression.CONSTELLATION);
        final ItemStack celestialCrystalCluster = new ItemStack((ItemLike)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER);
        celestialCrystalCluster.setDamageValue(4);
        final ResearchNode resCelestialCrystalCluster = new ResearchNode(celestialCrystalCluster, "CEL_CRYSTAL_GROW", 6.25f, 4.0f).addPage(text("CEL_CRYSTAL_GROW.1")).addPage(text("CEL_CRYSTAL_GROW.2")).addPage(text("CEL_CRYSTAL_GROW.3")).addTomeLookup((ItemLike)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER, 0, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resCelestialCrystals = new ResearchNode((ItemLike)ItemsAS.CELESTIAL_CRYSTAL, "CEL_CRYSTALS", 5.0f, 3.75f).addPage(text("CEL_CRYSTALS.1")).addTomeLookup((ItemLike)ItemsAS.CELESTIAL_CRYSTAL, 0, ResearchProgression.CONSTELLATION).register(ResearchProgression.CONSTELLATION);
        final ResearchNode resEnhancedCollectorCrystal = new ResearchNode((ItemLike)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL, "ENHANCED_COLLECTOR", 4.0f, 4.5f).addPage(text("ENHANCED_COLLECTOR.1")).addPage(structure(StructureTypesAS.PTYPE_ENHANCED_COLLECTOR_CRYSTAL)).register(ResearchProgression.CONSTELLATION);
        resColoredLensFire.addSourceConnectionFrom(resLenses);
        resColoredLensBreak.addSourceConnectionFrom(resLenses);
        resColoredLensDamage.addSourceConnectionFrom(resLenses);
        resColoredLensPush.addSourceConnectionFrom(resLenses);
        resColoredLensRegeneration.addSourceConnectionFrom(resLenses);
        resColoredLensGrowth.addSourceConnectionFrom(resLenses);
        resColoredLensSpectral.addSourceConnectionFrom(resLenses);
        resColoredLensSpectral.addSourceConnectionFrom(resInfuser);
        resEngravingTable.addSourceConnectionFrom(resColoredLensSpectral);
        resEnchantmentAmulet.addSourceConnectionFrom(resInfuser);
        resRitualLink.addSourceConnectionFrom(resInfuser);
        resIlluminationWand.addSourceConnectionFrom(resInfuser);
        resInfusedTools.addSourceConnectionFrom(resInfuser);
        resTreeBeacon.addSourceConnectionFrom(resInfuser);
        resPrism.addSourceConnectionFrom(resInfuser);
        resAltar4.addSourceConnectionFrom(resInfuser);
        resEngravingTable.addSourceConnectionFrom(resInfuser);
        resCollectorCrystal.addSourceConnectionFrom(resInfuser);
        resCelestialCrystals.addSourceConnectionFrom(resCelestialCrystalCluster);
        resAltar4.addSourceConnectionFrom(resCelestialCrystals);
        resEnhancedCollectorCrystal.addSourceConnectionFrom(resCollectorCrystal);
        resEnhancedCollectorCrystal.addSourceConnectionFrom(resCelestialCrystals);
    }
    
    private static void registerAttunement() {
        final ResearchNode resTelescope = new ResearchNode((ItemLike)BlocksAS.TELESCOPE, "TELESCOPE", 0.5f, 0.0f).addPage(text("TELESCOPE.1")).addPage(recipe((ItemLike)BlocksAS.TELESCOPE)).addTomeLookup((ItemLike)BlocksAS.TELESCOPE, 1, ResearchProgression.ATTUNEMENT).addPage(text("TELESCOPE.3")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resKnowledgeShare = new ResearchNode((ItemLike)ItemsAS.KNOWLEDGE_SHARE, "KNOWLEDGE_SHARE", 2.5f, 0.25f).addPage(text("KNOWLEDGE_SHARE.1")).addPage(recipe((ItemLike)ItemsAS.KNOWLEDGE_SHARE)).addTomeLookup((ItemLike)ItemsAS.KNOWLEDGE_SHARE, 1, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resLens = new ResearchNode((ItemLike)BlocksAS.LENS, "LENS", 0.0f, 1.25f).addPage(text("LENS.1")).addPage(recipe((ItemLike)BlocksAS.LENS)).addTomeLookup((ItemLike)BlocksAS.LENS, 1, ResearchProgression.ATTUNEMENT).addPage(text("LENS.3")).addPage(text("LENS.4")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resLinkingTool = new ResearchNode((ItemLike)ItemsAS.LINKING_TOOL, "LINKTOOL", 0.25f, 2.25f).addPage(text("LINKTOOL.1")).addPage(recipe((ItemLike)ItemsAS.LINKING_TOOL)).addTomeLookup((ItemLike)ItemsAS.LINKING_TOOL, 1, ResearchProgression.ATTUNEMENT).addPage(text("LINKTOOL.3")).addPage(text("LINKTOOL.4")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resStarlightNetwork = new ResearchNode((ItemLike)BlocksAS.LENS, "STARLIGHT_NETWORK", 1.5f, 1.0f).addPage(text("STARLIGHT_NETWORK.1")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resTransmutationOres = new ResearchNode(new ItemLike[] { (ItemLike)Blocks.field_196814_hQ, (ItemLike)Blocks.field_150354_m, (ItemLike)Blocks.field_150482_ag, (ItemLike)Blocks.field_189878_dg, (ItemLike)Blocks.field_150423_aK, (ItemLike)Blocks.field_150322_A }, "TRANSMUTATION_ORES", 2.75f, 1.5f).addPage(text("TRANSMUTATION_ORES.1")).addPage(recipeTransmutation((ItemLike)Blocks.field_150435_aG)).addPage(recipeTransmutation((ItemLike)Blocks.field_150412_bA)).addPage(recipeTransmutation((ItemLike)Blocks.field_150414_aQ)).addPage(recipeTransmutation((ItemLike)Blocks.field_150368_y)).addPage(recipeTransmutation((ItemLike)Blocks.field_150377_bs)).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resStarmetalOre = new ResearchNode((ItemLike)BlocksAS.STARMETAL_ORE, "STARMETAL_ORE", 4.25f, 1.75f).addPage(text("STARMETAL_ORE.1")).addPage(recipeTransmutation((ItemLike)BlocksAS.STARMETAL_ORE)).addTomeLookup((ItemLike)ItemsAS.STARMETAL_INGOT, 0, ResearchProgression.ATTUNEMENT).addTomeLookup((ItemLike)BlocksAS.STARMETAL_ORE, 0, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resCuttingTool = new ResearchNode((ItemLike)ItemsAS.CHISEL, "CUTTING_TOOL", 5.0f, 1.0f).addPage(text("CUTTING_TOOL.1")).addPage(recipe((ItemLike)ItemsAS.CHISEL)).addTomeLookup((ItemLike)ItemsAS.CHISEL, 1, ResearchProgression.ATTUNEMENT).addPage(text("CUTTING_TOOL.3")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resStardust = new ResearchNode((ItemLike)ItemsAS.STARDUST, "STARDUST", 6.0f, 0.5f).addPage(text("STARDUST.1")).addPage(text("STARDUST.2")).addTomeLookup((ItemLike)ItemsAS.STARDUST, 0, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ItemStack structureResonator = ItemResonator.setUpgradeUnlocked(new ItemStack((ItemLike)ItemsAS.RESONATOR), ItemResonator.ResonatorUpgrade.STARLIGHT, ItemResonator.ResonatorUpgrade.AREA_SIZE);
        ItemResonator.setCurrentUpgradeUnsafe(structureResonator, ItemResonator.ResonatorUpgrade.AREA_SIZE);
        final ResearchNode resResonatorStructure = new ResearchNode(structureResonator, "RESONATOR_AREA_SIZE", 6.5f, 2.0f).addPage(text("RESONATOR_AREA_SIZE.1")).addPage(recipe(stack -> ItemsAS.RESONATOR.equals(stack.getItem()) && ItemResonator.getCurrentUpgrade(null, stack) == ItemResonator.ResonatorUpgrade.AREA_SIZE)).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resCelestialGateway = new ResearchNode((ItemLike)BlocksAS.GATEWAY, "CELESTIAL_GATEWAY", 7.5f, 1.5f).addPage(text("CELESTIAL_GATEWAY.1")).addPage(recipe((ItemLike)BlocksAS.GATEWAY)).addTomeLookup((ItemLike)BlocksAS.GATEWAY, 1, ResearchProgression.ATTUNEMENT).addPage(text("CELESTIAL_GATEWAY.3")).addPage(structure(StructureTypesAS.PTYPE_CELESTIAL_GATEWAY)).addPage(text("CELESTIAL_GATEWAY.5")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resAltar3 = new ResearchNode((ItemLike)BlocksAS.ALTAR_CONSTELLATION, "ALTAR3", 7.25f, 0.0f).addPage(text("ALTAR3.1")).addPage(recipe((ItemLike)BlocksAS.ALTAR_CONSTELLATION)).addTomeLookup((ItemLike)BlocksAS.ALTAR_CONSTELLATION, 1, ResearchProgression.ATTUNEMENT).addPage(structure(StructureTypesAS.PTYPE_ALTAR_CONSTELLATION)).addPage(text("ALTAR3.4")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resAttunePlayer = new ResearchNode((ItemLike)BlocksAS.ATTUNEMENT_ALTAR, "ATT_PLAYER", 3.75f, 2.75f).addPage(text("ATT_PLAYER.1")).addPage(text("ATT_PLAYER.2")).addPage(recipe((ItemLike)BlocksAS.ATTUNEMENT_ALTAR)).addTomeLookup((ItemLike)BlocksAS.ATTUNEMENT_ALTAR, 2, ResearchProgression.ATTUNEMENT).addPage(text("ATT_PLAYER.4")).addPage(structure(StructureTypesAS.PTYPE_ATTUNEMENT_ALTAR)).addPage(text("ATT_PLAYER.6")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resPerks = new ResearchNode((ItemLike)BlocksAS.SPECTRAL_RELAY, "ATT_PERKS", 4.5f, 3.5f).addPage(text("ATT_PERKS.1")).addPage(text("ATT_PERKS.2")).addPage(text("ATT_PERKS.3")).addPage(text("ATT_PERKS.4")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resShiftingStar = new ResearchNode((ItemLike)ItemsAS.SHIFTING_STAR, "SHIFT_STAR", 5.75f, 3.25f).addPage(text("SHIFT_STAR.1")).addPage(recipe((ItemLike)ItemsAS.SHIFTING_STAR)).addTomeLookup((ItemLike)ItemsAS.SHIFTING_STAR, 1, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resPerkSeal = new ResearchNode((ItemLike)ItemsAS.PERK_SEAL, "ATT_PERKS_SEAL", 5.5f, 4.5f).addPage(text("ATT_PERKS_SEAL.1")).addPage(recipe((ItemLike)ItemsAS.PERK_SEAL)).addTomeLookup((ItemLike)ItemsAS.PERK_SEAL, 1, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resPerkGems = new ResearchNode(new ItemLike[] { (ItemLike)ItemsAS.PERK_GEM_DAY, (ItemLike)ItemsAS.PERK_GEM_NIGHT, (ItemLike)ItemsAS.PERK_GEM_SKY }, "ATT_PERK_GEMS", 4.75f, 5.25f).addPage(text("ATT_PERK_GEMS.1")).addPage(text("ATT_PERK_GEMS.2")).addPage(text("ATT_PERK_GEMS.3")).addTomeLookup((ItemLike)ItemsAS.PERK_GEM_DAY, 1, ResearchProgression.ATTUNEMENT).addTomeLookup((ItemLike)ItemsAS.PERK_GEM_NIGHT, 1, ResearchProgression.ATTUNEMENT).addTomeLookup((ItemLike)ItemsAS.PERK_GEM_SKY, 1, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resAttuneCrystal = new ResearchNode((ItemLike)ItemsAS.ROCK_CRYSTAL, "ATT_CRYSTAL", 3.5f, 4.0f).addPage(text("ATT_CRYSTAL.1")).addPage(text("ATT_CRYSTAL.2")).addTomeLookup((ItemLike)ItemsAS.ATTUNED_ROCK_CRYSTAL, 0, ResearchProgression.ATTUNEMENT).addTomeLookup((ItemLike)ItemsAS.ATTUNED_CELESTIAL_CRYSTAL, 0, ResearchProgression.CONSTELLATION).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resRitualPedestal = new ResearchNode((ItemLike)BlocksAS.RITUAL_PEDESTAL, "RIT_PEDESTAL", 3.0f, 5.0f).addPage(text("RIT_PEDESTAL.1")).addPage(recipe((ItemLike)BlocksAS.RITUAL_PEDESTAL)).addTomeLookup((ItemLike)BlocksAS.RITUAL_PEDESTAL, 1, ResearchProgression.ATTUNEMENT).addPage(structure(StructureTypesAS.PTYPE_RITUAL_PEDESTAL)).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resRitualPedestalAcceleration = new ResearchNode((ItemLike)BlocksAS.RITUAL_PEDESTAL, "PED_ACCEL", 1.75f, 5.5f).addPage(text("PED_ACCEL.1")).addPage(text("PED_ACCEL.2")).addPage(text("PED_ACCEL.3")).addPage(text("PED_ACCEL.4")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resAlignmentCharge = new ResearchNode(new SpriteQuery(AssetLoader.TextureLocation.EFFECT, 6, 8, new String[] { "relay_flare" }), "QUICK_CHARGE", 0.75f, 4.5f).addPage(text("QUICK_CHARGE.1")).addPage(text("QUICK_CHARGE.2")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resToolChanneling = new ResearchNode((ItemLike)BlocksAS.SPECTRAL_RELAY, "TOOL_CHANNEL", 1.25f, 3.25f).addPage(text("TOOL_CHANNEL.1")).addPage(text("TOOL_CHANNEL.2")).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resBlinkWand = new ResearchNode((ItemLike)ItemsAS.BLINK_WAND, "TRAVERSAL_WAND", 2.25f, 3.5f).addPage(text("TRAVERSAL_WAND.1")).addPage(recipe((ItemLike)ItemsAS.BLINK_WAND)).addTomeLookup((ItemLike)ItemsAS.BLINK_WAND, 1, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resGrappleWand = new ResearchNode((ItemLike)ItemsAS.GRAPPLE_WAND, "GRAPPLE_WAND", 2.5f, 2.5f).addPage(text("GRAPPLE_WAND.1")).addPage(recipe((ItemLike)ItemsAS.GRAPPLE_WAND)).addTomeLookup((ItemLike)ItemsAS.GRAPPLE_WAND, 1, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        final ResearchNode resToolWands = new ResearchNode(new ItemLike[] { (ItemLike)ItemsAS.ARCHITECT_WAND, (ItemLike)ItemsAS.EXCHANGE_WAND }, "TOOL_WANDS", 1.5f, 2.25f).addPage(text("TOOL_WANDS.1")).addPage(recipe((ItemLike)ItemsAS.ARCHITECT_WAND)).addTomeLookup((ItemLike)ItemsAS.ARCHITECT_WAND, 1, ResearchProgression.ATTUNEMENT).addPage(text("TOOL_WANDS.3")).addPage(recipe((ItemLike)ItemsAS.EXCHANGE_WAND)).addTomeLookup((ItemLike)ItemsAS.EXCHANGE_WAND, 3, ResearchProgression.ATTUNEMENT).register(ResearchProgression.ATTUNEMENT);
        resStarlightNetwork.addSourceConnectionFrom(resLens);
        resStarlightNetwork.addSourceConnectionFrom(resLinkingTool);
        resTransmutationOres.addSourceConnectionFrom(resStarlightNetwork);
        resStarmetalOre.addSourceConnectionFrom(resTransmutationOres);
        resCuttingTool.addSourceConnectionFrom(resStarmetalOre);
        resStardust.addSourceConnectionFrom(resCuttingTool);
        resResonatorStructure.addSourceConnectionFrom(resStardust);
        resCelestialGateway.addSourceConnectionFrom(resStardust);
        resAltar3.addSourceConnectionFrom(resStardust);
        resAttunePlayer.addSourceConnectionFrom(resStarmetalOre);
        resPerks.addSourceConnectionFrom(resAttunePlayer);
        resShiftingStar.addSourceConnectionFrom(resPerks);
        resPerkSeal.addSourceConnectionFrom(resPerks);
        resPerkGems.addSourceConnectionFrom(resPerks);
        resAttuneCrystal.addSourceConnectionFrom(resAttunePlayer);
        resRitualPedestal.addSourceConnectionFrom(resAttuneCrystal);
        resRitualPedestalAcceleration.addSourceConnectionFrom(resRitualPedestal);
        resToolChanneling.addSourceConnectionFrom(resAlignmentCharge);
        resBlinkWand.addSourceConnectionFrom(resToolChanneling);
        resGrappleWand.addSourceConnectionFrom(resToolChanneling);
        resToolWands.addSourceConnectionFrom(resToolChanneling);
    }
    
    private static void registerCrafting() {
        final ResearchNode resHandTelescope = new ResearchNode((ItemLike)ItemsAS.HAND_TELESCOPE, "HAND_TELESCOPE", 0.0f, 1.0f).addPage(text("HAND_TELESCOPE.1")).addPage(recipe((ItemLike)ItemsAS.GLASS_LENS)).addTomeLookup((ItemLike)ItemsAS.GLASS_LENS, 1, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)ItemsAS.HAND_TELESCOPE)).addTomeLookup((ItemLike)ItemsAS.HAND_TELESCOPE, 2, ResearchProgression.BASIC_CRAFT).addPage(text("HAND_TELESCOPE.4")).addPage(text("HAND_TELESCOPE.5")).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resIlluminationPowder = new ResearchNode((ItemLike)ItemsAS.ILLUMINATION_POWDER, "ILLUM_POWDER", 1.0f, 2.0f).addPage(text("ILLUM_POWDER.1")).addPage(recipe((ItemLike)ItemsAS.ILLUMINATION_POWDER)).addTomeLookup((ItemLike)ItemsAS.ILLUMINATION_POWDER, 1, ResearchProgression.BASIC_CRAFT).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resNocturnalPowder = new ResearchNode((ItemLike)ItemsAS.NOCTURNAL_POWDER, "NOC_POWDER", 0.0f, 2.5f).addPage(text("NOC_POWDER.1")).addPage(recipe((ItemLike)ItemsAS.NOCTURNAL_POWDER)).addTomeLookup((ItemLike)ItemsAS.NOCTURNAL_POWDER, 1, ResearchProgression.BASIC_CRAFT).addPage(text("NOC_POWDER.3")).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resIlluminator = new ResearchNode((ItemLike)BlocksAS.ILLUMINATOR, "ILLUMINATOR", 0.75f, 3.0f).addPage(text("ILLUMINATOR.1")).addPage(recipe((ItemLike)BlocksAS.ILLUMINATOR)).addTomeLookup((ItemLike)BlocksAS.ILLUMINATOR, 1, ResearchProgression.BASIC_CRAFT).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resRockCrystals = new ResearchNode((ItemLike)ItemsAS.ROCK_CRYSTAL, "ROCK_CRYSTALS", 2.0f, 1.5f).addPage(text("ROCK_CRYSTALS.1")).addPage(text("ROCK_CRYSTALS.2")).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resCrystalGrowth = new ResearchNode((ItemLike)ItemsAS.ROCK_CRYSTAL, "CRYSTAL_GROWTH", 3.0f, 2.5f).addPage(text("CRYSTAL_GROWTH.1")).addPage(text("CRYSTAL_GROWTH.2")).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resTools = new ResearchNode(new ItemLike[] { (ItemLike)ItemsAS.CRYSTAL_SWORD, (ItemLike)ItemsAS.CRYSTAL_PICKAXE, (ItemLike)ItemsAS.CRYSTAL_AXE, (ItemLike)ItemsAS.CRYSTAL_SHOVEL }, "TOOLS", 4.5f, 3.0f).addPage(text("TOOLS.1")).addPage(text("TOOLS.3")).addPage(recipe((ItemLike)ItemsAS.CRYSTAL_SWORD)).addTomeLookup((ItemLike)ItemsAS.CRYSTAL_SWORD, 2, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)ItemsAS.CRYSTAL_PICKAXE)).addTomeLookup((ItemLike)ItemsAS.CRYSTAL_PICKAXE, 3, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)ItemsAS.CRYSTAL_AXE)).addTomeLookup((ItemLike)ItemsAS.CRYSTAL_AXE, 4, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)ItemsAS.CRYSTAL_SHOVEL)).addTomeLookup((ItemLike)ItemsAS.CRYSTAL_SHOVEL, 5, ResearchProgression.BASIC_CRAFT).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resLightwell = new ResearchNode((ItemLike)BlocksAS.WELL, "WELL", 3.0f, 1.0f).addPage(text("WELL.1")).addPage(recipe((ItemLike)BlocksAS.WELL)).addTomeLookup((ItemLike)BlocksAS.WELL, 1, ResearchProgression.BASIC_CRAFT).addPage(text("WELL.3")).addPage(text("WELL.4")).addPage(text("WELL.5")).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resInfusedWood = new ResearchNode(new ItemLike[] { (ItemLike)BlocksAS.INFUSED_WOOD, (ItemLike)BlocksAS.INFUSED_WOOD_ARCH, (ItemLike)BlocksAS.INFUSED_WOOD_COLUMN, (ItemLike)BlocksAS.INFUSED_WOOD_ENGRAVED, (ItemLike)BlocksAS.INFUSED_WOOD_ENRICHED, (ItemLike)BlocksAS.INFUSED_WOOD_PLANKS, (ItemLike)BlocksAS.INFUSED_WOOD_STAIRS, (ItemLike)BlocksAS.INFUSED_WOOD_SLAB }, "INFUSED_WOOD", 4.0f, 0.0f).addPage(text("INFUSED_WOOD.1")).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD, 0, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)BlocksAS.INFUSED_WOOD_PLANKS)).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD_PLANKS, 1, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)BlocksAS.INFUSED_WOOD_ARCH)).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD_ARCH, 2, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)BlocksAS.INFUSED_WOOD_COLUMN)).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD_COLUMN, 3, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)BlocksAS.INFUSED_WOOD_ENGRAVED)).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD_ENGRAVED, 4, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)BlocksAS.INFUSED_WOOD_ENRICHED)).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD_ENRICHED, 5, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)BlocksAS.INFUSED_WOOD_STAIRS)).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD_STAIRS, 6, ResearchProgression.BASIC_CRAFT).addPage(recipe((ItemLike)BlocksAS.INFUSED_WOOD_SLAB)).addTomeLookup((ItemLike)BlocksAS.INFUSED_WOOD_SLAB, 7, ResearchProgression.BASIC_CRAFT).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resAltar2 = new ResearchNode((ItemLike)BlocksAS.ALTAR_ATTUNEMENT, "ALTAR2", 4.0f, 1.5f).addPage(text("ALTAR2.1")).addPage(recipe((ItemLike)BlocksAS.ALTAR_ATTUNEMENT)).addTomeLookup((ItemLike)BlocksAS.ALTAR_ATTUNEMENT, 1, ResearchProgression.BASIC_CRAFT).addPage(structure(StructureTypesAS.PTYPE_ALTAR_ATTUNEMENT)).addPage(text("ALTAR2.4")).register(ResearchProgression.BASIC_CRAFT);
        final ItemStack starlightResonator = ItemResonator.setUpgradeUnlocked(new ItemStack((ItemLike)ItemsAS.RESONATOR), ItemResonator.ResonatorUpgrade.STARLIGHT);
        ItemResonator.setCurrentUpgradeUnsafe(starlightResonator, ItemResonator.ResonatorUpgrade.STARLIGHT);
        final ResearchNode resResonator = new ResearchNode(starlightResonator, "SKY_RESO", 5.0f, 0.5f).addPage(text("SKY_RESO.1")).addPage(recipe(stack -> ItemsAS.RESONATOR.equals(stack.getItem()) && ItemResonator.getCurrentUpgrade(null, stack) == ItemResonator.ResonatorUpgrade.STARLIGHT)).addPage(text("SKY_RESO.2")).addPage(text("SKY_RESO.3")).register(ResearchProgression.BASIC_CRAFT);
        final ResearchNode resSpectralRelay = new ResearchNode((ItemLike)BlocksAS.SPECTRAL_RELAY, "SPEC_RELAY", 6.0f, 1.0f).addPage(text("SPEC_RELAY.1")).addPage(recipe((ItemLike)BlocksAS.SPECTRAL_RELAY)).addTomeLookup((ItemLike)BlocksAS.SPECTRAL_RELAY, 1, ResearchProgression.BASIC_CRAFT).addPage(structure(StructureTypesAS.PTYPE_SPECTRAL_RELAY)).addPage(text("SPEC_RELAY.4")).register(ResearchProgression.BASIC_CRAFT);
        resNocturnalPowder.addSourceConnectionFrom(resIlluminationPowder);
        resIlluminator.addSourceConnectionFrom(resIlluminationPowder);
        resCrystalGrowth.addSourceConnectionFrom(resRockCrystals);
        resTools.addSourceConnectionFrom(resCrystalGrowth);
        resLightwell.addSourceConnectionFrom(resRockCrystals);
        resInfusedWood.addSourceConnectionFrom(resLightwell);
        resAltar2.addSourceConnectionFrom(resLightwell);
        resResonator.addSourceConnectionFrom(resLightwell);
        resSpectralRelay.addSourceConnectionFrom(resResonator);
    }
    
    private static void registerDiscovery() {
        final ResearchNode resWelcome = new ResearchNode((ItemLike)ItemsAS.TOME, "WELCOME", 1.0f, 1.0f).addPage(text("WELCOME.1")).addPage(text("WELCOME.2")).register(ResearchProgression.DISCOVERY);
        final ResearchNode resShrines = new ResearchNode((ItemLike)ItemsAS.ROCK_CRYSTAL, "SHRINES", 2.0f, 0.0f).addPage(text("SHRINES.1")).addPage(text("SHRINES.2")).addPage(text("SHRINES.3")).register(ResearchProgression.DISCOVERY);
        final ResearchNode resConstellationPaper = new ResearchNode((ItemLike)ItemsAS.CONSTELLATION_PAPER, "CPAPER", 3.0f, 1.0f).addPage(text("CPAPER.1")).addPage(recipe((ItemLike)ItemsAS.TOME)).addPage(text("CPAPER.3")).addPage(recipe((ItemLike)ItemsAS.PARCHMENT)).addTomeLookup((ItemLike)ItemsAS.CONSTELLATION_PAPER, 0, ResearchProgression.DISCOVERY).addTomeLookup((ItemLike)ItemsAS.TOME, 1, ResearchProgression.DISCOVERY).addTomeLookup((ItemLike)ItemsAS.PARCHMENT, 3, ResearchProgression.DISCOVERY).register(ResearchProgression.DISCOVERY);
        final ResearchNode resOres = new ResearchNode(new ItemLike[] { (ItemLike)BlocksAS.ROCK_CRYSTAL_ORE, (ItemLike)BlocksAS.AQUAMARINE_SAND_ORE }, "ORES", 2.0f, 2.0f).addPage(text("ORES.1")).addPage(text("ORES.2")).addTomeLookup((ItemLike)ItemsAS.AQUAMARINE, 0, ResearchProgression.DISCOVERY).addTomeLookup((ItemLike)ItemsAS.ROCK_CRYSTAL, 0, ResearchProgression.DISCOVERY).addTomeLookup((ItemLike)BlocksAS.AQUAMARINE_SAND_ORE, 0, ResearchProgression.DISCOVERY).addTomeLookup((ItemLike)BlocksAS.ROCK_CRYSTAL_ORE, 0, ResearchProgression.DISCOVERY).register(ResearchProgression.DISCOVERY);
        final ResearchNode resResonatingWand = new ResearchNode((ItemLike)ItemsAS.WAND, "WAND", 3.0f, 3.0f).addPage(text("WAND.1")).addPage(recipeVanilla((ItemLike)ItemsAS.WAND)).addTomeLookup((ItemLike)ItemsAS.WAND, 1, ResearchProgression.DISCOVERY).addPage(text("WAND.3")).addPage(text("WAND.4")).register(ResearchProgression.DISCOVERY);
        final ResearchNode resAltar1 = new ResearchNode((ItemLike)BlocksAS.ALTAR_DISCOVERY, "ALTAR1", 4.0f, 2.0f).addPage(text("ALTAR1.1")).addPage(text("ALTAR1.2")).addPage(recipeTransmutation((ItemLike)BlocksAS.ALTAR_DISCOVERY)).addTomeLookup((ItemLike)BlocksAS.ALTAR_DISCOVERY, 1, ResearchProgression.DISCOVERY).addPage(text("ALTAR1.4")).addPage(text("ALTAR1.5")).addPage(text("ALTAR1.6")).register(ResearchProgression.DISCOVERY);
        final ResearchNode resMarbles = new ResearchNode(new ItemLike[] { (ItemLike)BlocksAS.MARBLE_RAW, (ItemLike)BlocksAS.MARBLE_PILLAR, (ItemLike)BlocksAS.MARBLE_ARCH, (ItemLike)BlocksAS.MARBLE_BRICKS, (ItemLike)BlocksAS.MARBLE_CHISELED, (ItemLike)BlocksAS.MARBLE_ENGRAVED, (ItemLike)BlocksAS.MARBLE_RUNED, (ItemLike)BlocksAS.MARBLE_SLAB, (ItemLike)BlocksAS.MARBLE_STAIRS }, "MARBLETYPES", 0.0f, 2.5f).addPage(text("MARBLETYPES.1")).addPage(text("MARBLETYPES.2")).addTomeLookup((ItemLike)BlocksAS.MARBLE_RAW, 1, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_PILLAR)).addTomeLookup((ItemLike)BlocksAS.MARBLE_PILLAR, 2, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_ARCH)).addTomeLookup((ItemLike)BlocksAS.MARBLE_ARCH, 3, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_BRICKS)).addTomeLookup((ItemLike)BlocksAS.MARBLE_BRICKS, 4, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_CHISELED)).addTomeLookup((ItemLike)BlocksAS.MARBLE_CHISELED, 5, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_ENGRAVED)).addTomeLookup((ItemLike)BlocksAS.MARBLE_ENGRAVED, 6, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_RUNED)).addTomeLookup((ItemLike)BlocksAS.MARBLE_RUNED, 7, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_SLAB)).addTomeLookup((ItemLike)BlocksAS.MARBLE_SLAB, 8, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.MARBLE_STAIRS)).addTomeLookup((ItemLike)BlocksAS.MARBLE_STAIRS, 9, ResearchProgression.DISCOVERY).register(ResearchProgression.DISCOVERY);
        final ResearchNode resSootyMarble = new ResearchNode(new ItemLike[] { (ItemLike)BlocksAS.BLACK_MARBLE_RAW, (ItemLike)BlocksAS.BLACK_MARBLE_PILLAR, (ItemLike)BlocksAS.BLACK_MARBLE_ARCH, (ItemLike)BlocksAS.BLACK_MARBLE_BRICKS, (ItemLike)BlocksAS.BLACK_MARBLE_CHISELED, (ItemLike)BlocksAS.BLACK_MARBLE_ENGRAVED, (ItemLike)BlocksAS.BLACK_MARBLE_RUNED, (ItemLike)BlocksAS.BLACK_MARBLE_SLAB, (ItemLike)BlocksAS.BLACK_MARBLE_STAIRS }, "SOOTYMARBLE", 1.0f, 3.0f).addPage(text("SOOTYMARBLE.1")).addPage(text("SOOTYMARBLE.2")).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_RAW)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_RAW, 2, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_PILLAR)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_PILLAR, 3, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_ARCH)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_ARCH, 4, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_BRICKS)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_BRICKS, 5, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_CHISELED)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_CHISELED, 6, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_ENGRAVED)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_ENGRAVED, 7, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_RUNED)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_RUNED, 8, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_SLAB)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_SLAB, 9, ResearchProgression.DISCOVERY).addPage(recipe((ItemLike)BlocksAS.BLACK_MARBLE_STAIRS)).addTomeLookup((ItemLike)BlocksAS.BLACK_MARBLE_STAIRS, 10, ResearchProgression.DISCOVERY).register(ResearchProgression.DISCOVERY);
        resShrines.addSourceConnectionFrom(resWelcome);
        resConstellationPaper.addSourceConnectionFrom(resShrines);
        resMarbles.addSourceConnectionFrom(resWelcome);
        resSootyMarble.addSourceConnectionFrom(resMarbles);
        resOres.addSourceConnectionFrom(resWelcome);
        resResonatingWand.addSourceConnectionFrom(resOres);
        resAltar1.addSourceConnectionFrom(resResonatingWand);
    }
    
    private static JournalPage recipe(final ItemLike outputItem) {
        return JournalPageRecipe.fromOutputPreferAltarRecipes(stack -> !stack.isEmpty() && stack.getItem().equals(outputItem.func_199767_j()));
    }
    
    private static JournalPage recipeVanilla(final ItemLike outputItem) {
        return JournalPageRecipe.fromOutputPreferVanillaRecipes(stack -> !stack.isEmpty() && stack.getItem().equals(outputItem.func_199767_j()));
    }
    
    private static JournalPage recipeTransmutation(final ItemLike outputItem) {
        return JournalPageBlockTransmutation.fromOutput(stack -> !stack.isEmpty() && stack.getItem().equals(outputItem.func_199767_j()));
    }
    
    private static JournalPage recipeInfusion(final ItemLike outputItem) {
        return JournalPageLiquidInfusion.fromOutput(stack -> !stack.isEmpty() && stack.getItem().equals(outputItem.func_199767_j()));
    }
    
    private static JournalPage recipe(final Predicate<ItemStack> itemStackFilter) {
        return JournalPageRecipe.fromOutputPreferAltarRecipes(itemStackFilter);
    }
    
    private static JournalPage recipeVanilla(final Predicate<ItemStack> itemStackFilter) {
        return JournalPageRecipe.fromOutputPreferVanillaRecipes(itemStackFilter);
    }
    
    private static JournalPageBlockTransmutation recipeTransmutation(final Predicate<ItemStack> itemStackFilter) {
        return JournalPageBlockTransmutation.fromOutput(itemStackFilter);
    }
    
    private static JournalPageLiquidInfusion recipeInfusion(final Predicate<ItemStack> itemStackFilter) {
        return JournalPageLiquidInfusion.fromOutput(itemStackFilter);
    }
    
    private static JournalPage structure(final StructureType structure) {
        return new JournalPageStructure(structure.getStructure());
    }
    
    private static JournalPage text(final String identifier) {
        return new JournalPageText("astralsorcery".toLowerCase(Locale.ROOT) + ".journal." + identifier + ".text");
    }
}
