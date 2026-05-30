package hellfirepvp.astralsorcery.common.event.helper;

import java.util.HashMap;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.Collections;
import net.minecraftforge.event.TickEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import java.util.HashSet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.damagesource.DamageSource;
import java.util.Set;
import java.util.UUID;
import java.util.Map;

public class EventHelperDamageCancelling
{
    private static final Map<UUID, Set<DamageSource>> invulnerableTypes;
    
    private EventHelperDamageCancelling() {
    }
    
    public static void markInvulnerableToNextDamage(final Player player, final DamageSource source) {
        if (player.level()) {

        }
        EventHelperDamageCancelling.invulnerableTypes.computeIfAbsent(player.getUUID(), uuid -> new HashSet()).add(source);
    }
    
    public static void attachListeners(final IEventBus bus) {
        bus.addListener((Consumer)EventHelperDamageCancelling::onLivingDamage);
        bus.addListener((Consumer)EventHelperDamageCancelling::onPlayerTick);
    }
    
    private static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        final Player player = event.player;
        if (event.phase == TickEvent.Phase.END && !player.level().isClientSide() && player.func_233570_aj_()) {
            final Set<DamageSource> sources = EventHelperDamageCancelling.invulnerableTypes.getOrDefault(event.player.getUUID(), Collections.emptySet());
            sources.remove(DamageSource.field_76379_h);
        }
    }
    
    private static void onLivingDamage(final LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {

        }
        final Player player = (Player)event.getEntityLiving();
        final Set<DamageSource> sources = EventHelperDamageCancelling.invulnerableTypes.getOrDefault(player.getUUID(), Collections.emptySet());
        if (sources.remove(event.getSource())) {
            if (sources.isEmpty()) {
                EventHelperDamageCancelling.invulnerableTypes.remove(player.getUUID());
            }
            event.setCanceled(true);
        }
    }
    
    static {
        invulnerableTypes = new HashMap<UUID, Set<DamageSource>>();
    }
}
