package hellfirepvp.astralsorcery.client.util;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraft.client.Minecraft;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class MouseUtil
{
    private static boolean preventGuiChange;
    
    public static void attachEventListeners(final IEventBus eventBus) {
        eventBus.addListener(EventPriority.HIGHEST, (Consumer)MouseUtil::onGuiOpen);
    }
    
    public static void ungrab() {
        Minecraft.func_71410_x().field_71417_B.func_198032_j();
    }
    
    public static void grab() {
        MouseUtil.preventGuiChange = true;
        Minecraft.func_71410_x().field_71417_B.func_198034_i();
    }
    
    private static void onGuiOpen(final GuiOpenEvent event) {
        if (MouseUtil.preventGuiChange) {
            MouseUtil.preventGuiChange = false;
            event.setCanceled(true);
        }
    }
    
    static {
        MouseUtil.preventGuiChange = false;
    }
}
