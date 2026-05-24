package hellfirepvp.astralsorcery.client.util.draw;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.level.phys.Vec3;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class RenderInfo implements ITickHandler
{
    private static final RenderInfo instance;
    private float rotationX;
    private float rotationXZ;
    private float rotationZ;
    private float rotationYZ;
    private float rotationXY;
    private Vec3 view;
    
    private RenderInfo() {
        this.view = Vec3.field_186680_a;
    }
    
    public static RenderInfo getInstance() {
        return RenderInfo.instance;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Camera info = this.getARI();
        if (info != null) {
            this.rotationX = Mth.func_76134_b(info.func_216778_f() * 0.017453292f);
            this.rotationZ = Mth.func_76126_a(info.func_216778_f() * 0.017453292f);
            this.rotationYZ = -this.rotationZ * Mth.func_76126_a(info.func_216777_e() * 0.017453292f);
            this.rotationXY = this.rotationX * Mth.func_76126_a(info.func_216777_e() * 0.017453292f);
            this.rotationXZ = Mth.func_76134_b(info.func_216777_e() * 0.017453292f);
        }
    }
    
    public float getRotationX() {
        return this.rotationX;
    }
    
    public float getRotationXZ() {
        return this.rotationXZ;
    }
    
    public float getRotationZ() {
        return this.rotationZ;
    }
    
    public float getRotationYZ() {
        return this.rotationYZ;
    }
    
    public float getRotationXY() {
        return this.rotationXY;
    }
    
    @Nullable
    public Camera getARI() {
        final GameRenderer gr = Minecraft.func_71410_x().field_71460_t;
        if (gr != null) {
            return gr.func_215316_n();
        }
        return null;
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.RENDER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.START;
    }
    
    public String getName() {
        return "RenderInfo Cache Compat";
    }
    
    static {
        instance = new RenderInfo();
    }
}
