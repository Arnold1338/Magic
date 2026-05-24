package hellfirepvp.astralsorcery.client.screen.journal.page;

import java.util.Iterator;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.network.chat.Component;
import java.util.LinkedList;
import net.minecraft.util.text.LanguageMap;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.util.IReorderingProcessor;
import java.util.List;
import net.minecraft.client.gui.Font;

public class RenderPageText extends RenderablePage
{
    private final FontRenderer fontRenderer;
    private final List<IReorderingProcessor> localizedText;
    
    public RenderPageText(final String unlocalized) {
        this(RenderablePage.getFontRenderer(), unlocalized);
    }
    
    public RenderPageText(final FontRenderer fontRenderer, final String unlocalized) {
        super(null, -1);
        this.fontRenderer = fontRenderer;
        this.localizedText = this.buildLines(unlocalized);
    }
    
    private List<IReorderingProcessor> buildLines(final String unlocText) {
        final String text = LanguageMap.func_74808_a().func_230503_a_(unlocText);
        final List<IReorderingProcessor> lines = new LinkedList<IReorderingProcessor>();
        for (final String segment : text.split("<NL>")) {
            lines.addAll(this.fontRenderer.func_238425_b_((ITextProperties)new Component(segment), 175));
            lines.add(IReorderingProcessor.field_242232_a);
        }
        return lines;
    }
    
    @Override
    public void render(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)x, (double)y, (double)z);
        for (final IReorderingProcessor text : this.localizedText) {
            RenderingDrawUtils.renderStringAt(text, renderStack, this.fontRenderer, 13421772, false);
            renderStack.func_227861_a_(0.0, 10.0, 0.0);
        }
        renderStack.func_227865_b_();
    }
}
