package hellfirepvp.astralsorcery.client;

import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import net.minecraftforge.resource.VanillaResourceType;
import net.minecraftforge.resource.SelectiveReloadStateHandler;
import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.IProfiler;
import net.minecraft.resources.IResourceManager;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPerkTree;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalConstellationOverview;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournal;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.client.screen.journal.bookmark.BookmarkProvider;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalProgression;
import java.util.Map;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.model.BipedModel;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import hellfirepvp.astralsorcery.client.render.entity.layer.StarryLayerRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import hellfirepvp.astralsorcery.common.registry.RegistryBlockRenderTypes;
import hellfirepvp.astralsorcery.client.registry.RegistryKeyBindings;
import hellfirepvp.astralsorcery.common.registry.RegistryTileEntities;
import hellfirepvp.astralsorcery.common.registry.RegistryEntities;
import hellfirepvp.astralsorcery.common.registry.RegistryContainerTypes;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.player.AbstractClientPlayerEntity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.client.event.effect.LightbeamRenderHelper;
import hellfirepvp.astralsorcery.client.util.AreaOfInfluencePreview;
import hellfirepvp.astralsorcery.client.event.TimeStopEffectHandler;
import hellfirepvp.astralsorcery.client.util.camera.ClientCameraManager;
import hellfirepvp.astralsorcery.common.base.patreon.manager.PatreonManagerClient;
import hellfirepvp.astralsorcery.client.effect.handler.EffectUpdater;
import hellfirepvp.astralsorcery.client.util.draw.RenderInfo;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.astralsorcery.client.event.SkyRenderEventHandler;
import net.minecraftforge.eventbus.api.EventPriority;
import hellfirepvp.astralsorcery.client.event.GatewayInteractionHandler;
import hellfirepvp.astralsorcery.client.util.MouseUtil;
import hellfirepvp.astralsorcery.client.util.camera.CameraEventHelper;
import hellfirepvp.astralsorcery.client.event.OverlayRenderer;
import hellfirepvp.astralsorcery.client.event.ItemHeldEffectRenderer;
import hellfirepvp.astralsorcery.client.event.PerkExperienceRenderer;
import hellfirepvp.astralsorcery.client.event.AlignmentChargeRenderer;
import hellfirepvp.astralsorcery.client.event.effect.EffectRenderEventHandler;
import hellfirepvp.astralsorcery.common.registry.RegistryBlocks;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.client.util.word.RandomWordGenerator;
import hellfirepvp.astralsorcery.client.util.ColorizationHelper;
import hellfirepvp.astralsorcery.client.resource.AssetPreLoader;
import net.minecraft.resources.IFutureReloadListener;
import hellfirepvp.astralsorcery.client.resource.AssetLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.data.config.ClientConfig;
import hellfirepvp.astralsorcery.common.CommonProxy;

public class ClientProxy extends CommonProxy
{
    private ClientScheduler clientScheduler;
    private ClientConfig clientConfig;
    
