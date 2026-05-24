package hellfirepvp.astralsorcery.common.perk;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.config.entry.PerkConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.util.SidedReference;

public class PerkLevelManager
{
    private static final SidedReference<LevelData> LEVEL_DATA;
    
    private PerkLevelManager() {
    }
    
    public static void clearCache(final LogicalSide side) {
        PerkLevelManager.LEVEL_DATA.setData(side, null);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void receiveLevelCap(final int maxLevel) {
        PerkLevelManager.LEVEL_DATA.setData(LogicalSide.CLIENT, new LevelData(maxLevel));
    }
    
    public static void loadPerkLevels() {
        PerkLevelManager.LEVEL_DATA.setData(LogicalSide.SERVER, new LevelData((int)PerkConfig.CONFIG.perkLevelCap.get()));
    }
    
    public static int getLevel(final double totalExp, final Player player, final LogicalSide side) {
        return getLevel(Mth.func_76124_d(totalExp), player, side);
    }
    
    private static int getLevel(final long totalExp, final Player player, final LogicalSide side) {
        if (totalExp <= 0L) {
            return 1;
        }
        final int levelCap = getLevelCap(side, player);
        return PerkLevelManager.LEVEL_DATA.getData(side).map(data -> {
            int i = 1;
            while (i <= levelCap) {
                if (totalExp < data.totalExpLevelRequired.getOrDefault(i, Long.MAX_VALUE)) {
                    return i;
                }
                else {
                    ++i;
                }
            }
            return levelCap;
        }).orElse(1);
    }
    
    public static long getExpForLevel(final int targetLevel, final Player player, final LogicalSide side) {
        if (targetLevel <= 1) {
            return 0L;
        }
        final int levelCap = getLevelCap(side, player);
        return PerkLevelManager.LEVEL_DATA.getData(side).map(data -> {
            int level = targetLevel;
            if (level > levelCap) {
                level = levelCap;
            }
            return data.totalExpLevelRequired.get(level);
        }).orElse(0L);
    }
    
    public static float getNextLevelPercent(final double totalExp, final Player player, final LogicalSide side) {
        final int level = getLevel(totalExp, player, side);
        if (level >= getLevelCap(side, player)) {
            return 1.0f;
        }
        return PerkLevelManager.LEVEL_DATA.getData(side).map(data -> {
            final long nextLevel = data.totalExpLevelRequired.getOrDefault(level, 0L);
            final long prevLevel = data.totalExpLevelRequired.getOrDefault(level - 1, 0L);
            return (float)(totalExp - prevLevel) / (nextLevel - prevLevel);
        }).orElse(1.0f);
    }
    
    public static int getLevelCap(final LogicalSide side, @Nullable final Player player) {
        return PerkLevelManager.LEVEL_DATA.getData(side).map(data -> data.levelCap).orElse(1);
    }
    
    static {
        LEVEL_DATA = new SidedReference<LevelData>();
    }
    
    private static class LevelData
    {
        private final Map<Integer, Long> totalExpLevelRequired;
        private final int levelCap;
        
        public LevelData(final int levelCap) {
            this.totalExpLevelRequired = new HashMap<Integer, Long>();
            this.levelCap = levelCap;
            this.buildLevelRequirements();
        }
        
        private void buildLevelRequirements() {
            if (this.totalExpLevelRequired.isEmpty()) {
                for (int i = 1; i <= this.levelCap; ++i) {
                    final long prev = this.totalExpLevelRequired.getOrDefault(i - 1, 0L);
                    this.totalExpLevelRequired.put(i, prev + 150L + 100L * Mth.func_76128_c(Math.pow(1.2000000476837158, i)));
                }
            }
        }
    }
}
