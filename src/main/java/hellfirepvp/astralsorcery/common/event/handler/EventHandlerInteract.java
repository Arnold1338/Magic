package hellfirepvp.astralsorcery.common.event.handler;

import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.base.TileOwned;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import hellfirepvp.astralsorcery.common.item.base.OverrideInteractItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandlerInteract
{
    private EventHandlerInteract() {
    }
    
    public static void attachListeners(final IEventBus eventBus) {
        eventBus.addListener((Consumer)EventHandlerInteract::onBlockInteract);
        eventBus.addListener((Consumer)EventHandlerInteract::onEntityInteract);
        eventBus.addListener((Consumer)EventHandlerInteract::onSinglePlace);
        eventBus.addListener((Consumer)EventHandlerInteract::onMultiPlace);
    }
    
    private static void onEntityInteract(final PlayerInteractEvent.EntityInteract event) {
        final ItemStack held = event.getItemStack();
        if (held.getItem() instanceof OverrideInteractItem) {
            final OverrideInteractItem item = (OverrideInteractItem)held.getItem();
            if (item.shouldInterceptEntityInteract(event.getSide(), event.getPlayer(), event.getHand(), event.getTarget()) && item.doEntityInteract(event.getSide(), event.getPlayer(), event.getHand(), event.getTarget())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
    
    private static void onBlockInteract(final PlayerInteractEvent.RightClickBlock event) {
        final ItemStack held = event.getItemStack();
        if (held.getItem() instanceof OverrideInteractItem) {
            final OverrideInteractItem item = (OverrideInteractItem)held.getItem();
            if (item.shouldInterceptBlockInteract(event.getSide(), event.getPlayer(), event.getHand(), event.getPos(), event.getFace()) && item.doBlockInteract(event.getSide(), event.getPlayer(), event.getHand(), event.getPos(), event.getFace())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
    
    private static void onSinglePlace(final BlockEvent.EntityPlaceEvent event) {
        if (event instanceof BlockEvent.EntityMultiPlaceEvent) {
            return;
        }
        final IWorld world = event.getWorld();
        if (world.func_201670_d() || !(event.getEntity() instanceof Player)) {
            return;
        }
        handleOwnerPlacement(world, event.getPos(), (Player)event.getEntity());
    }
    
    private static void onMultiPlace(final BlockEvent.EntityMultiPlaceEvent event) {
        final IWorld world = event.getWorld();
        if (world.func_201670_d() || !(event.getEntity() instanceof Player)) {
            return;
        }
        final Player placer = (Player)event.getEntity();
        for (final BlockSnapshot snapshot : event.getReplacedBlockSnapshots()) {
            handleOwnerPlacement(world, snapshot.getPos(), placer);
        }
    }
    
    private static void handleOwnerPlacement(final IWorld world, final BlockPos pos, final Player placer) {
        final TileOwned owned = MiscUtils.getTileAt((IBlockReader)world, pos, TileOwned.class, true);
        if (owned != null) {
            owned.setOwner(placer);
        }
    }
}
