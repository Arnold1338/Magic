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
import hellfirepvp.astralsorcery.common.container.ContainerAltarTrait;

public class ContainerAltarRadianceProvider extends CustomContainerProvider<ContainerAltarTrait>
{
    private final TileAltar ta;
    
    public ContainerAltarRadianceProvider(final TileAltar ta) {
        super(ContainerTypesAS.ALTAR_RADIANCE);
        this.ta = ta;
    }
    
    @Override
    protected void writeExtraData(final FriendlyByteBuf buf) {
        ByteBufUtils.writePos(buf, this.ta.func_174877_v());
    }
    
    @Nonnull
    @Override
    public ContainerAltarTrait createMenu(final int id, final Inventory plInventory, final Player player) {
        return new ContainerAltarTrait(this.ta, plInventory, id);
    }
    
    private static ContainerAltarTrait createFromPacket(final int id, final Inventory plInventory, final FriendlyByteBuf data) {
        final BlockPos at = ByteBufUtils.readPos(data);
        final Player player = plInventory.field_70458_d;
        final TileAltar ta = MiscUtils.getTileAt((IBlockReader)player.func_130014_f_(), at, TileAltar.class, true);
        return new ContainerAltarTrait(ta, plInventory, id);
    }
    
    public static class Factory implements IContainerFactory<ContainerAltarTrait>
    {
        public ContainerAltarTrait create(final int windowId, final Inventory inv, final FriendlyByteBuf data) {
            return createFromPacket(windowId, inv, data);
        }
    }
}
