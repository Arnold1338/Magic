package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Optional;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.common.perk.PerkEffectHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktSyncPerkActivity extends ASPacket<PktSyncPerkActivity>
{
    private Type type;
    private ResourceLocation perkKey;
    private CompoundTag newData;
    private CompoundTag oldData;
    private List<ResourceLocation> perkKeys;
    
    public PktSyncPerkActivity() {
        this.type = null;
        this.perkKey = null;
        this.newData = null;
        this.oldData = null;
        this.perkKeys = new ArrayList<ResourceLocation>();
    }
    
    public PktSyncPerkActivity(final Type type) {
        this.type = null;
        this.perkKey = null;
        this.newData = null;
        this.oldData = null;
        this.perkKeys = new ArrayList<ResourceLocation>();
        if (type == Type.DATACHANGE) {
            throw new IllegalArgumentException("Passed Datachange-Type without supplying data!");
        }
        this.type = type;
    }
    
    public PktSyncPerkActivity(final AbstractPerk perk, final CompoundTag oldData, final CompoundTag newData) {
        this.type = null;
        this.perkKey = null;
        this.newData = null;
        this.oldData = null;
        this.perkKeys = new ArrayList<ResourceLocation>();
        this.type = Type.DATACHANGE;
        this.perkKey = perk.getRegistryName();
        this.oldData = oldData;
        this.newData = newData;
    }
    
    public PktSyncPerkActivity(final Collection<ResourceLocation> removals) {
        this.type = null;
        this.perkKey = null;
        this.newData = null;
        this.oldData = null;
        this.perkKeys = new ArrayList<ResourceLocation>();
        this.type = Type.REMOVE_LISTED;
        this.perkKeys = new ArrayList<ResourceLocation>(removals);
    }
    
    @Nonnull
    @Override
    public Encoder<PktSyncPerkActivity> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeOptional(buffer, packet.type, ByteBufUtils::writeEnumValue);
            ByteBufUtils.writeOptional(buffer, packet.perkKey, ByteBufUtils::writeResourceLocation);
            ByteBufUtils.writeOptional(buffer, packet.newData, ByteBufUtils::writeNBTTag);
            ByteBufUtils.writeOptional(buffer, packet.oldData, ByteBufUtils::writeNBTTag);
            ByteBufUtils.writeCollection(buffer, packet.perkKeys, ByteBufUtils::writeResourceLocation);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktSyncPerkActivity> decoder() {
        return (Decoder<PktSyncPerkActivity>)(buffer -> {
            final PktSyncPerkActivity pkt = new PktSyncPerkActivity();
            pkt.type = ByteBufUtils.readOptional(buffer, byteBuf -> ByteBufUtils.readEnumValue(byteBuf, Type.class));
            pkt.perkKey = ByteBufUtils.readOptional(buffer, ByteBufUtils::readResourceLocation);
            pkt.newData = ByteBufUtils.readOptional(buffer, ByteBufUtils::readNBTTag);
            pkt.oldData = ByteBufUtils.readOptional(buffer, ByteBufUtils::readNBTTag);
            pkt.perkKeys = ByteBufUtils.readList(buffer, ByteBufUtils::readResourceLocation);
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktSyncPerkActivity> handler() {
        return new Handler<PktSyncPerkActivity>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktSyncPerkActivity packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final Player player = (Player)Minecraft.getInstance().field_71439_g;
                    if (player != null) {
                        switch (packet.type) {
                            case CLEARALL: {
                                PerkEffectHelper.clientClearAllPerks();
                                break;
                            }
                            case REMOVE_LISTED: {
                                final List perks = (List)packet.perkKeys.stream().map(key -> PerkTree.PERK_TREE.getPerk(LogicalSide.CLIENT, key)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
                                PerkEffectHelper.modifySources(player, LogicalSide.CLIENT, (Collection<AbstractPerk>)perks, PerkEffectHelper.Action.REMOVE);
                                break;
                            }
                            case UNLOCKALL: {
                                PerkEffectHelper.clientRefreshAllPerks();
                                break;
                            }
                            case DATACHANGE: {
                                PerkTree.PERK_TREE.getPerk(LogicalSide.CLIENT, packet.perkKey).ifPresent(perk -> PerkEffectHelper.clientChangePerkData(perk, packet.oldData, packet.newData));
                                break;
                            }
                        }
                    }
                });
            }
            
            @Override
            public void handle(final PktSyncPerkActivity packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
    
    public enum Type
    {
        CLEARALL, 
        REMOVE_LISTED, 
        UNLOCKALL, 
        DATACHANGE;
    }
}
