package hellfirepvp.astralsorcery.common.data.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class EntityConfig extends ConfigEntry
{
    public static final EntityConfig CONFIG;
    public ForgeConfigSpec.IntValue flareAmbientSpawnChance;
    public ForgeConfigSpec.BooleanValue flareAttackBats;
    public ForgeConfigSpec.BooleanValue flareAttackPhantoms;
    
    private EntityConfig() {
        super("entities");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.flareAmbientSpawnChance = cfgBuilder.comment("Defines how common ***ambient*** flares are. the lower the more common. 0 = ambient ones don't appear/disable").translation(this.translationKey("flareAmbientSpawnChance")).defineInRange("flareAmbientSpawnChance", 10, 0, 200000);
        this.flareAttackBats = cfgBuilder.comment("If this is set to true, occasionally, a spawned flare will (attempt to) kill bats close to it.").translation(this.translationKey("flareAttackBats")).define("flareAttackBats", true);
        this.flareAttackPhantoms = cfgBuilder.comment("If this is set to true, occasionally, a spawned flare will (attempt to) kill phantoms close to it.").translation(this.translationKey("flareAttackPhantoms")).define("flareAttackPhantoms", true);
    }
    
    static {
        CONFIG = new EntityConfig();
    }
}
