package hellfirepvp.astralsorcery.common.perk.data;

import java.util.Objects;
import java.util.Collections;
import java.util.HashSet;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;

public class LoadedPerkData
{
    private final AbstractPerk perk;
    private final JsonObject perkDataObject;
    private final Set<ResourceLocation> connections;
    
    public LoadedPerkData(final AbstractPerk perk, final JsonObject perkDataObject) {
        this.connections = new HashSet<ResourceLocation>();
        this.perk = perk;
        this.perkDataObject = perkDataObject;
    }
    
    void addConnection(final ResourceLocation to) {
        this.connections.add(to);
    }
    
    public Set<ResourceLocation> getConnections() {
        return Collections.unmodifiableSet((Set<? extends ResourceLocation>)this.connections);
    }
    
    public AbstractPerk getPerk() {
        return this.perk;
    }
    
    public JsonObject getPerkDataObject() {
        return this.perkDataObject;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final LoadedPerkData that = (LoadedPerkData)o;
        return Objects.equals(this.perk.getRegistryName(), that.perk.getRegistryName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.perk.getRegistryName());
    }
}
