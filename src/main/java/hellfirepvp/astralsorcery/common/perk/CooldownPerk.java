package hellfirepvp.astralsorcery.common.perk;

import net.minecraft.world.entity.player.Player;

public interface CooldownPerk
{
    void onCooldownTimeout(final Player p0);
}
