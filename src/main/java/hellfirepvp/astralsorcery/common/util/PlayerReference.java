package hellfirepvp.astralsorcery.common.util;

import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.text.IFormattableTextComponent;
import java.util.UUID;

public class PlayerReference
{
    private final UUID playerUUID;
    private final IFormattableTextComponent playerName;
    
    public PlayerReference(final UUID playerUUID, final IFormattableTextComponent playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
    }
    
    public static PlayerReference of(final Player player) {
        final Component txt = player.func_145748_c_();
        if (txt instanceof IFormattableTextComponent) {
            return new PlayerReference(player.getUUID(), (IFormattableTextComponent)txt);
        }
        return new PlayerReference(player.getUUID(), new Component("").func_230529_a_(txt));
    }
    
    public boolean isPlayer(final Player player) {
        return this.getPlayerUUID().equals(player.getUUID());
    }
    
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
    
    public Component getPlayerName() {
        return (Component)this.playerName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PlayerReference that = (PlayerReference)o;
        return Objects.equals(this.playerUUID, that.playerUUID);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.playerUUID);
    }
    
    @Nullable
    public ServerPlayer getOnlinePlayer() {
        final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            throw new IllegalArgumentException("Called getOnlinePlayer on clientside or while no server is running!");
        }
        return server.getPlayerList().getPlayer(this.playerUUID);
    }
    
    public CompoundTag serialize() {
        final CompoundTag tag = new CompoundTag();
        this.writeToNBT(tag);
        return tag;
    }
    
    public void writeToNBT(final CompoundTag tag) {
        tag.putUUID("playerUUID", this.playerUUID);
        tag.putString("playerName", Component.Serializer.func_150696_a((Component)this.playerName));
    }
    
    public void write(final FriendlyByteBuf buf) {
        ByteBufUtils.writeUUID(buf, this.playerUUID);
        ByteBufUtils.writeTextComponent(buf, (Component)this.playerName);
    }
    
    public static PlayerReference deserialize(final CompoundTag tag) {
        return new PlayerReference(tag.getUUID("playerUUID"), Component.Serializer.func_240643_a_(tag.getString("playerName")));
    }
    
    public static PlayerReference read(final FriendlyByteBuf buf) {
        return new PlayerReference(ByteBufUtils.readUUID(buf), ByteBufUtils.readTextComponent(buf));
    }
}
