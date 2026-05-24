package hellfirepvp.astralsorcery.common.perk.source;

import hellfirepvp.astralsorcery.common.network.play.server.PktSyncModifierSource;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.PerkEffectHelper;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public abstract class ModifierSourceProvider<T extends ModifierSource>
{
    private final ResourceLocation key;
    private final Map<UUID, Map<ResourceLocation, T>> cachedSources;
    
    protected ModifierSourceProvider(final ResourceLocation key) {
        this.cachedSources = new HashMap<UUID, Map<ResourceLocation, T>>();
        this.key = key;
    }
    
    protected abstract void update(final ServerPlayer p0);
    
    protected abstract void removeModifiers(final ServerPlayer p0);
    
    public abstract void serialize(final T p0, final FriendlyByteBuf p1);
    
    public abstract T deserialize(final FriendlyByteBuf p0);
    
    @Nullable
    private T getModifier(final ServerPlayer player, final ResourceLocation identifier) {
        final Map<ResourceLocation, T> playerModifiers = this.cachedSources.computeIfAbsent(player.getUUID(), uuid -> new HashMap());
        return playerModifiers.get(identifier);
    }
    
    private void setModifier(final ServerPlayer player, final ResourceLocation identifier, @Nullable final T source) {
        final Map<ResourceLocation, T> playerModifiers = this.cachedSources.computeIfAbsent(player.getUUID(), uuid -> new HashMap());
        if (source != null) {
            playerModifiers.put(identifier, source);
        }
        else {
            playerModifiers.remove(identifier);
        }
    }
    
    protected void updateSource(final ServerPlayer player, final ResourceLocation identifier, @Nullable final T source) {
        boolean needsRemoval = false;
        boolean needsAddition = false;
        final T existing = this.getModifier(player, identifier);
        if (existing != null) {
            if (existing.isEqual(source)) {
                return;
            }
            needsRemoval = true;
        }
        if (source != null) {
            needsAddition = true;
        }
        if (needsRemoval) {
            if (needsAddition) {
                PerkEffectHelper.updateSource((Player)player, LogicalSide.SERVER, existing, source);
                PacketChannel.CHANNEL.sendToPlayer((Player)player, PktSyncModifierSource.update(existing, source));
            }
            else {
                PerkEffectHelper.modifySource((Player)player, LogicalSide.SERVER, existing, PerkEffectHelper.Action.REMOVE);
                PacketChannel.CHANNEL.sendToPlayer((Player)player, PktSyncModifierSource.remove(existing));
            }
        }
        else if (needsAddition) {
            PerkEffectHelper.modifySource((Player)player, LogicalSide.SERVER, source, PerkEffectHelper.Action.ADD);
            PacketChannel.CHANNEL.sendToPlayer((Player)player, PktSyncModifierSource.add(source));
        }
        this.setModifier(player, identifier, source);
    }
    
    public final ResourceLocation getKey() {
        return this.key;
    }
}
