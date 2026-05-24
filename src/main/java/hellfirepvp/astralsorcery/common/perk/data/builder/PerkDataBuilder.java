package hellfirepvp.astralsorcery.common.perk.data.builder;

import java.util.Collection;
import com.google.common.collect.ImmutableList;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeConverterPerk;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierPerk;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.data.PerkTypeHandler;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;

public class PerkDataBuilder<T extends AbstractPerk>
{
    private final T perk;
    private final List<ResourceLocation> connections;
    
    public PerkDataBuilder(final T perk) {
        this.connections = new ArrayList<ResourceLocation>();
        this.perk = perk;
    }
    
    public static PerkBuilder<AbstractPerk> builder() {
        return new PerkBuilder<AbstractPerk>((PerkTypeHandler.Type)PerkTypeHandler.DEFAULT);
    }
    
    public static <T extends AbstractPerk> PerkBuilder<T> ofType(final PerkTypeHandler.Type<T> perkType) {
        return new PerkBuilder<T>((PerkTypeHandler.Type)perkType);
    }
    
    public PerkDataBuilder<T> setName(final String perkDisplayName) {
        this.perk.setName(perkDisplayName);
        return this;
    }
    
    public PerkDataBuilder<T> addModifier(final float modifier, final ModifierType mode, final PerkAttributeType type) {
        if (!(this.perk instanceof AttributeModifierPerk)) {
            throw new IllegalArgumentException("Cannot add modifiers to non-modifier perks!");
        }
        ((AttributeModifierPerk)this.perk).addModifier(modifier, mode, type);
        return this;
    }
    
    public PerkDataBuilder<T> addModifier(final PerkAttributeModifier modifier) {
        if (!(this.perk instanceof AttributeModifierPerk)) {
            throw new IllegalArgumentException("Cannot add modifiers to non-modifier perks!");
        }
        ((AttributeModifierPerk)this.perk).addModifier(modifier);
        return this;
    }
    
    public PerkDataBuilder<T> addConverter(final PerkConverter converter) {
        if (!(this.perk instanceof AttributeConverterPerk)) {
            throw new IllegalArgumentException("Cannot add converter to non-converter perks!");
        }
        ((AttributeConverterPerk)this.perk).addConverter(converter);
        return this;
    }
    
    public PerkDataBuilder<T> modify(final Consumer<T> recipeFn) {
        recipeFn.accept(this.perk);
        return this;
    }
    
    public PerkDataBuilder<?> chain(final PerkDataBuilder<?> other) {
        this.connect(((AbstractPerk)other.perk).getRegistryName());
        return other;
    }
    
    public PerkDataBuilder<T> connect(final PerkDataBuilder<?> other) {
        return this.connect(((AbstractPerk)other.perk).getRegistryName());
    }
    
    public PerkDataBuilder<T> connect(final ResourceLocation key) {
        this.connections.add(key);
        return this;
    }
    
    public PerkDataBuilder<T> build(final Consumer<PerkDataProvider.FinishedPerk> consumerIn) {
        consumerIn.accept(new PerkDataProvider.FinishedPerk(this.perk, (List<ResourceLocation>)ImmutableList.copyOf((Collection)this.connections)));
        return this;
    }
    
    public static class PerkBuilder<T extends AbstractPerk>
    {
        private final PerkTypeHandler.Type<T> perkType;
        
        private PerkBuilder(final PerkTypeHandler.Type<T> perkType) {
            this.perkType = perkType;
        }
        
        public PerkDataBuilder<T> create(final ResourceLocation perkKey, final float x, final float y) {
            final T perk = this.perkType.convert(perkKey, x, y);
            if (!this.perkType.getKey().equals((Object)PerkTypeHandler.DEFAULT.getKey())) {
                perk.setCustomPerkType(this.perkType.getKey());
            }
            return new PerkDataBuilder<T>(perk);
        }
    }
}
