package hellfirepvp.observerlib;

import hellfirepvp.observerlib.api.ObserverHelper;
import hellfirepvp.observerlib.client.ClientProxy;
import hellfirepvp.observerlib.common.CommonProxy;
import hellfirepvp.observerlib.common.api.MatcherObserverHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

@Mod("observerlib")
public class ObserverLib {
    public static final String MODID = "observerlib";
    public static final String NAME = "ObserverLib";
    public static final Logger log = LogManager.getLogger("ObserverLib");

    private static ObserverLib instance;
    private final CommonProxy proxy;

    public ObserverLib() {
        ObserverLib.instance = this;
        this.proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        this.proxy.initialize();
        this.proxy.attachLifecycle(FMLJavaModLoadingContext.get().getModEventBus());
        this.proxy.attachEventHandlers(MinecraftForge.EVENT_BUS);
        ObserverHelper.setHelper(new MatcherObserverHelper());
    }

    public static CommonProxy getProxy() {
        return getInstance().proxy;
    }

    public static ObserverLib getInstance() {
        return ObserverLib.instance;
    }
}
