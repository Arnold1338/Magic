package hellfirepvp.astralsorcery.common.data.sync;

import java.util.HashSet;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.EnumSet;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncData;
import net.minecraft.nbt.CompoundTag;
import java.util.HashMap;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.astralsorcery.common.data.sync.server.DataPatreonFlares;
import hellfirepvp.astralsorcery.common.data.sync.server.DataTimeFreezeEntities;
import hellfirepvp.astralsorcery.common.data.sync.server.DataTimeFreezeEffects;
import hellfirepvp.astralsorcery.common.data.sync.server.DataLightBlockEndpoints;
import hellfirepvp.astralsorcery.common.data.sync.server.DataLightConnections;
import net.minecraftforge.fml.LogicalSide;
import java.util.Iterator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractDataProvider;
import java.util.Set;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractData;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class SyncDataHolder implements ITickHandler
{
    private static final SyncDataHolder tickInstance;
    public static final ResourceLocation DATA_LIGHT_CONNECTIONS;
    public static final ResourceLocation DATA_LIGHT_BLOCK_ENDPOINTS;
    public static final ResourceLocation DATA_TIME_FREEZE_EFFECTS;
    public static final ResourceLocation DATA_TIME_FREEZE_ENTITIES;
    public static final ResourceLocation DATA_PATREON_FLARES;
    private static final Map<ResourceLocation, AbstractData> serverData;
    private static final Map<ResourceLocation, ClientData<?>> clientData;
    private static final Map<ResourceLocation, ClientDataReader<?>> readers;
    private static final Set<ResourceLocation> dirtyData;
    private static final Object lck;
    
    private SyncDataHolder() {
    }
    
    public static SyncDataHolder getTickInstance() {
        return SyncDataHolder.tickInstance;
    }
    
    public static void register(final AbstractDataProvider<? extends AbstractData, ? extends ClientData> provider) {
        SyncDataRegistry.register(provider);
        SyncDataHolder.serverData.put(provider.getKey(), (AbstractData)provider.provideServerData());
        SyncDataHolder.clientData.put(provider.getKey(), (ClientData<?>)provider.provideClientData());
        SyncDataHolder.readers.put(provider.getKey(), provider.createReader());
    }
    
    public static <T extends AbstractData> void executeServer(final ResourceLocation key, final Class<T> typeHint, final Consumer<T> fct) {
        computeServer(key, typeHint, (Function<T, Object>)MiscUtils.nullFunction((Consumer<T>)fct));
    }
    
    public static <T extends AbstractData, V> Optional<V> computeServer(final ResourceLocation key, final Class<T> typeHint, final Function<T, V> fct) {
        synchronized (SyncDataHolder.lck) {
            final T dat = (T)SyncDataHolder.serverData.get(key);
            if (dat != null) {
                return Optional.ofNullable(fct.apply(dat));
            }
            return Optional.empty();
        }
    }
    
    public static <T extends ClientData<T>> void executeClient(final ResourceLocation key, final Class<T> typeHint, final Consumer<T> fct) {
        computeClient(key, typeHint, (Function<T, Object>)MiscUtils.nullFunction((Consumer<T>)fct));
    }
    
    public static <T extends ClientData<T>, V> Optional<V> computeClient(final ResourceLocation key, final Class<T> typeHint, final Function<T, V> fct) {
        final T dat = (T)SyncDataHolder.clientData.get(key);
        if (dat != null) {
            return Optional.ofNullable(fct.apply(dat));
        }
        return Optional.empty();
    }
    
    @Nullable
    public static <T extends ClientData<T>> ClientDataReader<T> getReader(final ResourceLocation key) {
        return (ClientDataReader)SyncDataHolder.readers.get(key);
    }
    
    public static void markForUpdate(final ResourceLocation key) {
        synchronized (SyncDataHolder.lck) {
            SyncDataHolder.dirtyData.add(key);
        }
    }
    
    public static void clearWorld(final World world) {
        final RegistryKey<World> dim = (RegistryKey<World>)world.dimension();
        for (final ResourceLocation key : SyncDataRegistry.getKnownKeys()) {
            if (!world.func_201670_d()) {
                executeServer(key, AbstractData.class, data -> data.clear((RegistryKey<World>)dim));
            }
        }
    }
    
    public static void clear(final LogicalSide side) {
        for (final ResourceLocation key : SyncDataRegistry.getKnownKeys()) {
            if (side.isClient()) {
                executeClient(key, ClientData.class, ClientData::clearClient);
            }
            else {
                executeServer(key, AbstractData.class, AbstractData::clearServer);
            }
        }
    }
    
    public static void initialize() {
        register(new DataLightConnections.Provider(SyncDataHolder.DATA_LIGHT_CONNECTIONS));
        register(new DataLightBlockEndpoints.Provider(SyncDataHolder.DATA_LIGHT_BLOCK_ENDPOINTS));
        register(new DataTimeFreezeEffects.Provider(SyncDataHolder.DATA_TIME_FREEZE_EFFECTS));
        register(new DataTimeFreezeEntities.Provider(SyncDataHolder.DATA_TIME_FREEZE_ENTITIES));
        register(new DataPatreonFlares.Provider(SyncDataHolder.DATA_PATREON_FLARES));
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        if (SyncDataHolder.dirtyData.isEmpty()) {
            return;
        }
        final Map<ResourceLocation, CompoundTag> pktData = new HashMap<ResourceLocation, CompoundTag>();
        synchronized (SyncDataHolder.lck) {
            for (final ResourceLocation key : SyncDataHolder.dirtyData) {
                final AbstractData dat = SyncDataHolder.serverData.get(key);
                if (dat != null) {
                    final CompoundTag nbt = new CompoundTag();
                    dat.writeDiffDataToPacket(nbt);
                    pktData.put(key, nbt);
                }
            }
            SyncDataHolder.dirtyData.clear();
        }
        final PktSyncData dataSync = new PktSyncData(pktData);
        PacketChannel.CHANNEL.sendToAll(dataSync);
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.SERVER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Sync Data Holder";
    }
    
    static {
        tickInstance = new SyncDataHolder();
        DATA_LIGHT_CONNECTIONS = AstralSorcery.key("connections");
        DATA_LIGHT_BLOCK_ENDPOINTS = AstralSorcery.key("endpoints");
        DATA_TIME_FREEZE_EFFECTS = AstralSorcery.key("time_freeze");
        DATA_TIME_FREEZE_ENTITIES = AstralSorcery.key("time_freeze_entities");
        DATA_PATREON_FLARES = AstralSorcery.key("patreon");
        serverData = new HashMap<ResourceLocation, AbstractData>();
        clientData = new HashMap<ResourceLocation, ClientData<?>>();
        readers = new HashMap<ResourceLocation, ClientDataReader<?>>();
        dirtyData = new HashSet<ResourceLocation>();
        lck = new Object();
    }
}
