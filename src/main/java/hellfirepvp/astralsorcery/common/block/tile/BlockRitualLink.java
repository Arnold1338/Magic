package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.phys.shapes.Shapes;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.tile.TileRitualLink;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesGlass;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.block.ContainerBlock;

public class BlockRitualLink extends ContainerBlock implements CustomItemBlock
{
    private static final VoxelShape RITUAL_LINK;
    
    public BlockRitualLink() {
        super(PropertiesGlass.coatedGlass().harvestTool(ToolType.PICKAXE));
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return BlockRitualLink.RITUAL_LINK;
    }
    
    public BlockRenderType func_149645_b(final BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileRitualLink();
    }
    
    static {
        RITUAL_LINK = VoxelShapes.func_197873_a(0.375, 0.125, 0.375, 0.625, 0.875, 0.625);
    }
}
