package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktOpenGui extends ASPacket<PktOpenGui>
{
    private GuiType type;
    private CompoundTag data;
    
    public PktOpenGui() {
    }
    
    public PktOpenGui(final GuiType type, final CompoundTag data) {
        this.type = type;
        this.data = data;
    }
    
    @Nonnull
    @Override
    public Encoder<PktOpenGui> encoder() {
        return (pkt, buffer) -> {
            ByteBufUtils.writeEnumValue(buffer, pkt.type);
            ByteBufUtils.writeNBTTag(buffer, pkt.data);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktOpenGui> decoder() {
        return (Decoder<PktOpenGui>)(buffer -> new PktOpenGui(ByteBufUtils.readEnumValue(buffer, GuiType.class), ByteBufUtils.readNBTTag(buffer)));
    }
    
    @Nonnull
    @Override
    public Handler<PktOpenGui> handler() {
        return new Handler<PktOpenGui>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktOpenGui packet, final NetworkEvent.Context context) {
                if (Minecraft.func_71410_x().field_71439_g != null) {
                    context.enqueueWork(() -> AstralSorcery.getProxy().openGuiClient(packet.type, packet.data));
                }
            }
            
            @Override
            public void handle(final PktOpenGui packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
