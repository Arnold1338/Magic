package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.world.level.BlockGetter;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.BlockDropCaptureAssist;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockSpherePositionGenerator;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockPositionGenerator;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ListEntries;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;

public class CEffectEvorsio extends CEffectAbstractList<ListEntries.PosEntry>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static EvorsioConfig CONFIG;
    
    public CEffectEvorsio(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.evorsio, 1, (world, pos, state) -> true);
        this.excludeRitualPositions();
    }
    
    @Nonnull
    @Override
    protected BlockPositionGenerator createPositionStrategy() {
        return new BlockSpherePositionGenerator();
    }
    
    @Nullable
    @Override
    public ListEntries.PosEntry recreateElement(final CompoundTag tag, final BlockPos pos) {
        return new ListEntries.PosEntry(pos);
    }
    
    @Nullable
    @Override
    public ListEntries.PosEntry createElement(final Level world, final BlockPos pos) {
        return new ListEntries.PosEntry(pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final Level world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        float addY = 1.0f;
        if (!pedestal.getBlockState().equals((Object)pos)) {
            addY = 0.0f;
        }
        final Vector3 motion = Vector3.random().multiply(0.1);
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vec3i)pos).add(0.5, 0.5, 0.5).addY(addY)).alpha(VFXAlphaFunction.FADE_OUT).setMotion(motion).color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_EVORSIO)).setScaleMultiplier(0.3f + CEffectEvorsio.rand.nextFloat() * 0.4f).setMaxAge(50);
    }
    
    @Override
    public boolean playEffect(final Level world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        return world instanceof ServerLevel && this.peekNewPosition(world, pos, properties).mapLeft(newEntry -> {
            final BlockPos at = newEntry.getPos();
            if (properties.isCorrupted()) {
                if (at.getY() < pos.getY() && world.isEmptyBlock(at)) {
                    final double distance = pos.func_177951_i((Vec3i)at) / (properties.getSize() * properties.getSize());
                    BlockState state = Blocks.field_150347_e.defaultBlockState();
                    if (distance >= 0.8500000238418579 && CEffectEvorsio.rand.nextInt(4) == 0) {
                        state = Blocks.field_150346_d.defaultBlockState();
                    }
                    if (distance <= 0.25) {
                        state = Blocks.field_150348_b.defaultBlockState();
                    }
                    else if (distance <= 0.10000000149011612 && CEffectEvorsio.rand.nextInt(5) == 0) {
                        state = Blocks.field_150343_Z.defaultBlockState();
                    }
                    world.func_180501_a(at, state, 11);
                }
                return false;
            }
            else {
                final TileRitualPedestal pedestal = this.getPedestal(world, pos);
                if (pedestal != null) {
                    final BlockState state2 = world.getBlockState(at);
                    if (this.canBreakBlock(world, at, state2, this.buildFilter(pedestal))) {
                        BlockDropCaptureAssist.startCapturing();
                        try {
                            BlockUtils.breakBlockWithoutPlayer((ServerLevel)world, at, state2, ItemStack.EMPTY, true, true);
                        }
                        finally {
                            final NonNullList<ItemStack> captured = BlockDropCaptureAssist.getCapturedStacksAndStop();
                            captured.forEach(stack -> ItemUtils.dropItemNaturally(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack));
                        }
                        return true;
                    }
                    else {
                        this.sendConstellationPing(world, new Vector3((Vec3i)at).add(0.5, 0.5, 0.5));
                    }
                }
                return false;
            }
        }).ifRight(attemptedBreak -> this.sendConstellationPing(world, new Vector3((Vec3i)attemptedBreak).add(0.5, 0.5, 0.5))).left().orElse(false);
    }
    
    private boolean canBreakBlock(final Level world, final BlockPos pos, final BlockState state, final Predicate<BlockState> blacklist) {
        if (blacklist.test(state)) {
            return false;
        }
        final float hardness = state.func_185887_b((IBlockReader)world, pos);
        return hardness >= 0.0f && hardness < 75.0f && !state.isAir((IBlockReader)world, pos);
    }
    
    private Predicate<BlockState> buildFilter(final TileRitualPedestal pedestal) {
        final List<Predicate<BlockState>> filteredBlocks = pedestal.getConfiguredBlockStates().stream().map(blockState -> blockState::equals).collect((Collector<? super Object, ?, List<Predicate<BlockState>>>)Collectors.toList());
        this.addDefaultBreakBlacklist(filteredBlocks);
        return blockState -> {
            filteredBlocks.iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final Predicate<BlockState> filterTest = iterator.next();
                if (filterTest.test(blockState)) {
                    return true;
                }
            }
            return false;
        };
    }
    
    private void addDefaultBreakBlacklist(final List<Predicate<BlockState>> out) {
        out.add(state -> state.getBlock().equals(BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL));
        out.add(state -> state.getBlock().equals(BlocksAS.ROCK_COLLECTOR_CRYSTAL));
        out.add(state -> state.getBlock().equals(BlocksAS.LENS));
        out.add(state -> state.getBlock().equals(BlocksAS.PRISM));
    }
    
    @Override
    public Config getConfig() {
        return CEffectEvorsio.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectEvorsio.FLAG;
    }
    
    static {
        CEffectEvorsio.FLAG = ConstellationEffect.makeAffectionFlag("evorsio");
        CEffectEvorsio.CONFIG = new EvorsioConfig();
    }
    
    private static class EvorsioConfig extends Config
    {
        public EvorsioConfig() {
            super("evorsio", 6.0, 1.0);
        }
    }
}
