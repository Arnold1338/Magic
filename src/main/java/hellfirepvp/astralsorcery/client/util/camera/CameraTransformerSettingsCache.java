package hellfirepvp.astralsorcery.client.util.camera;

import net.minecraft.client.GameSettings;
import net.minecraft.world.entity.player.Player;
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
        final Minecraft mc = Minecraft.getInstance();
        this.viewBobbing = mc.options.field_74336_f;
        this.hideGui = mc.options.field_74319_N;
        this.thirdPersonView = mc.options.func_243230_g();
        final Player player = (Player)mc.player;
        this.flying = player.field_71075_bZ.field_75100_b;
        this.startPosition = new Vector3(player.getX(), player.getY(), player.getZ());
        this.startYaw = player.yRot;
        this.startPitch = player.xRot;
        player.setDeltaMovement(0.0, 0.0, 0.0);
        this.active = true;
    }
    
    @Override
    public void onStopTransforming(final float pTicks) {
        if (this.active) {
            final GameSettings settings = Minecraft.getInstance().options;
            settings.field_74336_f = this.viewBobbing;
            settings.field_74319_N = this.hideGui;
            settings.func_243229_a(this.thirdPersonView);
            final Player player = (Player)Minecraft.getInstance().player;
            player.field_71075_bZ.field_75100_b = this.flying;
            player.func_70080_a(this.startPosition.getX(), this.startPosition.getY(), this.startPosition.getZ(), this.startYaw, this.startPitch);
            player.setDeltaMovement(0.0, 0.0, 0.0);
            this.active = false;
        }
    }
    
    @Override
    public void transformRenderView(final float pTicks) {
        if (!this.active) {
            return;
        }
        final GameSettings settings = Minecraft.getInstance().options;
        settings.field_74319_N = true;
        settings.field_74336_f = false;
        settings.func_243229_a(PointOfView.THIRD_PERSON_BACK);
        Minecraft.getInstance().player.field_71075_bZ.field_75100_b = true;
        Minecraft.getInstance().player.setDeltaMovement(0.0, 0.0, 0.0);
    }
}
