package hellfirepvp.astralsorcery.client.screen.journal.perk;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.client.screen.helper.SizeHandler;

public class PerkTreeSizeHandler extends SizeHandler
{
    public PerkTreeSizeHandler() {
        this.setWidthHeightNodes(10.0f);
        this.setSpaceBetweenNodes(10.0f);
    }
    
    @Nullable
    @Override
    public float[] buildRequiredRectangle() {
        float leftMost = 0.0f;
        float rightMost = 0.0f;
        float upperMost = 0.0f;
        float lowerMost = 0.0f;
        for (final PerkTreePoint<?> point : PerkTree.PERK_TREE.getPerkPoints(LogicalSide.CLIENT)) {
            final Point2D.Float offset = point.getOffset();
            final float x = offset.x;
            final float y = offset.y;
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
