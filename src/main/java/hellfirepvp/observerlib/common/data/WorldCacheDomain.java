package hellfirepvp.observerlib.common.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.function.Function;

public class WorldCacheDomain {
    private final ResourceLocation key;
    private final Set<SaveKey<? extends CachedWorldData>> knownSaveKeys = new HashSet<>();
    private final Map<ResourceLocation, Map<SaveKey<?>, CachedWorldData>> domainData = new HashMap<>();

    WorldCacheDomain(ResourceLocation key) { this.key = key; }

    public <T extends CachedWorldData> SaveKey<T> createSaveKey(String name, Function<SaveKey<T>, T> dataProvider) {
        for (SaveKey<?> k : knownSaveKeys) {
            if (((SaveKey<CachedWorldData>) k).identifier.equalsIgnoreCase(name)) return (SaveKey<T>) k;
        }
        SaveKey<T> key2 = new SaveKey<>(name, (Function) dataProvider);
        knownSaveKeys.add(key2);
        return key2;
    }

    @Nullable
    public <T extends CachedWorldData> SaveKey<T> getKey(String identifier) {
        for (SaveKey<?> k : knownSaveKeys) {
            if (((SaveKey<CachedWorldData>) k).identifier.equalsIgnoreCase(identifier)) return (SaveKey<T>) k;
        }
        return null;
    }

    @Nonnull
    public Set<SaveKey<? extends CachedWorldData>> getKnownSaveKeys() {
        return Collections.unmodifiableSet(knownSaveKeys);
    }

    public ResourceLocation getName() { return key; }

    // In 1.20.1: Level has level().dimension().location() for the dim key
    void tick(Level world) {
        ResourceLocation dimName = world.dimension().location();
        if (!domainData.containsKey(dimName)) return;
        Map<SaveKey<?>, ? extends CachedWorldData> dataMap = domainData.get(dimName);
        for (SaveKey<?> k : getKnownSaveKeys()) {
            if (dataMap.containsKey(k)) ((CachedWorldData) dataMap.get(k)).updateTick(world);
        }
    }

    @Nullable
    <T extends CachedWorldData> T getCachedData(ResourceLocation dimTypeName, SaveKey<T> key) {
        return (T) domainData.getOrDefault(dimTypeName, Collections.emptyMap()).get(key);
    }

    @Nullable
    private <T extends CachedWorldData> T getFromCache(Level world, SaveKey<T> key) {
        return getCachedData(world.dimension().location(), key);
    }

    Collection<ResourceLocation> getUsedWorlds() { return domainData.keySet(); }

    @Nonnull
    public <T extends CachedWorldData> T getData(Level world, SaveKey<T> key) {
        T data = getFromCache(world, key);
        if (data == null) {
            data = WorldCacheIOThread.loadNow(this, world, key);
            domainData.computeIfAbsent(world.dimension().location(), i -> new HashMap<>()).put(key, data);
        }
        return data;
    }

    public File getSaveDirectory() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return null;
        File dataDir = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT)
            .resolve(key.getPath()).toFile();
        if (!dataDir.exists()) dataDir.mkdirs();
        return dataDir;
    }

    void clear() { domainData.clear(); }

    public static class SaveKey<T extends CachedWorldData> {
        private final String identifier;
        private final Function<SaveKey<T>, T> instanceProvider;

        private SaveKey(String identifier, Function<SaveKey<T>, T> provider) {
            this.identifier = identifier;
            this.instanceProvider = provider;
        }

        public T getNewInstance(SaveKey<T> key) { return instanceProvider.apply(key); }
        public String getIdentifier() { return identifier; }
    }
}
