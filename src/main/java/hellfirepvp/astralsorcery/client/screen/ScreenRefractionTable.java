package hellfirepvp.astralsorcery.client.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.client.PktEngraveGlass;
import com.mojang.math.Matrix4f;
import java.util.function.Supplier;
import net.minecraft.util.math.MathHelper;
import java.util.Collection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.util.ITooltipFlag;
import java.awt.Color;
import java.util.Collections;
import java.util.Random;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.util.world.WorldSeedCache;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravedStarMap;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.item.ItemInfusedGlass;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import java.util.Iterator;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.util.Blending;
import java.awt.Point;
import net.minecraft.client.gui.Font;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextProperties;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.constellation.DrawnConstellation;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Map;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.common.tile.TileRefractionTable;
import hellfirepvp.astralsorcery.client.screen.base.TileEntityScreen;

public class ScreenRefractionTable extends TileEntityScreen<TileRefractionTable>
{
    private static final Rectangle PLACEMENT_GRID;
    private final Map<Rectangle, IConstellation> mapRenderedConstellations;
    private final List<DrawnConstellation> currentlyDrawnConstellations;
    private IConstellation dragging;
    
    public ScreenRefractionTable(final TileRefractionTable tile) {
        super(tile, 188, 256);
        this.mapRenderedConstellations = new HashMap<Rectangle, IConstellation>();
        this.currentlyDrawnConstellations = new ArrayList<DrawnConstellation>();
        this.dragging = null;
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        RenderSystem.enableDepthTest();
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        this.mapRenderedConstellations.clear();
        if (this.getTile().hasParchment()) {
            this.drawWHRect(renderStack, TexturesAS.TEX_GUI_REFRACTION_TABLE_PARCHMENT);
        }
        else {
            this.drawWHRect(renderStack, TexturesAS.TEX_GUI_REFRACTION_TABLE_EMPTY);
        }
        if (DayTimeHelper.getCurrentDaytimeDistribution(this.getTile().func_145831_w()) <= 0.05 || !this.getTile().hasParchment()) {
            this.currentlyDrawnConstellations.clear();
            this.dragging = null;
        }
        final List<ITextProperties> tooltip = new ArrayList<ITextProperties>();
        FontRenderer tooltipRenderer = Minecraft.func_71410_x().field_71466_p;
        tooltipRenderer = this.renderTileItems(renderStack, mouseX, mouseY, tooltip, tooltipRenderer);
        this.renderConstellationOptions(renderStack, mouseX, mouseY, tooltip);
        this.renderRunningHalo(renderStack);
        this.renderInputItem(renderStack);
        this.renderDrawnConstellations(renderStack, mouseX, mouseY, tooltip);
        this.renderDraggedConstellations(renderStack);
        this.renderDragging(renderStack, mouseX, mouseY);
        if (!tooltip.isEmpty()) {
            this.func_230926_e_(510);
            RenderingDrawUtils.renderBlueTooltipComponents(renderStack, (float)mouseX, (float)mouseY, (float)this.getGuiZLevel(), tooltip, tooltipRenderer, true);
            this.func_230926_e_(0);
        }
    }
    
    private void renderDragging(final PoseStack renderStack, final int mouseX, final int mouseY) {
        if (this.dragging == null) {
            return;
        }
        final int whDrawn = 30;
        final Point offset = new Point(mouseX, mouseY);
        offset.translate(-whDrawn, -whDrawn);
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderingConstellationUtils.renderConstellationIntoGUI(this.dragging, renderStack, (float)offset.x, (float)offset.y, (float)this.getGuiZLevel(), (float)(whDrawn * 2), (float)(whDrawn * 2), 1.399999976158142, () -> DayTimeHelper.getCurrentDaytimeDistribution(this.getTile().func_145831_w()), true, false);
        RenderSystem.disableBlend();
        this.renderBox(renderStack, (float)offset.x, (float)offset.y, (float)(whDrawn * 2), (float)(whDrawn * 2), this.dragging.getTierRenderColor());
        final Rectangle r = new Rectangle(ScreenRefractionTable.PLACEMENT_GRID);
        r.grow(30, 30);
        r.translate(this.guiLeft, this.guiTop);
        this.renderBox(renderStack, (float)r.x, (float)r.y, (float)r.width, (float)r.height, this.dragging.getTierRenderColor());
    }
    
