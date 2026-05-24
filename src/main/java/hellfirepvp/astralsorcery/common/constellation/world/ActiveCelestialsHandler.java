package hellfirepvp.astralsorcery.common.constellation.world;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.List;
import java.util.Random;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Map;
import java.util.LinkedList;

public class ActiveCelestialsHandler
{
    public static LinkedList<RenderPosition> availablePositions;
    private final Map<IConstellation, RenderPosition> activePositions;
    
    public ActiveCelestialsHandler() {
        this.activePositions = new HashMap<IConstellation, RenderPosition>();
    }
    
    public void updatePositions(final LinkedList<IConstellation> activeConstellations) {
        this.activePositions.clear();
        for (int i = 0; i < Math.min(activeConstellations.size(), ActiveCelestialsHandler.availablePositions.size()); ++i) {
            this.activePositions.put(activeConstellations.get(i), ActiveCelestialsHandler.availablePositions.get(i));
        }
    }
    
    public Map<IConstellation, RenderPosition> getCurrentRenderPositions() {
        return Collections.unmodifiableMap((Map<? extends IConstellation, ? extends RenderPosition>)this.activePositions);
    }
    
    public Collection<IConstellation> getActiveConstellations() {
        return Collections.unmodifiableCollection((Collection<? extends IConstellation>)this.activePositions.keySet());
    }
    
    static {
        (ActiveCelestialsHandler.availablePositions = new LinkedList<RenderPosition>()).add(RenderPosition.createRenderInfoFor(0.9, -0.4, 0.0, 25.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(1.0, -0.42, 0.9, 30.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(0.0, -0.43, 1.0, 25.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(-0.8, -0.44, 1.0, 30.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(-1.1, -0.41, 0.0, 25.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(-1.0, -0.46, -0.9, 30.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(0.0, -0.38, -1.0, 25.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(1.1, -0.43, -0.9, 30.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(0.6, -0.62, -0.1, 15.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(0.5, -0.64, 0.4, 20.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(0.1, -0.62, 0.6, 15.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(-0.4, -0.66, 0.5, 20.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(-0.5, -0.62, 0.1, 15.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(-0.6, -0.67, -0.4, 20.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(0.0, -0.62, -0.5, 15.0));
        ActiveCelestialsHandler.availablePositions.add(RenderPosition.createRenderInfoFor(0.4, -0.62, -0.6, 20.0));
        Collections.shuffle(ActiveCelestialsHandler.availablePositions, new Random(6886440415996285987L));
    }
    
    public static class RenderPosition
    {
        public final Vector3 offset;
        public final Vector3 incU;
        public final Vector3 incV;
        
        RenderPosition(final Vector3 offsetVecUV00, final Vector3 vecUV10, final Vector3 vecUV01) {
            this.offset = offsetVecUV00;
            this.incU = vecUV10;
            this.incV = vecUV01;
        }
        
        static RenderPosition createRenderInfoFor(final double x, final double y, final double z, final double rSize) {
            final double modSize = 0.5 * rSize;
            final double fx = x * 100.0;
            final double fy = y * 100.0;
            final double fz = z * 100.0;
            final double d8 = Math.atan2(x, z);
            final double d9 = Math.sin(d8);
            final double d10 = Math.cos(d8);
            final double d11 = Math.atan2(Math.sqrt(x * x + z * z), y);
            final double d12 = Math.sin(d11);
            final double d13 = Math.cos(d11);
            double d14 = modSize * d12;
            double d15 = -(modSize * d13);
            double d16 = d15 * d9 - modSize * d10;
            double d17 = modSize * d9 + d15 * d10;
            final Vector3 vecUV00 = new Vector3(fx + d16, fy + d14, fz + d17);
            d14 = -(modSize * d12);
            d15 = modSize * d13;
            d16 = d15 * d9 - modSize * d10;
            d17 = modSize * d9 + d15 * d10;
            final Vector3 vecUV2 = new Vector3(fx + d16, fy + d14, fz + d17);
            d14 = modSize * d12;
            d15 = -(modSize * d13);
            d16 = d15 * d9 + modSize * d10;
            d17 = -(modSize * d9) + d15 * d10;
            final Vector3 vecUV3 = new Vector3(fx + d16, fy + d14, fz + d17);
            return new RenderPosition(vecUV00, vecUV2, vecUV3);
        }
    }
}
