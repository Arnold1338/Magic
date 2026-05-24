package hellfirepvp.astralsorcery.common.item.wand;

import net.minecraft.core.Direction;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;

public interface WandInteractable
{
    boolean onInteract(final World p0, final BlockPos p1, final Player p2, final Direction p3, final boolean p4);
}
