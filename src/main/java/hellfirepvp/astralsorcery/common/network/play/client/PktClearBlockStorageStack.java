package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.item.base.ItemBlockStorage;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktClearBlockStorageStack extends ASPacket<PktClearBlockStorageStack>
{
    @Nonnull
    @Override
    public Encoder<PktClearBlockStorageStack> encoder() {
        return (packet, buffer) -> {};
    }
    
    @Nonnull
    @Override
    public Decoder<PktClearBlockStorageStack> decoder() {
        return (Decoder<PktClearBlockStorageStack>)(buffer -> new PktClearBlockStorageStack());
    }
    
    @Nonnull
    @Override
    public Handler<PktClearBlockStorageStack> handler() {
        return (packet, context, side) -> context.enqueueWork(() -> {
            if (side == LogicalSide.SERVER) {
                ItemBlockStorage.clearContainerFor((Player)context.getSender());
            }
        });
    }
}
