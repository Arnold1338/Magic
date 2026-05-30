package hellfirepvp.astralsorcery.common;

import net.minecraft.server.packs.resources.PreparableReloadListener;

import hellfirepvp.astralsorcery.common.item.armor.ArmorMaterialImbuedLeather;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.DamageSourceUtil;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStartedEvent;

import hellfirepvp.astralsorcery.common.perk.data.PerkTreeLoader;
import net.minecraftforge.event.AddReloadListenerEvent;
import hellfirepvp.astralsorcery.common.integration.IntegrationCurios;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import hellfirepvp.astralsorcery.common.cmd.CommandAstralSorcery;
import net.minecraftforge.event.RegisterCommandsEvent;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonDataManager;
import hellfirepvp.astralsorcery.common.util.collision.CollisionManager;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightNetworkRegistry;
import hellfirepvp.astralsorcery.common.registry.RegistryCapabilities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import hellfirepvp.astralsorcery.common.network.play.server.PktOpenGui;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import java.io.File;
import net.minecraftforge.common.util.FakePlayerFactory;
import com.mojang.authlib.GameProfile;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraft.server.level.ServerLevel;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffectRegistry;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectRegistry;
import hellfirepvp.astralsorcery.common.data.config.entry.WorldGenConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.common.CommonGeneralConfig;
import hellfirepvp.astralsorcery.common.registry.RegistryPerks;
import hellfirepvp.astralsorcery.common.enchantment.amulet.AmuletRandomizeHelper;
import hellfirepvp.astralsorcery.common.data.config.entry.PerkConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.LogConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.LightNetworkConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.CraftingConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.EntityConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.MachineryConfig;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import hellfirepvp.astralsorcery.common.data.config.entry.WandsConfig;
import hellfirepvp.astralsorcery.common.data.config.entry.ToolsConfig;
import hellfirepvp.astralsorcery.common.data.config.registry.EntityTransmutationRegistry;
import hellfirepvp.astralsorcery.common.data.config.registry.OreBlockRarityRegistry;
import hellfirepvp.astralsorcery.common.data.config.registry.OreItemRarityRegistry;
import hellfirepvp.astralsorcery.common.data.config.registry.WeightedPerkAttributeRegistry;
import hellfirepvp.astralsorcery.common.data.config.registry.AmuletEnchantmentRegistry;
import hellfirepvp.astralsorcery.common.data.config.registry.TileAccelerationBlacklistRegistry;
import hellfirepvp.astralsorcery.common.data.config.registry.TechnicalEntityRegistry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;
import hellfirepvp.astralsorcery.common.data.config.registry.FluidRarityRegistry;
import hellfirepvp.astralsorcery.common.perk.PerkCooldownHelper;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperTemporaryFlight;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperEnchantmentTick;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.util.time.TimeStopController;
import hellfirepvp.astralsorcery.common.base.patreon.manager.PatreonManager;
import hellfirepvp.astralsorcery.common.perk.tick.PerkTickHelper;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.common.auxiliary.link.LinkHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.astralsorcery.common.integration.IntegrationCraftTweaker;
import hellfirepvp.astralsorcery.common.base.Mods;
import hellfirepvp.observerlib.common.event.BlockChangeNotifier;
import hellfirepvp.astralsorcery.common.event.handler.EventHandlerAutoLink;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionChunkTracker;
import net.minecraftforge.eventbus.api.EventPriority;
import hellfirepvp.astralsorcery.common.util.BlockDropCaptureAssist;
import hellfirepvp.astralsorcery.common.enchantment.amulet.PlayerAmuletHandler;
import hellfirepvp.astralsorcery.common.registry.RegistryWorldGeneration;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeLimiter;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperDamageCancelling;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperEntityFreeze;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperInvulnerability;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperSpawnDeny;
import hellfirepvp.astralsorcery.common.event.handler.EventHandlerMisc;
import hellfirepvp.astralsorcery.common.event.handler.EventHandlerBlockStorage;
import hellfirepvp.astralsorcery.common.event.handler.EventHandlerInteract;
import hellfirepvp.astralsorcery.common.registry.RegistryEntities;
import hellfirepvp.astralsorcery.common.registry.RegistryRegistries;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.tile.TileTreeBeacon;
import hellfirepvp.astralsorcery.common.auxiliary.BlockBreakHelper;
import hellfirepvp.astralsorcery.common.perk.PerkLevelManager;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.common.auxiliary.gateway.CelestialGatewayHandler;
import hellfirepvp.astralsorcery.common.event.handler.EventHandlerCache;
import hellfirepvp.astralsorcery.common.data.research.ResearchIOThread;
import hellfirepvp.astralsorcery.common.data.config.base.BaseConfiguration;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigRegistries;
import hellfirepvp.astralsorcery.common.registry.RegistryArgumentTypes;
import hellfirepvp.astralsorcery.common.registry.RegistryConstellations;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import hellfirepvp.astralsorcery.common.perk.data.PerkTypeHandler;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeTypeHandler;
import hellfirepvp.astralsorcery.common.registry.RegistryAdvancements;
import hellfirepvp.astralsorcery.common.registry.RegistryIngredientTypes;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.registry.RegistryStructureTypes;
import hellfirepvp.astralsorcery.common.registry.RegistryGameRules;
import hellfirepvp.astralsorcery.common.registry.RegistryMaterials;
import hellfirepvp.astralsorcery.common.registry.RegistryData;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.data.config.ServerConfig;
import hellfirepvp.astralsorcery.common.data.config.CommonConfig;
import hellfirepvp.astralsorcery.common.util.ServerLifecycleListener;
import java.util.List;
import hellfirepvp.observerlib.common.util.tick.TickManager;
import hellfirepvp.astralsorcery.common.registry.internal.PrimerEventHandler;
import hellfirepvp.astralsorcery.common.registry.internal.InternalRegistryPrimer;
import net.minecraft.world.item.IArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.damagesource.DamageSource;
import java.util.UUID;

