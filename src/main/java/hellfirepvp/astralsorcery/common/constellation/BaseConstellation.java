package hellfirepvp.astralsorcery.common.constellation;

import java.util.Collections;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.constellation.star.StarConnection;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import java.util.List;


public abstract class BaseConstellation extends ForgeRegistryEntry<IConstellation> implements IConstellation
{
    private final List<StarLocation> starLocations;
    private final List<StarConnection> connections;
    
    public BaseConstellation() {
        this.starLocations = new ArrayList<StarLocation>();
        this.connections = new ArrayList<StarConnection>();
    }
    
    public StarLocation addStar(int x, int y) {
        x %= 31;
        y %= 31;
        final StarLocation star = new StarLocation(x, y);
        if (!this.starLocations.contains(star)) {
            this.starLocations.add(star);
            return star;
        }
        return null;
    }
    
    public StarConnection addConnection(final StarLocation star1, final StarLocation star2) {
        if (star1.equals(star2)) {
            return null;
        }
        final StarConnection sc = new StarConnection(star1, star2);
        if (!this.connections.contains(sc)) {
            this.connections.add(sc);
            return sc;
        }
        return null;
    }
    
    public List<StarLocation> getStars() {
        return Collections.unmodifiableList((List<? extends StarLocation>)this.starLocations);
    }
    
    public List<StarConnection> getStarConnections() {
        return Collections.unmodifiableList((List<? extends StarConnection>)this.connections);
    }
}
