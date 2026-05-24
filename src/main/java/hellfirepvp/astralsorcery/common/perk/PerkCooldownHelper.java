package hellfirepvp.astralsorcery.common.perk;

import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.util.tick.TimeoutListContainer;

public class PerkCooldownHelper
{
    private static final TimeoutListContainer<UUID, ResourceLocation> perkCooldowns;
    private static final TimeoutListContainer<UUID, ResourceLocation> perkCooldownsClient;
    
    private PerkCooldownHelper() {
    }
    
    public static void attachTickListeners(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)PerkCooldownHelper.perkCooldowns);
        registrar.accept((ITickHandler)PerkCooldownHelper.perkCooldownsClient);
    }
    
    public static void clearCache(final LogicalSide side) {
        if (side.isClient()) {
            PerkCooldownHelper.perkCooldownsClient.clear();
        }
        else {
            PerkCooldownHelper.perkCooldowns.clear();
        }
    }
    
    public static void removeAllCooldowns(final Player player, final LogicalSide side) {
        final UUID playerUUID = player.getUUID();
        if (side.isClient()) {
            if (PerkCooldownHelper.perkCooldownsClient.hasList(playerUUID)) {
                PerkCooldownHelper.perkCooldownsClient.removeList(playerUUID);
            }
        }
        else if (PerkCooldownHelper.perkCooldowns.hasList(playerUUID)) {
            PerkCooldownHelper.perkCooldowns.removeList(playerUUID);
        }
    }
    
    public static void removePerkCooldowns(final LogicalSide side, final AbstractPerk perk) {
        if (!(perk instanceof CooldownPerk)) {
            return;
        }
        final TimeoutListContainer<UUID, ResourceLocation> container = side.isClient() ? PerkCooldownHelper.perkCooldownsClient : PerkCooldownHelper.perkCooldowns;
        container.removeList(key -> key.equals((Object)perk.getRegistryName()));
    }
    
    public static boolean isCooldownActiveForPlayer(final Player player, final AbstractPerk perk) {
        if (!(perk instanceof CooldownPerk)) {
            return false;
        }
        final TimeoutListContainer<UUID, ResourceLocation> container = player.func_130014_f_().isClientSide ? PerkCooldownHelper.perkCooldownsClient : PerkCooldownHelper.perkCooldowns;
        final UUID playerUUID = player.getUUID();
        return container.hasList(playerUUID) && container.getOrCreateList(playerUUID).contains(perk.getRegistryName());
    }
    
    public static void setCooldownActiveForPlayer(final Player player, final AbstractPerk perk, final int cooldownTicks) {
        if (!(perk instanceof CooldownPerk)) {
            return;
        }
        final TimeoutListContainer<UUID, ResourceLocation> container = player.func_130014_f_().isClientSide ? PerkCooldownHelper.perkCooldownsClient : PerkCooldownHelper.perkCooldowns;
        final UUID playerUUID = player.getUUID();
        container.getOrCreateList(playerUUID).setOrAddTimeout(cooldownTicks, perk.getRegistryName());
    }
    
    public static void forceSetCooldownForPlayer(final Player player, final AbstractPerk perk, final int cooldownTicks) {
        if (!(perk instanceof CooldownPerk)) {
            return;
        }
        final TimeoutListContainer<UUID, ResourceLocation> container = player.func_130014_f_().isClientSide ? PerkCooldownHelper.perkCooldownsClient : PerkCooldownHelper.perkCooldowns;
        final UUID playerUUID = player.getUUID();
        if (!container.getOrCreateList(playerUUID).setTimeout(cooldownTicks, perk.getRegistryName())) {
            setCooldownActiveForPlayer(player, perk, cooldownTicks);
        }
    }
    
    public static int getActiveCooldownForPlayer(final Player player, final AbstractPerk perk) {
        if (!(perk instanceof CooldownPerk)) {
            return -1;
        }
        final TimeoutListContainer<UUID, ResourceLocation> container = player.func_130014_f_().isClientSide ? PerkCooldownHelper.perkCooldownsClient : PerkCooldownHelper.perkCooldowns;
        final UUID playerUUID = player.getUUID();
        if (!container.hasList(playerUUID)) {
            return -1;
        }
        return container.getOrCreateList(playerUUID).getTimeout(perk.getRegistryName());
    }
    
    static {
        perkCooldowns = new TimeoutListContainer<UUID, ResourceLocation>(new PerkTimeoutHandler(LogicalSide.SERVER), new TickEvent.Type[] { TickEvent.Type.SERVER });
        perkCooldownsClient = new TimeoutListContainer<UUID, ResourceLocation>(new PerkTimeoutHandler(LogicalSide.CLIENT), new TickEvent.Type[] { TickEvent.Type.CLIENT });
    }
    
    public static class PerkTimeoutHandler implements TimeoutListContainer.ContainerTimeoutDelegate<UUID, ResourceLocation>
    {
        private final LogicalSide side;
        
        public PerkTimeoutHandler(final LogicalSide side) {
            this.side = side;
        }
        
        @Override
        public void onContainerTimeout(final UUID playerUUID, final ResourceLocation key) {
            final Player player = EntityUtils.getPlayer(playerUUID, this.side);
            if (player != null) {
                PerkTree.PERK_TREE.getPerk(this.side, key).ifPresent(perk -> {
                    if (perk instanceof CooldownPerk) {
                        ((CooldownPerk)perk).onCooldownTimeout(player);
                    }
                });
            }
        }
    }
}
