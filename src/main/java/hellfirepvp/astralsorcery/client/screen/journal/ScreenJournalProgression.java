package hellfirepvp.astralsorcery.client.screen.journal;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.Comparator;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import java.util.Locale;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.util.Iterator;
import net.minecraft.client.gui.Font;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.ITextProperties;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.client.screen.journal.progression.ScreenJournalProgressionRenderer;
import java.util.Map;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.client.util.ScreenTextEntry;

public class ScreenJournalProgression extends ScreenJournal
{
    private static ScreenJournalProgression currentInstance;
    private boolean expectReinit;
    private boolean rescaleAndRefresh;
    private final ScreenTextEntry searchTextEntry;
    private static final int searchEntriesLeft = 15;
    private static final int searchEntriesRight = 14;
    private static final int searchEntryDrawWidth = 170;
    private int searchPageOffset;
    private Rectangle searchPrevRct;
    private Rectangle searchNextRct;
    private ResearchNode searchHoverNode;
    private final List<ResearchNode> searchResult;
    private final Map<Integer, List<ResearchNode>> searchResultPageIndex;
    private static ScreenJournalProgressionRenderer progressionRenderer;
    
    private ScreenJournalProgression() {
        super((Component)new Component("screen.astralsorcery.tome.progression"), 10);
        this.expectReinit = false;
        this.rescaleAndRefresh = true;
        this.searchTextEntry = new ScreenTextEntry();
        this.searchPageOffset = 0;
        this.searchHoverNode = null;
        this.searchResult = new ArrayList<ResearchNode>();
        this.searchResultPageIndex = Maps.newHashMap();
        this.searchTextEntry.setChangeCallback(this::onSearchTextInput);
    }
    
    public static ScreenJournalProgression getJournalInstance() {
        if (ScreenJournalProgression.currentInstance != null) {
            return ScreenJournalProgression.currentInstance;
        }
        return new ScreenJournalProgression();
    }
    
    public static ScreenJournal getOpenJournalInstance() {
        ScreenJournal gui = ScreenJournalPages.getClearOpenGuiInstance();
        if (gui == null) {
            gui = getJournalInstance();
        }
        return gui;
    }
    
    public void expectReInit() {
        this.expectReinit = true;
    }
    
    public void preventRefresh() {
        this.rescaleAndRefresh = false;
    }
    
    public static void resetJournal() {
        ScreenJournalProgression.currentInstance = null;
        ScreenJournalPages.getClearOpenGuiInstance();
    }
    
