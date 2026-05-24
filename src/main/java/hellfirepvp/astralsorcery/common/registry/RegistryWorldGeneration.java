package hellfirepvp.astralsorcery.common.registry;

import java.util.HashMap;
import hellfirepvp.astralsorcery.common.world.TemplateStructureFeature;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.core.Registry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import java.util.function.Consumer;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import java.util.function.Function;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import java.util.ArrayList;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.tags.TagKey;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import hellfirepvp.astralsorcery.common.world.feature.config.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.world.structure.feature.FeatureSmallShrineStructure;
import hellfirepvp.astralsorcery.common.world.structure.feature.FeatureDesertShrineStructure;
import hellfirepvp.astralsorcery.common.world.structure.feature.FeatureAncientShrineStructure;
import hellfirepvp.astralsorcery.common.world.structure.SmallShrineStructure;
import hellfirepvp.astralsorcery.common.world.structure.DesertShrineStructure;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import hellfirepvp.astralsorcery.common.world.structure.AncientShrineStructure;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.feature.Feature;
import hellfirepvp.astralsorcery.common.lib.WorldGenerationAS;
import net.minecraft.world.gen.GenerationStage;
import hellfirepvp.astralsorcery.common.world.FeatureGenerationConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import hellfirepvp.astralsorcery.common.world.StructureGenerationConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import java.util.Map;

public class RegistryWorldGeneration
{
    private static final Map<StructureFeature<?, ?>, StructureGenerationConfig> STRUCTURES;
    private static final Map<ConfiguredFeature<?, ?>, FeatureGenerationConfig> FEATURES;
    private static final Map<ConfiguredFeature<?, ?>, GenerationStage.Decoration> FEATURE_STAGE;
    
