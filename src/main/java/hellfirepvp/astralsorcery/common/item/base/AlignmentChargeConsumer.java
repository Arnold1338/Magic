package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;

public interface AlignmentChargeConsumer extends AlignmentChargeRevealer
{
    float getAlignmentChargeCost(final Player p0, final ItemStack p1);
}
