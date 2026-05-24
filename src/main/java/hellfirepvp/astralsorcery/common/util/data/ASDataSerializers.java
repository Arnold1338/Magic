package hellfirepvp.astralsorcery.common.util.data;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.network.syncher.IDataSerializer;

public class ASDataSerializers
{
    public static IDataSerializer<Long> LONG;
    public static IDataSerializer<Vector3> VECTOR;
    public static IDataSerializer<FluidStack> FLUID;
    
    static {
        ASDataSerializers.LONG = (IDataSerializer<Long>)new IDataSerializer<Long>() {
            public void write(final FriendlyByteBuf buf, final Long value) {
                buf.writeLongLE((long)value);
            }
            
            public Long read(final FriendlyByteBuf buf) {
                return buf.readLongLE();
            }
            
            public EntityDataAccessor<Long> func_187161_a(final int id) {
                return (EntityDataAccessor<Long>)new EntityDataAccessor(id, (IDataSerializer)this);
            }
            
            public Long copyValue(final Long value) {
                return new Long(value);
            }
        };
        ASDataSerializers.VECTOR = (IDataSerializer<Vector3>)new IDataSerializer<Vector3>() {
            public void write(final FriendlyByteBuf buf, final Vector3 value) {
                buf.writeDouble(value.getX());
                buf.writeDouble(value.getY());
                buf.writeDouble(value.getZ());
            }
            
            public Vector3 read(final FriendlyByteBuf buf) {
                return new Vector3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            }
            
            public EntityDataAccessor<Vector3> func_187161_a(final int id) {
                return (EntityDataAccessor<Vector3>)new EntityDataAccessor(id, (IDataSerializer)this);
            }
            
            public Vector3 copyValue(final Vector3 value) {
                return value.clone();
            }
        };
        ASDataSerializers.FLUID = (IDataSerializer<FluidStack>)new IDataSerializer<FluidStack>() {
            public void write(final FriendlyByteBuf buf, final FluidStack value) {
                ByteBufUtils.writeFluidStack(buf, value);
            }
            
            public FluidStack read(final FriendlyByteBuf buf) {
                return ByteBufUtils.readFluidStack(buf);
            }
            
            public EntityDataAccessor<FluidStack> func_187161_a(final int id) {
                return (EntityDataAccessor<FluidStack>)new EntityDataAccessor(id, (IDataSerializer)this);
            }
            
            public FluidStack copyValue(final FluidStack value) {
                return value.copy();
            }
        };
    }
}
