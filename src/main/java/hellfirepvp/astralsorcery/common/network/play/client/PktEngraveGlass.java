package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileRefractionTable;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.awt.Point;
import javax.annotation.Nonnull;
import java.util.Collection;
import net.minecraftforge.registries.IForgeRegistryEntry;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.constellation.DrawnConstellation;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktEngraveGlass extends ASPacket<PktEngraveGlass>
{
    private RegistryKey<World> dim;
    private BlockPos pos;
    private List<DrawnConstellation> constellations;
    
    public PktEngraveGlass() {
        this.constellations = new LinkedList<DrawnConstellation>();
    }
    
    public PktEngraveGlass(final RegistryKey<World> dim, final BlockPos pos, final List<DrawnConstellation> constellations) {
        this.constellations = new LinkedList<DrawnConstellation>();
        this.dim = dim;
        this.pos = pos;
        this.constellations = constellations;
    }
    
    @Nonnull
    @Override
    public Encoder<PktEngraveGlass> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeVanillaRegistryEntry(buffer, packet.dim);
            ByteBufUtils.writePos(buffer, packet.pos);
            ByteBufUtils.writeCollection(buffer, packet.constellations, (buf, cst) -> {
                buf.writeInt(cst.getPoint().x);
                buf.writeInt(cst.getPoint().y);
                ByteBufUtils.writeRegistryEntry(buf, (net.minecraftforge.registries.IForgeRegistryEntry<Object>)cst.getConstellation());
            });
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktEngraveGlass> decoder() {
        return (Decoder<PktEngraveGlass>)(buffer -> {
            final PktEngraveGlass pkt = new PktEngraveGlass();
            pkt.dim = ByteBufUtils.readVanillaRegistryEntry(buffer);
            pkt.pos = ByteBufUtils.readPos(buffer);
            pkt.constellations = ByteBufUtils.readList(buffer, buf -> {
                new DrawnConstellation(new Point(buf.readInt(), buf.readInt()), ByteBufUtils.readRegistryEntry(buf));
                final DrawnConstellation drawnConstellation;
                return (PktEngraveGlass)drawnConstellation;
            });
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktEngraveGlass> handler() {
        return (packet, context, side) -> context.enqueueWork(() -> {
            final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
            final World world = (World)srv.func_71218_a((RegistryKey)packet.dim);
            final TileRefractionTable tmt = MiscUtils.getTileAt((IBlockReader)world, packet.pos, TileRefractionTable.class, false);
            if (tmt != null && !packet.constellations.isEmpty()) {
                final List<DrawnConstellation> cstList = packet.constellations.subList(0, Math.min(3, packet.constellations.size()));
                tmt.engraveGlass(cstList);
            }
        });
    }
}
