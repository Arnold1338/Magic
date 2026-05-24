package hellfirepvp.astralsorcery.common.registry;

import java.util.LinkedList;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import hellfirepvp.astralsorcery.common.block.base.template.BlockStairsTemplate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.level.block.state.BlockBehaviour;
import hellfirepvp.astralsorcery.common.block.base.template.BlockSlabTemplate;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.block.tile.BlockStructural;
import hellfirepvp.astralsorcery.common.block.tile.BlockVanishing;
import hellfirepvp.astralsorcery.common.block.tile.BlockTranslucentBlock;
import hellfirepvp.astralsorcery.common.block.tile.BlockFlareLight;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrimeOre;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrimeVortex;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrimeLiquid;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrime;
import hellfirepvp.astralsorcery.common.block.tile.BlockFountain;
import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialGateway;
import hellfirepvp.astralsorcery.common.block.tile.BlockTreeBeaconComponent;
import hellfirepvp.astralsorcery.common.block.tile.BlockTreeBeacon;
import hellfirepvp.astralsorcery.common.block.tile.BlockRefractionTable;
import hellfirepvp.astralsorcery.common.block.tile.BlockObservatory;
import hellfirepvp.astralsorcery.common.block.tile.BlockTelescope;
import hellfirepvp.astralsorcery.common.block.tile.BlockIlluminator;
import hellfirepvp.astralsorcery.common.block.tile.BlockWell;
import hellfirepvp.astralsorcery.common.block.tile.BlockChalice;
import hellfirepvp.astralsorcery.common.block.tile.BlockInfuser;
import hellfirepvp.astralsorcery.common.block.tile.BlockRitualPedestal;
import hellfirepvp.astralsorcery.common.block.tile.BlockRitualLink;
import hellfirepvp.astralsorcery.common.block.tile.BlockPrism;
import hellfirepvp.astralsorcery.common.block.tile.BlockLens;
import hellfirepvp.astralsorcery.common.block.tile.crystal.BlockCelestialCollectorCrystal;
import hellfirepvp.astralsorcery.common.block.tile.crystal.BlockRockCollectorCrystal;
import hellfirepvp.astralsorcery.common.block.tile.BlockGemCrystalCluster;
import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialCrystalCluster;
import hellfirepvp.astralsorcery.common.block.tile.BlockAttunementAltar;
import hellfirepvp.astralsorcery.common.block.tile.altar.BlockAltarRadiance;
import hellfirepvp.astralsorcery.common.block.tile.altar.BlockAltarConstellation;
import hellfirepvp.astralsorcery.common.block.tile.altar.BlockAltarAttunement;
import hellfirepvp.astralsorcery.common.block.tile.altar.BlockAltarDiscovery;
import hellfirepvp.astralsorcery.common.block.tile.BlockSpectralRelay;
import hellfirepvp.astralsorcery.common.block.foliage.BlockGlowFlower;
import hellfirepvp.astralsorcery.common.block.ore.BlockStarmetal;
import hellfirepvp.astralsorcery.common.block.ore.BlockStarmetalOre;
import hellfirepvp.astralsorcery.common.block.ore.BlockRockCrystalOre;
import hellfirepvp.astralsorcery.common.block.ore.BlockAquamarineSandOre;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWoodPlanks;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWoodInfused;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWoodEnriched;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWoodEngraved;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWoodColumn;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWoodArch;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWood;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarbleRuned;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarbleRaw;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarblePillar;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarbleEngraved;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarbleChiseled;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarbleBricks;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarbleArch;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarbleRuned;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarbleRaw;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarbleEngraved;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarbleChiseled;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarbleBricks;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarbleArch;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockDynamicColor;
import java.util.List;

public class RegistryBlocks
{
    private static final List<BlockDynamicColor> COLOR_BLOCKS;
    static final List<CustomItemBlock> ITEM_BLOCKS;
    
    private RegistryBlocks() {
    }
    
