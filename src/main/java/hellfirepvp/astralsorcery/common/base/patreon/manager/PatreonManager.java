package hellfirepvp.astralsorcery.common.base.patreon.manager;

import java.util.Set;
import java.util.EnumSet;
import net.minecraft.world.level.level.Level;
import net.minecraft.server.level.ServerPlayer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonPartialEntity;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectHelper;
import java.util.HashSet;
import java.util.Collection;
import java.util.UUID;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.data.sync.server.DataPatreonFlares;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class PatreonManager implements ITickHandler
{
    public static PatreonManager INSTANCE;
    
    private PatreonManager() {
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }
        SyncDataHolder.executeServer(SyncDataHolder.DATA_PATREON_FLARES, DataPatreonFlares.class, data -> {
            final ArrayList<UUID> owners = new ArrayList<UUID>(data.getOwners());
            final HashSet foundValidOwners = new HashSet<UUID>();
            final Map<UUID, List<PatreonEffect>> playerEffects = PatreonEffectHelper.getPatreonEffects((Collection<Player>)server.getPlayerList().func_181057_v());
            playerEffects.keySet().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final UUID playerUUID = iterator.next();
                final ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
                if (player == null) {
                    continue;
                }
                else {
                    final ArrayList<PatreonPartialEntity> knownEntities = new ArrayList<PatreonPartialEntity>(data.getEntities(playerUUID));
                    PatreonEffectHelper.getPatreonEffects(LogicalSide.SERVER, playerUUID).iterator();
                    final Iterator iterator2;
                    while (iterator2.hasNext()) {
                        final PatreonEffect effect = iterator2.next();
                        if (effect != null) {
                            if (!effect.hasPartialEntity()) {
                                continue;
                            }
                            else {
                                foundValidOwners.add(playerUUID);
                                PatreonPartialEntity effectEntity = MiscUtils.iterativeSearch(knownEntities, e -> e.getEffectUUID().equals(effect.getEffectUUID()));
                                if (effectEntity == null) {
                                    effectEntity = data.createEntity((Player)player, effect);
                                }
                                final World playerWorld = (World)player.func_71121_q();
                                if (effectEntity.getLastTickedDimension() != null && !playerWorld.dimension().equals(effectEntity.getLastTickedDimension())) {
                                    effectEntity.placeNear((Player)player);
                                }
                                if (effectEntity.tick(playerWorld)) {
                                    data.updateEntitiesOf(playerUUID);
                                }
                                else {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            owners.iterator();
            final Iterator iterator3;
            while (iterator3.hasNext()) {
                final UUID owner = iterator3.next();
                if (foundValidOwners.contains(owner)) {
                    continue;
                }
                else {
                    data.removeEntities(owner);
                }
            }
        });
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.SERVER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Patreon Flare Manager (Server)";
    }
    
    static {
        PatreonManager.INSTANCE = new PatreonManager();
    }
}
