package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.world.WorldSeedCache;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktRequestSeed extends ASPacket<PktRequestSeed>
{
    private RegistryKey<Level> dim;
    private Integer session;
    private Long seed;
    
    public PktRequestSeed() {
    }
    
    public PktRequestSeed(final Integer session, final RegistryKey<Level> dim) {
        this.dim = dim;
        this.session = session;
        this.seed = -1L;
    }
    
    private PktRequestSeed seed(final Long seed) {
        this.seed = seed;
        return this;
    }
    
    @Nonnull
    @Override
    public Encoder<PktRequestSeed> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeOptional(buffer, packet.dim, ByteBufUtils::writeVanillaRegistryEntry);
            ByteBufUtils.writeOptional(buffer, packet.session, FriendlyByteBuf::writeInt);
            ByteBufUtils.writeOptional(buffer, packet.seed, FriendlyByteBuf::writeLong);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktRequestSeed> decoder() {
        return (Decoder<PktRequestSeed>)(buffer -> {
            final PktRequestSeed pkt = new PktRequestSeed();
            pkt.dim = (RegistryKey<Level>)ByteBufUtils.readOptional(buffer, ByteBufUtils::readVanillaRegistryEntry);
            pkt.session = ByteBufUtils.readOptional(buffer, FriendlyByteBuf::readInt);
            pkt.seed = ByteBufUtils.readOptional(buffer, FriendlyByteBuf::readLong);
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktRequestSeed> handler() {
        return new Handler<PktRequestSeed>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktRequestSeed packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> WorldSeedCache.updateSeedCache(packet.dim, packet.session, packet.seed));
            }
            
            @Override
            public void handle(final PktRequestSeed packet, final NetworkEvent.Context context, final LogicalSide side) {
                context.enqueueWork(() -> {
                    final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
                    final ServerLevel w = srv.getLevel(packet.dim);
                    if (w != null) {
                        final PktRequestSeed seedResponse = new PktRequestSeed(packet.session, packet.dim);
                        seedResponse.seed(MiscUtils.getRandomWorldSeed((ISeedReader)w));
                        packet.replyWith(seedResponse, context);
                    }
                });
            }
        };
    }
}
