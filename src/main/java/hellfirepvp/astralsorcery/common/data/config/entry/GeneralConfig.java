package hellfirepvp.astralsorcery.common.data.config.entry;

import java.util.function.Predicate;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class GeneralConfig extends ConfigEntry
{
    public static final GeneralConfig CONFIG;
    public ForgeConfigSpec.IntValue dayLength;
    public ForgeConfigSpec.BooleanValue giveJournalOnJoin;
    public ForgeConfigSpec.BooleanValue mobSpawningDenyAllTypes;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> modidOreBlacklist;
    public ForgeConfigSpec.BooleanValue doColoredLensesAffectPlayers;
    
    private GeneralConfig() {
        super("general");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.dayLength = cfgBuilder.comment("Defines the length of a day (both daytime & nighttime obviously) for the mod's internal logic. NOTE: This does NOT CHANGE HOW LONG A DAY IN MC IS! It is only to provide potential compatibility for mods that do provide such functionality.").translation(this.translationKey("dayLength")).defineInRange("dayLength", 24000, 1000, 400000);
        this.giveJournalOnJoin = cfgBuilder.comment("If set to 'true', the player will receive an AstralSorcery Journal when they join the server for the first time.").translation(this.translationKey("giveJournalOnJoin")).define("giveJournalOnJoin", true);
        this.mobSpawningDenyAllTypes = cfgBuilder.comment("If set to 'true' anything that prevents mobspawning !by this mod!, will also prevent EVERY natural mobspawning of any mobtype. When set to 'false' it'll only stop monsters of type 'MONSTER' from spawning.").translation(this.translationKey("mobSpawningDenyAllTypes")).define("mobSpawningDenyAllTypes", false);
        this.modidOreBlacklist = (ForgeConfigSpec.ConfigValue<List<? extends String>>)cfgBuilder.comment("Features generating random ores in AstralSorcery will not spawn ores from mods listed here.").translation(this.translationKey("modidOreBlacklist")).defineList("modidOreBlacklist", (List)Arrays.asList("techreborn", "gregtech"), (Predicate)Predicates.alwaysTrue());
        this.doColoredLensesAffectPlayers = cfgBuilder.comment("Set this to false to prevent players from being affected by entity-related colored lens effects.").translation(this.translationKey("doColoredLensesAffectPlayers")).define("doColoredLensesAffectPlayers", true);
    }
    
    static {
        CONFIG = new GeneralConfig();
    }
}
