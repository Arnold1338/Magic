package hellfirepvp.astralsorcery.client.util;

import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.phys.Vec3;
import hellfirepvp.astralsorcery.client.util.draw.RenderInfo;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

public class RenderingVectorUtils
{
    public static Vector3 getStandardTranslationRemovalVector(final float partialTicks) {
        final Vec3 view = RenderInfo.getInstance().getARI().func_216785_c();
        return new Vector3(view);
    }
    
    public static Vector3 interpolatePosition(final Entity e, final float partialTicks) {
        return new Vector3(interpolate(e.field_70169_q, e.func_226277_ct_(), partialTicks), interpolate(e.field_70167_r, e.func_226278_cu_(), partialTicks), interpolate(e.field_70166_s, e.func_226281_cx_(), partialTicks));
    }
    
    public static Vector3 interpolate(final Vector3 oldV, final Vector3 newV, final float partialTicks) {
        return new Vector3(interpolate(oldV.getX(), newV.getX(), partialTicks), interpolate(oldV.getY(), newV.getY(), partialTicks), interpolate(oldV.getZ(), newV.getZ(), partialTicks));
    }
    
    public static double interpolate(final double oldP, final double newP, final float partialTicks) {
        if (oldP == newP) {
            return oldP;
        }
        return oldP + (newP - oldP) * partialTicks;
    }
    
    public static float interpolate(final float oldP, final float newP, final float partialTicks) {
        if (oldP == newP) {
            return oldP;
        }
        return oldP + (newP - oldP) * partialTicks;
    }
    
    public static float interpolateRotation(final float prevRotation, final float nextRotation, final float partialTick) {
        float rot;
        for (rot = nextRotation - prevRotation; rot >= 180.0f; rot -= 360.0f) {}
        while (rot >= 180.0f) {
            rot -= 360.0f;
        }
        return prevRotation + partialTick * rot;
    }
}
