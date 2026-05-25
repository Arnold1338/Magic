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

public class ContainerAltarAttunement extends ContainerAltarBase
{
    public ContainerAltarAttunement(final TileAltar altar, final Inventory inv, final int windowId) {
        super(altar, ContainerTypesAS.ALTAR_ATTUNEMENT, inv, windowId);
    }
    
    @Override
    void bindPlayerInventory(final Inventory plInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.render(new Slot((Container)plInventory, j + i * 9 + 9, 48 + j * 18, 120 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.render(new Slot((Container)plInventory, i, 48 + i * 18, 178));
        }
    }
    
    @Override
    void bindAltarInventory(final TileInventory altarInventory) {
        this.render((Slot)new SlotItemHandler((IItemHandler)altarInventory, 0, 84, 11));
        this.render((Slot)new SlotItemHandler((IItemHandler)altarInventory, 4, 156, 11));
        for (int xx = 0; xx < 3; ++xx) {
            this.render((Slot)new SlotItemHandler((IItemHandler)altarInventory, 6 + xx, 102 + xx * 18, 29));
        }
        for (int xx = 0; xx < 3; ++xx) {
            this.render((Slot)new SlotItemHandler((IItemHandler)altarInventory, 11 + xx, 102 + xx * 18, 47));
        }
        for (int xx = 0; xx < 3; ++xx) {
            this.render((Slot)new SlotItemHandler((IItemHandler)altarInventory, 16 + xx, 102 + xx * 18, 65));
        }
        this.render((Slot)new SlotItemHandler((IItemHandler)altarInventory, 20, 84, 83));
        this.render((Slot)new SlotItemHandler((IItemHandler)altarInventory, 24, 156, 83));
    }
    
    @Override
    Optional<ItemStack> handleCustomTransfer(final Player player, final int index) {
        return Optional.empty();
    }
    
    @Override
    public int translateIndex(final int fromIndex) {
        if (fromIndex == 24) {
            return 12;
        }
        if (fromIndex >= 20) {
            return 11;
        }
        if (fromIndex >= 16) {
            return fromIndex - 8;
        }
        if (fromIndex >= 11) {
            return fromIndex - 6;
        }
        if (fromIndex >= 6) {
            return fromIndex - 4;
        }
        if (fromIndex >= 4) {
            return 1;
        }
        return 0;
    }
}
