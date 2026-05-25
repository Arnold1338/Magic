package hellfirepvp.astralsorcery.common.event.handler;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchSyncHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.util.time.TimeStopController;
import net.minecraftforge.event.level.LevelEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperInvulnerability;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperSpawnDeny;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperTemporaryFlight;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.common.util.world.WorldSeedCache;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import java.util.Collection;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.auxiliary.gateway.CelestialGatewayHandler;
import hellfirepvp.astralsorcery.common.perk.PerkCooldownHelper;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import hellfirepvp.astralsorcery.common.perk.PerkEffectHelper;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.PerkLevelManager;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.client.util.camera.ClientCameraManager;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalProgression;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHandler;
import hellfirepvp.astralsorcery.client.util.AreaOfInfluencePreview;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandlerCache
{
    private EventHandlerCache() {
    }
    
    public static void attachListeners(final IEventBus eventBus) {
        eventBus.register((Object)EventHandlerCache.class);
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientDisconnect(final ClientPlayerNetworkEvent.LoggedOutEvent event) {
        AreaOfInfluencePreview.INSTANCE.clearClient();
        EffectHandler.cleanUp();
        ScreenJournalProgression.resetJournal();
        ClientCameraManager.INSTANCE.removeAllAndCleanup();
        SyncDataHolder.clear(LogicalSide.CLIENT);
        PerkTree.PERK_TREE.clearCache(LogicalSide.CLIENT);
        PerkLevelManager.clearCache(LogicalSide.CLIENT);
        PerkAttributeHelper.clearClient();
        PerkAttributeType.clearCache(LogicalSide.CLIENT);
        PerkEffectHelper.clientClearAllPerks();
        ModifierManager.clearClientCache();
        PerkCooldownHelper.clearCache(LogicalSide.CLIENT);
        CelestialGatewayHandler.INSTANCE.updateClientCache(null);
        WorldSeedCache.clearClient();
        SkyHandler.getInstance().clientClearCache();
        AstralSorcery.log.info("Client cache cleared!");
    }
    
    public static void onServerStart() {
    }
    
    public static void onServerStop() {
        SyncDataHolder.clear(LogicalSide.SERVER);
        PerkTree.PERK_TREE.clearCache(LogicalSide.SERVER);
        PerkAttributeHelper.clearServer();
        PerkAttributeType.clearCache(LogicalSide.SERVER);
        PerkCooldownHelper.clearCache(LogicalSide.SERVER);
        StarlightTransmissionHandler.getInstance().clearServer();
        StarlightUpdateHandler.getInstance().clearServer();
        EventHelperTemporaryFlight.clearServer();
        EventHelperSpawnDeny.clearServer();
        EventHelperInvulnerability.clearServer();
        ResearchHelper.saveAndClearServerCache();
    }
    
    @SubscribeEvent
    public static void onUnload(final WorldEvent.Unload event) {
        final IWorld w = event.getWorld();
        if (w instanceof Level) {
            final Level world = (Level)w;
            SyncDataHolder.clearWorld(world);
            StarlightTransmissionHandler.getInstance().informWorldUnload(world);
            TimeStopController.onWorldUnload(world);
            SkyHandler.getInstance().informWorldUnload(world);
        }
    }
    
    @SubscribeEvent
    public static void onPlayerConnect(final PlayerEvent.PlayerLoggedInEvent event) {
        final ServerPlayer player = (ServerPlayer)event.getPlayer();
        final PlayerProgress progress = ResearchHelper.getProgress((Player)player, LogicalSide.SERVER);
        if ((boolean)GeneralConfig.CONFIG.giveJournalOnJoin.get() && !progress.didReceiveTome() && player.getInventory().func_70441_a(new ItemStack((ItemLike)ItemsAS.TOME))) {
            ResearchManager.setTomeReceived((Player)player);
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, (Player)player);
        PerkEffectHelper.onPlayerConnectEvent(player);
    }
    
    @SubscribeEvent
    public static void onPlayerDisconnect(final PlayerEvent.PlayerLoggedOutEvent event) {
        final ServerPlayer player = (ServerPlayer)event.getPlayer();
        EventHelperTemporaryFlight.onDisconnect(player);
        PerkEffectHelper.onPlayerDisconnectEvent(player);
        ModifierManager.onDisconnect(player);
    }
    
    @SubscribeEvent
    public static void onPlayerClone(final PlayerEvent.Clone event) {
        PerkEffectHelper.onPlayerCloneEvent((ServerPlayer)event.getOriginal(), (ServerPlayer)event.getPlayer());
    }
}
