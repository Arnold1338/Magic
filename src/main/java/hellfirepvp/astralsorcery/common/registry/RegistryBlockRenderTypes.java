package hellfirepvp.astralsorcery.common.registry;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.renderer.RenderTypeLookup;
import java.util.Arrays;
import net.minecraft.world.level.material.Fluid;
import hellfirepvp.astralsorcery.common.lib.FluidsAS;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;

public class RegistryBlockRenderTypes
{
    private RegistryBlockRenderTypes() {
    }
    
    public static void initBlocks() {
        setRenderLayer(BlocksAS.MARBLE_ARCH, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.MARBLE_BRICKS, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.MARBLE_CHISELED, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.MARBLE_ENGRAVED, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.MARBLE_PILLAR, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.MARBLE_RAW, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.MARBLE_RUNED, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.MARBLE_STAIRS, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.MARBLE_SLAB, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.BLACK_MARBLE_ARCH, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.BLACK_MARBLE_BRICKS, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.BLACK_MARBLE_CHISELED, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.BLACK_MARBLE_ENGRAVED, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.BLACK_MARBLE_PILLAR, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.BLACK_MARBLE_RAW, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.BLACK_MARBLE_RUNED, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.BLACK_MARBLE_STAIRS, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.BLACK_MARBLE_SLAB, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.INFUSED_WOOD, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.INFUSED_WOOD_ARCH, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.INFUSED_WOOD_COLUMN, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.INFUSED_WOOD_ENGRAVED, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.INFUSED_WOOD_ENRICHED, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.INFUSED_WOOD_INFUSED, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.INFUSED_WOOD_PLANKS, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.INFUSED_WOOD_STAIRS, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.INFUSED_WOOD_SLAB, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.AQUAMARINE_SAND_ORE, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.ROCK_CRYSTAL_ORE, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.STARMETAL_ORE, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.STARMETAL, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.GLOW_FLOWER, RenderType.func_228643_e_());
        setRenderLayer((Block)BlocksAS.SPECTRAL_RELAY, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.ALTAR_DISCOVERY, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.ALTAR_ATTUNEMENT, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.ALTAR_CONSTELLATION, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.ALTAR_RADIANCE, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.ATTUNEMENT_ALTAR, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.GEM_CRYSTAL_CLUSTER, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.ROCK_COLLECTOR_CRYSTAL, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.LENS, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.PRISM, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.RITUAL_LINK, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.RITUAL_PEDESTAL, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.INFUSER, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.CHALICE, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.WELL, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.ILLUMINATOR, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.TELESCOPE, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.TELESCOPE, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.OBSERVATORY, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.REFRACTION_TABLE, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.TREE_BEACON, RenderType.func_228639_c_());
        setRenderLayer((Block)BlocksAS.TREE_BEACON_COMPONENT, RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.GATEWAY, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.FOUNTAIN, RenderType.func_228639_c_());
        setRenderLayer(BlocksAS.FOUNTAIN_PRIME_LIQUID, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.FOUNTAIN_PRIME_VORTEX, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.FOUNTAIN_PRIME_ORE, RenderType.func_228639_c_(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.FLARE_LIGHT, RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.TRANSLUCENT_BLOCK, RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.VANISHING, RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.STRUCTURAL, RenderType.func_228645_f_());
    }
    
    public static void initFluids() {
        RegistryFluids.FLUID_BLOCKS.forEach(fluidBlock -> setRenderLayer(fluidBlock, RenderType.func_228645_f_()));
        setRenderLayer((Fluid)FluidsAS.LIQUID_STARLIGHT_SOURCE, RenderType.func_228645_f_());
        setRenderLayer((Fluid)FluidsAS.LIQUID_STARLIGHT_FLOWING, RenderType.func_228645_f_());
    }
    
    private static void setRenderLayer(final Block block, final RenderType... types) {
        final List<RenderType> typeList = Arrays.asList(types);
        RenderTypeLookup.setRenderLayer(block, (Predicate)typeList::contains);
    }
    
    private static void setRenderLayer(final Fluid fluid, final RenderType... types) {
        final List<RenderType> typeList = Arrays.asList(types);
        RenderTypeLookup.setRenderLayer(fluid, (Predicate)typeList::contains);
    }
}
