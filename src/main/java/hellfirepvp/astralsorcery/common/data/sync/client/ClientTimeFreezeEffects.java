package hellfirepvp.astralsorcery.common.data.sync.client;

import net.minecraft.nbt.ListTag;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.data.sync.server.DataTimeFreezeEffects;
import java.util.Collections;
import javax.annotation.Nonnull;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.util.time.TimeStopEffectHelper;
import java.util.List;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;

public class ClientTimeFreezeEffects extends ClientData<ClientTimeFreezeEffects>
{
    private final Map<RegistryKey<World>, List<TimeStopEffectHelper>> clientActiveFreezeZones;
    
    public ClientTimeFreezeEffects() {
        this.clientActiveFreezeZones = new HashMap<RegistryKey<World>, List<TimeStopEffectHelper>>();
    }
    
    @Nonnull
    public List<TimeStopEffectHelper> getTimeStopEffects(final World world) {
        return this.getTimeStopEffects((RegistryKey<World>)world.dimension());
    }
    
    @Nonnull
    public List<TimeStopEffectHelper> getTimeStopEffects(final RegistryKey<World> dim) {
        return this.clientActiveFreezeZones.getOrDefault(dim, Collections.emptyList());
    }
    
    private void applyChange(final DataTimeFreezeEffects.ServerSyncAction action) {
        final RegistryKey<World> worldKey = action.getDimKey();
        switch (action.getType()) {
            case ADD: {
                final List<TimeStopEffectHelper> zones = this.clientActiveFreezeZones.computeIfAbsent(worldKey, id -> new LinkedList());
                zones.add(action.getInvolvedEffect());
                break;
            }
            case REMOVE: {
                if (this.clientActiveFreezeZones.containsKey(worldKey)) {
                    this.clientActiveFreezeZones.get(worldKey).remove(action.getInvolvedEffect());
                    break;
                }
                break;
            }
            case CLEAR: {
                this.clientActiveFreezeZones.remove(worldKey);
                break;
            }
        }
    }
    
    @Override
    public void clear(final RegistryKey<World> dim) {
        this.clientActiveFreezeZones.remove(dim);
    }
    
    @Override
    public void clearClient() {
        this.clientActiveFreezeZones.clear();
    }
    
    public static class Reader extends ClientDataReader<ClientTimeFreezeEffects>
    {
        @Override
        public void readFromIncomingFullSync(final ClientTimeFreezeEffects data, final CompoundTag compound) {
            data.clientActiveFreezeZones.clear();
            final CompoundTag dimTag = compound.func_74775_l("dimTypes");
            for (final String dimKey : dimTag.func_150296_c()) {
                final RegistryKey<World> dim = (RegistryKey<World>)RegistryKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey));
                final List<TimeStopEffectHelper> effects = new LinkedList<TimeStopEffectHelper>();
                final ListTag listEffects = dimTag.getList(dimKey, 10);
                for (final Tag iNBT : listEffects) {
                    effects.add(TimeStopEffectHelper.deserializeNBT((CompoundTag)iNBT));
                }
                data.clientActiveFreezeZones.put(dim, effects);
            }
        }
        
        @Override
        public void readFromIncomingDiff(final ClientTimeFreezeEffects data, final CompoundTag compound) {
            final ListTag changes = compound.getList("changes", 10);
            for (final Tag iNBT : changes) {
                final DataTimeFreezeEffects.ServerSyncAction action = DataTimeFreezeEffects.ServerSyncAction.deserializeNBT((CompoundTag)iNBT);
                data.applyChange(action);
            }
        }
    }
}
