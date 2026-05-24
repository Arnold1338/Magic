package hellfirepvp.astralsorcery.common.perk.type.vanilla;

import net.minecraft.world.level.entity.player.Player;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.ai.attributes.Attribute;

public interface VanillaPerkAttributeType
{
    @Nonnull
    Attribute getAttribute();
    
    void refreshAttribute(final Player p0);
}
