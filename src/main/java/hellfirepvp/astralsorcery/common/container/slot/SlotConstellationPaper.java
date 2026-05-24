package hellfirepvp.astralsorcery.common.container.slot;

import hellfirepvp.astralsorcery.common.item.ItemConstellationPaper;
import net.minecraft.world.level.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.container.ContainerTome;
import net.minecraftforge.items.SlotItemHandler;

public class SlotConstellationPaper extends SlotItemHandler
{
    private final ContainerTome listener;
    
    public SlotConstellationPaper(final ContainerTome tome, final IItemHandler inventoryIn, final int index, final int xPosition, final int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.listener = tome;
    }
    
    public boolean func_75214_a(final ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemConstellationPaper && ((ItemConstellationPaper)stack.getItem()).getConstellation(stack) != null;
    }
    
    public void func_75218_e() {
        super.func_75218_e();
        this.listener.slotChanged();
    }
}
