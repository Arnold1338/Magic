package hellfirepvp.astralsorcery.common.starlight.transmission;

import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.IStarlightSource;
import hellfirepvp.astralsorcery.common.tile.base.TileNetwork;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionWorldHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.core.Vec3i;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.IStarlightTransmission;

public class TransmissionNetworkHelper
{
    private static final double MAX_TRANSMISSION_DIST = 16.0;
    
    public static boolean hasTransmissionLink(final IStarlightTransmission<?> tr, final BlockPos end) {
        final IPrismTransmissionNode node = (IPrismTransmissionNode)tr.getNode();
        if (node == null) {
            return false;
        }
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(tr.getTrWorld());
        final List<NodeConnection<IPrismTransmissionNode>> nextNodes = node.queryNext(handler);
        for (final NodeConnection<IPrismTransmissionNode> nextNode : nextNodes) {
            if (nextNode.getTo().equals((Object)end)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canCreateTransmissionLink(final IStarlightTransmission<?> tr, final BlockPos end) {
        final IPrismTransmissionNode node = (IPrismTransmissionNode)tr.getNode();
        if (node == null) {
            return false;
        }
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(tr.getTrWorld());
        final List<NodeConnection<IPrismTransmissionNode>> nextNodes = node.queryNext(handler);
        for (final NodeConnection<IPrismTransmissionNode> nextNode : nextNodes) {
            if (nextNode.getTo().equals((Object)end)) {
                return false;
            }
        }
        return tr.getTrPos().func_218141_a((Vector3i)new BlockPos((Vector3i)end), 16.0);
    }
    
    public static boolean createTransmissionLink(final IStarlightTransmission<?> tr, final BlockPos next) {
        final IPrismTransmissionNode node = (IPrismTransmissionNode)tr.getNode();
        if (node == null) {
            AstralSorcery.log.info("Trying to create transmission link on non-existing transmission tile! Not creating link!");
            return false;
        }
        createLink(node, tr, next);
        return true;
    }
    
    public static void removeTransmissionLink(final IStarlightTransmission<?> tr, final BlockPos next) {
        final IPrismTransmissionNode node = (IPrismTransmissionNode)tr.getNode();
        if (node == null) {
            return;
        }
        removeLink(node, tr, next);
    }
    
    private static void removeLink(final IPrismTransmissionNode transmissionNode, final IStarlightTransmission<?> transmission, final BlockPos to) {
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(transmission.getTrWorld());
        final IPrismTransmissionNode nextNode = handler.getTransmissionNode(to);
        removeLink(transmissionNode, nextNode, transmission.getTrWorld(), transmission.getTrPos(), to);
        handler.markDirty((Vector3i)transmission.getTrPos(), (Vector3i)to);
    }
    
    private static void removeLink(final IPrismTransmissionNode thisNode, final IPrismTransmissionNode nextNode, final Level world, final BlockPos from, final BlockPos to) {
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(world);
        if (nextNode != null) {
            nextNode.notifySourceUnlink(world, from);
            if (handle != null) {
                handle.notifyTransmissionNodeChange(nextNode);
            }
        }
        thisNode.notifyUnlink(world, to);
        if (handle != null) {
            handle.notifyTransmissionNodeChange(thisNode);
        }
    }
    
    private static void createLink(final IPrismTransmissionNode transmissionNode, final IStarlightTransmission<?> transmission, final BlockPos to) {
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(transmission.getTrWorld());
        final IPrismTransmissionNode nextNode = handler.getTransmissionNode(to);
        createLink(transmissionNode, nextNode, transmission.getTrWorld(), transmission.getTrPos(), to);
        handler.markDirty((Vector3i)transmission.getTrPos(), (Vector3i)to);
    }
    
    private static void createLink(final IPrismTransmissionNode thisNode, final IPrismTransmissionNode nextNode, final Level world, final BlockPos from, final BlockPos to) {
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(world);
        if (nextNode != null) {
            nextNode.notifySourceLink(world, from);
            if (handle != null) {
                handle.notifyTransmissionNodeChange(nextNode);
            }
        }
        thisNode.notifyLink(world, to);
        if (handle != null) {
            handle.notifyTransmissionNodeChange(thisNode);
        }
    }
    
    public static boolean isTileInNetwork(final TileNetwork<?> tileNetwork) {
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(tileNetwork.getLevel());
        return handler.getTransmissionNode(tileNetwork.getBlockState()) != null;
    }
    
    public static void informNetworkTilePlacement(final TileNetwork<?> tileNetwork) {
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(tileNetwork.getLevel());
        if (tileNetwork instanceof IStarlightSource) {
            handler.addNewSourceTile((IStarlightSource)tileNetwork);
        }
        else if (tileNetwork instanceof IStarlightTransmission) {
            handler.addTransmissionTile((IStarlightTransmission)tileNetwork);
        }
        else {
            AstralSorcery.log.warn("Placed a network tile that's not transmission/receiver or source! At: dim=" + tileNetwork.getLevel().dimension().func_240901_a_() + ", pos=" + tileNetwork.getBlockState());
        }
        final IPrismTransmissionNode node = handler.getTransmissionNode(tileNetwork.getBlockState());
        if (node == null) {
            AstralSorcery.log.warn("Placed a network tile that didn't produce a network node! At: dim=" + tileNetwork.getLevel().dimension().func_240901_a_() + ", pos=" + tileNetwork.getBlockState());
        }
        else if (node.needsUpdate()) {
            StarlightUpdateHandler.getInstance().addNode(tileNetwork.getLevel(), node);
        }
    }
    
    public static void informNetworkTileRemoval(final TileNetwork<?> tileNetwork) {
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(tileNetwork.getLevel());
        final IPrismTransmissionNode node = handler.getTransmissionNode(tileNetwork.getBlockState());
        if (node == null) {
            AstralSorcery.log.warn("Tried to get a network node at a BlockEntity, but didn't find one! At: dim=" + tileNetwork.getLevel().dimension().func_240901_a_() + ", pos=" + tileNetwork.getBlockState());
        }
        else {
            StarlightUpdateHandler.getInstance().removeNode(((IStarlightTransmission)tileNetwork).getTrWorld(), node);
        }
        if (tileNetwork instanceof IStarlightSource) {
            handler.removeSource((IStarlightSource)tileNetwork);
        }
        else if (tileNetwork instanceof IStarlightTransmission) {
            handler.removeTransmission((IStarlightTransmission)tileNetwork);
        }
        else {
            AstralSorcery.log.warn("Removed a network tile that's not transmission/receiver or source! At: dim=" + tileNetwork.getLevel().dimension().func_240901_a_() + ", pos=" + tileNetwork.getBlockState());
        }
    }
}
