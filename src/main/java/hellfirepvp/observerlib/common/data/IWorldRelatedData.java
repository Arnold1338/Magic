package hellfirepvp.observerlib.common.data;
import java.io.File;
import java.io.IOException;
public interface IWorldRelatedData {
    WorldCacheDomain.SaveKey<?> getSaveKey();
    void markSaved();
    void writeData(File actual, File backup) throws IOException;
    void readData(File baseDirectory) throws IOException;
}
