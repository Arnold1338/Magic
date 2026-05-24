package hellfirepvp.astralsorcery.common.data.research;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.event.PerkExperienceRenderer;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncKnowledge;
import net.minecraft.world.entity.player.Player;

public class ResearchSyncHelper
{
    public static void pushProgressToClientUnsafe(final PlayerProgress progress, final Player p) {
        final PktSyncKnowledge pkt = new PktSyncKnowledge((byte)0);
        pkt.load(progress);
        PacketChannel.CHANNEL.sendToPlayer(p, pkt);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void recieveProgressFromServer(final PktSyncKnowledge packet, final Player player) {
        final PlayerPerkData perkData = ResearchHelper.getClientProgress().getPerkData();
        final int currentLvl = perkData.getPerkLevel(player, LogicalSide.CLIENT);
        ResearchHelper.updateClientResearch(packet);
        if (perkData.getPerkLevel(player, LogicalSide.CLIENT) > currentLvl) {
            PerkExperienceRenderer.INSTANCE.revealExperience(160);
        }
    }
}
