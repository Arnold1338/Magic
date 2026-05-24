package hellfirepvp.astralsorcery.common.crafting.recipe;

import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.entity.player.Player;

public interface GatedRecipe
{
    boolean hasProgressionServer(final Player p0);
    
    @OnlyIn(Dist.CLIENT)
    boolean hasProgressionClient();
    
    public interface Progression extends GatedRecipe
    {
        @Nonnull
        ResearchProgression getRequiredProgression();
        
        default boolean hasProgressionServer(final Player player) {
            return ResearchHelper.getProgress(player, LogicalSide.SERVER).hasResearch(this.getRequiredProgression());
        }
        
        @OnlyIn(Dist.CLIENT)
        default boolean hasProgressionClient() {
            return ResearchHelper.getClientProgress().hasResearch(this.getRequiredProgression());
        }
    }
}
