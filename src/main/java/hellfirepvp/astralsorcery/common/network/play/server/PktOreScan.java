package hellfirepvp.astralsorcery.common.network.play.server;

import java.util.Random;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.client.util.MiscPlayEffect;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import java.util.List;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktOreScan extends ASPacket<PktOreScan>
{
    private List<BlockPos> positions;
    
    public PktOreScan() {
        this.positions = new ArrayList<BlockPos>();
    }
    
    public PktOreScan(final List<BlockPos> positions) {
        this.positions = new ArrayList<BlockPos>();
        this.positions = positions;
    }
    
    @Nonnull
    @Override
    public Encoder<PktOreScan> encoder() {
        return (packet, buffer) -> ByteBufUtils.writeCollection(buffer, packet.positions, ByteBufUtils::writePos);
    }
    
    @Nonnull
    @Override
    public Decoder<PktOreScan> decoder() {
        return (Decoder<PktOreScan>)(buffer -> new PktOreScan(ByteBufUtils.readList(buffer, ByteBufUtils::readPos)));
    }
    
    @Nonnull
    @Override
    public Handler<PktOreScan> handler() {
        return new Handler<PktOreScan>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktOreScan packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
                    if (player != null) {
                        packet.positions.iterator();
                        final Iterator iterator;
                        while (iterator.hasNext()) {
                            final BlockPos at = iterator.next();
                            final Vector3 atPos = new Vector3((Vector3i)at).add(0.5, 0.5, 0.5);
                            atPos.add(PktOreScan.rand.nextFloat() - PktOreScan.rand.nextFloat(), PktOreScan.rand.nextFloat() - PktOreScan.rand.nextFloat(), PktOreScan.rand.nextFloat() - PktOreScan.rand.nextFloat());
                            final BlockState state = Minecraft.func_71410_x().field_71441_e.getBlockState(at);
                            MiscPlayEffect.playSingleBlockTumbleDepthEffect(atPos, state);
                        }
                    }
                });
            }
            
            @Override
            public void handle(final PktOreScan packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
