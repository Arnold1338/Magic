package hellfirepvp.astralsorcery.common.data.sync.server;

import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientPatreonFlares;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractDataProvider;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import net.minecraft.world.entity.player.Player;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonPartialEntity;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractData;

public class DataPatreonFlares extends AbstractData
{
    private final Map<UUID, Set<PatreonPartialEntity>> entitiesServer;
    private final Set<UUID> flarePlayerUpdates;
    private final Set<UUID> flareRemovals;
    
    private DataPatreonFlares(final ResourceLocation key) {
        super(key);
        this.entitiesServer = new HashMap<UUID, Set<PatreonPartialEntity>>();
        this.flarePlayerUpdates = new HashSet<UUID>();
        this.flareRemovals = new HashSet<UUID>();
    }
    
    @Nullable
    public PatreonPartialEntity createEntity(final Player player, final PatreonEffect value) {
        final UUID owner = player.getUUID();
        final PatreonPartialEntity entity = value.createEntity(owner);
        if (entity == null) {
            return null;
        }
        entity.placeNear(player);
        this.entitiesServer.computeIfAbsent(owner, o -> new HashSet()).add(entity);
        this.flareRemovals.remove(owner);
        this.flarePlayerUpdates.add(owner);
        this.markDirty();
        return entity;
    }
    
    public void updateEntitiesOf(final UUID playerUUID) {
        this.flareRemovals.remove(playerUUID);
        this.flarePlayerUpdates.add(playerUUID);
        this.markDirty();
    }
    
    public void removeEntities(final UUID playerUUID) {
        this.flarePlayerUpdates.remove(playerUUID);
        this.flareRemovals.add(playerUUID);
        this.markDirty();
        this.entitiesServer.getOrDefault(playerUUID, Collections.emptySet()).forEach(p -> p.setRemoved(true));
    }
    
    @Nonnull
    public Collection<UUID> getOwners() {
        return this.entitiesServer.keySet();
    }
    
    @Nonnull
    public Collection<PatreonPartialEntity> getEntities(final UUID playerUUID) {
        return this.entitiesServer.getOrDefault(playerUUID, Collections.emptySet());
    }
    
    @Nonnull
    public Collection<Collection<PatreonPartialEntity>> getEntities() {
        return new ArrayList<Collection<PatreonPartialEntity>>(this.entitiesServer.values());
    }
    
    @Override
    public void clear(final RegistryKey<Level> dim) {
    }
    
    @Override
    public void clearServer() {
        this.entitiesServer.clear();
        this.flarePlayerUpdates.clear();
        this.flareRemovals.clear();
    }
    
    @Override
    public void writeAllDataToPacket(final CompoundTag compound) {
        final ListTag entities = new ListTag();
        for (final UUID playerUUID : this.entitiesServer.keySet()) {
            final CompoundTag tag = new CompoundTag();
            tag.putUUID("playerUUID", playerUUID);
            final ListTag entityList = new ListTag();
            for (final PatreonPartialEntity entity : this.entitiesServer.get(playerUUID)) {
                final CompoundTag entityNBT = new CompoundTag();
                entityNBT.putUUID("id", entity.getEffectUUID());
                final CompoundTag data = new CompoundTag();
                entity.writeToNBT(data);
                entityNBT.put("data", (Tag)data);
                entityList.add((Object)entityNBT);
            }
            tag.put("entityList", (Tag)entityList);
            entities.add((Object)tag);
        }
        compound.put("entities", (Tag)entities);
    }
    
    @Override
    public void writeDiffDataToPacket(final CompoundTag compound) {
        final ListTag listUpdates = new ListTag();
        for (final UUID playerUUID : this.flarePlayerUpdates) {
            final CompoundTag tag = new CompoundTag();
            tag.putUUID("playerUUID", playerUUID);
            final ListTag entityList = new ListTag();
            for (final PatreonPartialEntity entity : this.entitiesServer.get(playerUUID)) {
                final CompoundTag entityNBT = new CompoundTag();
                entityNBT.putUUID("id", entity.getEffectUUID());
                final CompoundTag data = new CompoundTag();
                entity.writeToNBT(data);
                entityNBT.put("data", (Tag)data);
                entityList.add((Object)entityNBT);
            }
            tag.put("entityList", (Tag)entityList);
            listUpdates.add((Object)tag);
        }
        final ListTag listRemovals = new ListTag();
        for (final UUID playerUUID2 : this.flareRemovals) {
            final CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("playerUUID", playerUUID2);
            listRemovals.add((Object)playerTag);
        }
        compound.put("updates", (Tag)listUpdates);
        compound.put("removals", (Tag)listRemovals);
        this.flarePlayerUpdates.clear();
        this.flareRemovals.clear();
    }
    
    public static class Provider extends AbstractDataProvider<DataPatreonFlares, ClientPatreonFlares>
    {
        public Provider(final ResourceLocation key) {
            super(key);
        }
        
        @Override
        public DataPatreonFlares provideServerData() {
            return new DataPatreonFlares(this.getKey(), null);
        }
        
        @Override
        public ClientPatreonFlares provideClientData() {
            return new ClientPatreonFlares();
        }
        
        @Override
        public ClientDataReader<ClientPatreonFlares> createReader() {
            return new ClientPatreonFlares.Reader();
        }
    }
}
