package hellfirepvp.astralsorcery.common.util.object;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.function.Predicate;

public class PredicateBuilder<T>
{
    private Predicate<T> predicate;
    
    private PredicateBuilder(final Predicate<T> predicate) {
        this.predicate = predicate;
    }
    
    public static <T> Predicate<T> joinOr(final Collection<? extends Predicate<T>> collection) {
        final PredicateBuilder<T> builder = startOr();
        collection.forEach(builder::or);
        return builder.build();
    }
    
    public static <T> Predicate<T> joinAnd(final Collection<? extends Predicate<T>> collection) {
        final PredicateBuilder<T> builder = startAnd();
        collection.forEach(builder::and);
        return builder.build();
    }
    
    public static <T> PredicateBuilder<T> startOr() {
        return new PredicateBuilder<T>(el -> false);
    }
    
    public static <T> PredicateBuilder<T> startAnd() {
        return new PredicateBuilder<T>(el -> true);
    }
    
    public PredicateBuilder<T> or(final Predicate<T> predicate) {
        this.predicate = this.predicate.or(predicate);
        return this;
    }
    
    public PredicateBuilder<T> and(final Predicate<T> predicate) {
        this.predicate = this.predicate.and(predicate);
        return this;
    }
    
    public Predicate<T> build() {
        return this.predicate;
    }
}
