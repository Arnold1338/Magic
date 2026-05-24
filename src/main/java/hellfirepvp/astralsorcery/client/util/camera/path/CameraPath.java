package hellfirepvp.astralsorcery.client.util.camera.path;

import java.util.Iterator;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.client.util.camera.EntityClientReplacement;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.util.camera.ICameraStopListener;
import hellfirepvp.astralsorcery.client.util.camera.ICameraTickListener;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.client.util.camera.ICameraPersistencyFunction;
import hellfirepvp.astralsorcery.client.util.camera.EntityCameraRenderView;

public class CameraPath extends EntityCameraRenderView implements ICameraPersistencyFunction
{
    LinkedList<PathPoint> pathPoints;
    private final Vector3 startVector;
    private final Vector3 focus;
    private ICameraTickListener delegate;
    private ICameraStopListener stopDelegate;
    private int totalTickDuration;
    private boolean expired;
    private boolean stopped;
    
    CameraPath(final Vector3 startPoint, final Vector3 focusPoint, @Nullable final ICameraTickListener tick) {
        this.pathPoints = new LinkedList<PathPoint>();
        this.totalTickDuration = 0;
        this.expired = false;
        this.stopped = false;
        this.startVector = startPoint;
        this.focus = focusPoint;
        this.func_226288_n_(startPoint.getX(), startPoint.getY(), startPoint.getZ());
        this.field_70169_q = this.func_226277_ct_();
        this.field_70167_r = this.func_226278_cu_();
        this.field_70166_s = this.func_226281_cx_();
        this.delegate = tick;
        this.setCameraFocus(focusPoint);
        this.transformToFocusOnPoint(focusPoint, 0.0f, false);
    }
    
    public void setTickListener(final ICameraTickListener delegate) {
        this.delegate = delegate;
    }
    
    public void setStopListener(final ICameraStopListener delegate) {
        this.stopDelegate = delegate;
    }
    
    void addPoint(final Vector3 point, final int ticks) {
        this.pathPoints.addLast(new PathPoint(point, ticks));
        this.totalTickDuration += ticks;
    }
    
    @Override
    public void moveEntityTick(final EntityCameraRenderView entity, final EntityClientReplacement replacement, final int ticksExisted) {
        if (this.delegate != null) {
            this.delegate.onCameraTick(entity, replacement);
        }
        this.setCameraFocus(Vector3.atEntityCorner((Entity)replacement));
        this.expired = (this.field_70173_aa > this.totalTickDuration);
        if (this.pathPoints.isEmpty()) {
            this.expired = true;
        }
        else {
            final Vector3 position = this.queryByTicks(ticksExisted);
            this.field_70169_q = this.func_226277_ct_();
            this.field_70167_r = this.func_226278_cu_();
            this.field_70166_s = this.func_226281_cx_();
            this.func_226288_n_(position.getX(), position.getY(), position.getZ());
        }
    }
    
    @Override
    public void onStopTransforming() {
        if (this.stopDelegate != null && Minecraft.getInstance().field_71441_e != null) {
            this.stopDelegate.onCameraStop();
        }
    }
    
    private Vector3 queryByTicks(final int ticks) {
        if (ticks <= 0) {
            return this.startVector;
        }
        int acc = 0;
        PathPoint current = null;
        for (final PathPoint point : this.pathPoints) {
            final int accumulator = acc + point.ticksToGetThere;
            final Vector3 prev = (current == null) ? this.startVector : current.dstPoint;
            current = point;
            if (accumulator >= ticks) {
                final int interp = current.ticksToGetThere - (accumulator - ticks);
                final int dstJump = current.ticksToGetThere;
                return current.dstPoint.clone().subtract(prev).divide(dstJump).multiply(Mth.func_76125_a(interp, 1, dstJump)).add(prev);
            }
            acc = accumulator;
        }
        return this.pathPoints.getLast().dstPoint;
    }
    
    @Override
    public boolean isExpired() {
        return this.expired;
    }
    
    @Override
    public void setExpired() {
        this.expired = true;
    }
    
    @Override
    public void forceStop() {
        this.stopped = true;
        this.setExpired();
    }
    
    @Override
    public boolean wasForciblyStopped() {
        return this.stopped;
    }
    
    CameraPath copy() {
        final CameraPath c = new CameraPath(this.startVector, this.focus, this.delegate);
        for (final PathPoint fp : this.pathPoints) {
            c.pathPoints.addLast(new PathPoint(fp.dstPoint, fp.ticksToGetThere));
        }
        c.totalTickDuration = this.totalTickDuration;
        return c;
    }
    
    private static class PathPoint
    {
        private final Vector3 dstPoint;
        private final int ticksToGetThere;
        
        PathPoint(final Vector3 dstPoint, final int ticksToGetThere) {
            this.dstPoint = dstPoint;
            this.ticksToGetThere = ticksToGetThere;
        }
    }
}
