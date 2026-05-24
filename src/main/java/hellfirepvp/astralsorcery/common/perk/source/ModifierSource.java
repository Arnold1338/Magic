package hellfirepvp.astralsorcery.common.perk.source;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;

public interface ModifierSource
{
    boolean canApplySource(final Player p0, final LogicalSide p1);
    
    void onRemove(final Player p0, final LogicalSide p1);
    
    void onApply(final Player p0, final LogicalSide p1);
    
    boolean isEqual(final ModifierSource p0);
    
    ResourceLocation getProviderName();
}
