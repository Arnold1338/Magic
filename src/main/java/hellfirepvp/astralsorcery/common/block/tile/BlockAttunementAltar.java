package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.AABB;
import hellfirepvp.astralsorcery.common.block.base.LargeBlock;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockAttunementAltar extends BaseEntityBlock implements CustomItemBlock, LargeBlock
{
    private static final AABB PLACEMENT_BOX;
    private static final VoxelShape ATTUNEMENT_ALTAR;
    private static final VoxelShape ATTUNEMENT_ALTAR_COLLISION;
    
    public BlockAttunementAltar() {
        super(PropertiesMarble.defaultMarble().func_235838_a_(state -> 4).harvestLevel(1).harvestTool(ToolType.PICKAXE));
    }
    
    public AABB getBlockSpace() {
        return BlockAttunementAltar.PLACEMENT_BOX;
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext context) {
        return this.canPlaceAt(context) ? this.defaultBlockState() : null;
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return BlockAttunementAltar.ATTUNEMENT_ALTAR;
    }
    
    public VoxelShape func_220071_b(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return BlockAttunementAltar.ATTUNEMENT_ALTAR_COLLISION;
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileAttunementAltar();
    }
    
    static {
        PLACEMENT_BOX = new AABB(-1.0, 0.0, -1.0, 1.0, 1.0, 1.0);
        ATTUNEMENT_ALTAR = Block.of(-2.0, 0.0, -2.0, 18.0, 6.0, 18.0);
        ATTUNEMENT_ALTAR_COLLISION = Block.of(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);
    }
}
