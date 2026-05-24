package hellfirepvp.astralsorcery.common.util;

import java.util.Objects;
import java.util.function.Function;

public interface TriFunction<S, T, U, R>
{
    R apply(final S p0, final T p1, final U p2);
    
    default <V> TriFunction<S, T, U, V> andThen(final Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (s, t, u) -> after.apply(this.apply(s, t, u));
    }
}
