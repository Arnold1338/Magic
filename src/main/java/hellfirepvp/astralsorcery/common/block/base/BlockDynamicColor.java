package hellfirepvp.astralsorcery.common.block.base;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockDynamicColor
{
    @OnlyIn(Dist.CLIENT)
    int getColor(final BlockState p0, @Nullable final IBlockDisplayReader p1, @Nullable final BlockPos p2, final int p3);
}
