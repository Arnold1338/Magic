package hellfirepvp.astralsorcery.common.data.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class ToolsConfig extends ConfigEntry
{
    public static final ToolsConfig CONFIG;
    public ForgeConfigSpec.DoubleValue capeChaosResistance;
    
    private ToolsConfig() {
        super("tools");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.capeChaosResistance = cfgBuilder.comment("Sets the amount of damage reduction a player gets when being hit by a DE chaos-damage-related damagetype.").translation(this.translationKey("capeChaosResistance")).defineInRange("capeChaosResistance", 0.8, 0.0, 1.0);
    }
    
    static {
        CONFIG = new ToolsConfig();
    }
}
