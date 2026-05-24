package hellfirepvp.astralsorcery.client.screen.journal;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.IFormattableTextComponent;
import java.util.Iterator;
import java.util.function.Function;
import java.util.Comparator;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.LanguageMap;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.ITextProperties;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import com.google.common.collect.Maps;
import net.minecraft.network.chat.Component;
import java.awt.Rectangle;
import java.util.Map;
import hellfirepvp.astralsorcery.client.screen.journal.bookmark.BookmarkProvider;
import java.util.List;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;

public class ScreenJournal extends WidthHeightScreen
{
    public static final int NO_BOOKMARK = -1;
    protected static List<BookmarkProvider> bookmarks;
    protected final int bookmarkIndex;
    protected Map<Rectangle, BookmarkProvider> drawnBookmarks;
    
    protected ScreenJournal(final Component titleIn, final int bookmarkIndex) {
        this(titleIn, 270, 420, bookmarkIndex);
    }
    
    public ScreenJournal(final Component titleIn, final int guiHeight, final int guiWidth, final int bookmarkIndex) {
        super(titleIn, guiHeight, guiWidth);
        this.drawnBookmarks = Maps.newHashMap();
        this.bookmarkIndex = bookmarkIndex;
    }
    
    public static boolean addBookmark(final BookmarkProvider bookmarkProvider) {
        final int index = bookmarkProvider.getIndex();
        if (MiscUtils.contains(ScreenJournal.bookmarks, bm -> bm.getIndex() == index)) {
            return false;
        }
        ScreenJournal.bookmarks.add(bookmarkProvider);
        return true;
    }
    
    protected FormattedCharSequence localize(final ITextProperties txt) {
        return LanguageMap.func_74808_a().func_241870_a(txt);
    }
    
    protected void drawDefault(final PoseStack renderStack, final AbstractRenderableTexture texture, final int mouseX, final int mouseY) {
        this.func_230926_e_(100);
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        this.drawWHRect(renderStack, texture);
        RenderSystem.disableBlend();
        this.drawBookmarks(renderStack, mouseX, mouseY);
        this.func_230926_e_(0);
    }
    
    private void drawBookmarks(final PoseStack renderStack, final int mouseX, final int mouseY) {
        this.drawnBookmarks.clear();
        final int bookmarkWidth = 67;
        final int bookmarkHeight = 15;
        final float bookmarkGap = 18.0f;
        final float offsetX = this.guiLeft + this.guiWidth - 17.25f;
        float offsetY = (float)(this.guiTop + 20);
        ScreenJournal.bookmarks.sort(Comparator.comparing((Function<? super BookmarkProvider, ? extends Comparable>)BookmarkProvider::getIndex));
        for (final BookmarkProvider bookmarkProvider : ScreenJournal.bookmarks) {
            if (bookmarkProvider.canSee()) {
                final Rectangle r = this.drawBookmark(renderStack, offsetX, offsetY, bookmarkWidth, bookmarkHeight, bookmarkWidth + ((this.bookmarkIndex == bookmarkProvider.getIndex()) ? 0 : 5), (float)this.getGuiZLevel(), bookmarkProvider.getUnlocalizedName(), -572662307, mouseX, mouseY, bookmarkProvider.getTextureBookmark(), bookmarkProvider.getTextureBookmarkStretched());
                this.drawnBookmarks.put(r, bookmarkProvider);
                offsetY += bookmarkGap;
            }
        }
    }
    
    private Rectangle drawBookmark(final PoseStack renderStack, final float offsetX, final float offsetY, int width, final int height, final int mouseOverWidth, final float zLevel, final IFormattableTextComponent title, final int titleRGBColor, final int mouseX, final int mouseY, final AbstractRenderableTexture texture, final AbstractRenderableTexture textureStretched) {
        texture.bindTexture();
        Rectangle r = new Rectangle(Mth.func_76141_d(offsetX), Mth.func_76141_d(offsetY), Mth.func_76141_d((float)width), Mth.func_76141_d((float)height));
        if (r.contains(mouseX, mouseY)) {
            if (mouseOverWidth > width) {
                textureStretched.bindTexture();
            }
            width = mouseOverWidth;
            r = new Rectangle(Mth.func_76141_d(offsetX), Mth.func_76141_d(offsetY), Mth.func_76141_d((float)width), Mth.func_76141_d((float)height));
        }
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        final int actualWidth = width;
        RenderingUtils.draw(7, DefaultVertexFormat.field_181707_g, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, offsetX, offsetY, zLevel, (float)actualWidth, (float)height).draw());
        RenderSystem.disableBlend();
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(offsetX + 2.0f), (double)(offsetY + 4.0f), (double)(zLevel + 50.0f));
        renderStack.func_227862_a_(0.7f, 0.7f, 0.7f);
        RenderingDrawUtils.renderStringAt(null, renderStack, (ITextProperties)title, titleRGBColor);
        renderStack.func_227865_b_();
        return r;
    }
    
    protected boolean handleBookmarkClick(final double mouseX, final double mouseY) {
        return this.handleJournalNavigationBookmarkClick(mouseX, mouseY);
    }
    
    private boolean handleJournalNavigationBookmarkClick(final double mouseX, final double mouseY) {
        for (final Rectangle bookmarkRectangle : this.drawnBookmarks.keySet()) {
            final BookmarkProvider provider = this.drawnBookmarks.get(bookmarkRectangle);
            if (this.bookmarkIndex != provider.getIndex() && bookmarkRectangle.contains(mouseX, mouseY)) {
                ScreenJournalProgression.resetJournal();
                Minecraft.func_71410_x().func_147108_a(provider.getGuiScreen());
                return true;
            }
        }
        return false;
    }
    
    public boolean func_231177_au__() {
        return false;
    }
    
    static {
        ScreenJournal.bookmarks = Lists.newArrayList();
    }
}
