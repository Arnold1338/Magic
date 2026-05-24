package hellfirepvp.astralsorcery.common.event;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.tick.TimeoutList;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import java.util.function.Consumer;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.util.tick.TimeoutListContainer;

public class PlayerAffectionFlags
{
    private static final int DEFAULT_TICK_TIMEOUT = 10;
    private static final TimeoutListContainer<UUID, AffectionFlag> affectMap;
    
    private PlayerAffectionFlags() {
    }
    
    public static void attachTickListeners(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)PlayerAffectionFlags.affectMap);
    }
    
    public static void clearServerCache() {
        PlayerAffectionFlags.affectMap.clear();
    }
    
    public static void markPlayerAffected(final Player player, final AffectionFlag flag) {
        PlayerAffectionFlags.affectMap.getOrCreateList(player.getUUID()).setOrAddTimeout(10, flag);
    }
    
    public static boolean isPlayerAffected(final Player player, final AffectionFlag flag) {
        final UUID playerUUID = player.getUUID();
        return PlayerAffectionFlags.affectMap.hasList(playerUUID) && PlayerAffectionFlags.affectMap.getOrCreateList(playerUUID).contains(flag);
    }
    
    static {
        affectMap = new TimeoutListContainer<UUID, AffectionFlag>(new TimeoutListContainer.ForwardingTimeoutDelegate<UUID, AffectionFlag>(), new TickEvent.Type[] { TickEvent.Type.SERVER });
    }
    
    public abstract static class AffectionFlag implements TimeoutList.TimeoutDelegate<UUID>
    {
        private final ResourceLocation key;
        
        public AffectionFlag(final ResourceLocation key) {
            this.key = key;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final AffectionFlag that = (AffectionFlag)o;
            return Objects.equals(this.key, that.key);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.key);
        }
    }
    
    public static class NoOpAffectionFlag extends AffectionFlag
    {
        public NoOpAffectionFlag(final ResourceLocation key) {
            super(key);
        }
        
        @Override
        public void onTimeout(final UUID object) {
        }
    }
}
