package hellfirepvp.astralsorcery.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.world.level.inventory.MenuType;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import net.minecraft.world.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public abstract class ContainerAltarBase extends ContainerTileEntity<TileAltar>
{
    private final Inventory playerInv;
    private final TileInventory invHandler;
    
    protected ContainerAltarBase(final TileAltar altar, @Nullable final ContainerType<?> type, final Inventory inv, final int windowId) {
        super(altar, type, windowId);
        this.playerInv = inv;
        this.invHandler = altar.getInventory();
        this.bindPlayerInventory(this.playerInv);
        this.bindAltarInventory(this.invHandler);
    }
    
    abstract void bindPlayerInventory(final Inventory p0);
    
    abstract void bindAltarInventory(final TileInventory p0);
    
    abstract Optional<ItemStack> handleCustomTransfer(final Player p0, final int p1);
    
    public abstract int translateIndex(final int p0);
    
    public ItemStack func_82846_b(final Player playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.field_75151_b.get(index);
        if (slot != null && slot.func_75216_d()) {
            final ItemStack slotStack = slot.func_75211_c();
            itemstack = slotStack.copy();
            final Optional<ItemStack> stackOpt = this.handleCustomTransfer(playerIn, index);
            if (stackOpt.isPresent()) {
                return stackOpt.get();
            }
            if (index >= 0 && index < 27) {
                if (!this.func_75135_a(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 27 && index < 36) {
                if (!this.func_75135_a(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.func_75135_a(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.func_190916_E() == 0) {
                slot.func_75215_d(ItemStack.EMPTY);
            }
            else {
                slot.func_75218_e();
            }
            if (slotStack.func_190916_E() == itemstack.func_190916_E()) {
                return ItemStack.EMPTY;
            }
            slot.func_190901_a(playerIn, slotStack);
        }
        return itemstack;
    }
    
    public boolean func_75145_c(final Player player) {
        final BlockPos pos = this.getTileEntity().func_174877_v();
        return MiscUtils.getTileAt((IBlockReader)this.getTileEntity().func_145831_w(), pos, BlockEntity.class, false) == ((ContainerTileEntity<BlockEntity>)this).getTileEntity() && player.func_70092_e(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }
}
