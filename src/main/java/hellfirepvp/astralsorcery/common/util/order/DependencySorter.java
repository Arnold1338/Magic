package hellfirepvp.astralsorcery.common.util.order;

import java.util.Comparator;
import com.google.common.graph.Graph;
import net.minecraftforge.fml.loading.toposort.TopologicalSort;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.function.BiPredicate;
import java.util.List;
import java.util.function.Consumer;
import com.google.common.graph.GraphBuilder;
import java.util.Collection;
import com.google.common.graph.MutableGraph;

public class DependencySorter<T>
{
    private final MutableGraph<T> graph;
    
    private DependencySorter(final Collection<T> itemsToSort) {
        this.graph = (MutableGraph<T>)GraphBuilder.directed().build();
        itemsToSort.forEach(this.graph::addNode);
    }
    
    public static <T extends OrderSortable> List<T> getSorted(final Collection<T> itemsToSort) {
        return getSorted(itemsToSort, OrderSortable::isBefore, OrderSortable::isAfter);
    }
    
    public static <T> List<T> getSorted(final Collection<T> itemsToSort, final BiPredicate<T, Object> isBefore, final BiPredicate<T, Object> isAfter) {
        final DependencySorter<T> sorter = new DependencySorter<T>(itemsToSort);
        for (final T item : itemsToSort) {
            final List<T> before = Lists.newArrayList();
            final List<T> after = Lists.newArrayList();
            for (final Object otherItem : itemsToSort) {
                if (item == otherItem) {
                    continue;
                }
                if (isBefore.test(item, otherItem)) {
                    before.add((T)otherItem);
                }
                if (!isAfter.test(item, otherItem)) {
                    continue;
                }
                after.add((T)otherItem);
            }
            sorter.setOrdering(item, before, after);
        }
        return sorter.getSorted();
    }
    
    private void setOrdering(final T node, final Collection<T> before, final Collection<T> after) {
        before.forEach(n -> this.graph.putEdge(node, n));
        after.forEach(n -> this.graph.putEdge(n, node));
    }
    
    private List<T> getSorted() {
        return TopologicalSort.topologicalSort((Graph)this.graph, (Comparator)null);
    }
}
