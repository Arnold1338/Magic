package hellfirepvp.astralsorcery.client.util.camera;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ClientCameraUtil
{
    public static void positionCamera(final Player renderView, final float pTicks, final double x, final double y, final double z, final double prevX, final double prevY, final double prevZ, final double yaw, double yawPrev, final double pitch, final double pitchPrev) {
        double dYaw = Mth.func_191273_b(yaw - yawPrev, 360.0);
        if (dYaw > 180.0) {
            dYaw -= 360.0;
        }
        yawPrev = yaw - dYaw;
        final float iYaw = Mth.func_219799_g(pTicks, (float)yawPrev, (float)yaw);
        final float iPitch = Mth.func_219799_g(pTicks, (float)pitchPrev, (float)pitch);
        final Minecraft mc = Minecraft.getInstance();
        Entity rv = mc.func_175606_aa();
        if (rv == null || !rv.equals((Object)renderView)) {
            mc.func_175607_a((Entity)renderView);
            rv = (Entity)renderView;
        }
        Player render = (Player)rv;
        render.func_226288_n_(x, y, z);
        render.field_70169_q = prevX;
        render.field_70167_r = prevY;
        render.field_70166_s = prevZ;
        render.field_70142_S = prevX;
        render.field_70137_T = prevY;
        render.field_70136_U = prevZ;
        render.yRot = iYaw;
        render.field_70126_B = iYaw;
        render.field_70759_as = iYaw;
        render.field_70758_at = iYaw;
        render.field_71109_bG = iYaw;
        render.field_71107_bF = iYaw;
        render.field_70761_aq = iYaw;
        render.field_70760_ar = iYaw;
        render.xRot = iPitch;
        render.field_70127_C = iPitch;
        render = (Player)Minecraft.getInstance().player;
        render.func_226288_n_(x, y, z);
        render.field_70169_q = prevX;
        render.field_70167_r = prevY;
        render.field_70166_s = prevZ;
        render.field_70142_S = prevX;
        render.field_70137_T = prevY;
        render.field_70136_U = prevZ;
        render.yRot = iYaw;
        render.field_70126_B = iYaw;
        render.field_70759_as = iYaw;
        render.field_70758_at = iYaw;
        render.field_71109_bG = iYaw;
        render.field_71107_bF = iYaw;
        render.field_70761_aq = iYaw;
        render.field_70760_ar = iYaw;
        render.xRot = iPitch;
        render.field_70127_C = iPitch;
    }
    
    public static void resetCamera() {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            final Player player = (Player)mc.player;
            mc.func_175607_a((Entity)player);
            if (mc.gui != null) {
                mc.func_147108_a((Screen)null);
            }
        }
    }
}
