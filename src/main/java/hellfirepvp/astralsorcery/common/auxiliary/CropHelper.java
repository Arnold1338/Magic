package hellfirepvp.astralsorcery.common.auxiliary;

import net.minecraft.world.level.Level;
import java.util.Iterator;
import net.minecraft.core.Direction;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.state.Property;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StemBlock;
import java.util.Random;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;
import java.util.HashMap;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.IPlantable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropsBlock;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import java.util.Map;

public class CropHelper
{
    public static final String GROWABLE = "growable";
    public static final String GROWABLE_CROP = "growable_crop";
    public static final String GROWABLE_REED = "growable_reed";
    public static final String GROWABLE_CACTUS = "growable_cactus";
    public static final String GROWABLE_NETHERWART = "growable_netherwart";
    public static final String HARVESTABLE = "harvestable";
    public static Map<String, Function<BlockPos, GrowablePlant>> growableFactoryWrapper;
    
    @Nullable
    public static GrowablePlant fromNBT(final CompoundTag nbt, final BlockPos pos) {
        return CropHelper.growableFactoryWrapper.getOrDefault((Object)nbt.getString("identifier"), p -> null).apply(pos);
    }
    
    @Nullable
    public static GrowablePlant wrapPlant(final IWorld world, final BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        final Block b = state.getBlock();
        if (b instanceof CropsBlock) {
            return new GrowableCropWrapper(pos);
        }
        if (b instanceof BonemealableBlock) {
            if (b instanceof GrassBlock) {
                return null;
            }
            if (b instanceof TallGrassBlock) {
                return null;
            }
            if (b instanceof DoublePlantBlock) {
                return null;
            }
            return new GrowableWrapper(pos);
        }
        else {
            if (b instanceof SugarCaneBlock && isReedBase(world, pos)) {
                return new GrowableReedWrapper(pos);
            }
            if (b instanceof CactusBlock && isCactusBase(world, pos)) {
                return new GrowableCactusWrapper(pos);
            }
            if (b instanceof NetherWartBlock) {
                return new GrowableNetherwartWrapper(pos);
            }
            return null;
        }
    }
    
    @Nullable
    public static HarvestablePlant wrapHarvestablePlant(final IWorld world, final BlockPos pos) {
        final GrowablePlant growable = wrapPlant(world, pos);
        if (growable == null) {
            return null;
        }
        final Block block = world.getBlockState(growable.getPos()).getBlock();
        if (growable instanceof GrowableCropWrapper) {
            return (GrowableCropWrapper)growable;
        }
        if (block instanceof SugarCaneBlock && growable instanceof GrowableReedWrapper) {
            return (GrowableReedWrapper)growable;
        }
        if (block instanceof CactusBlock && growable instanceof GrowableCactusWrapper) {
            return (GrowableCactusWrapper)growable;
        }
        if (block instanceof NetherWartBlock && growable instanceof GrowableNetherwartWrapper) {
            return (GrowableNetherwartWrapper)growable;
        }
        if (block instanceof IPlantable) {
            return new HarvestableWrapper(pos);
        }
        return null;
    }
    
    private static boolean isReedBase(final IWorld world, final BlockPos pos) {
        return !world.getBlockState(pos.func_177977_b()).getBlock().equals(Blocks.field_196608_cF);
    }
    
    private static boolean isCactusBase(final IWorld world, final BlockPos pos) {
        return !world.getBlockState(pos.func_177977_b()).getBlock().equals(Blocks.field_150434_aF);
    }
    
    static {
        CropHelper.growableFactoryWrapper = new HashMap<String, Function<BlockPos, GrowablePlant>>() {
            {
                this.put("growable", (Function<BlockPos, GrowablePlant>)GrowableWrapper::new);
                this.put("growable_crop", (Function<BlockPos, GrowablePlant>)GrowableCropWrapper::new);
                this.put("growable_reed", (Function<BlockPos, GrowablePlant>)GrowableReedWrapper::new);
                this.put("growable_cactus", (Function<BlockPos, GrowablePlant>)GrowableCactusWrapper::new);
                this.put("growable_netherwart", (Function<BlockPos, GrowablePlant>)GrowableNetherwartWrapper::new);
                this.put("harvestable", (Function<BlockPos, GrowablePlant>)HarvestableWrapper::new);
            }
        };
    }
    
    public interface GrowablePlant extends CEffectAbstractList.ListEntry
    {
        String getIdentifier();
        
        boolean isValid(final IWorld p0);
        
        boolean canGrow(final IWorld p0);
        
        boolean tryGrow(final IWorld p0, final Random p1);
        
