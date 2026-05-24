package hellfirepvp.astralsorcery.common.base.patreon;

import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderBlockRing;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderStarryPlayerLayer;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderNebulaCloud;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderStarHalo;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderCelestialWings;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderWraithWings;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderHelmetRender;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderCrystalFootprints;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderTreeBeaconColor;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderFlareCrystal;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderFlareDynamicColor;
import hellfirepvp.astralsorcery.common.base.patreon.types.provider.ProviderFlare;

public enum PatreonEffectType
{
    FLARE((PatreonEffectProvider<?>)new ProviderFlare()), 
    FLARE_DYNAMIC_COLOR((PatreonEffectProvider<?>)new ProviderFlareDynamicColor()), 
    FLARE_CRYSTAL((PatreonEffectProvider<?>)new ProviderFlareCrystal()), 
    TREE_BEACON_COLOR((PatreonEffectProvider<?>)new ProviderTreeBeaconColor()), 
    CRYSTAL_FOOTPRINTS((PatreonEffectProvider<?>)new ProviderCrystalFootprints()), 
    HELMET((PatreonEffectProvider<?>)new ProviderHelmetRender()), 
    WRAITH_WINGS((PatreonEffectProvider<?>)new ProviderWraithWings()), 
    CELESTIAL_WINGS((PatreonEffectProvider<?>)new ProviderCelestialWings()), 
    STAR_HALO((PatreonEffectProvider<?>)new ProviderStarHalo()), 
    NEBULA_CLOUD((PatreonEffectProvider<?>)new ProviderNebulaCloud()), 
    STARRY_LAYER((PatreonEffectProvider<?>)new ProviderStarryPlayerLayer()), 
    BLOCK_RING((PatreonEffectProvider<?>)new ProviderBlockRing());
    
    private final PatreonEffectProvider<?> provider;
    
    private PatreonEffectType(final PatreonEffectProvider<?> provider) {
        this.provider = provider;
    }
    
    public PatreonEffectProvider<?> getProvider() {
        return this.provider;
    }
}
