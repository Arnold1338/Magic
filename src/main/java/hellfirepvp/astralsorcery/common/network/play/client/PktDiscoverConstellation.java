package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import net.minecraftforge.registries.IForgeRegistryEntry;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktDiscoverConstellation extends ASPacket<PktDiscoverConstellation>
{
    private IConstellation constellation;
    
    public PktDiscoverConstellation() {
    }
    
    public PktDiscoverConstellation(final IConstellation constellation) {
        this.constellation = constellation;
    }
    
    @Nonnull
    @Override
    public Encoder<PktDiscoverConstellation> encoder() {
        return (packet, buffer) -> ByteBufUtils.writeRegistryEntry(buffer, (net.minecraftforge.registries.IForgeRegistryEntry<Object>)packet.constellation);
    }
    
    @Nonnull
    @Override
    public Decoder<PktDiscoverConstellation> decoder() {
        return (Decoder<PktDiscoverConstellation>)(buffer -> new PktDiscoverConstellation(ByteBufUtils.readRegistryEntry(buffer)));
    }
    
    @Nonnull
    @Override
    public Handler<PktDiscoverConstellation> handler() {
        return (packet, context, side) -> context.enqueueWork(() -> {
            if (side == LogicalSide.SERVER) {
                final Player player = (Player)context.getSender();
                final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
                if (prog.isValid() && packet.constellation.canDiscover(player, prog) && ResearchManager.discoverConstellation(packet.constellation, player)) {
                    ResearchHelper.sendConstellationDiscoveryMessage((CommandSource)player, packet.constellation);
                }
            }
        });
    }
}
