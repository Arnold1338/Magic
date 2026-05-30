package hellfirepvp.astralsorcery.common.constellation.effect.base;

import net.minecraft.world.level.LevelReader;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.IServerWorld;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.world.level.biome.MobSpawnInfo;
import java.util.LinkedList;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;

public class ListEntries
{
    public static class EntitySpawnEntry extends CounterEntry
    {
        private EntityType<?> type;
        
        public EntitySpawnEntry(final BlockPos pos) {
            super(pos);
        }
        
        public EntitySpawnEntry(final BlockPos pos, final EntityType<?> type) {
            super(pos);
            this.type = type;
        }
        
        @Override
        public void readFromNBT(final CompoundTag nbt) {
            super.readFromNBT(nbt);
            this.type = (EntityType<?>)ForgeRegistries.ENTITIES.getValue(new ResourceLocation(nbt.getString("entity")));
        }
        
        @Override
        public void writeToNBT(final CompoundTag nbt) {
            super.writeToNBT(nbt);
            nbt.putString("entity", this.type.getRegistryName().toString());
        }
        
        public static EntitySpawnEntry createEntry(final ServerLevel world, final BlockPos pos, final MobSpawnType reason) {
            final Biome b = world.func_226691_t_(pos);
            final List<MobSpawnInfo.Spawners> applicable = new LinkedList<MobSpawnInfo.Spawners>();
            if (DayTimeHelper.isNight((Level)world)) {
                applicable.addAll(b.func_242433_b().func_242559_a(MobCategory.MONSTER));
            }
            else {
                applicable.addAll(b.func_242433_b().func_242559_a(MobCategory.CREATURE));
            }
            if (applicable.isEmpty()) {
                return null;
            }
            Collections.shuffle(applicable);
            final MobSpawnInfo.Spawners entry = applicable.get(world.field_73012_v.nextInt(applicable.size()));
            final EntityType<?> type = (EntityType<?>)entry.field_242588_c;
            if (type != null && EntityUtils.canEntitySpawnHere(world, pos, (EntityType<? extends Entity>)type, reason, 9, e -> e.func_184211_a("skip.spawn.deny"))) {
                return new EntitySpawnEntry(pos, type);
            }
            return null;
        }
        
        public void spawn(final ServerLevel world, final MobSpawnType reason) {
            if (this.type == null) {

            }
            final Entity e = this.type.func_200721_a((Level)world);
            if (e != null) {
                e.func_184211_a("skip.spawn.deny");
                final BlockPos at = this.getPos();
                e.func_70012_b(at.getX() + 0.5, at.getY() + 0.5, at.getZ() + 0.5, world.field_73012_v.nextFloat() * 360.0f, 0.0f);
                if (e instanceof MobEntity) {
                    ((MobEntity)e).func_213386_a((IServerWorld)world, world.func_175649_E(at), reason, (SpawnGroupData)null, (CompoundTag)null);
                    if (!((MobEntity)e).func_205019_a((IWorldReader)world)) {
                        e.func_70106_y();

                    }
                }
                world.addFreshEntity(e);
                world.func_217379_c(2004, e.func_233580_cy_(), 0);
                world.func_217379_c(2004, e.func_233580_cy_(), 0);
            }
        }
    }
    
    public static class CounterMaxEntry extends CounterEntry
    {
        private int maxCount;
        
        public CounterMaxEntry(final BlockPos pos) {
            super(pos);
        }
        
        public CounterMaxEntry(final BlockPos pos, final int maxCount) {
            super(pos);
            this.maxCount = maxCount;
        }
        
        public int getMaxCount() {
            return this.maxCount;
        }
        
        public void setMaxCount(final int maxCount) {
            this.maxCount = maxCount;
        }
        
        @Override
        public void writeToNBT(final CompoundTag nbt) {
            super.writeToNBT(nbt);
            nbt.putInt("maxCount", this.maxCount);
        }
        
        @Override
        public void readFromNBT(final CompoundTag nbt) {
            super.readFromNBT(nbt);
            this.maxCount = nbt.getInt("maxCount");
        }
    }
    
    public static class CounterEntry extends PosEntry
    {
        private int counter;
        
        public CounterEntry(final BlockPos pos) {
            super(pos);
            this.counter = 0;
        }
        
        public int getCounter() {
            return this.counter;
        }
        
        public void setCounter(final int counter) {
            this.counter = counter;
        }
        
        @Override
        public void writeToNBT(final CompoundTag nbt) {
            super.writeToNBT(nbt);
            nbt.putInt("counter", this.counter);
        }
        
        @Override
        public void readFromNBT(final CompoundTag nbt) {
            super.readFromNBT(nbt);
            this.counter = nbt.getInt("counter");
        }
    }
    
    public static class PosEntry implements CEffectAbstractList.ListEntry
    {
        private final BlockPos pos;
        
        public PosEntry(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public BlockPos getPos() {
            return this.pos;
        }
        
        @Override
        public void writeToNBT(final CompoundTag nbt) {
        }
        
        @Override
        public void readFromNBT(final CompoundTag nbt) {
        }
    }
}
