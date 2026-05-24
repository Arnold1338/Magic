package hellfirepvp.astralsorcery.common.block.tile;

import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.util.Locale;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import java.awt.Color;
import net.minecraft.util.IStringSerializable;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.tile.TileGemCrystals;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.pathfinding.PathType;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.state.StateContainer;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockGemCrystalCluster;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import hellfirepvp.astralsorcery.common.block.tile.crystal.CollectorCrystalType;
import net.minecraft.world.level.material.Material;
import net.minecraft.state.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.block.ContainerBlock;

public class BlockGemCrystalCluster extends ContainerBlock implements CustomItemBlock
{
    private static final VoxelShape STAGE_0;
    private static final VoxelShape STAGE_1;
    private static final VoxelShape STAGE_2_SKY;
    private static final VoxelShape STAGE_2_DAY;
    private static final VoxelShape STAGE_2_NIGHT;
    public static final EnumProperty<GrowthStageType> STAGE;
    
    public BlockGemCrystalCluster() {
        super(AbstractBlock.Properties.func_200949_a(Material.field_151592_s, CollectorCrystalType.ROCK_CRYSTAL.getMaterialColor()).func_200948_a(3.0f, 3.0f).harvestTool(ToolType.PICKAXE).harvestLevel(1).func_200947_a(SoundType.field_185853_f).func_235838_a_(state -> 6));
    }
    
    public Class<? extends BlockItem> getItemBlockClass() {
        return ItemBlockGemCrystalCluster.class;
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        builder.func_206894_a(new Property[] { (Property)BlockGemCrystalCluster.STAGE });
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
        final Vec3 offset = state.func_191059_e(world, pos);
        VoxelShape shape = VoxelShapes.func_197868_b();
        switch ((GrowthStageType)state.getValue((Property)BlockGemCrystalCluster.STAGE)) {
            case STAGE_0: {
                shape = BlockGemCrystalCluster.STAGE_0;
                break;
            }
            case STAGE_1: {
                shape = BlockGemCrystalCluster.STAGE_1;
                break;
            }
            case STAGE_2_SKY: {
                shape = BlockGemCrystalCluster.STAGE_2_SKY;
                break;
            }
            case STAGE_2_DAY: {
                shape = BlockGemCrystalCluster.STAGE_2_DAY;
                break;
            }
            case STAGE_2_NIGHT: {
                shape = BlockGemCrystalCluster.STAGE_2_NIGHT;
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
            final PktPlayEffect effect = new PktPlayEffect(PktPlayEffect.Type.GEM_CRYSTAL_BREAK).addData(buf -> {
                ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos).add(state.func_191059_e((IBlockReader)world, pos)));
                buf.writeInt(((GrowthStageType)state.getValue((Property)BlockGemCrystalCluster.STAGE)).ordinal());
                return;
            });
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
        return new TileGemCrystals();
    }
    
    static {
        STAGE_0 = Block.func_208617_a(4.0, 0.0, 4.0, 12.0, 6.0, 12.0);
        STAGE_1 = Block.func_208617_a(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
        STAGE_2_SKY = Block.func_208617_a(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);
        STAGE_2_DAY = Block.func_208617_a(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
        STAGE_2_NIGHT = Block.func_208617_a(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);
        STAGE = EnumProperty.func_177709_a("stage", (Class)GrowthStageType.class);
    }
    
    public enum GrowthStageType implements IStringSerializable
    {
        STAGE_0(0, Color.WHITE), 
        STAGE_1(1, Color.WHITE), 
        STAGE_2_SKY(2, ColorsAS.GEM_SKY), 
        STAGE_2_DAY(2, ColorsAS.GEM_DAY), 
        STAGE_2_NIGHT(2, ColorsAS.GEM_NIGHT);
        
        private final int growthStage;
        private final Color displayColor;
        
        private GrowthStageType(final int growthStage, final Color displayColor) {
            this.growthStage = growthStage;
            this.displayColor = displayColor;
        }
        
        public Color getDisplayColor() {
            return this.displayColor;
        }
        
        public int getGrowthStage() {
            return this.growthStage;
        }
        
        public GrowthStageType grow(final World world) {
            if (this == GrowthStageType.STAGE_0) {
                return GrowthStageType.STAGE_1;
            }
            if (this != GrowthStageType.STAGE_1) {
                return this;
            }
            if (DayTimeHelper.isDay(world)) {
                return GrowthStageType.STAGE_2_DAY;
            }
            if (DayTimeHelper.isNight(world)) {
                return GrowthStageType.STAGE_2_NIGHT;
            }
            return GrowthStageType.STAGE_2_SKY;
        }
        
        public GrowthStageType shrink() {
            final int stage = this.getGrowthStage();
            if (stage == 2) {
                return GrowthStageType.STAGE_1;
            }
            return GrowthStageType.STAGE_0;
        }
        
        public String func_176610_l() {
            return this.name().toLowerCase(Locale.ROOT);
        }
        
        @Override
        public String toString() {
            return this.func_176610_l();
        }
    }
}
