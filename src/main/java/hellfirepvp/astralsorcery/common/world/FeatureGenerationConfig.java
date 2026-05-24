package hellfirepvp.astralsorcery.common.world;

import net.minecraft.core.Registry;
import hellfirepvp.astralsorcery.common.world.placement.config.WorldFilterConfig;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Arrays;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class FeatureGenerationConfig extends ConfigEntry
{
    private List<Biome.Category> categories;
    private List<RegistryKey<World>> worlds;
    private boolean defaultEveryBiome;
    private boolean defaultEveryWorld;
    private ForgeConfigSpec.BooleanValue enabled;
    private ForgeConfigSpec.BooleanValue everyBiome;
    private ForgeConfigSpec.BooleanValue everyWorld;
    private ForgeConfigSpec.ConfigValue<List<String>> biomeCategoryNames;
    private ForgeConfigSpec.ConfigValue<List<String>> worldNames;
    
    public FeatureGenerationConfig(final ResourceLocation featureName) {
        this(featureName.func_110623_a());
    }
    
    public FeatureGenerationConfig(final String featureName) {
        super(featureName);
        this.categories = new ArrayList<Biome.Category>();
        this.worlds = new ArrayList<RegistryKey<World>>();
        this.defaultEveryBiome = false;
        this.defaultEveryWorld = false;
    }
    
    public <T extends FeatureGenerationConfig> T generatesInBiomes(final List<Biome.Category> biomeCategories) {
        this.categories = biomeCategories;
        return (T)this;
    }
    
    public <T extends FeatureGenerationConfig> T generatesInWorlds(final List<RegistryKey<World>> worlds) {
        this.worlds = worlds;
        return (T)this;
    }
    
    public <T extends FeatureGenerationConfig> T setGenerateEveryBiome() {
        this.defaultEveryBiome = true;
        return (T)this;
    }
    
    public <T extends FeatureGenerationConfig> T setGenerateEveryWorld() {
        this.defaultEveryWorld = true;
        return (T)this;
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.enabled = cfgBuilder.comment("Set this to false to disable this worldgen feature.").translation(this.translationKey("enabled")).define("enabled", true);
        this.everyBiome = cfgBuilder.comment("Set this to true to let this feature generate in any biome.").translation(this.translationKey("everyBiome")).define("everyBiome", this.defaultEveryBiome);
        this.everyWorld = cfgBuilder.comment("Set this to true to let this feature generate in any world. (Does NOT work for structures!)").translation(this.translationKey("everyWorld")).define("everyWorld", this.defaultEveryWorld);
        final String allCategories = Arrays.stream(Biome.Category.values()).map((Function<? super Biome.Category, ?>)Biome.Category::func_176610_l).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
        final List<String> defaultCategories = this.categories.stream().map((Function<? super Object, ?>)Biome.Category::func_176610_l).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        this.biomeCategoryNames = (ForgeConfigSpec.ConfigValue<List<String>>)cfgBuilder.comment("Sets the categories to generate this feature in. Available categories: " + allCategories).translation(this.translationKey("biomeCategoryNames")).define("biomeCategoryNames", (Object)defaultCategories);
        final List<String> defaultWorlds = this.worlds.stream().map((Function<? super Object, ?>)RegistryKey::func_240901_a_).map((Function<? super Object, ?>)ResourceLocation::func_110623_a).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        this.worldNames = (ForgeConfigSpec.ConfigValue<List<String>>)cfgBuilder.comment("Sets the worlds to generate this feature in. (Does NOT work for structures!)").translation(this.translationKey("worldNames")).define("worldNames", (Object)defaultWorlds);
    }
    
    public boolean isEnabled() {
        return (boolean)this.enabled.get();
    }
    
    public boolean canGenerateIn(final Biome.Category category) {
        return (boolean)this.everyBiome.get() || ((List)this.biomeCategoryNames.get()).contains(category.func_176610_l());
    }
    
    public WorldFilterConfig worldFilterConfig() {
        return new WorldFilterConfig(this.everyWorld::get, () -> ((List)this.worldNames.get()).stream().map(ResourceLocation::new).map(key -> RegistryKey.func_240903_a_(Registry.field_239699_ae_, key)).collect(Collectors.toList()));
    }
}
