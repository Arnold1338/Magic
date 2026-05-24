package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;

public interface OverrideInteractItem
{
    boolean shouldInterceptBlockInteract(final LogicalSide p0, final Player p1, final Hand p2, final BlockPos p3, final Direction p4);
    
    default boolean shouldInterceptEntityInteract(final LogicalSide side, final Player player, final Hand hand, final Entity interacted) {
        return false;
    }
    
    boolean doBlockInteract(final LogicalSide p0, final Player p1, final Hand p2, final BlockPos p3, final Direction p4);
    
    default boolean doEntityInteract(final LogicalSide side, final Player player, final Hand hand, final Entity interacted) {
        return false;
    }
}
