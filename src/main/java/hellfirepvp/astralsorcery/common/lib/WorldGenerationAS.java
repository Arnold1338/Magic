package hellfirepvp.astralsorcery.common.lib;

import net.minecraft.resources.ResourceKey;
import java.util.Collections;
import net.minecraft.world.level.Level;
import java.util.Arrays;
import net.minecraft.world.level.biome.Biome;
import hellfirepvp.astralsorcery.common.world.FeatureGenerationConfig;
import hellfirepvp.astralsorcery.common.world.StructureGenerationConfig;
import hellfirepvp.astralsorcery.common.world.placement.WorldFilteredPlacement;
import hellfirepvp.astralsorcery.common.world.placement.RiverbedPlacement;
import hellfirepvp.astralsorcery.common.world.placement.ChancePlacement;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import hellfirepvp.astralsorcery.common.world.feature.RockCrystalFeature;
import hellfirepvp.astralsorcery.common.world.feature.ReplaceBlockFeature;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.resources.ResourceLocation;

public class WorldGenerationAS
{
    public static class Structures
    {
        public static final ResourceLocation KEY_ANCIENT_SHRINE;
        public static final ResourceLocation KEY_DESERT_SHRINE;
        public static final ResourceLocation KEY_SMALL_SHRINE;
        public static IStructurePieceType ANCIENT_SHRINE_PIECE;
        public static IStructurePieceType DESERT_SHRINE_PIECE;
        public static IStructurePieceType SMALL_SHRINE_PIECE;
        public static Structure<NoFeatureConfig> STRUCTURE_ANCIENT_SHRINE;
        public static Structure<NoFeatureConfig> STRUCTURE_DESERT_SHRINE;
        public static Structure<NoFeatureConfig> STRUCTURE_SMALL_SHRINE;
        
        static {
            KEY_ANCIENT_SHRINE = AstralSorcery.key("ancient_shrine");
            KEY_DESERT_SHRINE = AstralSorcery.key("desert_shrine");
            KEY_SMALL_SHRINE = AstralSorcery.key("small_shrine");
        }
    }
    
    public static class Features
    {
        public static final ResourceLocation KEY_GLOW_FLOWER;
        public static final ResourceLocation KEY_ROCK_CRYSTAL;
        public static final ResourceLocation KEY_AQUAMARINE;
        public static final ResourceLocation KEY_MARBLE;
        public static final ResourceLocation KEY_FEATURE_REPLACE_BLOCK;
        public static final ResourceLocation KEY_FEATURE_ROCK_CRYSTAL;
        public static final ReplaceBlockFeature REPLACE_BLOCK;
        public static final RockCrystalFeature ROCK_CRYSTAL;
        public static ConfiguredFeature<?, ?> GEN_GLOW_FLOWER;
        public static ConfiguredFeature<?, ?> GEN_ROCK_CRYSTAL;
        public static ConfiguredFeature<?, ?> GEN_AQUAMARINE;
        public static ConfiguredFeature<?, ?> GEN_MARBLE;
        
        static {
            KEY_GLOW_FLOWER = AstralSorcery.key("glow_flower");
            KEY_ROCK_CRYSTAL = AstralSorcery.key("rock_crystal");
            KEY_AQUAMARINE = AstralSorcery.key("aquamarine");
            KEY_MARBLE = AstralSorcery.key("marble");
            KEY_FEATURE_REPLACE_BLOCK = AstralSorcery.key("replace_block");
            KEY_FEATURE_ROCK_CRYSTAL = AstralSorcery.key("rock_crystal");
            REPLACE_BLOCK = new ReplaceBlockFeature();
            ROCK_CRYSTAL = new RockCrystalFeature();
        }
    }
    
    public static class Placements
    {
        public static final ResourceLocation KEY_PLACEMENT_CHANCE;
        public static final ResourceLocation KEY_PLACEMENT_RIVERBED;
        public static final ResourceLocation KEY_PLACEMENT_WORLD_FILTER;
        public static final ChancePlacement CHANCE;
        public static final RiverbedPlacement RIVERBED;
        public static final WorldFilteredPlacement WORLD_FILTER;
        
        static {
            KEY_PLACEMENT_CHANCE = AstralSorcery.key("chance");
            KEY_PLACEMENT_RIVERBED = AstralSorcery.key("riverbed");
            KEY_PLACEMENT_WORLD_FILTER = AstralSorcery.key("world_filter");
            CHANCE = new ChancePlacement();
            RIVERBED = new RiverbedPlacement();
            WORLD_FILTER = new WorldFilteredPlacement();
        }
    }
    
    public static class Config
    {
        public static StructureGenerationConfig CFG_ANCIENT_SHRINE;
        public static StructureGenerationConfig CFG_DESERT_SHRINE;
        public static StructureGenerationConfig CFG_SMALL_SHRINE;
        public static FeatureGenerationConfig CFG_GLOW_FLOWER;
        public static FeatureGenerationConfig CFG_ROCK_CRYSTAL;
        public static FeatureGenerationConfig CFG_AQUAMARINE;
        public static FeatureGenerationConfig CFG_MARBLE;
        
        static {
            Config.CFG_ANCIENT_SHRINE = new StructureGenerationConfig(Structures.KEY_ANCIENT_SHRINE, 18, 4).generatesInBiomes(Arrays.asList(Biome.Category.ICY, Biome.Category.EXTREME_HILLS)).generatesInWorlds(Collections.singletonList(World.field_234918_g_));
            Config.CFG_DESERT_SHRINE = new StructureGenerationConfig(Structures.KEY_DESERT_SHRINE, 18, 4).generatesInBiomes(Arrays.asList(Biome.Category.MESA, Biome.Category.DESERT, Biome.Category.SAVANNA)).generatesInWorlds(Collections.singletonList(World.field_234918_g_));
            Config.CFG_SMALL_SHRINE = new StructureGenerationConfig(Structures.KEY_SMALL_SHRINE, 18, 4).generatesInBiomes(Arrays.asList(Biome.Category.FOREST, Biome.Category.PLAINS)).generatesInWorlds(Collections.singletonList(World.field_234918_g_));
            Config.CFG_GLOW_FLOWER = new FeatureGenerationConfig(Features.KEY_GLOW_FLOWER).generatesInBiomes(Arrays.asList(Biome.Category.ICY, Biome.Category.EXTREME_HILLS)).generatesInWorlds(Collections.singletonList(World.field_234918_g_));
            Config.CFG_ROCK_CRYSTAL = new FeatureGenerationConfig(Features.KEY_ROCK_CRYSTAL).setGenerateEveryBiome().generatesInWorlds(Collections.singletonList(World.field_234918_g_));
            Config.CFG_AQUAMARINE = new FeatureGenerationConfig(Features.KEY_AQUAMARINE).setGenerateEveryBiome().generatesInWorlds(Collections.singletonList(World.field_234918_g_));
            Config.CFG_MARBLE = new FeatureGenerationConfig(Features.KEY_MARBLE).setGenerateEveryBiome().setGenerateEveryWorld();
        }
    }
}
