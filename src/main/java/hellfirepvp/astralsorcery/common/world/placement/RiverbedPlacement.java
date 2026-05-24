package hellfirepvp.astralsorcery.common.world.placement;

import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.FluidTags;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.gen.Heightmap;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class RiverbedPlacement extends Placement<NoPlacementConfig>
{
    public RiverbedPlacement() {
        super(NoPlacementConfig.field_236555_a_);
    }
    
    public Stream<BlockPos> getPositions(final WorldDecoratingHelper helper, final Random rand, final NoPlacementConfig config, final BlockPos pos) {
        final int x = rand.nextInt(16) + pos.getX();
        final int z = rand.nextInt(16) + pos.getZ();
        final int y = helper.func_242893_a(Heightmap.Type.OCEAN_FLOOR_WG, x, z);
        if (y <= 0) {
            return Stream.of(new BlockPos[0]);
        }
        BlockPos floor = new BlockPos(x, y - 4, z);
        boolean foundWater = false;
        for (int yy = 0; yy < 5; ++yy) {
            final BlockPos check = floor.func_177967_a(Direction.UP, yy);
            final BlockState state = helper.func_242894_a(check);
            final Block block = state.getBlock();
            final Fluid f;
            if (((f = MiscUtils.tryGetFuild(state)) != null && f.func_207185_a((ITag)FluidTags.field_206959_a)) || block.func_203417_a((ITag)BlockTags.field_205213_E)) {
                foundWater = true;
                floor = check.func_177977_b();
                break;
            }
        }
        if (foundWater && BlockTags.field_203436_u.func_230235_a_((Object)helper.func_242894_a(floor).getBlock())) {
            return Stream.of(floor);
        }
        return Stream.of(new BlockPos[0]);
    }
}
