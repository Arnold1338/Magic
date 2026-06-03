package hellfirepvp.astralsorcery.common.container.factory;

import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.container.ContainerTome;

public class ContainerTomeProvider extends CustomContainerProvider<ContainerTome>
{
    private final ItemStack stackTome;
    private final int slotTome;
    
    public ContainerTomeProvider(final ItemStack stackTome, final int slotTome) {
        super(ContainerTypesAS.TOME);
        this.stackTome = stackTome;
        this.slotTome = slotTome;
    }
    
    @Override
    protected void writeExtraData(final FriendlyByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.stackTome);
        buf.writeInt(this.slotTome);
    }
    
    @Nonnull
    @Override
    public ContainerTome createMenu(final int id, final Inventory plInventory, final Player player) {
        return new ContainerTome(id, plInventory, player, this.stackTome, this.slotTome);
    }
    
    private static ContainerTome createFromPacket(final int id, final Inventory plInventory, final FriendlyByteBuf data) {
        final ItemStack tome = ByteBufUtils.readItemStack(data);
        final int slot = data.readInt();
        return new ContainerTome(id, plInventory, plInventory.field_70458_d, tome, slot);
    }
    
    public static class Factory implements IContainerFactory<ContainerTome>
    {
        public ContainerTome create(final int windowId, final Inventory inv, final FriendlyByteBuf data) {
            return createFromPacket(windowId, inv, data);
        }
    }
}