    public void func_231164_f_() {
        super.func_231164_f_();
        this.rescaleAndRefresh = false;
    }
    
    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        if (this.expectReinit) {
            this.expectReinit = false;
            return;
        }
        if (ScreenJournalProgression.currentInstance == null || ScreenJournalProgression.progressionRenderer == null) {
            ScreenJournalProgression.currentInstance = this;
            (ScreenJournalProgression.progressionRenderer = new ScreenJournalProgressionRenderer(ScreenJournalProgression.currentInstance)).centerMouse();
        }
        ScreenJournalProgression.progressionRenderer.updateOffset(this.guiLeft + 10, this.guiTop + 10);
        ScreenJournalProgression.progressionRenderer.setBox(10, 10, this.guiWidth - 10, this.guiHeight - 10);
        if (this.rescaleAndRefresh) {
            ScreenJournalProgression.progressionRenderer.resetZoom();
            ScreenJournalProgression.progressionRenderer.unfocus();
            ScreenJournalProgression.progressionRenderer.refreshSize();
            ScreenJournalProgression.progressionRenderer.updateMouseState();
        }
        else {
            this.rescaleAndRefresh = true;
        }
    }
    
    private boolean inProgressView() {
        return this.searchTextEntry.getText().length() < 3;
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        this.searchPrevRct = null;
        this.searchNextRct = null;
        this.searchHoverNode = null;
        if (this.inProgressView()) {
            this.searchPageOffset = 0;
            this.renderProgressView(renderStack, mouseX, mouseY, pTicks);
        }
        else {
            this.renderSearchView(renderStack, mouseX, mouseY, pTicks);
        }
    }
    
    private void renderSearchView(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        this.drawDefault(renderStack, TexturesAS.TEX_GUI_BOOK_BLANK, mouseX, mouseY);
        this.func_230926_e_(300);
        this.drawSearchResults(renderStack, mouseX, mouseY, pTicks);
        this.drawSearchBox(renderStack);
        this.func_230926_e_(170);
        this.drawSearchPageNavArrows(renderStack, mouseX, mouseY, pTicks);
        this.func_230926_e_(0);
    }
    
    private void renderProgressView(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        final double guiFactor = Minecraft.getInstance().func_228018_at_().func_198100_s();
        GL11.glEnable(3089);
        GL11.glScissor(Mth.func_76128_c((this.guiLeft + 27) * guiFactor), Mth.func_76128_c((this.guiTop + 27) * guiFactor), Mth.func_76128_c((this.guiWidth - 54) * guiFactor), Mth.func_76128_c((this.guiHeight - 54) * guiFactor));
        ScreenJournalProgression.progressionRenderer.drawProgressionPart(renderStack, (float)this.getGuiZLevel(), mouseX, mouseY);
        GL11.glDisable(3089);
        RenderSystem.disableDepthTest();
        this.drawDefault(renderStack, TexturesAS.TEX_GUI_BOOK_FRAME_FULL, mouseX, mouseY);
        RenderSystem.enableDepthTest();
        this.func_230926_e_(300);
        this.drawSearchBox(renderStack);
        this.func_230926_e_(150);
        this.drawMouseHighlight(renderStack, (float)this.getGuiZLevel(), mouseX, mouseY);
        this.func_230926_e_(0);
    }
    
    private void drawSearchResults(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        final FontRenderer fr = Minecraft.getInstance().field_71466_p;
        final int lineHeight = 12;
        int offsetX = this.getGuiLeft() + 35;
        int offsetY = this.getGuiTop() + 26;
        final double effectPart = (Math.sin(Math.toRadians(ClientScheduler.getClientTick() * 5.0 % 360.0)) + 1.0) / 2.0;
        final int alpha = Math.round((0.45f + 0.1f * (float)effectPart) * 255.0f);
        final int grayScale = Math.round((0.7f + 0.2f * (float)effectPart) * 255.0f);
        final Color boxColor = new Color(grayScale, grayScale, grayScale, alpha);
        List<ResearchNode> entries = this.searchResultPageIndex.getOrDefault(this.searchPageOffset, new ArrayList<ResearchNode>());
        for (final ResearchNode node : entries) {
            final int startOffsetY = offsetY;
            final List<FormattedCharSequence> nodeTitle = fr.func_238425_b_((ITextProperties)node.getName(), 170);
            float maxLength = 0.0f;
            for (final FormattedCharSequence line : nodeTitle) {
                renderStack.func_227860_a_();
                renderStack.func_227861_a_((double)offsetX, (double)offsetY, (double)this.getGuiZLevel());
                final float length = RenderingDrawUtils.renderStringAt(line, renderStack, fr, 13684944, false);
                renderStack.func_227865_b_();
                if (length > maxLength) {
                    maxLength = length;
                }
                offsetY += lineHeight;
            }
            if (this.searchHoverNode == null) {
                final Rectangle rctDrawn = new Rectangle(offsetX - 2, startOffsetY - 2, (int)(maxLength + 4.0f), offsetY - startOffsetY);
                if (!rctDrawn.contains(mouseX, mouseY)) {
                    continue;
                }
                func_238467_a_(renderStack, rctDrawn.x, rctDrawn.y, rctDrawn.x + rctDrawn.width, rctDrawn.y + rctDrawn.height, boxColor.getRGB());
                this.searchHoverNode = node;
            }
        }
        offsetX = this.getGuiLeft() + 225;
        offsetY = this.getGuiTop() + 39;
        entries = this.searchResultPageIndex.getOrDefault(this.searchPageOffset + 1, new ArrayList<ResearchNode>());
        for (final ResearchNode node : entries) {
            final int startOffsetY = offsetY;
            final List<FormattedCharSequence> nodeTitle = fr.func_238425_b_((ITextProperties)node.getName(), 170);
            float maxLength = 0.0f;
            for (final FormattedCharSequence line : nodeTitle) {
                renderStack.func_227860_a_();
                renderStack.func_227861_a_((double)offsetX, (double)offsetY, (double)this.getGuiZLevel());
                final float length = RenderingDrawUtils.renderStringAt(line, renderStack, fr, 13684944, false);
                renderStack.func_227865_b_();
                if (length > maxLength) {
                    maxLength = length;
                }
                offsetY += lineHeight;
            }
            if (this.searchHoverNode == null) {
                final Rectangle rctDrawn = new Rectangle(offsetX - 2, startOffsetY - 2, (int)(maxLength + 4.0f), offsetY - startOffsetY);
                if (!rctDrawn.contains(mouseX, mouseY)) {
                    continue;
                }
                func_238467_a_(renderStack, rctDrawn.x, rctDrawn.y, rctDrawn.x + rctDrawn.width, rctDrawn.y + rctDrawn.height, boxColor.getRGB());
                this.searchHoverNode = node;
            }
        }
    }
    
    private void drawMouseHighlight(final PoseStack renderStack, final float zLevel, final int mouseX, final int mouseY) {
        ScreenJournalProgression.progressionRenderer.drawMouseHighlight(renderStack, zLevel, mouseX, mouseY);
    }
    
    private void drawSearchBox(final PoseStack renderStack) {
        TexturesAS.TEX_GUI_TEXT_FIELD.bindTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(this.guiLeft + 300), (float)(this.guiTop + 16), (float)this.getGuiZLevel(), 88.5f, 15.0f).draw());
        RenderSystem.disableBlend();
        String text = this.searchTextEntry.getText();
        int length = this.field_230712_o_.func_78256_a(text);
        final boolean addDots = length > 75;
        while (length > 75) {
            text = text.substring(1);
            length = this.field_230712_o_.func_78256_a("..." + text);
        }
        if (addDots) {
            text = "..." + text;
        }
        if (ClientScheduler.getClientTick() % 20L > 10L) {
            text += "_";
        }
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(this.guiLeft + 304), (double)(this.guiTop + 20), (double)this.getGuiZLevel());
        RenderingDrawUtils.renderStringAt(this.field_230712_o_, renderStack, (ITextProperties)new Component(text), 13421772);
        renderStack.func_227865_b_();
    }
    
    private void drawSearchPageNavArrows(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        int width = 0;
        int height = 0;
        float vFrom = 0.0f;
        float uFrom = 0.0f;
        if (this.searchPageOffset > 0) {
            width = 30;
            height = 15;
            this.searchPrevRct = new Rectangle(this.guiLeft + 25, this.guiTop + 220, width, height);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(this.searchPrevRct.getX() + width / 2.0f, this.searchPrevRct.getY() + height / 2.0f, (double)this.getGuiZLevel());
            vFrom = 0.5f;
            if (this.searchPrevRct.contains(mouseX, mouseY)) {
                uFrom = 0.5f;
                renderStack.func_227862_a_(1.1f, 1.1f, 1.0f);
            }
            else {
                uFrom = 0.0f;
                final double t = ClientScheduler.getClientTick() + pTicks;
                final float sin = (float)Math.sin(t / 4.0) / 32.0f + 1.0f;
                renderStack.func_227862_a_(sin, sin, 1.0f);
            }
            renderStack.func_227861_a_((double)(-(width / 2.0f)), (double)(-(height / 2.0f)), 0.0);
            TexturesAS.TEX_GUI_BOOK_ARROWS.bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, 0.0f, 0.0f, 0.0f, (float)width, (float)height).tex(uFrom, vFrom, 0.5f, 0.5f).color(1.0f, 1.0f, 1.0f, 0.8f).draw());
            renderStack.func_227865_b_();
        }
        final int nextDoublePageIndex = this.searchPageOffset * 2 + 2;
        if (this.searchResultPageIndex.size() >= nextDoublePageIndex + 1) {
            final int width2 = 30;
            final int height2 = 15;
            this.searchNextRct = new Rectangle(this.guiLeft + 367, this.guiTop + 220, width2, height2);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(this.searchNextRct.getX() + width2 / 2.0f, this.searchNextRct.getY() + height2 / 2.0f, (double)this.getGuiZLevel());
            final float vFrom2 = 0.0f;
            if (this.searchNextRct.contains(mouseX, mouseY)) {
                final float uFrom2 = 0.5f;
                renderStack.func_227862_a_(1.1f, 1.1f, 1.0f);
            }
            else {
                final float uFrom2 = 0.0f;
                final double t2 = ClientScheduler.getClientTick() + pTicks;
                final float sin2 = (float)Math.sin(t2 / 4.0) / 32.0f + 1.0f;
                renderStack.func_227862_a_(sin2, sin2, 1.0f);
            }
            renderStack.func_227861_a_((double)(-(width2 / 2.0f)), (double)(-(height2 / 2.0f)), 0.0);
            TexturesAS.TEX_GUI_BOOK_ARROWS.bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, 0.0f, 0.0f, 0.0f, (float)width, (float)height).tex(uFrom, vFrom, 0.5f, 0.5f).color(1.0f, 1.0f, 1.0f, 0.8f).draw());
            renderStack.func_227865_b_();
        }
    }
    
    private void onSearchTextInput() {
        if (!this.inProgressView() && this.isCurrentlyDragging()) {
            this.stopDragging(-1.0, -1.0);
            ScreenJournalProgression.progressionRenderer.applyMovedMouseOffset();
        }
        final PlayerProgress prog = ResearchHelper.getClientProgress();
        this.searchResult.clear();
        this.searchResultPageIndex.clear();
        final String searchText = this.searchTextEntry.getText().toLowerCase(Locale.ROOT);
        ResearchNode node = null;
        for (final ResearchProgression research : ResearchProgression.values()) {
            if (prog.hasResearch(research)) {
                final Iterator<ResearchNode> iterator = research.getResearchNodes().iterator();
                while (iterator.hasNext()) {
                    node = iterator.next();
                    if (node.getName().getString().toLowerCase(Locale.ROOT).contains(searchText) && !this.searchResult.contains(node)) {
                        this.searchResult.add(node);
                    }
                }
            }
        }
        this.searchResult.sort(Comparator.comparing(node -> node.getName().getString()));
        final FontRenderer fr = Minecraft.getInstance().field_71466_p;
        int addedPages = 0;
        int pageIndex = 0;
        while (addedPages < this.searchResult.size()) {
            final List<ResearchNode> page = this.searchResultPageIndex.computeIfAbsent(Integer.valueOf(pageIndex), index -> new ArrayList());
            final int remainingLines = ((pageIndex % 2 == 0) ? 15 : 14) - page.size();
            final ResearchNode toAddNode = this.searchResult.get(addedPages);
            final int lines = fr.func_238425_b_((ITextProperties)toAddNode.getName(), 170).size();
            if (remainingLines < lines) {
                ++pageIndex;
            }
            else {
                page.add(toAddNode);
                ++addedPages;
            }
        }
        while (this.searchPageOffset > 0 && this.searchPageOffset >= this.searchResultPageIndex.size()) {
            --this.searchPageOffset;
        }
    }
    
    @Override
    protected void mouseDragTick(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY, final double mouseOffsetX, final double mouseOffsetY) {
        super.mouseDragTick(mouseX, mouseY, mouseDiffX, mouseDiffY, mouseOffsetX, mouseOffsetY);
        if (this.inProgressView()) {
            ScreenJournalProgression.progressionRenderer.moveMouse((float)mouseDiffX, (float)mouseDiffY);
        }
    }
    
    @Override
    protected void mouseDragStop(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY) {
        super.mouseDragStop(mouseX, mouseY, mouseDiffX, mouseDiffY);
        if (this.inProgressView()) {
            ScreenJournalProgression.progressionRenderer.applyMovedMouseOffset();
        }
    }
    
    public boolean func_231043_a_(final double mouseX, final double mouseY, final double scroll) {
        if (this.inProgressView()) {
            if (scroll < 0.0) {
                ScreenJournalProgression.progressionRenderer.handleZoomOut();
                return true;
            }
            if (scroll > 0.0) {
                ScreenJournalProgression.progressionRenderer.handleZoomIn((float)mouseX, (float)mouseY);
                return true;
            }
        }
        return false;
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
        if (this.inProgressView()) {
            return ScreenJournalProgression.progressionRenderer.propagateClick((float)mouseX, (float)mouseY);
        }
        if (this.searchPrevRct != null && this.searchPrevRct.contains(mouseX, mouseY)) {
            --this.searchPageOffset;
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        if (this.searchNextRct != null && this.searchNextRct.contains(mouseX, mouseY)) {
            ++this.searchPageOffset;
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        if (this.searchHoverNode != null) {
            this.searchTextEntry.setText("");
            Minecraft.getInstance().func_147108_a((Screen)new ScreenJournalPages(this, this.searchHoverNode));
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return true;
    }
    
    @Override
    public boolean func_231046_a_(final int key, final int scanCode, final int modifiers) {
        return this.searchTextEntry.keyTyped(key) || super.func_231046_a_(key, scanCode, modifiers);
    }
    
    @Override
    public boolean func_231042_a_(final char charCode, final int keyModifiers) {
        return this.searchTextEntry.charTyped(charCode) || super.func_231042_a_(charCode, keyModifiers);
    }
    
    static {
        ScreenJournalProgression.currentInstance = null;
    }
}
