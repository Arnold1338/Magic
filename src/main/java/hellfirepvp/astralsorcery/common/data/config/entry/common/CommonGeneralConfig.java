package hellfirepvp.astralsorcery.common.data.config.entry.common;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class CommonGeneralConfig extends ConfigEntry
{
    public static final CommonGeneralConfig CONFIG;
    
    private CommonGeneralConfig() {
        super("general");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
    }
    
    static {
        CONFIG = new CommonGeneralConfig();
    }
}
