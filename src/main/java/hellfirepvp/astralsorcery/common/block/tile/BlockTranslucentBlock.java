package hellfirepvp.astralsorcery.common.block.tile;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.tile.TileTranslucentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Material;
import hellfirepvp.astralsorcery.common.block.base.BlockFakedState;

public class BlockTranslucentBlock extends BlockFakedState
{
    public BlockTranslucentBlock() {
        super(AbstractBlock.Properties.func_200949_a(Material.field_175972_I, MaterialColor.field_151660_b).func_200948_a(-1.0f, 6000000.0f).func_235838_a_(state -> 12));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_180655_c(final BlockState state, final Level world, final BlockPos pos, final Random rand) {
        this.playParticles(world, pos, rand);
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader world) {
        return new TileTranslucentBlock();
    }
}
