package hellfirepvp.astralsorcery.common.constellation;

public interface ConstellationTile
{
    IWeakConstellation getAttunedConstellation();
    
    boolean setAttunedConstellation(final IWeakConstellation p0);
    
    IMinorConstellation getTraitConstellation();
    
    boolean setTraitConstellation(final IMinorConstellation p0);
}
