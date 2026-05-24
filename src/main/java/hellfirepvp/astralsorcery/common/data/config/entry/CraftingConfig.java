package hellfirepvp.astralsorcery.common.data.config.entry;

import hellfirepvp.astralsorcery.common.util.block.BlockStateHelper;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class CraftingConfig extends ConfigEntry
{
    public static final CraftingConfig CONFIG;
    public ForgeConfigSpec.BooleanValue liquidStarlightCrystalGrowth;
    public ForgeConfigSpec.BooleanValue liquidStarlightFormCelestialCrystalCluster;
    public ForgeConfigSpec.BooleanValue liquidStarlightFormGemCrystalCluster;
    public ForgeConfigSpec.BooleanValue liquidStarlightDropInfusedWood;
    public ForgeConfigSpec.BooleanValue liquidStarlightMergeCrystals;
    public ForgeConfigSpec.BooleanValue liquidStarlightInteractionAquamarine;
    public ForgeConfigSpec.BooleanValue liquidStarlightInteractionSand;
    public ForgeConfigSpec.BooleanValue liquidStarlightInteractionIce;
    public ForgeConfigSpec.ConfigValue<String> starmetalRevertState;
    
    private CraftingConfig() {
        super("crafting");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.liquidStarlightCrystalGrowth = cfgBuilder.comment("Set this to false to disable Rock/Celestial Crystal growing in liquid starlight.").translation(this.translationKey("liquidStarlightCrystalGrowth")).define("liquidStarlightCrystalGrowth", true);
        this.liquidStarlightFormCelestialCrystalCluster = cfgBuilder.comment("Set this to false to disable crystal + stardust -> Celestial Crystal cluster forming").translation(this.translationKey("liquidStarlightFormCelestialCrystalCluster")).define("liquidStarlightFormCelestialCrystalCluster", true);
        this.liquidStarlightFormGemCrystalCluster = cfgBuilder.comment("Set this to false to disable crystal + illumination powder -> Gem Crystal cluster forming").translation(this.translationKey("liquidStarlightFormGemCrystalCluster")).define("liquidStarlightFormGemCrystalCluster", true);
        this.liquidStarlightInteractionAquamarine = cfgBuilder.comment("Set this to false to disable that liquid starlight + lava occasionally/rarely produces aquamarine shale instead of sand.").translation(this.translationKey("liquidStarlightInteractionAquamarine")).define("liquidStarlightInteractionAquamarine", true);
        this.liquidStarlightInteractionSand = cfgBuilder.comment("Set this to false to disable that liquid starlight + lava produces sand.").translation(this.translationKey("liquidStarlightInteractionSand")).define("liquidStarlightInteractionSand", true);
        this.liquidStarlightInteractionIce = cfgBuilder.comment("Set this to false to disable that liquid starlight + water produces ice.").translation(this.translationKey("liquidStarlightInteractionIce")).define("liquidStarlightInteractionIce", true);
        this.liquidStarlightDropInfusedWood = cfgBuilder.comment("Set this to false to disable the functionality that wood logs will be converted to infused wood when thrown into liquid starlight.").translation(this.translationKey("liquidStarlightDropInfusedWood")).define("liquidStarlightDropInfusedWood", true);
        this.liquidStarlightMergeCrystals = cfgBuilder.comment("Set this to false to disable the functionality that two crystals can merge and combine stats when thrown into liquid starlight.").translation(this.translationKey("liquidStarlightMergeCrystals")).define("liquidStarlightMergeCrystals", true);
        this.starmetalRevertState = (ForgeConfigSpec.ConfigValue<String>)cfgBuilder.comment("Defines the state the starmetal ore will revert into when used up by a celestial crystal cluster. Obtain a valid state-string via '/astralsorcery serialize look' and look at the block you want to get. (Chat-Message can be copied)").translation(this.translationKey("starmetalRevertState")).define("starmetalRevertState", (Object)"minecraft:iron_ore");
    }
    
    public BlockState getStarmetalRevertBlockState() {
        return BlockStateHelper.deserialize((String)this.starmetalRevertState.get());
    }
    
    static {
        CONFIG = new CraftingConfig();
    }
}
