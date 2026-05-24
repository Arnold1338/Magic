package hellfirepvp.astralsorcery.client.util;

import java.util.Objects;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.object.ObjectReference;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.gateway.CelestialGatewayHandler;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.constellation.ConstellationGenerator;
import java.util.Random;
import hellfirepvp.astralsorcery.common.util.PlayerReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.UUID;
import java.util.Map;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;

public class GatewayUI
{
    private final RegistryKey<World> dimType;
    private final BlockPos pos;
    private final Vector3 renderCenter;
    private final double sphereRadius;
    private final Map<UUID, IConstellation> playerConstellations;
    private final List<GatewayEntry> gatewayEntries;
    private int visibleTicks;
    
    private GatewayUI(final RegistryKey<World> dimType, final BlockPos pos, final Vector3 renderCenter, final double sphereRadius) {
        this.playerConstellations = new HashMap<UUID, IConstellation>();
        this.gatewayEntries = new ArrayList<GatewayEntry>();
        this.visibleTicks = 20;
        this.dimType = dimType;
        this.pos = pos;
        this.renderCenter = renderCenter;
        this.sphereRadius = sphereRadius;
        this.initializePlayerConstellations();
    }
    
    private void initializePlayerConstellations() {
        if (!this.getThisGatewayNode().isLocked() || this.getThisGatewayNode().getAllowedUsers().isEmpty()) {
            return;
        }
        for (final PlayerReference playerRef : this.getThisGatewayNode().getAllowedUsers().values()) {
            final UUID playerUUID = playerRef.getPlayerUUID();
            final long plSeed = playerUUID.getMostSignificantBits() ^ playerUUID.getLeastSignificantBits();
            final Random sRand = new Random(plSeed);
            for (int i = 0; i < sRand.nextInt(5); ++i) {
                sRand.nextLong();
            }
            this.playerConstellations.put(playerUUID, ConstellationGenerator.generateRandom(sRand.nextLong()));
        }
    }
    
