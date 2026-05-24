package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.tile.TileRitualLink;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesGlass;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockRitualLink extends BaseEntityBlock implements CustomItemBlock
{
    private static final VoxelShape RITUAL_LINK;
    
    public BlockRitualLink() {
        super(PropertiesGlass.coatedGlass().harvestTool(ToolType.PICKAXE));
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return BlockRitualLink.RITUAL_LINK;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileRitualLink();
    }
    
    static {
        RITUAL_LINK = VoxelShapes.func_197873_a(0.375, 0.125, 0.375, 0.625, 0.875, 0.625);
    }
}
