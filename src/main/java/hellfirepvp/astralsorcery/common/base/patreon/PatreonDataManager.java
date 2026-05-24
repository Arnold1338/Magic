package hellfirepvp.astralsorcery.common.base.patreon;

import com.google.gson.GsonBuilder;
import java.util.Iterator;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import java.util.UUID;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.net.URL;
import com.google.gson.Gson;

public class PatreonDataManager
{
    private static final String PATREON_EFFECT_URL = "http://hellfiredev.net/patreon.json";
    private static final Gson GSON;
    
    public static void loadPatreonEffects() {
        final Thread tr = new Thread(() -> {
            URLConnection conn;
            try {
                conn = new URL("http://hellfiredev.net/patreon.json").openConnection();
            }
            catch (final IOException e) {
                AstralSorcery.log.error("Failed to connect to patreon fileserver! Not loading patreon files...");
                e.printStackTrace();
                PatreonEffectHelper.loadingFinished = true;
                return;
            }
            PatreonData data;
            try {
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final BufferedReader bufferedReader;
                final BufferedReader br = bufferedReader;
                try {
                    data = (PatreonData)PatreonDataManager.GSON.fromJson((Reader)br, (Class)PatreonData.class);
                }
                catch (final Throwable t) {
                    throw t;
                }
                finally {
                    if (br != null) {
                        final Throwable t2;
                        if (t2 != null) {
                            try {
                                br.close();
                            }
                            catch (final Throwable exception) {
                                t2.addSuppressed(exception);
                            }
                        }
                        else {
                            br.close();
                        }
                    }
                }
            }
            catch (final IOException e2) {
                AstralSorcery.log.error("Failed to connect to patreon fileserver! Not loading patreon files...");
                e2.printStackTrace();
                PatreonEffectHelper.loadingFinished = true;
                return;
            }
            int skipped = 0;
            data.getEffectList().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final PatreonData.EffectEntry entry = iterator.next();
                UUID plUuid;
                PatreonEffectType type;
                try {
                    plUuid = UUID.fromString(entry.getUuid());
                    type = PatreonEffectType.valueOf(entry.getEffectClass());
                }
                catch (final Exception exc) {
                    ++skipped;
                    continue;
                }
                try {
                    final PatreonEffect pe = (PatreonEffect)type.getProvider().buildEffect(plUuid, entry.getParameters());
                    pe.initialize();
                    pe.attachEventListeners(MinecraftForge.EVENT_BUS);
                    pe.attachTickListeners(AstralSorcery.getProxy().getTickManager()::register);
                    PatreonEffectHelper.playerEffectMap.computeIfAbsent(plUuid, uuid -> new ArrayList()).add(pe);
                    PatreonEffectHelper.effectMap.put(pe.getEffectUUID(), pe);
                }
                catch (final Exception exc2) {
                    ++skipped;
                }
            }
            if (skipped > 0) {
                AstralSorcery.log.warn("Skipped " + skipped + " patreon effects during loading due to malformed data!");
            }
            AstralSorcery.log.info("Patreon effect loading finished.");
            final UUID hellfire = UUID.fromString("7f6971c5-fb58-4519-a975-b1b5766e92d1");
            PatreonEffectHelper.loadingFinished = true;
            return;
        });
        tr.setName("AstralSorcery Patreon Effect Loader");
        tr.start();
    }
    
    static {
        GSON = new GsonBuilder().create();
    }
}
