package hellfirepvp.astralsorcery.common.data.sync.server;

import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientTimeFreezeEntities;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractDataProvider;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import net.minecraft.world.entity.Entity;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractData;

public class DataTimeFreezeEntities extends AbstractData
{
    private final Map<RegistryKey<Level>, Set<Integer>> serverActiveEntityFreeze;
    private final Set<RegistryKey<Level>> serverSyncTypes;
    
    private DataTimeFreezeEntities(final ResourceLocation key) {
        super(key);
        this.serverActiveEntityFreeze = new HashMap<RegistryKey<Level>, Set<Integer>>();
        this.serverSyncTypes = new HashSet<RegistryKey<Level>>();
    }
    
    public void freezeEntity(final Entity e) {
        final RegistryKey<Level> dim = (RegistryKey<Level>)e.level().dimension();
        if (this.serverActiveEntityFreeze.computeIfAbsent(dim, dimType -> new HashSet()).add(e.func_145782_y())) {
            this.serverSyncTypes.add(dim);
            this.markDirty();
        }
    }
    
    public void unfreezeEntity(final Entity e) {
        final RegistryKey<Level> dim = (RegistryKey<Level>)e.level().dimension();
        if (this.serverActiveEntityFreeze.getOrDefault(dim, Collections.emptySet()).remove(e.func_145782_y())) {
            this.serverSyncTypes.add(dim);
            this.markDirty();
        }
    }
    
    public boolean isFrozen(final Entity e) {
        final RegistryKey<Level> dim = (RegistryKey<Level>)e.level().dimension();
        return this.serverActiveEntityFreeze.getOrDefault(dim, Collections.emptySet()).contains(e.func_145782_y());
    }
    
    @Override
    public void clear(final RegistryKey<Level> dimType) {
        this.serverActiveEntityFreeze.remove(dimType);
    }
    
    @Override
    public void clearServer() {
        this.serverActiveEntityFreeze.clear();
        this.serverSyncTypes.clear();
    }
    
    @Override
    public void writeAllDataToPacket(final CompoundTag compound) {
        this.writeEntityInformation(compound, this.serverActiveEntityFreeze);
    }
    
    @Override
    public void writeDiffDataToPacket(final CompoundTag compound) {
        final Map<RegistryKey<Level>, Set<Integer>> entities = new HashMap<RegistryKey<Level>, Set<Integer>>();
        this.serverSyncTypes.forEach(type -> entities.put(type, this.serverActiveEntityFreeze.getOrDefault(type, new HashSet<Integer>())));
        this.writeEntityInformation(compound, entities);
        this.serverSyncTypes.clear();
    }
    
    private void writeEntityInformation(final CompoundTag out, final Map<RegistryKey<Level>, Set<Integer>> entities) {
        final CompoundTag dimTag = new CompoundTag();
        entities.forEach((dim, entityIds) -> {
            final ListTag nbtEntities = new ListTag();
            entityIds.forEach(id -> nbtEntities.add((Object)IntTag.func_229692_a_((int)id)));
            dimTag.put(dim.func_240901_a_().toString(), (Tag)nbtEntities);
            return;
        });
        out.put("dimTypes", (Tag)dimTag);
    }
    
    public static class Provider extends AbstractDataProvider<DataTimeFreezeEntities, ClientTimeFreezeEntities>
    {
        public Provider(final ResourceLocation key) {
            super(key);
        }
        
        @Override
        public DataTimeFreezeEntities provideServerData() {
            return new DataTimeFreezeEntities(this.getKey(), null);
        }
        
        @Override
        public ClientTimeFreezeEntities provideClientData() {
            return new ClientTimeFreezeEntities();
        }
        
        @Override
        public ClientDataReader<ClientTimeFreezeEntities> createReader() {
            return new ClientTimeFreezeEntities.Reader();
        }
    }
}
