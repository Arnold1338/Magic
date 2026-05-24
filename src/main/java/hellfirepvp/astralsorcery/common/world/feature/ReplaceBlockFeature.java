package hellfirepvp.astralsorcery.common.world.feature;

import net.minecraft.world.level.levelgen.feature.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.IServerWorld;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.ISeedReader;
import com.mojang.serialization.Codec;
import hellfirepvp.astralsorcery.common.world.feature.config.ReplaceBlockConfig;
import net.minecraft.world.level.levelgen.feature.Feature;

public class ReplaceBlockFeature extends Feature<ReplaceBlockConfig>
{
    public ReplaceBlockFeature() {
        super((Codec)ReplaceBlockConfig.CODEC);
    }
    
    public boolean generate(final ISeedReader reader, final ChunkGenerator generator, final Random rand, final BlockPos pos, final ReplaceBlockConfig config) {
        return !config.target.func_215181_a(reader.getBlockState(pos), rand) || this.setBlockState((IServerWorld)reader, pos, config.state);
    }
    
    protected boolean setBlockState(final IServerWorld world, final BlockPos pos, final BlockState state) {
        return world.func_180501_a(pos, state, 2);
    }
}
