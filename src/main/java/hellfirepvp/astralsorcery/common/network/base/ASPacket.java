package hellfirepvp.astralsorcery.common.network.base;

import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Function;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import java.util.function.Supplier;
import java.util.function.BiConsumer;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import java.util.Random;

public abstract class ASPacket<T extends ASPacket<T>>
{
    protected static Random rand;
    
    @Nonnull
    public abstract Encoder<T> encoder();
    
    @Nonnull
    public abstract Decoder<T> decoder();
    
    @Nonnull
    public abstract Handler<T> handler();
    
    protected final void replyWith(final T packet, final NetworkEvent.Context ctx) {
        PacketChannel.CHANNEL.reply(packet, ctx);
    }
    
    static {
        ASPacket.rand = new Random();
    }
    
    public interface Handler<T extends ASPacket<T>> extends BiConsumer<T, Supplier<NetworkEvent.Context>>
    {
        default void accept(final T t, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context ctx = contextSupplier.get();
            switch (ctx.getDirection().getReceptionSide()) {
                case CLIENT: {
                    this.handleClient(t, ctx);

                }
                case SERVER: {
                    this.handleServer(t, ctx);

                }
            }
            ctx.setPacketHandled(true);
        }
        
        @OnlyIn(Dist.CLIENT)
        default void handleClient(final T packet, final NetworkEvent.Context context) {
            this.handle(packet, context, LogicalSide.CLIENT);
        }
        
        default void handleServer(final T packet, final NetworkEvent.Context context) {
            this.handle(packet, context, LogicalSide.SERVER);
        }
        
        void handle(final T p0, final NetworkEvent.Context p1, final LogicalSide p2);
    }
    
    public interface Decoder<T extends ASPacket<T>> extends Function<FriendlyByteBuf, T>
    {
    }
    
    public interface Encoder<T extends ASPacket<T>> extends BiConsumer<T, FriendlyByteBuf>
    {
    }
}
