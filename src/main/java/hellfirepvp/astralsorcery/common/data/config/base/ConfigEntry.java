package hellfirepvp.astralsorcery.common.data.config.base;

import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.config.CommonConfig;
import java.util.Locale;
import java.util.HashSet;
import net.minecraftforge.fml.config.ModConfig;
import java.util.Set;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.function.Consumer;

public abstract class ConfigEntry implements Consumer<ForgeConfigSpec.Builder>
{
    private final Set<ConfigEntry> subSections;
    private final String path;
    private String subPath;
    private ModConfig.Type configType;
    
    public ConfigEntry(final String section) {
        this.subSections = new HashSet<ConfigEntry>();
        this.subPath = "";
        this.path = section.toLowerCase(Locale.ROOT);
    }
    
    public ConfigEntry newSubSection(final ConfigEntry entry) {
        entry.subPath = this.getPath();
        entry.setConfigType(this.configType);
        this.subSections.add(entry);
        return entry;
    }
    
    final void setConfigType(final ModConfig.Type type) {
        this.configType = type;
        this.subSections.forEach(section -> section.setConfigType(type));
    }
    
    @Override
    public final void accept(final ForgeConfigSpec.Builder builder) {
        this.createEntries(builder);
        for (final ConfigEntry section : this.subSections) {
            final List<String> splitPath = CommonConfig.DOT_SPLITTER.splitToList((CharSequence)section.getPath());
            builder.push((List)splitPath);
            section.accept(builder);
            builder.pop(splitPath.size());
        }
    }
    
    protected String translationKey(final String key) {
        return String.format("config.%s.%s.%s", this.configType.extension(), this.getFullPath(), key);
    }
    
    public abstract void createEntries(final ForgeConfigSpec.Builder p0);
    
    public void reload() {
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getFullPath() {
        return this.subPath.isEmpty() ? this.getPath() : String.format("%s.%s", this.subPath, this.getPath());
    }
}
