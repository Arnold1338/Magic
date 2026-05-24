package hellfirepvp.astralsorcery.common.network;

import hellfirepvp.astralsorcery.common.network.channel.BufferedReplyChannel;
import java.util.function.Predicate;
import net.minecraftforge.fml.network.NetworkRegistry;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;
import java.util.Collections;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.network.base.ASLoginPacket;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.network.play.client.PktRevokeGatewayAccess;
import hellfirepvp.astralsorcery.common.network.play.client.PktToggleClientOption;
import hellfirepvp.astralsorcery.common.network.play.client.PktUnlockPerk;
import hellfirepvp.astralsorcery.common.network.play.client.PktRotateTelescope;
import hellfirepvp.astralsorcery.common.network.play.client.PktRequestTeleport;
import hellfirepvp.astralsorcery.common.network.play.client.PktRequestSeed;
import hellfirepvp.astralsorcery.common.network.play.client.PktRequestPerkSealAction;
import hellfirepvp.astralsorcery.common.network.play.client.PktPerkGemModification;
import hellfirepvp.astralsorcery.common.network.play.client.PktEngraveGlass;
import hellfirepvp.astralsorcery.common.network.play.client.PktDiscoverConstellation;
import hellfirepvp.astralsorcery.common.network.play.client.PktClearBlockStorageStack;
import hellfirepvp.astralsorcery.common.network.play.client.PktAttunePlayerConstellation;
import hellfirepvp.astralsorcery.common.network.play.server.PktOpenGui;
import hellfirepvp.astralsorcery.common.network.play.server.PktUpdateGateways;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncStepAssist;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncPerkActivity;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncModifierSource;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncKnowledge;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncData;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncCharge;
import hellfirepvp.astralsorcery.common.network.play.server.PktShootEntity;
import hellfirepvp.astralsorcery.common.network.play.server.PktProgressionUpdate;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.network.play.server.PktOreScan;
import hellfirepvp.astralsorcery.common.network.login.client.PktLoginAcknowledge;
import hellfirepvp.astralsorcery.common.network.login.server.PktLoginSyncPerkInformation;
import hellfirepvp.astralsorcery.common.network.login.server.PktLoginSyncGateway;
import hellfirepvp.astralsorcery.common.network.login.server.PktLoginSyncDataHolder;
import hellfirepvp.astralsorcery.common.network.channel.SimpleSendChannel;

public class PacketChannel
{
    private static int packetIndex;
    private static final String NET_COMM_VERSION = "0";
    public static final SimpleSendChannel CHANNEL;
    
    public static void registerPackets() {
        registerLoginMessage(PktLoginSyncDataHolder::new, PktLoginSyncDataHolder::makeLogin);
        registerLoginMessage(PktLoginSyncGateway::new, PktLoginSyncGateway::makeLogin);
        registerLoginMessage(PktLoginSyncPerkInformation::new, PktLoginSyncPerkInformation::makeLogin);
        registerLoginMessage(PktLoginAcknowledge::new, PktLoginAcknowledge::new);
        registerMessage(PktOreScan::new);
        registerMessage(PktPlayEffect::new);
        registerMessage(PktProgressionUpdate::new);
        registerMessage(PktShootEntity::new);
        registerMessage(PktSyncCharge::new);
        registerMessage(PktSyncData::new);
        registerMessage(PktSyncKnowledge::new);
        registerMessage(PktSyncModifierSource::new);
        registerMessage(PktSyncPerkActivity::new);
        registerMessage(PktSyncStepAssist::new);
        registerMessage(PktUpdateGateways::new);
        registerMessage(PktOpenGui::new);
        registerMessage(PktAttunePlayerConstellation::new);
        registerMessage(PktClearBlockStorageStack::new);
        registerMessage(PktDiscoverConstellation::new);
        registerMessage(PktEngraveGlass::new);
        registerMessage(PktPerkGemModification::new);
        registerMessage(PktRequestPerkSealAction::new);
        registerMessage(PktRequestSeed::new);
        registerMessage(PktRequestTeleport::new);
        registerMessage(PktRotateTelescope::new);
        registerMessage(PktUnlockPerk::new);
        registerMessage(PktToggleClientOption::new);
        registerMessage(PktRevokeGatewayAccess::new);
    }
    
    private static <T extends ASLoginPacket<T>> void registerLoginMessage(final Supplier<T> pktSupplier, final Supplier<T> makeLoginPacket) {
        final T packet = pktSupplier.get();
        final int index = PacketChannel.packetIndex++;
        PacketChannel.CHANNEL.messageBuilder(packet.getClass(), index).loginIndex((Function)ASLoginPacket::getLoginIndex, ASLoginPacket::setLoginIndex).encoder((BiConsumer)packet.encoder()).decoder((Function)packet.decoder()).consumer((t, contextSupplier) -> {
            BiConsumer<Object, Supplier<NetworkEvent.Context>> handler;
            if (contextSupplier.get().getDirection().getReceptionSide().isServer()) {
                handler = FMLHandshakeHandler.indexFirst((handshakeHandler, pkt, ctxSupplier) -> packet.handler().accept(pkt, ctxSupplier));
            }
            else {
                handler = packet.handler();
            }
            handler.accept(t, contextSupplier);
        }).buildLoginPacketList(local -> Collections.singletonList(Pair.of((Object)packet.getClass().getName(), makeLoginPacket.get()))).add();
    }
    
    private static <T extends ASPacket<T>> void registerMessage(final Supplier<T> pktSupplier) {
        final T packet = pktSupplier.get();
        PacketChannel.CHANNEL.messageBuilder((Class<Object>)packet.getClass(), PacketChannel.packetIndex++).encoder((BiConsumer)packet.encoder()).decoder((Function)packet.decoder()).consumer((BiConsumer)packet.handler()).add();
    }
    
    public static PacketDistributor.TargetPoint pointFromPos(final World world, final Vector3i pos, final double range) {
        return pointFromPos((RegistryKey<World>)world.dimension(), pos, range);
    }
    
    public static PacketDistributor.TargetPoint pointFromPos(final RegistryKey<World> world, final Vector3i pos, final double range) {
        return new PacketDistributor.TargetPoint((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), range, (RegistryKey)world);
    }
    
    static {
        PacketChannel.packetIndex = 0;
        CHANNEL = new BufferedReplyChannel(NetworkRegistry.newSimpleChannel(AstralSorcery.key("net_channel"), () -> "0", (Predicate)"0"::equals, (Predicate)"0"::equals));
    }
}
