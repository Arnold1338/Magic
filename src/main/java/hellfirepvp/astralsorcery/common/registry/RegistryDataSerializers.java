package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.Locale;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraft.network.syncher.IDataSerializer;
import hellfirepvp.astralsorcery.common.lib.DataSerializersAS;
import hellfirepvp.astralsorcery.common.util.data.ASDataSerializers;

public class RegistryDataSerializers
{
    private RegistryDataSerializers() {
    }
    
    public static void registerSerializers() {
        DataSerializersAS.LONG = register(ASDataSerializers.LONG, "long");
        DataSerializersAS.VECTOR = register(ASDataSerializers.VECTOR, "vector");
        DataSerializersAS.FLUID = register(ASDataSerializers.FLUID, "fluid");
    }
    
    private static <V, T extends IDataSerializer<V>> T register(final T dataSerializer, final String name) {
        final DataSerializerEntry entry = new DataSerializerEntry((IDataSerializer)dataSerializer);
        entry.setRegistryName(AstralSorcery.key(name.toLowerCase(Locale.ROOT)));
        AstralSorcery.getProxy().getRegistryPrimer().register(entry);
        return dataSerializer;
    }
}
