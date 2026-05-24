package hellfirepvp.astralsorcery.common.advancement.instance;

import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.util.JSONUtils;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.loot.ConditionArraySerializer;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Set;
import net.minecraft.advancements.criterion.CriterionInstance;

public class ConstellationInstance extends CriterionInstance
{
    private boolean constellationMajor;
    private boolean constellationWeak;
    private boolean constellationMinor;
    private final Set<IConstellation> constellations;
    
    private ConstellationInstance(final ResourceLocation id) {
        super(id, EntityPredicate.AndPredicate.field_234582_a_);
        this.constellationMajor = false;
        this.constellationWeak = false;
        this.constellationMinor = false;
        this.constellations = new HashSet<IConstellation>();
    }
    
    public static ConstellationInstance any(final ResourceLocation type) {
        return new ConstellationInstance(type);
    }
    
    public static ConstellationInstance anyMajor(final ResourceLocation type) {
        final ConstellationInstance instance = new ConstellationInstance(type);
        instance.constellationMajor = true;
        return instance;
    }
    
    public static ConstellationInstance anyWeak(final ResourceLocation type) {
        final ConstellationInstance instance = new ConstellationInstance(type);
        instance.constellationWeak = true;
        return instance;
    }
    
    public static ConstellationInstance anyMinor(final ResourceLocation type) {
        final ConstellationInstance instance = new ConstellationInstance(type);
        instance.constellationMinor = true;
        return instance;
    }
    
    public static ConstellationInstance anyOf(final ResourceLocation type, final IConstellation... cst) {
        final ConstellationInstance instance = new ConstellationInstance(type);
        instance.constellations.addAll(Arrays.asList(cst));
        return instance;
    }
    
    public JsonObject func_230240_a_(final ConditionArraySerializer conditions) {
        final JsonObject out = super.func_230240_a_(conditions);
        if (this.constellationMajor) {
            out.addProperty("major", Boolean.valueOf(true));
        }
        if (this.constellationWeak) {
            out.addProperty("weak", Boolean.valueOf(true));
        }
        if (this.constellationMinor) {
            out.addProperty("minor", Boolean.valueOf(true));
        }
        if (!this.constellations.isEmpty()) {
            final JsonArray names = new JsonArray();
            for (final IConstellation cst : this.constellations) {
                names.add(cst.getRegistryName().toString());
            }
            out.add("constellations", (JsonElement)names);
        }
        return out;
    }
    
    public static ConstellationInstance deserialize(final ResourceLocation id, final JsonObject json) {
        final ConstellationInstance instance = new ConstellationInstance(id);
        instance.constellationMajor = JSONUtils.func_151209_a(json, "major", false);
        instance.constellationWeak = JSONUtils.func_151209_a(json, "weak", false);
        instance.constellationMinor = JSONUtils.func_151209_a(json, "minor", false);
        final JsonArray constellationNames = JSONUtils.func_151213_a(json, "constellations", new JsonArray());
        for (int idx = 0; idx < constellationNames.size(); ++idx) {
            final JsonElement element = constellationNames.get(idx);
            final String key = JSONUtils.func_151206_a(element, String.format("constellations[%s]", idx));
            final IConstellation cst = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(new ResourceLocation(key));
            if (cst == null) {
                throw new IllegalArgumentException(String.format("Unknown constellation: %s - at constellations[%s]", key, idx));
            }
            instance.constellations.add(cst);
        }
        return instance;
    }
    
    public boolean test(final IConstellation discovered) {
        return (!this.constellationMajor || discovered instanceof IMajorConstellation) && (!this.constellationWeak || (discovered instanceof IWeakConstellation && !(discovered instanceof IMajorConstellation))) && (!this.constellationMinor || discovered instanceof IMinorConstellation) && (this.constellations.isEmpty() || this.constellations.contains(discovered));
    }
}
