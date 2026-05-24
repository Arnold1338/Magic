package hellfirepvp.astralsorcery.common.network.channel;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.world.level.level.chunk.LevelChunk;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public abstract class SimpleSendChannel
{
    private final SimpleChannel channel;
    
    public SimpleSendChannel(final SimpleChannel channel) {
        this.channel = channel;
    }
    
    public <P extends ASPacket<P>> void sendToPlayer(final Player player, final P packet) {
        if (player instanceof ServerPlayer) {
            this.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), packet);
        }
    }
    
    public <P extends ASPacket<P>> void sendToAll(final P packet) {
        this.send(PacketDistributor.ALL.noArg(), packet);
    }
    
    public <P extends ASPacket<P>> void sendToAllObservingChunk(final P packet, final Chunk ch) {
        this.send(PacketDistributor.TRACKING_CHUNK.with(() -> ch), packet);
    }
    
    public <P extends ASPacket<P>> void sendToAllAround(final P packet, final PacketDistributor.TargetPoint point) {
        this.send(PacketDistributor.NEAR.with(() -> point), packet);
    }
    
    public <MSG> void sendToServer(final MSG message) {
        this.channel.sendToServer((Object)message);
    }
    
    public <MSG> void sendTo(final MSG message, final NetworkManager manager, final NetworkDirection direction) {
        this.channel.sendTo((Object)message, manager, direction);
    }
    
    public <MSG> void send(final PacketDistributor.PacketTarget target, final MSG message) {
        this.channel.send(target, (Object)message);
    }
    
    public <MSG> void reply(final MSG msgToReply, final NetworkEvent.Context context) {
        this.channel.reply((Object)msgToReply, context);
    }
    
    public <MSG> SimpleChannel.MessageBuilder<MSG> messageBuilder(final Class<MSG> type, final int id) {
        return (SimpleChannel.MessageBuilder<MSG>)this.channel.messageBuilder((Class)type, id);
    }
}