public class CommonProxy
{
    public static final UUID FAKEPLAYER_UUID;
    public static DamageSource DAMAGE_SOURCE_BLEED;
    public static DamageSource DAMAGE_SOURCE_STELLAR;
    public static DamageSource DAMAGE_SOURCE_REFLECT;
    public static final CreativeModeTab ITEM_GROUP_AS;
    public static final CreativeModeTab ITEM_GROUP_AS_PAPERS;
    public static final CreativeModeTab ITEM_GROUP_AS_CRYSTALS;
    public static final Rarity RARITY_CELESTIAL;
    public static final Rarity RARITY_ARTIFACT;
    public static final Rarity RARITY_VESTIGE;
    public static final IArmorMaterial ARMOR_MATERIAL_IMBUED_LEATHER;
    private InternalRegistryPrimer registryPrimer;
    private PrimerEventHandler registryEventHandler;
    private CommonScheduler commonScheduler;
    private TickManager tickManager;
    private final List<ServerLifecycleListener> serverLifecycleListeners;
    private CommonConfig commonConfig;
    private ServerConfig serverConfig;
    
    public CommonProxy() {
        this.serverLifecycleListeners = Lists.newArrayList();
    }
    
    public void initialize() {
        this.registryPrimer = new InternalRegistryPrimer();
        this.registryEventHandler = new PrimerEventHandler(this.registryPrimer);
        this.commonScheduler = new CommonScheduler();
        this.commonConfig = new CommonConfig();
        this.serverConfig = new ServerConfig();
        RegistryData.init();
        RegistryMaterials.init();
        RegistryGameRules.init();
        RegistryStructureTypes.init();
        PacketChannel.registerPackets();
        RegistryIngredientTypes.init();
        RegistryAdvancements.init();
        AltarRecipeTypeHandler.init();
        PerkTypeHandler.init();
        ModifierManager.init();
        RegistryConstellations.init();
        RegistryArgumentTypes.init();
        this.initializeConfigurations();
        ConfigRegistries.getRegistries().buildDataRegistries(this.serverConfig);
        this.tickManager = new TickManager();
        this.attachTickListeners(this.tickManager::register);
        this.serverLifecycleListeners.add(ResearchIOThread.getInstance());
        this.serverLifecycleListeners.add(ServerLifecycleListener.wrap(EventHandlerCache::onServerStart, EventHandlerCache::onServerStop));
        this.serverLifecycleListeners.add(ServerLifecycleListener.wrap(CelestialGatewayHandler.INSTANCE::onServerStart, CelestialGatewayHandler.INSTANCE::onServerStop));
        this.serverLifecycleListeners.add(ServerLifecycleListener.start(PerkTree.PERK_TREE::setupServerPerkTree));
        this.serverLifecycleListeners.add(ServerLifecycleListener.start(PerkLevelManager::loadPerkLevels));
        this.serverLifecycleListeners.add(ServerLifecycleListener.stop(BlockBreakHelper::clearServerCache));
        this.serverLifecycleListeners.add(ServerLifecycleListener.stop(TileTreeBeacon.TreeWatcher::clearServerCache));
        this.serverLifecycleListeners.add(ServerLifecycleListener.stop(PlayerAffectionFlags::clearServerCache));
        SyncDataHolder.initialize();
        this.commonConfig.buildConfiguration();
    }
    
