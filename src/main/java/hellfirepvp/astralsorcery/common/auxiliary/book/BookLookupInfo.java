package hellfirepvp.astralsorcery.common.auxiliary.book;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.screens.Screen;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPages;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;

public class BookLookupInfo
{
    private final ResearchNode node;
    private final int pageIndex;
    private final ResearchProgression neededKnowledge;
    
    public BookLookupInfo(final ResearchNode node, final int pageIndex, final ResearchProgression neededKnowledge) {
        this.node = node;
        this.pageIndex = pageIndex;
        this.neededKnowledge = neededKnowledge;
    }
    
    public ResearchNode getResearchNode() {
        return this.node;
    }
    
    public int getPageIndex() {
        return this.pageIndex;
    }
    
    public boolean canSee(final PlayerProgress progress) {
        return this.getResearchNode().canSee(progress) && progress.hasResearch(this.neededKnowledge);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void openGui() {
        Minecraft.func_71410_x().func_147108_a((Screen)new ScreenJournalPages(Minecraft.func_71410_x().field_71462_r, this.getResearchNode(), this.getPageIndex()));
    }
}
