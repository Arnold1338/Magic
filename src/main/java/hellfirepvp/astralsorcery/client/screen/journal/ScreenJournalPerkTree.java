package hellfirepvp.astralsorcery.client.screen.journal;

import net.minecraft.util.SoundEvent;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.util.SoundEvents;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.network.play.client.PktPerkGemModification;
import net.minecraft.util.Util;
import net.minecraft.client.gui.screens.Screen;
import hellfirepvp.astralsorcery.client.screen.journal.overlay.ScreenJournalOverlayPerkStatistics;
import hellfirepvp.astralsorcery.common.network.play.client.PktUnlockPerk;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.client.PktRequestPerkSealAction;
import hellfirepvp.astralsorcery.common.perk.ProgressGatedPerk;
import java.util.Locale;
import java.awt.Color;
import org.joml.Matrix4f;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.util.draw.BufferContext;
import net.minecraft.client.renderer.BufferBuilder;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.common.perk.source.AttributeConverterProvider;
import hellfirepvp.astralsorcery.client.screen.journal.perk.DynamicPerkRender;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.common.perk.AllocationStatus;
import hellfirepvp.astralsorcery.common.data.research.PerkAllocationType;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.perk.node.socket.GemSocketItem;
import net.minecraft.util.text.Style;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.MutableComponent;
import java.util.LinkedList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.util.FormattedCharSequence;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import net.minecraft.util.IItemProvider;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.item.useables.ItemPerkSeal;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import java.util.Iterator;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkRenderGroup;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkTreeSizeHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.perk.node.socket.GemSocketPerk;
import java.util.List;
import hellfirepvp.astralsorcery.client.util.ScreenTextEntry;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.client.screen.helper.ScalingPoint;
import hellfirepvp.astralsorcery.client.screen.helper.ScreenRenderBoundingBox;
import hellfirepvp.astralsorcery.client.screen.helper.SizeHandler;
import hellfirepvp.astralsorcery.client.screen.journal.perk.BatchPerkContext;
import java.awt.Rectangle;

public class ScreenJournalPerkTree extends ScreenJournal
{
    private static final Rectangle rectSealBox;
    private static final Rectangle rectSearchTextEntry;
    private static Long lastPreparedBuffer;
    private static BatchPerkContext drawBuffer;
    private static BatchPerkContext.TextureObjectGroup searchContext;
    private static BatchPerkContext.TextureObjectGroup sealContext;
    private SizeHandler sizeHandler;
    private ScreenRenderBoundingBox guiBox;
    private ScalingPoint mousePosition;
    private ScalingPoint previousMousePosition;
    private AbstractPerk unlockPrimed;
    private AbstractPerk sealBreakPrimed;
    private int tickSealBreak;
    private int guiOffsetX;
    private int guiOffsetY;
    public boolean expectReinit;
    private final Map<AbstractPerk, Rectangle2D.Float> thisFramePerks;
    private final Map<AbstractPerk, Long> unlockEffects;
    private final Map<AbstractPerk, Long> breakEffects;
    private final ScreenTextEntry searchTextEntry;
    private final List<AbstractPerk> searchMatches;
    private GemSocketPerk socketMenu;
    private Rectangle2D.Float rSocketMenu;
    private final Map<Rectangle2D.Float, Integer> slotsSocketMenu;
    private Rectangle rStatStar;
    private ItemStack mouseSealStack;
    private ItemStack foundSeals;
    
    public ScreenJournalPerkTree() {
        super((ITextComponent)Component.translatable("screen.astralsorcery.tome.perks"), 30);
        this.unlockPrimed = null;
        this.sealBreakPrimed = null;
        this.tickSealBreak = 0;
        this.expectReinit = false;
        this.thisFramePerks = Maps.newHashMap();
        this.unlockEffects = Maps.newHashMap();
        this.breakEffects = Maps.newHashMap();
        this.searchTextEntry = new ScreenTextEntry();
        this.searchMatches = Lists.newArrayList();
        this.socketMenu = null;
        this.rSocketMenu = null;
        this.slotsSocketMenu = Maps.newHashMap();
        this.rStatStar = null;
        this.mouseSealStack = ItemStack.field_190927_a;
        this.foundSeals = ItemStack.field_190927_a;
        this.closeWithInventoryKey = false;
        this.searchTextEntry.setChangeCallback(this::updateSearchHighlight);
        this.buildTree();
    }
    
    private void buildTree() {
        this.guiBox = new ScreenRenderBoundingBox(10.0, 10.0, this.guiWidth - 10, this.guiHeight - 10);
        (this.sizeHandler = new PerkTreeSizeHandler()).setScaleSpeed(0.04f);
        this.sizeHandler.setMaxScale(1.0f);
        this.sizeHandler.setMinScale(0.1f);
        this.sizeHandler.updateSize();
        this.mousePosition = ScalingPoint.createPoint(0.0f, 0.0f, this.sizeHandler.getScalingFactor(), false);
    }
    
    public static void refreshDrawBuffer() {
        ScreenJournalPerkTree.lastPreparedBuffer = null;
    }
    
