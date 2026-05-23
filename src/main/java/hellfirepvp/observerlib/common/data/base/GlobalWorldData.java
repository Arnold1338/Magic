package hellfirepvp.observerlib.common.data.base;

import com.google.common.io.Files;
import hellfirepvp.observerlib.ObserverLib;
import hellfirepvp.observerlib.common.data.CachedWorldData;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;

public abstract class GlobalWorldData extends CachedWorldData {
    private boolean dirty = false;
    private final String saveFileName;

    protected GlobalWorldData(WorldCacheDomain.SaveKey<?> key) {
        super(key); this.saveFileName = key.getIdentifier() + ".dat";
    }

    public final void markDirty() { this.dirty = true; }

    private File getSaveFile(File directory) { return directory.toPath().resolve(saveFileName).toFile(); }

    @Override
    public final void writeData(File baseDirectory, File backupDirectory) throws IOException {
        if (!baseDirectory.exists()) baseDirectory.mkdirs();
        if (!backupDirectory.exists()) backupDirectory.mkdirs();
        File saveFile = getSaveFile(baseDirectory);
        if (saveFile.exists()) {
            try { Files.copy(saveFile, getSaveFile(backupDirectory)); }
            catch (Exception exc) { ObserverLib.log.info("Copying '" + getSaveKey().getIdentifier() + "' to backup failed!"); }
        } else saveFile.createNewFile();
        CompoundTag data = new CompoundTag();
        readIO(() -> writeToNBT(data));
        NbtIo.writeCompressed(data, saveFile);
    }

    @Override
    public final void readData(File baseDirectory) throws IOException {
        writeIO(() -> readFromNBT(NbtIo.readCompressed(getSaveFile(baseDirectory))));
    }

    public abstract void writeToNBT(CompoundTag tag);
    public abstract void readFromNBT(CompoundTag tag);

    @Override public final boolean needsSaving() { return dirty; }
    @Override public final void markSaved() { dirty = false; }
}
