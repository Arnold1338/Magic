package hellfirepvp.astralsorcery.common.data.research;

import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class PlayerProgressTestAccess extends PlayerProgress
{
    @Override
    public boolean isValid() {
        return false;
    }
    
    @Override
    public Collection<ResearchProgression> getResearchProgression() {
        return (Collection<ResearchProgression>)Collections.emptyList();
    }
    
    @Override
    public List<ResourceLocation> getSeenConstellations() {
        return Collections.emptyList();
    }
    
    @Override
    public List<ResourceLocation> getKnownConstellations() {
        return Collections.emptyList();
    }
    
    @Override
    public List<ResourceLocation> getStoredConstellationPapers() {
        return Collections.emptyList();
    }
    
    @Override
    public PlayerPerkData getPerkData() {
        return new PlayerPerkData();
    }
    
    @Override
    public IMajorConstellation getAttunedConstellation() {
        return null;
    }
    
    @Override
    public ProgressionTier getTierReached() {
        return ProgressionTier.DISCOVERY;
    }
    
    @Override
    public boolean hasSeenConstellation(final ResourceLocation constellation) {
        return false;
    }
    
    @Override
    public boolean hasConstellationDiscovered(final ResourceLocation constellation) {
        return false;
    }
    
    @Override
    public boolean wasOnceAttuned() {
        return false;
    }
    
    @Override
    public boolean didReceiveTome() {
        return true;
    }
    
    @Override
    public boolean doPerkAbilities() {
        return false;
    }
}
