package hellfirepvp.astralsorcery.common.network.login.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.perk.data.PerkTreeData;
import hellfirepvp.astralsorcery.common.perk.data.PerkTreeLoader;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.PerkLevelManager;
import net.minecraftforge.fml.LogicalSide;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import java.util.List;
import hellfirepvp.astralsorcery.common.network.base.ASLoginPacket;

public class PktLoginSyncPerkInformation extends ASLoginPacket<PktLoginSyncPerkInformation>
{
    private List<JsonObject> rawPerkTreeData;
    private int maxLevel;
    
    public PktLoginSyncPerkInformation() {
        this.rawPerkTreeData = new ArrayList<JsonObject>();
        this.maxLevel = 0;
    }
    
    public static PktLoginSyncPerkInformation makeLogin() {
        final PktLoginSyncPerkInformation pkt = new PktLoginSyncPerkInformation();
        PerkTree.PERK_TREE.getLoginPerkData().ifPresent(treeData -> pkt.rawPerkTreeData.addAll(treeData));
        pkt.maxLevel = PerkLevelManager.getLevelCap(LogicalSide.SERVER, null);
        return pkt;
    }
    
    @Nonnull
    @Override
    public Encoder<PktLoginSyncPerkInformation> encoder() {
        return (pkt, buf) -> {
            ByteBufUtils.writeCollection(buf, pkt.rawPerkTreeData, ByteBufUtils::writeJsonObject);
            buf.writeInt(pkt.maxLevel);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktLoginSyncPerkInformation> decoder() {
        return (Decoder<PktLoginSyncPerkInformation>)(buf -> {
            final PktLoginSyncPerkInformation pkt = new PktLoginSyncPerkInformation();
            pkt.rawPerkTreeData = ByteBufUtils.readList(buf, ByteBufUtils::readJsonObject);
            pkt.maxLevel = buf.readInt();
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktLoginSyncPerkInformation> handler() {
        return new Handler<PktLoginSyncPerkInformation>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktLoginSyncPerkInformation packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final PerkTreeData treeData = PerkTreeLoader.loadPerkTree(packet.rawPerkTreeData);
                    PerkTree.PERK_TREE.receivePerkTree(treeData.prepare());
                    PerkLevelManager.receiveLevelCap(packet.maxLevel);
                    ASLoginPacket.this.acknowledge(context);
                });
            }
            
            @Override
            public void handle(final PktLoginSyncPerkInformation packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
