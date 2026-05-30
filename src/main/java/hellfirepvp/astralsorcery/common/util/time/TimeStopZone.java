package hellfirepvp.astralsorcery.common.util.time;

import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.boss.dragon.phase.IPhase;
import net.minecraft.world.entity.boss.dragon.phase.PhaseType;
import net.minecraft.world.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import java.util.Iterator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.data.config.registry.TileAccelerationBlacklistRegistry;
import java.util.Map;
import net.minecraft.util.Mth;
import java.util.LinkedList;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class TimeStopZone
{
    final EntityTargetController targetController;
    final float range;
    final BlockPos offset;
    private final Level world;
    private int ticksToLive;
    private boolean active;
    private final List<BlockEntity> cachedTiles;
    
    TimeStopZone(final EntityTargetController ctrl, final float range, final BlockPos offset, final Level world, final int tickLivespan) {
        this.active = true;
        this.cachedTiles = new LinkedList<BlockEntity>();
        this.targetController = ctrl;
        this.range = range;
        this.offset = offset;
        this.world = world;
        this.ticksToLive = tickLivespan;
    }
    
    void onServerTick() {
        if (!this.active) {

        }
        --this.ticksToLive;
        final int minX = Mth.func_76128_c((this.offset.getX() - this.range) / 16.0);
        final int maxX = Mth.func_76128_c((this.offset.getX() + this.range) / 16.0);
        final int minZ = Mth.func_76128_c((this.offset.getZ() - this.range) / 16.0);
        final int maxZ = Mth.func_76128_c((this.offset.getZ() + this.range) / 16.0);
        for (int xx = minX; xx <= maxX; ++xx) {
            for (int zz = minZ; zz <= maxZ; ++zz) {
                final Chunk ch = this.world.func_212866_a_(xx, zz);
                if (!ch.func_76621_g()) {
                    final Map<BlockPos, BlockEntity> map = ch.func_177434_r();
                    for (final Map.Entry<BlockPos, BlockEntity> teEntry : map.entrySet()) {
                        final BlockEntity te = teEntry.getValue();
                        if (TileAccelerationBlacklistRegistry.INSTANCE.canBeInfluenced(te) && te.getBlockState().func_218141_a((Vec3i)this.offset, (double)this.range) && this.world.field_175730_i.contains(te)) {
                            this.world.field_175730_i.remove(te);
                            this.safeCacheTile(te);
                        }
                    }
                }
            }
        }
    }
    
    private void safeCacheTile(final BlockEntity te) {
        if (te == null) {

        }
        for (final BlockEntity tile : this.cachedTiles) {
            if (tile.getBlockState().equals((Object)te.getBlockState())) {

            }
        }
        this.cachedTiles.add(te);
    }
    
    public void setTicksToLive(final int ticksToLive) {
        this.ticksToLive = ticksToLive;
    }
    
    void stopEffect() {
        for (final BlockEntity cached : this.cachedTiles) {
            final BlockState state = this.world.getBlockState(cached.getBlockState());
            if (state.getBlock().hasTileEntity(state)) {
                final BlockEntity te = state.getBlock().createTileEntity(state, (IBlockReader)this.world);
                if (te == null || !te.getClass().isAssignableFrom(cached.getClass())) {

                }
                this.world.field_175730_i.add(cached);
            }
        }
        this.cachedTiles.clear();
        this.active = false;
    }
    
    boolean shouldDespawn() {
        return this.ticksToLive <= 0 || !this.active;
    }
    
    boolean interceptEntityTick(final LivingEntity e) {
        return this.active && e != null && this.targetController.shouldFreezeEntity(e) && Vector3.atEntityCorner((Entity)e).distance((Vec3i)this.offset) <= this.range;
    }
    
    static void handleImportantEntityTicks(final LivingEntity e) {
        if (e.field_70737_aN > 0) {
            --e.field_70737_aN;
        }
        if (e.field_70172_ad > 0) {
            --e.field_70172_ad;
        }
        e.field_70169_q = e.getX();
        e.field_70167_r = e.getY();
        e.field_70166_s = e.getZ();
        e.field_184618_aE = e.field_70721_aZ;
        e.field_70760_ar = e.field_70761_aq;
        e.field_70127_C = e.xRot;
        e.field_70126_B = e.yRot;
        e.field_70758_at = e.field_70759_as;
        e.field_70732_aI = e.field_70733_aJ;
        e.field_70141_P = e.field_70140_Q;
        if (!e.level()) {
            e.func_213352_e(Vec3.field_186680_a);
        }
        if (e instanceof EnderDragonEntity) {
            final IPhase phase = ((EnderDragonEntity)e).func_184670_cT().func_188756_a();
            if (phase.func_188652_i() != PhaseType.field_188741_a && phase.func_188652_i() != PhaseType.field_188750_j) {
                ((EnderDragonEntity)e).func_184670_cT().func_188758_a(PhaseType.field_188741_a);
            }
        }
    }
    
    public static class EntityTargetController
    {
        final int ownerId;
        final boolean hasOwner;
        final boolean targetPlayers;
        
        EntityTargetController(final int ownerId, final boolean hasOwner, final boolean targetPlayers) {
            this.ownerId = ownerId;
            this.hasOwner = hasOwner;
            this.targetPlayers = targetPlayers;
        }
        
        boolean shouldFreezeEntity(final LivingEntity e) {
            return e.isAlive() && e.getMaxHealth() > 0.0f && (!(e instanceof EnderDragonEntity) || ((EnderDragonEntity)e).func_184670_cT().func_188756_a().func_188652_i() != PhaseType.field_188750_j) && (!this.hasOwner || e.func_145782_y() != this.ownerId) && (this.targetPlayers || !(e instanceof Player));
        }
        
        public static EntityTargetController allExcept(final Entity entity) {
            return new EntityTargetController(entity.func_145782_y(), true, true);
        }
        
        public static EntityTargetController noPlayers() {
            return new EntityTargetController(-1, false, false);
        }
        
        @Nonnull
        public CompoundTag serializeNBT() {
            final CompoundTag out = new CompoundTag();
            out.putBoolean("targetPlayers", this.targetPlayers);
            out.putBoolean("hasOwner", this.hasOwner);
            out.putInt("ownerEntityId", this.ownerId);
            return out;
        }
        
        @Nonnull
        public static EntityTargetController deserializeNBT(final CompoundTag cmp) {
            final boolean targetPlayers = cmp.getBoolean("targetPlayers");
            final boolean hasOwner = cmp.getBoolean("hasOwner");
            final int ownerId = cmp.getInt("ownerEntityId");
            return new EntityTargetController(ownerId, hasOwner, targetPlayers);
        }
    }
}
