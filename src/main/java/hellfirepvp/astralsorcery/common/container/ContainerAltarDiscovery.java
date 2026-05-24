package hellfirepvp.astralsorcery.common.container;

import net.minecraft.world.item.ItemStack;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import net.minecraft.world.level.Container;
import net.minecraft.world.level.inventory.Slot;
import net.minecraft.world.level.inventory.MenuType;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import net.minecraft.world.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class ContainerAltarDiscovery extends ContainerAltarBase
{
    public ContainerAltarDiscovery(final TileAltar altar, final Inventory inv, final int windowId) {
        super(altar, ContainerTypesAS.ALTAR_DISCOVERY, inv, windowId);
    }
    
    @Override
    void bindPlayerInventory(final Inventory plInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot((Container)plInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot((Container)plInventory, i, 8 + i * 18, 142));
        }
    }
    
    @Override
    void bindAltarInventory(final TileInventory altarInventory) {
        for (int xx = 0; xx < 3; ++xx) {
            this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 6 + xx, 62 + xx * 18, 11));
        }
        for (int xx = 0; xx < 3; ++xx) {
            this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 11 + xx, 62 + xx * 18, 29));
        }
        for (int xx = 0; xx < 3; ++xx) {
            this.func_75146_a((Slot)new SlotItemHandler((IItemHandler)altarInventory, 16 + xx, 62 + xx * 18, 47));
        }
    }
    
    @Override
    Optional<ItemStack> handleCustomTransfer(final Player player, final int index) {
        return Optional.empty();
    }
    
    @Override
    public int translateIndex(final int fromIndex) {
        if (fromIndex >= 16) {
            return fromIndex - 10;
        }
        if (fromIndex >= 11) {
            return fromIndex - 8;
        }
        return fromIndex - 6;
    }
}
