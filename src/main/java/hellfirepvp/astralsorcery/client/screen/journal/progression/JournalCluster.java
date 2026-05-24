package hellfirepvp.astralsorcery.client.screen.journal.progression;

import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import java.awt.Rectangle;

public class JournalCluster extends Rectangle
{
    public final AbstractRenderableTexture cloudTexture;
    public final AbstractRenderableTexture clusterBackgroundTexture;
    public int maxX;
    public int maxY;
    
    public JournalCluster(final AbstractRenderableTexture cloudTexture, final AbstractRenderableTexture clusterBackgroundTexture, final int leftMost, final int upperMost, final int rightMost, final int lowerMost) {
        super(leftMost, upperMost, rightMost - leftMost, lowerMost - upperMost);
        this.cloudTexture = cloudTexture;
        this.clusterBackgroundTexture = clusterBackgroundTexture;
        this.maxX = rightMost;
        this.maxY = lowerMost;
    }
}
