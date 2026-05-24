package hellfirepvp.astralsorcery.common.container.factory;

import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraft.world.level.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;
import hellfirepvp.astralsorcery.common.container.ContainerObservatory;

public class ContainerObservatoryProvider extends CustomContainerProvider<ContainerObservatory>
{
    private final TileObservatory observatory;
    
    public ContainerObservatoryProvider(final TileObservatory observatory) {
        super(ContainerTypesAS.OBSERVATORY);
        this.observatory = observatory;
    }
    
    @Override
    protected void writeExtraData(final FriendlyByteBuf buf) {
        ByteBufUtils.writePos(buf, this.observatory.func_174877_v());
    }
    
    @Nonnull
    @Override
    public ContainerObservatory createMenu(final int windowId, final Inventory plInventory, final Player player) {
        return new ContainerObservatory(this.observatory, windowId);
    }
    
    private static ContainerObservatory createFromPacket(final int windowId, final Inventory plInventory, final FriendlyByteBuf data) {
        final BlockPos at = ByteBufUtils.readPos(data);
        final Player player = plInventory.field_70458_d;
        final TileObservatory observatory = MiscUtils.getTileAt((IBlockReader)player.func_130014_f_(), at, TileObservatory.class, true);
        return new ContainerObservatory(observatory, windowId);
    }
    
    public static class Factory implements IContainerFactory<ContainerObservatory>
    {
        public ContainerObservatory create(final int windowId, final Inventory inv, final FriendlyByteBuf data) {
            return createFromPacket(windowId, inv, data);
        }
    }
}
