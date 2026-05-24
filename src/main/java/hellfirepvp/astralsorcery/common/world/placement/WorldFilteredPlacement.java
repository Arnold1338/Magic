package hellfirepvp.astralsorcery.common.world.placement;

import net.minecraft.world.IServerWorld;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.IPlacementConfig;
import java.util.function.Supplier;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.List;
import com.mojang.serialization.Codec;
import hellfirepvp.astralsorcery.common.world.placement.config.WorldFilterConfig;
import net.minecraft.world.gen.placement.Placement;

public class WorldFilteredPlacement extends Placement<WorldFilterConfig>
{
    public WorldFilteredPlacement() {
        super((Codec)WorldFilterConfig.CODEC);
    }
    
    public ConfiguredPlacement<WorldFilterConfig> inWorlds(final boolean ignoreFilter, final List<RegistryKey<World>> worlds) {
        return this.inWorlds(() -> ignoreFilter, () -> worlds);
    }
    
    public ConfiguredPlacement<WorldFilterConfig> inWorlds(final Supplier<Boolean> ignoreFilter, final Supplier<List<RegistryKey<World>>> worlds) {
        return (ConfiguredPlacement<WorldFilterConfig>)this.func_227446_a_((IPlacementConfig)new WorldFilterConfig(ignoreFilter, worlds));
    }
    
    public Stream<BlockPos> getPositions(final WorldDecoratingHelper helper, final Random rand, final WorldFilterConfig config, final BlockPos pos) {
        if (config.generatesIn((IServerWorld)helper.field_242889_a)) {
            return Stream.of(pos);
        }
        return Stream.empty();
    }
}