    @Override
    public void initialize() {
        this.clientScheduler = new ClientScheduler();
        if (!AstralSorcery.isDoingDataGeneration()) {
            final IReloadableResourceManager resMgr = (IReloadableResourceManager)Minecraft.func_71410_x().func_195551_G();
            resMgr.func_219534_a((IFutureReloadListener)AssetLibrary.INSTANCE);
            resMgr.func_219534_a((IFutureReloadListener)AssetPreLoader.INSTANCE);
            resMgr.func_219534_a(ColorizationHelper.onReload());
            resMgr.func_219534_a((stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> stage.func_216872_a((Object)Unit.INSTANCE).thenRunAsync(() -> {
                if (!(!SelectiveReloadStateHandler.INSTANCE.get().test(VanillaResourceType.LANGUAGES))) {
                    PerkTree.PERK_TREE.getPerkPoints(LogicalSide.CLIENT).stream().map((Function<? super PerkTreePoint<?>, ?>)PerkTreePoint::getPerk).forEach(AbstractPerk::clearClientTextCaches);
                }
            }));
        }
        this.clientConfig = new ClientConfig();
        super.initialize();
        this.addTomeBookmarks();
        RandomWordGenerator.init();
        this.clientConfig.buildConfiguration();
    }
    
    @Override
    protected void initializeConfigurations() {
        super.initializeConfigurations();
        this.clientConfig.addConfigEntry(RenderingConfig.CONFIG);
    }
    
    @Override
    public void attachLifecycle(final IEventBus modEventBus) {
        super.attachLifecycle(modEventBus);
        modEventBus.addListener((Consumer)RegistryItems::registerColors);
        modEventBus.addListener((Consumer)RegistryBlocks::registerColors);
        modEventBus.addListener((Consumer)this::onClientSetup);
    }
    
    @Override
    public void attachEventHandlers(final IEventBus eventBus) {
        super.attachEventHandlers(eventBus);
        EffectRenderEventHandler.getInstance().attachEventListeners(eventBus);
        AlignmentChargeRenderer.INSTANCE.attachEventListeners(eventBus);
        PerkExperienceRenderer.INSTANCE.attachEventListeners(eventBus);
        ItemHeldEffectRenderer.INSTANCE.attachEventListeners(eventBus);
        OverlayRenderer.INSTANCE.attachEventListeners(eventBus);
        CameraEventHelper.attachEventListeners(eventBus);
        MouseUtil.attachEventListeners(eventBus);
        GatewayInteractionHandler.attachEventListeners(eventBus);
        eventBus.addListener(EventPriority.LOWEST, (Consumer)SkyRenderEventHandler::onRender);
        eventBus.addListener(EventPriority.LOWEST, (Consumer)SkyRenderEventHandler::onFog);
    }
    
    @Override
    public void attachTickListeners(final Consumer<ITickHandler> registrar) {
        super.attachTickListeners(registrar);
        registrar.accept((ITickHandler)this.clientScheduler);
        registrar.accept((ITickHandler)RenderInfo.getInstance());
        registrar.accept((ITickHandler)EffectUpdater.getInstance());
        registrar.accept((ITickHandler)PatreonManagerClient.INSTANCE);
        registrar.accept((ITickHandler)ClientCameraManager.INSTANCE);
        registrar.accept((ITickHandler)TimeStopEffectHandler.INSTANCE);
        registrar.accept((ITickHandler)AlignmentChargeRenderer.INSTANCE);
        registrar.accept((ITickHandler)PerkExperienceRenderer.INSTANCE);
        registrar.accept((ITickHandler)AreaOfInfluencePreview.INSTANCE);
        LightbeamRenderHelper.attachTickListener(registrar);
        EffectRenderEventHandler.getInstance().attachTickListeners(registrar);
    }
    
    @Override
    public void scheduleClientside(final Runnable r, final int tickDelay) {
        this.clientScheduler.addRunnable(r, tickDelay);
    }
    
    @Override
    public void openGuiClient(final GuiType type, final CompoundTag data) {
        final Screen toOpen = type.deserialize(data);
        if (toOpen != null) {
            Minecraft.func_71410_x().func_147108_a(toOpen);
        }
    }
    
    @Override
    public void openGui(final Player player, final GuiType type, final Object... data) {
        if (player instanceof AbstractClientPlayerEntity) {
            this.openGuiClient(type, type.serializeArguments(data));
            return;
        }
        super.openGui(player, type, data);
    }
    
    private void onClientSetup(final FMLClientSetupEvent event) {
        RegistryContainerTypes.initClient();
        RegistryEntities.initClient();
        RegistryTileEntities.initClient();
        RegistryKeyBindings.init();
        RegistryBlockRenderTypes.initBlocks();
        RegistryBlockRenderTypes.initFluids();
        RegistryItems.registerItemProperties();
        final Map<String, PlayerRenderer> playerRenderMap = Minecraft.func_71410_x().func_175598_ae().getSkinMap();
        PlayerRenderer renderer = playerRenderMap.get("slim");
        renderer.func_177094_a((LayerRenderer)new StarryLayerRenderer((net.minecraft.client.renderer.entity.IEntityRenderer<LivingEntity, BipedModel>)renderer, true));
        renderer = playerRenderMap.get("default");
        renderer.func_177094_a((LayerRenderer)new StarryLayerRenderer((net.minecraft.client.renderer.entity.IEntityRenderer<LivingEntity, BipedModel>)renderer, false));
    }
    
    private void addTomeBookmarks() {
        ScreenJournal.addBookmark(new BookmarkProvider("screen.astralsorcery.tome.progression", 10, (Supplier<Screen>)ScreenJournalProgression::getJournalInstance, () -> true));
        ScreenJournal.addBookmark(new BookmarkProvider("screen.astralsorcery.tome.constellations", 20, (Supplier<Screen>)ScreenJournalConstellationOverview::getConstellationScreen, () -> !ResearchHelper.getClientProgress().getSeenConstellations().isEmpty()));
        ScreenJournal.addBookmark(new BookmarkProvider("screen.astralsorcery.tome.perks", 30, (Supplier<Screen>)ScreenJournalPerkTree::new, () -> ResearchHelper.getClientProgress().isAttuned()));
    }
}
