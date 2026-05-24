package hellfirepvp.astralsorcery.common.constellation.world;

import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import java.util.Random;

public class WorldContext
{
    private final long seed;
    private final CelestialEventHandler celestialHandler;
    private final ActiveCelestialsHandler activeCelestialsHandler;
    private final ConstellationHandler constellationHandler;
    private final DistributionHandler distributionHandler;
    
    public WorldContext(final long randSeed) {
        this.seed = randSeed;
        this.celestialHandler = new CelestialEventHandler(this);
        this.activeCelestialsHandler = new ActiveCelestialsHandler();
        this.constellationHandler = new ConstellationHandler(this);
        this.distributionHandler = new DistributionHandler(this);
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    @Nonnull
    public Random getRandom() {
        return this.getRandom(0L);
    }
    
    public Random getRandom(final long seedModifier) {
        return new Random(this.seed + seedModifier);
    }
    
    @Nonnull
    public Random getDayRandom() {
        final int track = this.getConstellationHandler().getLastTrackedDay();
        return new Random(this.getSeed() * 31L + track * 31);
    }
    
    @Nonnull
    public CelestialEventHandler getCelestialEventHandler() {
        return this.celestialHandler;
    }
    
    @Nonnull
    public ConstellationHandler getConstellationHandler() {
        return this.constellationHandler;
    }
    
    @Nonnull
    public DistributionHandler getDistributionHandler() {
        return this.distributionHandler;
    }
    
    @Nonnull
    public ActiveCelestialsHandler getActiveCelestialsHandler() {
        return this.activeCelestialsHandler;
    }
    
    public void tick(final World world) {
        this.celestialHandler.tick(world);
        this.constellationHandler.tick(world);
        this.distributionHandler.tick(world);
    }
}
