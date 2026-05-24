package hellfirepvp.astralsorcery.common.util.log;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.data.config.entry.LogConfig;
import java.util.function.Supplier;

public class LogUtil
{
    private static final String PREFIX = "[DEBUG-%s]: %s";
    
    public static void info(final LogCategory category, final Supplier<String> msgProvider) {
        if (LogConfig.CONFIG.isLoggingEnabled(category)) {
            AstralSorcery.log.info(String.format("[DEBUG-%s]: %s", category.name(), msgProvider.get()));
        }
    }
    
    public static void warn(final LogCategory category, final Supplier<String> msgProvider) {
        if (LogConfig.CONFIG.isLoggingEnabled(category)) {
            AstralSorcery.log.warn(String.format("[DEBUG-%s]: %s", category.name(), msgProvider.get()));
        }
    }
}
