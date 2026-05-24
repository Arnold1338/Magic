package hellfirepvp.astralsorcery.common.perk;

import java.util.HashMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nonnull;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
import java.util.Map;

public class PerkAttributeHelper
{
    private static final Map<UUID, PerkAttributeMap> playerPerkAttributes;
    private static final Map<UUID, PerkAttributeMap> playerPerkAttributesClient;
    
    private PerkAttributeHelper() {
    }
    
    @Nonnull
    public static PerkAttributeMap getOrCreateMap(final Player player, final LogicalSide dist) {
        if (dist.isClient()) {
            return PerkAttributeHelper.playerPerkAttributesClient.computeIfAbsent(player.getUUID(), uuid -> new PerkAttributeMap(dist));
        }
        return PerkAttributeHelper.playerPerkAttributes.computeIfAbsent(player.getUUID(), uuid -> new PerkAttributeMap(dist));
    }
    
    public static PerkAttributeMap getMockInstance(final LogicalSide side) {
        return new PerkAttributeMap(side);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void clearClient() {
        PerkAttributeHelper.playerPerkAttributesClient.clear();
    }
    
    public static void clearServer() {
        PerkAttributeHelper.playerPerkAttributes.clear();
    }
    
    static {
        playerPerkAttributes = new HashMap<UUID, PerkAttributeMap>();
        playerPerkAttributesClient = new HashMap<UUID, PerkAttributeMap>();
    }
}
