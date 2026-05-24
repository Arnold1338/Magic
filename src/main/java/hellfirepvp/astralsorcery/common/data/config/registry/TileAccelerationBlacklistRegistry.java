package hellfirepvp.astralsorcery.common.data.config.registry;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.tile.TileVanishing;
import hellfirepvp.astralsorcery.common.tile.TileTreeBeaconComponent;
import hellfirepvp.astralsorcery.common.tile.TileTranslucentBlock;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import hellfirepvp.astralsorcery.common.tile.TileSpectralRelay;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import hellfirepvp.astralsorcery.common.tile.TileRitualLink;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.tile.base.network.TileSourceBase;
import hellfirepvp.astralsorcery.common.tile.base.network.TileTransmissionBase;
import net.minecraft.world.level.block.entity.LockableLootTileEntity;
import net.minecraft.world.level.block.entity.PistonTileEntity;
import java.util.Iterator;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import com.google.common.collect.Lists;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.TileAccelerationBlacklistEntry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;

public class TileAccelerationBlacklistRegistry extends ConfigDataAdapter<TileAccelerationBlacklistEntry>
{
    public static final TileAccelerationBlacklistRegistry INSTANCE;
    private final List<Class<?>> erroredTiles;
    
    private TileAccelerationBlacklistRegistry() {
        this.erroredTiles = Lists.newArrayList();
    }
    
    public boolean canBeInfluenced(final BlockEntity tile) {
        if (!(tile instanceof ITickableTileEntity)) {
            return false;
        }
        final Class<?> tileClass = tile.getClass();
        if (this.erroredTiles.contains(tileClass)) {
            return false;
        }
        for (final Class<?> excludedTile : this.erroredTiles) {
            if (excludedTile.isAssignableFrom(tileClass)) {
                return false;
            }
        }
        for (final TileAccelerationBlacklistEntry entry : this.getConfiguredValues()) {
            if (entry.test(tile)) {
                return false;
            }
        }
        return true;
    }
    
    public void addErrored(final BlockEntity tile) {
        if (tile != null && !this.erroredTiles.contains(tile.getClass())) {
            this.erroredTiles.add(tile.getClass());
        }
    }
    
    @Override
    public List<TileAccelerationBlacklistEntry> getDefaultValues() {
        return Lists.newArrayList((Object[])new TileAccelerationBlacklistEntry[] { new TileAccelerationBlacklistEntry(PistonTileEntity.class.getName()), new TileAccelerationBlacklistEntry(LockableLootTileEntity.class.getName()), new TileAccelerationBlacklistEntry("appeng"), new TileAccelerationBlacklistEntry("raoulvdberge.refinedstorage"), new TileAccelerationBlacklistEntry(TileTransmissionBase.class.getName()), new TileAccelerationBlacklistEntry(TileSourceBase.class.getName()), new TileAccelerationBlacklistEntry(TileAltar.class.getName()), new TileAccelerationBlacklistEntry(TileAttunementAltar.class.getName()), new TileAccelerationBlacklistEntry(TileObservatory.class.getName()), new TileAccelerationBlacklistEntry(TileRitualLink.class.getName()), new TileAccelerationBlacklistEntry(TileRitualPedestal.class.getName()), new TileAccelerationBlacklistEntry(TileSpectralRelay.class.getName()), new TileAccelerationBlacklistEntry(TileTelescope.class.getName()), new TileAccelerationBlacklistEntry(TileTranslucentBlock.class.getName()), new TileAccelerationBlacklistEntry(TileTreeBeaconComponent.class.getName()), new TileAccelerationBlacklistEntry(TileVanishing.class.getName()) });
    }
    
    @Override
    public String getSectionName() {
        return "tile_acceleration_blacklist";
    }
    
    @Override
    public String getCommentDescription() {
        return "Accepts & matches against strings: 1) what a tileentity-type's registry name starts with, 2) what a tileentity's fully qualified class name starts with, 3) (special case) a fully qualified class name (Instances & sub-class instance of that class will be blacklisted)";
    }
    
    @Override
    public String getTranslationKey() {
        return this.translationKey("data");
    }
    
    @Override
    public Predicate<Object> getValidator() {
        return obj -> obj instanceof String;
    }
    
    @Nullable
    @Override
    public TileAccelerationBlacklistEntry deserialize(final String string) throws IllegalArgumentException {
        if (string.isEmpty()) {
            throw new IllegalArgumentException("TileAccelerationBlacklist entry filter must not be empty!");
        }
        return new TileAccelerationBlacklistEntry(string);
    }
    
    static {
        INSTANCE = new TileAccelerationBlacklistRegistry();
    }
}
