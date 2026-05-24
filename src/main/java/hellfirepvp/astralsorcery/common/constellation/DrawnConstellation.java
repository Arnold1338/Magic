package hellfirepvp.astralsorcery.common.constellation;

import java.awt.Point;

public class DrawnConstellation
{
    public static final int CONSTELLATION_DRAW_SIZE = 30;
    public static final float CONSTELLATION_SIZE_PART = 1.875f;
    public static final float CONSTELLATION_STAR_SIZE = 4.6875f;
    private final Point point;
    private final IConstellation constellation;
    
    public DrawnConstellation(final Point point, final IConstellation constellation) {
        this.point = point;
        this.constellation = constellation;
    }
    
    public IConstellation getConstellation() {
        return this.constellation;
    }
    
    public Point getPoint() {
        return this.point;
    }
}
