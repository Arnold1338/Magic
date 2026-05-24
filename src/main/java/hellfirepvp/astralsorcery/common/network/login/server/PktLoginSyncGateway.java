package hellfirepvp.astralsorcery.common.network.login.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.gateway.CelestialGatewayHandler;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import java.util.Collection;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.network.base.ASLoginPacket;

public class PktLoginSyncGateway extends ASLoginPacket<PktLoginSyncGateway>
{
    private Map<RegistryKey<World>, Collection<GatewayCache.GatewayNode>> positions;
    
    public PktLoginSyncGateway() {
        this.positions = new HashMap<RegistryKey<World>, Collection<GatewayCache.GatewayNode>>();
    }
    
    public static PktLoginSyncGateway makeLogin() {
        final PktLoginSyncGateway pkt = new PktLoginSyncGateway();
        pkt.positions = CelestialGatewayHandler.INSTANCE.getGatewayCache(LogicalSide.SERVER);
        return pkt;
    }
    
    @Nonnull
    @Override
    public Encoder<PktLoginSyncGateway> encoder() {
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
    public Decoder<PktLoginSyncGateway> decoder() {
        return (Decoder<PktLoginSyncGateway>)(buffer -> {
            final PktLoginSyncGateway pkt = new PktLoginSyncGateway();
            for (int dimSize = buffer.readInt(), i = 0; i < dimSize; ++i) {
                final net.minecraft.util.RegistryKey<World> dim = ByteBufUtils.readVanillaRegistryEntry(buffer);
                pkt.positions.put(dim, ByteBufUtils.readList(buffer, GatewayCache.GatewayNode::read));
            }
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktLoginSyncGateway> handler() {
        return new Handler<PktLoginSyncGateway>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktLoginSyncGateway packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    CelestialGatewayHandler.INSTANCE.updateClientCache(packet.positions);
                    ASLoginPacket.this.acknowledge(context);
                });
            }
            
            @Override
            public void handle(final PktLoginSyncGateway packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
