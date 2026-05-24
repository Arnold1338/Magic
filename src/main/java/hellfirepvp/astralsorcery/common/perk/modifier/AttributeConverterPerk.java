package hellfirepvp.astralsorcery.common.perk.modifier;

import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import java.awt.geom.Point2D;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import java.util.Set;
import hellfirepvp.astralsorcery.common.perk.source.AttributeConverterProvider;
import hellfirepvp.astralsorcery.common.perk.ProgressGatedPerk;

public class AttributeConverterPerk extends ProgressGatedPerk implements AttributeConverterProvider
{
    private final Set<PerkConverter> converters;
    
    public AttributeConverterPerk(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.converters = Sets.newHashSet();
    }
    
    public <T> T addConverter(final PerkConverter converter) {
        this.converters.add(converter);
        return (T)this;
    }
    
    public <T> T addRangedConverter(final float radius, final PerkConverter converter) {
        return this.addConverter(converter.asRangedConverter(new Point2D.Float(this.getOffset().x, this.getOffset().y), radius));
    }
    
    @Override
    public Collection<PerkConverter> getConverters(final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        if (!ignoreRequirements && ResearchHelper.getProgress(player, side).getPerkData().isPerkSealed(this)) {
            return (Collection<PerkConverter>)Collections.emptyList();
        }
        return (Collection<PerkConverter>)Collections.unmodifiableSet((Set<?>)this.converters);
    }
    
    public void applyPerkLogic(final Player player, final LogicalSide side) {
    }
    
    public void removePerkLogic(final Player player, final LogicalSide side) {
    }
    
    @Override
    public void deserializeData(final JsonObject perkData) {
        super.deserializeData(perkData);
        this.converters.clear();
        if (JSONUtils.func_151204_g(perkData, "converters")) {
            final JsonArray array = JSONUtils.func_151214_t(perkData, "converters");
            for (int i = 0; i < array.size(); ++i) {
                final JsonObject serializedConverter = JSONUtils.func_151210_l(array.get(i), "converters[%s]");
                final String key = JSONUtils.func_151200_h(serializedConverter, "name");
                final PerkConverter converter = (PerkConverter)RegistriesAS.REGISTRY_PERK_ATTRIBUTE_CONVERTERS.getValue(new ResourceLocation(key));
                if (converter == null) {
                    throw new JsonParseException("Unknown converter: " + key);
                }
                if (serializedConverter.has("radius")) {
                    final float radius = JSONUtils.func_151217_k(serializedConverter, "radius");
                    this.addRangedConverter(radius, converter);
                }
                else {
                    this.addConverter(converter);
                }
            }
        }
    }
    
    @Override
    public void serializeData(final JsonObject perkData) {
        super.serializeData(perkData);
        if (!this.converters.isEmpty()) {
            final JsonArray converters = new JsonArray();
            for (final PerkConverter converter : this.converters) {
                final JsonObject serializedConverter = new JsonObject();
                serializedConverter.addProperty("name", converter.getRegistryName().toString());
                if (converter instanceof PerkConverter.Radius) {
                    serializedConverter.addProperty("radius", (Number)((PerkConverter.Radius)converter).getRadius());
                }
                converters.add((JsonElement)serializedConverter);
            }
            perkData.add("converters", (JsonElement)converters);
        }
    }
}