    public void attachLifecycle(final IEventBus modEventBus) {
        modEventBus.addListener((Consumer)this::onCommonSetup);
        modEventBus.addListener((Consumer)this::onEnqueueIMC);
        modEventBus.addListener((Consumer)BaseConfiguration::refreshConfiguration);
        modEventBus.addListener((Consumer)RegistryRegistries::buildRegistries);
        
        this.registryEventHandler.attachEventHandlers(modEventBus);
    }
    
    public void attachEventHandlers(final IEventBus eventBus) {
        eventBus.addListener((Consumer)this::onRegisterCommands);
        eventBus.addListener((Consumer)this::onServerStop);
        eventBus.addListener((Consumer)this::onServerStopping);
        eventBus.addListener((Consumer)this::onServerStarting);
        eventBus.addListener((Consumer)this::onServerStarted);
        eventBus.addListener((Consumer)this::onRegisterReloadListeners);
        EventHandlerInteract.attachListeners(eventBus);
        EventHandlerCache.attachListeners(eventBus);
        EventHandlerBlockStorage.attachListeners(eventBus);
        EventHandlerMisc.attachListeners(eventBus);
        EventHelperSpawnDeny.attachListeners(eventBus);
        EventHelperInvulnerability.attachListeners(eventBus);
        EventHelperEntityFreeze.attachListeners(eventBus);
        EventHelperDamageCancelling.attachListeners(eventBus);
        PerkAttributeLimiter.attachListeners(eventBus);
        eventBus.addListener((Consumer)RegistryWorldGeneration::loadBiomeFeatures);
        eventBus.addListener((Consumer)PlayerAmuletHandler::onEnchantmentAdd);
        eventBus.addListener((Consumer)BlockDropCaptureAssist.INSTANCE::onDrop);
        eventBus.addListener((Consumer)CelestialGatewayHandler.INSTANCE::onWorldInit);
        eventBus.addListener(EventPriority.LOW, (Consumer)TileTreeBeacon.TreeWatcher::onGrow);
        this.tickManager.attachListeners(eventBus);
        TransmissionChunkTracker.INSTANCE.attachListeners(eventBus);
        BlockChangeNotifier.addListener((BlockChangeNotifier.Listener)new EventHandlerAutoLink());
        Mods.CRAFTTWEAKER.executeIfPresent(() -> () -> IntegrationCraftTweaker.attachListeners(eventBus));
    }
    
