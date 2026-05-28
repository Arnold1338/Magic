package hellfirepvp.astralsorcery.common.perk.type;

import java.util.Objects;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashSet;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeReader;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.UUID;
import java.util.Set;
import net.minecraftforge.fml.LogicalSide;
import java.util.Map;
import java.util.Random;
import hellfirepvp.astralsorcery.common.util.ReadWriteLockable;


public class PerkAttributeType implements ReadWriteLockable {
    protected static final Random rand;
    private final Map<LogicalSide, Set<UUID>> applicationCache;
    private final ReadWriteLock accessLock;
    private final boolean isOnlyMultiplicative;
    
    protected PerkAttributeType(final ResourceLocation key) {
        this(key, false);
    }
    
    protected PerkAttributeType(final ResourceLocation key, final boolean isMultiplicative) {
        this.applicationCache = Maps.newHashMap();
        this.accessLock = new ReentrantReadWriteLock(true);
        this.setRegistryName(key);
        this.isOnlyMultiplicative = isMultiplicative;
        this.init();
        this.attachListeners(MinecraftForge.EVENT_BUS);
    }
    
    public static PerkAttributeType makeDefault(final ResourceLocation name, final boolean isMultiplicative) {
        return new PerkAttributeType(name, isMultiplicative);
    }
    
    public boolean isMultiplicative() {
        return this.isOnlyMultiplicative;
    }
    
    public Component getTranslatedName() {
        return (Component)new Component(this.getUnlocalizedName());
    }
    
    public String getUnlocalizedName() {
        return String.format("perk.attribute.%s.%s.name", this.getRegistryName().func_110624_b(), this.getRegistryName().addTransientModifier());
    }
    
    protected void init() {
    }
    
    protected void attachListeners(final IEventBus eventBus) {
    }
    
    protected LogicalSide getSide(final Entity entity) {
        return entity.level() ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }
    
    @Nullable
    public PerkAttributeReader getReader() {
        return (PerkAttributeReader)RegistriesAS.REGISTRY_PERK_ATTRIBUTE_READERS.getValue(this.getRegistryName());
    }
    
    @Nonnull
    public PerkAttributeModifier createModifier(final float modifier, final ModifierType mode) {
        if (this.isMultiplicative() && mode == ModifierType.ADDITION) {
            throw new IllegalArgumentException("Tried creating addition-modifier for a multiplicative-only modifier!");
        }
        return new PerkAttributeModifier(this, mode, modifier);
    }
    
    public void onApply(final Player player, final LogicalSide side, final ModifierSource source) {
        this.write(() -> this.applicationCache.computeIfAbsent(side, s -> new HashSet()).add(player.getUUID()));
    }
    
    public void onRemove(final Player player, final LogicalSide side, final boolean removedCompletely, final ModifierSource source) {
        if (removedCompletely) {
            this.write(() -> this.applicationCache.getOrDefault(side, Collections.emptySet()).remove(player.getUUID()));
        }
    }
    
    public void onModeApply(final Player player, final ModifierType mode, final LogicalSide side) {
    }
    
    public void onModeRemove(final Player player, final ModifierType mode, final LogicalSide side, final boolean removedCompletely) {
    }
    
    public boolean hasTypeApplied(final Player player, final LogicalSide side) {
        return this.read(() -> this.applicationCache.getOrDefault(side, Collections.emptySet()).contains(player.getUUID()));
    }
    
    private void clear(final LogicalSide side) {
        this.write(() -> this.applicationCache.remove(side));
    }
    
    public static void clearCache(final LogicalSide side) {
        for (final PerkAttributeType type : RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES) {
            type.clear(side);
        }
    }
    
    public ReadWriteLock getLock() {
        return this.accessLock;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PerkAttributeType that = (PerkAttributeType)o;
        return Objects.equals(this.getRegistryName(), that.getRegistryName());
    }
    
    public int hashCode() {
        return this.getRegistryName().hashCode();
    }
    
    static {
        rand = new Random();
    }
}
