package hellfirepvp.astralsorcery.client.event;

import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.item.base.client.ItemOverlayRender;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class OverlayRenderer
{
    public static final OverlayRenderer INSTANCE;
    
    private OverlayRenderer() {
    }
    
    public void attachEventListeners(final IEventBus bus) {
        bus.addListener(EventPriority.LOW, (Consumer)this::onOverlayRender);
    }
    
    private void onOverlayRender(final RenderGameOverlayEvent.Post event) {
        final float pTicks = event.getPartialTicks();
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        final Player player = (Player)Minecraft.getInstance().field_71439_g;
        if (player == null || Minecraft.getInstance().field_71441_e == null) {
            return;
        }
        final PoseStack renderStack = event.getMatrixStack();
        for (final EquipmentSlot type : EquipmentSlot.values()) {
            if (this.doHudRender(renderStack, player.getItemBySlot(type), pTicks)) {
                break;
            }
        }
    }
    
    private boolean doHudRender(final PoseStack renderStack, final ItemStack heldItem, final float pTicks) {
        if (heldItem.isEmpty()) {
            return false;
        }
        final Item held = heldItem.getItem();
        return held instanceof ItemOverlayRender && ((ItemOverlayRender)held).renderOverlay(renderStack, heldItem, pTicks);
    }
    
    static {
        INSTANCE = new OverlayRenderer();
    }
}
