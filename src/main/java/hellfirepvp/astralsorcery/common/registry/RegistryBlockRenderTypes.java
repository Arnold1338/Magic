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
        setRenderLayer(BlocksAS.MARBLE_ARCH, RenderType.last());
        setRenderLayer(BlocksAS.MARBLE_BRICKS, RenderType.last());
        setRenderLayer(BlocksAS.MARBLE_CHISELED, RenderType.last());
        setRenderLayer(BlocksAS.MARBLE_ENGRAVED, RenderType.last());
        setRenderLayer(BlocksAS.MARBLE_PILLAR, RenderType.last());
        setRenderLayer(BlocksAS.MARBLE_RAW, RenderType.last());
        setRenderLayer(BlocksAS.MARBLE_RUNED, RenderType.last());
        setRenderLayer((Block)BlocksAS.MARBLE_STAIRS, RenderType.last());
        setRenderLayer((Block)BlocksAS.MARBLE_SLAB, RenderType.last());
        setRenderLayer(BlocksAS.BLACK_MARBLE_ARCH, RenderType.last());
        setRenderLayer(BlocksAS.BLACK_MARBLE_BRICKS, RenderType.last());
        setRenderLayer(BlocksAS.BLACK_MARBLE_CHISELED, RenderType.last());
        setRenderLayer(BlocksAS.BLACK_MARBLE_ENGRAVED, RenderType.last());
        setRenderLayer(BlocksAS.BLACK_MARBLE_PILLAR, RenderType.last());
        setRenderLayer(BlocksAS.BLACK_MARBLE_RAW, RenderType.last());
        setRenderLayer(BlocksAS.BLACK_MARBLE_RUNED, RenderType.last());
        setRenderLayer((Block)BlocksAS.BLACK_MARBLE_STAIRS, RenderType.last());
        setRenderLayer((Block)BlocksAS.BLACK_MARBLE_SLAB, RenderType.last());
        setRenderLayer(BlocksAS.INFUSED_WOOD, RenderType.last());
        setRenderLayer(BlocksAS.INFUSED_WOOD_ARCH, RenderType.last());
        setRenderLayer(BlocksAS.INFUSED_WOOD_COLUMN, RenderType.last());
        setRenderLayer(BlocksAS.INFUSED_WOOD_ENGRAVED, RenderType.last());
        setRenderLayer(BlocksAS.INFUSED_WOOD_ENRICHED, RenderType.last());
        setRenderLayer(BlocksAS.INFUSED_WOOD_INFUSED, RenderType.last());
        setRenderLayer(BlocksAS.INFUSED_WOOD_PLANKS, RenderType.last());
        setRenderLayer((Block)BlocksAS.INFUSED_WOOD_STAIRS, RenderType.last());
        setRenderLayer((Block)BlocksAS.INFUSED_WOOD_SLAB, RenderType.last());
        setRenderLayer((Block)BlocksAS.AQUAMARINE_SAND_ORE, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.ROCK_CRYSTAL_ORE, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.STARMETAL_ORE, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.STARMETAL, RenderType.last());
        setRenderLayer((Block)BlocksAS.GLOW_FLOWER, RenderType.func_228643_e_());
        setRenderLayer((Block)BlocksAS.SPECTRAL_RELAY, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.ALTAR_DISCOVERY, RenderType.last());
        setRenderLayer((Block)BlocksAS.ALTAR_ATTUNEMENT, RenderType.last());
        setRenderLayer((Block)BlocksAS.ALTAR_CONSTELLATION, RenderType.last());
        setRenderLayer((Block)BlocksAS.ALTAR_RADIANCE, RenderType.last());
        setRenderLayer((Block)BlocksAS.ATTUNEMENT_ALTAR, RenderType.last());
        setRenderLayer((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.GEM_CRYSTAL_CLUSTER, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.ROCK_COLLECTOR_CRYSTAL, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.LENS, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.PRISM, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.RITUAL_LINK, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.RITUAL_PEDESTAL, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.INFUSER, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.CHALICE, RenderType.last());
        setRenderLayer((Block)BlocksAS.WELL, RenderType.last());
        setRenderLayer((Block)BlocksAS.ILLUMINATOR, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.TELESCOPE, RenderType.last());
        setRenderLayer((Block)BlocksAS.TELESCOPE, RenderType.last());
        setRenderLayer((Block)BlocksAS.OBSERVATORY, RenderType.last());
        setRenderLayer((Block)BlocksAS.REFRACTION_TABLE, RenderType.last());
        setRenderLayer((Block)BlocksAS.TREE_BEACON, RenderType.last());
        setRenderLayer((Block)BlocksAS.TREE_BEACON_COMPONENT, RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.GATEWAY, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer((Block)BlocksAS.FOUNTAIN, RenderType.last());
        setRenderLayer(BlocksAS.FOUNTAIN_PRIME_LIQUID, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.FOUNTAIN_PRIME_VORTEX, RenderType.last(), RenderType.func_228645_f_());
        setRenderLayer(BlocksAS.FOUNTAIN_PRIME_ORE, RenderType.last(), RenderType.func_228645_f_());
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
