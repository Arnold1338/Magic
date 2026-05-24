package hellfirepvp.astralsorcery.common.cmd.argument;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraftforge.registries.IForgeRegistryEntry;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import com.mojang.brigadier.ImmutableStringReader;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.StringReader;
import java.util.function.Predicate;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import com.mojang.brigadier.arguments.ArgumentType;

public class ArgumentTypeConstellation implements ArgumentType<IConstellation>
{
    public static final SimpleCommandExceptionType CONSTELLATION_NOT_FOUND;
    private final Predicate<IConstellation> filter;
    
    private ArgumentTypeConstellation(final Predicate<IConstellation> filter) {
        this.filter = filter;
    }
    
    public IConstellation parse(final StringReader reader) throws CommandSyntaxException {
        final ResourceLocation name = ResourceLocation.func_195826_a(reader);
        for (final IConstellation cst : ConstellationRegistry.getAllConstellations()) {
            if (!this.filter.test(cst)) {
                continue;
            }
            if (cst.getRegistryName().equals((Object)name)) {
                return cst;
            }
        }
        throw ArgumentTypeConstellation.CONSTELLATION_NOT_FOUND.createWithContext((ImmutableStringReader)reader);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return ISuggestionProvider.func_197014_a((Iterable)ConstellationRegistry.getAllConstellations().stream().filter(this.filter).map((Function<? super IConstellation, ?>)IForgeRegistryEntry::getRegistryName).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), builder);
    }
    
    public static ArgumentTypeConstellation weak() {
        return new ArgumentTypeConstellation(c -> c instanceof IWeakConstellation);
    }
    
    public static ArgumentTypeConstellation major() {
        return new ArgumentTypeConstellation(c -> c instanceof IMajorConstellation);
    }
    
    public static ArgumentTypeConstellation minor() {
        return new ArgumentTypeConstellation(c -> c instanceof IMinorConstellation);
    }
    
    public static ArgumentTypeConstellation any() {
        return new ArgumentTypeConstellation(c -> true);
    }
    
    static {
        CONSTELLATION_NOT_FOUND = new SimpleCommandExceptionType((Message)new Component("astralsorcery.command.argument.constellation.notfound"));
    }
}
