package hellfirepvp.astralsorcery.common.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class SlotUnclickable extends Slot
{
    public SlotUnclickable(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }
    
    public boolean func_82869_a(final Player playerIn) {
        return false;
    }
}
