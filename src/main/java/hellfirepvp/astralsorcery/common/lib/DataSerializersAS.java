package hellfirepvp.astralsorcery.common.lib;

import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.network.syncher.IDataSerializer;

public class DataSerializersAS
{
    public static IDataSerializer<Long> LONG;
    public static IDataSerializer<Vector3> VECTOR;
    public static IDataSerializer<FluidStack> FLUID;
    
    private DataSerializersAS() {
    }
}
