package hellfirepvp.observerlib.common.data;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import hellfirepvp.observerlib.ObserverLib;
import net.minecraft.SharedConstants;
import net.minecraft.world.level.Level;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class WorldCacheIOThread extends TimerTask {
    private static WorldCacheIOThread saveTask;
    private static Timer ioThread;
    private Map<WorldCacheDomain, Map<ResourceLocation_Key, List<IWorldRelatedData>>> worldSaveQueue = Maps.newHashMap();
    private Map<WorldCacheDomain, Map<ResourceLocation_Key, List<IWorldRelatedData>>> awaitingSaveQueue = Maps.newHashMap();
    private boolean inSave = false;
    private boolean skipTick = false;

    // Inner class to avoid dependency on net.minecraft.resources.ResourceLocation for map keys
    private static class ResourceLocation_Key {
        final String key;
        ResourceLocation_Key(net.minecraft.resources.ResourceLocation loc) { this.key = loc.toString(); }
        @Override public boolean equals(Object o) { return o instanceof ResourceLocation_Key && key.equals(((ResourceLocation_Key)o).key); }
        @Override public int hashCode() { return key.hashCode(); }
    }

    private WorldCacheIOThread() {}

    public static void onServerStart() {
        if (ioThread != null) return;
        saveTask = new WorldCacheIOThread();
        (ioThread = new Timer("WorldCacheIOThread", true)).scheduleAtFixedRate(saveTask, 30000L, 30000L);
    }

    public static void onServerStop() {
        if (saveTask != null) { saveTask.flushAndSaveAll(); saveTask.cancel(); saveTask = null; }
        if (ioThread != null) { ioThread.cancel(); ioThread = null; }
    }

    @Override
    public void run() {
        if (skipTick) return;
        inSave = true;
        saveAllNow();
        worldSaveQueue.clear();
        for (WorldCacheDomain domain : awaitingSaveQueue.keySet()) {
            for (Map.Entry<ResourceLocation_Key, List<IWorldRelatedData>> entry : awaitingSaveQueue.get(domain).entrySet()) {
                worldSaveQueue.computeIfAbsent(domain, d -> new HashMap<>()).put(entry.getKey(), entry.getValue());
            }
        }
        awaitingSaveQueue.clear();
        inSave = false;
    }

    private void flushAndSaveAll() {
        skipTick = true;
        for (WorldCacheDomain domain : awaitingSaveQueue.keySet()) {
            for (Map.Entry<ResourceLocation_Key, List<IWorldRelatedData>> entry : awaitingSaveQueue.get(domain).entrySet()) {
                worldSaveQueue.computeIfAbsent(domain, d -> new HashMap<>()).put(entry.getKey(), entry.getValue());
            }
        }
        saveAllNow();
        worldSaveQueue.clear();
        awaitingSaveQueue.clear();
        skipTick = false;
        inSave = false;
    }

    static void scheduleSave(WorldCacheDomain domain, net.minecraft.resources.ResourceLocation dimTypeName, IWorldRelatedData data) {
        WorldCacheIOThread tr = saveTask;
        if (tr == null) return;
        ResourceLocation_Key key = new ResourceLocation_Key(dimTypeName);
        if (tr.inSave) {
            tr.awaitingSaveQueue.computeIfAbsent(domain, d -> new HashMap<>()).computeIfAbsent(key, id -> new ArrayList<>()).add(data);
        } else {
            tr.worldSaveQueue.computeIfAbsent(domain, d -> new HashMap<>()).computeIfAbsent(key, id -> new ArrayList<>()).add(data);
        }
    }

    @Nonnull
    static <T extends CachedWorldData> T loadNow(WorldCacheDomain domain, Level world, WorldCacheDomain.SaveKey<T> key) {
        T loaded = loadDataFromFile(domain, world.dimension().location(), key);
        loaded.onLoad(world);
        return loaded;
    }

    private void saveAllNow() {
        for (WorldCacheDomain domain : worldSaveQueue.keySet()) {
            for (Map.Entry<ResourceLocation_Key, List<IWorldRelatedData>> entry : worldSaveQueue.get(domain).entrySet()) {
                entry.getValue().forEach(data -> saveNow(domain, entry.getKey(), data));
            }
        }
    }

    private void saveNow(WorldCacheDomain domain, ResourceLocation_Key dimTypeName, IWorldRelatedData data) {
        try {
            saveDataToFile(domain.getSaveDirectory(), dimTypeName, data);
        } catch (IOException e) {
            ObserverLib.log.warn("Unable to save WorldData! Dim=" + dimTypeName.key + " key=" + data.getSaveKey().getIdentifier());
            e.printStackTrace();
        }
        data.markSaved();
    }

    private static void saveDataToFile(File baseDirectory, ResourceLocation_Key dimTypeName, IWorldRelatedData data) throws IOException {
        DirectorySet f = getDirectorySet(baseDirectory, dimTypeName, data.getSaveKey());
        if (!f.getParentDirectory().exists()) f.getParentDirectory().mkdirs();
        data.writeData(f.getActualDirectory(), f.getBackupDirectory());
    }

    @Nonnull
    private static <T extends CachedWorldData> T loadDataFromFile(WorldCacheDomain domain, net.minecraft.resources.ResourceLocation dimTypeName, WorldCacheDomain.SaveKey<T> key) {
        ResourceLocation_Key rlKey = new ResourceLocation_Key(dimTypeName);
        DirectorySet f = getDirectorySet(domain.getSaveDirectory(), rlKey, key);
        if (!f.getActualDirectory().exists() && !f.getBackupDirectory().exists()) return key.getNewInstance(key);
        ObserverLib.log.info("Load CachedWorldData '" + key.getIdentifier() + "' for world " + dimTypeName);
        boolean errored = false;
        T data = null;
        try { if (f.getActualDirectory().exists()) data = attemptLoad(key, f.getActualDirectory()); }
        catch (Exception exc) { ObserverLib.log.info("Loading worlddata '" + key.getIdentifier() + "' failed for actual save, trying backup."); errored = true; }
        if (data == null) {
            try { if (f.getBackupDirectory().exists()) data = attemptLoad(key, f.getBackupDirectory()); }
            catch (Exception exc) { ObserverLib.log.info("Loading worlddata '" + key.getIdentifier() + "' failed for backup save."); errored = true; }
        }
        if (data == null && errored) {
            DirectorySet errorSet = f.getErrorDirectories();
            try {
                if (f.getActualDirectory().exists()) { Files.copy(f.getActualDirectory(), errorSet.getActualDirectory()); FileUtils.deleteDirectory(f.getActualDirectory()); }
                if (f.getBackupDirectory().exists()) { Files.copy(f.getBackupDirectory(), errorSet.getBackupDirectory()); FileUtils.deleteDirectory(f.getBackupDirectory()); }
            } catch (Exception e) { ObserverLib.log.info("Could not copy erroneous worlddata to error directory."); e.printStackTrace(); }
        }
        if (data == null) data = key.getNewInstance(key);
        ObserverLib.log.info("Loading of '" + key.getIdentifier() + "' for world " + dimTypeName + " finished.");
        return data;
    }

    private static <T extends CachedWorldData> T attemptLoad(WorldCacheDomain.SaveKey<T> key, File baseDirectory) throws IOException {
        T data = key.getNewInstance(key);
        data.readData(baseDirectory);
        return data;
    }

    private static synchronized DirectorySet getDirectorySet(File baseDirectory, ResourceLocation_Key dimTypeName, WorldCacheDomain.SaveKey<?> key) {
        File worldDir = new File(baseDirectory, "DIM_" + sanitizeFileName(dimTypeName.key));
        if (!worldDir.exists()) worldDir.mkdirs();
        else ensureFolder(worldDir);
        return new DirectorySet(new File(worldDir, key.getIdentifier()));
    }

    private static void ensureFolder(File f) {
        if (!f.isDirectory()) {
            ObserverLib.log.warn("dataFile exists but is not a folder: " + f.getAbsolutePath());
            throw new IllegalStateException("Affected file: " + f.getAbsolutePath());
        }
    }

    private static String sanitizeFileName(String name) {
        name = name.trim().replace(' ', '_').toLowerCase();
        name = name.replaceAll("[^a-zA-Z0-9\\.\\-:_]", "_");
        return name;
    }

    private static class DirectorySet {
        private final File actualDirectory;
        private final File backupDirectory;
        private DirectorySet(File worldDirectory) {
            this.actualDirectory = worldDirectory;
            this.backupDirectory = new File(worldDirectory.getParent(), worldDirectory.getName() + "-Backup");
        }
        File getParentDirectory() { return actualDirectory.getParentFile(); }
        File getActualDirectory() { return actualDirectory; }
        File getBackupDirectory() { return backupDirectory; }
        DirectorySet getErrorDirectories() {
            File errorDir = new File(actualDirectory.getParent(), actualDirectory.getName() + "-Error");
            if (!errorDir.exists()) errorDir.mkdirs();
            return new DirectorySet(errorDir);
        }
    }
}
