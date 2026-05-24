package hellfirepvp.astralsorcery.common.container;

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

public class ContainerAltarConstellation extends ContainerAltarBase
{
    public ContainerAltarConstellation(final TileAltar altar, final Inventory inv, final int windowId) {
        super(altar, ContainerTypesAS.ALTAR_CONSTELLATION, inv, windowId);
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
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 0, 84, 11));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 1, 102, 11));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 3, 138, 11));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 4, 156, 11));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 5, 84, 29));
        for (int xx = 0; xx < 3; ++xx) {
            this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 6 + xx, 102 + xx * 18, 29));
        }
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 9, 156, 29));
        for (int xx = 0; xx < 3; ++xx) {
            this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 11 + xx, 102 + xx * 18, 47));
        }
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 15, 84, 65));
        for (int xx = 0; xx < 3; ++xx) {
            this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 16 + xx, 102 + xx * 18, 65));
        }
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 19, 156, 65));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 20, 84, 83));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 21, 102, 83));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 23, 138, 83));
        this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 24, 156, 83));
    }
    
    @Override
    Optional<ItemStack> handleCustomTransfer(final Player player, final int index) {
        return Optional.empty();
    }
    
    @Override
    public int translateIndex(final int fromIndex) {
        if (fromIndex >= 23) {
            return fromIndex - 4;
        }
        if (fromIndex >= 15) {
            return fromIndex - 3;
        }
        if (fromIndex >= 11) {
            return fromIndex - 2;
        }
        if (fromIndex >= 3) {
            return fromIndex - 1;
        }
        return fromIndex;
    }
}
