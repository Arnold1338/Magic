package hellfirepvp.astralsorcery.common.perk.source;

import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.EnumSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Collection;
import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import javax.annotation.Nullable;
import net.minecraftforge.eventbus.api.Event;
import hellfirepvp.astralsorcery.common.event.ASRegistryEvents;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.common.perk.source.provider.equipment.EquipmentSourceProvider;
import hellfirepvp.astralsorcery.common.perk.source.provider.PerkSourceProvider;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class ModifierManager implements ITickHandler
{
    public static final ResourceLocation PERK_PROVIDER_KEY;
    public static final ResourceLocation EQUIPMENT_PROVIDER_KEY;
    public static final ModifierManager INSTANCE;
    private static Map<ResourceLocation, ModifierSourceProvider<?>> sourceProviders;
    private static final Map<UUID, Set<ModifierSource>> modifierCache;
    private static final Map<UUID, Set<ModifierSource>> modifierCacheClient;
    
    private ModifierManager() {
    }
    
    public static void init() {
        if (ModifierManager.sourceProviders == null) {
            (ModifierManager.sourceProviders = new HashMap<ResourceLocation, ModifierSourceProvider<?>>()).put(ModifierManager.PERK_PROVIDER_KEY, new PerkSourceProvider());
            ModifierManager.sourceProviders.put(ModifierManager.EQUIPMENT_PROVIDER_KEY, new EquipmentSourceProvider());
            MinecraftForge.EVENT_BUS.post((Event)new ASRegistryEvents.ModifierSourceRegister(sourceProvider -> ModifierManager.sourceProviders.put(sourceProvider.getKey(), sourceProvider)));
        }
    }
    
    @Nullable
    public static ModifierSourceProvider<?> getProvider(final ResourceLocation key) {
        return ModifierManager.sourceProviders.get(key);
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        if (!side.isServer() || !(player instanceof ServerPlayer)) {
            return;
        }
        final ServerPlayer serverPlayer = (ServerPlayer)player;
        for (final ModifierSourceProvider<?> sourceProvider : ModifierManager.sourceProviders.values()) {
            sourceProvider.update(serverPlayer);
        }
    }
    
    @Nonnull
    private static Set<ModifierSource> getModifiers(final Player player, final LogicalSide side) {
        if (side.isClient()) {
            return ModifierManager.modifierCacheClient.computeIfAbsent(player.getUUID(), uuid -> new HashSet());
        }
        return ModifierManager.modifierCache.computeIfAbsent(player.getUUID(), uuid -> new HashSet());
    }
    
    @Nonnull
    public static Set<ModifierSource> getAppliedModifiers(final Player player, final LogicalSide side) {
        return new HashSet<ModifierSource>(getModifiers(player, side));
    }
    
    public static void addModifier(final Player player, final LogicalSide side, final ModifierSource source) {
        final Set<ModifierSource> modifiers = getModifiers(player, side);
        if (!modifiers.contains(source) && modifiers.add(source)) {
            source.onApply(player, side);
        }
    }
    
    public static void removeModifier(final Player player, final LogicalSide side, final ModifierSource source) {
        final Set<ModifierSource> modifiers = getModifiers(player, side);
        if (modifiers.remove(source)) {
            source.onRemove(player, side);
        }
    }
    
    public static boolean isModifierApplied(final Player player, final LogicalSide side, final ModifierSource source) {
        return getModifiers(player, side).contains(source);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void clearClientCache() {
        ModifierManager.modifierCacheClient.clear();
    }
    
    public static void onDisconnect(final ServerPlayer player) {
        for (final ModifierSourceProvider<?> sourceProvider : ModifierManager.sourceProviders.values()) {
            sourceProvider.removeModifiers(player);
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Modifier Source Manager";
    }
    
    static {
        PERK_PROVIDER_KEY = AstralSorcery.key("perks");
        EQUIPMENT_PROVIDER_KEY = AstralSorcery.key("equipment");
        INSTANCE = new ModifierManager();
        ModifierManager.sourceProviders = null;
        modifierCache = new HashMap<UUID, Set<ModifierSource>>();
        modifierCacheClient = new HashMap<UUID, Set<ModifierSource>>();
    }
}
