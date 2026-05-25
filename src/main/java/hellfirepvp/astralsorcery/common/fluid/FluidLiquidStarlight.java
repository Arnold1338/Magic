package hellfirepvp.astralsorcery.common.fluid;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateContainer;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class FluidLiquidStarlight extends ForgeFlowingFluid
{
    private FluidLiquidStarlight(final ForgeFlowingFluid.Properties properties) {
        super(properties);
    }
    
    public static FluidAttributes.Builder addAttributes(final FluidAttributes.Builder attributeBuilder) {
        return attributeBuilder.rarity(Rarity.EPIC).luminosity(15).density(1001).viscosity(300).temperature(40);
    }
    
    public static class Flowing extends FluidLiquidStarlight
    {
        public Flowing(final ForgeFlowingFluid.Properties properties) {
            super(properties, null);
            this.func_207183_f((FluidState)((FluidState)this.func_207182_e().func_177621_b()).setValue((Property)Flowing.field_207210_b, (Comparable)7));
        }
        
        protected void func_207184_a(final StateContainer.Builder<Fluid, FluidState> builder) {
            super.func_207184_a((StateContainer.Builder)builder);
            builder.func_206894_a(new Property[] { (Property)Flowing.field_207210_b });
        }
        
        public int func_207192_d(final FluidState state) {
            return (int)state.getValue((Property)Flowing.field_207210_b);
        }
        
        public boolean func_207193_c(final FluidState state) {
            return false;
        }
    }
    
    public static class Source extends FluidLiquidStarlight
    {
        public Source(final ForgeFlowingFluid.Properties properties) {
            super(properties, null);
        }
        
        public int func_207192_d(final FluidState state) {
            return 8;
        }
        
        public boolean func_207193_c(final FluidState state) {
            return true;
        }
    }
}
