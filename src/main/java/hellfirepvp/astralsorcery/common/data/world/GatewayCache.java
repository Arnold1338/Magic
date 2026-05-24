package hellfirepvp.astralsorcery.common.data.world;

import java.util.Objects;
import java.util.function.Function;
import net.minecraft.util.Tuple;
import java.util.function.BiConsumer;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import hellfirepvp.astralsorcery.common.util.PlayerReference;
import net.minecraft.item.DyeColor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.util.log.LogUtil;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.auxiliary.gateway.CelestialGatewayHandler;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import java.util.Set;
import hellfirepvp.observerlib.common.data.base.GlobalWorldData;

public class GatewayCache extends GlobalWorldData
{
    private final Set<GatewayNode> gatewayPositions;
    
    public GatewayCache(final WorldCacheDomain.SaveKey<?> key) {
        super((WorldCacheDomain.SaveKey)key);
        this.gatewayPositions = new HashSet<GatewayNode>();
    }
    
    public Collection<GatewayNode> getGatewayPositions() {
        return Collections.unmodifiableCollection((Collection<? extends GatewayNode>)this.gatewayPositions);
    }
    
    public boolean hasGateway(final BlockPos pos) {
        return this.gatewayPositions.stream().anyMatch(gateway -> gateway.getPos().equals((Object)pos));
    }
    
    @Nullable
    public GatewayNode getGatewayNode(final BlockPos pos) {
        return this.gatewayPositions.stream().filter(gateway -> gateway.getPos().equals((Object)pos)).findFirst().orElse(null);
    }
    
    public void updateGatewayNode(final BlockPos pos, final Consumer<GatewayNodeAccess> nodeFn) {
        this.gatewayPositions.stream().filter(gateway -> gateway.getPos().equals((Object)pos)).findFirst().ifPresent(gatewayNode -> this.update(gatewayNode, nodeFn));
    }
    
    private void update(final GatewayNode node, final Consumer<GatewayNodeAccess> nodeFn) {
        nodeFn.accept(node.writeAccess());
        this.markDirty();
        CelestialGatewayHandler.INSTANCE.syncToAll();
    }
    
    public boolean offerPosition(final World world, final BlockPos pos) {
        final TileCelestialGateway te = MiscUtils.getTileAt((IBlockReader)world, pos, TileCelestialGateway.class, false);
        if (te == null) {
            return false;
        }
        final GatewayNode node = new GatewayNode(pos);
        if (!this.gatewayPositions.add(node)) {
            return false;
        }
        this.markDirty();
        CelestialGatewayHandler.INSTANCE.addPosition(world, node);
        LogUtil.info(LogCategory.GATEWAY_CACHE, () -> "Added new gateway node at: dim=" + world.dimension().func_240901_a_() + ", " + pos.toString());
        return true;
    }
    
    public void removePosition(final World world, final BlockPos pos) {
        if (this.gatewayPositions.removeIf(node -> node.getPos().equals((Object)pos))) {
            this.markDirty();
            CelestialGatewayHandler.INSTANCE.removePosition(world, pos);
            LogUtil.info(LogCategory.GATEWAY_CACHE, () -> "Removed gateway node at: dim=" + world.dimension().func_240901_a_() + ", " + pos.toString());
        }
    }
    
    public void updateTick(final World world) {
    }
    
    public void onLoad(final World world) {
        super.onLoad(world);
        LogUtil.info(LogCategory.GATEWAY_CACHE, () -> "Checking GatewayCache integrity for dimension " + world.dimension().func_240901_a_());
        final long msStart = System.currentTimeMillis();
        final Iterator<GatewayNode> iterator = this.gatewayPositions.iterator();
        while (iterator.hasNext()) {
            final GatewayNode node = iterator.next();
            TileCelestialGateway gateway;
            try {
                gateway = MiscUtils.getTileAt((IBlockReader)world, node.getPos(), TileCelestialGateway.class, true);
            }
            catch (final Exception loadEx) {
                LogUtil.info(LogCategory.GATEWAY_CACHE, () -> "Failed to check gateway for " + node + " skipping");
                continue;
            }
            if (gateway == null) {
                iterator.remove();
                LogUtil.info(LogCategory.GATEWAY_CACHE, () -> "Invalid entry: " + node + " - no gateway tileentity found there!");
            }
        }
        LogUtil.info(LogCategory.GATEWAY_CACHE, () -> "GatewayCache checked and fully loaded in " + (System.currentTimeMillis() - msStart) + "ms! Collected and checked " + this.gatewayPositions.size() + " gateway nodes!");
    }
    
    public void writeToNBT(final CompoundTag compound) {
        final ListTag list = new ListTag();
        for (final GatewayNode node : this.gatewayPositions) {
            final CompoundTag tag = new CompoundTag();
            node.write(tag);
            list.add((Object)tag);
        }
        compound.put("posList", (Tag)list);
    }
    
    public void readFromNBT(final CompoundTag compound) {
        final ListTag list = compound.getList("posList", 10);
        for (int i = 0; i < list.size(); ++i) {
            final CompoundTag tag = list.getCompound(i);
            this.gatewayPositions.add(GatewayNode.read(tag));
        }
    }
    
    public static class GatewayNode
    {
        private final BlockPos pos;
        private Component display;
        private DyeColor color;
        private boolean locked;
        private PlayerReference owner;
        private Map<Integer, PlayerReference> allowedUsers;
        
        private GatewayNode(final BlockPos pos) {
            this.locked = false;
            this.owner = null;
            this.allowedUsers = new HashMap<Integer, PlayerReference>();
            this.pos = pos;
        }
        
        private GatewayNodeAccess writeAccess() {
            return new GatewayNodeAccess(this);
        }
        
