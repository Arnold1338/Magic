package hellfirepvp.astralsorcery.common.container;

import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.constellation.ConstellationBaseItem;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.LinkedList;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import hellfirepvp.astralsorcery.common.item.ItemConstellationPaper;
import hellfirepvp.astralsorcery.common.container.slot.SlotConstellationPaper;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.Container;
import hellfirepvp.astralsorcery.common.container.slot.SlotUnclickable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import hellfirepvp.astralsorcery.common.item.ItemTome;
import net.minecraft.world.inventory.MenuType;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerTome extends Container
{
    private final Player owningPlayer;
    private final ItemStack parentTome;
    private final int tomeIndex;
    
    public ContainerTome(final int id, final Inventory plInventory, final Player owningPlayer, final ItemStack tome, final int tomeIndex) {
        super((ContainerType)ContainerTypesAS.TOME, id);
        this.parentTome = tome;
        this.tomeIndex = tomeIndex;
        this.owningPlayer = owningPlayer;
        this.buildPlayerSlots(plInventory);
        this.buildSlots((IItemHandler)new InvWrapper(ItemTome.getTomeStorage(tome, this.owningPlayer)));
    }
    
    private void buildPlayerSlots(final Inventory playerInv) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                final int index = j + i * 9 + 9;
                if (index == this.tomeIndex) {
                    this.func_75146_a((Slot)new SlotUnclickable((IInventory)playerInv, index, 8 + j * 18, 84 + i * 18));
                }
                else {
                    this.func_75146_a(new Slot((IInventory)playerInv, index, 8 + j * 18, 84 + i * 18));
                }
            }
        }
        for (int i = 0; i < 9; ++i) {
            if (i == this.tomeIndex) {
                this.func_75146_a((Slot)new SlotUnclickable((IInventory)playerInv, i, 8 + i * 18, 142));
            }
            else {
                this.func_75146_a(new Slot((IInventory)playerInv, i, 8 + i * 18, 142));
            }
        }
    }
    
    private void buildSlots(final IItemHandler handle) {
        for (int i = 0; i < 3; ++i) {
            for (int xx = 0; xx < 9; ++xx) {
                this.func_75146_a((Slot)new SlotConstellationPaper(this, handle, i * 9 + xx, 8 + xx * 18, 13 + i * 18));
            }
        }
    }
    
    public ItemStack func_82846_b(final Player playerIn, final int index) {
        ItemStack itemstack = ItemStack.field_190927_a;
        final Slot slot = this.field_75151_b.get(index);
        if (slot != null && slot.func_75216_d()) {
            final ItemStack itemstack2 = slot.func_75211_c();
            itemstack = itemstack2.copy();
            if (!itemstack2.isEmpty() && itemstack2.getItem() instanceof ItemConstellationPaper && ((ItemConstellationPaper)itemstack2.getItem()).getConstellation(itemstack2) != null && index >= 0 && index < 36 && !this.func_75135_a(itemstack2, 36, 63, false)) {
                return ItemStack.field_190927_a;
            }
            if (index >= 0 && index < 27) {
                if (!this.func_75135_a(itemstack2, 27, 36, false)) {
                    return ItemStack.field_190927_a;
                }
            }
            else if (index >= 27 && index < 36) {
                if (!this.func_75135_a(itemstack2, 0, 27, false)) {
                    return ItemStack.field_190927_a;
                }
            }
            else if (!this.func_75135_a(itemstack2, 0, 36, false)) {
                return ItemStack.field_190927_a;
            }
            if (itemstack2.func_190916_E() == 0) {
                slot.func_75215_d(ItemStack.field_190927_a);
            }
            else {
                slot.func_75218_e();
            }
            if (itemstack2.func_190916_E() == itemstack.func_190916_E()) {
                return ItemStack.field_190927_a;
            }
            slot.func_190901_a(playerIn, itemstack2);
        }
        return itemstack;
    }
    
    public boolean func_75145_c(final Player playerIn) {
        return true;
    }
    
    public void slotChanged() {
        if (EffectiveSide.get().isServer()) {
            final LinkedList<IConstellation> saveConstellations = new LinkedList<IConstellation>();
            for (int i = 36; i < 63; ++i) {
                final ItemStack in = this.field_75151_b.get(i).func_75211_c();
                if (!in.isEmpty()) {
                    if (in.getItem() instanceof ConstellationBaseItem) {
                        final IConstellation c = ((ConstellationBaseItem)in.getItem()).getConstellation(in);
                        if (c != null) {
                            saveConstellations.add(c);
                        }
                    }
                }
            }
            ResearchManager.updateConstellationPapers(saveConstellations, this.owningPlayer);
        }
    }
}
