package hellfirepvp.astralsorcery.client.event;

import net.minecraft.world.level.item.Item;
import hellfirepvp.astralsorcery.common.item.base.client.ItemHeldRender;
import net.minecraft.world.level.item.ItemStack;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.entity.EquipmentSlot;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class ItemHeldEffectRenderer
{
    public static final ItemHeldEffectRenderer INSTANCE;
    
    private ItemHeldEffectRenderer() {
    }
    
    public void attachEventListeners(final IEventBus bus) {
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onHeldRender);
    }
    
    private void onHeldRender(final RenderWorldLastEvent event) {
        final float pTicks = event.getPartialTicks();
        final PoseStack renderStack = event.getMatrixStack();
        if (Minecraft.func_71410_x().field_71439_g == null || Minecraft.func_71410_x().field_71441_e == null) {
            return;
        }
        for (final EquipmentSlot type : EquipmentSlot.values()) {
            if (this.doHeldRender(Minecraft.func_71410_x().field_71439_g.getItemBySlot(type), renderStack, pTicks)) {
                break;
            }
        }
    }
    
    private boolean doHeldRender(final ItemStack heldItem, final PoseStack renderStack, final float pTicks) {
        if (heldItem.isEmpty()) {
            return false;
        }
        final Item held = heldItem.getItem();
        return held instanceof ItemHeldRender && ((ItemHeldRender)held).renderInHand(heldItem, renderStack, pTicks);
    }
    
    static {
        INSTANCE = new ItemHeldEffectRenderer();
    }
}
