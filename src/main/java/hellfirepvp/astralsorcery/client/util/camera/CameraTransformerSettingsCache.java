package hellfirepvp.astralsorcery.client.util.camera;

import net.minecraft.client.GameSettings;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.options.PointOfView;

public abstract class CameraTransformerSettingsCache implements ICameraTransformer
{
    private boolean active;
    private boolean viewBobbing;
    private boolean hideGui;
    private boolean flying;
    private PointOfView thirdPersonView;
    private Vector3 startPosition;
    private float startYaw;
    private float startPitch;
    
    public CameraTransformerSettingsCache() {
        this.active = false;
        this.viewBobbing = false;
        this.hideGui = false;
        this.flying = false;
    }
    
    @Override
    public void onStartTransforming(final float pTicks) {
        final Minecraft mc = Minecraft.func_71410_x();
        this.viewBobbing = mc.field_71474_y.field_74336_f;
        this.hideGui = mc.field_71474_y.field_74319_N;
        this.thirdPersonView = mc.field_71474_y.func_243230_g();
        final Player player = (Player)mc.field_71439_g;
        this.flying = player.field_71075_bZ.field_75100_b;
        this.startPosition = new Vector3(player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_());
        this.startYaw = player.field_70177_z;
        this.startPitch = player.field_70125_A;
        player.func_70016_h(0.0, 0.0, 0.0);
        this.active = true;
    }
    
    @Override
    public void onStopTransforming(final float pTicks) {
        if (this.active) {
            final GameSettings settings = Minecraft.func_71410_x().field_71474_y;
            settings.field_74336_f = this.viewBobbing;
            settings.field_74319_N = this.hideGui;
            settings.func_243229_a(this.thirdPersonView);
            final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
            player.field_71075_bZ.field_75100_b = this.flying;
            player.func_70080_a(this.startPosition.getX(), this.startPosition.getY(), this.startPosition.getZ(), this.startYaw, this.startPitch);
            player.func_70016_h(0.0, 0.0, 0.0);
            this.active = false;
        }
    }
    
    @Override
    public void transformRenderView(final float pTicks) {
        if (!this.active) {
            return;
        }
        final GameSettings settings = Minecraft.func_71410_x().field_71474_y;
        settings.field_74319_N = true;
        settings.field_74336_f = false;
        settings.func_243229_a(PointOfView.THIRD_PERSON_BACK);
        Minecraft.func_71410_x().field_71439_g.field_71075_bZ.field_75100_b = true;
        Minecraft.func_71410_x().field_71439_g.func_70016_h(0.0, 0.0, 0.0);
    }
}
