package hellfirepvp.astralsorcery.common.data.config.base;

import java.util.HashMap;
import net.minecraftforge.fml.ModContainer;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.Iterator;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Splitter;
import net.minecraftforge.fml.config.ModConfig;
import java.util.Map;

public class BaseConfiguration
{
    private static final Map<ModConfig.Type, BaseConfiguration> REGISTERED_CONFIGS;
    public static final Splitter DOT_SPLITTER;
    private final List<ConfigEntry> configEntries;
    private final ModConfig.Type configType;
    
    protected BaseConfiguration(final ModConfig.Type configType) {
        this.configEntries = new ArrayList<ConfigEntry>();
        this.configType = configType;
    }
    
    public <T extends ConfigEntry> T addConfigEntry(final T configEntry) {
        configEntry.setConfigType(this.configType);
        this.configEntries.add(configEntry);
        return configEntry;
    }
    
    public void buildConfiguration() {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        for (final ConfigEntry entry : this.configEntries) {
            final List<String> splitPath = BaseConfiguration.DOT_SPLITTER.splitToList((CharSequence)entry.getPath());
            builder.push((List)splitPath);
            entry.accept(builder);
            builder.pop(splitPath.size());
        }
        this.makeAndRegister(builder.build(), "astralsorcery");
    }
    
    private void makeAndRegister(final ForgeConfigSpec spec, final String file) {
        final String fileName = (this.configType == ModConfig.Type.SERVER) ? String.format("%s.toml", file) : String.format("%s-%s.toml", file, this.configType.extension());
        final ModContainer ct = AstralSorcery.getModContainer();
        final ModConfig cfg = new ModConfig(this.configType, spec, ct, fileName);
        ct.addConfig(cfg);
        BaseConfiguration.REGISTERED_CONFIGS.put(this.configType, this);
    }
    
    public static void refreshConfiguration(final ModConfig.Loading cfgLoadEvent) {
        final ModConfig config = cfgLoadEvent.getConfig();
        if (config.getModId().equals("astralsorcery")) {
            final BaseConfiguration cfg = BaseConfiguration.REGISTERED_CONFIGS.get(config.getType());
            if (cfg != null) {
                cfg.configEntries.forEach(ConfigEntry::reload);
            }
        }
    }
    
    static {
        REGISTERED_CONFIGS = new HashMap<ModConfig.Type, BaseConfiguration>();
        DOT_SPLITTER = Splitter.on(".");
    }
}
