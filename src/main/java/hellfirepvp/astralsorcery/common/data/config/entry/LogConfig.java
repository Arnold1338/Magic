package hellfirepvp.astralsorcery.common.data.config.entry;

import java.util.Locale;
import java.util.HashMap;
import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class LogConfig extends ConfigEntry
{
    public static final LogConfig CONFIG;
    private Map<LogCategory, ForgeConfigSpec.BooleanValue> loggingConfigurations;
    
    private LogConfig() {
        super("logging");
        this.loggingConfigurations = new HashMap<LogCategory, ForgeConfigSpec.BooleanValue>();
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        for (final LogCategory category : LogCategory.values()) {
            final ForgeConfigSpec.BooleanValue bValLogging = cfgBuilder.comment("Set to true to enable this logging category. Only do this if you have to debug this section of code! May spam your log HEAVILY!").translation(this.translationKey(category.name().toLowerCase(Locale.ROOT))).define(category.name().toLowerCase(Locale.ROOT), false);
            this.loggingConfigurations.put(category, bValLogging);
        }
    }
    
    public boolean isLoggingEnabled(final LogCategory category) {
        final ForgeConfigSpec.BooleanValue cfg = this.loggingConfigurations.get(category);
        return cfg != null && (boolean)cfg.get();
    }
    
    static {
        CONFIG = new LogConfig();
    }
}
