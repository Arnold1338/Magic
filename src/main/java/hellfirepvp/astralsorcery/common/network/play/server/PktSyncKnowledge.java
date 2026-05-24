package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchSyncHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.BiConsumer;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktSyncKnowledge extends ASPacket<PktSyncKnowledge>
{
    public static final byte STATE_ADD = 0;
    public static final byte STATE_WIPE = 1;
    private byte state;
    public List<ResourceLocation> knownConstellations;
    public List<ResourceLocation> seenConstellations;
    public List<ResourceLocation> storedConstellationPapers;
    public Collection<ResearchProgression> researchProgression;
    public IMajorConstellation attunedConstellation;
    public boolean wasOnceAttuned;
    public int progressTier;
    public boolean doPerkAbilities;
    public PlayerPerkData perkData;
    
    public PktSyncKnowledge() {
        this.knownConstellations = new ArrayList<ResourceLocation>();
        this.seenConstellations = new ArrayList<ResourceLocation>();
        this.storedConstellationPapers = new ArrayList<ResourceLocation>();
        this.researchProgression = new ArrayList<ResearchProgression>();
        this.attunedConstellation = null;
        this.wasOnceAttuned = false;
        this.progressTier = 0;
        this.doPerkAbilities = true;
        this.perkData = null;
    }
    
    public PktSyncKnowledge(final byte state) {
        this.knownConstellations = new ArrayList<ResourceLocation>();
        this.seenConstellations = new ArrayList<ResourceLocation>();
        this.storedConstellationPapers = new ArrayList<ResourceLocation>();
        this.researchProgression = new ArrayList<ResearchProgression>();
        this.attunedConstellation = null;
        this.wasOnceAttuned = false;
        this.progressTier = 0;
        this.doPerkAbilities = true;
        this.perkData = null;
        this.state = state;
    }
    
    public void load(final PlayerProgress progress) {
        this.knownConstellations = progress.getKnownConstellations();
        this.seenConstellations = progress.getSeenConstellations();
        this.storedConstellationPapers = progress.getStoredConstellationPapers();
        this.researchProgression = progress.getResearchProgression();
        this.progressTier = progress.getTierReached().ordinal();
        this.attunedConstellation = progress.getAttunedConstellation();
        this.perkData = progress.getPerkData();
        this.wasOnceAttuned = progress.wasOnceAttuned();
        this.doPerkAbilities = progress.doPerkAbilities();
    }
    
    @Nonnull
    @Override
    public Encoder<PktSyncKnowledge> encoder() {
        return (packet, buffer) -> {
            buffer.writeByte((int)packet.state);
            ByteBufUtils.writeOptional(buffer, packet.perkData, (buf, perkData) -> perkData.write(buf));
            ByteBufUtils.writeCollection(buffer, packet.knownConstellations, ByteBufUtils::writeResourceLocation);
            ByteBufUtils.writeCollection(buffer, packet.seenConstellations, ByteBufUtils::writeResourceLocation);
            ByteBufUtils.writeCollection(buffer, packet.storedConstellationPapers, ByteBufUtils::writeResourceLocation);
            ByteBufUtils.writeCollection(buffer, (Collection<Enum<ResearchProgression>>)packet.researchProgression, ByteBufUtils::writeEnumValue);
            ByteBufUtils.writeOptional(buffer, packet.attunedConstellation, (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeRegistryEntry);
            buffer.writeBoolean(packet.wasOnceAttuned);
            buffer.writeInt(packet.progressTier);
            buffer.writeBoolean(packet.doPerkAbilities);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktSyncKnowledge> decoder() {
        return (Decoder<PktSyncKnowledge>)(buffer -> {
            final PktSyncKnowledge pkt = new PktSyncKnowledge(buffer.readByte());
            pkt.perkData = ByteBufUtils.readOptional(buffer, buf -> PlayerPerkData.read(buf, LogicalSide.CLIENT));
            pkt.knownConstellations = ByteBufUtils.readList(buffer, ByteBufUtils::readResourceLocation);
            pkt.seenConstellations = ByteBufUtils.readList(buffer, ByteBufUtils::readResourceLocation);
            pkt.storedConstellationPapers = ByteBufUtils.readList(buffer, ByteBufUtils::readResourceLocation);
            pkt.researchProgression = ByteBufUtils.readList(buffer, buf -> ByteBufUtils.readEnumValue(buf, ResearchProgression.class));
            pkt.attunedConstellation = ByteBufUtils.readOptional(buffer, ByteBufUtils::readRegistryEntry);
            pkt.wasOnceAttuned = buffer.readBoolean();
            pkt.progressTier = buffer.readInt();
            pkt.doPerkAbilities = buffer.readBoolean();
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktSyncKnowledge> handler() {
        return new Handler<PktSyncKnowledge>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktSyncKnowledge packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
                    if (player != null) {
                        if (packet.state == 0) {
                            ResearchSyncHelper.recieveProgressFromServer(packet, player);
                        }
                        else {
                            ResearchHelper.updateClientResearch(null);
                        }
                    }
                });
            }
            
            @Override
            public void handle(final PktSyncKnowledge packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
