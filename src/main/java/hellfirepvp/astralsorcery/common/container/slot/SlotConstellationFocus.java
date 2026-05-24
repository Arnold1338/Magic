package hellfirepvp.astralsorcery.common.container.slot;

import net.minecraft.world.level.inventory.Slot;
import net.minecraft.world.level.entity.player.Player;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.item.base.IConstellationFocus;
import net.minecraft.world.level.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraftforge.items.SlotItemHandler;

public class SlotConstellationFocus extends SlotItemHandler
{
    private final TileAltar altar;
    
    public SlotConstellationFocus(final IItemHandler itemHandler, final TileAltar altar, final int xPosition, final int yPosition) {
        super(itemHandler, 100, xPosition, yPosition);
        this.altar = altar;
    }
    
    public boolean func_75214_a(final ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IConstellationFocus && ((IConstellationFocus)stack.getItem()).getFocusConstellation(stack) != null;
    }
    
    public ItemStack func_75211_c() {
        return this.altar.getFocusItem();
    }
    
    public void func_75215_d(@Nonnull final ItemStack stack) {
        this.altar.setFocusItem(stack);
    }
    
    public boolean func_82869_a(final Player playerIn) {
        return true;
    }
    
    public ItemStack func_190901_a(final Player thePlayer, final ItemStack stack) {
        this.altar.markForUpdate();
        return super.func_190901_a(thePlayer, stack);
    }
    
    public ItemStack func_75209_a(final int amount) {
        final ItemStack focus = this.altar.getFocusItem();
        this.altar.setFocusItem(ItemStack.field_190927_a);
        return focus;
    }
    
    public boolean isSameInventory(final Slot other) {
        return false;
    }
    
    public int func_178170_b(final ItemStack stack) {
        return 1;
    }
    
    public int func_75219_a() {
        return 1;
    }
}
