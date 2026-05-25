package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateContainer;
import hellfirepvp.astralsorcery.common.item.lens.LensColorType;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileLens;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockLens;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesGlass;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.EnumProperty;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public class BlockLens extends BlockStarlightNetwork implements CustomItemBlock
{
    private static final VoxelShape LENS_DOWN;
    private static final VoxelShape LENS_UP;
    private static final VoxelShape LENS_NORTH;
    private static final VoxelShape LENS_SOUTH;
    private static final VoxelShape LENS_EAST;
    private static final VoxelShape LENS_WEST;
    public static EnumProperty<Direction> PLACED_AGAINST;
    
    public BlockLens() {
        super(PropertiesGlass.coatedGlass().harvestTool(ToolType.PICKAXE));
        this.func_180632_j((BlockState)((BlockState)this.func_176194_O().func_177621_b()).setValue((Property)BlockLens.PLACED_AGAINST, (Comparable)Direction.DOWN));
    }
    
    @Override
    public Class<? extends BlockItem> getItemBlockClass() {
        return ItemBlockLens.class;
    }
    
    public void func_176208_a(final Level world, final BlockPos pos, final BlockState state, final Player player) {
        final TileLens lens = MiscUtils.getTileAt((IBlockReader)world, pos, TileLens.class, true);
        if (lens != null && !world.level() && !player.getVehicle() && lens.getColorType() != null) {
            final ItemStack drop = lens.getColorType().getStack();
            ItemUtils.dropItemNaturally(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
        }
        super.func_176208_a(world, pos, state, player);
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        if (!world.level() && player.func_225608_bj_()) {
            final TileLens lens = MiscUtils.getTileAt((IBlockReader)world, pos, TileLens.class, true);
            if (lens != null && lens.getColorType() != null) {
                final ItemStack drop = lens.getColorType().getStack();
                if (player.getItemInHand(hand).isEmpty()) {
                    player.func_184611_a(hand, drop);
                }
                else if (!player.getInventory().func_70441_a(drop)) {
                    ItemUtils.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
                }
                SoundHelper.playSoundAround(SoundsAS.BLOCK_COLOREDLENS_ATTACH, world, (Vector3i)pos, 0.8f, 1.5f);
                lens.setColorType(null);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        builder.func_206894_a(new Property[] { (Property)BlockLens.PLACED_AGAINST });
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext context) {
        return (BlockState)this.defaultBlockState().setValue((Property)BlockLens.PLACED_AGAINST, (Comparable)context.func_196000_l().func_176734_d());
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        switch ((Direction)state.getValue((Property)BlockLens.PLACED_AGAINST)) {
            case UP: {
                return BlockLens.LENS_UP;
            }
            case NORTH: {
                return BlockLens.LENS_NORTH;
            }
            case SOUTH: {
                return BlockLens.LENS_SOUTH;
            }
            case WEST: {
                return BlockLens.LENS_WEST;
            }
            case EAST: {
                return BlockLens.LENS_EAST;
            }
            default: {
                return BlockLens.LENS_DOWN;
            }
        }
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileLens();
    }
    
    static {
        LENS_DOWN = VoxelShapes.func_197873_a(0.15625, 0.0, 0.15625, 0.84375, 0.90625, 0.84375);
        LENS_UP = VoxelShapes.func_197873_a(0.15625, 0.09375, 0.15625, 0.84375, 1.0, 0.84375);
        LENS_NORTH = VoxelShapes.func_197873_a(0.15625, 0.15625, 0.0, 0.84375, 0.84375, 0.90625);
        LENS_SOUTH = VoxelShapes.func_197873_a(0.15625, 0.15625, 0.09375, 0.84375, 0.84375, 1.0);
        LENS_EAST = VoxelShapes.func_197873_a(0.09375, 0.15625, 0.15625, 1.0, 0.84375, 0.84375);
        LENS_WEST = VoxelShapes.func_197873_a(0.0, 0.15625, 0.15625, 0.90625, 0.84375, 0.84375);
        BlockLens.PLACED_AGAINST = (EnumProperty<Direction>)EnumProperty.func_177709_a("against", (Class)Direction.class);
    }
}
