package hellfirepvp.astralsorcery.common.world.feature;

import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.astralsorcery.common.data.world.RockCrystalBuffer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.IServerWorld;

public class RockCrystalFeature extends ReplaceBlockFeature
{
    @Override
    protected boolean setBlockState(final IServerWorld world, final BlockPos pos, final BlockState state) {
        ((RockCrystalBuffer)DataAS.DOMAIN_AS.getData((Level)world.func_201672_e(), (WorldCacheDomain.SaveKey)DataAS.KEY_ROCK_CRYSTAL_BUFFER)).addOre(pos);
        return super.setBlockState(world, pos, state);
    }
}
