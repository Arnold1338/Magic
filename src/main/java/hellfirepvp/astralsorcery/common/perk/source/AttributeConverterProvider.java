package hellfirepvp.astralsorcery.common.perk.source;

import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;

public interface AttributeConverterProvider
{
    Collection<PerkConverter> getConverters(final Player p0, final LogicalSide p1, final boolean p2);
}
