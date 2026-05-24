package hellfirepvp.astralsorcery.common.data.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class WorldGenConfig extends ConfigEntry
{
    public static final WorldGenConfig CONFIG;
    
    private WorldGenConfig() {
        super("worldgen");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
    }
    
    static {
        CONFIG = new WorldGenConfig();
    }
}
