package hellfirepvp.astralsorcery.common.perk.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import java.util.Iterator;
import hellfirepvp.astralsorcery.AstralSorcery;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.util.MapStream;
import com.google.gson.JsonObject;
import java.util.Collection;
import net.minecraft.util.profiling.IProfiler;
import net.minecraft.resources.IResourceManager;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import com.google.gson.Gson;
import net.minecraft.client.resources.JsonReloadListener;

public class PerkTreeLoader extends JsonReloadListener
{
    private static final Gson GSON;
    public static final PerkTreeLoader INSTANCE;
    
    private PerkTreeLoader() {
        super(PerkTreeLoader.GSON, "perks");
    }
    
    protected void apply(final Map<ResourceLocation, JsonElement> dataMap, final IResourceManager resourceManager, final IProfiler profiler) {
        final Collection<JsonObject> loadingPerkObjects = MapStream.of(dataMap).filterKey(key -> !key.func_110623_a().startsWith("_")).filterValue(JsonElement::isJsonObject).mapValue(JsonElement::getAsJsonObject).valueStream().collect((Collector<? super JsonObject, ?, Collection<JsonObject>>)Collectors.toList());
        PerkTree.PERK_TREE.updateOriginPerkTree(loadPerkTree(loadingPerkObjects));
    }
    
    public static PerkTreeData loadPerkTree(final Collection<JsonObject> perkTreeObjects) {
        final PerkTreeData newTree = new PerkTreeData();
        int count = 0;
        for (final JsonObject serializedPerkData : perkTreeObjects) {
            final ResourceLocation perkRegistryName = new ResourceLocation(JSONUtils.func_151200_h(serializedPerkData, "registry_name"));
            ResourceLocation customClass = PerkTypeHandler.DEFAULT.getKey();
            if (serializedPerkData.has("perk_class")) {
                customClass = new ResourceLocation(JSONUtils.func_151200_h(serializedPerkData, "perk_class"));
                if (!PerkTypeHandler.hasCustomType(customClass)) {
                    throw new JsonParseException("Unknown perk_class: " + customClass.toString());
                }
            }
            final float posX = JSONUtils.func_151217_k(serializedPerkData, "x");
            final float posY = JSONUtils.func_151217_k(serializedPerkData, "y");
            final AbstractPerk perk = PerkTypeHandler.convert(perkRegistryName, posX, posY, customClass);
            if (serializedPerkData.has("name")) {
                final String name = JSONUtils.func_151200_h(serializedPerkData, "name");
                perk.setName(name);
            }
            if (serializedPerkData.has("hiddenUnlessAllocated")) {
                perk.setHiddenUnlessAllocated(JSONUtils.func_151212_i(serializedPerkData, "hiddenUnlessAllocated"));
            }
            if (serializedPerkData.has("data")) {
                final JsonObject perkData = JSONUtils.func_152754_s(serializedPerkData, "data");
                perk.deserializeData(perkData);
            }
            final LoadedPerkData connector = newTree.addPerk(perk, serializedPerkData);
            if (serializedPerkData.has("connection")) {
                final JsonArray connectionArray = JSONUtils.func_151214_t(serializedPerkData, "connection");
                for (int i = 0; i < connectionArray.size(); ++i) {
                    final JsonElement connection = connectionArray.get(i);
                    final String connectedPerkKey = JSONUtils.func_151206_a(connection, String.format("connection[%s]", i));
                    connector.addConnection(new ResourceLocation(connectedPerkKey));
                }
            }
            ++count;
        }
        AstralSorcery.log.info("Built PerkTree with {} perks!", (Object)count);
        return newTree;
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        INSTANCE = new PerkTreeLoader();
    }
}