        @Nonnull
        public final BlockPos getPos() {
            return this.pos;
        }
        
        @Nullable
        public Component getDisplayName() {
            return this.display;
        }
        
        @Nullable
        public DyeColor getColor() {
            return this.color;
        }
        
        public boolean isLocked() {
            return this.locked;
        }
        
        @Nullable
        public PlayerReference getOwner() {
            return this.owner;
        }
        
        public Map<Integer, PlayerReference> getAllowedUsers() {
            return Collections.unmodifiableMap((Map<? extends Integer, ? extends PlayerReference>)this.allowedUsers);
        }
        
        public boolean hasAccess(final Player player) {
            final PlayerReference owner = this.getOwner();
            return owner == null || !this.isLocked() || owner.isPlayer(player) || this.getAllowedUsers().values().stream().anyMatch(ref -> ref.isPlayer(player));
        }
        
        public void write(final CompoundTag tag) {
            NBTHelper.writeBlockPosToNBT(this.getPos(), tag);
            if (this.getDisplayName() != null) {
                tag.putString("display", Component.Serializer.func_150696_a(this.getDisplayName()));
            }
            if (this.getColor() != null) {
                NBTHelper.writeEnum(tag, "color", this.getColor());
            }
            tag.putBoolean("locked", this.isLocked());
            NBTHelper.writeOptional(tag, "owningPlayer", this.getOwner(), (compound, playerRef) -> playerRef.writeToNBT(compound));
            NBTHelper.writeList(tag, "allowedUsers", (Collection<Map.Entry<Integer, PlayerReference>>)this.allowedUsers.entrySet(), entry -> {
                final CompoundTag compound2 = new CompoundTag();
                compound2.putInt("index", (int)entry.getKey());
                compound2.put("player", (Tag)entry.getValue().serialize());
                return compound2;
            });
        }
        
        public void write(final FriendlyByteBuf buf) {
            ByteBufUtils.writePos(buf, this.getPos());
            ByteBufUtils.writeOptional(buf, this.getDisplayName(), ByteBufUtils::writeTextComponent);
            ByteBufUtils.writeOptional(buf, this.getColor(), (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeEnumValue);
            buf.writeBoolean(this.isLocked());
            ByteBufUtils.writeOptional(buf, this.getOwner(), (buffer, ref) -> ref.write(buffer));
            ByteBufUtils.writeMap(buf, this.getAllowedUsers(), FriendlyByteBuf::writeInt, (buffer, ref) -> ref.write(buffer));
        }
        
        public static GatewayNode read(final CompoundTag tag) {
            final GatewayNode node = new GatewayNode(NBTHelper.readBlockPosFromNBT(tag));
            if (tag.contains("display")) {
                node.display = (Component)Component.Serializer.func_240643_a_(tag.getString("display"));
            }
            if (tag.contains("color")) {
                node.color = NBTHelper.readEnum(tag, "color", DyeColor.class);
            }
            node.locked = tag.getBoolean("locked");
            node.owner = NBTHelper.readOptional(tag, "owningPlayer", PlayerReference::deserialize);
            NBTHelper.readList(tag, "allowedUsers", 10, nbt -> {
                final CompoundTag compound = (CompoundTag)nbt;
                return new Tuple((Object)compound.getInt("index"), (Object)PlayerReference.deserialize(compound.func_74775_l("player")));
            }).forEach(tpl -> {
                final PlayerReference playerReference = node.allowedUsers.put((Integer)tpl.func_76341_a(), (PlayerReference)tpl.func_76340_b());
                return;
            });
            return node;
        }
        
        public static GatewayNode read(final FriendlyByteBuf buf) {
            final GatewayNode node = new GatewayNode(ByteBufUtils.readPos(buf));
            node.display = ByteBufUtils.readOptional(buf, (Function<FriendlyByteBuf, Component>)ByteBufUtils::readTextComponent);
            node.color = ByteBufUtils.readOptional(buf, buffer -> ByteBufUtils.readEnumValue(buffer, DyeColor.class));
            node.locked = buf.readBoolean();
            node.owner = ByteBufUtils.readOptional(buf, PlayerReference::read);
            node.allowedUsers = ByteBufUtils.readMap(buf, FriendlyByteBuf::readInt, PlayerReference::read);
            return node;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final GatewayNode that = (GatewayNode)o;
            return Objects.equals(this.getPos(), that.getPos());
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.getPos());
        }
    }
    
    public static class GatewayNodeAccess extends GatewayNode
    {
        private final GatewayNode decorated;
        
        public GatewayNodeAccess(final GatewayNode decorated) {
            super(decorated.getPos());
            this.decorated = decorated;
        }
        
        @Nullable
        @Override
        public DyeColor getColor() {
            return this.decorated.getColor();
        }
        
        public void setColor(@Nullable final DyeColor color) {
            this.decorated.color = color;
        }
        
        @Nullable
        @Override
        public Component getDisplayName() {
            return this.decorated.getDisplayName();
        }
        
        public void setDisplayName(@Nullable final Component displayName) {
            this.decorated.display = displayName;
        }
        
        @Override
        public boolean isLocked() {
            return this.decorated.isLocked();
        }
        
        public void setLocked(final boolean locked) {
            this.decorated.locked = locked;
        }
        
        @Nullable
        @Override
        public PlayerReference getOwner() {
            return this.decorated.getOwner();
        }
        
        public void setOwner(final PlayerReference owner) {
            this.decorated.owner = owner;
        }
        
        @Override
        public Map<Integer, PlayerReference> getAllowedUsers() {
            return this.decorated.getAllowedUsers();
        }
        
        public void setAllowedUsers(final Map<Integer, PlayerReference> users) {
            this.decorated.allowedUsers.clear();
            this.decorated.allowedUsers.putAll(users);
        }
    }
}
