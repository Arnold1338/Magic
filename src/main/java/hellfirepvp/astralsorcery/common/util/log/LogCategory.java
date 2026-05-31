package hellfirepvp.astralsorcery.common.util.log;

import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.data.config.entry.LogConfig;

public enum LogCategory
{
    PERKS, 
    UNINTENDED_CHUNK_LOADING, 
    STRUCTURE_MATCH, 
    GATEWAY_CACHE;
    
    public boolean isEnabled() {
        return LogConfig.CONFIG.isLoggingEnabled(this);
    }
    
    public void info(final Supplier<String> message) {
        LogUtil.info(this, message);
    }
    
    public void warn(final Supplier<String> message) {
        LogUtil.warn(this, message);
    }
}
