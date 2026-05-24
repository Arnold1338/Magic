package hellfirepvp.astralsorcery.common.perk.node.focus;

import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.ConstellationPerk;

public abstract class FocusPerk extends ConstellationPerk
{
    public FocusPerk(final ResourceLocation name, final IConstellation cst, final float x, final float y) {
        super(name, cst, x, y);
        this.setCategory(FocusPerk.CATEGORY_FOCUS);
    }
    
    @Override
    public boolean mayUnlockPerk(final PlayerProgress progress, final Player player) {
        return super.mayUnlockPerk(progress, player) && !progress.getPerkData().hasPerkEffect(perk -> perk.getCategory().equals(FocusPerk.CATEGORY_FOCUS));
    }
}
