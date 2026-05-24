package hellfirepvp.astralsorcery.common.data.research;

import java.io.File;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import java.io.IOException;
import hellfirepvp.astralsorcery.AstralSorcery;
import com.google.common.io.Files;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.UUID;
import java.util.Map;
import java.util.TimerTask;
import java.util.Timer;
import hellfirepvp.astralsorcery.common.util.ServerLifecycleListener;

public class ResearchIOThread implements ServerLifecycleListener
{
    private static final ResearchIOThread instance;
    private static Timer timer;
    private static TimerTask saveTask;
    private final Map<UUID, PlayerProgress> playerSaveQueue;
    private final Map<UUID, PlayerProgress> awaitingSaveQueue;
    private boolean inSave;
    private boolean skipTick;
    
    private ResearchIOThread() {
        this.playerSaveQueue = Maps.newHashMap();
        this.awaitingSaveQueue = Maps.newHashMap();
        this.inSave = false;
        this.skipTick = false;
    }
    
    public static ResearchIOThread getInstance() {
        return ResearchIOThread.instance;
    }
    
    @Override
    public void onServerStart() {
        this.reset();
        ResearchIOThread.saveTask = new TimerTask() {
            @Override
            public void run() {
                ResearchIOThread.instance.doSave();
            }
        };
        (ResearchIOThread.timer = new Timer("ResearchIOThread", true)).scheduleAtFixedRate(ResearchIOThread.saveTask, 30000L, 30000L);
    }
    
    @Override
    public void onServerStop() {
        this.flushAndSaveAll();
        this.reset();
    }
    
    private void reset() {
        if (ResearchIOThread.saveTask != null) {
            ResearchIOThread.saveTask.cancel();
            ResearchIOThread.saveTask = null;
        }
        if (ResearchIOThread.timer != null) {
            ResearchIOThread.timer.cancel();
            ResearchIOThread.timer = null;
        }
    }
    
    public void doSave() {
        if (this.skipTick) {
            return;
        }
        this.inSave = true;
        for (final Map.Entry<UUID, PlayerProgress> entry : this.playerSaveQueue.entrySet()) {
            saveNow(entry.getKey(), entry.getValue());
        }
        this.playerSaveQueue.clear();
        this.inSave = false;
        this.playerSaveQueue.putAll(this.awaitingSaveQueue);
        this.awaitingSaveQueue.clear();
    }
    
    private void scheduleSave(final UUID playerUUID, final PlayerProgress copiedProgress) {
        if (this.inSave) {
            this.awaitingSaveQueue.put(playerUUID, copiedProgress);
        }
        else {
            this.playerSaveQueue.put(playerUUID, copiedProgress);
        }
    }
    
    private void cancelScheduledSave(final UUID playerUUID) {
        this.awaitingSaveQueue.remove(playerUUID);
        this.playerSaveQueue.remove(playerUUID);
    }
    
    private void flushAndSaveAll() {
        this.skipTick = true;
        this.playerSaveQueue.putAll(this.awaitingSaveQueue);
        for (final Map.Entry<UUID, PlayerProgress> entry : this.playerSaveQueue.entrySet()) {
            saveNow(entry.getKey(), entry.getValue());
        }
        this.playerSaveQueue.clear();
        this.awaitingSaveQueue.clear();
        this.skipTick = false;
        this.inSave = false;
    }
    
    public static void saveProgress(final UUID playerUUID, final PlayerProgress copiedProgress) {
        if (ResearchIOThread.instance != null) {
            ResearchIOThread.instance.scheduleSave(playerUUID, copiedProgress);
        }
    }
    
    public static void cancelSave(final UUID playerUUID) {
        if (ResearchIOThread.instance != null) {
            ResearchIOThread.instance.cancelScheduledSave(playerUUID);
        }
    }
    
    static void saveNow(final UUID playerUUID, final PlayerProgress progress) {
        final File playerFile = ResearchHelper.getPlayerFile(playerUUID);
        try {
            Files.copy(playerFile, ResearchHelper.getPlayerBackupFile(playerUUID));
        }
        catch (final IOException exc) {
            AstralSorcery.log.warn("Failed copying progress file contents to backup file!");
            exc.printStackTrace();
        }
        try {
            final CompoundTag cmp = new CompoundTag();
            progress.store(cmp);
            NbtIo.func_74795_b(cmp, playerFile);
        }
        catch (final IOException ex) {}
    }
    
    static {
        instance = new ResearchIOThread();
    }
}
