package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.tile.TileIlluminator;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.tick.PlayerTickPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeySpawnLights extends KeyPerk implements PlayerTickPerk
{
    private static final int defaultLightSpawnRate = 15;
    private static final int defaultLightSpawnRadius = 5;
    private static final int defaultChargeCost = 60;
    public static final Config CONFIG;
    
    public KeySpawnLights(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    @Override
    public void onPlayerTick(final Player player, final LogicalSide side) {
        if (side.isServer()) {
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (!prog.isValid() || !prog.doPerkAbilities()) {
                return;
            }
            int spawnRate = (int)KeySpawnLights.CONFIG.lightSpawnRate.get();
            spawnRate = Math.max(spawnRate, 1);
            if (player.field_70173_aa % spawnRate == 0) {
                for (int attempts = 4; attempts > 0; --attempts) {
                    final int radius = (int)KeySpawnLights.CONFIG.lightSpawnRadius.get();
                    final BlockPos pos = player.func_233580_cy_().offset(KeySpawnLights.rand.nextInt(radius) * (KeySpawnLights.rand.nextBoolean() ? 1 : -1), KeySpawnLights.rand.nextInt(radius) * (KeySpawnLights.rand.nextBoolean() ? 1 : -1), KeySpawnLights.rand.nextInt(radius) * (KeySpawnLights.rand.nextBoolean() ? 1 : -1));
                    if (MiscUtils.executeWithChunk((IWorldReader)player.func_130014_f_(), pos, () -> {
                        if (TileIlluminator.ILLUMINATOR_CHECK.test(player.func_130014_f_(), pos, player.func_130014_f_().getBlockState(pos)) && AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeySpawnLights.CONFIG.chargeCost.get(), true)) {
                            if (player.func_130014_f_().func_175656_a(pos, BlocksAS.FLARE_LIGHT.defaultBlockState())) {
                                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeySpawnLights.CONFIG.chargeCost.get(), false);
                                return Boolean.valueOf(true);
                            }
                            else {
                                return Boolean.valueOf(false);
                            }
                        }
                        else {
                            return Boolean.valueOf(false);
                        }
                    }, false)) {
                        return;
                    }
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.spawn_lights");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.IntValue lightSpawnRate;
        private ForgeConfigSpec.IntValue lightSpawnRadius;
        private ForgeConfigSpec.IntValue chargeCost;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.lightSpawnRate = cfgBuilder.comment("Defines the rate in ticks a position to spawn a light in is attempted to be found near the player").translation(this.translationKey("lightSpawnRate")).defineInRange("lightSpawnRate", 15, 4, 1000);
            this.lightSpawnRadius = cfgBuilder.comment("Defines the radius around the player the perk will search for a suitable position").translation(this.translationKey("lightSpawnRadius")).defineInRange("lightSpawnRadius", 5, 2, 10);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per spawned light.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 60, 1, 500);
        }
    }
}
