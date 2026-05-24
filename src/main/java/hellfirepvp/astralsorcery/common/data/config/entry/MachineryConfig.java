package hellfirepvp.astralsorcery.common.data.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class MachineryConfig extends ConfigEntry
{
    public static final MachineryConfig CONFIG;
    
    private MachineryConfig() {
        super("machinery");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
    }
    
    static {
        CONFIG = new MachineryConfig();
    }
}
