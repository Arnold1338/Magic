package hellfirepvp.astralsorcery.client.screen.journal.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;

public abstract class RenderablePage
{
    @Nullable
    private final ResearchNode node;
    private final int nodePage;
    
    public RenderablePage(@Nullable final ResearchNode node, final int nodePage) {
        this.node = node;
        this.nodePage = nodePage;
    }
    
    public abstract void render(final PoseStack p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    public void postRender(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
    }
    
    public boolean propagateMouseClick(final double mouseX, final double mouseZ) {
        return false;
    }
    
    public boolean propagateMouseDrag(final double mouseDX, final double mouseDZ) {
        return false;
    }
    
    public static FontRenderer getFontRenderer() {
        return Minecraft.getInstance().font;
    }
    
    @Nullable
    protected final ResearchNode getResearchNode() {
        return this.node;
    }
    
    protected final int getNodePage() {
        return this.nodePage;
    }
}
