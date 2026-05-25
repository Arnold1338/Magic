package hellfirepvp.astralsorcery.client.util.camera;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.player.AbstractClientPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.Minecraft;

public class CameraTransformerPlayerFocus extends CameraTransformerSettingsCache
{
    private final EntityCameraRenderView entity;
    private final ICameraPersistencyFunction func;
    private EntityClientReplacement clientEntity;
    
    public CameraTransformerPlayerFocus(final EntityCameraRenderView renderView, final ICameraPersistencyFunction func) {
        this.entity = renderView;
        this.func = func;
    }
    
    @Override
    public void onStartTransforming(final float pTicks) {
        super.onStartTransforming(pTicks);
        final EntityClientReplacement repl = new EntityClientReplacement();
        repl.func_70020_e(Minecraft.getInstance().player.func_189511_e(new CompoundTag()));
        Minecraft.getInstance().level.func_217408_a(repl.func_145782_y(), (AbstractClientPlayerEntity)repl);
        this.clientEntity = repl;
        this.entity.setAsRenderViewEntity();
    }
    
    @Override
    public void onStopTransforming(final float pTicks) {
        super.onStopTransforming(pTicks);
        final Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            mc.level.func_217413_d(this.clientEntity.func_145782_y());
        }
        if (mc.player != null) {
            final Player player = (Player)mc.player;
            player.func_70080_a(this.clientEntity.getX(), this.clientEntity.getY(), this.clientEntity.getZ(), this.clientEntity.yRot, this.clientEntity.xRot);
            player.setDeltaMovement(0.0, 0.0, 0.0);
        }
        ClientCameraUtil.resetCamera();
        if (mc.level != null) {
            this.entity.onStopTransforming();
        }
    }
    
    @Override
    public void transformRenderView(final float pTicks) {
        super.transformRenderView(pTicks);
        final Vector3 focus = this.entity.getCameraFocus();
        if (focus != null) {
            this.entity.transformToFocusOnPoint(focus, pTicks, true);
        }
    }
    
    @Override
    public ICameraPersistencyFunction getPersistencyFunction() {
        return this.func;
    }
    
    @Override
    public int getPriority() {
        return 0;
    }
    
    @Override
    public void onClientTick() {
        final EntityCameraRenderView entity = this.entity;
        ++entity.field_70173_aa;
        if (this.clientEntity != null) {
            this.entity.moveEntityTick(this.entity, this.clientEntity, this.entity.field_70173_aa);
        }
    }
}