    public static GatewayUI create(final World world, final BlockPos tilePos, final Vector3 renderPos, final double sphereRadius) {
        final GatewayCache.GatewayNode gatewayNode = CelestialGatewayHandler.INSTANCE.getGatewayNode(world, LogicalSide.CLIENT, tilePos);
        if (gatewayNode == null) {
            return null;
        }
        final RegistryKey<World> dimType = (RegistryKey<World>)world.dimension();
        final GatewayUI ui = new GatewayUI(dimType, tilePos, renderPos, sphereRadius);
        final Player thisPlayer = (Player)Minecraft.func_71410_x().field_71439_g;
        for (final GatewayCache.GatewayNode node : CelestialGatewayHandler.INSTANCE.getGatewaysForWorld(world, LogicalSide.CLIENT)) {
            if (!node.hasAccess(thisPlayer)) {
                continue;
            }
            appendEntry(ui, node, dimType, true, sphereRadius);
        }
        CelestialGatewayHandler.INSTANCE.getGatewayCache(LogicalSide.CLIENT).forEach((dimTypeKey, gatewayNodes) -> {
            if (dimTypeKey.equals(dimType)) {
                return;
            }
            else {
                gatewayNodes.iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final GatewayCache.GatewayNode node2 = iterator2.next();
                    if (!node2.hasAccess(thisPlayer)) {
                        continue;
                    }
                    else {
                        appendEntry(ui, node2, (RegistryKey<World>)dimTypeKey, false, sphereRadius);
                    }
                }
                return;
            }
        });
        return ui;
    }
    
    private static void appendEntry(final GatewayUI ui, final GatewayCache.GatewayNode node, final RegistryKey<World> nodeDimType, final boolean sameWorld, final double sphereRadius) {
        final Vector3 renderPos = ui.getRenderCenter();
        final Vector3 nodePos = new Vector3((Vector3i)node.getPos());
        if (sameWorld) {
            if (renderPos.distance(nodePos) < 16.0) {
                return;
            }
            final Vector3 dir = nodePos.subtract(renderPos);
            dir.setY(Math.max(dir.getY(), 0.0));
            final Vector3 sphereDirection = dir.normalize().multiply(sphereRadius);
            final GatewayEntry entry = new GatewayEntry(node, (RegistryKey)nodeDimType, sphereDirection);
            final ObjectReference<GatewayEntry> overlapping = new ObjectReference<GatewayEntry>(null);
            final GatewayEntry otherEntry;
            ui.gatewayEntries.removeIf(otherEntry -> {
                if (Math.abs(otherEntry.pitch - entry.pitch) < 7.0f && (Math.abs(otherEntry.yaw - entry.yaw) <= 7.0f || Math.abs(otherEntry.yaw - entry.yaw - 360.0f) <= 7.0f)) {
                    if (renderPos.distanceSquared(entry.getRelativePos()) < renderPos.distanceSquared(otherEntry.getRelativePos())) {
                        return true;
                    }
                    else {
                        overlapping.set(otherEntry);
                        return false;
                    }
                }
                else {
                    return false;
                }
            });
            if (overlapping.get() == null) {
                ui.gatewayEntries.add(entry);
            }
        }
        else {
            long seed = -6395079716991528811L;
            seed |= (long)node.getPos().getX() << 48;
            seed |= (long)node.getPos().getY() << 24;
            seed |= node.getPos().getZ();
            final Random rand = new Random(seed);
            Vector3 direction = Vector3.positiveYRandom(rand).normalize().multiply(sphereRadius);
            GatewayEntry entry2 = new GatewayEntry(node, (RegistryKey)nodeDimType, direction);
            int tries;
            boolean foundSpace;
            boolean mayAdd;
            Iterator<GatewayEntry> iterator;
            GatewayEntry otherEntry;
            for (tries = 50, foundSpace = false; !foundSpace && tries > 0; --tries) {
                mayAdd = true;
                iterator = ui.gatewayEntries.iterator();
                while (iterator.hasNext()) {
                    otherEntry = iterator.next();
                    if (Math.abs(otherEntry.pitch - entry2.pitch) < 15.0f && (Math.abs(otherEntry.yaw - entry2.yaw) <= 15.0f || Math.abs(otherEntry.yaw - entry2.yaw - 360.0f) <= 15.0f)) {
                        mayAdd = false;
                        break;
                    }
                }
                if (mayAdd) {
                    foundSpace = true;
                }
                else {
                    direction = Vector3.positiveYRandom(rand).normalize().multiply(sphereRadius);
                    entry2 = new GatewayEntry(node, (RegistryKey)nodeDimType, direction);
                }
            }
            if (foundSpace) {
                ui.gatewayEntries.add(entry2);
            }
        }
    }
    
    public RegistryKey<World> getDimType() {
        return this.dimType;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Vector3 getRenderCenter() {
        return this.renderCenter;
    }
    
    public double getSphereRadius() {
        return this.sphereRadius;
    }
    
    @Nullable
    public GatewayCache.GatewayNode getThisGatewayNode() {
        return CelestialGatewayHandler.INSTANCE.getGatewayNode((World)Minecraft.func_71410_x().field_71441_e, LogicalSide.CLIENT, this.getPos());
    }
    
    @Nullable
    public IConstellation getGeneratedConstellation(final UUID playerUUID) {
        return this.playerConstellations.get(playerUUID);
    }
    
    public List<GatewayEntry> getGatewayEntries() {
        return Collections.unmodifiableList((List<? extends GatewayEntry>)this.gatewayEntries);
    }
    
    public void refreshView() {
        this.visibleTicks = 20;
    }
    
    public int getVisibleTicks() {
        return this.visibleTicks;
    }
    
    public void decrementVisibleTicks() {
        --this.visibleTicks;
    }
    
    public static class GatewayEntry
    {
        private final GatewayCache.GatewayNode node;
        private final RegistryKey<World> nodeDimension;
        private final Vector3 relativePos;
        private final float yaw;
        private final float pitch;
        
        private GatewayEntry(final GatewayCache.GatewayNode node, final RegistryKey<World> nodeDimension, final Vector3 relativePos) {
            this.node = node;
            this.nodeDimension = nodeDimension;
            this.relativePos = relativePos.clone();
            if (this.relativePos.getY() < 0.0) {
                this.relativePos.setY(0);
            }
            final Vector3 angles = relativePos.copyToPolar();
            this.yaw = (float)(180.0 - angles.getZ());
            this.pitch = Math.min(0.0f, (float)(-90.0 + angles.getY()));
        }
        
        public GatewayCache.GatewayNode getNode() {
            return this.node;
        }
        
        public RegistryKey<World> getNodeDimension() {
            return this.nodeDimension;
        }
        
        public Vector3 getRelativePos() {
            return this.relativePos;
        }
        
        public float getYaw() {
            return this.yaw;
        }
        
        public float getPitch() {
            return this.pitch;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final GatewayEntry that = (GatewayEntry)o;
            return Objects.equals(this.node, that.node) && Objects.equals(this.nodeDimension, that.nodeDimension);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.node, this.nodeDimension);
        }
    }
}
