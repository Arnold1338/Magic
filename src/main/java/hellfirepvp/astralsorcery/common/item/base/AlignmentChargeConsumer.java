package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.entity.player.Player;

public interface AlignmentChargeConsumer extends AlignmentChargeRevealer
{
    float getAlignmentChargeCost(final Player p0, final ItemStack p1);
}
