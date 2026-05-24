package hellfirepvp.astralsorcery.client.screen.journal.progression;

import javax.annotation.Nullable;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.client.screen.helper.SizeHandler;

public class ProgressionSizeHandler extends SizeHandler
{
    private final ResearchProgression part;
    
    public ProgressionSizeHandler(final ResearchProgression part) {
        this.part = part;
    }
    
    @Nullable
    @Override
    public float[] buildRequiredRectangle() {
        float leftMost = 0.0f;
        float rightMost = 0.0f;
        float upperMost = 0.0f;
        float lowerMost = 0.0f;
        for (final ResearchNode node : this.part.getResearchNodes()) {
            final float x = node.renderPosX;
            final float y = node.renderPosZ;
            if (x < leftMost) {
                leftMost = x;
            }
            if (x > rightMost) {
                rightMost = x;
            }
            if (y > lowerMost) {
                lowerMost = y;
            }
            if (y < upperMost) {
                upperMost = y;
            }
        }
        return new float[] { leftMost, rightMost, upperMost, lowerMost };
    }
}
