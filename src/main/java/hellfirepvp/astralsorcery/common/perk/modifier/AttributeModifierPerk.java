package hellfirepvp.astralsorcery.common.perk.modifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.util.JSONUtils;
import com.google.gson.JsonObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.IFormattableTextComponent;
import java.util.ArrayList;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import hellfirepvp.astralsorcery.common.perk.source.AttributeModifierProvider;

public class AttributeModifierPerk extends AttributeConverterPerk implements AttributeModifierProvider
{
    private final Set<PerkAttributeModifier> modifiers;
    
    public AttributeModifierPerk(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.modifiers = Sets.newHashSet();
    }
    
    @Nonnull
    public <V extends AttributeModifierPerk> V addModifier(final float modifier, final ModifierType mode, final PerkAttributeType type) {
        return this.addModifier(type.createModifier(modifier, mode));
    }
    
    @Nonnull
    public <T extends PerkAttributeModifier, V extends AttributeModifierPerk> V addModifier(final T modifier) {
        this.modifiers.add(modifier);
        return (V)this;
    }
    
    @Override
    public Collection<PerkAttributeModifier> getModifiers(final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        if (!ignoreRequirements && ResearchHelper.getProgress(player, side).getPerkData().isPerkSealed(this)) {
            return (Collection<PerkAttributeModifier>)Collections.emptyList();
        }
        return new ArrayList<PerkAttributeModifier>(this.modifiers);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean addLocalizedTooltip(final Collection<IFormattableTextComponent> tooltip) {
        final Collection<PerkAttributeModifier> modifiers = this.getModifiers((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT, true);
        boolean addEmptyLine = !modifiers.isEmpty();
        if (this.canSeeClient()) {
            for (final PerkAttributeModifier modifier : modifiers) {
                final String modifierDisplay = modifier.getLocalizedDisplayString();
                if (modifierDisplay != null) {
                    tooltip.add((IFormattableTextComponent)new Component(modifierDisplay));
                }
                else {
                    addEmptyLine = false;
                }
            }
        }
        return addEmptyLine;
    }
    
    @Override
    public void deserializeData(final JsonObject perkData) {
        super.deserializeData(perkData);
        this.modifiers.clear();
        if (JSONUtils.func_151204_g(perkData, "modifiers")) {
            final JsonArray array = JSONUtils.func_151214_t(perkData, "modifiers");
            for (int i = 0; i < array.size(); ++i) {
                final JsonObject serializedModifier = JSONUtils.func_151210_l(array.get(i), "modifiers[%s]");
                if (serializedModifier.has("custom")) {
                    final String customKey = JSONUtils.func_151200_h(serializedModifier, "custom");
                    final PerkAttributeModifier customModifier = (PerkAttributeModifier)RegistriesAS.REGISTRY_PERK_CUSTOM_MODIFIERS.getValue(new ResourceLocation(customKey));
                    if (customModifier == null) {
                        throw new IllegalArgumentException("Unknown specified modifier: " + customKey);
                    }
                    this.addModifier(customModifier);
                }
                else {
                    final String typeKey = JSONUtils.func_151200_h(serializedModifier, "type");
                    final PerkAttributeType type = (PerkAttributeType)RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValue(new ResourceLocation(typeKey));
                    if (type == null) {
                        throw new IllegalArgumentException("Unknown modifier type: " + typeKey);
                    }
                    final String modeKey = JSONUtils.func_151200_h(serializedModifier, "mode");
                    ModifierType mode;
                    try {
                        mode = ModifierType.valueOf(modeKey);
                    }
                    catch (final Exception exc) {
                        throw new IllegalArgumentException("Unknown mode: " + modeKey);
                    }
                    final float value = JSONUtils.func_151217_k(serializedModifier, "value");
                    this.addModifier(value, mode, type);
                }
            }
        }
    }
    
    @Override
    public void serializeData(final JsonObject perkData) {
        super.serializeData(perkData);
        if (!this.modifiers.isEmpty()) {
            final JsonArray array = new JsonArray();
            for (final PerkAttributeModifier modifier : this.modifiers) {
                if (modifier instanceof DynamicAttributeModifier) {
                    continue;
                }
                final JsonObject serializedModifier = new JsonObject();
                if (modifier.getRegistryName() != null) {
                    serializedModifier.addProperty("custom", modifier.getRegistryName().toString());
                }
                else {
                    serializedModifier.addProperty("type", modifier.getAttributeType().getRegistryName().toString());
                    serializedModifier.addProperty("mode", modifier.getMode().name());
                    serializedModifier.addProperty("value", (Number)modifier.getRawValue());
                }
                array.add((JsonElement)serializedModifier);
            }
            perkData.add("modifiers", (JsonElement)array);
        }
    }
}
