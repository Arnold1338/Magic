package hellfirepvp.astralsorcery.common.auxiliary;

import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;
import java.util.HashMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.observerlib.common.util.tick.TickManager;
import java.util.function.Consumer;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.event.TickEvent;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.tick.TickTokenMap;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;

public class BlockBreakHelper
{
    private static final Map<RegistryKey<World>, TickTokenMap<BlockPos, BreakEntry>> breakMap;
    
    public static void addProgress(final World world, final BlockPos pos, final float percStrength, final Supplier<Float> expectedHardness) {
        final TickTokenMap<BlockPos, BreakEntry> map = BlockBreakHelper.breakMap.computeIfAbsent((RegistryKey<World>)world.dimension(), key -> {
            final TickTokenMap<BlockPos, BreakEntry> tkMap = new TickTokenMap<BlockPos, BreakEntry>(TickEvent.Type.SERVER, new TickEvent.Type[0]);
            AstralSorcery.getProxy().getTickManager().register((ITickHandler)tkMap);
            return tkMap;
        });
        BreakEntry breakProgress = map.get(pos);
        if (breakProgress == null) {
            final Float hardness = expectedHardness.get();
            if (hardness == null) {
                return;
            }
            breakProgress = new BreakEntry(expectedHardness.get(), (IWorld)world, pos, world.getBlockState(pos));
            map.put(pos, breakProgress);
        }
        final BreakEntry breakEntry = breakProgress;
        breakEntry.breakProgress -= percStrength;
        breakProgress.idleTimeout = 0;
    }
    
    public static void clearServerCache() {
        final TickManager mgr = AstralSorcery.getProxy().getTickManager();
        BlockBreakHelper.breakMap.values().forEach((Consumer<? super Object>)mgr::unregister);
        BlockBreakHelper.breakMap.clear();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void blockBreakAnimation(final PktPlayEffect pktPlayEffect) {
        final BlockPos pos = ByteBufUtils.readPos(pktPlayEffect.getExtraData());
        final int id = pktPlayEffect.getExtraData().readInt();
        final BlockState state = Block.func_196257_b(id);
        RenderingUtils.playBlockBreakParticles(pos, state, state);
    }
    
    static {
        breakMap = new HashMap<RegistryKey<World>, TickTokenMap<BlockPos, BreakEntry>>();
    }
    
    public static class BreakEntry implements TickTokenMap.TickMapToken<Float>, CEffectAbstractList.ListEntry
    {
        private float breakProgress;
        private final IWorld world;
        private BlockPos pos;
        private BlockState expected;
        private int idleTimeout;
        
        public BreakEntry(@Nonnull final Float value, final IWorld world, final BlockPos at, final BlockState expectedToBreak) {
            this.breakProgress = value;
            this.world = world;
            this.pos = at;
            this.expected = expectedToBreak;
        }
        
        @Override
        public int getRemainingTimeout() {
            return (this.breakProgress > 0.0f && this.idleTimeout < 20) ? 1 : 0;
        }
        
        @Override
        public void tick() {
            ++this.idleTimeout;
        }
        
        @Override
        public void onTimeout() {
            if (this.breakProgress > 0.0f) {
                return;
            }
            final BlockState nowAt = this.world.getBlockState(this.pos);
            if (this.world instanceof ServerLevel && BlockUtils.matchStateExact(this.expected, nowAt)) {
                BlockUtils.breakBlockWithoutPlayer((ServerLevel)this.world, this.pos, this.world.getBlockState(this.pos), ItemStack.field_190927_a, true, true);
            }
        }
        
        @Override
        public Float getValue() {
            return this.breakProgress;
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
        
        @Override
        public void readFromNBT(final CompoundTag nbt) {
            this.breakProgress = nbt.getFloat("breakProgress");
            this.pos = NBTHelper.readBlockPosFromNBT(nbt);
            this.expected = Block.func_196257_b(nbt.getInt("expectedStateId"));
        }
        
        @Override
        public void writeToNBT(final CompoundTag nbt) {
            nbt.func_74776_a("breakProgress", this.breakProgress);
            NBTHelper.writeBlockPosToNBT(this.pos, nbt);
            nbt.putInt("expectedStateId", Block.func_196246_j(this.expected));
        }
    }
}
