package hellfirepvp.astralsorcery.common.network.login.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractData;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataRegistry;
import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import hellfirepvp.astralsorcery.common.network.base.ASLoginPacket;

public class PktLoginSyncDataHolder extends ASLoginPacket<PktLoginSyncDataHolder>
{
    private Map<ResourceLocation, CompoundTag> syncData;
    
    public PktLoginSyncDataHolder() {
        this.syncData = new HashMap<ResourceLocation, CompoundTag>();
    }
    
    public static PktLoginSyncDataHolder makeLogin() {
        final PktLoginSyncDataHolder pkt = new PktLoginSyncDataHolder();
        for (final ResourceLocation key : SyncDataRegistry.getKnownKeys()) {
            SyncDataHolder.executeServer(key, AbstractData.class, data -> {
                final CompoundTag nbt = new CompoundTag();
                data.writeAllDataToPacket(nbt);
                pkt.syncData.put(key, nbt);
                return;
            });
        }
        return pkt;
    }
    
    @Nonnull
    @Override
    public Encoder<PktLoginSyncDataHolder> encoder() {
        return (packet, buffer) -> {
            buffer.writeInt(packet.syncData.size());
            packet.syncData.keySet().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final ResourceLocation key = iterator.next();
                ByteBufUtils.writeResourceLocation(buffer, key);
                ByteBufUtils.writeNBTTag(buffer, packet.syncData.get(key));
            }
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktLoginSyncDataHolder> decoder() {
        return (Decoder<PktLoginSyncDataHolder>)(buffer -> {
            final PktLoginSyncDataHolder pktData = new PktLoginSyncDataHolder();
            for (int size = buffer.readInt(), i = 0; i < size; ++i) {
                final ResourceLocation key = ByteBufUtils.readResourceLocation(buffer);
                final CompoundTag tag = ByteBufUtils.readNBTTag(buffer);
                pktData.syncData.put(key, tag);
            }
            return pktData;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktLoginSyncDataHolder> handler() {
        return new Handler<PktLoginSyncDataHolder>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktLoginSyncDataHolder packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    packet.syncData.keySet().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final ResourceLocation key = iterator.next();
                        final ClientDataReader reader = SyncDataHolder.getReader(key);
                        if (reader != null) {
                            SyncDataHolder.executeClient(key, ClientData.class, data -> reader.readFromIncomingFullSync(data, packet.syncData.get(key)));
                        }
                    }
                    ASLoginPacket.this.acknowledge(context);
                });
            }
            
            @Override
            public void handle(final PktLoginSyncDataHolder packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
