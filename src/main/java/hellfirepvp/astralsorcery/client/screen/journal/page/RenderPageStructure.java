// 
// Decompiled by Procyon v0.6.0
// 

package hellfirepvp.astralsorcery.client.screen.journal.page;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import java.awt.Rectangle;
import com.google.common.collect.Lists;
import net.minecraft.util.text.TranslationTextComponent;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import java.awt.geom.Point2D;
import net.minecraft.client.gui.Font;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.observerlib.api.block.MatchableState;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.vector.Vector3f;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.StringTextComponent;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import java.awt.geom.Rectangle2D;
import java.util.Optional;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.observerlib.api.structure.Structure;
import hellfirepvp.observerlib.api.client.StructureRenderer;

public class RenderPageStructure extends RenderablePage
{
    private final StructureRenderer structureRenderer;
    private final Structure structure;
    private final Vector3 shift;
    private final List<Tuple<ItemStack, FormattedCharSequence>> contentStacks;
    private final FormattedCharSequence name;
    private Optional<Integer> drawSlice;
    private Rectangle2D.Float switchView;
    private Rectangle2D.Float sliceUp;
    private Rectangle2D.Float sliceDown;
    private Rectangle2D.Float switchRequiredAir;
    private long totalRenderFrame;
    private boolean showAirBlocks;
    
    public RenderPageStructure(@Nullable final ResearchNode node, final int nodePage, final Structure structure, @Nullable final FormattedCharSequence name, @Nonnull final Vector3 shift) {
        super(node, nodePage);
        this.drawSlice = Optional.empty();
        this.switchView = null;
        this.sliceUp = null;
        this.sliceDown = null;
        this.switchRequiredAir = null;
        this.totalRenderFrame = 0L;
        this.showAirBlocks = false;
        this.structure = structure;
        this.structureRenderer = new StructureRenderer(this.structure).setIsolateIndividualBlock(true);
        this.name = name;
        this.shift = shift;
        this.contentStacks = new ArrayList<Tuple<ItemStack, FormattedCharSequence>>();
        structure.getAsStacks((IBlockReader)this.structureRenderer.getRenderWorld(), (PlayerEntity)Minecraft.func_71410_x().field_71439_g).forEach(stack -> {
            final ItemStack display = ItemUtils.copyStackWithSize(stack, 1);
            new StringTextComponent(stack.func_190916_E() + "x ");
            final StringTextComponent stringTextComponent;
            final FormattedCharSequence description = (FormattedCharSequence)stringTextComponent.func_230529_a_(stack.func_200301_q());
            this.contentStacks.add((Tuple<ItemStack, FormattedCharSequence>)new Tuple((Object)display, (Object)description));
        });
    }
    
    @Override
    public void render(final MatrixStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        ++this.totalRenderFrame;
        this.renderStructure(renderStack, x, y, pTicks);
        final float shift = this.renderSizeDescription(renderStack, x, y + 5.0f, z);
        if (this.name != null) {
            this.renderHeadline(renderStack, x + shift, y + 5.0f, z, this.name);
        }
        this.renderSliceButtons(renderStack, x, y + 10.0f, z, mouseX, mouseY);
    }
    
