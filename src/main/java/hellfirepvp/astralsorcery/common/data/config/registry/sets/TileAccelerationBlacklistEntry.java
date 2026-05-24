package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import java.util.Locale;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class TileAccelerationBlacklistEntry implements ConfigDataSet, Predicate<BlockEntity>
{
    private final String filterString;
    private Class<?> filteredSuperClass;
    
    public TileAccelerationBlacklistEntry(final String filterString) {
        this.filterString = filterString;
        try {
            this.filteredSuperClass = Class.forName(filterString);
        }
        catch (final ClassNotFoundException | NoClassDefFoundError e) {
            this.filteredSuperClass = null;
        }
    }
    
    @Override
    public boolean test(final BlockEntity tile) {
        final String testStr = this.filterString.toLowerCase(Locale.ROOT);
        if (testStr.isEmpty()) {
            return false;
        }
        if (this.filteredSuperClass != null) {
            return this.filteredSuperClass.isAssignableFrom(tile.getClass());
        }
        final ResourceLocation key = tile.func_200662_C().getRegistryName();
        if (key != null && key.toString().toLowerCase(Locale.ROOT).startsWith(testStr)) {
            return true;
        }
        final String className = tile.getClass().getName().toLowerCase(Locale.ROOT);
        return className.startsWith(testStr);
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return this.filterString;
    }
}
