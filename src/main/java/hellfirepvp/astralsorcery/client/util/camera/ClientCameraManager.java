package hellfirepvp.astralsorcery.client.util.camera;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import java.util.Comparator;
import java.util.TreeSet;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class ClientCameraManager implements ITickHandler
{
    public static final ClientCameraManager INSTANCE;
    private final TreeSet<ICameraTransformer> transformers;
    private ICameraTransformer lastTransformer;
    
    public ClientCameraManager() {
        this.transformers = new TreeSet<ICameraTransformer>(Comparator.comparingInt(ICameraTransformer::getPriority));
        this.lastTransformer = null;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        if (type == TickEvent.Type.RENDER) {
            final float pTicks = (float)context[0];
            if (this.hasActiveTransformer()) {
                final ICameraTransformer prio = this.getActiveTransformer();
                if (!prio.equals(this.lastTransformer)) {
                    if (this.lastTransformer != null) {
                        this.lastTransformer.onStopTransforming(pTicks);
                    }
                    prio.onStartTransforming(pTicks);
                    this.lastTransformer = prio;
                }
                prio.transformRenderView(Minecraft.func_71410_x().func_147113_T() ? 0.0f : pTicks);
                if (prio.getPersistencyFunction().isExpired()) {
                    prio.onStopTransforming(pTicks);
                    this.transformers.remove(prio);
                }
            }
            else if (this.lastTransformer != null) {
                this.lastTransformer.onStopTransforming(pTicks);
                this.lastTransformer = null;
            }
        }
        else if (!Minecraft.func_71410_x().func_147113_T() && this.hasActiveTransformer()) {
            this.getActiveTransformer().onClientTick();
        }
    }
    
    public void removeAllAndCleanup() {
        if (this.hasActiveTransformer()) {
            this.transformers.last().onStopTransforming(0.0f);
        }
        this.transformers.clear();
    }
    
    public void addTransformer(final ICameraTransformer transformer) {
        this.transformers.add(transformer);
    }
    
    public void removeTransformer(final ICameraTransformer transformer) {
        this.transformers.remove(transformer);
    }
    
    @Nullable
    public ICameraTransformer getActiveTransformer() {
        if (this.hasActiveTransformer()) {
            return this.transformers.last();
        }
        return null;
    }
    
    public boolean hasActiveTransformer() {
        return !this.transformers.isEmpty();
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.RENDER, TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.START;
    }
    
    public String getName() {
        return "Client Camera Manager";
    }
    
    static {
        INSTANCE = new ClientCameraManager();
    }
}
