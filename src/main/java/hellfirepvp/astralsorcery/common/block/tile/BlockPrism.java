package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.IBlockDisplayReader;
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
import hellfirepvp.astralsorcery.common.tile.TilePrism;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockPrism;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesGlass;
import net.minecraft.world.level.block.state.BooleanProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.EnumProperty;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.BlockDynamicColor;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public class BlockPrism extends BlockStarlightNetwork implements CustomItemBlock, BlockDynamicColor
{
    private static final VoxelShape PRISM_DOWN;
    private static final VoxelShape PRISM_UP;
    private static final VoxelShape PRISM_NORTH;
    private static final VoxelShape PRISM_SOUTH;
    private static final VoxelShape PRISM_EAST;
    private static final VoxelShape PRISM_WEST;
    public static EnumProperty<Direction> PLACED_AGAINST;
    public static BooleanProperty HAS_COLORED_LENS;
    
    public BlockPrism() {
        super(PropertiesGlass.coatedGlass().harvestTool(ToolType.PICKAXE));
        this.func_180632_j((BlockState)((BlockState)((BlockState)this.func_176194_O().func_177621_b()).setValue((Property)BlockPrism.PLACED_AGAINST, (Comparable)Direction.DOWN)).setValue((Property)BlockPrism.HAS_COLORED_LENS, (Comparable)false));
    }
    
    @Override
    public Class<? extends BlockItem> getItemBlockClass() {
        return ItemBlockPrism.class;
    }
    
    public void func_176208_a(final Level world, final BlockPos pos, final BlockState state, final Player player) {
        final TilePrism lens = MiscUtils.getTileAt((IBlockReader)world, pos, TilePrism.class, true);
        if (lens != null && !world.level().isClientSide() && !player.getVehicle() && lens.getColorType() != null) {
            final ItemStack drop = lens.getColorType().getStack();
            ItemUtils.dropItemNaturally(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
        }
        super.func_176208_a(world, pos, state, player);
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        if (!world.level().isClientSide() && player.isCrouching()) {
            final TilePrism lens = MiscUtils.getTileAt((IBlockReader)world, pos, TilePrism.class, true);
            if (lens != null && lens.getColorType() != null) {
                final ItemStack drop = lens.getColorType().getStack();
                if (!player.getVehicle()) {
                    if (player.getItemInHand(hand).isEmpty()) {
                        player.func_184611_a(hand, drop);
                    }
                    else if (!player.getInventory().func_70441_a(drop)) {
                        ItemUtils.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
                    }
                }
                SoundHelper.playSoundAround(SoundsAS.BLOCK_COLOREDLENS_ATTACH, world, (Vector3i)pos, 0.8f, 1.5f);
                lens.setColorType(null);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        builder.func_206894_a(new Property[] { (Property)BlockPrism.PLACED_AGAINST, (Property)BlockPrism.HAS_COLORED_LENS });
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext context) {
        return (BlockState)this.defaultBlockState().setValue((Property)BlockPrism.PLACED_AGAINST, (Comparable)context.func_196000_l().func_176734_d());
    }
    
    @Override
    public void func_196243_a(final BlockState state, final Level worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            super.func_196243_a(state, worldIn, pos, newState, isMoving);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(final BlockState state, @Nullable final IBlockDisplayReader world, @Nullable final BlockPos pos, final int tintIndex) {
        if (tintIndex != 3) {
            return -1;
        }
        final TilePrism prism = MiscUtils.getTileAt((IBlockReader)world, pos, TilePrism.class, false);
        if (prism != null) {
            final LensColorType type = prism.getColorType();
            if (type != null) {
                return type.getColor().getRGB();
            }
        }
        return -1;
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        switch ((Direction)state.getValue((Property)BlockPrism.PLACED_AGAINST)) {
            case UP: {
                return BlockPrism.PRISM_UP;
            }
            case NORTH: {
                return BlockPrism.PRISM_NORTH;
            }
            case SOUTH: {
                return BlockPrism.PRISM_SOUTH;
            }
            case WEST: {
                return BlockPrism.PRISM_WEST;
            }
            case EAST: {
                return BlockPrism.PRISM_EAST;
            }
            default: {
                return BlockPrism.PRISM_DOWN;
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
        return new TilePrism();
    }
    
    static {
        PRISM_DOWN = VoxelShapes.func_197873_a(0.1875, 0.0, 0.1875, 0.8125, 0.875, 0.8125);
        PRISM_UP = VoxelShapes.func_197873_a(0.1875, 0.125, 0.1875, 0.8125, 1.0, 0.8125);
        PRISM_NORTH = VoxelShapes.func_197873_a(0.1875, 0.1875, 0.0, 0.8125, 0.8125, 0.875);
        PRISM_SOUTH = VoxelShapes.func_197873_a(0.1875, 0.1875, 0.125, 0.8125, 0.8125, 1.0);
        PRISM_EAST = VoxelShapes.func_197873_a(0.125, 0.1875, 0.1875, 1.0, 0.8125, 0.8125);
        PRISM_WEST = VoxelShapes.func_197873_a(0.0, 0.1875, 0.1875, 0.875, 0.8125, 0.8125);
        BlockPrism.PLACED_AGAINST = (EnumProperty<Direction>)EnumProperty.func_177709_a("against", (Class)Direction.class);
        BlockPrism.HAS_COLORED_LENS = BooleanProperty.func_177716_a("has_lens");
    }
}
