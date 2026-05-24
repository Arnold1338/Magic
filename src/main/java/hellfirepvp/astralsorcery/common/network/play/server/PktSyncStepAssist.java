package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktSyncStepAssist extends ASPacket<PktSyncStepAssist>
{
    private float stepHeight;
    
    public PktSyncStepAssist() {
    }
    
    public PktSyncStepAssist(final float stepHeight) {
        this.stepHeight = stepHeight - 0.4f;
    }
    
    @Nonnull
    @Override
    public Encoder<PktSyncStepAssist> encoder() {
        return (packet, buffer) -> buffer.writeFloat(packet.stepHeight);
    }
    
    @Nonnull
    @Override
    public Decoder<PktSyncStepAssist> decoder() {
        return (Decoder<PktSyncStepAssist>)(buffer -> {
            final PktSyncStepAssist pkt = new PktSyncStepAssist();
            pkt.stepHeight = buffer.readFloat();
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktSyncStepAssist> handler() {
        return new Handler<PktSyncStepAssist>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktSyncStepAssist packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final Player player = (Player)Minecraft.getInstance().field_71439_g;
                    if (player != null) {
                        player.field_70138_W = packet.stepHeight;
                    }
                });
            }
            
            @Override
            public void handle(final PktSyncStepAssist packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
