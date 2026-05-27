package hellfirepvp.astralsorcery.datagen.assets;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import java.util.Iterator;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import java.util.Collection;
import com.google.common.collect.Iterables;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.block.tile.BlockStructural;
import org.apache.commons.lang3.ArrayUtils;
import hellfirepvp.astralsorcery.common.block.tile.BlockPrism;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraft.core.Direction;
import hellfirepvp.astralsorcery.common.block.tile.BlockLens;
import hellfirepvp.astralsorcery.common.block.infusedwood.BlockInfusedWoodColumn;
import hellfirepvp.astralsorcery.common.block.blackmarble.BlockBlackMarblePillar;
import net.minecraft.world.level.block.StairsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;

public class AstralBlockStateMappingProvider extends BlockStateProvider
{
    public AstralBlockStateMappingProvider(final DataGenerator gen, final ExistingFileHelper exFileHelper) {
        super(gen, "astralsorcery", exFileHelper);
    }
    
    protected void registerStatesAndModels() {
        this.simpleBlockState((Block)BlocksAS.FLUID_LIQUID_STARLIGHT);
        this.simpleBlockState(BlocksAS.MARBLE_ARCH);
        this.simpleBlockState(BlocksAS.MARBLE_BRICKS);
        this.simpleBlockState(BlocksAS.MARBLE_CHISELED);
        this.simpleBlockState(BlocksAS.MARBLE_ENGRAVED);
        this.pillarModel(BlocksAS.MARBLE_PILLAR, (net.minecraft.state.Property<BlockMarblePillar.PillarType>)BlockMarblePillar.PILLAR_TYPE, BlockMarblePillar.PillarType.MIDDLE, BlockMarblePillar.PillarType.TOP, BlockMarblePillar.PillarType.BOTTOM);
        this.simpleBlockState(BlocksAS.MARBLE_RAW);
        this.simpleBlockState(BlocksAS.MARBLE_RUNED);
        this.simpleSlabs(BlocksAS.MARBLE_SLAB, this.model((IForgeRegistryEntry<?>)BlocksAS.MARBLE_BRICKS));
        this.simpleStairs(BlocksAS.MARBLE_STAIRS);
        this.simpleBlockState(BlocksAS.BLACK_MARBLE_ARCH);
        this.simpleBlockState(BlocksAS.BLACK_MARBLE_BRICKS);
        this.simpleBlockState(BlocksAS.BLACK_MARBLE_CHISELED);
        this.simpleBlockState(BlocksAS.BLACK_MARBLE_ENGRAVED);
        this.pillarModel(BlocksAS.BLACK_MARBLE_PILLAR, (net.minecraft.state.Property<BlockBlackMarblePillar.PillarType>)BlockBlackMarblePillar.PILLAR_TYPE, BlockBlackMarblePillar.PillarType.MIDDLE, BlockBlackMarblePillar.PillarType.TOP, BlockBlackMarblePillar.PillarType.BOTTOM);
        this.simpleBlockState(BlocksAS.BLACK_MARBLE_RAW);
        this.simpleBlockState(BlocksAS.BLACK_MARBLE_RUNED);
        this.simpleSlabs(BlocksAS.BLACK_MARBLE_SLAB, this.model((IForgeRegistryEntry<?>)BlocksAS.BLACK_MARBLE_BRICKS));
        this.simpleStairs(BlocksAS.BLACK_MARBLE_STAIRS);
        this.simpleBlockState(BlocksAS.INFUSED_WOOD);
        this.simpleBlockState(BlocksAS.INFUSED_WOOD_ARCH);
        this.pillarModel(BlocksAS.INFUSED_WOOD_COLUMN, (net.minecraft.state.Property<BlockInfusedWoodColumn.PillarType>)BlockInfusedWoodColumn.PILLAR_TYPE, BlockInfusedWoodColumn.PillarType.MIDDLE, BlockInfusedWoodColumn.PillarType.TOP, BlockInfusedWoodColumn.PillarType.BOTTOM);
        this.simpleBlockState(BlocksAS.INFUSED_WOOD_ENGRAVED);
        this.simpleBlockState(BlocksAS.INFUSED_WOOD_ENRICHED);
        this.simpleBlockState(BlocksAS.INFUSED_WOOD_INFUSED);
        this.simpleBlockState(BlocksAS.INFUSED_WOOD_PLANKS);
        this.simpleSlabs(BlocksAS.INFUSED_WOOD_SLAB, this.model((IForgeRegistryEntry<?>)BlocksAS.INFUSED_WOOD_PLANKS));
        this.simpleStairs(BlocksAS.INFUSED_WOOD_STAIRS);
        this.multiLayerBlockState((Block)BlocksAS.AQUAMARINE_SAND_ORE);
        this.multiLayerBlockState(BlocksAS.ROCK_CRYSTAL_ORE);
        this.multiLayerBlockState(BlocksAS.STARMETAL_ORE);
        this.simpleBlockState(BlocksAS.STARMETAL);
        this.simpleBlockState((Block)BlocksAS.GLOW_FLOWER);
        this.multiLayerBlockState((Block)BlocksAS.SPECTRAL_RELAY);
        this.simpleBlockState((Block)BlocksAS.ALTAR_DISCOVERY);
        this.simpleBlockState((Block)BlocksAS.ALTAR_ATTUNEMENT);
        this.simpleBlockState((Block)BlocksAS.ALTAR_CONSTELLATION);
        this.simpleBlockState((Block)BlocksAS.ALTAR_RADIANCE);
        this.simpleBlockState((Block)BlocksAS.ATTUNEMENT_ALTAR);
        this.allStateSuffixMultiLayerModel((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER);
        this.allStateSuffixMultiLayerModel((Block)BlocksAS.GEM_CRYSTAL_CLUSTER);
        this.multiLayerBlockState((Block)BlocksAS.ROCK_COLLECTOR_CRYSTAL);
        this.multiLayerBlockState((Block)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL);
        this.getVariantBuilder((Block)BlocksAS.LENS).partialState().with((Property)BlockLens.PLACED_AGAINST, (Comparable)Direction.UP).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(AstralSorcery.key("lens_base")), 180, 0, false) }).partialState().with((Property)BlockLens.PLACED_AGAINST, (Comparable)Direction.DOWN).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(AstralSorcery.key("lens_base")), 0, 0, false) }).partialState().with((Property)BlockLens.PLACED_AGAINST, (Comparable)Direction.NORTH).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(AstralSorcery.key("lens_base")), 90, 180, false) }).partialState().with((Property)BlockLens.PLACED_AGAINST, (Comparable)Direction.SOUTH).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(AstralSorcery.key("lens_base")), 90, 0, false) }).partialState().with((Property)BlockLens.PLACED_AGAINST, (Comparable)Direction.EAST).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(AstralSorcery.key("lens_base")), 90, 270, false) }).partialState().with((Property)BlockLens.PLACED_AGAINST, (Comparable)Direction.WEST).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(AstralSorcery.key("lens_base")), 90, 90, false) });
        final ResourceLocation prism = BlocksAS.PRISM.getRegistryName();
        final ResourceLocation prismColored = NameUtil.suffixPath(prism, "_colored");
        ((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)((MultiPartBlockStateBuilder.PartBuilder)this.getMultipartBuilder((Block)BlocksAS.PRISM).part().modelFile(this.multiLayerModel(prism)).rotationX(180).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.UP }).end().part().modelFile(this.multiLayerModel(prismColored)).rotationX(180).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.UP }).condition((Property)BlockPrism.HAS_COLORED_LENS, (Comparable[])new Boolean[] { true }).end().part().modelFile(this.multiLayerModel(prism)).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.DOWN }).end().part().modelFile(this.multiLayerModel(prismColored)).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.DOWN }).condition((Property)BlockPrism.HAS_COLORED_LENS, (Comparable[])new Boolean[] { true }).end().part().modelFile(this.multiLayerModel(prism)).rotationX(90).rotationY(180).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.NORTH }).end().part().modelFile(this.multiLayerModel(prismColored)).rotationX(90).rotationY(180).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.NORTH }).condition((Property)BlockPrism.HAS_COLORED_LENS, (Comparable[])new Boolean[] { true }).end().part().modelFile(this.multiLayerModel(prism)).rotationX(90).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.SOUTH }).end().part().modelFile(this.multiLayerModel(prismColored)).rotationX(90).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.SOUTH }).condition((Property)BlockPrism.HAS_COLORED_LENS, (Comparable[])new Boolean[] { true }).end().part().modelFile(this.multiLayerModel(prism)).rotationX(90).rotationY(270).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.EAST }).end().part().modelFile(this.multiLayerModel(prismColored)).rotationX(90).rotationY(270).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.EAST }).condition((Property)BlockPrism.HAS_COLORED_LENS, (Comparable[])new Boolean[] { true }).end().part().modelFile(this.multiLayerModel(prism)).rotationX(90).rotationY(90).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.WEST }).end().part().modelFile(this.multiLayerModel(prismColored)).rotationX(90).rotationY(90).addModel()).condition((Property)BlockPrism.PLACED_AGAINST, (Comparable[])new Direction[] { Direction.WEST }).condition((Property)BlockPrism.HAS_COLORED_LENS, (Comparable[])new Boolean[] { true }).end();
        this.simpleBlockState((Block)BlocksAS.REFRACTION_TABLE, this.modelAS("refraction_table_particle"));
        this.multiLayerBlockState((Block)BlocksAS.RITUAL_LINK);
        this.multiLayerBlockState((Block)BlocksAS.RITUAL_PEDESTAL);
        this.multiLayerBlockState((Block)BlocksAS.ILLUMINATOR);
        this.multiLayerBlockState((Block)BlocksAS.INFUSER);
        this.simpleBlockState((Block)BlocksAS.CHALICE);
        this.simpleBlockState((Block)BlocksAS.TELESCOPE);
        this.simpleBlockState((Block)BlocksAS.OBSERVATORY);
        this.simpleBlockState((Block)BlocksAS.WELL);
        this.simpleBlockState((Block)BlocksAS.TREE_BEACON);
        this.simpleBlockState((Block)BlocksAS.TREE_BEACON_COMPONENT, this.modelNothing());
        this.multiLayerBlockState((Block)BlocksAS.GATEWAY);
        this.simpleBlockState((Block)BlocksAS.FOUNTAIN);
        this.multiLayerBlockState(BlocksAS.FOUNTAIN_PRIME_LIQUID);
        this.multiLayerBlockState(BlocksAS.FOUNTAIN_PRIME_VORTEX);
        this.multiLayerBlockState(BlocksAS.FOUNTAIN_PRIME_ORE);
        this.getVariantBuilder((Block)BlocksAS.FLARE_LIGHT).forAllStates(state -> ArrayUtils.toArray((Object[])new ConfiguredModel[] { new ConfiguredModel(this.modelNothing()) }));
        this.simpleBlockState((Block)BlocksAS.TRANSLUCENT_BLOCK, this.modelNothing());
        this.simpleBlockState((Block)BlocksAS.VANISHING, this.modelNothing());
        this.getVariantBuilder((Block)BlocksAS.STRUCTURAL).partialState().with((Property)BlockStructural.BLOCK_TYPE, (Comparable)BlockStructural.BlockType.TELESCOPE).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model((IForgeRegistryEntry<?>)BlocksAS.TELESCOPE)) }).partialState().with((Property)BlockStructural.BLOCK_TYPE, (Comparable)BlockStructural.BlockType.DUMMY).addModels(new ConfiguredModel[] { new ConfiguredModel(this.modelNothing()) });
    }
    
    private <T extends Comparable<T>> void pillarModel(final Block b, final Property<T> pillarType, final T middle, final T top, final T bottom) {
        final ResourceLocation key = b.getRegistryName();
        this.getVariantBuilder(b).partialState().with((Property)pillarType, (Comparable)middle).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(key)) }).partialState().with((Property)pillarType, (Comparable)top).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(NameUtil.suffixPath(key, "_top"))) }).partialState().with((Property)pillarType, (Comparable)bottom).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(NameUtil.suffixPath(key, "_bottom"))) });
    }
    
    private <T extends Comparable<T>> void allStateSuffixModel(final Block b) {
        final Collection<Property<?>> properties = b.func_176194_O().func_177623_d();
        if (properties.size() != 1) {
            throw new IllegalArgumentException("Can only make path-suffix enumeration for blockstates with exactly 1 property!");
        }
        final ResourceLocation key = b.getRegistryName();
        final Property<T> property = (Property<T>)Iterables.getFirst((Iterable)properties, (Object)null);
        final VariantBlockStateBuilder builder = this.getVariantBuilder(b);
        for (final T value : property.getPossibleValues()) {
            builder.partialState().with((Property)property, (Comparable)value).addModels(new ConfiguredModel[] { new ConfiguredModel(this.model(NameUtil.suffixPath(key, "_" + value.getSerializedName()))) });
        }
    }
    
    private <T extends Comparable<T>> void allStateSuffixMultiLayerModel(final Block b) {
        final Collection<Property<?>> properties = b.func_176194_O().func_177623_d();
        if (properties.size() != 1) {
            throw new IllegalArgumentException("Can only make path-suffix enumeration for blockstates with exactly 1 property!");
        }
        final ResourceLocation key = b.getRegistryName();
        final Property<T> property = (Property<T>)Iterables.getFirst((Iterable)properties, (Object)null);
        final VariantBlockStateBuilder builder = this.getVariantBuilder(b);
        for (final T value : property.getPossibleValues()) {
            builder.partialState().with((Property)property, (Comparable)value).addModels(new ConfiguredModel[] { new ConfiguredModel(this.multiLayerModel(NameUtil.suffixPath(key, "_" + value.getSerializedName()))) });
        }
    }
    
    private void simpleSlabs(final SlabBlock b, final ModelFile doubleSlabModel) {
        final ResourceLocation key = b.getRegistryName();
        this.slabBlock(b, this.model(key), this.model(NameUtil.suffixPath(key, "_top")), doubleSlabModel);
    }
    
    private void simpleStairs(final StairsBlock b) {
        final ResourceLocation key = b.getRegistryName();
        this.stairsBlock(b, this.model(key), this.model(NameUtil.suffixPath(key, "_inner")), this.model(NameUtil.suffixPath(key, "_outer")));
    }
    
    private void multiLayerBlockState(final Block b) {
        this.simpleBlockState(b, this.multiLayerModel(b.getRegistryName()));
    }
    
    private void simpleBlockState(final Block b) {
        this.simpleBlockState(b, this.model(b.getRegistryName()));
    }
    
    private void simpleBlockState(final Block b, final ModelFile targetModel) {
        this.getVariantBuilder(b).partialState().addModels(new ConfiguredModel[] { new ConfiguredModel(targetModel) });
    }
    
    private ModelFile modelNothing() {
        return this.modelAS("base/nothing");
    }
    
    private ModelFile modelAS(final String name) {
        return this.model(AstralSorcery.key(name));
    }
    
    private ModelFile model(final IForgeRegistryEntry<?> entry) {
        return this.model(entry.getRegistryName());
    }
    
    private ModelFile model(final ResourceLocation name) {
        return (ModelFile)new ModelFile.UncheckedModelFile(NameUtil.prefixPath(name, "block/"));
    }
    
    private ModelFile multiLayerModel(final ResourceLocation name) {
        return (ModelFile)new ModelFile.UncheckedModelFile(NameUtil.prefixPath(name, "block/multilayer/"));
    }
}
