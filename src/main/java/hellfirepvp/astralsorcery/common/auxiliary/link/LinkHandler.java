package hellfirepvp.astralsorcery.common.auxiliary.link;

import java.util.HashMap;
import net.minecraft.world.item.ItemStack;
import java.util.EnumSet;
import java.util.Iterator;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
import java.util.Map;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class LinkHandler implements ITickHandler
{
    private static final LinkHandler instance;
    private static final Map<UUID, LinkSession> players;
    
    private LinkHandler() {
    }
    
    public static LinkHandler getInstance() {
        return LinkHandler.instance;
    }
    
    @Nullable
    public static LinkSession getActiveSession(final Player player) {
        return LinkHandler.players.get(player.getUUID());
    }
    
    @Nonnull
    public static RightClickResult onInteractEntity(final Player clicked, final LivingEntity entity) {
        final LinkSession session = LinkSession.entity(entity);
        LinkHandler.players.put(clicked.getUUID(), session);
        return new RightClickResult(RightClickResultType.SELECT_START, session);
    }
    
    @Nonnull
    public static RightClickResult onInteractBlock(final Player clicked, final Level world, final BlockPos pos, final boolean sneak) {
        final UUID playerUUID = clicked.getUUID();
        if (!LinkHandler.players.containsKey(playerUUID)) {
            final LinkableTileEntity tile = MiscUtils.getTileAt((IBlockReader)world, pos, LinkableTileEntity.class, true);
            if (tile == null) {
                return new RightClickResult(RightClickResultType.NONE, null);
            }
            final LinkSession session = LinkSession.tile(tile);
            LinkHandler.players.put(playerUUID, session);
            return new RightClickResult(RightClickResultType.SELECT_START, session);
        }
        else {
            final LinkSession session2 = LinkHandler.players.get(playerUUID);
            if (session2.getType() == LinkType.ENTITY) {
                final LinkableTileEntity tile2 = MiscUtils.getTileAt((IBlockReader)world, pos, LinkableTileEntity.class, true);
                if (tile2 == null) {
                    LinkHandler.players.remove(playerUUID);
                    return new RightClickResult(RightClickResultType.NONE, null);
                }
                session2.setSelected(tile2);
                return new RightClickResult(RightClickResultType.TRY_LINK, session2);
            }
            else {
                if (sneak) {
                    return new RightClickResult(RightClickResultType.TRY_UNLINK, session2);
                }
                return new RightClickResult(RightClickResultType.TRY_LINK, session2);
            }
        }
    }
    
    public static void processInteraction(final RightClickResult result, final Player playerIn, final Level world, final BlockPos pos) {
        final LinkSession session = result.getLinkingSession();
        final LinkableTileEntity tile = session.getSelectedTile();
        switch (result.getType()) {
            case SELECT_START: {
                if (session.getType() == LinkType.ENTITY) {
                    playerIn.func_145747_a((Component)new Component("astralsorcery.misc.link.start", new Object[] { result.getLinkingSession().getSelectedEntity().getDisplayName() }).withStyle(ChatFormatting.GREEN)), Util.NIL_UUID);
                    break;
                }
                final String name = tile.getUnLocalizedDisplayName();
                if (tile.onSelect(playerIn) && name != null) {
                    playerIn.func_145747_a((Component)new Component("astralsorcery.misc.link.start", new Object[] { new Component(name) }).withStyle(ChatFormatting.GREEN)), Util.NIL_UUID);
                }
                break;
            }
            case TRY_LINK: {
                final BlockEntity te = MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, true);
                String linkedToName = "astralsorcery.misc.link.link.block";
                if (te instanceof LinkableTileEntity) {
                    if (!((LinkableTileEntity)te).doesAcceptLinks()) {
                        return;
                    }
                    final String unloc = ((LinkableTileEntity)te).getUnLocalizedDisplayName();
                    if (unloc != null) {
                        linkedToName = unloc;
                    }
                }
                if (session.getType() == LinkType.ENTITY && te instanceof LinkableTileEntity) {
                    final LinkableTileEntity linkTarget = (LinkableTileEntity)te;
                    final LivingEntity linked = session.getSelectedEntity();
                    if (linkTarget.tryLinkEntity(playerIn, linked)) {
                        linkTarget.onEntityLinkCreate(playerIn, linked);
                    }
                    break;
                }
                if (tile.tryLinkBlock(playerIn, pos)) {
                    tile.onBlockLinkCreate(playerIn, pos);
                    final String linkedFrom = tile.getUnLocalizedDisplayName();
                    if (linkedFrom != null) {
                        playerIn.func_145747_a((Component)new Component("astralsorcery.misc.link.link", new Object[] { new Component(linkedFrom), new Component(linkedToName) }).withStyle(ChatFormatting.GREEN)), Util.NIL_UUID);
                    }
                    break;
                }
                break;
            }
            case TRY_UNLINK: {
                if (tile.tryUnlink(playerIn, pos)) {
                    String linkedToName = "astralsorcery.misc.link.link.block";
                    final BlockEntity te = MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, true);
                    if (te instanceof LinkableTileEntity) {
                        final String unloc = ((LinkableTileEntity)te).getUnLocalizedDisplayName();
                        if (unloc != null) {
                            linkedToName = unloc;
                        }
                    }
                    final String linkedFrom = tile.getUnLocalizedDisplayName();
                    if (linkedFrom != null) {
                        playerIn.func_145747_a((Component)new Component("astralsorcery.misc.link.unlink", new Object[] { new Component(linkedFrom), new Component(linkedToName) }).withStyle(ChatFormatting.GREEN)), Util.NIL_UUID);
                    }
                    break;
                }
                break;
            }
        }
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }
        final Iterator<UUID> iterator = LinkHandler.players.keySet().iterator();
        while (iterator.hasNext()) {
            final UUID uuid = iterator.next();
            final LinkSession session = LinkHandler.players.get(uuid);
            final Player player = (Player)server.getPlayerList().getPlayer(uuid);
            if (player == null) {
                iterator.remove();
            }
            else {
                boolean needsRemoval = MiscUtils.getMainOrOffHand((LivingEntity)player, stack -> stack.getItem() instanceof IItemLinkingTool) == null;
                switch (session.getType()) {
                    case ENTITY: {
                        final LivingEntity entity = session.getSelectedEntity();
                        if (!entity.isAlive() || !entity.level().dimension().equals(player.level().dimension())) {
                            needsRemoval = true;
                            break;
                        }
                        break;
                    }
                    case BLOCK: {
                        if (!session.getSelectedTile().getLinkWorld().dimension().equals(player.level().dimension())) {
                            needsRemoval = true;
                            break;
                        }
                        break;
                    }
                }
                if (!needsRemoval) {
                    continue;
                }
                iterator.remove();
                player.func_145747_a((Component)new Component("astralsorcery.misc.link.stop").withStyle(ChatFormatting.RED)), Util.NIL_UUID);
            }
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.SERVER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "LinkHandler";
    }
    
    static {
        instance = new LinkHandler();
        players = new HashMap<UUID, LinkSession>();
    }
    
    public static class LinkSession
    {
        private final LinkType type;
        private LinkableTileEntity selected;
        private final LivingEntity entity;
        
        private LinkSession(final LinkType type, final LinkableTileEntity selected, final LivingEntity entity) {
            this.type = type;
            this.selected = selected;
            this.entity = entity;
        }
        
        public static LinkSession tile(final LinkableTileEntity selected) {
            return new LinkSession(LinkType.BLOCK, selected, null);
        }
        
        public static LinkSession entity(final LivingEntity entity) {
            return new LinkSession(LinkType.ENTITY, null, entity);
        }
        
        public LinkType getType() {
            return this.type;
        }
        
        @Nullable
        public LinkableTileEntity getSelectedTile() {
            return this.selected;
        }
        
        public void setSelected(final LinkableTileEntity selected) {
            this.selected = selected;
        }
        
        @Nullable
        public LivingEntity getSelectedEntity() {
            return this.entity;
        }
    }
    
    public static class RightClickResult
    {
        private final RightClickResultType type;
        private final LinkSession linkingSession;
        
        RightClickResult(final RightClickResultType type, final LinkSession linkingSession) {
            this.type = type;
            this.linkingSession = linkingSession;
        }
        
        public RightClickResultType getType() {
            return this.type;
        }
        
        public LinkSession getLinkingSession() {
            return this.linkingSession;
        }
        
        public boolean shouldProcess() {
            return this.getType() != RightClickResultType.NONE;
        }
    }
    
    public enum LinkType
    {
        ENTITY, 
        BLOCK;
    }
    
    public enum RightClickResultType
    {
        SELECT_START, 
        TRY_LINK, 
        TRY_UNLINK, 
        NONE;
    }
}
