package hellfirepvp.astralsorcery.common.tile.base;

import hellfirepvp.astralsorcery.common.util.PlayerReference;
import javax.annotation.Nullable;
import net.minecraft.world.level.entity.player.Player;

public interface TileOwned
{
    @Nullable
    default PlayerReference setOwner(@Nullable final Player player) {
        return this.setOwner((player == null) ? null : PlayerReference.of(player));
    }
    
    @Nullable
    PlayerReference setOwner(@Nullable final PlayerReference p0);
    
    @Nullable
    PlayerReference getOwner();
}
