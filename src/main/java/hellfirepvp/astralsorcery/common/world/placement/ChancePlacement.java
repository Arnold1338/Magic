package hellfirepvp.astralsorcery.common.world.placement;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import com.mojang.serialization.Codec;
import hellfirepvp.astralsorcery.common.world.placement.config.ChanceConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

public class ChancePlacement extends SimplePlacement<ChanceConfig>
{
    public ChancePlacement() {
        super((Codec)ChanceConfig.CODEC);
    }
    
    public ConfiguredPlacement<ChanceConfig> withChance(final float chance) {
        return (ConfiguredPlacement<ChanceConfig>)this.func_227446_a_((IPlacementConfig)new ChanceConfig(chance));
    }
    
    protected Stream<BlockPos> getPositions(final Random random, final ChanceConfig config, final BlockPos pos) {
        return config.test(random) ? Stream.of(pos) : Stream.empty();
    }
}
