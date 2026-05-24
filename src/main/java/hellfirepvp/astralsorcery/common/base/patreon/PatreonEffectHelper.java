package hellfirepvp.astralsorcery.common.base.patreon;

import java.util.HashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.player.Player;
import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.Collections;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import net.minecraftforge.fml.LogicalSide;
import java.util.List;
import java.util.UUID;
import java.util.Map;

public class PatreonEffectHelper
{
    static boolean loadingFinished;
    static Map<UUID, List<PatreonEffect>> playerEffectMap;
    static Map<UUID, PatreonEffect> effectMap;
    
    @Nonnull
    public static List<PatreonEffect> getPatreonEffects(final LogicalSide side, final UUID playerUUID) {
        if (side.isClient() && !(boolean)RenderingConfig.CONFIG.patreonEffects.get()) {
            return Collections.emptyList();
        }
        if (!PatreonEffectHelper.loadingFinished) {
            return Collections.emptyList();
        }
        return PatreonEffectHelper.playerEffectMap.getOrDefault(playerUUID, Collections.emptyList());
    }
    
    @Nullable
    public static PatreonEffect getEffect(final UUID effectUUID) {
        return PatreonEffectHelper.effectMap.get(effectUUID);
    }
    
    public static <T extends Player> Map<UUID, List<PatreonEffect>> getPatreonEffects(final Collection<T> players) {
        if (!PatreonEffectHelper.loadingFinished) {
            return Maps.newHashMap();
        }
        final Collection<UUID> playerUUIDs = players.stream().map((Function<? super T, ?>)Entity::func_110124_au).collect((Collector<? super Object, ?, Collection<UUID>>)Collectors.toList());
        return PatreonEffectHelper.playerEffectMap.entrySet().stream().filter(e -> playerUUIDs.contains(e.getKey())).collect(Collectors.toMap((Function<? super Object, ? extends UUID>)Map.Entry::getKey, (Function<? super Object, ? extends List<PatreonEffect>>)Map.Entry::getValue));
    }
    
    static {
        PatreonEffectHelper.loadingFinished = false;
        PatreonEffectHelper.playerEffectMap = new HashMap<UUID, List<PatreonEffect>>();
        PatreonEffectHelper.effectMap = new HashMap<UUID, PatreonEffect>();
    }
}
