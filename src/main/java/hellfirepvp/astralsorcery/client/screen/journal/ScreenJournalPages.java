package hellfirepvp.astralsorcery.client.screen.journal;

import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.util.text.ITextProperties;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.data.journal.JournalPage;
import java.util.ArrayList;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.client.gui.screens.Screen;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.screen.base.NavigationArrowScreen;

public class ScreenJournalPages extends ScreenJournal implements NavigationArrowScreen
{
    private static ScreenJournalPages openGuiInstance;
    private static boolean saveSite;
    @Nullable
    private final ScreenJournalProgression origin;
    @Nullable
    private final Screen previous;
    private final ResearchNode researchNode;
    private final List<RenderablePage> pages;
    private boolean informPreviousClose;
    private int currentPageOffset;
    private Rectangle rectBack;
    private Rectangle rectNext;
    private Rectangle rectPrev;
    
    public ScreenJournalPages(@Nullable final ScreenJournalProgression origin, final ResearchNode node) {
        super(node.getName(), -1);
        this.informPreviousClose = true;
        this.currentPageOffset = 0;
        this.researchNode = node;
        this.origin = origin;
        this.previous = null;
        final List<JournalPage> pageList = node.getPages();
        this.pages = new ArrayList<RenderablePage>(pageList.size());
        for (int i = 0; i < pageList.size(); ++i) {
            this.pages.add(pageList.get(i).buildRenderPage(node, i));
        }
    }
    
    public ScreenJournalPages(@Nullable final Screen previous, final ResearchNode detailedInformation, final int exactPage) {
        super(detailedInformation.getName(), -1);
        this.informPreviousClose = true;
        this.currentPageOffset = 0;
        this.researchNode = detailedInformation;
        this.origin = null;
        this.previous = previous;
        this.currentPageOffset = exactPage / 2;
        final List<JournalPage> pageList = detailedInformation.getPages();
        this.pages = new ArrayList<RenderablePage>(pageList.size());
        for (int i = 0; i < pageList.size(); ++i) {
            this.pages.add(pageList.get(i).buildRenderPage(detailedInformation, i));
        }
    }
    
    public static ScreenJournalPages getClearOpenGuiInstance() {
        final ScreenJournalPages gui = ScreenJournalPages.openGuiInstance;
        ScreenJournalPages.openGuiInstance = null;
        return gui;
    }
    
    public int getCurrentPageOffset() {
        return this.currentPageOffset;
    }
    
    public ResearchNode getResearchNode() {
        return this.researchNode;
    }
    