    private void renderDraggedConstellations(final PoseStack renderStack) {
        final int whDrawn = 30;
        for (final DrawnConstellation dragged : this.currentlyDrawnConstellations) {
            final Point offset = new Point(dragged.getPoint());
            offset.translate(this.guiLeft, this.guiTop);
            offset.translate(ScreenRefractionTable.PLACEMENT_GRID.x, ScreenRefractionTable.PLACEMENT_GRID.y);
            offset.translate(-whDrawn, -whDrawn);
            RenderSystem.enableBlend();
            Blending.DEFAULT.apply();
            RenderingConstellationUtils.renderConstellationIntoGUI(dragged.getConstellation(), renderStack, (float)offset.x, (float)offset.y, (float)this.getGuiZLevel(), (float)(whDrawn * 2), (float)(whDrawn * 2), 1.399999976158142, () -> DayTimeHelper.getCurrentDaytimeDistribution(this.getTile().func_145831_w()), true, false);
            RenderSystem.disableBlend();
        }
    }
    
    private void renderInputItem(final PoseStack renderStack) {
        if (this.getTile().getInputStack().isEmpty() || this.getTile().hasParchment()) {
            return;
        }
        this.func_230926_e_(100);
        final ItemStack input = this.getTile().getInputStack();
        RenderSystem.disableDepthTest();
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(this.guiLeft + 63 + 16.25, this.guiTop + 42 + 16.25, (double)this.getGuiZLevel());
        renderStack.func_227862_a_(6.0f, 6.0f, 1.0f);
        RenderingUtils.renderItemStackGUI(renderStack, input, null);
        renderStack.func_227865_b_();
        RenderSystem.enableDepthTest();
        this.func_230926_e_(0);
    }
    
