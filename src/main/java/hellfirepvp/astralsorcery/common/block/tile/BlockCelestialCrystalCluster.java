package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.pathfinding.PathType;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.state.StateContainer;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCelestialCrystals;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCelestialCrystalCluster;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import hellfirepvp.astralsorcery.common.block.tile.crystal.CollectorCrystalType;
import net.minecraft.world.level.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightRecipient;
import hellfirepvp.astralsorcery.common.block.base.BlockCrystalContainer;

public class BlockCelestialCrystalCluster extends BlockCrystalContainer implements BlockStarlightRecipient, CustomItemBlock
{
    private static final VoxelShape GROWTH_STAGE_0;
    private static final VoxelShape GROWTH_STAGE_1;
    private static final VoxelShape GROWTH_STAGE_2;
    private static final VoxelShape GROWTH_STAGE_3;
    private static final VoxelShape GROWTH_STAGE_4;
    public static IntegerProperty STAGE;
    
    public BlockCelestialCrystalCluster() {
        super(AbstractBlock.Properties.func_200949_a(Material.field_151592_s, CollectorCrystalType.CELESTIAL_CRYSTAL.getMaterialColor()).func_200948_a(3.0f, 3.0f).harvestTool(ToolType.PICKAXE).harvestLevel(1).func_200947_a(SoundType.field_185853_f).func_235838_a_(state -> 8));
    }
    
    @Override
    public Class<? extends BlockItem> getItemBlockClass() {
        return ItemBlockCelestialCrystalCluster.class;
    }
    
    @Override
    public void receiveStarlight(final World world, final Random rand, final BlockPos pos, final IWeakConstellation starlightType, final double amount) {
        final TileCelestialCrystals crystals = MiscUtils.getTileAt((IBlockReader)world, pos, TileCelestialCrystals.class, false);
        if (crystals != null) {
            crystals.grow((int)(18000.0 / amount));
        }
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        builder.func_206894_a(new Property[] { (Property)BlockCelestialCrystalCluster.STAGE });
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
        final Vec3 offset = state.func_191059_e(world, pos);
        VoxelShape shape = null;
        switch ((int)state.getValue((Property)BlockCelestialCrystalCluster.STAGE)) {
            case 4: {
                shape = BlockCelestialCrystalCluster.GROWTH_STAGE_4;
                break;
            }
            case 3: {
                shape = BlockCelestialCrystalCluster.GROWTH_STAGE_3;
                break;
            }
            case 2: {
                shape = BlockCelestialCrystalCluster.GROWTH_STAGE_2;
                break;
            }
            case 1: {
                shape = BlockCelestialCrystalCluster.GROWTH_STAGE_1;
                break;
            }
            default: {
                shape = BlockCelestialCrystalCluster.GROWTH_STAGE_0;
                break;
            }
        }
        return shape.func_197751_a(offset.field_72450_a, offset.field_72448_b, offset.field_72449_c);
    }
    
    public AbstractBlock.OffsetType func_176218_Q() {
        return AbstractBlock.OffsetType.XZ;
    }
    
    public BlockState func_196271_a(final BlockState state, final Direction placedAgainst, final BlockState facingState, final IWorld world, final BlockPos pos, final BlockPos facingPos) {
        if (!this.func_196260_a(state, (IWorldReader)world, pos)) {
            return Blocks.field_150350_a.defaultBlockState();
        }
        return state;
    }
    
    public boolean func_196260_a(final BlockState state, final IWorldReader world, final BlockPos pos) {
        return func_220064_c((IBlockReader)world, pos.func_177977_b());
    }
    
    public void func_196243_a(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            super.func_196243_a(state, world, pos, newState, isMoving);
            final PktPlayEffect effect = new PktPlayEffect(PktPlayEffect.Type.SMALL_CRYSTAL_BREAK).addData(buf -> ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos).add(state.func_191059_e((IBlockReader)world, pos)).add(0.5, 0.4, 0.5)));
            PacketChannel.CHANNEL.sendToAllAround(effect, PacketChannel.pointFromPos(world, (Vector3i)pos, 32.0));
        }
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public BlockRenderType func_149645_b(final BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader world) {
        return new TileCelestialCrystals();
    }
    
    static {
        GROWTH_STAGE_0 = Block.func_208617_a(4.0, 0.0, 5.0, 12.0, 8.0, 11.0);
        GROWTH_STAGE_1 = Block.func_208617_a(4.0, 0.0, 5.0, 12.0, 10.0, 11.0);
        GROWTH_STAGE_2 = Block.func_208617_a(2.0, 0.0, 4.0, 12.0, 12.0, 14.0);
        GROWTH_STAGE_3 = Block.func_208617_a(2.0, 0.0, 2.0, 14.0, 14.0, 14.0);
        GROWTH_STAGE_4 = Block.func_208617_a(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
        BlockCelestialCrystalCluster.STAGE = IntegerProperty.func_177719_a("stage", 0, 4);
    }
}
