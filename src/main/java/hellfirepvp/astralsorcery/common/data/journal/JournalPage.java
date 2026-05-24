package hellfirepvp.astralsorcery.common.data.journal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;

public interface JournalPage
{
    public static final int DEFAULT_WIDTH = 175;
    public static final int DEFAULT_HEIGHT = 220;
    
    @OnlyIn(Dist.CLIENT)
    RenderablePage buildRenderPage(final ResearchNode p0, final int p1);
}
