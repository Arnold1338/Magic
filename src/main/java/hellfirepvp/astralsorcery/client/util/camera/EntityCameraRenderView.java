package hellfirepvp.astralsorcery.client.util.camera;

import net.minecraft.world.entity.HumanoidArm;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.EquipmentSlot;
import java.util.Collections;
import net.minecraft.world.level.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.player.ClientPlayerEntity;

public abstract class EntityCameraRenderView extends ClientPlayerEntity
{
    private Vector3 cameraFocus;
    
    public EntityCameraRenderView() {
        super(Minecraft.func_71410_x(), Minecraft.func_71410_x().field_71441_e, Minecraft.func_71410_x().field_71439_g.field_71174_a, Minecraft.func_71410_x().field_71439_g.func_146107_m(), Minecraft.func_71410_x().field_71439_g.func_199507_B(), false, false);
        this.cameraFocus = null;
        this.field_71075_bZ.field_75101_c = true;
        this.field_71075_bZ.field_75100_b = true;
        this.field_71075_bZ.field_75102_a = true;
    }
    
    @Nullable
    public Vector3 getCameraFocus() {
        return this.cameraFocus;
    }
    
    public void setCameraFocus(@Nullable final Vector3 cameraFocus) {
        this.cameraFocus = cameraFocus;
    }
    
    public void setAsRenderViewEntity() {
        Minecraft.func_71410_x().func_175607_a((Entity)this);
    }
    
    public void transformToFocusOnPoint(final Vector3 toFocus, final float pTicks, final boolean propagate) {
        final Vector3 angles = Vector3.atEntityCorner((Entity)this).subtract(toFocus).copyToPolar();
        final Vector3 prevAngles = new Vector3(this.field_70169_q, this.field_70167_r, this.field_70166_s).subtract(toFocus).copyToPolar();
        final double pitch = 90.0 - angles.getY();
        final double pitchPrev = 90.0 - prevAngles.getY();
        final double yaw = -angles.getZ();
        final double yawPrev = -prevAngles.getZ();
        if (propagate) {
            ClientCameraUtil.positionCamera((Player)this, pTicks, this.func_226277_ct_(), this.func_226278_cu_(), this.func_226281_cx_(), this.field_70169_q, this.field_70167_r, this.field_70166_s, yaw, yawPrev, pitch, pitchPrev);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_195049_a(final double yaw, final double pitch) {
    }
    
    public abstract void moveEntityTick(final EntityCameraRenderView p0, final EntityClientReplacement p1, final int p2);
    
    public abstract void onStopTransforming();
    
    public boolean func_175149_v() {
        return false;
    }
    
    public boolean func_184812_l_() {
        return false;
    }
    
    public Iterable<ItemStack> func_184193_aE() {
        return (Iterable<ItemStack>)Collections.emptyList();
    }
    
    @Nonnull
    public ItemStack func_184582_a(final EquipmentSlot slotIn) {
        return ItemStack.field_190927_a;
    }
    
    public void func_184201_a(final EquipmentSlot slotIn, @Nullable final ItemStack stack) {
    }
    
    public HumanoidArm func_184591_cq() {
        return HumanoidArm.RIGHT;
    }
}