    public static void init() {
        registerFeature(WorldGenerationAS.Features.KEY_FEATURE_REPLACE_BLOCK, WorldGenerationAS.Features.REPLACE_BLOCK);
        registerFeature(WorldGenerationAS.Features.KEY_FEATURE_ROCK_CRYSTAL, WorldGenerationAS.Features.ROCK_CRYSTAL);
        registerPlacement(WorldGenerationAS.Placements.KEY_PLACEMENT_CHANCE, (Placement<?>)WorldGenerationAS.Placements.CHANCE);
        registerPlacement(WorldGenerationAS.Placements.KEY_PLACEMENT_RIVERBED, WorldGenerationAS.Placements.RIVERBED);
        registerPlacement(WorldGenerationAS.Placements.KEY_PLACEMENT_WORLD_FILTER, WorldGenerationAS.Placements.WORLD_FILTER);
        WorldGenerationAS.Structures.ANCIENT_SHRINE_PIECE = registerStructurePiece(WorldGenerationAS.Structures.KEY_ANCIENT_SHRINE, (IStructurePieceType)AncientShrineStructure::new);
        WorldGenerationAS.Structures.DESERT_SHRINE_PIECE = registerStructurePiece(WorldGenerationAS.Structures.KEY_DESERT_SHRINE, (IStructurePieceType)DesertShrineStructure::new);
        WorldGenerationAS.Structures.SMALL_SHRINE_PIECE = registerStructurePiece(WorldGenerationAS.Structures.KEY_SMALL_SHRINE, (IStructurePieceType)SmallShrineStructure::new);
        WorldGenerationAS.Structures.STRUCTURE_ANCIENT_SHRINE = registerStructure(WorldGenerationAS.Structures.KEY_ANCIENT_SHRINE, WorldGenerationAS.Config.CFG_ANCIENT_SHRINE, new FeatureAncientShrineStructure());
        WorldGenerationAS.Structures.STRUCTURE_DESERT_SHRINE = registerStructure(WorldGenerationAS.Structures.KEY_DESERT_SHRINE, WorldGenerationAS.Config.CFG_DESERT_SHRINE, new FeatureDesertShrineStructure());
        WorldGenerationAS.Structures.STRUCTURE_SMALL_SHRINE = registerStructure(WorldGenerationAS.Structures.KEY_SMALL_SHRINE, WorldGenerationAS.Config.CFG_SMALL_SHRINE, new FeatureSmallShrineStructure());
        WorldGenerationAS.Features.GEN_GLOW_FLOWER = registerConfiguredFeature(WorldGenerationAS.Features.KEY_GLOW_FLOWER, GenerationStage.Decoration.VEGETAL_DECORATION, WorldGenerationAS.Config.CFG_GLOW_FLOWER, (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.field_227247_y_.func_225566_b_((IFeatureConfig)new BlockClusterFeatureConfig.Builder((BlockStateProvider)new SimpleBlockStateProvider(BlocksAS.GLOW_FLOWER.defaultBlockState()), (BlockPlacer)SimpleBlockPlacer.field_236447_c_).func_227315_a_(12).func_227322_d_()).func_242732_c(6)).func_227228_a_(Features.Placements.field_244000_k).func_227228_a_(Features.Placements.field_244001_l).func_227228_a_(WorldGenerationAS.Placements.WORLD_FILTER.func_227446_a_((IPlacementConfig)WorldGenerationAS.Config.CFG_GLOW_FLOWER.worldFilterConfig())));
        WorldGenerationAS.Features.GEN_ROCK_CRYSTAL = registerConfiguredFeature(WorldGenerationAS.Features.KEY_ROCK_CRYSTAL, GenerationStage.Decoration.UNDERGROUND_ORES, WorldGenerationAS.Config.CFG_ROCK_CRYSTAL, (ConfiguredFeature<?, ?>)WorldGenerationAS.Features.ROCK_CRYSTAL.func_225566_b_((IFeatureConfig)new ReplaceBlockConfig(OreFeatureConfig.FillerBlockType.field_241882_a, BlocksAS.ROCK_CRYSTAL_ORE.defaultBlockState())).func_227228_a_(Placement.field_242907_l.func_227446_a_((IPlacementConfig)new TopSolidRangeConfig(5, 0, 2))).func_227228_a_((ConfiguredPlacement)WorldGenerationAS.Placements.CHANCE.withChance(0.04f)).func_227228_a_(WorldGenerationAS.Placements.WORLD_FILTER.func_227446_a_((IPlacementConfig)WorldGenerationAS.Config.CFG_ROCK_CRYSTAL.worldFilterConfig())));
        WorldGenerationAS.Features.GEN_AQUAMARINE = registerConfiguredFeature(WorldGenerationAS.Features.KEY_AQUAMARINE, GenerationStage.Decoration.UNDERGROUND_ORES, WorldGenerationAS.Config.CFG_AQUAMARINE, (ConfiguredFeature<?, ?>)((ConfiguredFeature)WorldGenerationAS.Features.REPLACE_BLOCK.func_225566_b_((IFeatureConfig)new ReplaceBlockConfig((RuleTest)new TagMatchRuleTest((ITag)BlockTags.field_203436_u), BlocksAS.AQUAMARINE_SAND_ORE.defaultBlockState())).func_227228_a_(WorldGenerationAS.Placements.RIVERBED.func_227446_a_((IPlacementConfig)NoPlacementConfig.field_236556_b_)).func_242732_c(8)).func_227228_a_(WorldGenerationAS.Placements.WORLD_FILTER.func_227446_a_((IPlacementConfig)WorldGenerationAS.Config.CFG_AQUAMARINE.worldFilterConfig())));
        WorldGenerationAS.Features.GEN_MARBLE = registerConfiguredFeature(WorldGenerationAS.Features.KEY_MARBLE, GenerationStage.Decoration.UNDERGROUND_ORES, WorldGenerationAS.Config.CFG_MARBLE, (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)((ConfiguredFeature)Feature.field_202290_aj.func_225566_b_((IFeatureConfig)new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, BlocksAS.MARBLE_RAW.defaultBlockState(), 26)).func_242733_d(96)).func_242728_a()).func_242732_c(10)).func_227228_a_(WorldGenerationAS.Placements.WORLD_FILTER.func_227446_a_((IPlacementConfig)WorldGenerationAS.Config.CFG_MARBLE.worldFilterConfig())));
    }
    
    public static void registerStructureGeneration() {
        final List<Map<Structure<?>, StructureSeparationSettings>> structureSettings = new ArrayList<Map<Structure<?>, StructureSeparationSettings>>();
        structureSettings.add(DimensionSettings.field_242740_q.func_236108_a_().func_236195_a_());
        WorldGenRegistries.field_243658_j.forEach(settings -> structureSettings.add(settings.func_236108_a_().func_236195_a_()));
        final ImmutableMap.Builder<Structure<?>, StructureSeparationSettings> builder = (ImmutableMap.Builder<Structure<?>, StructureSeparationSettings>)ImmutableMap.builder();
        builder.putAll((Map)DimensionStructuresSettings.field_236191_b_);
        RegistryWorldGeneration.STRUCTURES.forEach((structureFeature, cfg) -> {
            if (cfg.isEnabled()) {
                final StructureSeparationSettings settings2 = cfg.createSettings();
                builder.put((Object)structureFeature.field_236268_b_, (Object)settings2);
                structureSettings.forEach(noiseStructureSettings -> {
                    final StructureSeparationSettings structureSeparationSettings = noiseStructureSettings.put(structureFeature.field_236268_b_, settings);
                });
            }
            return;
        });
        DimensionStructuresSettings.field_236191_b_ = builder.build();
        Structure.field_236384_t_ = (List)ImmutableList.builder().addAll((Iterable)Structure.field_236384_t_).add((Object[])new Structure[] { WorldGenerationAS.Structures.STRUCTURE_ANCIENT_SHRINE, WorldGenerationAS.Structures.STRUCTURE_DESERT_SHRINE, WorldGenerationAS.Structures.STRUCTURE_SMALL_SHRINE }).build();
    }
    
    public static void loadBiomeFeatures(final BiomeLoadingEvent event) {
        final BiomeGenerationSettingsBuilder gen = event.getGeneration();
        RegistryWorldGeneration.STRUCTURES.forEach((structureFeature, cfg) -> {
            if (cfg.isEnabled() && cfg.canGenerateIn(event.getCategory())) {
                gen.func_242516_a(structureFeature);
            }
            return;
        });
        RegistryWorldGeneration.FEATURES.forEach((feature, cfg) -> {
            if (cfg.isEnabled() && cfg.canGenerateIn(event.getCategory())) {
                final GenerationStage.Decoration stage = RegistryWorldGeneration.FEATURE_STAGE.get(feature);
                if (stage == null) {
                    final ResourceLocation key = WorldGenRegistries.field_243653_e.func_230519_c_((Object)feature).map(RegistryKey::func_240901_a_).orElse(new ResourceLocation("not_registered"));
                    throw new IllegalArgumentException("Unknown generation stage for feature " + key + "!");
                }
                else {
                    gen.func_242513_a(stage, feature);
                }
            }
        });
    }
    
    public static void addConfigEntries(final Consumer<ConfigEntry> registrar) {
        registrar.accept(WorldGenerationAS.Config.CFG_ANCIENT_SHRINE);
        registrar.accept(WorldGenerationAS.Config.CFG_DESERT_SHRINE);
        registrar.accept(WorldGenerationAS.Config.CFG_SMALL_SHRINE);
        registrar.accept(WorldGenerationAS.Config.CFG_GLOW_FLOWER);
        registrar.accept(WorldGenerationAS.Config.CFG_ROCK_CRYSTAL);
        registrar.accept(WorldGenerationAS.Config.CFG_AQUAMARINE);
        registrar.accept(WorldGenerationAS.Config.CFG_MARBLE);
    }
    
    private static ConfiguredFeature<?, ?> registerConfiguredFeature(final ResourceLocation key, final GenerationStage.Decoration stage, final FeatureGenerationConfig cfg, final ConfiguredFeature<?, ?> feature) {
        RegistryWorldGeneration.FEATURE_STAGE.put(feature, stage);
        RegistryWorldGeneration.FEATURES.put(feature, cfg);
        return (ConfiguredFeature<?, ?>)Registry.func_218322_a(WorldGenRegistries.field_243653_e, key, (Object)feature);
    }
    
    private static void registerFeature(final ResourceLocation key, final Feature<?> feature) {
        AstralSorcery.getProxy().getRegistryPrimer().register(feature.setRegistryName(key));
    }
    
    private static void registerPlacement(final ResourceLocation key, final Placement<?> placement) {
        AstralSorcery.getProxy().getRegistryPrimer().register(placement.setRegistryName(key));
    }
    
    private static <T extends IStructurePieceType> T registerStructurePiece(final ResourceLocation key, final T type) {
        return (T)Registry.func_218322_a(Registry.field_218362_C, key, (Object)type);
    }
    
    private static <S extends TemplateStructureFeature> S registerStructure(final ResourceLocation key, final StructureGenerationConfig cfg, final S structure) {
        AstralSorcery.getProxy().getRegistryPrimer().register(structure.setRegistryName(key));
        Structure.field_236365_a_.put((Object)structure.func_143025_a(), (Object)structure);
        final StructureFeature<?, ?> structureFeature = (StructureFeature<?, ?>)structure.func_236391_a_((IFeatureConfig)IFeatureConfig.field_202429_e);
        RegistryWorldGeneration.STRUCTURES.put(structureFeature, cfg);
        WorldGenRegistries.func_243664_a(WorldGenRegistries.field_243654_f, key, (Object)structureFeature);
        return structure;
    }
    
    static {
        STRUCTURES = new HashMap<StructureFeature<?, ?>, StructureGenerationConfig>();
        FEATURES = new HashMap<ConfiguredFeature<?, ?>, FeatureGenerationConfig>();
        FEATURE_STAGE = new HashMap<ConfiguredFeature<?, ?>, GenerationStage.Decoration>();
    }
}