        default void readFromNBT(final CompoundTag nbt) {
        }
        
        default void writeToNBT(final CompoundTag nbt) {
            nbt.putString("identifier", this.getIdentifier());
        }
    }
    
    public static class HarvestableWrapper implements HarvestablePlant
    {
        private final BlockPos pos;
        
        public HarvestableWrapper(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public boolean canHarvest(final IWorld world) {
            final BlockState at = world.getBlockState(this.pos);
            return at.getBlock() instanceof BonemealableBlock && !(at.getBlock() instanceof StemBlock) && !((BonemealableBlock)at.getBlock()).func_176473_a((IBlockReader)world, this.pos, at, false);
        }
        
        @Override
        public NonNullList<ItemStack> harvestDropsAndReplant(final ServerLevel world, final Random rand, final int harvestFortune) {
            final NonNullList<ItemStack> drops = (NonNullList<ItemStack>)NonNullList.func_191196_a();
            if (this.canHarvest((IWorld)world)) {
                final BlockPos pos = this.getPos();
                final BlockState at = world.getBlockState(this.getPos());
                if (at.getBlock() instanceof IPlantable) {
                    drops.addAll((Collection)BlockUtils.getDrops(world, pos, harvestFortune, rand));
                    world.func_175656_a(pos, ((IPlantable)at.getBlock()).getPlant((IBlockReader)world, pos));
                }
            }
            return drops;
        }
        
        @Override
        public String getIdentifier() {
            return "harvestable";
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
        
        @Override
        public boolean isValid(final IWorld world) {
            return CropHelper.wrapHarvestablePlant(world, this.getPos()) instanceof HarvestableWrapper;
        }
        
        @Override
        public boolean canGrow(final IWorld world) {
            final BlockState at = world.getBlockState(this.pos);
            if (at.getBlock() instanceof BonemealableBlock) {
                if (((BonemealableBlock)at.getBlock()).func_176473_a((IBlockReader)world, this.pos, at, false)) {
                    return true;
                }
                if (at.getBlock() instanceof StemBlock) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean tryGrow(final IWorld world, final Random rand) {
            if (!(world instanceof ServerLevel)) {
                return false;
            }
            final BlockState at = world.getBlockState(this.pos);
            if (at.getBlock() instanceof BonemealableBlock) {
                if (((BonemealableBlock)at.getBlock()).func_176473_a((IBlockReader)world, this.pos, at, false)) {
                    ((BonemealableBlock)at.getBlock()).func_225535_a_((ServerLevel)world, rand, this.pos, at);
                    return true;
                }
                if (at.getBlock() instanceof StemBlock && rand.nextInt(4) == 0) {
                    at.func_227034_b_((ServerLevel)world, this.pos, rand);
                }
            }
            return false;
        }
    }
    
    public static class GrowableNetherwartWrapper implements HarvestablePlant
    {
        private final BlockPos pos;
        
        public GrowableNetherwartWrapper(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public boolean isValid(final IWorld world) {
            return world.getBlockState(this.pos).getBlock() instanceof NetherWartBlock;
        }
        
        @Override
        public boolean canGrow(final IWorld world) {
            final BlockState at = world.getBlockState(this.pos);
            return at.getBlock() instanceof NetherWartBlock && (int)at.getValue((Property)NetherWartBlock.field_176486_a) < 3;
        }
        
        @Override
        public boolean tryGrow(final IWorld world, final Random rand) {
            if (rand.nextBoolean()) {
                final BlockState current = world.getBlockState(this.pos);
                return world.func_180501_a(this.pos, (BlockState)current.func_206870_a((Property)NetherWartBlock.field_176486_a, (Comparable)Math.min(3, (int)current.getValue((Property)NetherWartBlock.field_176486_a) + 1)), 3);
            }
            return false;
        }
        
        @Override
        public boolean canHarvest(final IWorld world) {
            final BlockState current = world.getBlockState(this.pos);
            return current.getBlock() instanceof NetherWartBlock && (int)current.getValue((Property)NetherWartBlock.field_176486_a) >= 3;
        }
        
        @Override
        public NonNullList<ItemStack> harvestDropsAndReplant(final ServerLevel world, final Random rand, final int harvestFortune) {
            final NonNullList<ItemStack> stacks = (NonNullList<ItemStack>)NonNullList.func_191196_a();
            stacks.addAll((Collection)BlockUtils.getDrops(world, this.pos, harvestFortune, rand));
            world.func_180501_a(this.pos, (BlockState)Blocks.field_150388_bm.defaultBlockState().func_206870_a((Property)NetherWartBlock.field_176486_a, (Comparable)0), 3);
            return stacks;
        }
        
        @Override
        public String getIdentifier() {
            return "growable_netherwart";
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
    }
    
    public static class GrowableCactusWrapper implements HarvestablePlant
    {
        private final BlockPos pos;
        
        public GrowableCactusWrapper(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public boolean canHarvest(final IWorld world) {
            return world.getBlockState(this.pos.above()).getBlock() instanceof CactusBlock;
        }
        
        @Override
        public boolean isValid(final IWorld world) {
            return world.getBlockState(this.pos).getBlock() instanceof CactusBlock;
        }
        
        @Override
        public NonNullList<ItemStack> harvestDropsAndReplant(final ServerLevel world, final Random rand, final int harvestFortune) {
            final NonNullList<ItemStack> drops = (NonNullList<ItemStack>)NonNullList.func_191196_a();
            for (int i = 2; i > 0; --i) {
                final BlockPos bp = this.pos.func_177981_b(i);
                final BlockState at = world.getBlockState(bp);
                if (at.getBlock() instanceof CactusBlock) {
                    drops.addAll((Collection)BlockUtils.getDrops(world, this.pos, harvestFortune, rand));
                    world.func_217377_a(bp, false);
                }
            }
            return drops;
        }
        
        @Override
        public boolean canGrow(final IWorld world) {
            BlockPos cache = this.pos;
            for (int i = 1; i < 3; ++i) {
                cache = cache.above();
                final BlockState upState = world.getBlockState(cache);
                if (upState.isAir((IBlockReader)world, cache)) {
                    return true;
                }
                if (!(upState.getBlock() instanceof CactusBlock)) {
                    return false;
                }
            }
            return false;
        }
        
        @Override
        public boolean tryGrow(final IWorld world, final Random rand) {
            BlockPos cache = this.pos;
            for (int i = 1; i < 3; ++i) {
                cache = cache.above();
                final BlockState upState = world.getBlockState(cache);
                if (upState.isAir((IBlockReader)world, cache)) {
                    return rand.nextBoolean() && world.func_180501_a(cache, Blocks.field_150434_aF.defaultBlockState(), 3);
                }
                if (!(upState.getBlock() instanceof CactusBlock)) {
                    return false;
                }
            }
            return false;
        }
        
        @Override
        public String getIdentifier() {
            return "growable_cactus";
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
    }
    
    public static class GrowableReedWrapper implements HarvestablePlant
    {
        private final BlockPos pos;
        
        public GrowableReedWrapper(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public boolean canHarvest(final IWorld world) {
            return world.getBlockState(this.pos.above()).getBlock() instanceof SugarCaneBlock;
        }
        
        @Override
        public NonNullList<ItemStack> harvestDropsAndReplant(final ServerLevel world, final Random rand, final int harvestFortune) {
            final NonNullList<ItemStack> drops = (NonNullList<ItemStack>)NonNullList.func_191196_a();
            for (int i = 2; i > 0; --i) {
                final BlockPos bp = this.pos.func_177981_b(i);
                final BlockState at = world.getBlockState(bp);
                if (at.getBlock() instanceof SugarCaneBlock) {
                    drops.addAll((Collection)BlockUtils.getDrops(world, this.pos, harvestFortune, rand));
                    world.func_217377_a(bp, false);
                }
            }
            return drops;
        }
        
        @Override
        public boolean isValid(final IWorld world) {
            return world.getBlockState(this.pos).getBlock() instanceof SugarCaneBlock;
        }
        
        @Override
        public boolean canGrow(final IWorld world) {
            BlockPos cache = this.pos;
            for (int i = 1; i < 3; ++i) {
                cache = cache.above();
                final BlockState upState = world.getBlockState(cache);
                if (upState.isAir((IBlockReader)world, cache)) {
                    return true;
                }
                if (!(upState.getBlock() instanceof SugarCaneBlock)) {
                    return false;
                }
            }
            return false;
        }
        
        @Override
        public boolean tryGrow(final IWorld world, final Random rand) {
            BlockPos cache = this.pos;
            for (int i = 1; i < 3; ++i) {
                cache = cache.above();
                final BlockState upState = world.getBlockState(cache);
                if (upState.isAir((IBlockReader)world, cache)) {
                    return rand.nextBoolean() && world.func_180501_a(cache, Blocks.field_196608_cF.defaultBlockState(), 3);
                }
                if (!(upState.getBlock() instanceof SugarCaneBlock)) {
                    return false;
                }
            }
            return false;
        }
        
        @Override
        public String getIdentifier() {
            return "growable_reed";
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
    }
    
    public static class GrowableCropWrapper implements HarvestablePlant
    {
        private final BlockPos pos;
        
        public GrowableCropWrapper(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public boolean isValid(final IWorld world) {
            return CropHelper.wrapPlant(world, this.pos) instanceof GrowableCropWrapper;
        }
        
        @Override
        public boolean canGrow(final IWorld world) {
            final BlockState state = world.getBlockState(this.pos);
            return state.getBlock() instanceof CropsBlock && ((CropsBlock)state.getBlock()).func_176473_a((IBlockReader)world, this.pos, state, false);
        }
        
        @Override
        public boolean tryGrow(final IWorld world, final Random rand) {
            final BlockState state = world.getBlockState(this.pos);
            if (state.getBlock() instanceof CropsBlock) {
                final CropsBlock block = (CropsBlock)state.getBlock();
                if (block.func_176473_a((IBlockReader)world, this.pos, state, false)) {
                    final int age = (int)state.getValue((Property)block.func_185524_e());
                    final int next = Math.min(age + 1, block.func_185526_g());
                    return world.func_180501_a(this.pos, block.func_185528_e(next), 3);
                }
            }
            return false;
        }
        
        @Override
        public boolean canHarvest(final IWorld world) {
            final BlockState state = world.getBlockState(this.pos);
            return state.getBlock() instanceof CropsBlock && !((CropsBlock)state.getBlock()).func_176473_a((IBlockReader)world, this.pos, state, false);
        }
        
        @Override
        public NonNullList<ItemStack> harvestDropsAndReplant(final ServerLevel world, final Random rand, final int harvestFortune) {
            final NonNullList<ItemStack> drops = (NonNullList<ItemStack>)NonNullList.func_191196_a();
            final BlockState state = world.getBlockState(this.pos);
            if (state.getBlock() instanceof CropsBlock) {
                final CropsBlock block = (CropsBlock)state.getBlock();
                drops.addAll((Collection)BlockUtils.getDrops(world, this.pos, harvestFortune, rand));
                final int startingAge = MiscUtils.getMinEntry((Collection<Integer>)block.func_185524_e().func_177700_c());
                world.func_175656_a(this.pos, block.func_185528_e(startingAge));
            }
            return drops;
        }
        
        @Override
        public String getIdentifier() {
            return "growable_crop";
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
    }
    
    public static class GrowableWrapper implements GrowablePlant
    {
        private final BlockPos pos;
        
        public GrowableWrapper(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public String getIdentifier() {
            return "growable";
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
        
        @Override
        public boolean isValid(final IWorld world) {
            return CropHelper.wrapPlant(world, this.pos) instanceof GrowableWrapper;
        }
        
        @Override
        public boolean canGrow(final IWorld world) {
            final BlockState at = world.getBlockState(this.pos);
            return at.getBlock() instanceof BonemealableBlock && (((BonemealableBlock)at.getBlock()).func_176473_a((IBlockReader)world, this.pos, at, false) || (at.getBlock() instanceof StemBlock && !this.stemHasCrop(world, (Block)((StemBlock)at.getBlock()).func_208486_d())));
        }
        
        private boolean stemHasCrop(final IWorld world, final Block stemGrownBlock) {
            for (final Direction enumfacing : Direction.Plane.HORIZONTAL) {
                final Block offset = world.getBlockState(this.pos.func_177972_a(enumfacing)).getBlock();
                if (offset.equals(stemGrownBlock)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean tryGrow(final IWorld world, final Random rand) {
            final BlockState at = world.getBlockState(this.pos);
            if (at.getBlock() instanceof BonemealableBlock && world instanceof ServerLevel) {
                if (((BonemealableBlock)at.getBlock()).func_176473_a((IBlockReader)world, this.pos, at, false)) {
                    if (!((BonemealableBlock)at.getBlock()).func_180670_a((World)world, rand, this.pos, at) && rand.nextInt(20) != 0) {
                        return true;
                    }
                    ((BonemealableBlock)at.getBlock()).func_225535_a_((ServerLevel)world, rand, this.pos, at);
                    return true;
                }
                else if (at.getBlock() instanceof StemBlock) {
                    at.func_227034_b_((ServerLevel)world, this.pos, rand);
                    return true;
                }
            }
            return false;
        }
    }
    
    public interface HarvestablePlant extends GrowablePlant
    {
        boolean canHarvest(final IWorld p0);
        
        NonNullList<ItemStack> harvestDropsAndReplant(final ServerLevel p0, final Random p1, final int p2);
    }
}
