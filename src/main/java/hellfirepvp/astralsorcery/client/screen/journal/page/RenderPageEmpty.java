package hellfirepvp.astralsorcery.client.screen.journal.page;

import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;

public class RenderPageEmpty extends RenderablePage
{
    public static final RenderPageEmpty INSTANCE;
    
    private RenderPageEmpty() {
        super(null, -1);
    }
    
    @Override
    public void render(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
    }
    
    static {
        INSTANCE = new RenderPageEmpty();
    }
}
