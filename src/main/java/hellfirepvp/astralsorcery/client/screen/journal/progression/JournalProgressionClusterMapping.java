package hellfirepvp.astralsorcery.client.screen.journal.progression;

import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import java.util.HashMap;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import java.util.Map;

public class JournalProgressionClusterMapping
{
    private static final Map<ResearchProgression, JournalCluster> clusterMapping;
    
    public static void register(final ResearchProgression prog, final JournalCluster cluster) {
        JournalProgressionClusterMapping.clusterMapping.put(prog, cluster);
    }
    
    @Nonnull
    public static JournalCluster getClusterMapping(final ResearchProgression progression) {
        final JournalCluster cluster = JournalProgressionClusterMapping.clusterMapping.get(progression);
        if (cluster == null) {
            throw new IllegalArgumentException("ResearchProgression " + progression.name() + " has no registered cluster!");
        }
        return cluster;
    }
    
    static {
        clusterMapping = new HashMap<ResearchProgression, JournalCluster>();
        AbstractRenderableTexture tex = TexturesAS.TEX_GUI_CLUSTER_DISCOVERY;
        register(ResearchProgression.DISCOVERY, new JournalCluster(tex, tex, -2, -2, 0, 0));
        tex = TexturesAS.TEX_GUI_CLUSTER_BASICCRAFT;
        register(ResearchProgression.BASIC_CRAFT, new JournalCluster(tex, tex, 0, 1, 3, 3));
        tex = TexturesAS.TEX_GUI_CLUSTER_ATTUNEMENT;
        register(ResearchProgression.ATTUNEMENT, new JournalCluster(tex, tex, 2, -2, 4, 0));
        tex = TexturesAS.TEX_GUI_CLUSTER_CONSTELLATION;
        register(ResearchProgression.CONSTELLATION, new JournalCluster(tex, tex, 4, 0, 7, 2));
        tex = TexturesAS.TEX_GUI_CLUSTER_RADIANCE;
        register(ResearchProgression.RADIANCE, new JournalCluster(tex, tex, 5, -3, 8, -1));
        tex = TexturesAS.TEX_GUI_CLUSTER_BRILLIANCE;
        register(ResearchProgression.BRILLIANCE, new JournalCluster(tex, tex, 8, -1, 10, 1));
    }
}
