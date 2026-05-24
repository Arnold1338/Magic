package hellfirepvp.astralsorcery.common.network.play.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.PerkEffectHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktSyncModifierSource extends ASPacket<PktSyncModifierSource>
{
    private ModifierSource source;
    private ModifierSource newSource;
    private ActionType actionType;
    
    public PktSyncModifierSource() {
    }
    
    private PktSyncModifierSource(final ModifierSource source, final ModifierSource newSource, final ActionType actionType) {
        this.source = source;
        this.newSource = newSource;
        this.actionType = actionType;
    }
    
    public static PktSyncModifierSource add(final ModifierSource source) {
        return new PktSyncModifierSource(source, null, ActionType.ADD);
    }
    
    public static PktSyncModifierSource remove(final ModifierSource source) {
        return new PktSyncModifierSource(source, null, ActionType.REMOVE);
    }
    
    public static PktSyncModifierSource update(final ModifierSource existing, final ModifierSource newSource) {
        return new PktSyncModifierSource(existing, newSource, ActionType.UPDATE);
    }
    
    @Nonnull
    @Override
    public Encoder<PktSyncModifierSource> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeOptional(buffer, packet.source, ByteBufUtils::writeModifierSource);
            ByteBufUtils.writeOptional(buffer, packet.newSource, ByteBufUtils::writeModifierSource);
            ByteBufUtils.writeEnumValue(buffer, packet.actionType);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktSyncModifierSource> decoder() {
        return (Decoder<PktSyncModifierSource>)(buffer -> {
            final PktSyncModifierSource pkt = new PktSyncModifierSource();
            pkt.source = ByteBufUtils.readOptional(buffer, ByteBufUtils::readModifierSource);
            pkt.newSource = ByteBufUtils.readOptional(buffer, ByteBufUtils::readModifierSource);
            pkt.actionType = ByteBufUtils.readEnumValue(buffer, ActionType.class);
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktSyncModifierSource> handler() {
        return new Handler<PktSyncModifierSource>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktSyncModifierSource packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
                    if (player != null) {
                        switch (packet.actionType) {
                            case UPDATE: {
                                PerkEffectHelper.updateSource(player, LogicalSide.CLIENT, packet.source, packet.newSource);
                                break;
                            }
                            case ADD: {
                                PerkEffectHelper.modifySource(player, LogicalSide.CLIENT, packet.source, PerkEffectHelper.Action.ADD);
                                break;
                            }
                            case REMOVE: {
                                PerkEffectHelper.modifySource(player, LogicalSide.CLIENT, packet.source, PerkEffectHelper.Action.REMOVE);
                                break;
                            }
                        }
                    }
                });
            }
            
            @Override
            public void handle(final PktSyncModifierSource packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
    
    private enum ActionType
    {
        ADD, 
        REMOVE, 
        UPDATE;
    }
}
