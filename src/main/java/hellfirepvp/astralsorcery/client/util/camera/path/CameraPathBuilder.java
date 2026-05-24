package hellfirepvp.astralsorcery.client.util.camera.path;

import hellfirepvp.astralsorcery.client.util.camera.ClientCameraManager;
import hellfirepvp.astralsorcery.client.util.camera.ICameraPersistencyFunction;
import hellfirepvp.astralsorcery.client.util.camera.EntityCameraRenderView;
import hellfirepvp.astralsorcery.client.util.camera.CameraTransformerPlayerFocus;
import hellfirepvp.astralsorcery.client.util.camera.ICameraTransformer;
import hellfirepvp.astralsorcery.client.util.camera.ICameraStopListener;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.util.camera.ICameraTickListener;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

public class CameraPathBuilder
{
    private final CameraPath path;
    
    private CameraPathBuilder(final Vector3 start, final Vector3 cameraFocus) {
        this.path = new CameraPath(start, cameraFocus, null);
    }
    
    public CameraPathBuilder(final CameraPath path) {
        this.path = path;
    }
    
    public static CameraPathBuilder builder(final Vector3 start, final Vector3 cameraFocus) {
        return new CameraPathBuilder(start, cameraFocus);
    }
    
    public CameraPathBuilder addPoint(final Vector3 nextPoint, final int ticksToFlyThere) {
        if (ticksToFlyThere < 0) {
            AstralSorcery.log.warn("Tried to add a point with negative tick-timespan to a camera flight. Skipping...");
            return this;
        }
        this.path.addPoint(nextPoint, ticksToFlyThere);
        return this;
    }
    
    public CameraPathBuilder addCircularPoints(final Vector3 centerOffset, final double radius, final int amountOfPointsOnCircle, final int ticksBetweenEachPoint) {
        return this.addCircularPoints(centerOffset, deg -> radius, amountOfPointsOnCircle, ticksBetweenEachPoint);
    }
    
    public CameraPathBuilder addCircularPoints(final Vector3 centerOffset, final DynamicRadiusGetter radiusFn, final int amountOfPointsOnCircle, final int ticksBetweenEachPoint) {
        if (ticksBetweenEachPoint < 0) {
            AstralSorcery.log.warn("Tried to add a point with negative tick-timespan to a camera flight. Skipping...");
            return this;
        }
        final double degPerPoint = 360.0 / amountOfPointsOnCircle;
        for (int i = 0; i < amountOfPointsOnCircle; ++i) {
            final double deg = i * degPerPoint;
            final Vector3 point = Vector3.RotAxis.Y_AXIS.clone().perpendicular().normalize().multiply(radiusFn.getRadius(deg)).rotate(Math.toRadians(deg), Vector3.RotAxis.Y_AXIS).add(centerOffset);
            this.addPoint(point, ticksBetweenEachPoint);
        }
        return this;
    }
    
    public CameraPathBuilder copy() {
        return new CameraPathBuilder(this.path.copy());
    }
    
    public CameraPathBuilder setTickDelegate(final ICameraTickListener delegate) {
        this.path.setTickListener(delegate);
        return this;
    }
    
    public CameraPathBuilder setStopDelegate(final ICameraStopListener delegate) {
        this.path.setStopListener(delegate);
        return this;
    }
    
    public ICameraTransformer finishAndStart() {
        if (this.path.pathPoints.size() <= 0) {
            AstralSorcery.log.warn("Tried to start a camera path without any points! Skipping...");
            return null;
        }
        final CameraTransformerPlayerFocus cameraTransformer = new CameraTransformerPlayerFocus(this.path, this.path);
        ClientCameraManager.INSTANCE.addTransformer(cameraTransformer);
        return cameraTransformer;
    }
    
    public interface DynamicRadiusGetter
    {
        double getRadius(final double p0);
        
        default DynamicRadiusGetter dyanmicIncrease(final double base, final double incPerStep) {
            return new DynamicRadiusGetter() {
                private double baseDst = base;
                private int count = 0;
                
                @Override
                public double getRadius(final double degree) {
                    final double rad = this.baseDst + this.count * incPerStep;
                    ++this.count;
                    return rad;
                }
            };
        }
    }
}
