package hellfirepvp.astralsorcery.common.event.handler;

import hellfirepvp.astralsorcery.common.network.play.client.PktClearBlockStorageStack;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.item.base.ItemBlockStorage;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandlerBlockStorage
{
    public static void attachListeners(final IEventBus bus) {
        bus.addListener((Consumer)EventHandlerBlockStorage::onClickAir);
        bus.addListener((Consumer)EventHandlerBlockStorage::onClickBlockServer);
    }
    
    private static void onClickBlockServer(final PlayerInteractEvent.LeftClickBlock event) {
        final ItemStack held = event.getItemStack();
        if (!event.getWorld().func_201670_d() && !held.isEmpty() && held.getItem() instanceof ItemBlockStorage) {
            ItemBlockStorage.clearContainerFor(event.getPlayer());
        }
    }
    
    private static void onClickAir(final PlayerInteractEvent.LeftClickEmpty event) {
        final ItemStack held = event.getItemStack();
        if (!held.isEmpty() && held.getItem() instanceof ItemBlockStorage) {
            PacketChannel.CHANNEL.sendToServer(new PktClearBlockStorageStack());
        }
    }
}
