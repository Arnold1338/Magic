package hellfirepvp.astralsorcery.common.crystal;

import net.minecraft.world.level.item.ItemStack;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.IndependentCrystalSource;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.crystal.source.Ritual;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverRitualPedestal;
import java.util.Iterator;

public class CrystalCalculations
{
    private static final float TICKS_PER_HOUR = 72000.0f;
    
    private CrystalCalculations() {
    }
    
    public static double calculate(double value, final CrystalAttributes attributes, final CalculationContext context) {
        for (final CrystalAttributes.Attribute attribute : attributes.getCrystalAttributes()) {
            value = attribute.getProperty().modify(value, attribute.getTier(), context);
        }
        return value;
    }
    
    public static double getRitualEffectRangeFactor(final StarlightReceiverRitualPedestal pedestal, final CrystalAttributes attributes) {
        return getRitualEffectRangeFactor(CrystalPropertiesAS.Sources.SOURCE_RITUAL_PEDESTAL.createInstance(pedestal), attributes);
    }
    
    public static double getRitualEffectRangeFactor(final TileRitualPedestal pedestal, final CrystalAttributes attributes) {
        return getRitualEffectRangeFactor(CrystalPropertiesAS.Sources.SOURCE_TILE_RITUAL_PEDESTAL.createInstance(pedestal), attributes);
    }
    
    private static double getRitualEffectRangeFactor(final Ritual pedestalSrc, final CrystalAttributes attributes) {
        final CalculationContext ctx = CalculationContext.Builder.newBuilder().fromSource(pedestalSrc).addUsage(CrystalPropertiesAS.Usages.USE_RITUAL_RANGE).build();
        return calculate(1.0, attributes, ctx);
    }
    
    public static double getRitualCostReductionFactor(final StarlightReceiverRitualPedestal pedestal, final CrystalAttributes attributes) {
        final CalculationContext ctx = CalculationContext.Builder.newBuilder().fromSource(CrystalPropertiesAS.Sources.SOURCE_RITUAL_PEDESTAL.createInstance(pedestal)).addUsage(CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT).build();
        return 1.0 / calculate(1.0, attributes, ctx);
    }
    
    public static int getSizeCraftingAmount(final CrystalAttributes attributes) {
        int amt = 1;
        final CrystalAttributes.Attribute sizeAttr = attributes.getAttribute(CrystalPropertiesAS.Properties.PROPERTY_SIZE);
        if (sizeAttr != null) {
            amt += 2 * sizeAttr.getTier();
        }
        return amt;
    }
    
    public static float getCollectorCrystalCollectionRate(final IndependentCrystalSource collectorSource) {
        final CalculationContext ctx = CalculationContext.Builder.newBuilder().fromSource(CrystalPropertiesAS.Sources.SOURCE_COLLECTOR_CRYSTAL.createInstance(collectorSource)).addUsage(CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL).build();
        final CrystalAttributes attr = collectorSource.getCrystalAttributes();
        return (float)calculate(1.0, attr, ctx);
    }
    
    public static float getCrystalCollectionRate(final CrystalAttributes attributes) {
        final CalculationContext ctx = CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL).build();
        return (float)calculate(1.0, attributes, ctx);
    }
    
    public static float getThroughputMultiplier(final CrystalAttributes attributes) {
        final CalculationContext ctx = CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_LENS_TRANSFER).build();
        return Mth.func_76131_a((float)calculate(1.0, attributes, ctx), 0.0f, 1.0f);
    }
    
    public static float getThroughputEffectMultiplier(final CrystalAttributes attributes) {
        final CalculationContext ctx = CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_LENS_EFFECT).build();
        return Mth.func_76131_a((float)calculate(1.0, attributes, ctx), 0.0f, 1.0f);
    }
    
    public static int getToolDurability(int durability, final ItemStack tool) {
        if (tool.getItem() instanceof CrystalAttributeItem) {
            final CrystalAttributes attr = ((CrystalAttributeItem)tool.getItem()).getAttributes(tool);
            if (attr != null) {
                final CalculationContext ctx = CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_TOOL_DURABILITY).build();
                durability = (int)Math.round(durability * calculate(1.0, attr, ctx));
            }
        }
        return durability;
    }
    
    public static float getToolEfficiency(float efficiency, final ItemStack tool) {
        if (tool.getItem() instanceof CrystalAttributeItem) {
            final CrystalAttributes attr = ((CrystalAttributeItem)tool.getItem()).getAttributes(tool);
            if (attr != null) {
                final CalculationContext ctx = CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS).build();
                efficiency *= (float)calculate(1.0, attr, ctx);
            }
        }
        return efficiency;
    }
}
