package hellfirepvp.astralsorcery.common.data.sync.client;

import java.util.Iterator;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectHelper;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import java.util.HashSet;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonPartialEntity;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;

public class ClientPatreonFlares extends ClientData<ClientPatreonFlares>
{
    private final Map<UUID, Set<PatreonPartialEntity>> entitiesClient;
    
    public ClientPatreonFlares() {
        this.entitiesClient = new HashMap<UUID, Set<PatreonPartialEntity>>();
    }
    
    @Nonnull
    public Collection<UUID> getOwners() {
        return this.entitiesClient.keySet();
    }
    
    @Nonnull
    public Collection<PatreonPartialEntity> getEntities(final UUID playerUUID) {
        return this.entitiesClient.getOrDefault(playerUUID, Collections.emptySet());
    }
    
    @Nonnull
    public Collection<Collection<PatreonPartialEntity>> getEntities() {
        return new ArrayList<Collection<PatreonPartialEntity>>(this.entitiesClient.values());
    }
    
    @Override
    public void clear(final ResourceKey<Level> dim) {
    }
    
    @Override
    public void clearClient() {
        this.entitiesClient.clear();
    }
    
    public static class Reader extends ClientDataReader<ClientPatreonFlares>
    {
        @Override
        public void readFromIncomingFullSync(final ClientPatreonFlares data, final CompoundTag compound) {
            data.entitiesClient.clear();
            final ListTag entities = compound.getList("entities", 10);
            for (final Tag iNBT : entities) {
                final CompoundTag tag = (CompoundTag)iNBT;
                final UUID playerUUID = tag.getUUID("playerUUID");
                final Set<PatreonPartialEntity> entitySet = new HashSet<PatreonPartialEntity>();
                final ListTag entityList = tag.getList("entityList", 10);
                for (final Tag iEntityTag : entityList) {
                    final CompoundTag entityNBT = (CompoundTag)iEntityTag;
                    final UUID effectUUID = entityNBT.getUUID("id");
                    final PatreonEffect effect = PatreonEffectHelper.getPatreonEffects(LogicalSide.CLIENT, playerUUID).stream().filter(eff -> eff.getEffectUUID().equals(effectUUID)).findFirst().orElse(null);
                    if (effect == null) {
                        continue;
                    }
                    final PatreonPartialEntity entity = effect.createEntity(playerUUID);
                    if (entity == null) {
                        continue;
                    }
                    entity.readFromNBT(entityNBT.func_74775_l("data"));
                    entitySet.add(entity);
                }
                data.entitiesClient.put(playerUUID, entitySet);
            }
        }
        
        @Override
        public void readFromIncomingDiff(final ClientPatreonFlares data, final CompoundTag compound) {
            final ListTag entities = compound.getList("updates", 10);
            for (final Tag iNBT : entities) {
                final CompoundTag tag = (CompoundTag)iNBT;
                final UUID playerUUID = tag.getUUID("playerUUID");
                final Set<PatreonPartialEntity> entitySet = data.entitiesClient.computeIfAbsent(playerUUID, p -> new HashSet());
                final ListTag entityList = tag.getList("entityList", 10);
                for (final Tag iEntityTag : entityList) {
                    final CompoundTag entityNBT = (CompoundTag)iEntityTag;
                    final UUID effectUUID = entityNBT.getUUID("id");
                    final PatreonEffect effect = PatreonEffectHelper.getPatreonEffects(LogicalSide.CLIENT, playerUUID).stream().filter(eff -> eff.getEffectUUID().equals(effectUUID)).findFirst().orElse(null);
                    if (effect == null) {
                        continue;
                    }
                    PatreonPartialEntity entity = entitySet.stream().filter(e -> e.getEffectUUID().equals(effectUUID)).findFirst().orElse(null);
                    if (entity == null) {
                        entity = effect.createEntity(playerUUID);
                        if (entity == null) {
                            continue;
                        }
                        entitySet.add(entity);
                    }
                    entity.readFromNBT(entityNBT.func_74775_l("data"));
                }
            }
            final ListTag removals = compound.getList("removals", 10);
            for (final Tag iNBT2 : removals) {
                final CompoundTag tag2 = (CompoundTag)iNBT2;
                final UUID playerUUID2 = tag2.getUUID("playerUUID");
                data.entitiesClient.remove(playerUUID2);
            }
        }
    }
}
