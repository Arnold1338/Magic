package hellfirepvp.astralsorcery.common.perk.modifier;

import java.util.Objects;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import com.google.common.collect.HashBasedTable;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.AstralSorcery;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import java.util.Map;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import net.minecraft.resources.ResourceLocation;


public class PerkAttributeModifier {
    private static long counter;
    protected ResourceLocation comparisonKey;
    protected final ModifierType mode;
    protected final PerkAttributeType attributeType;
    protected float value;
    private boolean absolute;
    private final Map<PerkConverter, Table<PerkAttributeType, ModifierType, PerkAttributeModifier>> cachedConverters;
    
    public PerkAttributeModifier(final PerkAttributeType type, final ModifierType mode, final float value) {
        this.absolute = false;
        this.cachedConverters = Maps.newHashMap();
        Preconditions.checkNotNull((Object)type, (Object)"Perk attribute type must not be null!");
        Preconditions.checkNotNull((Object)mode, (Object)"Modifier type must not be null!");
        this.comparisonKey = AstralSorcery.key("generic_perk_modifier_" + PerkAttributeModifier.counter++);
        this.attributeType = type;
        this.mode = mode;
        this.value = value;
        this.initModifier();
    }
    
    public PerkAttributeModifier(final ResourceLocation persistentKey, final PerkAttributeType type, final ModifierType mode, final float value) {
        this.absolute = false;
        this.cachedConverters = Maps.newHashMap();
        this.comparisonKey = persistentKey;
        this.attributeType = type;
        this.mode = mode;
        this.value = value;

        this.initModifier();
    }
    
    public ResourceLocation getComparisonKey() {
        return this.comparisonKey;
    }
    
    protected void initModifier() {
    }
    
    protected void setAbsolute() {
        this.absolute = true;
    }
    
    @Nonnull
    public PerkAttributeModifier convertModifier(final PerkAttributeType type, final ModifierType mode, final float value) {
        if (this.absolute) {
            return this;
        }
        final PerkAttributeModifier mod = this.createModifier(type, mode, value);
        mod.comparisonKey = this.comparisonKey;
        return mod;
    }
    
    @Nonnull
    public PerkAttributeModifier gainAsExtraModifier(final PerkConverter converter, final PerkAttributeType type, final ModifierType mode, final float value) {
        PerkAttributeModifier modifier = this.getCachedAttributeModifier(converter, type, mode);
        if (modifier == null) {
            modifier = this.createModifier(type, mode, value);
            modifier.setAbsolute();
            this.addModifierToCache(converter, type, mode, modifier);
        }
        return modifier;
    }
    
    @Nullable
    protected PerkAttributeModifier getCachedAttributeModifier(final PerkConverter converter, final PerkAttributeType type, final ModifierType mode) {
        final Table<PerkAttributeType, ModifierType, PerkAttributeModifier> cachedModifiers = this.cachedConverters.computeIfAbsent(converter, c -> HashBasedTable.create());
        return (PerkAttributeModifier)cachedModifiers.get((Object)type, (Object)mode);
    }
    
    protected void addModifierToCache(final PerkConverter converter, final PerkAttributeType type, final ModifierType mode, final PerkAttributeModifier modifier) {
        final Table<PerkAttributeType, ModifierType, PerkAttributeModifier> cachedModifiers = this.cachedConverters.computeIfAbsent(converter, c -> HashBasedTable.create());
        cachedModifiers.put((Object)type, (Object)mode, (Object)modifier);
    }
    
    @Nonnull
    protected PerkAttributeModifier createModifier(final PerkAttributeType type, final ModifierType mode, final float value) {
        return type.createModifier(value, mode);
    }
    
    @Deprecated
    public final float getRawValue() {
        return this.value;
    }
    
    public float getValue(final Player player, final PlayerProgress progress) {
        return this.getRawValue();
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getValueForDisplay(final Player player, final PlayerProgress progress) {
        return this.getValue(player, progress);
    }
    
    public ModifierType getMode() {
        return this.mode;
    }
    
    public PerkAttributeType getAttributeType() {
        return this.attributeType;
    }
    
    protected String getUnlocalizedAttributeName() {
        return this.getAttributeType().getUnlocalizedName();
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean hasDisplayString() {
        return net.minecraft.client.resources.language.I18n.func_188566_a(this.getAttributeType().getUnlocalizedName());
    }
    
    @OnlyIn(Dist.CLIENT)
    public String getLocalizedAttributeValue() {
        return this.getMode().stringifyValue(this.getValueForDisplay((Player)Minecraft.getInstance().player, ResearchHelper.getClientProgress()));
    }
    
    @OnlyIn(Dist.CLIENT)
    public String getLocalizedModifierName() {
        return net.minecraft.client.resources.language.I18n.func_135052_a(this.getMode().getUnlocalizedModifierName(this.getValueForDisplay((Player)Minecraft.getInstance().player, ResearchHelper.getClientProgress())), new Object[0]);
    }
    
    @OnlyIn(Dist.CLIENT)
    public String getAttributeDisplayFormat() {
        return net.minecraft.client.resources.language.I18n.func_135052_a("perk.modifier.astralsorcery.format", new Object[0]);
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getLocalizedDisplayString() {
        if (!this.hasDisplayString()) {
            return null;
        }
        return String.format(this.getAttributeDisplayFormat(), this.getLocalizedAttributeValue(), this.getLocalizedModifierName(), net.minecraft.client.resources.language.I18n.func_135052_a(this.getUnlocalizedAttributeName(), new Object[0]));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PerkAttributeModifier that = (PerkAttributeModifier)o;
        return this.comparisonKey.equals((Object)that.comparisonKey);
    }
    
    public int hashCode() {
        return Objects.hashCode(this.comparisonKey);
    }
    
    static {
        PerkAttributeModifier.counter = 0L;
    }
}