    public static void registerBlocks() {
        BlocksAS.MARBLE_ARCH = registerBlock(new BlockMarbleArch());
        BlocksAS.MARBLE_BRICKS = registerBlock(new BlockMarbleBricks());
        BlocksAS.MARBLE_CHISELED = registerBlock(new BlockMarbleChiseled());
        BlocksAS.MARBLE_ENGRAVED = registerBlock(new BlockMarbleEngraved());
        BlocksAS.MARBLE_PILLAR = registerBlock(new BlockMarblePillar());
        BlocksAS.MARBLE_RAW = registerBlock(new BlockMarbleRaw());
        BlocksAS.MARBLE_RUNED = registerBlock(new BlockMarbleRuned());
        BlocksAS.MARBLE_STAIRS = makeStairs(BlocksAS.MARBLE_BRICKS.defaultBlockState(), "marble_stairs");
        BlocksAS.MARBLE_SLAB = makeSlab(BlocksAS.MARBLE_BRICKS.defaultBlockState(), "marble_slab");
        BlocksAS.BLACK_MARBLE_ARCH = registerBlock(new BlockBlackMarbleArch());
        BlocksAS.BLACK_MARBLE_BRICKS = registerBlock(new BlockBlackMarbleBricks());
        BlocksAS.BLACK_MARBLE_CHISELED = registerBlock(new BlockBlackMarbleChiseled());
        BlocksAS.BLACK_MARBLE_ENGRAVED = registerBlock(new BlockBlackMarbleEngraved());
        BlocksAS.BLACK_MARBLE_PILLAR = registerBlock(new BlockBlackMarblePillar());
        BlocksAS.BLACK_MARBLE_RAW = registerBlock(new BlockBlackMarbleRaw());
        BlocksAS.BLACK_MARBLE_RUNED = registerBlock(new BlockBlackMarbleRuned());
        BlocksAS.BLACK_MARBLE_STAIRS = makeStairs(BlocksAS.BLACK_MARBLE_BRICKS.defaultBlockState(), "black_marble_stairs");
        BlocksAS.BLACK_MARBLE_SLAB = makeSlab(BlocksAS.BLACK_MARBLE_BRICKS.defaultBlockState(), "black_marble_slab");
        BlocksAS.INFUSED_WOOD = registerBlock(new BlockInfusedWood());
        BlocksAS.INFUSED_WOOD_ARCH = registerBlock(new BlockInfusedWoodArch());
        BlocksAS.INFUSED_WOOD_COLUMN = registerBlock(new BlockInfusedWoodColumn());
        BlocksAS.INFUSED_WOOD_ENGRAVED = registerBlock(new BlockInfusedWoodEngraved());
        BlocksAS.INFUSED_WOOD_ENRICHED = registerBlock(new BlockInfusedWoodEnriched());
        BlocksAS.INFUSED_WOOD_INFUSED = registerBlock(new BlockInfusedWoodInfused());
        BlocksAS.INFUSED_WOOD_PLANKS = registerBlock(new BlockInfusedWoodPlanks());
        BlocksAS.INFUSED_WOOD_STAIRS = makeStairs(BlocksAS.INFUSED_WOOD_PLANKS.defaultBlockState(), "infused_wood_stairs");
        BlocksAS.INFUSED_WOOD_SLAB = makeSlab(BlocksAS.INFUSED_WOOD_PLANKS.defaultBlockState(), "infused_wood_slab");
        BlocksAS.AQUAMARINE_SAND_ORE = registerBlock(new BlockAquamarineSandOre());
        BlocksAS.ROCK_CRYSTAL_ORE = registerBlock(new BlockRockCrystalOre());
        BlocksAS.STARMETAL_ORE = registerBlock(new BlockStarmetalOre());
        BlocksAS.STARMETAL = registerBlock(new BlockStarmetal());
        BlocksAS.GLOW_FLOWER = registerBlock(new BlockGlowFlower());
        BlocksAS.SPECTRAL_RELAY = registerBlock(new BlockSpectralRelay());
        BlocksAS.ALTAR_DISCOVERY = registerBlock(new BlockAltarDiscovery());
        BlocksAS.ALTAR_ATTUNEMENT = registerBlock(new BlockAltarAttunement());
        BlocksAS.ALTAR_CONSTELLATION = registerBlock(new BlockAltarConstellation());
        BlocksAS.ALTAR_RADIANCE = registerBlock(new BlockAltarRadiance());
        BlocksAS.ATTUNEMENT_ALTAR = registerBlock(new BlockAttunementAltar());
        BlocksAS.CELESTIAL_CRYSTAL_CLUSTER = registerBlock(new BlockCelestialCrystalCluster());
        BlocksAS.GEM_CRYSTAL_CLUSTER = registerBlock(new BlockGemCrystalCluster());
        BlocksAS.ROCK_COLLECTOR_CRYSTAL = registerBlock(new BlockRockCollectorCrystal());
        BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL = registerBlock(new BlockCelestialCollectorCrystal());
        BlocksAS.LENS = registerBlock(new BlockLens());
        BlocksAS.PRISM = registerBlock(new BlockPrism());
        BlocksAS.RITUAL_LINK = registerBlock(new BlockRitualLink());
        BlocksAS.RITUAL_PEDESTAL = registerBlock(new BlockRitualPedestal());
        BlocksAS.INFUSER = registerBlock(new BlockInfuser());
        BlocksAS.CHALICE = registerBlock(new BlockChalice());
        BlocksAS.WELL = registerBlock(new BlockWell());
        BlocksAS.ILLUMINATOR = registerBlock(new BlockIlluminator());
        BlocksAS.TELESCOPE = registerBlock(new BlockTelescope());
        BlocksAS.OBSERVATORY = registerBlock(new BlockObservatory());
        BlocksAS.REFRACTION_TABLE = registerBlock(new BlockRefractionTable());
        BlocksAS.TREE_BEACON = registerBlock(new BlockTreeBeacon());
        BlocksAS.TREE_BEACON_COMPONENT = registerBlock(new BlockTreeBeaconComponent());
        BlocksAS.GATEWAY = registerBlock(new BlockCelestialGateway());
        BlocksAS.FOUNTAIN = registerBlock(new BlockFountain());
        BlocksAS.FOUNTAIN_PRIME_LIQUID = registerBlock(new BlockFountainPrimeLiquid());
        BlocksAS.FOUNTAIN_PRIME_VORTEX = registerBlock(new BlockFountainPrimeVortex());
        BlocksAS.FOUNTAIN_PRIME_ORE = registerBlock(new BlockFountainPrimeOre());
        BlocksAS.FLARE_LIGHT = registerBlock(new BlockFlareLight());
        BlocksAS.TRANSLUCENT_BLOCK = registerBlock(new BlockTranslucentBlock());
        BlocksAS.VANISHING = registerBlock(new BlockVanishing());
        BlocksAS.STRUCTURAL = registerBlock(new BlockStructural());
    }
    
