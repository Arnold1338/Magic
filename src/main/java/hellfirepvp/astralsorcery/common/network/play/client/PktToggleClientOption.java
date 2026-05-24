package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktToggleClientOption extends ASPacket<PktToggleClientOption>
{
    private Option option;
    
    public PktToggleClientOption() {
    }
    
    public PktToggleClientOption(final Option option) {
        this.option = option;
    }
    
    @Nonnull
    @Override
    public Encoder<PktToggleClientOption> encoder() {
        return (pkt, buf) -> ByteBufUtils.writeEnumValue(buf, pkt.option);
    }
    
    @Nonnull
    @Override
    public Decoder<PktToggleClientOption> decoder() {
        return (Decoder<PktToggleClientOption>)(buf -> new PktToggleClientOption(ByteBufUtils.readEnumValue(buf, Option.class)));
    }
    
    @Nonnull
    @Override
    public Handler<PktToggleClientOption> handler() {
        return new Handler<PktToggleClientOption>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktToggleClientOption packet, final NetworkEvent.Context context) {
            }
            
            @Override
            public void handleServer(final PktToggleClientOption packet, final NetworkEvent.Context context) {
                final ServerPlayer player = context.getSender();
                switch (packet.option) {
                    case DISABLE_PERK_ABILITIES: {
                        if (!ResearchManager.togglePerkAbilities((Player)player)) {
                            break;
                        }
                        final PlayerProgress prog = ResearchHelper.getProgress((Player)player, LogicalSide.SERVER);
                        if (prog.isValid()) {
                            Component status;
                            if (prog.doPerkAbilities()) {
                                status = (Component)new Component("astralsorcery.progress.perk_abilities.enable").func_240699_a_(ChatFormatting.GREEN);
                            }
                            else {
                                status = (Component)new Component("astralsorcery.progress.perk_abilities.disable").func_240699_a_(ChatFormatting.RED);
                            }
                            player.func_145747_a((Component)new Component("astralsorcery.progress.perk_abilities", new Object[] { status }).func_240699_a_(ChatFormatting.GRAY), Util.NIL_UUID);
                            break;
                        }
                        break;
                    }
                }
            }
            
            @Override
            public void handle(final PktToggleClientOption packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
    
    public enum Option
    {
        DISABLE_PERK_ABILITIES;
    }
}
