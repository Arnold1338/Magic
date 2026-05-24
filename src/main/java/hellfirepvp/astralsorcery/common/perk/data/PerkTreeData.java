package hellfirepvp.astralsorcery.common.perk.data;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collection;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import java.util.HashSet;
import java.util.Set;

public class PerkTreeData
{
    private final Set<LoadedPerkData> perks;
    
    public PerkTreeData() {
        this.perks = new HashSet<LoadedPerkData>();
    }
    
    LoadedPerkData addPerk(final AbstractPerk perk, final JsonObject perkDataObject) {
        final LoadedPerkData data = new LoadedPerkData(perk, perkDataObject);
        this.perks.add(data);
        return data;
    }
    
    public PreparedPerkTreeData prepare() {
        return PreparedPerkTreeData.create(this.perks);
    }
    
    public Collection<JsonObject> getAsDataTree() {
        return this.perks.stream().map((Function<? super Object, ?>)LoadedPerkData::getPerkDataObject).collect((Collector<? super Object, ?, Collection<JsonObject>>)Collectors.toList());
    }
}
