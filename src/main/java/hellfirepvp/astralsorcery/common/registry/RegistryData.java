package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.data.world.RockCrystalBuffer;
import hellfirepvp.astralsorcery.common.data.world.StorageNetworkBuffer;
import hellfirepvp.astralsorcery.common.data.world.LightNetworkBuffer;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.observerlib.common.data.WorldCacheManager;

public class RegistryData
{
    private RegistryData() {
    }
    
    public static void init() {
        DataAS.DOMAIN_AS = WorldCacheManager.createDomain("astralsorcery");
        DataAS.KEY_GATEWAY_CACHE = (WorldCacheDomain.SaveKey<GatewayCache>)DataAS.DOMAIN_AS.createSaveKey("gateway-cache", (Function)GatewayCache::new);
        DataAS.KEY_STARLIGHT_NETWORK = (WorldCacheDomain.SaveKey<LightNetworkBuffer>)DataAS.DOMAIN_AS.createSaveKey("lightnetwork", (Function)LightNetworkBuffer::new);
        DataAS.KEY_STORAGE_NETWORK = (WorldCacheDomain.SaveKey<StorageNetworkBuffer>)DataAS.DOMAIN_AS.createSaveKey("storagenetwork", (Function)StorageNetworkBuffer::new);
        DataAS.KEY_ROCK_CRYSTAL_BUFFER = (WorldCacheDomain.SaveKey<RockCrystalBuffer>)DataAS.DOMAIN_AS.createSaveKey("rock-crystals", (Function)RockCrystalBuffer::new);
    }
}
