package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.common.data.world.RockCrystalBuffer;
import hellfirepvp.astralsorcery.common.data.world.StorageNetworkBuffer;
import hellfirepvp.astralsorcery.common.data.world.LightNetworkBuffer;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;

public class DataAS
{
    public static WorldCacheDomain DOMAIN_AS;
    public static WorldCacheDomain.SaveKey<GatewayCache> KEY_GATEWAY_CACHE;
    public static WorldCacheDomain.SaveKey<LightNetworkBuffer> KEY_STARLIGHT_NETWORK;
    public static WorldCacheDomain.SaveKey<StorageNetworkBuffer> KEY_STORAGE_NETWORK;
    public static WorldCacheDomain.SaveKey<RockCrystalBuffer> KEY_ROCK_CRYSTAL_BUFFER;
    
    private DataAS() {
    }
}
