package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.Blocks;
import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.IWorldWriter;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.EnumProperty;
import net.minecraft.world.level.block.Block;

public class BlockStructural extends Block
{
    public static EnumProperty<BlockType> BLOCK_TYPE;
    private static final VoxelShape STRUCT_TELESCOPE;
    
    public BlockStructural() {
        super(AbstractBlock.Properties.func_200949_a(Material.field_175972_I, MaterialColor.field_151660_b).func_200947_a(SoundType.field_185853_f));
        this.func_180632_j((BlockState)this.defaultBlockState().func_206870_a((Property)BlockStructural.BLOCK_TYPE, (Comparable)BlockType.TELESCOPE));
    }
    
    public void func_149666_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        builder.func_206894_a(new Property[] { (Property)BlockStructural.BLOCK_TYPE });
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
            case TELESCOPE: {
                return BlockStructural.STRUCT_TELESCOPE;
            }
            default: {
                return super.func_220053_a(state, worldIn, pos, context);
            }
        }
    }
    
    @Nullable
    public ToolType getHarvestTool(final BlockState state) {
        return ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)).getSupportedState().getHarvestTool();
    }
    
    public int getHarvestLevel(final BlockState state) {
        return ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)).getSupportedState().getHarvestLevel();
    }
    
    public SoundType getSoundType(final BlockState state, final IWorldReader world, final BlockPos pos, @Nullable final Entity entity) {
        switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
            case TELESCOPE: {
                return SoundType.field_185848_a;
            }
            default: {
                return super.getSoundType(state, world, pos, entity);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(final BlockState state, final World world, final BlockPos pos, final ParticleEngine manager) {
        EventFlags.PLAY_BLOCK_BREAK_EFFECTS.executeWithFlag(() -> {
            switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
                case TELESCOPE: {
                    manager.func_180533_a(pos.func_177977_b(), BlocksAS.TELESCOPE.defaultBlockState());
                    break;
                }
            }
            return;
        });
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(final BlockState state, final World world, final HitResult target, final ParticleEngine manager) {
        if (target instanceof BlockHitResult) {
            EventFlags.PLAY_BLOCK_BREAK_EFFECTS.executeWithFlag(() -> {
                switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
                    case TELESCOPE: {
                        manager.func_180533_a(((BlockHitResult)target).func_216350_a().func_177977_b(), BlocksAS.TELESCOPE.defaultBlockState());
                        break;
                    }
                }
                return;
            });
        }
        return true;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final World world, final BlockPos pos, final Player entity, final Hand hand, final BlockHitResult rayTraceResult) {
        switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
            case TELESCOPE: {
                if (world.func_201670_d()) {
                    AstralSorcery.getProxy().openGui(entity, GuiType.TELESCOPE, pos.func_177977_b());
                }
                return InteractionResult.SUCCESS;
            }
            default: {
                return super.func_225533_a_(state, world, pos, entity, hand, rayTraceResult);
            }
        }
    }
    
    public List<ItemStack> func_220076_a(final BlockState state, final LootContext.Builder builder) {
        final List<ItemStack> drops = Lists.newArrayList();
        switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
            case TELESCOPE: {
                return BlockType.TELESCOPE.getSupportedState().func_215693_a(builder);
            }
            default: {
                return drops;
            }
        }
    }
    
    public ItemStack getPickBlock(final BlockState state, final HitResult target, final IBlockReader world, final BlockPos pos, final Player player) {
        switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
            case TELESCOPE: {
                return BlockType.TELESCOPE.getSupportedState().getPickBlock(target, world, pos.func_177977_b(), player);
            }
            default: {
                return super.getPickBlock(state, target, world, pos, player);
            }
        }
    }
    
    public void func_220069_a(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos, final boolean isMoving) {
        switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
            case TELESCOPE: {
                if (world.isEmptyBlock(pos.func_177977_b())) {
                    world.func_217377_a(pos, isMoving);
                }
                return;
            }
            default: {
                super.func_220069_a(state, world, pos, block, fromPos, isMoving);
            }
        }
    }
    
    public void onNeighborChange(final BlockState state, final IWorldReader world, final BlockPos pos, final BlockPos neighbor) {
        if (!(world instanceof IWorldWriter)) {
            return;
        }
        switch ((BlockType)state.getValue((Property)BlockStructural.BLOCK_TYPE)) {
            case TELESCOPE: {
                if (world.isEmptyBlock(pos.func_177977_b())) {
                    ((IWorldWriter)world).func_217377_a(pos, false);
                    break;
                }
                break;
            }
        }
    }
    
    public RenderShape func_149645_b(final BlockState p_149645_1_) {
        return RenderShape.INVISIBLE;
    }
    
    static {
        BlockStructural.BLOCK_TYPE = (EnumProperty<BlockType>)EnumProperty.func_177709_a("blocktype", (Class)BlockType.class);
        STRUCT_TELESCOPE = VoxelShapes.func_197873_a(0.0625, -1.0, 0.0625, 0.9375, 1.0, 0.9375);
    }
    
    public enum BlockType implements StringRepresentable
    {
        DUMMY(Blocks.field_150350_a.defaultBlockState()), 
        TELESCOPE(BlocksAS.TELESCOPE.defaultBlockState());
        
        private final BlockState supportedState;
        
        private BlockType(final BlockState supportedState) {
            this.supportedState = supportedState;
        }
        
        public BlockState getSupportedState() {
            return this.supportedState;
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
