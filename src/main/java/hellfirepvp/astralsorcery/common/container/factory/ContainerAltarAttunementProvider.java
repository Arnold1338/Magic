package hellfirepvp.astralsorcery.common.container.factory;

import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraft.world.level.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.container.ContainerAltarAttunement;

public class ContainerAltarAttunementProvider extends CustomContainerProvider<ContainerAltarAttunement>
{
    private final TileAltar ta;
    
    public ContainerAltarAttunementProvider(final TileAltar ta) {
        super(ContainerTypesAS.ALTAR_ATTUNEMENT);
        this.ta = ta;
    }
    
    @Override
    protected void writeExtraData(final FriendlyByteBuf buf) {
        ByteBufUtils.writePos(buf, this.ta.getBlockState());
    }
    
    @Nonnull
    @Override
    public ContainerAltarAttunement createMenu(final int id, final Inventory plInventory, final Player player) {
        return new ContainerAltarAttunement(this.ta, plInventory, id);
    }
    
    private static ContainerAltarAttunement createFromPacket(final int id, final Inventory plInventory, final FriendlyByteBuf data) {
        final BlockPos at = ByteBufUtils.readPos(data);
        final Player player = plInventory.field_70458_d;
        final TileAltar ta = MiscUtils.getTileAt((IBlockReader)player.level(), at, TileAltar.class, true);
        return new ContainerAltarAttunement(ta, plInventory, id);
    }
    
    public static class Factory implements IContainerFactory<ContainerAltarAttunement>
    {
        public ContainerAltarAttunement create(final int windowId, final Inventory inv, final FriendlyByteBuf data) {
            return createFromPacket(windowId, inv, data);
        }
    }
}
