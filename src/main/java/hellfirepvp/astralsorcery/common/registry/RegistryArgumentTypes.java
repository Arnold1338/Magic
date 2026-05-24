package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.command.arguments.IArgumentSerializer;
import com.mojang.brigadier.arguments.ArgumentType;
import java.util.function.Supplier;
import net.minecraft.command.arguments.ArgumentSerializer;
import hellfirepvp.astralsorcery.common.cmd.argument.ArgumentTypeConstellation;
import hellfirepvp.astralsorcery.AstralSorcery;

public class RegistryArgumentTypes
{
    private RegistryArgumentTypes() {
    }
    
    public static void init() {
        register(AstralSorcery.key("constellation"), (Class<ArgumentType>)ArgumentTypeConstellation.class, (net.minecraft.command.arguments.IArgumentSerializer<ArgumentType>)new ArgumentSerializer((Supplier)ArgumentTypeConstellation::any));
    }
    
    private static <T extends ArgumentType<?>> void register(final ResourceLocation key, final Class<T> argumentClazz, final IArgumentSerializer<T> serializer) {
        ArgumentTypes.func_218136_a(key.toString(), (Class)argumentClazz, (IArgumentSerializer)serializer);
    }
}
