package hellfirepvp.astralsorcery.common.base.patreon.manager;

import java.util.EnumSet;
import java.util.Iterator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonPartialEntity;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientPatreonFlares;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class PatreonManagerClient implements ITickHandler
{
    public static PatreonManagerClient INSTANCE;
    
    private PatreonManagerClient() {
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Level clWorld = (Level)Minecraft.getInstance().level;
        final Player thisPlayer = (Player)Minecraft.getInstance().player;
        if (clWorld == null || thisPlayer == null) {

        }
        final ResourceKey<Level> clientWorld = (ResourceKey<Level>)clWorld.dimension();
        final Vector3 thisPlayerPos = Vector3.atEntityCenter((Entity)thisPlayer);
        SyncDataHolder.executeClient(SyncDataHolder.DATA_PATREON_FLARES, ClientPatreonFlares.class, data -> {
            data.getEntities().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final Collection<PatreonPartialEntity> playerEntities = iterator.next();
                playerEntities.iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final PatreonPartialEntity entity = iterator2.next();
                    if (entity.getLastTickedDimension() != null) {
                        if (!entity.getLastTickedDimension().equals(clientWorld)) {

                        }
                        else {
                            if (entity.getPos().distanceSquared(thisPlayerPos) <= RenderingConfig.CONFIG.getMaxEffectRenderDistanceSq()) {
                                entity.tickClient();
                            }
                            entity.tick(clWorld);
                        }
                    }
                }
            }

        });
        SyncDataHolder.executeClient(SyncDataHolder.DATA_PATREON_FLARES, ClientPatreonFlares.class, data -> {
            clWorld.func_217369_A().iterator();
            final Iterator iterator3;
            while (iterator3.hasNext()) {
                final Player player = iterator3.next();
                PatreonEffectHelper.getPatreonEffects(LogicalSide.CLIENT, player.getUUID()).iterator();
                final Iterator iterator4;
                while (iterator4.hasNext()) {
                    final PatreonEffect effect = iterator4.next();
                    effect.doClientEffect(player);
                }
            }
        });
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Patreon Flare Manager (Client)";
    }
    
    static {
        PatreonManagerClient.INSTANCE = new PatreonManagerClient();
    }
}