    public void attachTickListeners(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)this.commonScheduler);
        registrar.accept((ITickHandler)StarlightTransmissionHandler.getInstance());
        registrar.accept((ITickHandler)StarlightUpdateHandler.getInstance());
        registrar.accept((ITickHandler)SyncDataHolder.getTickInstance());
        registrar.accept((ITickHandler)LinkHandler.getInstance());
        registrar.accept((ITickHandler)SkyHandler.getInstance());
        registrar.accept((ITickHandler)PlayerAmuletHandler.INSTANCE);
        registrar.accept((ITickHandler)PerkTickHelper.INSTANCE);
        registrar.accept((ITickHandler)PatreonManager.INSTANCE);
        registrar.accept((ITickHandler)TimeStopController.INSTANCE);
        registrar.accept((ITickHandler)AlignmentChargeHandler.INSTANCE);
        registrar.accept((ITickHandler)ModifierManager.INSTANCE);
        registrar.accept((ITickHandler)EventHelperEnchantmentTick.INSTANCE);
        EventHelperTemporaryFlight.attachTickListener(registrar);
        EventHelperSpawnDeny.attachTickListener(registrar);
        EventHelperInvulnerability.attachTickListener(registrar);
        EventHelperEntityFreeze.attachTickListener(registrar);
        PerkCooldownHelper.attachTickListeners(registrar);
        PlayerAffectionFlags.attachTickListeners(registrar);
    }
    
    protected void initializeConfigurations() {
        ConfigRegistries.getRegistries().addDataRegistry(FluidRarityRegistry.INSTANCE);
        ConfigRegistries.getRegistries().addDataRegistry(TechnicalEntityRegistry.INSTANCE);
        ConfigRegistries.getRegistries().addDataRegistry(TileAccelerationBlacklistRegistry.INSTANCE);
        ConfigRegistries.getRegistries().addDataRegistry(AmuletEnchantmentRegistry.INSTANCE);
        ConfigRegistries.getRegistries().addDataRegistry(WeightedPerkAttributeRegistry.INSTANCE);
        ConfigRegistries.getRegistries().addDataRegistry(OreItemRarityRegistry.VOID_TRASH_REWARD);
        ConfigRegistries.getRegistries().addDataRegistry(OreBlockRarityRegistry.STONE_ENRICHMENT);
        ConfigRegistries.getRegistries().addDataRegistry(OreBlockRarityRegistry.MINERALIS_RITUAL);
        ConfigRegistries.getRegistries().addDataRegistry(EntityTransmutationRegistry.INSTANCE);
        ToolsConfig.CONFIG.newSubSection(WandsConfig.CONFIG);
        MachineryConfig.CONFIG.newSubSection(TileTreeBeacon.Config.CONFIG);
        this.serverConfig.addConfigEntry(GeneralConfig.CONFIG);
        this.serverConfig.addConfigEntry(ToolsConfig.CONFIG);
        this.serverConfig.addConfigEntry(EntityConfig.CONFIG);
        this.serverConfig.addConfigEntry(CraftingConfig.CONFIG);
        this.serverConfig.addConfigEntry(LightNetworkConfig.CONFIG);
        this.serverConfig.addConfigEntry(LogConfig.CONFIG);
        this.serverConfig.addConfigEntry(PerkConfig.CONFIG);
        this.serverConfig.addConfigEntry(AmuletRandomizeHelper.CONFIG);
        this.serverConfig.addConfigEntry(MachineryConfig.CONFIG);
        RegistryPerks.initConfig(PerkConfig.CONFIG::newSubSection);
        this.commonConfig.addConfigEntry(CommonGeneralConfig.CONFIG);
        this.commonConfig.addConfigEntry(WorldGenConfig.CONFIG);
        RegistryWorldGeneration.addConfigEntries(WorldGenConfig.CONFIG::newSubSection);
        ConstellationEffectRegistry.addConfigEntries(this.serverConfig);
        MantleEffectRegistry.addConfigEntries(this.serverConfig);
    }
    
    public InternalRegistryPrimer getRegistryPrimer() {
        return this.registryPrimer;
    }
    
    public TickManager getTickManager() {
        return this.tickManager;
    }
    
    public FakePlayer getASFakePlayerServer(final ServerLevel world) {
        return FakePlayerFactory.get(world, new GameProfile(CommonProxy.FAKEPLAYER_UUID, "AS-FakePlayer"));
    }
    
    public File getASServerDataDirectory() {
        final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return null;
        }
        final File asDataDir = server.func_240776_a_(new FolderName("astralsorcery")).toFile();
        if (!asDataDir.exists()) {
            asDataDir.mkdirs();
        }
        return asDataDir;
    }
    
    public void scheduleClientside(final Runnable r, final int tickDelay) {
    }
    
    public void scheduleClientside(final Runnable r) {
        this.scheduleClientside(r, 0);
    }
    
    public void scheduleDelayed(final Runnable r, final int tickDelay) {
        this.commonScheduler.addRunnable(r, tickDelay);
    }
    
    public void scheduleDelayed(final Runnable r) {
        this.scheduleDelayed(r, 0);
    }
    
    public void openGuiClient(final GuiType type, final CompoundTag data) {
    }
    
    public void openGui(final Player player, final GuiType type, final Object... data) {
        if (player instanceof ServerPlayer && !(player instanceof FakePlayer)) {
            final PktOpenGui pkt = new PktOpenGui(type, type.serializeArguments(data));
            PacketChannel.CHANNEL.sendToPlayer(player, pkt);
        }
    }
    
    private void onCommonSetup(final FMLCommonSetupEvent event) {
        this.serverConfig.buildConfiguration();
        RegistryCapabilities.init(MinecraftForge.EVENT_BUS);
        StarlightNetworkRegistry.setupRegistry();
        CollisionManager.init();
        PatreonDataManager.loadPatreonEffects();
        event.enqueueWork(RegistryWorldGeneration::registerStructureGeneration);
    }
    
    private void onRegisterCommands(final RegisterCommandsEvent event) {
        CommandAstralSorcery.register((CommandDispatcher<CommandSourceStack>)event.getDispatcher());
    }
    
    private void onEnqueueIMC(final InterModEnqueueEvent event) {
        Mods.CURIOS.executeIfPresent(() -> IntegrationCurios::initIMC);
    }
    
    private void onRegisterReloadListeners(final AddReloadListenerEvent event) {
        event.addListener((PreparableReloadListener)PerkTreeLoader.INSTANCE);
    }
    
    private void onServerStarted(final ServerStartedEvent event) {
        this.serverLifecycleListeners.forEach(ServerLifecycleListener::onServerStart);
    }
    
    private void onServerStarting(final ServerStartingEvent event) {
    }
    
    private void onServerStopping(final ServerStoppingEvent event) {
        this.serverLifecycleListeners.forEach(ServerLifecycleListener::onServerStop);
    }
    
    private void onServerStop(final ServerStoppedEvent event) {
    }
    
    static {
        FAKEPLAYER_UUID = UUID.fromString("b0c3097f-8391-4b4b-a89a-553ef730b13a");
        CommonProxy.DAMAGE_SOURCE_BLEED = DamageSourceUtil.newType("astralsorcery.bleed").func_76348_h();
        CommonProxy.DAMAGE_SOURCE_STELLAR = DamageSourceUtil.newType("astralsorcery.stellar").func_76348_h().func_82726_p();
        CommonProxy.DAMAGE_SOURCE_REFLECT = DamageSourceUtil.newType("thorns").func_76348_h().func_151518_m();
        ITEM_GROUP_AS = new CreativeModeTab("astralsorcery") {
            public ItemStack func_78016_d() {
                return new ItemStack((ItemLike)ItemsAS.TOME);
            }
        };
        ITEM_GROUP_AS_PAPERS = new CreativeModeTab("astralsorcery.papers") {
            public ItemStack func_78016_d() {
                return new ItemStack((ItemLike)ItemsAS.CONSTELLATION_PAPER);
            }
        };
        ITEM_GROUP_AS_CRYSTALS = new CreativeModeTab("astralsorcery.crystals") {
            public ItemStack func_78016_d() {
                return new ItemStack((ItemLike)ItemsAS.ROCK_CRYSTAL);
            }
        };
        RARITY_CELESTIAL = Rarity.create("AS_CELESTIAL", ChatFormatting.BLUE);
        RARITY_ARTIFACT = Rarity.create("AS_ARTIFACT", ChatFormatting.GOLD);
        RARITY_VESTIGE = Rarity.create("AS_VESTIGE", ChatFormatting.RED);
        ARMOR_MATERIAL_IMBUED_LEATHER = (IArmorMaterial)new ArmorMaterialImbuedLeather();
    }
}
