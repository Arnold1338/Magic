package hellfirepvp.observerlib.api.util;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;

public class SingleBiomeManager extends BiomeManager {
    public SingleBiomeManager(Holder<Biome> globalBiome) {
        super((x, y, z) -> globalBiome, 0L);
    }
}
