package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.auxiliary.gateway.CelestialGatewayHandler;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import java.util.Collection;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktUpdateGateways extends ASPacket<PktUpdateGateways>
{
    private Map<RegistryKey<Level>, Collection<GatewayCache.GatewayNode>> positions;
    
    public PktUpdateGateways() {
        this.positions = new HashMap<RegistryKey<Level>, Collection<GatewayCache.GatewayNode>>();
    }
    
    public PktUpdateGateways(final Map<RegistryKey<Level>, Collection<GatewayCache.GatewayNode>> positions) {
        this.positions = new HashMap<RegistryKey<Level>, Collection<GatewayCache.GatewayNode>>();
        this.positions = positions;
    }
    
    @Nonnull
    @Override
    public Encoder<PktUpdateGateways> encoder() {
        return (packet, buffer) -> {
            buffer.writeInt(packet.positions.size());
            packet.positions.keySet().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final RegistryKey dim = iterator.next();
                ByteBufUtils.writeVanillaRegistryEntry(buffer, (RegistryKey<?>)dim);
                ByteBufUtils.writeCollection(buffer, packet.positions.get(dim), (buf, node) -> node.write(buf));
            }
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktUpdateGateways> decoder() {
        return (Decoder<PktUpdateGateways>)(buffer -> {
            final PktUpdateGateways pkt = new PktUpdateGateways();
            for (int dimSize = buffer.readInt(), i = 0; i < dimSize; ++i) {
                final net.minecraft.util.RegistryKey<Level> dim = ByteBufUtils.readVanillaRegistryEntry(buffer);
                pkt.positions.put(dim, ByteBufUtils.readList(buffer, GatewayCache.GatewayNode::read));
            }
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktUpdateGateways> handler() {
        return new Handler<PktUpdateGateways>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktUpdateGateways packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> CelestialGatewayHandler.INSTANCE.updateClientCache(packet.positions));
            }
            
            @Override
            public void handle(final PktUpdateGateways packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