    public static void registerFluidBlocks() {
        RegistryFluids.FLUID_BLOCKS.forEach(RegistryBlocks::registerBlock);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void registerColors(final ColorHandlerEvent.Block blockColorEvent) {
        RegistryBlocks.COLOR_BLOCKS.forEach(block -> blockColorEvent.getBlockColors().func_186722_a(block::getColor, new Block[] { (Block)block }));
    }
    
    private static BlockSlabTemplate makeSlab(final BlockState base, final String name) {
        final BlockSlabTemplate slabs = new BlockSlabTemplate(base, AbstractBlock.Properties.func_200950_a((AbstractBlock)base.getBlock()));
        ResourceLocation slabsName = base.getBlock().getRegistryName();
        slabsName = new ResourceLocation(slabsName.func_110624_b(), name);
        return registerBlock(slabs, slabsName);
    }
    
    private static BlockStairsTemplate makeStairs(final BlockState base, final String name) {
        final BlockStairsTemplate stairs = new BlockStairsTemplate(base, AbstractBlock.Properties.func_200950_a((AbstractBlock)base.getBlock()));
        ResourceLocation stairsName = base.getBlock().getRegistryName();
        stairsName = new ResourceLocation(stairsName.func_110624_b(), name);
        return registerBlock(stairs, stairsName);
    }
    
    private static <T extends Block> T registerBlock(final T block) {
        return registerBlock(block, NameUtil.fromClass(block, "Block"));
    }
    
    private static <T extends Block> T registerBlock(final T block, final ResourceLocation name) {
        block.setRegistryName(name);
        AstralSorcery.getProxy().getRegistryPrimer().register(block);
        if (block instanceof CustomItemBlock) {
            RegistryBlocks.ITEM_BLOCKS.add((CustomItemBlock)block);
        }
        if (block instanceof BlockDynamicColor) {
            RegistryBlocks.COLOR_BLOCKS.add((BlockDynamicColor)block);
        }
        return block;
    }
    
    static {
        COLOR_BLOCKS = Lists.newArrayList();
        ITEM_BLOCKS = new LinkedList<CustomItemBlock>();
    }
}
