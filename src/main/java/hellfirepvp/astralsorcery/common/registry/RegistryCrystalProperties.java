package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;
import hellfirepvp.astralsorcery.common.crystal.source.Crystal;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.IndependentCrystalSource;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crystal.source.Ritual;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverRitualPedestal;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyCollectionRate;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyRitualRange;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyRitualEffect;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyToolEfficiency;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyToolDurability;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyShape;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyPurity;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.crystal.property.PropertySize;

public class RegistryCrystalProperties
{
    private RegistryCrystalProperties() {
    }
    
    public static void init() {
        CrystalPropertiesAS.Properties.PROPERTY_SIZE = registerProperty(new PropertySize());
        CrystalPropertiesAS.Properties.PROPERTY_PURITY = registerProperty(new PropertyPurity());
        CrystalPropertiesAS.Properties.PROPERTY_SHAPE = registerProperty(new PropertyShape());
        CrystalPropertiesAS.Properties.PROPERTY_TOOL_DURABILITY = registerProperty(new PropertyToolDurability());
        CrystalPropertiesAS.Properties.PROPERTY_TOOL_EFFICIENCY = registerProperty(new PropertyToolEfficiency());
        CrystalPropertiesAS.Properties.PROPERTY_RITUAL_EFFECT = registerProperty(new PropertyRitualEffect());
        CrystalPropertiesAS.Properties.PROPERTY_RITUAL_RANGE = registerProperty(new PropertyRitualRange());
        CrystalPropertiesAS.Properties.PROPERTY_COLLECTOR_COLLECTION_RATE = registerProperty(new PropertyCollectionRate());
        CrystalPropertiesAS.Properties.PROPERTY_CST_AEVITAS = registerProperty(new PropertyConstellation(ConstellationsAS.aevitas));
        CrystalPropertiesAS.Properties.PROPERTY_CST_DISCIDIA = registerProperty(new PropertyConstellation(ConstellationsAS.discidia));
        CrystalPropertiesAS.Properties.PROPERTY_CST_ARMARA = registerProperty(new PropertyConstellation(ConstellationsAS.armara));
        CrystalPropertiesAS.Properties.PROPERTY_CST_VICIO = registerProperty(new PropertyConstellation(ConstellationsAS.vicio));
        CrystalPropertiesAS.Properties.PROPERTY_CST_EVORSIO = registerProperty(new PropertyConstellation(ConstellationsAS.evorsio));
        CrystalPropertiesAS.Properties.PROPERTY_CST_LUCERNA = registerProperty(new PropertyConstellation(ConstellationsAS.lucerna));
        CrystalPropertiesAS.Properties.PROPERTY_CST_MINERALIS = registerProperty(new PropertyConstellation(ConstellationsAS.mineralis));
        CrystalPropertiesAS.Properties.PROPERTY_CST_OCTANS = registerProperty(new PropertyConstellation(ConstellationsAS.octans));
        CrystalPropertiesAS.Properties.PROPERTY_CST_BOOTES = registerProperty(new PropertyConstellation(ConstellationsAS.bootes));
        CrystalPropertiesAS.Properties.PROPERTY_CST_HOROLOGIUM = registerProperty(new PropertyConstellation(ConstellationsAS.horologium));
        CrystalPropertiesAS.Properties.PROPERTY_CST_FORNAX = registerProperty(new PropertyConstellation(ConstellationsAS.fornax));
        CrystalPropertiesAS.Properties.PROPERTY_CST_PELOTRIO = registerProperty(new PropertyConstellation(ConstellationsAS.pelotrio));
    }
    
    public static void initDefaultAttributes() {
        CrystalPropertiesAS.Sources.SOURCE_RITUAL_PEDESTAL = new PropertySource<StarlightReceiverRitualPedestal, Ritual>(AstralSorcery.key("ritual_network")) {
            @Override
            public Ritual createInstance(final StarlightReceiverRitualPedestal obj) {
                return new Ritual(this, obj.getChannelingType(), obj.getChannelingTrait());
            }
        };
        CrystalPropertiesAS.Sources.SOURCE_TILE_RITUAL_PEDESTAL = new PropertySource<TileRitualPedestal, Ritual>(AstralSorcery.key("ritual_tile")) {
            @Override
            public Ritual createInstance(final TileRitualPedestal obj) {
                return new Ritual(this, obj.getRitualConstellation(), obj.getRitualTrait());
            }
        };
        CrystalPropertiesAS.Sources.SOURCE_COLLECTOR_CRYSTAL = new PropertySource<IndependentCrystalSource, Crystal>(AstralSorcery.key("crystal_network")) {
            @Override
            public Crystal createInstance(final IndependentCrystalSource obj) {
                return new Crystal(this, obj.getStarlightType());
            }
        };
        CrystalPropertiesAS.Sources.SOURCE_TILE_COLLECTOR_CRYSTAL = new PropertySource<TileCollectorCrystal, Crystal>(AstralSorcery.key("crystal_tile")) {
            @Override
            public Crystal createInstance(final TileCollectorCrystal obj) {
                return new Crystal(this, obj.getAttunedConstellation());
            }
        };
        CrystalPropertiesAS.CREATIVE_CRYSTAL_TOOL_ATTRIBUTES = CrystalAttributes.Builder.newBuilder(false).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SIZE, 3).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SHAPE, 3).addProperty(CrystalPropertiesAS.Properties.PROPERTY_TOOL_DURABILITY, 3).addProperty(CrystalPropertiesAS.Properties.PROPERTY_TOOL_EFFICIENCY, 3).build();
        CrystalPropertiesAS.WORLDGEN_SHRINE_COLLECTOR_ATTRIBUTES = CrystalAttributes.Builder.newBuilder(false).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SIZE, 2).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SHAPE, 2).addProperty(CrystalPropertiesAS.Properties.PROPERTY_PURITY, 2).addProperty(CrystalPropertiesAS.Properties.PROPERTY_COLLECTOR_COLLECTION_RATE, 2).build();
        CrystalPropertiesAS.CREATIVE_ROCK_COLLECTOR_ATTRIBUTES = CrystalAttributes.Builder.newBuilder(false).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SIZE, 3).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SHAPE, 3).addProperty(CrystalPropertiesAS.Properties.PROPERTY_PURITY, 2).addProperty(CrystalPropertiesAS.Properties.PROPERTY_COLLECTOR_COLLECTION_RATE, 3).build();
        CrystalPropertiesAS.CREATIVE_CELESTIAL_COLLECTOR_ATTRIBUTES = CrystalAttributes.Builder.newBuilder(false).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SIZE, 3).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SHAPE, 3).addProperty(CrystalPropertiesAS.Properties.PROPERTY_PURITY, 2).addProperty(CrystalPropertiesAS.Properties.PROPERTY_COLLECTOR_COLLECTION_RATE, 3).build();
        CrystalPropertiesAS.LENS_PRISM_CREATIVE_ATTRIBUTES = CrystalAttributes.Builder.newBuilder(false).addProperty(CrystalPropertiesAS.Properties.PROPERTY_PURITY, 2).addProperty(CrystalPropertiesAS.Properties.PROPERTY_SHAPE, 3).build();
    }
    
    private static <T extends CrystalProperty> T registerProperty(final T property) {
        AstralSorcery.getProxy().getRegistryPrimer().register(property);
        return property;
    }
}
