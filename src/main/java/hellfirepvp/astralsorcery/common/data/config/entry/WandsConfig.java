package hellfirepvp.astralsorcery.common.data.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class WandsConfig extends ConfigEntry
{
    public static final WandsConfig CONFIG;
    public ForgeConfigSpec.DoubleValue illuminationWandCost;
    public ForgeConfigSpec.DoubleValue architectWandCost;
    public ForgeConfigSpec.DoubleValue exchangeWandCost;
    public ForgeConfigSpec.DoubleValue grappleWandCost;
    public ForgeConfigSpec.IntValue exchangeWandMaxHardness;
    
    private WandsConfig() {
        super("wands");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.illuminationWandCost = cfgBuilder.comment("Sets the quick-charge cost for one usage of the illumination wand").translation(this.translationKey("illuminationWandCost")).defineInRange("illuminationWandCost", 0.5, 0.0, 1.0);
        this.architectWandCost = cfgBuilder.comment("Sets the quick-charge cost for one usage of the architect wand").translation(this.translationKey("architectWandCost")).defineInRange("architectWandCost", 0.03, 0.0, 1.0);
        this.exchangeWandCost = cfgBuilder.comment("Sets the quick-charge cost for one usage of the exchange wand").translation(this.translationKey("exchangeWandCost")).defineInRange("exchangeWandCost", 0.002, 0.0, 1.0);
        this.grappleWandCost = cfgBuilder.comment("Sets the quick-charge cost for one usage of the grapple wand").translation(this.translationKey("grappleWandCost")).defineInRange("grappleWandCost", 0.7, 0.0, 1.0);
        this.exchangeWandMaxHardness = cfgBuilder.comment("Sets the max. hardness the exchange wand can swap !from!. If the block you're trying to \"mine\" with the conversion wand is higher than this number, it won't work. (-1 to disable this check)").translation(this.translationKey("exchangeWandMaxHardness")).defineInRange("exchangeWandMaxHardness", -1, -1, 50000);
    }
    
    static {
        CONFIG = new WandsConfig();
    }
}
