package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.container.ScreenContainerAltarRadiance;
import hellfirepvp.astralsorcery.client.screen.container.ScreenContainerAltarConstellation;
import hellfirepvp.astralsorcery.client.screen.container.ScreenContainerAltarAttunement;
import hellfirepvp.astralsorcery.client.screen.container.ScreenContainerAltarDiscovery;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import hellfirepvp.astralsorcery.client.screen.ScreenObservatory;
import hellfirepvp.astralsorcery.common.container.ContainerObservatory;
import net.minecraft.world.level.inventory.MenuType;
import net.minecraft.client.gui.ScreenManager;
import hellfirepvp.astralsorcery.client.screen.container.ScreenContainerTome;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarRadianceProvider;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarConstellationProvider;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarAttunementProvider;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarDiscoveryProvider;
import hellfirepvp.astralsorcery.common.container.factory.ContainerObservatoryProvider;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import net.minecraft.world.level.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.network.IContainerFactory;
import hellfirepvp.astralsorcery.common.container.factory.ContainerTomeProvider;

public class RegistryContainerTypes
{
    private RegistryContainerTypes() {
    }
    
    public static void init() {
        ContainerTypesAS.TOME = register("tome", (net.minecraftforge.fml.network.IContainerFactory<Container>)new ContainerTomeProvider.Factory());
        ContainerTypesAS.OBSERVATORY = register("observatory", (net.minecraftforge.fml.network.IContainerFactory<Container>)new ContainerObservatoryProvider.Factory());
        ContainerTypesAS.ALTAR_DISCOVERY = register("altar_discovery", (net.minecraftforge.fml.network.IContainerFactory<Container>)new ContainerAltarDiscoveryProvider.Factory());
        ContainerTypesAS.ALTAR_ATTUNEMENT = register("altar_attunement", (net.minecraftforge.fml.network.IContainerFactory<Container>)new ContainerAltarAttunementProvider.Factory());
        ContainerTypesAS.ALTAR_CONSTELLATION = register("altar_constellation", (net.minecraftforge.fml.network.IContainerFactory<Container>)new ContainerAltarConstellationProvider.Factory());
        ContainerTypesAS.ALTAR_RADIANCE = register("altar_radiance", (net.minecraftforge.fml.network.IContainerFactory<Container>)new ContainerAltarRadianceProvider.Factory());
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        ScreenManager.func_216911_a((ContainerType)ContainerTypesAS.TOME, ScreenContainerTome::new);
        ScreenManager.func_216911_a((ContainerType)ContainerTypesAS.OBSERVATORY, (ScreenManager.IScreenFactory)new ScreenManager.IScreenFactory<ContainerObservatory, ScreenObservatory>() {
            public ScreenObservatory create(final ContainerObservatory observatory, final Inventory playerInventory, final Component name) {
                return new ScreenObservatory(observatory);
            }
        });
        ScreenManager.func_216911_a((ContainerType)ContainerTypesAS.ALTAR_DISCOVERY, ScreenContainerAltarDiscovery::new);
        ScreenManager.func_216911_a((ContainerType)ContainerTypesAS.ALTAR_ATTUNEMENT, ScreenContainerAltarAttunement::new);
        ScreenManager.func_216911_a((ContainerType)ContainerTypesAS.ALTAR_CONSTELLATION, ScreenContainerAltarConstellation::new);
        ScreenManager.func_216911_a((ContainerType)ContainerTypesAS.ALTAR_RADIANCE, ScreenContainerAltarRadiance::new);
    }
    
    private static <C extends Container, T extends ContainerType<C>> T register(final String name, final IContainerFactory<C> containerFactory) {
        return register(AstralSorcery.key(name), containerFactory);
    }
    
    private static <C extends Container, T extends ContainerType<C>> T register(final ResourceLocation name, final IContainerFactory<C> containerFactory) {
        final ContainerType<C> type = (ContainerType<C>)new ContainerType((ContainerType.IFactory)containerFactory);
        type.setRegistryName(name);
        AstralSorcery.getProxy().getRegistryPrimer().register(type);
        return (T)type;
    }
}
