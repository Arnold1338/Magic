package hellfirepvp.astralsorcery.common.container.slot;

import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.Container;
import net.minecraft.world.level.inventory.Slot;

public class SlotUnclickable extends Slot
{
    public SlotUnclickable(final Container inventoryIn, final int index, final int xPosition, final int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }
    
    public boolean func_82869_a(final Player playerIn) {
        return false;
    }
}
