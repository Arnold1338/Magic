package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktSyncData extends ASPacket<PktSyncData>
{
    private Map<ResourceLocation, CompoundTag> diffData;
    
    public PktSyncData() {
        this.diffData = new HashMap<ResourceLocation, CompoundTag>();
    }
    
    public PktSyncData(final Map<ResourceLocation, CompoundTag> dataToSend) {
        this.diffData = new HashMap<ResourceLocation, CompoundTag>();
        this.diffData = dataToSend;
    }
    
    @Nonnull
    @Override
    public Encoder<PktSyncData> encoder() {
        return (packet, buffer) -> {
            buffer.writeInt(packet.diffData.size());
            packet.diffData.keySet().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final ResourceLocation key = iterator.next();
                ByteBufUtils.writeResourceLocation(buffer, key);
                ByteBufUtils.writeNBTTag(buffer, packet.diffData.get(key));
            }
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktSyncData> decoder() {
        return (Decoder<PktSyncData>)(buffer -> {
            final PktSyncData pktData = new PktSyncData();
            for (int size = buffer.readInt(), i = 0; i < size; ++i) {
                final ResourceLocation key = ByteBufUtils.readResourceLocation(buffer);
                final CompoundTag tag = ByteBufUtils.readNBTTag(buffer);
                pktData.diffData.put(key, tag);
            }
            return pktData;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktSyncData> handler() {
        return new Handler<PktSyncData>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktSyncData packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    packet.diffData.keySet().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final ResourceLocation key = iterator.next();
                        final ClientDataReader reader = SyncDataHolder.getReader(key);
                        if (reader != null) {
                            SyncDataHolder.executeClient(key, ClientData.class, data -> reader.readFromIncomingDiff(data, packet.diffData.get(key)));
                        }
                    }
                });
            }
            
            @Override
            public void handle(final PktSyncData packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
