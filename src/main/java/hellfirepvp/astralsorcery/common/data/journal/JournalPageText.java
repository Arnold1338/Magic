package hellfirepvp.astralsorcery.common.data.journal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageText;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;

public class JournalPageText implements JournalPage
{
    private final String unlocalizedKey;
    
    public JournalPageText(final String unlocalizedKey) {
        this.unlocalizedKey = unlocalizedKey;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderablePage buildRenderPage(final ResearchNode node, final int nodePage) {
        return new RenderPageText(this.unlocalizedKey);
    }
}
