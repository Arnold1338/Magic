package hellfirepvp.astralsorcery.common.perk.source;

import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;

public interface AttributeModifierProvider
{
    Collection<PerkAttributeModifier> getModifiers(final Player p0, final LogicalSide p1, final boolean p2);
}
