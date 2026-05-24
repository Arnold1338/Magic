package hellfirepvp.astralsorcery.common.container;

import hellfirepvp.astralsorcery.common.item.base.IConstellationFocus;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.MenuType;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import net.minecraft.world.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.container.slot.SlotConstellationFocus;

public class ContainerAltarTrait extends ContainerAltarBase
{
    private SlotConstellationFocus focusSlot;
    
    public ContainerAltarTrait(final TileAltar altar, final Inventory inv, final int windowId) {
        super(altar, ContainerTypesAS.ALTAR_RADIANCE, inv, windowId);
    }
    
    @Override
    void bindPlayerInventory(final Inventory plInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot((IInventory)plInventory, j + i * 9 + 9, 48 + j * 18, 120 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot((IInventory)plInventory, i, 48 + i * 18, 178));
        }
    }
    
    @Override
    void bindAltarInventory(final TileInventory altarInventory) {
        for (int yy = 0; yy < 5; ++yy) {
            for (int xx = 0; xx < 5; ++xx) {
                this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, xx + yy * 5, 84 + xx * 18, 11 + yy * 18));
            }
        }
        this.func_75146_a((Slot)(this.focusSlot = new SlotConstellationFocus((IItemHandler)altarInventory, this.getTileEntity(), 35, 11)));
    }
    
    @Override
    Optional<ItemStack> handleCustomTransfer(final Player player, final int index) {
        final Slot slot = this.field_75151_b.get(index);
        if (slot != null && slot.func_75216_d()) {
            final ItemStack slotStack = slot.func_75211_c();
            if (index < 36 && slotStack.getItem() instanceof IConstellationFocus && ((IConstellationFocus)slotStack.getItem()).getFocusConstellation(slotStack) != null && this.func_75135_a(slotStack, this.focusSlot.field_75222_d, this.focusSlot.field_75222_d + 1, false)) {
                return Optional.of(slotStack);
            }
        }
        return Optional.empty();
    }
    
    @Override
    public int translateIndex(final int fromIndex) {
        return fromIndex;
    }
}
