package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.common.capabilities.CapabilityInject;
import hellfirepvp.astralsorcery.common.capability.ChunkFluidEntry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.resources.ResourceLocation;

public class CapabilitiesAS
{
    public static final ResourceLocation CHUNK_FLUID_KEY;
    @CapabilityInject(ChunkFluidEntry.class)
    public static Capability<ChunkFluidEntry> CHUNK_FLUID;
    
    private CapabilitiesAS() {
    }
    
    static {
        CHUNK_FLUID_KEY = AstralSorcery.key("chunk_fluid");
        CapabilitiesAS.CHUNK_FLUID = null;
    }
}
