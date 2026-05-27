package hellfirepvp.astralsorcery.client.screen.journal;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Iterator;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FormattedCharSequence;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.util.Random;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import java.util.HashMap;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.chat.Component;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.List;
import java.awt.Point;
import java.util.Map;
import hellfirepvp.astralsorcery.client.screen.base.NavigationArrowScreen;

public class ScreenJournalConstellationOverview extends ScreenJournal implements NavigationArrowScreen
{
    private static final int CONSTELLATIONS_PER_PAGE = 4;
    private static final int width = 80;
    private static final int height = 110;
    private static final Map<Integer, Point> offsetMap;
    private final List<IConstellation> constellations;
    private final int pageId;
    private final Map<Rectangle, IConstellation> rectCRenderMap;
    private Rectangle rectPrev;
    private Rectangle rectNext;
    
    private ScreenJournalConstellationOverview(final int pageId, final List<IConstellation> constellations) {
        super((ITextComponent)Component.translatable("screen.astralsorcery.tome.constellations"), 20);
        this.rectCRenderMap = new HashMap<Rectangle, IConstellation>();
        this.constellations = constellations;
        this.pageId = pageId;
    }
    
    private ScreenJournalConstellationOverview(final List<IConstellation> constellations) {
        this(0, constellations);
    }
    
