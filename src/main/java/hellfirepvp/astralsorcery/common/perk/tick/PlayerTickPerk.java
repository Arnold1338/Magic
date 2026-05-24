package hellfirepvp.astralsorcery.common.perk.tick;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;

public interface PlayerTickPerk
{
    void onPlayerTick(final Player p0, final LogicalSide p1);
}
