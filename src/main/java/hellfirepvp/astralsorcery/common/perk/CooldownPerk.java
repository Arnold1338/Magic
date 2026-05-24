package hellfirepvp.astralsorcery.common.perk;

import net.minecraft.world.level.entity.player.Player;

public interface CooldownPerk
{
    void onCooldownTimeout(final Player p0);
}
