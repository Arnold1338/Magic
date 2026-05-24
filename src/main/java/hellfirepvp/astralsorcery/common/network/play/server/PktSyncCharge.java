package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktSyncCharge extends ASPacket<PktSyncCharge>
{
    private float maxCharge;
    private float charge;
    
    public PktSyncCharge() {
        this.maxCharge = 0.0f;
        this.charge = 0.0f;
    }
    
    private PktSyncCharge(final float maxCharge, final float charge) {
        this.maxCharge = 0.0f;
        this.charge = 0.0f;
        this.maxCharge = maxCharge;
        this.charge = charge;
    }
    
    public PktSyncCharge(final Player player) {
        this.maxCharge = 0.0f;
        this.charge = 0.0f;
        this.maxCharge = AlignmentChargeHandler.INSTANCE.getMaximumCharge(player, LogicalSide.SERVER);
        this.charge = AlignmentChargeHandler.INSTANCE.getCurrentCharge(player, LogicalSide.SERVER);
    }
    
    public float getMaxCharge() {
        return this.maxCharge;
    }
    
    public float getCharge() {
        return this.charge;
    }
    
    @Nonnull
    @Override
    public Encoder<PktSyncCharge> encoder() {
        return (packet, buffer) -> {
            buffer.writeFloat(packet.maxCharge);
            buffer.writeFloat(packet.charge);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktSyncCharge> decoder() {
        return (Decoder<PktSyncCharge>)(buffer -> new PktSyncCharge(buffer.readFloat(), buffer.readFloat()));
    }
    
    @Nonnull
    @Override
    public Handler<PktSyncCharge> handler() {
        return new Handler<PktSyncCharge>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktSyncCharge packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> AlignmentChargeHandler.INSTANCE.receiveCharge(packet, (Player)Minecraft.func_71410_x().field_71439_g));
            }
            
            @Override
            public void handle(final PktSyncCharge packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
