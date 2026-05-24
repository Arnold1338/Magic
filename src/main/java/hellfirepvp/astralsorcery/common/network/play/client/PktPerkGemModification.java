package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.perk.node.socket.GemSocketItem;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.node.socket.GemSocketPerk;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktPerkGemModification extends ASPacket<PktPerkGemModification>
{
    private int action;
    private ResourceLocation perkKey;
    private int slotId;
    
    public PktPerkGemModification() {
        this.action = 0;
        this.perkKey = null;
        this.slotId = -1;
    }
    
    public static PktPerkGemModification insertItem(final AbstractPerk perk, final int slotId) {
        final PktPerkGemModification pkt = new PktPerkGemModification();
        pkt.action = 0;
        pkt.perkKey = perk.getRegistryName();
        pkt.slotId = slotId;
        return pkt;
    }
    
    public static PktPerkGemModification dropItem(final AbstractPerk perk) {
        final PktPerkGemModification pkt = new PktPerkGemModification();
        pkt.action = 1;
        pkt.perkKey = perk.getRegistryName();
        return pkt;
    }
    
    @Nonnull
    @Override
    public Encoder<PktPerkGemModification> encoder() {
        return (packet, buffer) -> {
            buffer.writeInt(packet.action);
            ByteBufUtils.writeOptional(buffer, packet.perkKey, ByteBufUtils::writeResourceLocation);
            buffer.writeInt(packet.slotId);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktPerkGemModification> decoder() {
        return (Decoder<PktPerkGemModification>)(buffer -> {
            final PktPerkGemModification pkt = new PktPerkGemModification();
            pkt.action = buffer.readInt();
            pkt.perkKey = ByteBufUtils.readOptional(buffer, ByteBufUtils::readResourceLocation);
            pkt.slotId = buffer.readInt();
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktPerkGemModification> handler() {
        return (packet, context, side) -> context.enqueueWork(() -> PerkTree.PERK_TREE.getPerk(side, packet.perkKey).ifPresent(perk -> {
            final Player player = (Player)context.getSender();
            if (!(!(perk instanceof GemSocketPerk))) {
                switch (packet.action) {
                    case 0: {
                        this.tryInsertPerk(perk, player, packet);
                        break;
                    }
                    case 1: {
                        if (((GemSocketPerk)perk).hasItem(player, LogicalSide.SERVER)) {
                            ((GemSocketPerk)perk).dropItemToPlayer(player);
                            break;
                        }
                        else {
                            break;
                        }
                        break;
                    }
                }
            }
        }));
    }
    
    private <T extends AbstractPerk & GemSocketPerk> void tryInsertPerk(final AbstractPerk perk, final Player player, final PktPerkGemModification packet) {
        final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!prog.isValid()) {
            return;
        }
        final T socketPerk = (T)perk;
        final ItemStack stack = player.getInventory().func_70301_a(packet.slotId);
        if (stack.isEmpty()) {
            return;
        }
        final ItemStack toInsert = ItemUtils.copyStackWithSize(stack, 1);
        if (!toInsert.isEmpty() && toInsert.getItem() instanceof GemSocketItem) {
            final GemSocketItem socketItem = (GemSocketItem)toInsert.getItem();
            if (socketItem.canBeInserted(toInsert, socketPerk, player, prog, LogicalSide.SERVER) && !socketPerk.hasItem(player, LogicalSide.SERVER) && socketPerk.setContainedItem(player, LogicalSide.SERVER, toInsert)) {
                player.getInventory().func_70299_a(packet.slotId, ItemUtils.copyStackWithSize(stack, stack.func_190916_E() - 1));
            }
        }
    }
}