    public void func_231160_c_() {
        super.func_231160_c_();
        if (this.origin != null) {
            this.origin.preventRefresh();
            this.origin.field_230708_k_ = this.field_230708_k_;
            this.origin.field_230709_l_ = this.field_230709_l_;
            this.origin.func_231160_c_();
        }
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        if (this.origin != null) {
            this.drawDefault(renderStack, TexturesAS.TEX_GUI_BOOK_BLANK, mouseX, mouseY);
        }
        else {
            RenderSystem.enableBlend();
            Blending.DEFAULT.apply();
            this.drawWHRect(renderStack, TexturesAS.TEX_GUI_BOOK_BLANK);
            RenderSystem.disableBlend();
        }
        this.func_230926_e_(100);
        int pageYOffset = 20;
        if (this.currentPageOffset == 0) {
            final int width = this.field_230712_o_.func_238414_a_((ITextProperties)this.func_231171_q_());
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(this.guiLeft + 117), (double)(this.guiTop + 22), (double)this.getGuiZLevel());
            renderStack.func_227862_a_(1.3f, 1.3f, 1.0f);
            renderStack.func_227861_a_((double)(-width / 2.0f), 0.0, 0.0);
            RenderingDrawUtils.renderStringAt(this.field_230712_o_, renderStack, (ITextProperties)this.func_231171_q_(), 14540253);
            renderStack.func_227865_b_();
            RenderSystem.enableBlend();
            Blending.DEFAULT.apply();
            TexturesAS.TEX_GUI_BOOK_UNDERLINE.bindTexture();
            RenderingGuiUtils.drawRect(renderStack, (float)(this.guiLeft + 30), (float)(this.guiTop + 35), (float)this.getGuiZLevel(), 175.0f, 6.0f);
            RenderSystem.disableBlend();
            pageYOffset += 30;
        }
        int index = this.currentPageOffset * 2;
        if (this.pages.size() > index) {
            final RenderablePage page = this.pages.get(index);
            page.render(renderStack, (float)(this.guiLeft + 30), (float)(this.guiTop + pageYOffset), (float)this.getGuiZLevel(), pTicks, (float)mouseX, (float)mouseY);
        }
        ++index;
        if (this.pages.size() > index) {
            final RenderablePage page = this.pages.get(index);
            page.render(renderStack, (float)(this.guiLeft + 220), (float)(this.guiTop + 20), (float)this.getGuiZLevel(), pTicks, (float)mouseX, (float)mouseY);
        }
        this.func_230926_e_(120);
        this.drawNavArrows(renderStack, pTicks, mouseX, mouseY);
        this.func_230926_e_(100);
        index = this.currentPageOffset * 2;
        if (this.pages.size() > index) {
            final RenderablePage page = this.pages.get(index);
            page.postRender(renderStack, (float)(this.guiLeft + 30), (float)(this.guiTop + pageYOffset), (float)this.getGuiZLevel(), pTicks, (float)mouseX, (float)mouseY);
        }
        ++index;
        if (this.pages.size() > index) {
            final RenderablePage page = this.pages.get(index);
            page.postRender(renderStack, (float)(this.guiLeft + 220), (float)(this.guiTop + 20), (float)this.getGuiZLevel(), pTicks, (float)mouseX, (float)mouseY);
        }
        this.func_230926_e_(0);
    }
    
    private void drawNavArrows(final PoseStack renderStack, final float partialTicks, final int mouseX, final int mouseY) {
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        this.rectNext = null;
        this.rectPrev = null;
        this.rectBack = this.drawArrow(renderStack, this.guiLeft + 197, this.guiTop + 230, this.getGuiZLevel(), Type.LEFT, mouseX, mouseY, partialTicks);
        final int cIndex = this.currentPageOffset * 2;
        if (cIndex > 0) {
            this.rectPrev = this.drawArrow(renderStack, this.guiLeft + 25, this.guiTop + 220, this.getGuiZLevel(), Type.LEFT, mouseX, mouseY, partialTicks);
        }
        final int nextIndex = cIndex + 2;
        if (this.pages.size() >= nextIndex + 1) {
            this.rectNext = this.drawArrow(renderStack, this.guiLeft + 367, this.guiTop + 220, this.getGuiZLevel(), Type.RIGHT, mouseX, mouseY, partialTicks);
        }
        RenderSystem.disableBlend();
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        if (this.origin != null) {
            this.origin.expectReInit();
            ScreenJournalPages.saveSite = false;
        }
        else {
            this.informPreviousClose = false;
        }
        return true;
    }
    
    public void func_231175_as__() {
        if (this.origin != null) {
            if (ScreenJournalPages.saveSite) {
                ScreenJournalPages.openGuiInstance = this;
                ScreenJournalProgression.getJournalInstance().preventRefresh();
                Minecraft.func_71410_x().func_147108_a((Screen)null);
            }
            else {
                ScreenJournalPages.saveSite = true;
                ScreenJournalPages.openGuiInstance = null;
                Minecraft.func_71410_x().func_147108_a((Screen)this.origin);
            }
        }
        else {
            if (this.previous != null && this.informPreviousClose) {
                this.previous.func_231175_as__();
            }
            Minecraft.func_71410_x().func_147108_a(this.previous);
        }
    }
    
    @Override
    protected void mouseDragTick(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY, final double mouseOffsetX, final double mouseOffsetY) {
        int index = this.currentPageOffset * 2;
        if (this.pages.size() > index) {
            final RenderablePage page = this.pages.get(index);
            if (page != null && page.propagateMouseDrag(mouseOffsetX, mouseOffsetY)) {
                return;
            }
        }
        ++index;
        if (this.pages.size() > index) {
            final RenderablePage page = this.pages.get(index);
            if (page != null) {
                page.propagateMouseDrag(mouseOffsetX, mouseOffsetY);
            }
        }
    }
    
    @Override
    public boolean func_231044_a_(final double mouseX, final double mouseY, final int mouseButton) {
        if (super.func_231044_a_(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (mouseButton == 1) {
            return true;
        }
        if (mouseButton != 0) {
            return false;
        }
        if (this.origin != null && this.handleBookmarkClick(mouseX, mouseY)) {
            ScreenJournalPages.saveSite = false;
            return true;
        }
        if (this.rectBack != null && this.rectBack.contains(mouseX, mouseY)) {
            if (this.origin != null) {
                this.origin.expectReInit();
                ScreenJournalPages.saveSite = false;
                this.func_231175_as__();
                return true;
            }
            this.informPreviousClose = false;
            this.func_231175_as__();
            return true;
        }
        else {
            if (this.rectPrev != null && this.rectPrev.contains(mouseX, mouseY)) {
                --this.currentPageOffset;
                SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
                return true;
            }
            if (this.rectNext != null && this.rectNext.contains(mouseX, mouseY)) {
                ++this.currentPageOffset;
                SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
                return true;
            }
            int index = this.currentPageOffset * 2;
            if (this.pages.size() > index) {
                final RenderablePage page = this.pages.get(index);
                if (page != null && page.propagateMouseClick(mouseX, mouseY)) {
                    return true;
                }
            }
            ++index;
            if (this.pages.size() > index) {
                final RenderablePage page = this.pages.get(index);
                if (page != null && page.propagateMouseClick(mouseX, mouseY)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    static {
        ScreenJournalPages.saveSite = true;
    }
}
