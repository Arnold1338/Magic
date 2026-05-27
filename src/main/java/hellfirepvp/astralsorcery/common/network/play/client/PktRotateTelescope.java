package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.screen.ScreenTelescope;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import java.util.Optional;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktRotateTelescope extends ASPacket<PktRotateTelescope>
{
    private boolean isClockwise;
    private ResourceKey<Level> dim;
    private BlockPos pos;
    
    public PktRotateTelescope() {
        this.isClockwise = false;
        this.dim = null;
        this.pos = BlockPos.field_177992_a;
    }
    
    public PktRotateTelescope(final boolean isClockwise, final ResourceKey<Level> dim, final BlockPos pos) {
        this.isClockwise = false;
        this.dim = null;
        this.pos = BlockPos.field_177992_a;
        this.isClockwise = isClockwise;
        this.dim = dim;
        this.pos = pos;
    }
    
    @Nonnull
    @Override
    public Encoder<PktRotateTelescope> encoder() {
        return (packet, buffer) -> {
            buffer.writeBoolean(packet.isClockwise);
            ByteBufUtils.writeOptional(buffer, packet.dim, ByteBufUtils::writeVanillaRegistryEntry);
            ByteBufUtils.writePos(buffer, packet.pos);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktRotateTelescope> decoder() {
        return (Decoder<PktRotateTelescope>)(buffer -> {
            final PktRotateTelescope pkt = new PktRotateTelescope();
            pkt.isClockwise = buffer.readBoolean();
            pkt.dim = (ResourceKey<Level>)ByteBufUtils.readOptional(buffer, ByteBufUtils::readVanillaRegistryEntry);
            pkt.pos = ByteBufUtils.readPos(buffer);
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktRotateTelescope> handler() {
        return new Handler<PktRotateTelescope>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktRotateTelescope packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final Optional clWorld = (Optional)Optional.ofNullable(Minecraft.getInstance().level);
                    clWorld.ifPresent(world -> {
                        final TileTelescope tt = MiscUtils.getTileAt((IBlockReader)world, packet.pos, TileTelescope.class, (boolean)(0 != 0));
                        if (tt != null) {
                            tt.setRotation(packet.isClockwise ? tt.getRotation().nextClockWise() : tt.getRotation().nextCounterClockWise());
                        }
                        return;
                    });
                    if (Minecraft.getInstance().gui instanceof ScreenTelescope) {
                        ((ScreenTelescope)Minecraft.getInstance().gui).handleRotationChange(packet.isClockwise);
                    }
                });
            }
            
            @Override
            public void handle(final PktRotateTelescope packet, final NetworkEvent.Context context, final LogicalSide side) {
                context.enqueueWork(() -> {
                    final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
                    final Level world = (Level)srv.getLevel(packet.dim);
                    final TileTelescope tt = MiscUtils.getTileAt((IBlockReader)world, packet.pos, TileTelescope.class, false);
                    if (tt != null) {
                        tt.setRotation(packet.isClockwise ? tt.getRotation().nextClockWise() : tt.getRotation().nextCounterClockWise());
                        packet.replyWith(new PktRotateTelescope(packet.isClockwise, packet.dim, packet.pos), context);
                    }
                });
            }
        };
    }
}