    public static void initializeDrawBuffer() {
        PerkTree.PERK_TREE.getVersion(LogicalSide.CLIENT).ifPresent(version -> {
            if (ScreenJournalPerkTree.lastPreparedBuffer == null || version != (long)ScreenJournalPerkTree.lastPreparedBuffer) {
                ScreenJournalPerkTree.drawBuffer = new BatchPerkContext();
                ScreenJournalPerkTree.searchContext = ScreenJournalPerkTree.drawBuffer.addContext(SpritesAS.SPR_PERK_SEARCH, 300);
                ScreenJournalPerkTree.sealContext = ScreenJournalPerkTree.drawBuffer.addContext(SpritesAS.SPR_PERK_SEAL, 200);
                final ArrayList groups = Lists.newArrayList();
                PerkTree.PERK_TREE.getPerkPoints(LogicalSide.CLIENT).iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final PerkTreePoint<?> p = iterator.next();
                    p.addGroups(groups);
                }
                groups.iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final PerkRenderGroup group = iterator2.next();
                    group.batchRegister(ScreenJournalPerkTree.drawBuffer);
                }
                ScreenJournalPerkTree.lastPreparedBuffer = version;
            }
        });
    }
    
    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        if (this.expectReinit) {
            this.expectReinit = false;
            return;
        }
        this.guiOffsetX = this.guiLeft + 10;
        this.guiOffsetY = this.guiTop + 10;
        boolean shifted = false;
        final PlayerProgress progress = ResearchHelper.getClientProgress();
        final IMajorConstellation attunement = progress.getAttunedConstellation();
        if (attunement != null) {
            final AbstractPerk root = PerkTree.PERK_TREE.getRootPerk(LogicalSide.CLIENT, attunement);
            if (root != null) {
                final Point2D.Float shift = this.sizeHandler.evRelativePos(root.getOffset());
                this.moveMouse((float)MathHelper.func_76141_d(shift.x), (float)MathHelper.func_76141_d(shift.y));
                shifted = true;
            }
        }
        if (!shifted) {
            this.moveMouse((float)MathHelper.func_76141_d(this.sizeHandler.getTotalWidth() / 2.0f), (float)MathHelper.func_76141_d(this.sizeHandler.getTotalHeight() / 2.0f));
        }
        this.applyMovedMouseOffset();
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        initializeDrawBuffer();
        this.thisFramePerks.clear();
        final double guiFactor = Minecraft.getInstance().func_228018_at_().func_198100_s();
        GL11.glEnable(3089);
        GL11.glScissor(MathHelper.func_76128_c((this.guiLeft + 39) * guiFactor), MathHelper.func_76128_c((this.guiTop + 44) * guiFactor), MathHelper.func_76128_c((this.guiWidth - 76) * guiFactor), MathHelper.func_76128_c((this.guiHeight - 71) * guiFactor));
        this.func_230926_e_(-50);
        this.drawBackground(renderStack);
        this.func_230926_e_(0);
        this.drawPerkTree(renderStack, pTicks);
        GL11.glDisable(3089);
        RenderSystem.depthMask(false);
        this.drawDefault(renderStack, TexturesAS.TEX_GUI_BOOK_FRAME_FULL, mouseX, mouseY);
        RenderSystem.depthMask(true);
        this.drawSearchBox(renderStack);
        this.drawMiscInfo(renderStack, mouseX, mouseY, pTicks);
        this.drawSocketContextMenu(renderStack);
        this.drawSealBox(renderStack);
        this.func_230926_e_(510);
        this.drawHoverTooltips(renderStack, mouseX, mouseY);
        this.func_230926_e_(0);
        if (!this.mouseSealStack.func_190926_b()) {
            renderStack.popPose();
            renderStack.func_227861_a_((double)(mouseX - 8), (double)(mouseY - 8), (double)this.getGuiZLevel());
            RenderingUtils.renderItemStackGUI(renderStack, this.mouseSealStack, null);
            renderStack.scale();
        }
    }
    
    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        if (Minecraft.getInstance().player != null) {
            final int count = ItemPerkSeal.getPlayerSealCount((Player)Minecraft.getInstance().player);
            if (count > 0) {
                this.foundSeals = new ItemStack((IItemProvider)ItemsAS.PERK_SEAL, count);
            }
            else {
                this.foundSeals = ItemStack.field_190927_a;
            }
        }
        else {
            this.foundSeals = ItemStack.field_190927_a;
        }
        --this.tickSealBreak;
        if (this.tickSealBreak <= 0) {
            this.tickSealBreak = 0;
            this.sealBreakPrimed = null;
        }
    }
    
    private void drawSealBox(final PoseStack renderStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        TexturesAS.TEX_GUI_MENU_SLOT.bindTexture();
        RenderingGuiUtils.drawTexturedRect(renderStack, (float)(this.guiLeft + ScreenJournalPerkTree.rectSealBox.x - 1), (float)(this.guiTop + ScreenJournalPerkTree.rectSealBox.y - 1), (float)this.getGuiZLevel(), (float)(ScreenJournalPerkTree.rectSealBox.width + 2), (float)(ScreenJournalPerkTree.rectSealBox.height + 2), TexturesAS.TEX_GUI_MENU_SLOT);
        RenderSystem.disableBlend();
        if (!this.foundSeals.func_190926_b()) {
            renderStack.popPose();
            renderStack.func_227861_a_((double)(this.guiLeft + ScreenJournalPerkTree.rectSealBox.x), (double)(this.guiTop + ScreenJournalPerkTree.rectSealBox.y), (double)this.getGuiZLevel());
            RenderingUtils.renderItemStackGUI(renderStack, this.foundSeals, null);
            renderStack.scale();
        }
    }
    
    private void drawHoverTooltips(final PoseStack renderStack, final int mouseX, final int mouseY) {
        final Player player = (Player)Minecraft.getInstance().player;
        List<FormattedCharSequence> toolTip = null;
        for (final Rectangle2D.Float r : this.slotsSocketMenu.keySet()) {
            if (r.contains(mouseX, mouseY)) {
                final Integer slot = this.slotsSocketMenu.get(r);
                final ItemStack in = player.field_71071_by.func_70301_a((int)slot);
                if (!in.func_190926_b()) {
                    Font fr = in.func_77973_b().getFontRenderer(in);
                    if (fr == null) {
                        fr = Minecraft.getInstance().field_71466_p;
                    }
                    toolTip = new ArrayList<FormattedCharSequence>();
                    toolTip.addAll(this.func_231151_a_(in));
                    RenderingDrawUtils.renderBlueTooltipComponents(renderStack, (float)mouseX, (float)mouseY, (float)this.getGuiZLevel(), toolTip, fr, true);
                }
                return;
            }
        }
        if (this.rStatStar.contains(mouseX, mouseY)) {
            RenderingDrawUtils.renderBlueTooltipComponents(renderStack, this.rStatStar.x + this.rStatStar.width / 2.0f, (float)(this.rStatStar.y + this.rStatStar.height), (float)this.getGuiZLevel(), Lists.newArrayList((Object[])new FormattedCharSequence[] { (FormattedCharSequence)Component.translatable("perk.reader.astralsorcery.infostar") }), this.field_230712_o_, false);
            return;
        }
        if (!this.foundSeals.func_190926_b() && ScreenJournalPerkTree.rectSealBox.contains(mouseX - this.guiLeft, mouseY - this.guiTop)) {
            final List<FormattedCharSequence> toolTip2 = new ArrayList<FormattedCharSequence>();
            toolTip2.addAll(this.foundSeals.getTooltipLines((Player)Minecraft.getInstance().player, (TooltipFlag)(Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.TooltipFlags.ADVANCED : TooltipFlag.TooltipFlags.NORMAL)));
            toolTip2.add((FormattedCharSequence)Component.empty());
            toolTip2.add((FormattedCharSequence)Component.translatable("perk.info.astralsorcery.sealed.usage").func_240699_a_(ChatFormatting.GRAY));
            RenderingDrawUtils.renderBlueTooltipComponents(renderStack, (float)mouseX, (float)mouseY, (float)this.getGuiZLevel(), toolTip2, this.field_230712_o_, false);
        }
        else {
            for (final Map.Entry<AbstractPerk, Rectangle2D.Float> rctPerk : this.thisFramePerks.entrySet()) {
                if (rctPerk.getValue().contains(mouseX, mouseY) && this.guiBox.isInBox(mouseX - this.guiLeft, mouseY - this.guiTop)) {
                    final List<FormattedCharSequence> toolTip3 = new LinkedList<FormattedCharSequence>();
                    final AbstractPerk perk = rctPerk.getKey();
                    final PlayerProgress prog = ResearchHelper.getClientProgress();
                    final PlayerPerkData perkData = prog.getPerkData();
                    perk.getLocalizedTooltip().forEach(line -> {
                        final Style style = line.func_150256_b();
                        if (style.func_240711_a_() == null) {
                            line.func_240699_a_(ChatFormatting.GRAY).func_240699_a_(ChatFormatting.ITALIC);
                        }
                        toolTip.add(line);
                        return;
                    });
                    if (perkData.isPerkSealed(perk)) {
                        toolTip3.add((FormattedCharSequence)Component.translatable("perk.info.astralsorcery.sealed").func_240699_a_(ChatFormatting.RED));
                        toolTip3.add((FormattedCharSequence)Component.translatable("perk.info.astralsorcery.sealed.break").func_240699_a_(ChatFormatting.RED));
                    }
                    else if (perkData.hasPerkEffect(perk)) {
                        toolTip3.add((FormattedCharSequence)Component.translatable("perk.info.astralsorcery.active").func_240699_a_(ChatFormatting.GREEN));
                    }
                    else if (perk.mayUnlockPerk(prog, player)) {
                        toolTip3.add((FormattedCharSequence)Component.translatable("perk.info.astralsorcery.available").func_240699_a_(ChatFormatting.BLUE));
                    }
                    else {
                        toolTip3.add((FormattedCharSequence)Component.translatable("perk.info.astralsorcery.locked").func_240699_a_(ChatFormatting.GRAY));
                    }
                    if (Minecraft.getInstance().options.advancedItemTooltips && perk.getCategory() != AbstractPerk.CATEGORY_BASE) {
                        toolTip3.add((FormattedCharSequence)perk.getCategory().getName().func_240699_a_(ChatFormatting.GRAY).func_240699_a_(ChatFormatting.ITALIC));
                    }
                    final Collection<MutableComponent> modInfo = perk.getSource();
                    if (modInfo != null) {
                        for (final MutableComponent cmp : modInfo) {
                            toolTip3.add((FormattedCharSequence)cmp.func_240699_a_(ChatFormatting.BLUE).func_240699_a_(ChatFormatting.ITALIC));
                        }
                    }
                    if (Minecraft.getInstance().options.advancedItemTooltips) {
                        toolTip3.add((FormattedCharSequence)Component.empty());
                        toolTip3.add((FormattedCharSequence)Component.translatable(perk.getRegistryName().withStyle().func_240699_a_(ChatFormatting.GRAY)));
                        toolTip3.add((FormattedCharSequence)Component.translatable("astralsorcery.misc.ctrlcopy").func_240699_a_(ChatFormatting.GRAY));
                    }
                    RenderingDrawUtils.renderBlueTooltipComponents(renderStack, (float)mouseX, (float)mouseY, (float)this.getGuiZLevel(), toolTip3, this.field_230712_o_, true);
                    break;
                }
            }
        }
    }
    
    private <T extends AbstractPerk & GemSocketPerk> void drawSocketContextMenu(final PoseStack renderStack) {
        this.rSocketMenu = null;
        this.slotsSocketMenu.clear();
        if (this.socketMenu != null) {
            final T sMenuPerk = (T)this.socketMenu;
            ItemStack stack = null;
            final Map<Integer, ItemStack> found = ItemUtils.findItemsIndexedInPlayerInventory((Player)Minecraft.getInstance().player, stack -> {
                if (stack.func_190926_b() || !(stack.func_77973_b() instanceof GemSocketItem)) {
                    return false;
                }
                else {
                    final GemSocketItem item = (GemSocketItem)stack.func_77973_b();
                    return item.canBeInserted(stack, sMenuPerk, (Player)Minecraft.getInstance().player, ResearchHelper.getClientProgress(), LogicalSide.CLIENT);
                }
            });
            if (found.isEmpty()) {
                this.closeSocketMenu();
                return;
            }
            final Point2D.Float offset = this.sizeHandler.scalePointToGui(this, this.mousePosition, sMenuPerk.getPoint().getOffset());
            float offsetX = (float)MathHelper.func_76141_d(offset.x);
            float offsetY = (float)MathHelper.func_76141_d(offset.y);
            final float scale = this.sizeHandler.getScalingFactor();
            final float scaledSlotSize = 18.0f * scale;
            final int realWidth = Math.min(5, found.size());
            final int realHeight = found.size() / 5 + ((found.size() % 5 != 0) ? 1 : 0);
            final float width = realWidth * scaledSlotSize;
            final float height = realHeight * scaledSlotSize;
            this.rSocketMenu = new Rectangle2D.Float(offsetX + 12.0f * scale - 4.0f, offsetY - 12.0f * scale - 4.0f, width + 4.0f, height + 4.0f);
            if (!this.guiBox.isInBox(this.rSocketMenu.x - this.guiLeft, this.rSocketMenu.y - this.guiTop) || !this.guiBox.isInBox(this.rSocketMenu.x + this.rSocketMenu.width - this.guiLeft, this.rSocketMenu.y + this.rSocketMenu.height - this.guiTop)) {
                this.closeSocketMenu();
                return;
            }
            renderStack.popPose();
            renderStack.func_227861_a_((double)offsetX, (double)offsetY, (double)this.getGuiZLevel());
            renderStack.translate(scale, scale, 1.0f);
            RenderingDrawUtils.renderBlueTooltipBox(renderStack, 0, 0, realWidth * 18, realHeight * 18);
            renderStack.scale();
            final float inventoryOffsetX = offsetX + 12.0f * scale;
            final float inventoryOffsetY = offsetY - 12.0f * scale;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            TexturesAS.TEX_GUI_MENU_SLOT_GEM_CONTEXT.bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
                for (int index2 = 0; index2 < found.size(); ++index2) {
                    final float addedX2 = index2 % 5 * scaledSlotSize;
                    final float addedY2 = index2 / 5 * scaledSlotSize;
                    RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, inventoryOffsetX + addedX2, inventoryOffsetY + addedY2, (float)this.getGuiZLevel(), scaledSlotSize, scaledSlotSize).draw();
                }
                return;
            });
            RenderSystem.disableBlend();
            offsetX += 12.0f * scale;
            offsetY -= 12.0f * scale;
            int index = 0;
            for (final Integer slotId : found.keySet()) {
                stack = found.get(slotId);
                final float addedX = index % 5 * scaledSlotSize;
                final float addedY = index / 5 * scaledSlotSize;
                final Rectangle2D.Float r = new Rectangle2D.Float(offsetX + addedX, offsetY + addedY, scaledSlotSize, scaledSlotSize);
                renderStack.popPose();
                renderStack.func_227861_a_((double)(offsetX + addedX + 1.0f), (double)(offsetY + addedY + 1.0f), (double)this.getGuiZLevel());
                renderStack.translate(scale, scale, 1.0f);
                RenderingUtils.renderItemStackGUI(renderStack, stack, null);
                renderStack.scale();
                this.slotsSocketMenu.put(r, slotId);
                ++index;
            }
        }
    }
    
    private void drawMiscInfo(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        final PlayerProgress prog = ResearchHelper.getClientProgress();
        final Player player = (Player)Minecraft.getInstance().player;
        final int availablePerks;
        if (prog.isAttuned() && (availablePerks = prog.getPerkData().getAvailablePerkPoints(player, LogicalSide.CLIENT)) > 0) {
            renderStack.popPose();
            renderStack.func_227861_a_((double)(this.guiLeft + 50), (double)(this.guiTop + 18), (double)this.getGuiZLevel());
            final FormattedCharSequence points = (FormattedCharSequence)Component.translatable("perk.info.astralsorcery.points");
            RenderingDrawUtils.renderStringAt(points, renderStack, this.field_230712_o_, 13421772, true);
            renderStack.scale();
        }
        renderStack.popPose();
        renderStack.func_227861_a_((double)(this.guiLeft + 288), (double)(this.guiTop + 20), (double)this.getGuiZLevel());
        (this.rStatStar = RenderingDrawUtils.drawInfoStar(renderStack, IDrawRenderTypeBuffer.defaultBuffer(), 16.0f, pTicks)).translate(this.guiLeft + 288, this.guiTop + 20);
        renderStack.scale();
    }
    
    private void drawSearchBox(final PoseStack renderStack) {
        TexturesAS.TEX_GUI_TEXT_FIELD.bindTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(this.guiLeft + 300), (float)(this.guiTop + 16), (float)this.getGuiZLevel(), 88.5f, 15.0f).draw());
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
        renderStack.popPose();
        renderStack.func_227861_a_((double)(this.guiLeft + 304), (double)(this.guiTop + 20), (double)this.getGuiZLevel());
        RenderingDrawUtils.renderStringAt(this.field_230712_o_, renderStack, (FormattedCharSequence)new Component(text), 13421772);
        renderStack.scale();
    }
    
    private void drawPerkTree(final PoseStack renderStack, final float partialTicks) {
        final Player player = (Player)Minecraft.getInstance().player;
        final PlayerProgress progress = ResearchHelper.getClientProgress();
        final PlayerPerkData perkData = progress.getPerkData();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        TexturesAS.TEX_GUI_LINE_CONNECTION.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            PerkTree.PERK_TREE.getConnections().iterator();
            final Iterator iterator2;
            while (iterator2.hasNext()) {
                final Tuple<AbstractPerk, AbstractPerk> perkConnection = (Tuple<AbstractPerk, AbstractPerk>)iterator2.next();
                if (((AbstractPerk)perkConnection.func_76341_a()).isVisible(progress, player)) {
                    if (!((AbstractPerk)perkConnection.func_76340_b()).isVisible(progress, player)) {
                        continue;
                    }
                    else {
                        int alloc = 0;
                        if (perkData.hasPerkAllocation((AbstractPerk)perkConnection.func_76341_a(), PerkAllocationType.UNLOCKED)) {
                            ++alloc;
                        }
                        if (perkData.hasPerkAllocation((AbstractPerk)perkConnection.func_76340_b(), PerkAllocationType.UNLOCKED)) {
                            ++alloc;
                        }
                        AllocationStatus status;
                        if (alloc == 2) {
                            status = AllocationStatus.ALLOCATED;
                        }
                        else if (alloc == 1 && progress.getPerkData().hasFreeAllocationPoint(player, LogicalSide.CLIENT)) {
                            status = AllocationStatus.UNLOCKABLE;
                        }
                        else {
                            status = AllocationStatus.UNALLOCATED;
                        }
                        final Point2D.Float offsetOne = ((AbstractPerk)perkConnection.func_76341_a()).getPoint().getOffset();
                        final Point2D.Float offsetTwo = ((AbstractPerk)perkConnection.func_76340_b()).getPoint().getOffset();
                        this.drawConnection(buf, renderStack, status, offsetOne, offsetTwo, ClientScheduler.getClientTick() + (int)offsetOne.x + (int)offsetOne.y + (int)offsetTwo.x + (int)offsetTwo.y);
                    }
                }
            }
            return;
        });
        RenderSystem.disableBlend();
        ScreenJournalPerkTree.drawBuffer.beginDrawingPerks();
        final List<Runnable> renderDynamic = Lists.newArrayList();
        AbstractPerk perk = null;
        for (final PerkTreePoint<?> perkPoint : PerkTree.PERK_TREE.getPerkPoints(LogicalSide.CLIENT)) {
            perk = (AbstractPerk)perkPoint.getPerk();
            if (!perk.isVisible(progress, player)) {
                continue;
            }
            final Point2D.Float offset = perkPoint.getOffset();
            final Rectangle2D.Float perkRect = this.drawPerk(ScreenJournalPerkTree.drawBuffer, renderStack, perkPoint, partialTicks, ClientScheduler.getClientTick() + (int)offset.x + (int)offset.y, perkData.isPerkSealed(perk), renderDynamic);
            if (perkRect == null) {
                continue;
            }
            this.thisFramePerks.put(perk, perkRect);
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        ScreenJournalPerkTree.drawBuffer.draw();
        RenderSystem.disableBlend();
        renderDynamic.forEach(Runnable::run);
        this.unlockEffects.keySet().removeIf(perk -> !this.drawPerkUnlock(perk, renderStack, this.unlockEffects.get(perk)));
        this.breakEffects.keySet().removeIf(perk -> !this.drawPerkSealBreak(perk, renderStack, this.breakEffects.get(perk), partialTicks));
    }
    
    private boolean drawPerkSealBreak(final AbstractPerk perk, final PoseStack renderStack, final long tick, final float pTicks) {
        final int count = (int)(ClientScheduler.getClientTick() - tick);
        final SpriteSheetResource sealBreakSprite = SpritesAS.SPR_PERK_SEAL_BREAK;
        if (count >= sealBreakSprite.getFrameCount()) {
            return false;
        }
        final Point2D.Float offset = this.sizeHandler.scalePointToGui(this, this.mousePosition, perk.getOffset());
        final float sealFade = 1.0f - (count + pTicks) / sealBreakSprite.getFrameCount();
        float width = 22.0f;
        final Rectangle2D.Float rct;
        if ((rct = this.thisFramePerks.get(perk)) != null) {
            width = rct.width;
        }
        final float sealWidth = width * 0.75f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        SpritesAS.SPR_PERK_SEAL.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            final Point2D.Float pOffset = perk.getPoint().getOffset();
            this.drawSeal(buf, renderStack, sealWidth, offset.x, offset.y, ClientScheduler.getClientTick() + (int)pOffset.x + (int)pOffset.y, sealFade * 0.75f);
            return;
        });
        final float uLength = sealBreakSprite.getUWidth();
        final float vLength = sealBreakSprite.getVWidth();
        final Tuple<Float, Float> uv = sealBreakSprite.getUVOffset(count);
        sealBreakSprite.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, offset.x - sealWidth, offset.y - sealWidth, (float)this.getGuiZLevel(), sealWidth * 2.0f, sealWidth * 2.0f).color(1.0f, 1.0f, 1.0f, 0.85f).tex((float)uv.func_76341_a(), (float)uv.func_76340_b(), uLength, vLength).draw());
        RenderSystem.disableBlend();
        return true;
    }
    
    private boolean drawPerkUnlock(final AbstractPerk perk, final PoseStack renderStack, final long tick) {
        final int count = (int)(ClientScheduler.getClientTick() - tick);
        final SpriteSheetResource spritePerkUnlock = SpritesAS.SPR_PERK_UNLOCK;
        if (count >= spritePerkUnlock.getFrameCount()) {
            return false;
        }
        final Point2D.Float offset = this.sizeHandler.scalePointToGui(this, this.mousePosition, perk.getOffset());
        float width = 22.0f;
        final Rectangle2D.Float rct;
        if ((rct = this.thisFramePerks.get(perk)) != null) {
            width = rct.width;
        }
        final float unlockWidth = width * 2.5f;
        final float uLength = spritePerkUnlock.getUWidth();
        final float vLength = spritePerkUnlock.getVWidth();
        final Tuple<Float, Float> uv = spritePerkUnlock.getUVOffset(count);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        spritePerkUnlock.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, offset.x - unlockWidth, offset.y - unlockWidth, (float)this.getGuiZLevel(), unlockWidth * 2.0f, unlockWidth * 2.0f).tex((float)uv.func_76341_a(), (float)uv.func_76340_b(), uLength, vLength).draw());
        RenderSystem.disableBlend();
        return true;
    }
    
    @Nullable
    private Rectangle2D.Float drawPerk(final BatchPerkContext ctx, final PoseStack renderStack, final PerkTreePoint<?> perkPoint, final float pTicks, final long effectTick, final boolean renderSeal, final Collection<Runnable> outRenderDynamic) {
        final Point2D.Float offset = this.sizeHandler.scalePointToGui(this, this.mousePosition, perkPoint.getOffset());
        final float scale = this.sizeHandler.getScalingFactor();
        final AllocationStatus status = ((AbstractPerk)perkPoint.getPerk()).getPerkStatus((Player)Minecraft.getInstance().player, LogicalSide.CLIENT);
        final Rectangle2D.Float drawSize = perkPoint.renderPerkAtBatch(ctx, renderStack, status, effectTick, pTicks, offset.x, offset.y, (float)this.getGuiZLevel(), scale);
        if (perkPoint instanceof DynamicPerkRender) {
            outRenderDynamic.add(() -> ((DynamicPerkRender)perkPoint).renderAt(status, renderStack, effectTick, pTicks, offset.x, offset.y, (float)this.getGuiZLevel(), scale));
        }
        if (drawSize == null) {
            return null;
        }
        if (renderSeal) {
            this.drawSeal(ctx, renderStack, drawSize.width * 0.75, offset.x, offset.y, effectTick);
        }
        if (this.searchMatches.contains(perkPoint.getPerk())) {
            this.drawSearchMarkHalo(ctx, renderStack, drawSize, offset.x, offset.y);
        }
        final float mapDrawSize = 28.0f;
        if (perkPoint.getPerk() instanceof AttributeConverterProvider) {
            for (final PerkConverter converter : ((AttributeConverterProvider)perkPoint.getPerk()).getConverters((Player)Minecraft.getInstance().player, LogicalSide.CLIENT, true)) {
                if (converter instanceof PerkConverter.Radius) {
                    final float radius = ((PerkConverter.Radius)converter).getRadius();
                    this.drawSearchHalo(ctx, renderStack, mapDrawSize * radius * scale, offset.x, offset.y);
                }
            }
        }
        return new Rectangle2D.Float(offset.x - drawSize.width / 2.0f, offset.y - drawSize.height / 2.0f, drawSize.width, drawSize.height);
    }
    
    private void drawSeal(final BatchPerkContext ctx, final PoseStack renderStack, final double size, final double x, final double y, final long spriteOffsetTick) {
        final BufferContext batch = ctx.getContext(ScreenJournalPerkTree.sealContext);
        this.drawSeal(batch, renderStack, size, x, y, spriteOffsetTick, 1.0f);
    }
    
    private void drawSeal(final BufferBuilder vb, final PoseStack renderStack, final double size, final double x, final double y, final long spriteOffsetTick, final float alpha) {
        final SpriteSheetResource tex = SpritesAS.SPR_PERK_SEAL;
        if (tex == null) {
            return;
        }
        final float uLength = tex.getULength();
        final float vLength = tex.getVLength();
        final Tuple<Float, Float> frameUV = tex.getUVOffset(spriteOffsetTick);
        final Vector3 starVec = new Vector3(x - size, y - size, 0.0);
        final Matrix4f offset = renderStack.last().func_227870_a_();
        for (int i = 0; i < 4; ++i) {
            final int u = (i + 1 & 0x2) >> 1;
            final int v = (i + 2 & 0x2) >> 1;
            final Vector3 pos = starVec.clone().addX(size * u * 2.0).addY(size * v * 2.0);
            pos.drawPos(offset, (VertexConsumer)vb).color(1.0f, 1.0f, 1.0f, alpha).func_225583_a_((float)frameUV.func_76341_a() + uLength * u, (float)frameUV.func_76340_b() + vLength * v).endVertex();
        }
    }
    
    private void drawSearchMarkHalo(final BatchPerkContext ctx, final PoseStack renderStack, final Rectangle2D.Float draw, final float x, final float y) {
        this.drawSearchHalo(ctx, renderStack, (draw.width + draw.height) / 2.0f, x, y);
    }
    
    private void drawSearchHalo(final BatchPerkContext ctx, final PoseStack renderStack, final float size, final float x, final float y) {
        final BufferContext batch = ctx.getContext(ScreenJournalPerkTree.searchContext);
        final SpriteSheetResource searchMark = SpritesAS.SPR_PERK_SEARCH;
        searchMark.bindTexture();
        final Vector3 starVec = new Vector3(x - size, y - size, 0.0f);
        final float uLength = searchMark.getUWidth();
        final float vLength = searchMark.getVWidth();
        final Tuple<Float, Float> frameUV = searchMark.getUVOffset();
        final Matrix4f offset = renderStack.last().func_227870_a_();
        for (int i = 0; i < 4; ++i) {
            final int u = (i + 1 & 0x2) >> 1;
            final int v = (i + 2 & 0x2) >> 1;
            final Vector3 pos = starVec.clone().addX(size * u * 2.0f).addY(size * v * 2.0f);
            pos.drawPos(offset, (VertexConsumer)batch).color(0.8f, 0.1f, 0.1f, 1.0f).func_225583_a_((float)frameUV.func_76341_a() + uLength * u, (float)frameUV.func_76340_b() + vLength * v).endVertex();
        }
    }
    
    private void drawConnection(final BufferBuilder vb, final PoseStack renderStack, final AllocationStatus status, final Point2D.Float source, final Point2D.Float target, final long effectTick) {
        final Point2D.Float offsetSrc = this.sizeHandler.scalePointToGui(this, this.mousePosition, source);
        final Point2D.Float offsetDst = this.sizeHandler.scalePointToGui(this, this.mousePosition, target);
        final Color overlay = status.getPerkConnectionColor();
        final double effectPart = (Math.sin(Math.toRadians(effectTick * 8L % 360.0)) + 1.0) / 4.0;
        final float br = 0.1f + 0.4f * (2.0f - (float)effectPart);
        final float rR = overlay.getRed() / 255.0f * br;
        final float rG = overlay.getGreen() / 255.0f * br;
        final float rB = overlay.getBlue() / 255.0f * br;
        final float rA = overlay.getAlpha() / 255.0f * br;
        final Vector3 fromStar = new Vector3(offsetSrc.x, offsetSrc.y, 0.0f);
        final Vector3 toStar = new Vector3(offsetDst.x, offsetDst.y, 0.0f);
        final double width = 4.0 * this.sizeHandler.getScalingFactor();
        final Vector3 dir = toStar.clone().subtract(fromStar);
        final Vector3 degLot = dir.clone().crossProduct(new Vector3(0, 0, 1)).normalize().multiply(width);
        final Vector3 vec00 = fromStar.clone().add(degLot);
        final Vector3 vecV = degLot.clone().multiply(-2);
        final Matrix4f offset = renderStack.last().func_227870_a_();
        for (int i = 0; i < 4; ++i) {
            final int u = (i + 1 & 0x2) >> 1;
            final int v = (i + 2 & 0x2) >> 1;
            final Vector3 pos = vec00.clone().add(dir.clone().multiply(u)).add(vecV.clone().multiply(v));
            pos.drawPos(offset, (VertexConsumer)vb).color(rR, rG, rB, rA).func_225583_a_((float)u, (float)v).endVertex();
        }
    }
    
    @Override
    protected void mouseDragTick(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY, final double mouseOffsetX, final double mouseOffsetY) {
        super.mouseDragTick(mouseX, mouseY, mouseDiffX, mouseDiffY, mouseOffsetX, mouseOffsetY);
        if (this.mouseSealStack.func_190926_b()) {
            this.moveMouse((float)mouseDiffX, (float)mouseDiffY);
        }
    }
    
    @Override
    protected void mouseDragStop(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY) {
        super.mouseDragStop(mouseX, mouseY, mouseDiffX, mouseDiffY);
        if (this.mouseSealStack.func_190926_b()) {
            this.applyMovedMouseOffset();
        }
    }
    
    private void moveMouse(final float changeX, final float changeY) {
        if (this.previousMousePosition != null) {
            this.mousePosition.updateScaledPos(this.sizeHandler.clampX(this.previousMousePosition.getScaledPosX() + changeX), this.sizeHandler.clampY(this.previousMousePosition.getScaledPosY() + changeY), this.sizeHandler.getScalingFactor());
        }
        else {
            this.mousePosition.updateScaledPos(this.sizeHandler.clampX(changeX), this.sizeHandler.clampY(changeY), this.sizeHandler.getScalingFactor());
        }
    }
    
    private void applyMovedMouseOffset() {
        this.previousMousePosition = ScalingPoint.createPoint(this.mousePosition.getScaledPosX(), this.mousePosition.getScaledPosY(), this.sizeHandler.getScalingFactor(), true);
    }
    
    private void drawBackground(final PoseStack renderStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        TexturesAS.TEX_GUI_BACKGROUND_PERKS.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(this.guiLeft - 10), (float)(this.guiTop - 10), (float)this.getGuiZLevel(), (float)(this.guiWidth + 20), (float)(this.guiHeight + 20)).color(0.5f, 0.5f, 0.5f, 1.0f).draw());
        RenderSystem.disableAlphaTest();
    }
    
    private void updateSearchHighlight() {
        this.searchMatches.clear();
        final String matchText = this.searchTextEntry.getText().toLowerCase(Locale.ROOT);
        if (matchText.length() < 3) {
            return;
        }
        for (final PerkTreePoint<?> point : PerkTree.PERK_TREE.getPerkPoints(LogicalSide.CLIENT)) {
            final AbstractPerk perk = (AbstractPerk)point.getPerk();
            if (perk instanceof ProgressGatedPerk && !((ProgressGatedPerk)perk).canSeeClient()) {
                continue;
            }
            if (perk.getCategory().getName().getString().toLowerCase(Locale.ROOT).contains(matchText)) {
                this.searchMatches.add(perk);
            }
            else {
                for (final MutableComponent tooltip : perk.getLocalizedTooltip()) {
                    if (tooltip.getString().toLowerCase(Locale.ROOT).contains(matchText)) {
                        this.searchMatches.add(perk);
                        break;
                    }
                }
            }
        }
        final MutableComponent sealedInfo = (MutableComponent)Component.translatable("perk.info.astralsorcery.sealed");
        if (sealedInfo.getString().toLowerCase(Locale.ROOT).contains(matchText)) {
            final PlayerProgress prog = ResearchHelper.getClientProgress();
            for (final AbstractPerk sealed : prog.getPerkData().getSealedPerks()) {
                if (!this.searchMatches.contains(sealed)) {
                    this.searchMatches.add(sealed);
                }
            }
        }
    }
    
    private void closeSocketMenu() {
        this.socketMenu = null;
        this.rSocketMenu = null;
        this.slotsSocketMenu.clear();
    }
    
    @Override
    public boolean func_231048_c_(final double mouseX, final double mouseY, final int state) {
        if (super.func_231048_c_(mouseX, mouseY, state)) {
            return true;
        }
        final Player player = (Player)Minecraft.getInstance().player;
        if (!this.mouseSealStack.func_190926_b()) {
            this.mouseSealStack = ItemStack.field_190927_a;
            if (Minecraft.getInstance().player == null) {
                return false;
            }
            final PlayerPerkData perkData = ResearchHelper.getClientProgress().getPerkData();
            for (final Map.Entry<AbstractPerk, Rectangle2D.Float> rctPerk : this.thisFramePerks.entrySet()) {
                if (rctPerk.getValue().contains(mouseX, mouseY) && this.guiBox.isInBox(mouseX - this.guiLeft, mouseY - this.guiTop) && perkData.hasPerkEffect(rctPerk.getKey()) && !perkData.isPerkSealed(rctPerk.getKey()) && ItemPerkSeal.useSeal(player, true)) {
                    final PktRequestPerkSealAction pkt = new PktRequestPerkSealAction(rctPerk.getKey(), true);
                    PacketChannel.CHANNEL.sendToServer(pkt);
                    return true;
                }
            }
        }
        if (this.unlockPrimed == null) {
            return false;
        }
        for (final Map.Entry<AbstractPerk, Rectangle2D.Float> rctPerk2 : this.thisFramePerks.entrySet()) {
            if (this.unlockPrimed.equals(rctPerk2.getKey()) && rctPerk2.getValue().contains(mouseX, mouseY) && this.guiBox.isInBox(mouseX - this.guiLeft, mouseY - this.guiTop)) {
                final AbstractPerk perk = rctPerk2.getKey();
                final PlayerProgress prog = ResearchHelper.getClientProgress();
                final PlayerPerkData perkData2 = prog.getPerkData();
                if (!perkData2.hasPerkAllocation(perk) && perk.mayUnlockPerk(prog, player)) {
                    final PktUnlockPerk pkt2 = new PktUnlockPerk(false, rctPerk2.getKey());
                    PacketChannel.CHANNEL.sendToServer(pkt2);
                    break;
                }
                continue;
            }
        }
        this.unlockPrimed = null;
        return true;
    }
    
    public boolean func_231043_a_(final double mouseX, final double mouseY, final double scroll) {
        if (scroll < 0.0) {
            this.sizeHandler.handleZoomOut();
            this.rescaleMouse();
            return true;
        }
        if (scroll > 0.0) {
            this.sizeHandler.handleZoomIn();
            this.rescaleMouse();
            return true;
        }
        return false;
    }
    
    private void rescaleMouse() {
        this.mousePosition.rescale(this.sizeHandler.getScalingFactor());
        if (this.previousMousePosition != null) {
            this.previousMousePosition.rescale(this.sizeHandler.getScalingFactor());
        }
        this.moveMouse(0.0f, 0.0f);
    }
    
    @Override
    public boolean func_231045_a_(final double mouseX, final double mouseY, final int mouseButton, final double dragX, final double dragY) {
        this.unlockPrimed = null;
        return super.func_231045_a_(mouseX, mouseY, mouseButton, dragX, dragY);
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        if (ScreenJournalPerkTree.rectSearchTextEntry.contains(mouseX - this.guiLeft, mouseY - this.guiTop)) {
            this.searchTextEntry.setText("");
            return false;
        }
        if (this.socketMenu != null && this.rSocketMenu != null && !this.rSocketMenu.contains(mouseX, mouseY)) {
            this.closeSocketMenu();
            return false;
        }
        for (final Map.Entry<AbstractPerk, Rectangle2D.Float> rctPerk : this.thisFramePerks.entrySet()) {
            if (rctPerk.getValue().contains(mouseX, mouseY) && this.guiBox.isInBox(mouseX - this.guiLeft, mouseY - this.guiTop)) {
                final AbstractPerk perk = rctPerk.getKey();
                if (perk instanceof GemSocketPerk) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    @Override
    public boolean func_231044_a_(final double mouseX, final double mouseY, final int mouseButton) {
        if (super.func_231044_a_(mouseX, mouseY, mouseButton)) {
            return true;
        }
        final Minecraft mc = Minecraft.getInstance();
        if (this.socketMenu != null && (mouseButton == 0 || mouseButton == 1) && this.rSocketMenu != null && !this.rSocketMenu.contains(mouseX, mouseY)) {
            this.closeSocketMenu();
        }
        if (mouseButton == 0) {
            if (this.socketMenu != null) {
                for (final Rectangle2D.Float r : this.slotsSocketMenu.keySet()) {
                    if (r.contains(mouseX, mouseY) && !this.socketMenu.hasItem((Player)mc.player, LogicalSide.CLIENT)) {
                        final int slotId = this.slotsSocketMenu.get(r);
                        if (this.tryInsertGem(slotId, this.socketMenu)) {
                            return true;
                        }
                        continue;
                    }
                }
            }
            if (this.handleBookmarkClick(mouseX, mouseY)) {
                return true;
            }
            if (ScreenJournalPerkTree.rectSealBox.contains(mouseX - this.guiLeft, mouseY - this.guiTop)) {
                if (!this.foundSeals.func_190926_b()) {
                    this.mouseSealStack = new ItemStack((IItemProvider)ItemsAS.PERK_SEAL);
                }
                return true;
            }
            if (this.rStatStar.contains(mouseX, mouseY)) {
                this.expectReinit = true;
                mc.func_147108_a((Screen)new ScreenJournalOverlayPerkStatistics(this));
                return true;
            }
        }
        final PlayerProgress prog = ResearchHelper.getClientProgress();
        final PlayerPerkData perkData = prog.getPerkData();
        for (final Map.Entry<AbstractPerk, Rectangle2D.Float> rctPerk : this.thisFramePerks.entrySet()) {
            if (rctPerk.getValue().contains(mouseX, mouseY) && this.guiBox.isInBox(mouseX - this.guiLeft, mouseY - this.guiTop)) {
                final AbstractPerk perk = rctPerk.getKey();
                if (mouseButton == 0 && mc.options.advancedItemTooltips && func_231172_r_()) {
                    final String perkKey = perk.getRegistryName().toString();
                    Minecraft.getInstance().field_195559_v.func_197960_a(perkKey);
                    mc.player.sendSystemMessage((ITextComponent)Component.translatable("astralsorcery.misc.ctrlcopy.copied"), Util.field_240973_b_);
                    break;
                }
                if (mouseButton == 1) {
                    if (perkData.hasPerkEffect(perk) && perk instanceof GemSocketPerk) {
                        if (((GemSocketPerk)perk).hasItem((Player)mc.player, LogicalSide.CLIENT)) {
                            final PktPerkGemModification pkt = PktPerkGemModification.dropItem(perk);
                            PacketChannel.CHANNEL.sendToServer(pkt);
                            AstralSorcery.getProxy().scheduleClientside(() -> {
                                if (mc.field_71462_r == this) {
                                    this.updateSearchHighlight();
                                }
                                return;
                            }, 10);
                            SoundHelper.playSoundClient(SoundEvents.field_187567_bP, 0.35f, 9.0f);
                        }
                        else {
                            this.socketMenu = (GemSocketPerk)perk;
                        }
                        return true;
                    }
                    continue;
                }
                else {
                    if (mouseButton != 0) {
                        continue;
                    }
                    if (perk.handleMouseClick(this, mouseX, mouseY)) {
                        return true;
                    }
                    if (!perkData.hasPerkAllocation(perk) && perk.mayUnlockPerk(prog, (Player)mc.player)) {
                        this.unlockPrimed = perk;
                    }
                    else if (this.sealBreakPrimed != null && this.tickSealBreak > 0) {
                        final PktRequestPerkSealAction pkt2 = new PktRequestPerkSealAction(perk, false);
                        PacketChannel.CHANNEL.sendToServer(pkt2);
                    }
                    else if (prog.getPerkData().isPerkSealed(perk)) {
                        this.sealBreakPrimed = perk;
                        this.tickSealBreak = 4;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    private <T extends AbstractPerk & GemSocketPerk> boolean tryInsertGem(final int slotId, final GemSocketPerk perk) {
        if (!(perk instanceof AbstractPerk)) {
            return false;
        }
        final T socketPerk = (T)perk;
        final ItemStack potentialStack = this.field_230706_i_.player.field_71071_by.func_70301_a(slotId);
        if (!potentialStack.func_190926_b() && potentialStack.func_77973_b() instanceof GemSocketItem) {
            final GemSocketItem gemItem = (GemSocketItem)potentialStack.func_77973_b();
            if (gemItem.canBeInserted(potentialStack, socketPerk, (Player)this.field_230706_i_.player, ResearchHelper.getClientProgress(), LogicalSide.CLIENT)) {
                final PktPerkGemModification pkt = PktPerkGemModification.insertItem(socketPerk, slotId);
                PacketChannel.CHANNEL.sendToServer(pkt);
                this.closeSocketMenu();
                SoundHelper.playSoundClient(SoundEvents.field_187567_bP, 0.35f, 9.0f);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean func_231046_a_(final int key, final int scanCode, final int modifiers) {
        return this.searchTextEntry.keyTyped(key) || super.func_231046_a_(key, scanCode, modifiers);
    }
    
    @Override
    public boolean func_231042_a_(final char charCode, final int keyModifiers) {
        return this.searchTextEntry.charTyped(charCode) || super.func_231042_a_(charCode, keyModifiers);
    }
    
    public void playUnlockAnimation(final AbstractPerk perk) {
        this.unlockEffects.put(perk, ClientScheduler.getClientTick());
        SoundHelper.playSoundClient(SoundsAS.PERK_UNLOCK, 0.3f, 1.0f);
    }
    
    public void playSealBreakAnimation(final AbstractPerk perk) {
        this.updateSearchHighlight();
        this.breakEffects.put(perk, ClientScheduler.getClientTick());
        SoundHelper.playSoundClient(SoundsAS.PERK_UNSEAL, 0.3f, 1.0f);
    }
    
    public void playSealApplyAnimation(final AbstractPerk perk) {
        this.updateSearchHighlight();
        SoundHelper.playSoundClient(SoundsAS.PERK_SEAL, 0.3f, 1.0f);
    }
    
    static {
        rectSealBox = new Rectangle(29, 16, 16, 16);
        rectSearchTextEntry = new Rectangle(300, 16, 88, 15);
        ScreenJournalPerkTree.lastPreparedBuffer = null;
    }
}
