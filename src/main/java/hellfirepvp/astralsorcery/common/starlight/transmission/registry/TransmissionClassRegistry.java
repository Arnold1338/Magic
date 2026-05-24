package hellfirepvp.astralsorcery.common.starlight.transmission.registry;

import java.util.HashMap;
import net.minecraftforge.eventbus.api.Event;
import hellfirepvp.astralsorcery.common.event.StarlightNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.common.tile.network.StarlightTransmissionPrism;
import hellfirepvp.astralsorcery.common.tile.network.StarlightTransmissionLens;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverTreeBeacon;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverAltar;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverRitualPedestal;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverWell;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.CrystalPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.CrystalTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionSourceNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimplePrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionNode;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class TransmissionClassRegistry
{
    public static final TransmissionClassRegistry eventInstance;
    private static final Map<ResourceLocation, TransmissionProvider> providerMap;
    
    private TransmissionClassRegistry() {
    }
    
    public void registerProvider(final TransmissionProvider provider) {
        register(provider);
    }
    
    @Nullable
    public static TransmissionProvider getProvider(final ResourceLocation identifier) {
        return TransmissionClassRegistry.providerMap.get(identifier);
    }
    
    public static void register(final TransmissionProvider provider) {
        if (TransmissionClassRegistry.providerMap.containsKey(provider.getIdentifier())) {
            throw new RuntimeException("Already registered identifier TransmissionProvider: " + provider.getIdentifier());
        }
        TransmissionClassRegistry.providerMap.put(provider.getIdentifier(), provider);
    }
    
    public static void setupRegistry() {
        register(new SimpleTransmissionNode.Provider());
        register(new SimplePrismTransmissionNode.Provider());
        register(new SimpleTransmissionSourceNode.Provider());
        register(new CrystalTransmissionNode.Provider());
        register(new CrystalPrismTransmissionNode.Provider());
        register(new StarlightReceiverWell.Provider());
        register(new StarlightReceiverRitualPedestal.Provider());
        register(new StarlightReceiverAltar.Provider());
        register(new StarlightReceiverTreeBeacon.Provider());
        register(new StarlightTransmissionLens.Provider());
        register(new StarlightTransmissionPrism.Provider());
        MinecraftForge.EVENT_BUS.post((Event)new StarlightNetworkEvent.TransmissionRegister(TransmissionClassRegistry.eventInstance));
    }
    
    static {
        eventInstance = new TransmissionClassRegistry();
        providerMap = new HashMap<ResourceLocation, TransmissionProvider>();
    }
}
