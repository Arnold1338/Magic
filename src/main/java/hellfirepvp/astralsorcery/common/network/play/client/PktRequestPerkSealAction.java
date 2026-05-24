package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.item.useables.ItemPerkSeal;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPerkTree;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktRequestPerkSealAction extends ASPacket<PktRequestPerkSealAction>
{
    private ResourceLocation perkKey;
    private boolean doSealing;
    
    public PktRequestPerkSealAction() {
    }
    
    public PktRequestPerkSealAction(final AbstractPerk perk, final boolean seal) {
        this.perkKey = perk.getRegistryName();
        this.doSealing = seal;
    }
    
    @Nonnull
    @Override
    public Encoder<PktRequestPerkSealAction> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeResourceLocation(buffer, packet.perkKey);
            buffer.writeBoolean(packet.doSealing);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktRequestPerkSealAction> decoder() {
        return (Decoder<PktRequestPerkSealAction>)(buffer -> {
            final PktRequestPerkSealAction pkt = new PktRequestPerkSealAction();
            pkt.perkKey = ByteBufUtils.readResourceLocation(buffer);
            pkt.doSealing = buffer.readBoolean();
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktRequestPerkSealAction> handler() {
        return new Handler<PktRequestPerkSealAction>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktRequestPerkSealAction packet, final NetworkEvent.Context context) {
                final Screen current = Minecraft.func_71410_x().field_71462_r;
                if (current instanceof ScreenJournalPerkTree) {
                    PerkTree.PERK_TREE.getPerk(LogicalSide.CLIENT, packet.perkKey).ifPresent(perk -> {
                        if (!packet.doSealing) {
                            Minecraft.func_71410_x().func_212871_a_(() -> ((ScreenJournalPerkTree)current).playSealBreakAnimation(perk));
                        }
                        else {
                            Minecraft.func_71410_x().func_212871_a_(() -> ((ScreenJournalPerkTree)current).playSealApplyAnimation(perk));
                        }
                    });
                }
            }
            
            @Override
            public void handle(final PktRequestPerkSealAction packet, final NetworkEvent.Context context, final LogicalSide side) {
                context.enqueueWork(() -> {
                    if (packet.perkKey != null) {
                        PerkTree.PERK_TREE.getPerk(side, packet.perkKey).ifPresent(perk -> {
                            final Player player = (Player)context.getSender();
                            if (packet.doSealing) {
                                if (ItemPerkSeal.useSeal(player, true) && ResearchManager.applyPerkSeal(player, perk)) {
                                    if (!ItemPerkSeal.useSeal(player, false)) {
                                        ResearchManager.breakPerkSeal(player, perk);
                                    }
                                    else {
                                        packet.replyWith(new PktRequestPerkSealAction(perk, true), context);
                                    }
                                }
                            }
                            else if (ResearchManager.breakPerkSeal(player, perk)) {
                                packet.replyWith(new PktRequestPerkSealAction(perk, false), context);
                            }
                        });
                    }
                });
            }
        };
    }
}