    private void renderRunningHalo(final PoseStack renderStack) {
        if (this.getTile().getRunProgress() <= 0.0f) {
            return;
        }
        SpritesAS.SPR_HALO_INFUSION.bindTexture();
        final Tuple<Float, Float> uvFrame = SpritesAS.SPR_HALO_INFUSION.getUVOffset(ClientScheduler.getClientTick());
        final float scale = 160.0f;
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderSystem.disableAlphaTest();
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(this.guiWidth / 2.0f), (double)(this.guiHeight / 2.0f), 0.0);
        renderStack.func_227862_a_(-scale / 2.0f, -scale / 2.0f, 1.0f);
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> RenderingGuiUtils.rect((IVertexBuilder)buf, renderStack, this).dim(scale, scale).color(1.0f, 1.0f, 1.0f, this.getTile().getRunProgress()).tex((float)uvFrame.func_76341_a(), (float)uvFrame.func_76340_b(), SpritesAS.SPR_HALO_INFUSION.getUWidth(), SpritesAS.SPR_HALO_INFUSION.getVWidth()).draw());
        renderStack.func_227865_b_();
        RenderSystem.enableAlphaTest();
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
    }
    
    private void renderDrawnConstellations(final PoseStack renderStack, final int mouseX, final int mouseY, final List<ITextProperties> tooltip) {
        final ItemStack glass = this.getTile().getGlassStack();
        if (glass.isEmpty()) {
            return;
        }
        final World world = this.getTile().func_145831_w();
        final float nightPerc = DayTimeHelper.getCurrentDaytimeDistribution(world);
        final WorldContext ctx = SkyHandler.getContext(world, LogicalSide.CLIENT);
        if (ctx == null || !this.getTile().doesSeeSky() || nightPerc <= 0.05f) {
            return;
        }
        final EngravedStarMap map = ItemInfusedGlass.getEngraving(glass);
        if (map == null) {
            return;
        }
        for (final DrawnConstellation cst : map.getDrawnConstellations()) {
            final int whDrawn = 30;
            final Point offset = new Point(cst.getPoint());
            offset.translate(this.guiLeft, this.guiTop);
            offset.translate(ScreenRefractionTable.PLACEMENT_GRID.x, ScreenRefractionTable.PLACEMENT_GRID.y);
            offset.translate(-whDrawn, -whDrawn);
            RenderSystem.enableBlend();
            Blending.DEFAULT.apply();
            RenderingConstellationUtils.renderConstellationIntoGUI(cst.getConstellation(), renderStack, (float)offset.x, (float)offset.y, (float)this.getGuiZLevel(), (float)(whDrawn * 2), (float)(whDrawn * 2), 1.600000023841858, () -> DayTimeHelper.getCurrentDaytimeDistribution(world) * 0.8f, true, false);
            RenderSystem.disableBlend();
        }
    }
    
    private void renderConstellationOptions(final PoseStack renderStack, final int mouseX, final int mouseY, final List<ITextProperties> tooltip) {
        final ItemStack glass = this.getTile().getGlassStack();
        if (glass.isEmpty()) {
            return;
        }
        final World world = this.getTile().func_145831_w();
        final float nightPerc = DayTimeHelper.getCurrentDaytimeDistribution(world);
        final WorldContext ctx = SkyHandler.getContext(world, LogicalSide.CLIENT);
        if (ctx == null || !this.getTile().doesSeeSky() || nightPerc <= 0.05f) {
            return;
        }
        final List<IConstellation> cstList = ctx.getActiveCelestialsHandler().getActiveConstellations().stream().filter(c -> ResearchHelper.getClientProgress().hasConstellationDiscovered(c)).collect((Collector<? super IConstellation, ?, List<IConstellation>>)Collectors.toList());
        final Random rand = new Random(WorldSeedCache.getSeedIfPresent((RegistryKey<World>)world.dimension()).orElse(5863439008313086302L));
        for (int i = 0; i < ctx.getConstellationHandler().getLastTrackedDay(); ++i) {
            rand.nextLong();
        }
        Collections.shuffle(cstList, rand);
        for (int i = 0; i < Math.min(cstList.size(), 12); ++i) {
            final IConstellation cst = cstList.get(i);
            final int offsetX = this.guiLeft + ((i % 2 == 0) ? 8 : 232);
            final int offsetY = this.guiTop + (40 + i / 2 * 23);
            final Rectangle rct = new Rectangle(offsetX, offsetY, 16, 16);
            this.mapRenderedConstellations.put(rct, cst);
            RenderSystem.enableBlend();
            Blending.DEFAULT.apply();
            RenderingConstellationUtils.renderConstellationIntoGUI(Color.WHITE, cst, renderStack, (float)offsetX, (float)offsetY, (float)this.getGuiZLevel(), 16.0f, 16.0f, 0.5, () -> DayTimeHelper.getCurrentDaytimeDistribution(world), true, false);
            RenderSystem.disableBlend();
            if (rct.contains(mouseX, mouseY)) {
                tooltip.add((ITextProperties)cst.getConstellationName());
            }
        }
    }
    
    private FontRenderer renderTileItems(final PoseStack renderStack, final int mouseX, final int mouseY, final List<ITextProperties> tooltip, FontRenderer tooltipRenderer) {
        this.func_230926_e_(100);
        final ItemStack input = this.getTile().getInputStack();
        if (!input.isEmpty()) {
            final Rectangle itemRct = new Rectangle(this.guiLeft + 111, this.guiTop + 8, 16, 16);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)itemRct.x, (double)itemRct.y, (double)this.getGuiZLevel());
            RenderingUtils.renderItemStackGUI(renderStack, input, null);
            renderStack.func_227865_b_();
            if (itemRct.contains(mouseX, mouseY)) {
                final FontRenderer custom = input.getItem().getFontRenderer(input);
                if (custom != null) {
                    tooltipRenderer = custom;
                }
                tooltip.addAll(input.func_82840_a((Player)this.getMinecraft().field_71439_g, (ITooltipFlag)(Minecraft.func_71410_x().field_71474_y.field_82882_x ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)));
            }
        }
        final ItemStack glass = this.getTile().getGlassStack();
        if (!glass.isEmpty()) {
            final Rectangle itemRct2 = new Rectangle(this.guiLeft + 129, this.guiTop + 8, 16, 16);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)itemRct2.x, (double)itemRct2.y, (double)this.getGuiZLevel());
            RenderingUtils.renderItemStackGUI(renderStack, glass, null);
            renderStack.func_227865_b_();
            if (itemRct2.contains(mouseX, mouseY)) {
                final FontRenderer custom2 = glass.getItem().getFontRenderer(glass);
                if (custom2 != null) {
                    tooltipRenderer = custom2;
                }
                tooltip.addAll(glass.func_82840_a((Player)this.getMinecraft().field_71439_g, (ITooltipFlag)(Minecraft.func_71410_x().field_71474_y.field_82882_x ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)));
            }
        }
        this.func_230926_e_(0);
        return tooltipRenderer;
    }
    
    private void renderBox(final PoseStack renderStack, final float offsetX, final float offsetY, final float width, final float height, final Color c) {
        final Random rand = new Random(18L);
        final float r = c.getRed() / 255.0f;
        final float g = c.getGreen() / 255.0f;
        final float b = c.getBlue() / 255.0f;
        final Supplier<Float> alpha = () -> 0.1f + 0.4f * ((MathHelper.func_76126_a(rand.nextInt(200) + ClientScheduler.getClientTick() / 20.0f) + 1.0f) / 2.0f);
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderSystem.disableAlphaTest();
        RenderSystem.lineWidth(2.0f);
        RenderSystem.disableTexture();
        RenderSystem.disableDepthTest();
        RenderingUtils.draw(1, DefaultVertexFormats.field_181706_f, buf -> {
            final Matrix4f offset = renderStack.func_227866_c_().func_227870_a_();
            buf.func_227888_a_(offset, offsetX, offsetY, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            buf.func_227888_a_(offset, offsetX + width, offsetY, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            buf.func_227888_a_(offset, offsetX + width, offsetY, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            buf.func_227888_a_(offset, offsetX + width, offsetY + height, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            buf.func_227888_a_(offset, offsetX + width, offsetY + height, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            buf.func_227888_a_(offset, offsetX, offsetY + height, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            buf.func_227888_a_(offset, offsetX, offsetY + height, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            buf.func_227888_a_(offset, offsetX, offsetY, 0.0f).func_227885_a_(r, g, b, (float)alpha.get()).func_181675_d();
            return;
        });
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.enableAlphaTest();
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
    }
    
    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        if (this.currentlyDrawnConstellations.size() >= 3) {
            final List<DrawnConstellation> copyList = new ArrayList<DrawnConstellation>(this.currentlyDrawnConstellations);
            final PktEngraveGlass engraveGlass = new PktEngraveGlass((RegistryKey<World>)this.getTile().func_145831_w().dimension(), this.getTile().func_174877_v(), copyList);
            PacketChannel.CHANNEL.sendToServer(engraveGlass);
            this.currentlyDrawnConstellations.clear();
        }
    }
    
    @Override
    public boolean func_231044_a_(final double mouseX, final double mouseY, final int button) {
        if (super.func_231044_a_(mouseX, mouseY, button)) {
            return true;
        }
        if (button == 0 && this.dragging == null && this.getTile().hasParchment() && this.getTile().hasUnengravedGlass() && this.currentlyDrawnConstellations.size() < 3) {
            this.tryPick(mouseX, mouseY);
        }
        return false;
    }
    
    @Override
    public boolean func_231048_c_(final double mouseX, final double mouseY, final int click) {
        if (super.func_231048_c_(mouseX, mouseY, click)) {
            return true;
        }
        if (click == 0 && this.dragging != null && this.getTile().hasParchment() && this.getTile().hasUnengravedGlass() && this.currentlyDrawnConstellations.size() < 3) {
            this.tryDrop(mouseX, mouseY);
        }
        return false;
    }
    
    private void tryDrop(final double mouseX, final double mouseY) {
        if (this.dragging != null) {
            if (ScreenRefractionTable.PLACEMENT_GRID.contains(mouseX - this.guiLeft, mouseY - this.guiTop)) {
                final Point gridPoint = new Point((int)Math.round(mouseX), (int)Math.round(mouseY));
                gridPoint.translate(-this.guiLeft, -this.guiTop);
                gridPoint.translate(-ScreenRefractionTable.PLACEMENT_GRID.x, -ScreenRefractionTable.PLACEMENT_GRID.y);
                this.currentlyDrawnConstellations.add(new DrawnConstellation(gridPoint, this.dragging));
            }
            this.dragging = null;
        }
    }
    
    private void tryPick(final double mouseX, final double mouseY) {
        for (final Rectangle r : this.mapRenderedConstellations.keySet()) {
            if (r.contains(mouseX, mouseY)) {
                this.dragging = this.mapRenderedConstellations.get(r);
            }
        }
    }
    
    public boolean func_231177_au__() {
        return false;
    }
    
    static {
        PLACEMENT_GRID = new Rectangle(98, 75, 60, 60);
    }
}
