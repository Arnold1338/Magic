package hellfirepvp.astralsorcery.common.util;

import java.util.stream.BaseStream;
import java.util.Spliterator;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.function.Consumer;
import java.util.Comparator;
import java.util.stream.DoubleStream;
import java.util.function.ToDoubleFunction;
import java.util.stream.LongStream;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;
import java.util.function.ToIntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.Collection;
import java.util.Map;
import net.minecraft.util.Tuple;
import java.util.stream.Stream;

public class MapStream<K, V> implements Stream<Tuple<K, V>>
{
    private final Stream<Tuple<K, V>> decorated;
    
    private MapStream(final Stream<Tuple<K, V>> decorated) {
        this.decorated = decorated;
    }
    
    public static <K, V> MapStream<K, V> of(final Map<K, V> map) {
        return new MapStream<K, V>(map.entrySet().stream().map(e -> new Tuple(e.getKey(), e.getValue())));
    }
    
    public static <K, V> MapStream<K, V> of(final Collection<Tuple<K, V>> tplCollection) {
        return new MapStream<K, V>(tplCollection.stream());
    }
    
    public static <K, V> MapStream<K, V> of(final Stream<Tuple<K, V>> tplStream) {
        return new MapStream<K, V>(tplStream);
    }
    
    public static <K, V> MapStream<K, V> ofKeys(final Collection<K> collection, final Function<K, V> valueProvider) {
        return ofKeys(collection.stream(), valueProvider);
    }
    
    public static <K, V> MapStream<K, V> ofKeys(final Stream<K> stream, final Function<K, V> valueProvider) {
        return new MapStream<K, V>(stream.map(k -> new Tuple(k, valueProvider.apply(k))));
    }
    
    public static <K, V> MapStream<K, V> ofValues(final Collection<V> collection, final Function<V, K> keyProvider) {
        return ofValues(collection.stream(), keyProvider);
    }
    
    public static <K, V> MapStream<K, V> ofValues(final Stream<V> stream, final Function<V, K> keyProvider) {
        return new MapStream<K, V>(stream.map(v -> new Tuple(keyProvider.apply(v), v)));
    }
    
    public static <K, V> void forEach(final Map<K, V> map, final BiConsumer<K, V> forEachFn) {
        of(map).forEach(tpl -> forEachFn.accept(tpl.getA(), tpl.getB()));
    }
    
    public Map<K, V> toMap() {
        return this.decorated.collect(Collectors.toMap((Function<? super Tuple<K, V>, ? extends K>)Tuple::func_76341_a, (Function<? super Tuple<K, V>, ? extends V>)Tuple::func_76340_b));
    }
    
    public <R> List<R> toList(final BiFunction<K, V, R> flatFunction) {
        return this.decorated.map(tpl -> flatFunction.apply(tpl.getA(), tpl.getB())).collect((Collector<? super Object, ?, List<R>>)Collectors.toList());
    }
    
    public List<Tuple<K, V>> toTupleList() {
        return this.decorated.collect((Collector<? super Tuple<K, V>, ?, List<Tuple<K, V>>>)Collectors.toList());
    }
    
    public <R> MapStream<K, R> mapValue(final Function<V, R> valueMapper) {
        return of(this.decorated.map(tpl -> new Tuple(tpl.getA(), valueMapper.apply(tpl.getB()))));
    }
    
    public <R> MapStream<R, V> mapKey(final Function<K, R> keyMapper) {
        return of(this.decorated.map(tpl -> new Tuple(keyMapper.apply(tpl.getA()), tpl.getB())));
    }
    
    public <R> Stream<R> flatten(final BiFunction<K, V, R> flatFunction) {
        return this.decorated.map(tpl -> flatFunction.apply(tpl.getA(), tpl.getB()));
    }
    
    public MapStream<K, V> filterKey(final Predicate<K> predicate) {
        return of(this.decorated.filter(tpl -> predicate.test(tpl.getA())));
    }
    
    public MapStream<K, V> filterValue(final Predicate<V> predicate) {
        return of(this.decorated.filter(tpl -> predicate.test(tpl.getB())));
    }
    
    public Stream<V> valueStream() {
        return this.decorated.map((Function<? super Tuple<K, V>, ? extends V>)Tuple::func_76340_b);
    }
    
    public Stream<K> keyStream() {
        return this.decorated.map((Function<? super Tuple<K, V>, ? extends K>)Tuple::func_76341_a);
    }
    
    @Override
    public Stream<Tuple<K, V>> filter(final Predicate<? super Tuple<K, V>> predicate) {
        return this.decorated.filter(predicate);
    }
    
    @Override
    public <R> Stream<R> map(final Function<? super Tuple<K, V>, ? extends R> mapper) {
        return this.decorated.map(mapper);
    }
    
    @Override
    public IntStream mapToInt(final ToIntFunction<? super Tuple<K, V>> mapper) {
        return this.decorated.mapToInt(mapper);
    }
    
    @Override
    public LongStream mapToLong(final ToLongFunction<? super Tuple<K, V>> mapper) {
        return this.decorated.mapToLong(mapper);
    }
    
    @Override
    public DoubleStream mapToDouble(final ToDoubleFunction<? super Tuple<K, V>> mapper) {
        return this.decorated.mapToDouble(mapper);
    }
    
