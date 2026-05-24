package hellfirepvp.astralsorcery.client.screen.journal.progression;

import javax.annotation.Nullable;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.client.screen.helper.SizeHandler;

public class GalaxySizeHandler extends SizeHandler
{
    @Nullable
    @Override
    public float[] buildRequiredRectangle() {
        float leftMost = 0.0f;
        float rightMost = 0.0f;
        float upperMost = 0.0f;
        float lowerMost = 0.0f;
        final PlayerProgress progress = ResearchHelper.getClientProgress();
        for (final ResearchProgression resProgress : progress.getResearchProgression()) {
            final JournalCluster cluster = JournalProgressionClusterMapping.getClusterMapping(resProgress);
            int x = cluster.x;
            int y = cluster.y;
            if (x < leftMost) {
                leftMost = (float)x;
            }
            if (x > rightMost) {
                rightMost = (float)x;
            }
            if (y > lowerMost) {
                lowerMost = (float)y;
            }
            if (y < upperMost) {
                upperMost = (float)y;
            }
            x = cluster.maxX;
            y = cluster.maxY;
            if (x < leftMost) {
                leftMost = (float)x;
            }
            if (x > rightMost) {
                rightMost = (float)x;
            }
            if (y > lowerMost) {
                lowerMost = (float)y;
            }
            if (y < upperMost) {
                upperMost = (float)y;
            }
        }
        return new float[] { leftMost, rightMost, upperMost, lowerMost };
    }
}
