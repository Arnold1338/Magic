package hellfirepvp.astralsorcery.common.data.journal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageEmpty;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;

public class JournalPageEmpty implements JournalPage
{
    public static final JournalPageEmpty INSTANCE;
    
    private JournalPageEmpty() {
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderablePage buildRenderPage(final ResearchNode node, final int nodePage) {
        return RenderPageEmpty.INSTANCE;
    }
    
    static {
        INSTANCE = new JournalPageEmpty();
    }
}
