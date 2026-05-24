package hellfirepvp.astralsorcery.common.crystal;

import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.util.Mth;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.ItemStack;
import java.util.Random;
import java.util.List;

public class CrystalGenerator
{
    private static final int COUNT_PHYSICAL_PROPERTY_TIERS = 5;
    private static final float CHANCE_PHYSICAL_PROPERTIES = 0.65f;
    private static final List<CrystalProperty> PHYSICAL_PROPERTIES;
    private static final int COUNT_USAGE_PROPERTY_TIERS = 4;
    private static final float CHANCE_USAGE_PROPERTIES = 0.55f;
    private static final List<CrystalProperty> USAGE_PROPERTIES;
    private static final Random RAND;
    
    @Nonnull
    public static CrystalAttributes upgradeProperties(final ItemStack stack) {
        return upgradeProperties(stack, CrystalGenerator.RAND);
    }
    
    @Nonnull
    public static CrystalAttributes upgradeProperties(final ItemStack stack, final Random random) {
        if (!(stack.getItem() instanceof CrystalAttributeItem)) {
            return generateNewAttributes(stack, random);
        }
        final CrystalAttributes attr = ((CrystalAttributeItem)stack.getItem()).getAttributes(stack);
        if (attr == null) {
            return generateNewAttributes(stack, random);
        }
        if (!(stack.getItem() instanceof CrystalAttributeGenItem)) {
            return attr;
        }
        final int existing = attr.getTotalTierLevel();
        final int expected = Mth.func_76125_a(existing + 1, ((CrystalAttributeGenItem)stack.getItem()).getGeneratedPropertyTiers(), ((CrystalAttributeGenItem)stack.getItem()).getMaxPropertyTiers());
        final int generate = expected - attr.getTotalTierLevel();
        final CrystalAttributes.Builder builder = CrystalAttributes.Builder.newBuilder(false);
        builder.addAll(attr);
        for (int i = 0; i < generate; ++i) {
            final Collection<CrystalProperty> remaining = new ArrayList<CrystalProperty>(RegistriesAS.REGISTRY_CRYSTAL_PROPERTIES.getValues());
            while (!addRandomProperty(builder, remaining, random)) {}
        }
        return builder.build();
    }
    
    @Nullable
    public static CrystalProperty getRandomProperty() {
        return getRandomProperty(CrystalGenerator.RAND);
    }
    
    @Nullable
    public static CrystalProperty getRandomProperty(final Random random) {
        if (random.nextFloat() <= 0.65f) {
            return MiscUtils.getRandomEntry(CrystalGenerator.PHYSICAL_PROPERTIES, random);
        }
        if (random.nextFloat() <= 0.55f) {
            return MiscUtils.getRandomEntry(CrystalGenerator.USAGE_PROPERTIES, random);
        }
        final Collection<CrystalProperty> remaining = new ArrayList<CrystalProperty>(RegistriesAS.REGISTRY_CRYSTAL_PROPERTIES.getValues());
        remaining.removeAll(CrystalGenerator.USAGE_PROPERTIES);
        remaining.removeAll(CrystalGenerator.PHYSICAL_PROPERTIES);
        return MiscUtils.getRandomEntry(remaining, random);
    }
    
    @Nonnull
    public static CrystalAttributes generateNewAttributes(final ItemStack item) {
        return generateNewAttributes(item, CrystalGenerator.RAND);
    }
    
    @Nonnull
    public static CrystalAttributes generateNewAttributes(final ItemStack item, final Random random) {
        int toGenerate = 4;
        if (item.getItem() instanceof CrystalAttributeGenItem) {
            toGenerate = ((CrystalAttributeGenItem)item.getItem()).getGeneratedPropertyTiers();
        }
        final CrystalAttributes.Builder attrBuilder = CrystalAttributes.Builder.newBuilder(false);
        int totalAdded = 0;
        for (int x = 0; x < 5 && totalAdded < toGenerate; ++x) {
            if (random.nextFloat() <= 0.65f) {
                while (!addRandomProperty(attrBuilder, CrystalGenerator.PHYSICAL_PROPERTIES, random)) {}
                ++totalAdded;
            }
        }
        for (int x = 0; x < 4 && totalAdded < toGenerate; ++x) {
            if (random.nextFloat() <= 0.55f) {
                while (!addRandomProperty(attrBuilder, CrystalGenerator.USAGE_PROPERTIES, random)) {}
                ++totalAdded;
            }
        }
        final Collection<CrystalProperty> remaining = new ArrayList<CrystalProperty>(RegistriesAS.REGISTRY_CRYSTAL_PROPERTIES.getValues());
        remaining.removeAll(CrystalGenerator.USAGE_PROPERTIES);
        remaining.removeAll(CrystalGenerator.PHYSICAL_PROPERTIES);
        while (totalAdded < toGenerate) {
            while (!addRandomProperty(attrBuilder, remaining, random)) {}
            ++totalAdded;
        }
        return attrBuilder.build();
    }
    
    private static boolean addRandomProperty(final CrystalAttributes.Builder builder, final Collection<CrystalProperty> properties, final Random random) {
        final List<CrystalProperty> existing = builder.getProperties();
        existing.removeIf(o -> !properties.contains(o));
        existing.removeIf(property -> builder.getPropertyLvl(property, 0) >= property.getMaxTier());
        final CrystalProperty propExisting = MiscUtils.getRandomEntry(existing, random);
        final CrystalProperty prop = (random.nextFloat() <= 0.85f && propExisting != null) ? propExisting : MiscUtils.getRandomEntry(properties, random);
        final int existingLvl = builder.getPropertyLvl(prop, 0);
        if (existingLvl < prop.getMaxTier()) {
            builder.addProperty(prop, 1);
            return true;
        }
        return false;
    }
    
    static {
        PHYSICAL_PROPERTIES = Lists.newArrayList((Object[])new CrystalProperty[] { CrystalPropertiesAS.Properties.PROPERTY_SIZE, CrystalPropertiesAS.Properties.PROPERTY_SHAPE, CrystalPropertiesAS.Properties.PROPERTY_PURITY });
        USAGE_PROPERTIES = Lists.newArrayList((Object[])new CrystalProperty[] { CrystalPropertiesAS.Properties.PROPERTY_TOOL_DURABILITY, CrystalPropertiesAS.Properties.PROPERTY_TOOL_EFFICIENCY, CrystalPropertiesAS.Properties.PROPERTY_RITUAL_RANGE, CrystalPropertiesAS.Properties.PROPERTY_RITUAL_EFFECT, CrystalPropertiesAS.Properties.PROPERTY_COLLECTOR_COLLECTION_RATE });
        RAND = new Random();
    }
}
