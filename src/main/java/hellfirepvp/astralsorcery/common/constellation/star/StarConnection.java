package hellfirepvp.astralsorcery.common.constellation.star;

import hellfirepvp.astralsorcery.common.util.data.BiDiPair;

public class StarConnection extends BiDiPair<StarLocation, StarLocation>
{
    public final StarLocation from;
    public final StarLocation to;
    
    public StarConnection(final StarLocation from, final StarLocation to) {
        super(from, to);
        this.from = from;
        this.to = to;
    }
}
