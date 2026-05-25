package hellfirepvp.astralsorcery.common.crystal;

import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import com.google.common.collect.Maps;
import java.util.Objects;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Map;
import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.Random;

public final class CrystalAttributes
{
    private static final Random rand;
    private LinkedList<Attribute> crystalAttributes;
    
    private CrystalAttributes() {
        this.crystalAttributes = Lists.newLinkedList();
    }
    
    private CrystalAttributes(final Map<CrystalProperty, Integer> crystalAttributes) {
        this.crystalAttributes = Lists.newLinkedList();
        this.crystalAttributes = Lists.newLinkedList();
        for (final Map.Entry<CrystalProperty, Integer> propertyEntry : crystalAttributes.entrySet()) {
            this.crystalAttributes.add(new Attribute((CrystalProperty)propertyEntry.getKey(), (int)propertyEntry.getValue(), false));
        }
        this.crystalAttributes.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)Attribute::getProperty));
    }
    
    public List<Attribute> getCrystalAttributes() {
        return Collections.unmodifiableList((List<? extends Attribute>)this.crystalAttributes);
    }
    
    public List<CrystalProperty> getPropertiesPerTier(final boolean combineDuplicates) {
        final List<CrystalProperty> properties = new ArrayList<CrystalProperty>(combineDuplicates ? this.getCrystalAttributes().size() : this.getTotalTierLevel());
        for (final Attribute attribute : this.getCrystalAttributes()) {
            if (combineDuplicates) {
                properties.add(attribute.getProperty());
            }
            else {
                for (int i = 0; i < attribute.getTier(); ++i) {
                    properties.add(attribute.getProperty());
                }
            }
        }
        return properties;
    }
    
    @Nullable
    public Attribute getAttribute(final CrystalProperty property) {
        for (final Attribute attr : this.crystalAttributes) {
            if (attr.getProperty().equals(property)) {
                return attr;
            }
        }
        return null;
    }
    
    public int getTotalTierLevel() {
        int tier = 0;
        for (final Attribute attr : this.getCrystalAttributes()) {
            tier += attr.getTier();
        }
        return tier;
    }
    
    public CrystalAttributes discoverAll(final PlayerProgress prog) {
        if (MiscUtils.contains(this.getCrystalAttributes(), attr -> attr.canNewDiscover(prog))) {
            final CrystalAttributes thisCopy = this.copy();
            thisCopy.crystalAttributes.forEach(attribute -> attribute.discover(prog));
            return thisCopy;
        }
        return this;
    }
    
    public boolean hasUnknownAttributes() {
        return MiscUtils.contains(this.getCrystalAttributes(), attribute -> !attribute.isDiscovered());
    }
    
    public boolean isEmpty() {
        return this.getTotalTierLevel() <= 0;
    }
    
    public List<CrystalProperty> getProperties() {
        return this.getCrystalAttributes().stream().map((Function<? super Object, ?>)Attribute::getProperty).collect((Collector<? super Object, ?, List<CrystalProperty>>)Collectors.toList());
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public TooltipResult addTooltip(final List<Component> tooltip) {
        return this.addTooltip(tooltip, CalculationContext.Builder.newBuilder().build());
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public TooltipResult addTooltip(final List<Component> tooltip, final PlayerProgress progress) {
        return this.addTooltip(tooltip, progress, CalculationContext.Builder.newBuilder().build());
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public TooltipResult addTooltip(final List<Component> tooltip, final CalculationContext ctx) {
        return this.addTooltip(tooltip, ResearchHelper.getClientProgress(), ctx);
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    private TooltipResult addTooltip(final List<Component> tooltip, final PlayerProgress progress, final CalculationContext ctx) {
        boolean missing = false;
        boolean addedAtLeastOne = false;
        for (final Attribute attr : this.getCrystalAttributes()) {
            if (attr.getTier() > 0) {
                final CrystalProperty prop = attr.getProperty();
                if (!prop.hasUsageFor(ctx) && !ctx.isEmpty()) {
                    continue;
                }
                if (!prop.canSee(progress) || !attr.isDiscovered()) {
                    missing = true;
                }
                else {
                    final MutableComponent enchantmentLevel = new Component(String.format("enchantment.level.%s", attr.getTier())).withStyle(ChatFormatting.GOLD));
                    final MutableComponent propertyName = prop.getName(attr.getTier()).withStyle(ChatFormatting.GRAY));
                    tooltip.add((Component)propertyName.func_230529_a_((Component)Component.translatable(" ")).func_230529_a_((Component)enchantmentLevel));
                    addedAtLeastOne = true;
                }
            }
        }
        if (missing) {
            tooltip.add((Component)Component.translatable("astralsorcery.progress.missing.knowledge").withStyle(ChatFormatting.GRAY));

        }
        return (missing && !addedAtLeastOne) ? TooltipResult.ALL_MISSING : (missing ? TooltipResult.ADDED_ALL_WITH_MISSING : TooltipResult.ADDED_ALL);
    }
    
    public CrystalAttributes combine(final CrystalAttributes other, final boolean ignoreTierMax) {
        return this.combine(other, ignoreTierMax, 1.0f);
    }
    
    public CrystalAttributes combine(final CrystalAttributes other, final boolean ignoreTierMax, final float mergeChance) {
        final List<Attribute> otherAttributes = other.getCrystalAttributes();
        if (otherAttributes.isEmpty()) {
            return this.copy();
        }
        final Builder builder = Builder.newBuilder(ignoreTierMax);
        builder.addAll(this.copy());
        for (final Attribute otherAttr : otherAttributes) {
            for (int i = 0; i < otherAttr.getTier(); ++i) {
                if (CrystalAttributes.rand.nextFloat() <= mergeChance) {
                    builder.addProperty(otherAttr.getProperty(), 1);
                }
            }
        }
        return builder.build();
    }
    
    public CrystalAttributes modifyLevel(final CrystalProperty prop, final int change) {
        return this.modifyLevel(prop, change, false);
    }
    
    public CrystalAttributes modifyLevel(final CrystalProperty prop, final int change, final boolean ignoreTierMax) {
        final Attribute existing = this.getAttribute(prop);
        if (existing != null && change != 0) {
            final int newTier = Mth.getDescriptionId(existing.getTier() + change, 0, ignoreTierMax ? Integer.MAX_VALUE : prop.getMaxTier());
            if (newTier <= 0) {
                return this.transform(Function.identity(), Lists.newArrayList(), Lists.newArrayList((Object[])new CrystalProperty[] { prop }));
            }
            if (newTier != existing.getTier()) {
                return this.transform(attribute -> {
                    if (attribute.getProperty().equals(prop)) {
                        attribute.tier = newTier;
                    }
                    return attribute;
                });
            }
        }
        else if (change > 0) {
            return this.transform(Function.identity(), Lists.newArrayList((Object[])new Attribute[] { new Attribute(prop, Mth.getDescriptionId(change, 0, prop.getMaxTier())) }), Lists.newArrayList());
        }
        return this;
    }
    
    private CrystalAttributes transform(final Function<Attribute, Attribute> modify) {
        return this.transform(modify, Lists.newArrayList(), Lists.newArrayList());
    }
    
    private CrystalAttributes transform(final Function<Attribute, Attribute> modify, final Collection<Attribute> additions, final Collection<CrystalProperty> removals) {
        final List<Attribute> current = Lists.newArrayList();
        final Iterator<Attribute> iterator = this.getCrystalAttributes().iterator();
        Attribute attr = null;
        while (iterator.hasNext()) {
            attr = iterator.next();
            current.add(new Attribute(attr.getProperty(), attr.getTier()));
        }
        final List<Attribute> modified = current.stream().map((Function<? super Object, ?>)modify).collect((Collector<? super Object, ?, List<Attribute>>)Collectors.toList());
        for (final Attribute added : additions) {
            final Attribute existing;
            if ((existing = MiscUtils.iterativeSearch(modified, tpl -> tpl.getProperty().equals(added.getProperty()))) != null) {
                final Attribute attribute = existing;
                attribute.tier += added.getTier();
            }
            else {
                modified.add(new Attribute(added.getProperty(), added.getTier()));
            }
        }
        modified.removeIf(attr -> removals.contains(attr.getProperty()));
        return new CrystalAttributes((Map<CrystalProperty, Integer>)modified.stream().collect(Collectors.toMap((Function<? super Object, ?>)Attribute::getProperty, (Function<? super Object, ?>)Attribute::getTier)));
    }
    
    public CrystalAttributes copy() {
        return deserialize(this.serialize());
    }
    
    public CrystalAttributes clampMaxTier() {
        final CrystalAttributes attributes = this.copy();
        for (final Attribute attr : attributes.crystalAttributes) {
            attr.tier = Mth.getDescriptionId(attr.getTier(), 0, attr.getProperty().getMaxTier());
        }
        return attributes;
    }
    
    public void store(final ItemStack stack) {
        if (!stack.isEmpty()) {
            this.store(NBTHelper.getPersistentData(stack));
        }
    }
    
    public void store(final CompoundTag baseTag) {
        baseTag.put("crystalProperties", (Tag)this.serialize());
    }
    
    public static void storeNull(final ItemStack stack) {
        if (!stack.isEmpty()) {
            storeNull(NBTHelper.getPersistentData(stack));
        }
    }
    
    public static void storeNull(final CompoundTag baseTag) {
        baseTag.func_82580_o("crystalProperties");
    }
    
    @Nullable
    public static CrystalAttributes getCrystalAttributes(final ItemStack stack) {
        return stack.isEmpty() ? null : getCrystalAttributes(NBTHelper.getPersistentData(stack));
    }
    
    @Nullable
    public static CrystalAttributes getCrystalAttributes(final CompoundTag baseTag) {
        if (!baseTag.contains("crystalProperties")) {
            return null;
        }
        final CompoundTag tag = baseTag.func_74775_l("crystalProperties");
        if (tag.func_186856_d() == 0) {
            return null;
        }
        return deserialize(tag);
    }
    
    public CompoundTag serialize() {
        final CompoundTag tag = new CompoundTag();
        final ListTag list = new ListTag();
        for (final Attribute attr : this.crystalAttributes) {
            list.add((Object)attr.serialize());
        }
        tag.put("attributes", (Tag)list);
        return tag;
    }
    
    public static CrystalAttributes deserialize(final CompoundTag tag) {
        final CrystalAttributes attributes = new CrystalAttributes();
        final ListTag list = tag.getList("attributes", 10);
        for (int i = 0; i < list.size(); ++i) {
            final Attribute attr = deserialize(list.getCompound(i));
            if (attr != null) {
                attributes.crystalAttributes.add(attr);
            }
        }
        return attributes;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CrystalAttributes that = (CrystalAttributes)o;
        return Objects.equals(this.crystalAttributes, that.crystalAttributes);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.crystalAttributes);
    }
    
    static {
        rand = new Random();
    }
    
    public enum TooltipResult
    {
        ADDED_ALL, 
        ADDED_ALL_WITH_MISSING, 
        ALL_MISSING;
    }
    
    public static class Builder
    {
        private Map<CrystalProperty, Integer> properties;
        private final boolean ignoreTierCap;
        
        private Builder(final boolean ignoreTierCap) {
            this.properties = Maps.newHashMap();
            this.ignoreTierCap = ignoreTierCap;
        }
        
        public static Builder newBuilder(final boolean ignoreTierCap) {
            return new Builder(ignoreTierCap);
        }
        
        public Builder addAll(final CrystalAttributes other) {
            for (final Attribute attr : other.getCrystalAttributes()) {
                final CrystalProperty property = attr.getProperty();
                int cTier = this.properties.getOrDefault(property, 0);
                cTier = Mth.getDescriptionId(cTier + attr.getTier(), 0, this.ignoreTierCap ? Integer.MAX_VALUE : property.getMaxTier());
                this.properties.put(property, cTier);
            }
            return this;
        }
        
        public Builder addProperty(final CrystalProperty property, final int tier) {
            int cTier = this.properties.getOrDefault(property, 0);
            cTier = Mth.getDescriptionId(cTier + tier, 0, this.ignoreTierCap ? Integer.MAX_VALUE : property.getMaxTier());
            this.properties.remove(property);
            if (cTier > 0) {
                this.properties.put(property, cTier);
            }
            return this;
        }
        
        public int getPropertyLvl(final CrystalProperty property, final int defaultValue) {
            return this.properties.getOrDefault(property, defaultValue);
        }
        
        public List<CrystalProperty> getProperties() {
            return Lists.newArrayList((Iterable)this.properties.keySet());
        }
        
        public CrystalAttributes buildAverage(final int count) {
            final Map<CrystalProperty, Integer> average = new HashMap<CrystalProperty, Integer>();
            for (final CrystalProperty prop : this.properties.keySet()) {
                final int newLevel = Mth.func_76123_f(this.properties.getOrDefault(prop, 0) / (float)count);
                if (newLevel > 0) {
                    average.put(prop, newLevel);
                }
            }
            return new CrystalAttributes(average, null);
        }
        
        public CrystalAttributes build() {
            return new CrystalAttributes(this.properties, null);
        }
    }
    
    public static class Attribute
    {
        private boolean discovered;
        private int tier;
        private CrystalProperty property;
        
        private Attribute(final CrystalProperty property, final int tier) {
            this(property, tier, false);
        }
        
        private Attribute(final CrystalProperty property, final int tier, final boolean discovered) {
            this.tier = tier;
            this.property = property;
            this.discovered = true;
        }
        
        public CrystalProperty getProperty() {
            return this.property;
        }
        
        private boolean canNewDiscover(final PlayerProgress prog) {
            return !this.isDiscovered() && this.property.canSee(prog);
        }
        
        private void discover(final PlayerProgress prog) {
            if (!this.isDiscovered() && this.property.canSee(prog)) {
                this.discovered = true;
            }
        }
        
        public boolean isDiscovered() {
            return this.discovered;
        }
        
        public int getTier() {
            return this.tier;
        }
        
        private CompoundTag serialize() {
            final CompoundTag tag = new CompoundTag();
            tag.putString("property", this.property.getRegistryName().toString());
            tag.putInt("pLevel", this.tier);
            tag.putBoolean("discovered", this.discovered);
            return tag;
        }
        
        @Nullable
        private static Attribute deserialize(final CompoundTag tag) {
            final ResourceLocation key = new ResourceLocation(tag.getString("property"));
            final CrystalProperty prop = (CrystalProperty)RegistriesAS.REGISTRY_CRYSTAL_PROPERTIES.getValue(key);
            if (prop == null) {
                return null;
            }
            final int tier = tag.getInt("pLevel");
            final boolean discovered = tag.getBoolean("discovered");
            return new Attribute(prop, tier, discovered);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Attribute attribute = (Attribute)o;
            return this.discovered == attribute.discovered && this.tier == attribute.tier && Objects.equals(this.property, attribute.property);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.discovered, this.tier, this.property);
        }
    }
}
