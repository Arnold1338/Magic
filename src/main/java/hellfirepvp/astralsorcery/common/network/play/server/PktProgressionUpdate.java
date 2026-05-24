package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalProgression;
import net.minecraft.client.gui.screens.Screen;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPerkTree;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournal;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.player.ClientPlayerEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktProgressionUpdate extends ASPacket<PktProgressionUpdate>
{
    public ProgressionTier tier;
    public ResearchProgression prog;
    
    public PktProgressionUpdate() {
        this.tier = null;
        this.prog = null;
    }
    
    public PktProgressionUpdate(final ResearchProgression prog) {
        this.tier = null;
        this.prog = null;
        this.prog = prog;
    }
    
    public PktProgressionUpdate(final ProgressionTier tier) {
        this.tier = null;
        this.prog = null;
        this.tier = tier;
    }
    
    private PktProgressionUpdate(final ProgressionTier tier, final ResearchProgression prog) {
        this.tier = null;
        this.prog = null;
        this.tier = tier;
        this.prog = prog;
    }
    
    @Nonnull
    @Override
    public Encoder<PktProgressionUpdate> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeOptional(buffer, packet.tier, ByteBufUtils::writeEnumValue);
            ByteBufUtils.writeOptional(buffer, packet.prog, ByteBufUtils::writeEnumValue);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktProgressionUpdate> decoder() {
        return (Decoder<PktProgressionUpdate>)(buffer -> new PktProgressionUpdate(ByteBufUtils.readOptional(buffer, buf -> ByteBufUtils.readEnumValue(buf, ProgressionTier.class)), ByteBufUtils.readOptional(buffer, buf -> ByteBufUtils.readEnumValue(buf, ResearchProgression.class))));
    }
    
    @Nonnull
    @Override
    public Handler<PktProgressionUpdate> handler() {
        return new Handler<PktProgressionUpdate>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktProgressionUpdate packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    if (packet.tier != null) {
                        Minecraft.func_71410_x().field_71439_g.func_145747_a((Component)new Component("astralsorcery.progress.gain.progress.chat").func_240699_a_(ChatFormatting.BLUE), Util.NIL_UUID);
                    }
                    if (packet.prog != null) {
                        final ClientPlayerEntity field_71439_g = Minecraft.func_71410_x().field_71439_g;
                        new Component("astralsorcery.progress.gain.research.chat", new Object[] { packet.prog.getName() });
                        final Component translationTextComponent;
                        field_71439_g.func_145747_a((Component)translationTextComponent.func_240699_a_(ChatFormatting.AQUA), Util.NIL_UUID);
                    }
                    packet.refreshJournal();
                });
            }
            
            @Override
            public void handle(final PktProgressionUpdate packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
    
    @OnlyIn(Dist.CLIENT)
    private void refreshJournal() {
        final Screen open = Minecraft.func_71410_x().field_71462_r;
        if (open != null && open instanceof ScreenJournal && !(open instanceof ScreenJournalPerkTree)) {
            Minecraft.func_71410_x().func_147108_a((Screen)null);
        }
        ScreenJournalProgression.resetJournal();
    }
}