    private void renderSliceButtons(final MatrixStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float mouseX, final float mouseY) {
        TexturesAS.TEX_GUI_BOOK_STRUCTURE_ICONS.bindTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        this.switchView = null;
        this.sliceDown = null;
        this.sliceUp = null;
        this.switchRequiredAir = null;
        this.switchView = new Rectangle2D.Float(offsetX + 152.0f, offsetY + 10.0f, 16.0f, 16.0f);
        final float u = this.drawSlice.isPresent() ? 0.5f : 0.0f;
        RenderingGuiUtils.drawTexturedRect(renderStack, this.switchView.x, this.switchView.y, zLevel, this.switchView.width, this.switchView.height, u, 0.0f, 0.5f, 0.25f);
        if (this.drawSlice.isPresent()) {
            int yLevel = this.drawSlice.get();
            final int minSlice = this.getCurrentMinSlice();
            final int maxSlice = this.getCurrentMaxSlice();
            if (yLevel < minSlice) {
                yLevel = maxSlice;
            }
            if (yLevel > maxSlice) {
                yLevel = maxSlice;
            }
            if (minSlice <= yLevel - 1) {
                this.sliceDown = new Rectangle2D.Float(offsetX + 160.0f, offsetY + 28.0f, 11.0f, 16.0f);
                renderStack.func_227860_a_();
                renderStack.translate((double)(this.sliceDown.x + this.sliceDown.width / 2.0f), (double)(this.sliceDown.y + this.sliceDown.height / 2.0f), (double)zLevel);
                float v = 0.5f;
                if (this.sliceDown.contains(mouseX, mouseY)) {
                    v = 0.25f;
                    renderStack.func_227862_a_(1.1f, 1.1f, 1.0f);
                }
                renderStack.translate((double)(-this.sliceDown.width / 2.0f), (double)(-this.sliceDown.height / 2.0f), 0.0);
                RenderingGuiUtils.drawTexturedRect(renderStack, this.sliceDown.width, this.sliceDown.height, 0.375f, v, 0.34375f, 0.25f);
                renderStack.func_227865_b_();
            }
            if (maxSlice >= yLevel + 1) {
                this.sliceUp = new Rectangle2D.Float(offsetX + 148.0f, offsetY + 28.0f, 11.0f, 16.0f);
                renderStack.func_227860_a_();
                renderStack.translate((double)(this.sliceUp.x + this.sliceUp.width / 2.0f), (double)(this.sliceUp.y + this.sliceUp.height / 2.0f), (double)zLevel);
                float v = 0.5f;
                if (this.sliceUp.contains(mouseX, mouseY)) {
                    v = 0.25f;
                    renderStack.func_227862_a_(1.1f, 1.1f, 1.0f);
                }
                renderStack.translate((double)(-this.sliceUp.width / 2.0f), (double)(-this.sliceUp.height / 2.0f), 0.0);
                RenderingGuiUtils.drawTexturedRect(renderStack, this.sliceUp.width, this.sliceUp.height, 0.0f, v, 0.34375f, 0.25f);
                renderStack.func_227865_b_();
            }
        }
        this.switchRequiredAir = new Rectangle2D.Float(offsetX + 134.0f, offsetY + 10.0f, 16.0f, 16.0f);
        RenderingGuiUtils.drawTexturedRect(renderStack, this.switchRequiredAir.x, this.switchRequiredAir.y, zLevel, this.switchRequiredAir.width, this.switchRequiredAir.height, 0.0f, 0.75f, 0.5f, 0.25f);
        if (this.showAirBlocks) {
            BlockAtlasTexture.getInstance().bindTexture();
            RenderSystem.depthMask(false);
            RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX, buf -> {
                renderStack.func_227860_a_();
                renderStack.translate((double)(this.switchRequiredAir.x + 13.0f), (double)(this.switchRequiredAir.y + 11.0f), (double)(zLevel + 60.0f));
                renderStack.func_227862_a_(7.0f, -7.0f, 7.0f);
                renderStack.func_227863_a_(new org.joml.Vector3f(1, 0, 0).func_229187_a_(30.0f));
                renderStack.func_227863_a_(new org.joml.Vector3f(0, 1, 0).func_229187_a_(225.0f));
                RenderingUtils.renderSimpleBlockModel(Blocks.GLASS.func_176223_P(), renderStack, (IVertexBuilder)buf);
                renderStack.func_227865_b_();
                return;
            });
            RenderSystem.depthMask(true);
        }
        RenderSystem.disableBlend();
    }
    
    private int getCurrentMinSlice() {
        final int minSlice = this.structure.getMinimumOffset().func_177956_o();
        if (!this.showAirBlocks) {
            for (int yy = minSlice; yy <= this.structure.getMaximumOffset().func_177956_o(); ++yy) {
                final boolean onlyAir = this.structure.getStructureSlice(yy).stream().allMatch(tpl -> tpl.func_76340_b().equals(MatchableState.REQUIRES_AIR));
                if (!onlyAir) {
                    return yy;
                }
            }
        }
        return minSlice;
    }
    
    private int getCurrentMaxSlice() {
        final int maxSlice = this.structure.getMaximumOffset().func_177956_o();
        if (!this.showAirBlocks) {
            for (int yy = maxSlice; yy >= this.structure.getMinimumOffset().func_177956_o(); --yy) {
                final boolean onlyAir = this.structure.getStructureSlice(yy).stream().allMatch(tpl -> tpl.func_76340_b().equals(MatchableState.REQUIRES_AIR));
                if (!onlyAir) {
                    return yy;
                }
            }
        }
        return maxSlice;
    }
    
    private void renderHeadline(final MatrixStack renderStack, final float offsetX, final float offsetY, final float zLevel, final FormattedCharSequence title) {
        final float scale = 1.3f;
        RenderSystem.disableDepthTest();
        renderStack.func_227860_a_();
        renderStack.translate((double)offsetX, (double)offsetY, (double)zLevel);
        renderStack.func_227862_a_(scale, scale, scale);
        RenderingDrawUtils.renderStringAt(title, renderStack, null, 14540253, true);
        renderStack.func_227865_b_();
        RenderSystem.enableDepthTest();
    }
    
    private float renderSizeDescription(final MatrixStack renderStack, final float offsetX, final float offsetY, final float zLevel) {
        final Vector3 size = new Vector3(this.structure.getMaximumOffset()).subtract(this.structure.getMinimumOffset()).add(1.0f, 1.0f, 1.0f);
        final Font fr = RenderablePage.getFontRenderer();
        final float scale = 1.3f;
        final FormattedCharSequence description = (FormattedCharSequence)new StringTextComponent(String.format("%s - %s - %s", size.getBlockX(), size.getBlockY(), size.getBlockZ()));
        final float length = fr.func_238414_a_(description) * scale;
        RenderSystem.disableDepthTest();
        renderStack.func_227860_a_();
        renderStack.translate((double)offsetX, (double)offsetY, (double)zLevel);
        renderStack.func_227862_a_(scale, scale, scale);
        RenderingDrawUtils.renderStringAt(description, renderStack, fr, 14540253, true);
        renderStack.func_227865_b_();
        this.drawSlice.ifPresent(yLevel -> {
            final int min = this.getCurrentMinSlice();
            final int max = this.getCurrentMaxSlice();
            final int height = max - min;
            final int level = yLevel - min;
            new StringTextComponent(String.format("%s / %s", level + 1, height + 1));
            final StringTextComponent stringTextComponent;
            final FormattedCharSequence slice = (FormattedCharSequence)stringTextComponent;
            renderStack.func_227860_a_();
            renderStack.translate((double)offsetX, (double)(offsetY + 14.0f), (double)zLevel);
            renderStack.func_227862_a_(scale, scale, scale);
            RenderingDrawUtils.renderStringAt(slice, renderStack, fr, 14540253, true);
            renderStack.func_227865_b_();
            return;
        });
        RenderSystem.enableDepthTest();
        return length + 8.0f;
    }
    
    private void renderStructure(final MatrixStack renderStack, final float offsetX, final float offsetY, final float pTicks) {
        final Point2D.Double renderOffset = this.renderOffset(offsetX + 8.0f, offsetY);
        this.structureRenderer.setRenderWithRequiredAir(this.showAirBlocks);
        this.structureRenderer.render3DSliceGUI(renderStack, renderOffset.x + this.shift.getX(), renderOffset.y + this.shift.getY(), pTicks, (Optional)this.drawSlice);
        this.structureRenderer.setRenderWithRequiredAir(false);
    }
    
    private Point2D.Double renderOffset(final float stdPageOffsetX, final float stdPageOffsetY) {
        return new Point2D.Double(stdPageOffsetX + 78.75, stdPageOffsetY + 132.0);
    }
    
    @Override
    public void postRender(final MatrixStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        renderStack.func_227860_a_();
        renderStack.translate((double)(x + 160.0f), (double)(y + 10.0f), (double)z);
        final Rectangle rect = RenderingDrawUtils.drawInfoStar(renderStack, IDrawRenderTypeBuffer.defaultBuffer(), 15.0f, pTicks);
        rect.translate((int)(x + 160.0f), (int)(y + 10.0f));
        renderStack.func_227865_b_();
        if (rect.contains(mouseX, mouseY)) {
            RenderingDrawUtils.renderBlueTooltip(renderStack, x + 160.0f, y + 10.0f, z + 650.0f, this.contentStacks, RenderablePage.getFontRenderer(), false);
        }
        if (this.switchView != null && this.switchView.contains(mouseX, mouseY)) {
            final FormattedCharSequence switchInfo = (FormattedCharSequence)new TranslationTextComponent("astralsorcery.journal.structure.switch_view");
            RenderingDrawUtils.renderBlueTooltipComponents(renderStack, this.switchView.x + this.switchView.width / 2.0f, this.switchView.y + this.switchView.height / 2.0f, z + 500.0f, Lists.newArrayList((Object[])new FormattedCharSequence[] { switchInfo }), RenderablePage.getFontRenderer(), false);
        }
        if (this.switchRequiredAir != null && this.switchRequiredAir.contains(mouseX, mouseY)) {
            final FormattedCharSequence switchInfo = (FormattedCharSequence)new TranslationTextComponent("astralsorcery.journal.structure.required_air");
            RenderingDrawUtils.renderBlueTooltipComponents(renderStack, this.switchRequiredAir.x + this.switchRequiredAir.width / 2.0f, this.switchRequiredAir.y + this.switchRequiredAir.height / 2.0f, z + 500.0f, Lists.newArrayList((Object[])new FormattedCharSequence[] { switchInfo }), RenderablePage.getFontRenderer(), false);
        }
    }
    
    @Override
    public boolean propagateMouseDrag(final double mouseDX, final double mouseDZ) {
        this.structureRenderer.rotateFromMouseDrag((float)mouseDX, (float)mouseDZ);
        return true;
    }
    
    @Override
    public boolean propagateMouseClick(final double mouseX, final double mouseZ) {
        if (this.switchView != null && this.switchView.contains(mouseX, mouseZ)) {
            if (this.drawSlice.isPresent()) {
                this.drawSlice = Optional.empty();
            }
            else {
                this.drawSlice = Optional.of(this.getCurrentMinSlice());
            }
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        if (this.sliceUp != null && this.drawSlice.isPresent() && this.sliceUp.contains(mouseX, mouseZ)) {
            this.drawSlice = Optional.of(this.drawSlice.get() + 1);
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        if (this.sliceDown != null && this.drawSlice.isPresent() && this.sliceDown.contains(mouseX, mouseZ)) {
            this.drawSlice = Optional.of(this.drawSlice.get() - 1);
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        if (this.switchRequiredAir != null && this.switchRequiredAir.contains(mouseX, mouseZ)) {
            this.showAirBlocks = !this.showAirBlocks;
            if (this.drawSlice.isPresent()) {
                int yLevel = this.drawSlice.get();
                final int minSlice = this.getCurrentMinSlice();
                final int maxSlice = this.getCurrentMaxSlice();
                if (yLevel < minSlice) {
                    yLevel = maxSlice;
                }
                if (yLevel > maxSlice) {
                    yLevel = maxSlice;
                }
                this.drawSlice = Optional.of(yLevel);
            }
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        return false;
    }
}
