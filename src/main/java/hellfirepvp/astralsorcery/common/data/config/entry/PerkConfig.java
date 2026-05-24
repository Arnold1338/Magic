package hellfirepvp.astralsorcery.common.data.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class PerkConfig extends ConfigEntry
{
    public static final PerkConfig CONFIG;
    public ForgeConfigSpec.IntValue perkLevelCap;
    
    private PerkConfig() {
        super("perks");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.perkLevelCap = cfgBuilder.comment("Sets the max level for the perk tree levels.").translation(this.translationKey("perkLevelCap")).defineInRange("perkLevelCap", 40, 10, 100);
    }
    
    static {
        CONFIG = new PerkConfig();
    }
}
