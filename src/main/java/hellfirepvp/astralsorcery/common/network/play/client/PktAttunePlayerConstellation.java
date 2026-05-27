package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttunePlayerRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.active.ActivePlayerAttunementRecipe;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import javax.annotation.Nonnull;
import net.minecraftforge.registries.IForgeRegistryEntry;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktAttunePlayerConstellation extends ASPacket<PktAttunePlayerConstellation>
{
    private IMajorConstellation attunement;
    private ResourceKey<Level> world;
    private BlockPos at;
    
    public PktAttunePlayerConstellation() {
        this.attunement = null;
        this.world = null;
        this.at = BlockPos.field_177992_a;
    }
    
    public PktAttunePlayerConstellation(final IMajorConstellation attunement, final ResourceKey<Level> world, final BlockPos at) {
        this.attunement = null;
        this.world = null;
        this.at = BlockPos.field_177992_a;
        this.attunement = attunement;
        this.world = world;
        this.at = at;
    }
    
    @Nonnull
    @Override
    public Encoder<PktAttunePlayerConstellation> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeRegistryEntry(buffer, (net.minecraftforge.registries.IForgeRegistryEntry<Object>)packet.attunement);
            ByteBufUtils.writeVanillaRegistryEntry(buffer, packet.world);
            ByteBufUtils.writePos(buffer, packet.at);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktAttunePlayerConstellation> decoder() {
        return (Decoder<PktAttunePlayerConstellation>)(buffer -> {
            final PktAttunePlayerConstellation pkt = new PktAttunePlayerConstellation();
            pkt.attunement = ByteBufUtils.readRegistryEntry(buffer);
            pkt.world = ByteBufUtils.readVanillaRegistryEntry(buffer);
            pkt.at = ByteBufUtils.readPos(buffer);
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktAttunePlayerConstellation> handler() {
        return (packet, context, side) -> context.enqueueWork(() -> {
            final IMajorConstellation cst = packet.attunement;
            if (cst != null) {
                final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
                if (srv.forgeGetWorldMap().containsKey(packet.world)) {
                    final Level world = (Level)srv.getLevel((ResourceKey)packet.world);
                    final TileAttunementAltar ta = MiscUtils.getTileAt((IBlockReader)world, packet.at, TileAttunementAltar.class, false);
                    if (ta != null && ta.getActiveRecipe() instanceof ActivePlayerAttunementRecipe && context.getSender().getUUID().equals(((ActivePlayerAttunementRecipe)ta.getActiveRecipe()).getPlayerUUID()) && AttunePlayerRecipe.isEligablePlayer(context.getSender(), ta.getActiveConstellation())) {
                        ta.finishActiveRecipe();
                    }
                }
            }
        });
    }
}
