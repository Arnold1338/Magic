package hellfirepvp.astralsorcery.common.constellation.star;

import java.awt.Point;

public class StarLocation
{
    public final int x;
    public final int y;
    
    public StarLocation(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getDistanceToOrigin() {
        return this.x + this.y;
    }
    
    public Point asPoint() {
        return new Point(this.x, this.y);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StarLocation tuple = (StarLocation)o;
        return this.x == tuple.x && this.y == tuple.y;
    }
    
    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        return result;
    }
}
