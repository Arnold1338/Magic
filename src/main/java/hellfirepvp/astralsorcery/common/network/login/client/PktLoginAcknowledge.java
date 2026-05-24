package hellfirepvp.astralsorcery.common.network.login.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;
import hellfirepvp.astralsorcery.common.network.base.ASLoginPacket;

public class PktLoginAcknowledge extends ASLoginPacket<PktLoginAcknowledge>
{
    @Nonnull
    @Override
    public Encoder<PktLoginAcknowledge> encoder() {
        return (pktLoginAcknowledge, buffer) -> {};
    }
    
    @Nonnull
    @Override
    public Decoder<PktLoginAcknowledge> decoder() {
        return (Decoder<PktLoginAcknowledge>)(buf -> new PktLoginAcknowledge());
    }
    
    @Nonnull
    @Override
    public Handler<PktLoginAcknowledge> handler() {
        return new Handler<PktLoginAcknowledge>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktLoginAcknowledge packet, final NetworkEvent.Context context) {
                ASLoginPacket.this.acknowledge(context);
            }
            
            @Override
            public void handle(final PktLoginAcknowledge packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