    public static ScreenJournal getConstellationScreen() {
        final PlayerProgress client = ResearchHelper.getClientProgress();
        return new ScreenJournalConstellationOverview((List<IConstellation>)client.getSeenConstellations().stream().map((Function<? super Object, ?>)ConstellationRegistry::getConstellation).filter(Objects::nonNull).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        this.drawConstellationBackground(renderStack);
        this.drawDefault(renderStack, TexturesAS.TEX_GUI_BOOK_FRAME_FULL, mouseX, mouseY);
        this.func_230926_e_(250);
        this.drawNavArrows(renderStack, pTicks, mouseX, mouseY);
        this.drawConstellations(renderStack, pTicks, mouseX, mouseY);
        this.func_230926_e_(0);
    }
    
    private void drawConstellationBackground(final PoseStack renderStack) {
        TexturesAS.TEX_BLACK.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            final Matrix4f offset = renderStack.last().func_227870_a_();
            buf.vertex(offset, (float)(this.guiLeft + 15), (float)(this.guiTop + this.guiHeight - 10), (float)this.getGuiZLevel()).color(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(0.0f, 1.0f).endVertex();
            buf.vertex(offset, (float)(this.guiLeft + this.guiWidth - 15), (float)(this.guiTop + this.guiHeight - 10), (float)this.getGuiZLevel()).color(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(1.0f, 1.0f).endVertex();
            buf.vertex(offset, (float)(this.guiLeft + this.guiWidth - 15), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).color(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(1.0f, 0.0f).endVertex();
            buf.vertex(offset, (float)(this.guiLeft + 15), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).color(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(0.0f, 0.0f).endVertex();
            return;
        });
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        TexturesAS.TEX_GUI_BACKGROUND_CONSTELLATIONS.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            final Matrix4f offset2 = renderStack.last().func_227870_a_();
            buf.vertex(offset2, (float)(this.guiLeft + 15), (float)(this.guiTop + this.guiHeight - 10), (float)this.getGuiZLevel()).color(0.8f, 0.8f, 1.0f, 0.7f).func_225583_a_(0.1f, 0.9f).endVertex();
            buf.vertex(offset2, (float)(this.guiLeft + this.guiWidth - 15), (float)(this.guiTop + this.guiHeight - 10), (float)this.getGuiZLevel()).color(0.8f, 0.8f, 1.0f, 0.7f).func_225583_a_(0.9f, 0.9f).endVertex();
            buf.vertex(offset2, (float)(this.guiLeft + this.guiWidth - 15), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).color(0.8f, 0.8f, 1.0f, 0.7f).func_225583_a_(0.9f, 0.1f).endVertex();
            buf.vertex(offset2, (float)(this.guiLeft + 15), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).color(0.8f, 0.8f, 1.0f, 0.7f).func_225583_a_(0.1f, 0.1f).endVertex();
            return;
        });
        RenderSystem.disableBlend();
    }
    
    private void drawConstellations(final PoseStack renderStack, final float partial, final int mouseX, final int mouseY) {
        this.rectCRenderMap.clear();
        final List<IConstellation> cs = this.constellations.subList(this.pageId * 4, Math.min((this.pageId + 1) * 4, this.constellations.size()));
        for (int i = 0; i < cs.size(); ++i) {
            final IConstellation c = cs.get(i);
            final Point p = ScreenJournalConstellationOverview.offsetMap.get(i);
            final Rectangle cstRct = this.drawConstellation(renderStack, c, this.guiLeft + p.x, this.guiTop + p.y, (float)this.getGuiZLevel(), partial, mouseX, mouseY);
            this.rectCRenderMap.put(cstRct, c);
        }
    }
    
    private Rectangle drawConstellation(final PoseStack renderStack, final IConstellation display, final double offsetX, final double offsetY, final float zLevel, final float partial, final int mouseX, final int mouseY) {
        final Rectangle rect = new Rectangle(Mth.func_76128_c(offsetX), Mth.func_76128_c(offsetY), 80, 110);
        renderStack.popPose();
        renderStack.func_227861_a_(offsetX + 40.0, offsetY + 40.0, (double)zLevel);
        if (rect.contains(mouseX, mouseY)) {
            renderStack.translate(1.1f, 1.1f, 1.0f);
        }
        renderStack.func_227861_a_(-40.0, -40.0, (double)zLevel);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final Random rand = new Random(4726142277924544921L);
        RenderingConstellationUtils.renderConstellationIntoGUI(display.getConstellationColor(), display, renderStack, 0.0f, 0.0f, 0.0f, 95.0f, 95.0f, 1.600000023841858, () -> 0.5f + 0.5f * RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), partial, 12 + rand.nextInt(10)), true, false);
        RenderSystem.disableBlend();
        final FormattedCharSequence cstName = (FormattedCharSequence)display.getConstellationName();
        final float fullLength = 40.0f - this.field_230712_o_.func_238414_a_(cstName) / 2.0f;
        renderStack.func_227861_a_((double)fullLength, 90.0, 10.0);
        RenderingDrawUtils.renderStringAt(cstName, renderStack, this.field_230712_o_, -1143087651, false);
        renderStack.scale();
        return rect;
    }
    
    private void drawNavArrows(final PoseStack renderStack, final float partialTicks, final int mouseX, final int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        this.rectNext = null;
        this.rectPrev = null;
        final int cIndex = this.pageId * 4;
        if (cIndex > 0) {
            this.rectPrev = this.drawArrow(renderStack, this.guiLeft + 15, this.guiTop + 127, this.getGuiZLevel(), Type.LEFT, mouseX, mouseY, partialTicks);
        }
        final int nextIndex = cIndex + 4;
        if (this.constellations.size() >= nextIndex + 1) {
            this.rectNext = this.drawArrow(renderStack, this.guiLeft + 367, this.guiTop + 127, this.getGuiZLevel(), Type.RIGHT, mouseX, mouseY, partialTicks);
        }
        RenderSystem.disableBlend();
    }
    
    @Override
    public boolean func_231044_a_(final double mouseX, final double mouseY, final int mouseButton) {
        if (super.func_231044_a_(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (mouseButton != 0) {
            return false;
        }
        if (this.handleBookmarkClick(mouseX, mouseY)) {
            return true;
        }
        for (final Rectangle r : this.rectCRenderMap.keySet()) {
            if (r.contains(mouseX, mouseY)) {
                final IConstellation c = this.rectCRenderMap.get(r);
                Minecraft.getInstance().func_147108_a((Screen)new ScreenJournalConstellationDetail(this, c));
            }
        }
        if (this.rectPrev != null && this.rectPrev.contains(mouseX, mouseY)) {
            Minecraft.getInstance().func_147108_a((Screen)new ScreenJournalConstellationOverview(this.pageId - 1, this.constellations));
            return true;
        }
        if (this.rectNext != null && this.rectNext.contains(mouseX, mouseY)) {
            Minecraft.getInstance().func_147108_a((Screen)new ScreenJournalConstellationOverview(this.pageId + 1, this.constellations));
            return true;
        }
        return false;
    }
    
    static {
        (offsetMap = new HashMap<Integer, Point>()).put(0, new Point(45, 55));
        ScreenJournalConstellationOverview.offsetMap.put(1, new Point(125, 105));
        ScreenJournalConstellationOverview.offsetMap.put(2, new Point(200, 45));
        ScreenJournalConstellationOverview.offsetMap.put(3, new Point(280, 110));
    }
}
