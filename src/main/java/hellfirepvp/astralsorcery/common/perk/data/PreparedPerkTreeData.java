package hellfirepvp.astralsorcery.common.perk.data;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Collections;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.Iterator;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import java.util.HashMap;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.perk.node.RootPerk;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.util.Tuple;
import java.util.Collection;
import java.util.Map;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import java.util.List;

public class PreparedPerkTreeData
{
    private final List<PerkTreePoint<AbstractPerk>> treePoints;
    private final Map<AbstractPerk, Collection<AbstractPerk>> doubleConnections;
    private final List<Tuple<AbstractPerk, AbstractPerk>> connections;
    private final Map<IConstellation, RootPerk> rootPerks;
    private long version;
    
    PreparedPerkTreeData() {
        this.treePoints = new LinkedList<PerkTreePoint<AbstractPerk>>();
        this.doubleConnections = new HashMap<AbstractPerk, Collection<AbstractPerk>>();
        this.connections = new LinkedList<Tuple<AbstractPerk, AbstractPerk>>();
        this.rootPerks = new HashMap<IConstellation, RootPerk>();
        this.version = 0L;
    }
    
    static PreparedPerkTreeData create(final Collection<LoadedPerkData> perks) {
        final PreparedPerkTreeData treeData = new PreparedPerkTreeData();
        perks.stream().map((Function<? super LoadedPerkData, ?>)LoadedPerkData::getPerk).forEach(perk -> {
            if (perk instanceof RootPerk) {
                treeData.rootPerks.put(((RootPerk)perk).getConstellation(), (RootPerk)perk);
            }
            final PerkTreePoint<? extends AbstractPerk> offsetPoint = perk.getPoint();
            if (treeData.treePoints.contains(offsetPoint)) {
                new IllegalArgumentException("Tried to register perk-point at already placed position: " + offsetPoint.getOffset().toString());
                throw;
            }
            else {
                treeData.treePoints.add((PerkTreePoint<AbstractPerk>)offsetPoint);
                return;
            }
        });
        perks.forEach(perkData -> {
            perkData.getConnections().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final ResourceLocation connection = iterator.next();
                final AbstractPerk perkTo = treeData.getPerk(perk -> connection.equals((Object)perk.getRegistryName())).orElseThrow(() -> {
                    new JsonParseException("Cannot connect to unknown perk: " + connection);
                    final JsonParseException ex2;
                    return (Throwable)(Throwable)ex2;
                });
                treeData.getConnector(perkTo).ifPresent(connector -> connector.connect(perkData.getPerk()));
            }
            return;
        });
        treeData.version = treeData.computeTreeHash();
        return treeData;
    }
    
    public long getVersion() {
        return this.version;
    }
    
    public Optional<AbstractPerk> getPerk(final Predicate<AbstractPerk> test) {
        return this.treePoints.stream().map((Function<? super Object, ? extends AbstractPerk>)PerkTreePoint::getPerk).filter(test).findFirst();
    }
    
    public Optional<? extends AbstractPerk> getPerk(final float x, final float y) {
        return this.treePoints.stream().filter(treePoint -> treePoint.getOffset().distance(x, y) <= 1.0E-4).findFirst().map((Function<? super Object, ? extends AbstractPerk>)PerkTreePoint::getPerk);
    }
    
    @Nullable
    public RootPerk getRootPerk(final IConstellation constellation) {
        return this.rootPerks.get(constellation);
    }
    
    public Collection<AbstractPerk> getConnectedPerks(final AbstractPerk perk) {
        return this.doubleConnections.getOrDefault(perk, Lists.newArrayList());
    }
    
    public Collection<PerkTreePoint<?>> getPerkPoints() {
        return (Collection<PerkTreePoint<?>>)Collections.unmodifiableList((List<?>)this.treePoints);
    }
    
    @OnlyIn(Dist.CLIENT)
    public Collection<Tuple<AbstractPerk, AbstractPerk>> getConnections() {
        return (Collection<Tuple<AbstractPerk, AbstractPerk>>)Collections.unmodifiableList((List<?>)this.connections);
    }
    
    private Optional<PointConnector> getConnector(final AbstractPerk point) {
        if (point == null) {
            return Optional.empty();
        }
        if (this.treePoints.contains(point.getPoint())) {
            return Optional.of(new PointConnector(point));
        }
        return Optional.empty();
    }
    
    private long computeTreeHash() {
        final long[] perkHash = new long[this.treePoints.size()];
        for (int i = 0; i < this.treePoints.size(); ++i) {
            final PerkTreePoint<? extends AbstractPerk> treePoint = this.treePoints.get(i);
            perkHash[i] = ((long)((AbstractPerk)treePoint.getPerk()).hashCode() << 32 ^ (long)treePoint.getOffset().hashCode());
        }
        long hash = 1L;
        for (final long element : perkHash) {
            final long elementHash = element ^ element >>> 32;
            hash = 31L * hash + elementHash;
        }
        return hash;
    }
    
    public void clearPerkCache(final LogicalSide side) {
        this.treePoints.stream().map((Function<? super Object, ?>)PerkTreePoint::getPerk).forEach(p -> p.clearCaches(side));
    }
    
    public class PointConnector
    {
        private final AbstractPerk point;
        
        private PointConnector(final AbstractPerk point) {
            this.point = point;
        }
        
        public PointConnector connect(final AbstractPerk other) {
            if (other == null) {
                return this;
            }
            Collection<AbstractPerk> pointsTo = PreparedPerkTreeData.this.doubleConnections.computeIfAbsent(other, p -> new LinkedList());
            if (!pointsTo.contains(this.point)) {
                pointsTo.add(this.point);
            }
            pointsTo = PreparedPerkTreeData.this.doubleConnections.computeIfAbsent(this.point, p -> new LinkedList());
            if (!pointsTo.contains(other)) {
                pointsTo.add(other);
            }
            final Tuple<AbstractPerk, AbstractPerk> connection = (Tuple<AbstractPerk, AbstractPerk>)new Tuple((Object)this.point, (Object)other);
            final Tuple<AbstractPerk, AbstractPerk> reverse = (Tuple<AbstractPerk, AbstractPerk>)new Tuple((Object)other, (Object)this.point);
            if (!PreparedPerkTreeData.this.connections.contains(connection) && !PreparedPerkTreeData.this.connections.contains(reverse)) {
                PreparedPerkTreeData.this.connections.add(connection);
            }
            return this;
        }
        
        public PointConnector connect(final PointConnector other) {
            return this.connect(other.point);
        }
        
        public PointConnector chain(final PointConnector other) {
            this.connect(other.point);
            return other;
        }
    }
}
