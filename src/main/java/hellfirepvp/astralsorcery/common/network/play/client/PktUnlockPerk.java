package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkAllocation;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.screens.Screen;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPerkTree;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktUnlockPerk extends ASPacket<PktUnlockPerk>
{
    private ResourceLocation perkKey;
    private boolean serverAccept;
    
    public PktUnlockPerk() {
        this.perkKey = null;
        this.serverAccept = false;
    }
    
    public PktUnlockPerk(final boolean serverAccepted, final AbstractPerk perk) {
        this.perkKey = null;
        this.serverAccept = false;
        this.serverAccept = serverAccepted;
        this.perkKey = perk.getRegistryName();
    }
    
    @Nonnull
    @Override
    public Encoder<PktUnlockPerk> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeOptional(buffer, packet.perkKey, ByteBufUtils::writeResourceLocation);
            buffer.writeBoolean(packet.serverAccept);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktUnlockPerk> decoder() {
        return (Decoder<PktUnlockPerk>)(buffer -> {
            final PktUnlockPerk pkt = new PktUnlockPerk();
            pkt.perkKey = ByteBufUtils.readOptional(buffer, ByteBufUtils::readResourceLocation);
            pkt.serverAccept = buffer.readBoolean();
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktUnlockPerk> handler() {
        return new Handler<PktUnlockPerk>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktUnlockPerk packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    if (packet.serverAccept) {
                        PerkTree.PERK_TREE.getPerk(LogicalSide.CLIENT, packet.perkKey).ifPresent(perk -> {
                            final Screen current = Minecraft.getInstance().field_71462_r;
                            if (current instanceof ScreenJournalPerkTree) {
                                Minecraft.getInstance().func_212871_a_(() -> ((ScreenJournalPerkTree)current).playUnlockAnimation(perk));
                            }
                        });
                    }
                });
            }
            
            @Override
            public void handle(final PktUnlockPerk packet, final NetworkEvent.Context context, final LogicalSide side) {
                context.enqueueWork(() -> PerkTree.PERK_TREE.getPerk(side, packet.perkKey).ifPresent(perk -> {
                    final Player player = (Player)context.getSender();
                    final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
                    if (prog.isValid()) {
                        final PlayerPerkData perkData = prog.getPerkData();
                        if (!perkData.hasPerkAllocation(perk) && perk.mayUnlockPerk(prog, player) && ResearchManager.applyPerk(player, perk, PlayerPerkAllocation.unlock())) {
                            packet.replyWith(new PktUnlockPerk(true, perk), context);
                        }
                    }
                }));
            }
        };
    }
}
