package hellfirepvp.astralsorcery.common.data.sync.server;

import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientTimeFreezeEffects;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractDataProvider;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.LinkedList;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.time.TimeStopEffectHelper;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractData;

public class DataTimeFreezeEffects extends AbstractData
{
    private final Map<ResourceKey<Level>, List<TimeStopEffectHelper>> serverActiveFreezeZones;
    private final List<ServerSyncAction> scheduledServerSyncChanges;
    
    private DataTimeFreezeEffects(final ResourceLocation key) {
        super(key);
        this.serverActiveFreezeZones = new HashMap<ResourceKey<Level>, List<TimeStopEffectHelper>>();
        this.scheduledServerSyncChanges = new LinkedList<ServerSyncAction>();
    }
    
    public void addNewEffect(final ResourceKey<Level> dim, final TimeStopEffectHelper effectHelper) {
        final List<TimeStopEffectHelper> zones = this.serverActiveFreezeZones.computeIfAbsent(dim, id -> new LinkedList());
        zones.add(effectHelper);
        this.scheduledServerSyncChanges.add(new ServerSyncAction(ServerSyncAction.ActionType.ADD, (ResourceKey)dim, effectHelper));
        this.markDirty();
    }
    
    public void removeEffect(final ResourceKey<Level> dim, final TimeStopEffectHelper effectHelper) {
        if (this.serverActiveFreezeZones.containsKey(dim)) {
            this.serverActiveFreezeZones.get(dim).remove(effectHelper);
        }
        this.scheduledServerSyncChanges.add(new ServerSyncAction(ServerSyncAction.ActionType.REMOVE, (ResourceKey)dim, effectHelper));
        this.markDirty();
    }
    
    @Override
    public void clear(final ResourceKey<Level> dim) {
        this.serverActiveFreezeZones.remove(dim);
    }
    
    @Override
    public void clearServer() {
        this.serverActiveFreezeZones.clear();
        this.scheduledServerSyncChanges.clear();
    }
    
    @Override
    public void writeAllDataToPacket(final CompoundTag compound) {
        final CompoundTag dimTag = new CompoundTag();
        for (final ResourceKey<Level> dim : this.serverActiveFreezeZones.keySet()) {
            final ListTag tagList = new ListTag();
            for (final TimeStopEffectHelper effect : this.serverActiveFreezeZones.get(dim)) {
                tagList.add((Object)effect.serializeNBT());
            }
            dimTag.put(dim.func_240901_a_().toString(), (Tag)tagList);
        }
        compound.put("dimTypes", (Tag)dimTag);
    }
    
    @Override
    public void writeDiffDataToPacket(final CompoundTag compound) {
        final ListTag changes = new ListTag();
        for (final ServerSyncAction action : this.scheduledServerSyncChanges) {
            changes.add((Object)action.serializeNBT());
        }
        compound.put("changes", (Tag)changes);
        this.scheduledServerSyncChanges.clear();
    }
    
    public static class ServerSyncAction
    {
        private final ActionType type;
        private final ResourceKey<Level> dim;
        private final TimeStopEffectHelper involvedEffect;
        
        private ServerSyncAction(final ActionType type, final ResourceKey<Level> dim, final TimeStopEffectHelper involvedEffect) {
            this.type = type;
            this.dim = dim;
            this.involvedEffect = involvedEffect;
        }
        
        private CompoundTag serializeNBT() {
            final CompoundTag out = new CompoundTag();
            out.putInt("type", this.type.ordinal());
            out.putString("dimType", this.dim.func_240901_a_().toString());
            switch (this.type) {
                case ADD:
                case REMOVE: {
                    out.put("effectTag", (Tag)this.involvedEffect.serializeNBT());

                }
            }
            return out;
        }
        
        public static ServerSyncAction deserializeNBT(final CompoundTag cmp) {
            final ActionType type = MiscUtils.getEnumEntry(ActionType.class, cmp.getInt("type"));
            final String dimKey = cmp.getString("dimType");
            final ResourceKey<Level> dim = (ResourceKey<Level>)ResourceKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey));
            TimeStopEffectHelper helper = null;
            switch (type) {
                case ADD:
                case REMOVE: {
                    helper = TimeStopEffectHelper.deserializeNBT(cmp.func_74775_l("effectTag"));

                }
            }
            return new ServerSyncAction(type, dim, helper);
        }
        
        @Nullable
        public TimeStopEffectHelper getInvolvedEffect() {
            return this.involvedEffect;
        }
        
        public ResourceKey<Level> getDimKey() {
            return this.dim;
        }
        
        public ActionType getType() {
            return this.type;
        }
        
        public enum ActionType
        {
            ADD, 
            REMOVE, 

        }
    }
    
    public static class Provider extends AbstractDataProvider<DataTimeFreezeEffects, ClientTimeFreezeEffects>
    {
        public Provider(final ResourceLocation key) {
            super(key);
        }
        
        @Override
        public DataTimeFreezeEffects provideServerData() {
            return new DataTimeFreezeEffects(this.getKey(), null);
        }
        
        @Override
        public ClientTimeFreezeEffects provideClientData() {
            return new ClientTimeFreezeEffects();
        }
        
        @Override
        public ClientDataReader<ClientTimeFreezeEffects> createReader() {
            return new ClientTimeFreezeEffects.Reader();
        }
    }
}
