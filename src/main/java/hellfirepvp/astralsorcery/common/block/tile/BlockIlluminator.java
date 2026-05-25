package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileIlluminator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesGlass;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockIlluminator extends BaseEntityBlock implements CustomItemBlock
{
    private final VoxelShape shape;
    
    public BlockIlluminator() {
        super(PropertiesGlass.coatedGlass().func_235838_a_(state -> 10).harvestLevel(1).harvestTool(ToolType.PICKAXE));
        this.shape = this.createShape();
    }
    
    protected VoxelShape createShape() {
        final List<VoxelShape> shapes = new ArrayList<VoxelShape>();
        for (int xx = 0; xx < 3; ++xx) {
            for (int yy = 0; yy < 3; ++yy) {
                for (int zz = 0; zz < 3; ++zz) {
                    shapes.add(Block.of((double)(1 + xx * 5), (double)(1 + yy * 5), (double)(1 + zz * 5), (double)(5 + xx * 5), (double)(5 + yy * 5), (double)(5 + zz * 5)));
                }
            }
        }
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, shapes);
    }
    
    public void func_180633_a(final Level world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        super.func_180633_a(world, pos, state, placer, stack);
        if (!world.level() && placer instanceof Player) {
            final TileIlluminator illuminator = MiscUtils.getTileAt((IBlockReader)world, pos, TileIlluminator.class, true);
            if (illuminator != null) {
                illuminator.setPlayerPlaced(true);
            }
        }
    }
    
    public VoxelShape func_220053_a(final BlockState p_220053_1_, final IBlockReader p_220053_2_, final BlockPos p_220053_3_, final CollisionContext p_220053_4_) {
        return this.shape;
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader world) {
        return new TileIlluminator();
    }
}
