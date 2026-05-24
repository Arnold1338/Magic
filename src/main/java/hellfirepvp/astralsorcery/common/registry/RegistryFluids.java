package hellfirepvp.astralsorcery.common.registry;

import java.util.LinkedList;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.FlowingFluidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import java.util.function.Supplier;
import net.minecraftforge.fluids.FluidAttributes;
import java.util.function.Function;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.fluid.ItemLiquidStarlightBucket;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.fluid.BlockLiquidStarlight;
import hellfirepvp.astralsorcery.common.lib.FluidsAS;
import hellfirepvp.astralsorcery.common.fluid.FluidLiquidStarlight;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.List;

public class RegistryFluids
{
    static final List<Block> FLUID_BLOCKS;
    static final List<Item> FLUID_HOLDER_ITEMS;
    
    private RegistryFluids() {
    }
    
    public static void registerFluids() {
        makeProperties();
        FluidsAS.LIQUID_STARLIGHT_SOURCE = registerFluid(new FluidLiquidStarlight.Source(FluidsAS.LIQUID_STARLIGHT_PROPERTIES));
        FluidsAS.LIQUID_STARLIGHT_FLOWING = registerFluid(new FluidLiquidStarlight.Flowing(FluidsAS.LIQUID_STARLIGHT_PROPERTIES));
        RegistryFluids.FLUID_BLOCKS.add((Block)(BlocksAS.FLUID_LIQUID_STARLIGHT = new BlockLiquidStarlight(() -> FluidsAS.LIQUID_STARLIGHT_SOURCE)));
        RegistryFluids.FLUID_HOLDER_ITEMS.add((Item)(ItemsAS.BUCKET_LIQUID_STARLIGHT = new ItemLiquidStarlightBucket(() -> FluidsAS.LIQUID_STARLIGHT_SOURCE)));
    }
    
    private static void makeProperties() {
        FluidsAS.LIQUID_STARLIGHT_PROPERTIES = makeProperties((Class<? extends ForgeFlowingFluid>)FluidLiquidStarlight.class, FluidLiquidStarlight::addAttributes, () -> FluidsAS.LIQUID_STARLIGHT_SOURCE, () -> FluidsAS.LIQUID_STARLIGHT_FLOWING).block(() -> BlocksAS.FLUID_LIQUID_STARLIGHT).bucket(() -> ItemsAS.BUCKET_LIQUID_STARLIGHT);
    }
    
    private static ForgeFlowingFluid.Properties makeProperties(final Class<? extends ForgeFlowingFluid> fluidClass, final Function<FluidAttributes.Builder, FluidAttributes.Builder> postProcess, final Supplier<ForgeFlowingFluid> stillFluidSupplier, final Supplier<ForgeFlowingFluid> flowingFluidSupplier) {
        final String name = NameUtil.fromClass(fluidClass, "Fluid").func_110623_a();
        return new ForgeFlowingFluid.Properties((Supplier)stillFluidSupplier, (Supplier)flowingFluidSupplier, (FluidAttributes.Builder)postProcess.apply(builderFor(name)));
    }
    
    private static FluidAttributes.Builder builderFor(final String fluidName) {
        final ResourceLocation still = AstralSorcery.key("fluid/" + fluidName + "_still");
        final ResourceLocation flowing = AstralSorcery.key("fluid/" + fluidName + "_flowing");
        return FluidAttributes.builder(still, flowing);
    }
    
    private static <T extends Fluid> T registerFluid(final T fluid) {
        return registerFluid(fluid, NameUtil.fromClass(fluid, "Fluid", "Source"));
    }
    
    private static <T extends Fluid> T registerFluid(final T fluid, final ResourceLocation name) {
        fluid.setRegistryName(name);
        AstralSorcery.getProxy().getRegistryPrimer().register(fluid);
        return fluid;
    }
    
    static {
        FLUID_BLOCKS = new LinkedList<Block>();
        FLUID_HOLDER_ITEMS = new LinkedList<Item>();
    }
}
