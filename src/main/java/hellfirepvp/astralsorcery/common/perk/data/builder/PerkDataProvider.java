package hellfirepvp.astralsorcery.common.perk.data.builder;

import java.util.Iterator;
import com.google.gson.JsonArray;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Objects;
import java.io.IOException;
import java.awt.geom.Point2D;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.nio.file.Path;
import java.util.Comparator;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import net.minecraft.data.DirectoryCache;
import java.util.function.Consumer;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import net.minecraft.data.IDataProvider;

public abstract class PerkDataProvider implements IDataProvider
{
    private static final Gson GSON;
    protected final DataGenerator generator;
    
    public PerkDataProvider(final DataGenerator generator) {
        this.generator = generator;
    }
    
    public abstract void registerPerks(final Consumer<FinishedPerk> p0);
    
    public void func_200398_a(final DirectoryCache cache) throws IOException {
        final Path path = this.generator.func_200391_b();
        final List<FinishedPerk> builtPerks = new ArrayList<FinishedPerk>();
        this.registerPerks(finishedPerk -> {
            final ResourceLocation perkName = finishedPerk.perk.getRegistryName();
            final Point2D.Float offset = finishedPerk.perk.getOffset();
            if (builtPerks.stream().anyMatch(knownPerk -> knownPerk.perk.getOffset().equals(offset))) {
                throw new IllegalArgumentException("Duplicate perk registration at " + offset + " for " + perkName);
            }
            else if (builtPerks.contains(finishedPerk)) {
                throw new IllegalArgumentException("Duplicate perk registry name: " + perkName);
            }
            else {
                builtPerks.add(finishedPerk);
                this.savePerkFile(cache, (JsonElement)finishedPerk.serialize(), path.resolve(String.format("data/%s/perks/%s.json", perkName.func_110624_b(), perkName.addTransientModifier())));

            }
        });
        final JsonObject allPerks = new JsonObject();
        builtPerks.sort(Comparator.naturalOrder());
        builtPerks.forEach(perk -> allPerks.add(perk.perk.getRegistryName().toString(), (JsonElement)perk.serialize()));
        this.savePerkFile(cache, (JsonElement)allPerks, path.resolve("data/astralsorcery/perks/_full_tree.json"));
    }
    
    private void savePerkFile(final DirectoryCache cache, final JsonElement perk, final Path filePath) {
        try {
            final String perkJson = PerkDataProvider.GSON.toJson(perk);
            final String perkHash = PerkDataProvider.field_208307_a.hashUnencodedChars((CharSequence)perkJson).toString();
            if (!Objects.equals(cache.func_208323_a(filePath), perkHash) || !Files.exists(filePath, new LinkOption[0])) {
                Files.createDirectories(filePath.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
                try (final BufferedWriter bufferedwriter = Files.newBufferedWriter(filePath, new OpenOption[0])) {
                    bufferedwriter.write(perkJson);
                }
            }
            cache.func_208316_a(filePath, perkHash);
        }
        catch (final IOException exc) {
            AstralSorcery.log.error("Couldn't save perk {}", (Object)filePath, (Object)exc);
        }
    }
    
    public String func_200397_b() {
        return "Perks";
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
    
    public static class FinishedPerk implements Comparable<FinishedPerk>
    {
        private final AbstractPerk perk;
        private final List<ResourceLocation> connections;
        
        public FinishedPerk(final AbstractPerk perk, final List<ResourceLocation> connections) {
            this.perk = perk;
            this.connections = connections;
        }
        
        private JsonObject serialize() {
            final JsonObject object = this.perk.serializePerk();
            final JsonArray array = new JsonArray();
            for (final ResourceLocation connection : this.connections) {
                array.add(connection.toString());
            }
            object.add("connection", (JsonElement)array);
            return object;
        }
        
        @Override
        public int compareTo(final FinishedPerk that) {
            return this.perk.getRegistryName().compareTo(that.perk.getRegistryName());
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final FinishedPerk that = (FinishedPerk)o;
            return Objects.equals(this.perk.getRegistryName(), that.perk.getRegistryName());
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.perk.getRegistryName());
        }
    }
}
