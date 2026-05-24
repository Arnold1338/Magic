package hellfirepvp.astralsorcery.common.util.block;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraft.world.level.level.block.Blocks;
import hellfirepvp.astralsorcery.common.util.BlockDropCaptureAssist;
import net.minecraft.world.level.level.LevelAccessor;
import net.minecraft.world.level.level.material.Fluids;
import net.minecraftforge.common.util.BlockSnapshot;
import java.util.ArrayList;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.block.state.Property;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Collection;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.effect.EffectUtils;
import net.minecraft.world.level.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.level.storage.loot.LootContext;
import net.minecraft.world.level.level.block.state.BlockState;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.ItemStack;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class BlockUtils
{
    @Nonnull
    public static List<ItemStack> getDrops(final ServerLevel world, final BlockPos pos, final int harvestFortune, final Random rand) {
        return getDrops(world, pos, harvestFortune, rand, ItemStack.field_190927_a);
    }
    
    @Nonnull
    public static List<ItemStack> getDrops(final ServerLevel world, final BlockPos pos, final int harvestFortune, final Random rand, final ItemStack tool) {
        return getDrops(world, pos, world.getBlockState(pos), harvestFortune, rand, tool);
    }
    
    @Nonnull
    public static List<ItemStack> getDrops(final ServerLevel world, final BlockPos pos, final BlockState state, final int harvestFortune, final Random rand, final ItemStack tool) {
        final LootContext.Builder builder = new LootContext.Builder(world).func_216015_a(LootParameters.field_237457_g_, (Object)Vec3.func_237489_a_((Vector3i)pos)).func_216015_a(LootParameters.field_216287_g, (Object)state).func_216015_a(LootContextParams.TOOL, (Object)tool).func_216021_b(LootParameters.field_216288_h, (Object)MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, true)).func_216023_a(rand).func_186469_a((float)harvestFortune);
        return state.func_215693_a(builder);
    }
    
    @Nonnull
    public static BlockPos getWorldTopPos(final BlockPos at) {
        BlockPos it;
        for (it = at; !World.func_189509_E(it); it = it.above()) {}
        return it;
    }
    
    public static BlockPos firstSolidDown(final IBlockReader world, BlockPos at) {
        for (BlockState state = world.getBlockState(at); at.getY() > 0 && !state.func_185904_a().func_76230_c() && state.getFluidState().func_206888_e(); at = at.func_177977_b(), state = world.getBlockState(at)) {}
        return at;
    }
    
    public static boolean isReplaceable(final World world, final BlockPos pos) {
        return isReplaceable(world, pos, world.getBlockState(pos));
    }
    
    public static boolean isReplaceable(final World world, final BlockPos pos, final BlockState state) {
        if (world.isEmptyBlock(pos)) {
            return true;
        }
        final BlockItemUseContext ctx = TestBlockUseContext.getHandContext(world, null, InteractionHand.MAIN_HAND, pos, Direction.UP);
        return state.func_196953_a(ctx);
    }
    
    public static float getSimpleBreakSpeed(final LivingEntity entity, final ItemStack tool, final BlockState state) {
        float breakSpeed = tool.func_150997_a(state);
        if (breakSpeed > 1.0f) {
            final float efficiencyLevel = (float)EnchantmentHelper.func_185293_e(entity);
            if (efficiencyLevel > 0.0f && !tool.isEmpty()) {
                breakSpeed += efficiencyLevel * efficiencyLevel + 1.0f;
            }
        }
        if (EffectUtils.func_205135_a(entity)) {
            breakSpeed *= 1.0f + (EffectUtils.func_205134_b(entity) + 1.0f) * 0.2f;
        }
        if (entity.func_70644_a(Effects.field_76419_f)) {
            float fatigueMultiplier = 0.0f;
            switch (entity.func_70660_b(Effects.field_76419_f).func_76458_c()) {
                case 0: {
                    fatigueMultiplier = (float)Math.pow(0.30000001192092896, 1.0);
                    break;
                }
                case 1: {
                    fatigueMultiplier = (float)Math.pow(0.30000001192092896, 2.0);
                    break;
                }
                case 2: {
                    fatigueMultiplier = (float)Math.pow(0.30000001192092896, 3.0);
                    break;
                }
                default: {
                    fatigueMultiplier = (float)Math.pow(0.30000001192092896, 4.0);
                    break;
                }
            }
            breakSpeed *= fatigueMultiplier;
        }
        if (entity.func_208600_a((ITag)FluidTags.field_206959_a) && !EnchantmentHelper.func_185287_i(entity)) {
            breakSpeed /= 5.0f;
        }
        if (!entity.func_233570_aj_()) {
            breakSpeed /= 5.0f;
        }
        return breakSpeed;
    }
    
    public static boolean isFluidBlock(final World world, final BlockPos pos) {
        return isFluidBlock(world.getBlockState(pos));
    }
    
    public static boolean isFluidBlock(final BlockState state) {
        return state == state.getFluidState().func_206883_i();
    }
    
    @Nullable
    public static BlockState getMatchingState(final Collection<BlockState> applicableStates, @Nullable final BlockState test) {
        for (final BlockState state : applicableStates) {
            if (matchStateExact(state, test)) {
                return state;
            }
        }
        return null;
    }
    
    public static boolean matchStateExact(@Nullable final BlockState state, @Nullable final BlockState stateToTest) {
        if (state == null) {
            return stateToTest == null;
        }
        if (stateToTest == null) {
            return false;
        }
        if (!state.getBlock().getRegistryName().equals((Object)stateToTest.getBlock().getRegistryName())) {
            return false;
        }
        for (final Property<?> prop : state.func_235904_r_()) {
            final Comparable<?> original = state.getValue((Property)prop);
            try {
                final Comparable<?> test = stateToTest.getValue((Property)prop);
                if (!original.equals(test)) {
                    return false;
                }
                continue;
            }
            catch (final Exception exc) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean canToolBreakBlockWithoutPlayer(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final BlockState state, @Nonnull final ItemStack stack) {
        if (state.func_185887_b((IBlockReader)world, pos) == -1.0f) {
            return false;
        }
        if (!state.func_235783_q_()) {
            return true;
        }
        final ToolType tool = state.getHarvestTool();
        if (stack.isEmpty() || tool == null) {
            return !state.func_235783_q_() || stack.func_150998_b(state);
        }
        final int toolLevel = stack.getItem().getHarvestLevel(stack, tool, (Player)null, state);
        if (toolLevel < 0) {
            return !state.func_235783_q_() || stack.func_150998_b(state);
        }
        return toolLevel >= state.getHarvestLevel();
    }
    
    public static boolean breakBlockWithPlayer(final BlockPos pos, final ServerPlayer playerMP) {
        return playerMP.field_71134_c.func_180237_b(pos);
    }
    
    public static boolean breakBlockWithoutPlayer(final ServerLevel world, final BlockPos pos) {
        return breakBlockWithoutPlayer(world, pos, world.getBlockState(pos), ItemStack.field_190927_a, true, false);
    }
    
    @Deprecated
    public static boolean breakBlockWithoutPlayer(final ServerLevel world, final BlockPos pos, final BlockState stateBroken, final ItemStack heldItem, final boolean breakBlock, final boolean ignoreHarvestRestrictions, final boolean playEffects) {
        return breakBlockWithoutPlayer(world, pos, stateBroken, heldItem, breakBlock, ignoreHarvestRestrictions);
    }
    
    public static boolean breakBlockWithoutPlayer(final ServerLevel world, final BlockPos pos, final BlockState stateBroken, final ItemStack heldItem, final boolean breakBlock, final boolean ignoreHarvestRestrictions) {
        final FakePlayer fakePlayer = AstralSorcery.getProxy().getASFakePlayerServer(world);
        int xp;
        try {
            boolean preCancelEvent = false;
            if (!heldItem.isEmpty() && !heldItem.getItem().func_195938_a(stateBroken, (World)world, pos, (Player)fakePlayer)) {
                preCancelEvent = true;
            }
            final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent((World)world, pos, stateBroken, (Player)fakePlayer);
            event.setCanceled(preCancelEvent);
            MinecraftForge.EVENT_BUS.post((Event)event);
            if (event.isCanceled()) {
                return false;
            }
            xp = event.getExpToDrop();
        }
        catch (final Exception exc) {
            return false;
        }
        if (xp == -1) {
            return false;
        }
        if (heldItem.onBlockStartBreak(pos, (Player)fakePlayer)) {
            return false;
        }
        boolean harvestable = true;
        try {
            if (!ignoreHarvestRestrictions) {
                harvestable = stateBroken.canHarvestBlock((IBlockReader)world, pos, (Player)fakePlayer);
            }
        }
        catch (final Exception exc2) {
            return false;
        }
        final ItemStack heldCopy = heldItem.isEmpty() ? ItemStack.field_190927_a : heldItem.copy();
        try {
            heldCopy.func_179548_a((World)world, stateBroken, pos, (Player)fakePlayer);
        }
        catch (final Exception exc3) {
            return false;
        }
        final boolean wasCapturingStates = world.captureBlockSnapshots;
        final List<BlockSnapshot> previousCapturedStates = new ArrayList<BlockSnapshot>(world.capturedBlockSnapshots);
        world.captureBlockSnapshots = true;
        try {
            if (breakBlock) {
                if (!stateBroken.removedByPlayer((World)world, pos, (Player)fakePlayer, harvestable, Fluids.field_204541_a.func_207188_f())) {
                    restoreWorldState((World)world, wasCapturingStates, previousCapturedStates);
                    return false;
                }
            }
            else {
                stateBroken.getBlock().func_176208_a((World)world, pos, stateBroken, (Player)fakePlayer);
            }
        }
        catch (final Exception exc4) {
            restoreWorldState((World)world, wasCapturingStates, previousCapturedStates);
            return false;
        }
        stateBroken.getBlock().func_176206_d((IWorld)world, pos, stateBroken);
        if (harvestable) {
            try {
                final BlockEntity tileentity = MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, true);
                final ItemStack harvestStack = heldCopy.isEmpty() ? ItemStack.field_190927_a : heldCopy.copy();
                stateBroken.getBlock().func_180657_a((World)world, (Player)fakePlayer, pos, stateBroken, tileentity, harvestStack);
            }
            catch (final Exception exc4) {
                restoreWorldState((World)world, wasCapturingStates, previousCapturedStates);
                return false;
            }
        }
        if (xp > 0) {
            stateBroken.getBlock().func_180637_b(world, pos, xp);
        }
        BlockDropCaptureAssist.startCapturing();
        try {
            world.captureBlockSnapshots = false;
            world.restoringBlockSnapshots = true;
            world.capturedBlockSnapshots.forEach(s -> s.restore(true));
            world.restoringBlockSnapshots = false;
            world.capturedBlockSnapshots.forEach(s -> world.func_175656_a(s.getPos(), Blocks.field_150350_a.defaultBlockState()));
        }
        finally {
            BlockDropCaptureAssist.getCapturedStacksAndStop();
            world.capturedBlockSnapshots.clear();
            world.captureBlockSnapshots = wasCapturingStates;
            world.capturedBlockSnapshots.addAll(previousCapturedStates);
        }
        return true;
    }
    
    private static void restoreWorldState(final World world, final boolean prevCaptureFlag, final List<BlockSnapshot> prevSnapshots) {
        world.captureBlockSnapshots = false;
        world.restoringBlockSnapshots = true;
        world.capturedBlockSnapshots.forEach(s -> s.restore(true));
        world.restoringBlockSnapshots = false;
        world.capturedBlockSnapshots.clear();
        world.captureBlockSnapshots = prevCaptureFlag;
        world.capturedBlockSnapshots.addAll(prevSnapshots);
    }
}
