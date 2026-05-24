package hellfirepvp.astralsorcery.common.auxiliary.gateway;

import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import java.io.IOException;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.HashSet;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Set;
import java.io.File;

public class CelestialGatewayFilter
{
    private final File gatewayFilter;
    private Set<RegistryKey<World>> cache;
    
    CelestialGatewayFilter() {
        this.cache = new HashSet<RegistryKey<World>>();
        this.gatewayFilter = this.loadFilter();
        this.loadCache();
    }
    
    private File loadFilter() {
        final File dataDir = AstralSorcery.getProxy().getASServerDataDirectory();
        final File gatewayFilter = new File(dataDir, "gateway_filter.dat");
        if (!gatewayFilter.exists()) {
            try {
                gatewayFilter.createNewFile();
            }
            catch (final IOException exc) {
                throw new IllegalStateException("Couldn't create plain world filter file! Are we missing file permissions?", exc);
            }
        }
        return gatewayFilter;
    }
    
    public boolean hasGateways(final ResourceLocation worldKey) {
        return this.cache.contains(worldKey);
    }
    
    void addDim(final RegistryKey<World> worldKey) {
        if (this.cache.add(worldKey)) {
            this.saveCache();
        }
    }
    
    void removeDim(final RegistryKey<World> worldKey) {
        if (this.cache.remove(worldKey)) {
            this.saveCache();
        }
    }
    
    private void loadCache() {
        try {
            final CompoundTag tag = NbtIo.func_74797_a(this.gatewayFilter);
            final ListTag list = tag.getList("list", 8);
            this.cache = new HashSet<RegistryKey<World>>();
            for (int i = 0; i < list.size(); ++i) {
                final ResourceLocation location = new ResourceLocation(list.func_150307_f(i));
                this.cache.add((RegistryKey<World>)RegistryKey.func_240903_a_(Registry.field_239699_ae_, location));
            }
        }
        catch (final IOException ignored) {
            this.cache = new HashSet<RegistryKey<World>>();
        }
    }
    
    private void saveCache() {
        try {
            final ListTag list = new ListTag();
            for (final RegistryKey<World> dimType : this.cache) {
                list.add((Object)StringTag.func_229705_a_(dimType.func_240901_a_().toString()));
            }
            final CompoundTag cmp = new CompoundTag();
            cmp.put("list", (Tag)list);
            NbtIo.func_74795_b(cmp, this.gatewayFilter);
        }
        catch (final IOException ex) {}
    }
}
