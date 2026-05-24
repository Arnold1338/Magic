package hellfirepvp.astralsorcery;

import org.apache.logging.log4j.LogManager;
import java.util.function.Supplier;
import net.minecraftforge.fml.DatagenModLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.DistExecutor;
import hellfirepvp.astralsorcery.client.ClientProxy;
import net.minecraftforge.fml.ModList;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraftforge.fml.ModContainer;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod("astralsorcery")
public class AstralSorcery
{
    public static final String MODID = "astralsorcery";
    public static final String NAME = "Astral Sorcery";
    public static Logger log;
    private static AstralSorcery instance;
    private static ModContainer modContainer;
    private final CommonProxy proxy;
    
    public AstralSorcery() {
        AstralSorcery.instance = this;
        AstralSorcery.modContainer = ModList.get().getModContainerById("astralsorcery").get();
        (this.proxy = (CommonProxy)DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new)).initialize();
        this.proxy.attachLifecycle(FMLJavaModLoadingContext.get().getModEventBus());
        this.proxy.attachEventHandlers(MinecraftForge.EVENT_BUS);
    }
    
    public static AstralSorcery getInstance() {
        return AstralSorcery.instance;
    }
    
    public static ModContainer getModContainer() {
        return AstralSorcery.modContainer;
    }
    
    public static CommonProxy getProxy() {
        return getInstance().proxy;
    }
    
    public static ResourceLocation key(final String path) {
        return new ResourceLocation("astralsorcery", path);
    }
    
    public static boolean isDoingDataGeneration() {
        return DatagenModLoader.isRunningDataGen();
    }
    
    static {
        AstralSorcery.log = LogManager.getLogger("Astral Sorcery");
    }
}
