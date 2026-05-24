package hellfirepvp.astralsorcery.common.starlight.network;

import java.util.HashMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Iterator;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import java.util.List;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class StarlightUpdateHandler implements ITickHandler
{
    private static final StarlightUpdateHandler instance;
    private static final Map<RegistryKey<World>, List<IPrismTransmissionNode>> updateRequired;
    private static final Object accessLock;
    
    private StarlightUpdateHandler() {
    }
    
    public static StarlightUpdateHandler getInstance() {
        return StarlightUpdateHandler.instance;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final World world = (World)context[0];
        if (world.func_201670_d()) {
            return;
        }
        final List<IPrismTransmissionNode> nodes = this.getNodes(world);
        synchronized (StarlightUpdateHandler.accessLock) {
            for (final IPrismTransmissionNode node : nodes) {
                node.update(world);
            }
        }
    }
    
    private List<IPrismTransmissionNode> getNodes(final World world) {
        return StarlightUpdateHandler.updateRequired.computeIfAbsent((RegistryKey<World>)world.dimension(), k -> new LinkedList());
    }
    
    public void removeNode(final World world, final IPrismTransmissionNode node) {
        synchronized (StarlightUpdateHandler.accessLock) {
            this.getNodes(world).remove(node);
        }
    }
    
    public void addNode(final World world, final IPrismTransmissionNode node) {
        synchronized (StarlightUpdateHandler.accessLock) {
            this.getNodes(world).add(node);
        }
    }
    
    public void informWorldLoad(final World world) {
        synchronized (StarlightUpdateHandler.accessLock) {
            StarlightUpdateHandler.updateRequired.remove(world.dimension());
        }
    }
    
    public void clearServer() {
        synchronized (StarlightUpdateHandler.accessLock) {
            StarlightUpdateHandler.updateRequired.clear();
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.WORLD);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Starlight Update Handler";
    }
    
    static {
        instance = new StarlightUpdateHandler();
        updateRequired = new HashMap<RegistryKey<World>, List<IPrismTransmissionNode>>();
        accessLock = new Object();
    }
}