    @Override
    public <R> Stream<R> flatMap(final Function<? super Tuple<K, V>, ? extends Stream<? extends R>> mapper) {
        return this.decorated.flatMap(mapper);
    }
    
    @Override
    public IntStream flatMapToInt(final Function<? super Tuple<K, V>, ? extends IntStream> mapper) {
        return this.decorated.flatMapToInt(mapper);
    }
    
    @Override
    public LongStream flatMapToLong(final Function<? super Tuple<K, V>, ? extends LongStream> mapper) {
        return this.decorated.flatMapToLong(mapper);
    }
    
    @Override
    public DoubleStream flatMapToDouble(final Function<? super Tuple<K, V>, ? extends DoubleStream> mapper) {
        return this.decorated.flatMapToDouble(mapper);
    }
    
    @Override
    public Stream<Tuple<K, V>> distinct() {
        return this.decorated.distinct();
    }
    
    @Override
    public Stream<Tuple<K, V>> sorted() {
        return this.decorated.sorted();
    }
    
    @Override
    public Stream<Tuple<K, V>> sorted(final Comparator<? super Tuple<K, V>> comparator) {
        return this.decorated.sorted(comparator);
    }
    
    @Override
    public Stream<Tuple<K, V>> peek(final Consumer<? super Tuple<K, V>> action) {
        return this.decorated.peek(action);
    }
    
    @Override
    public Stream<Tuple<K, V>> limit(final long maxSize) {
        return this.decorated.limit(maxSize);
    }
    
    @Override
    public Stream<Tuple<K, V>> skip(final long n) {
        return this.decorated.skip(n);
    }
    
    @Override
    public void forEach(final Consumer<? super Tuple<K, V>> action) {
        this.decorated.forEach(action);
    }
    
    public void forEach(final BiConsumer<K, V> forEachFn) {
        this.decorated.forEach(tpl -> forEachFn.accept(tpl.getA(), tpl.getB()));
    }
    
    @Override
    public void forEachOrdered(final Consumer<? super Tuple<K, V>> action) {
        this.decorated.forEachOrdered(action);
    }
    
    @Override
    public Object[] toArray() {
        return this.decorated.toArray();
    }
    
    @Override
    public <A> A[] toArray(final IntFunction<A[]> generator) {
        return this.decorated.toArray(generator);
    }
    
    @Override
    public Tuple<K, V> reduce(final Tuple<K, V> identity, final BinaryOperator<Tuple<K, V>> accumulator) {
        return this.decorated.reduce(identity, accumulator);
    }
    
    @Override
    public Optional<Tuple<K, V>> reduce(final BinaryOperator<Tuple<K, V>> accumulator) {
        return this.decorated.reduce(accumulator);
    }
    
    @Override
    public <U> U reduce(final U identity, final BiFunction<U, ? super Tuple<K, V>, U> accumulator, final BinaryOperator<U> combiner) {
        return this.decorated.reduce(identity, accumulator, combiner);
    }
    
    @Override
    public <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super Tuple<K, V>> accumulator, final BiConsumer<R, R> combiner) {
        return this.decorated.collect(supplier, accumulator, combiner);
    }
    
    @Override
    public <R, A> R collect(final Collector<? super Tuple<K, V>, A, R> collector) {
        return this.decorated.collect(collector);
    }
    
    @Override
    public Optional<Tuple<K, V>> min(final Comparator<? super Tuple<K, V>> comparator) {
        return this.decorated.min(comparator);
    }
    
    @Override
    public Optional<Tuple<K, V>> max(final Comparator<? super Tuple<K, V>> comparator) {
        return this.decorated.max(comparator);
    }
    
    @Override
    public long count() {
        return this.decorated.count();
    }
    
    @Override
    public boolean anyMatch(final Predicate<? super Tuple<K, V>> predicate) {
        return this.decorated.anyMatch(predicate);
    }
    
    @Override
    public boolean allMatch(final Predicate<? super Tuple<K, V>> predicate) {
        return this.decorated.allMatch(predicate);
    }
    
    @Override
    public boolean noneMatch(final Predicate<? super Tuple<K, V>> predicate) {
        return this.decorated.noneMatch(predicate);
    }
    
    @Override
    public Optional<Tuple<K, V>> findFirst() {
        return this.decorated.findFirst();
    }
    
    @Override
    public Optional<Tuple<K, V>> findAny() {
        return this.decorated.findAny();
    }
    
    @Override
    public Iterator<Tuple<K, V>> iterator() {
        return this.decorated.iterator();
    }
    
    @Override
    public Spliterator<Tuple<K, V>> spliterator() {
        return this.decorated.spliterator();
    }
    
    @Override
    public boolean isParallel() {
        return this.decorated.isParallel();
    }
    
    @Override
    public Stream<Tuple<K, V>> sequential() {
        return this.decorated.sequential();
    }
    
    @Override
    public Stream<Tuple<K, V>> parallel() {
        return this.decorated.parallel();
    }
    
    @Override
    public Stream<Tuple<K, V>> unordered() {
        return this.decorated.unordered();
    }
    
    @Override
    public Stream<Tuple<K, V>> onClose(final Runnable closeHandler) {
        return this.decorated.onClose(closeHandler);
    }
    
    @Override
    public void close() {
        this.decorated.close();
    }
}
