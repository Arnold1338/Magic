package hellfirepvp.observerlib.common.data.base;

import com.google.common.io.Files;
import hellfirepvp.observerlib.ObserverLib;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.observerlib.common.data.CachedWorldData;
import hellfirepvp.observerlib.common.util.AlternatingSet;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public abstract class SectionWorldData<T extends WorldSection> extends CachedWorldData {
    public static final int PRECISION_REGION = 9;
    public static final int PRECISION_AREA = 6;
    public static final int PRECISION_SECTION = 5;
    public static final int PRECISION_CHUNK = 4;

    private final Map<SectionKey, T> sections = new HashMap<>();
    private final int precision;
    private final AlternatingSet<SectionKey> dirtySections = new AlternatingSet<>();
    private final Set<SectionKey> removedSections = new HashSet<>();

    protected SectionWorldData(WorldCacheDomain.SaveKey<?> key, int sectionPrecision) {
        super(key); this.precision = sectionPrecision;
    }

    public void markDirty(Vec3i absolute) {
        SectionKey key = resolve(absolute, precision);
        T section = getSection(key);
        if (section != null) write(() -> dirtySections.add(key));
    }

    public void markDirty(T section) { write(() -> dirtySections.add(from(section))); }

    protected abstract T createNewSection(int sectionX, int sectionZ);

    @Nonnull public Collection<T> getSections(Vec3i absoluteMin, Vec3i absoluteMax) {
        return resolveSections(absoluteMin, absoluteMax, this::getSection);
    }
    @Nonnull public Collection<T> getOrCreateSections(Vec3i absoluteMin, Vec3i absoluteMax) {
        return resolveSections(absoluteMin, absoluteMax, this::getOrCreateSection);
    }
    @Nonnull private Collection<T> resolveSections(Vec3i absoluteMin, Vec3i absoluteMax, Function<SectionKey, T> sectionFct) {
        SectionKey lower = resolve(absoluteMin, precision), higher = resolve(absoluteMax, precision);
        Collection<T> out = new HashSet<>();
        for (int xx = lower.x; xx <= higher.x; xx++)
            for (int zz = lower.z; zz <= higher.z; zz++) { T s = sectionFct.apply(new SectionKey(xx, zz)); if (s != null) out.add(s); }
        return out;
    }
    @Nonnull public T getOrCreateSection(Vec3i absolute) { return getOrCreateSection(resolve(absolute, precision)); }
    @Nonnull private T getOrCreateSection(SectionKey key) { return write(() -> sections.computeIfAbsent(key, k -> createNewSection(k.x, k.z))); }
    @Nullable public T getSection(Vec3i absolute) { return getSection(resolve(absolute, precision)); }
    @Nullable private T getSection(SectionKey key) { return read(() -> sections.get(key)); }

    public boolean removeSection(T section) { SectionKey k = from(section); return sections.remove(k) == section && removedSections.add(k); }
    public boolean removeSection(Vec3i absolute) { SectionKey k = resolve(absolute, precision); return sections.remove(k) != null && removedSections.add(k); }
    @Nonnull public Collection<T> getSections() { return sections.values(); }

    @Override public boolean needsSaving() { return !dirtySections.isEmpty(); }
    @Override public void markSaved() { write(() -> dirtySections.clear()); }

    public abstract void writeToNBT(CompoundTag tag);
    public abstract void readFromNBT(CompoundTag tag);

    private File getSaveFile(File directory, T section) {
        return directory.toPath().resolve(String.format("%s_%s_%s.dat", getSaveKey().getIdentifier(), section.getSectionX(), section.getSectionZ())).toFile();
    }

    @Override
    public final void writeData(File baseDirectory, File backupDirectory) throws IOException {
        if (!baseDirectory.exists()) baseDirectory.mkdirs();
        if (!backupDirectory.exists()) backupDirectory.mkdirs();
        File generalSaveFile = new File(baseDirectory, "general.dat");
        if (generalSaveFile.exists()) {
            try { Files.copy(generalSaveFile, new File(backupDirectory, "general.dat")); }
            catch (Exception exc) { ObserverLib.log.info("Copying '" + getSaveKey().getIdentifier() + "' general file to backup failed!"); }
        } else generalSaveFile.createNewFile();
        CompoundTag generalData = new CompoundTag();
        readIO(() -> writeToNBT(generalData));
        NbtIo.writeCompressed(generalData, generalSaveFile);
        Set<SectionKey> sectionsToSave = new HashSet<>();
        dirtySections.forEach(k -> { sectionsToSave.add(k); return false; });
        for (SectionKey sectionKey : sectionsToSave) {
            T section = getSection(sectionKey);
            if (section != null) {
                File saveFile = getSaveFile(baseDirectory, section);
                if (saveFile.exists()) {
                    try { Files.copy(saveFile, getSaveFile(backupDirectory, section)); }
                    catch (Exception exc) { ObserverLib.log.info("Copying '" + getSaveKey().getIdentifier() + "' file to backup failed!"); }
                } else saveFile.createNewFile();
                CompoundTag data = new CompoundTag();
                readIO(() -> section.writeToNBT(data));
                NbtIo.writeCompressed(data, saveFile);
            }
        }
    }

    @Override
    public final void readData(File baseDirectory) throws IOException {
        String identifier = getSaveKey().getIdentifier();
        File generalSaveFile = new File(baseDirectory, "general.dat");
        if (generalSaveFile.exists()) { CompoundTag tag = NbtIo.readCompressed(generalSaveFile); writeIO(() -> readFromNBT(tag)); }
        else writeIO(() -> readFromNBT(new CompoundTag()));
        File[] files = baseDirectory.listFiles();
        if (files == null) return;
        for (File subFile : files) {
            String fileName = subFile.getName();
            if (!fileName.endsWith(".dat")) continue;
            fileName = fileName.substring(0, fileName.length() - 4);
            String[] ptrn = fileName.split("_");
            if (ptrn.length != 3 || !ptrn[0].equalsIgnoreCase(identifier)) continue;
            int sX, sZ;
            try { sX = Integer.parseInt(ptrn[1]); sZ = Integer.parseInt(ptrn[2]); } catch (NumberFormatException e) { continue; }
            writeIO(() -> { T section = createNewSection(sX, sZ); section.readFromNBT(NbtIo.readCompressed(subFile)); sections.put(new SectionKey(sX, sZ), section); });
        }
    }

    private static SectionKey from(WorldSection section) { return new SectionKey(section.getSectionX(), section.getSectionZ()); }
    private static SectionKey resolve(Vec3i absolute, int shift) { return new SectionKey(absolute.getX() >> shift, absolute.getZ() >> shift); }

    private static class SectionKey {
        final int x, z;
        SectionKey(int x, int z) { this.x = x; this.z = z; }
        @Override public boolean equals(Object o) { if (!(o instanceof SectionKey)) return false; SectionKey s = (SectionKey)o; return x == s.x && z == s.z; }
        @Override public int hashCode() { return Objects.hash(x, z); }
    }
}
