package hellfirepvp.astralsorcery.common.data.config.base;

import java.util.Locale;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.ArrayList;
import java.util.List;

public class ConfigRegistries
{
    private static final ConfigRegistries INSTANCE;
    private final List<ConfigDataAdapter<?>> dataRegistries;
    
    private ConfigRegistries() {
        this.dataRegistries = new ArrayList<ConfigDataAdapter<?>>();
    }
    
    public static ConfigRegistries getRegistries() {
        return ConfigRegistries.INSTANCE;
    }
    
    public void addDataRegistry(final ConfigDataAdapter<?> dataAdapter) {
        this.dataRegistries.add(dataAdapter);
    }
    
    public void buildDataRegistries(final BaseConfiguration out) {
        out.addConfigEntry(new RegistrySection());
    }
    
    static {
        INSTANCE = new ConfigRegistries();
    }
    
    private class RegistrySection extends ConfigEntry
    {
        private RegistrySection() {
            super("registries");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            for (final ConfigDataAdapter<?> dataRegistry : ConfigRegistries.this.dataRegistries) {
                final ForgeConfigSpec.ConfigValue<List<? extends String>> cfgList = (ForgeConfigSpec.ConfigValue<List<? extends String>>)cfgBuilder.comment(dataRegistry.getCommentDescription()).translation(dataRegistry.getTranslationKey()).defineList(this.registrySubSection(dataRegistry.getSectionName()), (List)dataRegistry.getDefaultValues().stream().map((Function<? super Object, ?>)ConfigDataSet::serialize).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), (Predicate)dataRegistry.getValidator());
                dataRegistry.configBuilt(cfgList);
            }
        }
        
        @Override
        public void reload() {
            ConfigRegistries.this.dataRegistries.forEach(registry -> registry.configuredValues = null);
        }
        
        private String registrySubSection(final String section) {
            return String.format("%s.%s", section.toLowerCase(Locale.ROOT), section.toLowerCase(Locale.ROOT));
        }
    }
}
