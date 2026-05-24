package hellfirepvp.astralsorcery.client.screen.journal;

import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import net.minecraft.client.gui.screens.Screen;
import org.joml.Matrix4f;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.util.Random;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.item.ItemConstellationPaper;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageAltarRecipe;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import java.util.Collection;
import net.minecraft.network.chat.ITextProperties;
import net.minecraft.network.chat.Component;
import java.util.LinkedList;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.screen.base.NavigationArrowScreen;

public class ScreenJournalConstellationDetail extends ScreenJournal implements NavigationArrowScreen
{
    private final ScreenJournal origin;
    private final IConstellation constellation;
    private final boolean detailed;
    private int doublePageID;
    private int doublePages;
    private List<MoonPhase> activePhases;
    private RenderablePage lastFramePage;
    private Rectangle rectBack;
    private Rectangle rectNext;
    private Rectangle rectPrev;
    private final List<FormattedCharSequence> locTextMain;
    private final List<FormattedCharSequence> locTextRitual;
    private final List<FormattedCharSequence> locTextRefraction;
    private final List<FormattedCharSequence> locTextMantle;
    
    public ScreenJournalConstellationDetail(final ScreenJournal origin, final IConstellation cst) {
        super((Component)cst.getConstellationName(), -1);
        this.doublePageID = 0;
        this.doublePages = 0;
        this.activePhases = null;
        this.lastFramePage = null;
        this.locTextMain = new ArrayList<FormattedCharSequence>();
        this.locTextRitual = new ArrayList<FormattedCharSequence>();
        this.locTextRefraction = new ArrayList<FormattedCharSequence>();
        this.locTextMantle = new ArrayList<FormattedCharSequence>();
        this.origin = origin;
        this.constellation = cst;
        this.field_230712_o_ = Minecraft.getInstance().field_71466_p;
        this.detailed = ResearchHelper.getClientProgress().hasConstellationDiscovered(cst);
        final PlayerProgress playerProgress = ResearchHelper.getClientProgress();
        if (this.detailed) {
            if (playerProgress.getTierReached().isThisLaterOrEqual(ProgressionTier.ATTUNEMENT)) {
                ++this.doublePages;
            }
            if (playerProgress.getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT)) {
                if (!(this.constellation instanceof IMinorConstellation)) {
                    ++this.doublePages;
                }
                ++this.doublePages;
            }
        }
        this.testActivePhases();
        this.buildMainText();
        this.buildEnchText();
        this.buildRitualText();
        this.buildCapeText();
    }
    
    public IConstellation getConstellation() {
        return this.constellation;
    }
    
    private void buildCapeText() {
        if (this.constellation instanceof IWeakConstellation && ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT)) {
            final Component txtMantle = ((IWeakConstellation)this.constellation).getInfoMantleEffect();
            final ITextProperties headTxt = (ITextProperties)new Component("astralsorcery.journal.constellation.mantle");
            this.locTextMantle.add(this.localize(headTxt));
            this.locTextMantle.add(FormattedCharSequence.field_242232_a);
            final List<FormattedCharSequence> lines = new LinkedList<FormattedCharSequence>();
            for (final String segment : txtMantle.getString().split("<NL>")) {
                lines.addAll(this.field_230712_o_.func_238425_b_((ITextProperties)new Component(segment), 175));
                lines.add(FormattedCharSequence.field_242232_a);
            }
            this.locTextMantle.addAll(lines);
            this.locTextMantle.add(FormattedCharSequence.field_242232_a);
        }
    }
    
    private void buildEnchText() {
        if (ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.CONSTELLATION_CRAFT)) {
            final Component txtEnchantments = (Component)this.constellation.getConstellationEnchantmentDescription();
            final ITextProperties headTxt = (ITextProperties)new Component("astralsorcery.journal.constellation.enchantments");
            this.locTextRefraction.add(this.localize(headTxt));
            this.locTextRefraction.add(FormattedCharSequence.field_242232_a);
            final List<FormattedCharSequence> lines = new LinkedList<FormattedCharSequence>();
            for (final String segment : txtEnchantments.getString().split("<NL>")) {
                lines.addAll(this.field_230712_o_.func_238425_b_((ITextProperties)new Component(segment), 175));
                lines.add(FormattedCharSequence.field_242232_a);
            }
            this.locTextRefraction.addAll(lines);
            this.locTextRefraction.add(FormattedCharSequence.field_242232_a);
        }
    }
    
    private void buildRitualText() {
        if (this.constellation instanceof IMinorConstellation) {
            if (ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT)) {
                final Component txtRitual = ((IMinorConstellation)this.constellation).getInfoTraitEffect();
                final ITextProperties headTxt = (ITextProperties)new Component("astralsorcery.journal.constellation.ritual.trait");
                this.locTextRitual.add(this.localize(headTxt));
                this.locTextRitual.add(FormattedCharSequence.field_242232_a);
                final List<FormattedCharSequence> lines = new LinkedList<FormattedCharSequence>();
                for (final String segment : txtRitual.getString().split("<NL>")) {
                    lines.addAll(this.field_230712_o_.func_238425_b_((ITextProperties)new Component(segment), 175));
                    lines.add(FormattedCharSequence.field_242232_a);
                }
                this.locTextRitual.addAll(lines);
            }
        }
        else if (this.constellation instanceof IWeakConstellation) {
            if (ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.ATTUNEMENT)) {
                final Component txtRitual = ((IWeakConstellation)this.constellation).getInfoRitualEffect();
                final ITextProperties headTxt = (ITextProperties)new Component("astralsorcery.journal.constellation.ritual");
                this.locTextRitual.add(this.localize(headTxt));
                this.locTextRitual.add(FormattedCharSequence.field_242232_a);
                final List<FormattedCharSequence> lines = new LinkedList<FormattedCharSequence>();
                for (final String segment : txtRitual.getString().split("<NL>")) {
                    lines.addAll(this.field_230712_o_.func_238425_b_((ITextProperties)new Component(segment), 175));
                    lines.add(FormattedCharSequence.field_242232_a);
                }
                this.locTextRitual.addAll(lines);
                this.locTextRitual.add(FormattedCharSequence.field_242232_a);
            }
            if (ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT)) {
                final Component txtCorruptedRitual = ((IWeakConstellation)this.constellation).getInfoCorruptedRitualEffect();
                final ITextProperties headTxt = (ITextProperties)new Component("astralsorcery.journal.constellation.corruption");
                this.locTextRitual.add(this.localize(headTxt));
                this.locTextRitual.add(FormattedCharSequence.field_242232_a);
                final List<FormattedCharSequence> lines = new LinkedList<FormattedCharSequence>();
                for (final String segment : txtCorruptedRitual.getString().split("<NL>")) {
                    lines.addAll(this.field_230712_o_.func_238425_b_((ITextProperties)new Component(segment), 175));
                    lines.add(FormattedCharSequence.field_242232_a);
                }
                this.locTextRitual.addAll(lines);
                this.locTextRitual.add(FormattedCharSequence.field_242232_a);
            }
        }
    }
    
    private void buildMainText() {
        final Component txtDescription = (Component)this.constellation.getConstellationDescription();
        final List<FormattedCharSequence> lines = new LinkedList<FormattedCharSequence>();
        for (final String segment : txtDescription.getString().split("<NL>")) {
            lines.addAll(this.field_230712_o_.func_238425_b_((ITextProperties)new Component(segment), 175));
            lines.add(FormattedCharSequence.field_242232_a);
        }
        this.locTextMain.addAll(lines);
    }
    
    private void testActivePhases() {
        final WorldContext ctx = SkyHandler.getContext((World)Minecraft.getInstance().field_71441_e, LogicalSide.CLIENT);
        if (ctx == null) {
            return;
        }
        this.activePhases = new LinkedList<MoonPhase>();
        for (final MoonPhase phase : MoonPhase.values()) {
            if (ctx.getConstellationHandler().isActiveInPhase(this.constellation, phase)) {
                this.activePhases.add(phase);
            }
        }
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        this.lastFramePage = null;
        if (this.doublePageID == 0) {
            this.drawCstBackground(renderStack);
            this.drawDefault(renderStack, TexturesAS.TEX_GUI_BOOK_FRAME_LEFT, mouseX, mouseY);
        }
        else {
            this.drawDefault(renderStack, TexturesAS.TEX_GUI_BOOK_BLANK, mouseX, mouseY);
        }
        this.drawNavArrows(renderStack, pTicks, mouseX, mouseY);
        this.func_230926_e_(120);
        switch (this.doublePageID) {
            case 0: {
                this.drawPageConstellation(renderStack, pTicks);
                this.drawPagePhaseInformation(renderStack);
                this.drawPageExtendedInformation(renderStack);
                break;
            }
            case 1: {
                this.drawRefractionTableInformation(renderStack, mouseX, mouseY, pTicks);
                break;
            }
            case 2: {
                this.drawCapeInformationPages(renderStack, mouseX, mouseY, pTicks);
                if (this.constellation instanceof IMinorConstellation) {
                    this.drawConstellationPaperRecipePage(renderStack, mouseX, mouseY, pTicks);
                    break;
                }
                break;
            }
            case 3: {
                this.drawConstellationPaperRecipePage(renderStack, mouseX, mouseY, pTicks);
                break;
            }
        }
        this.func_230926_e_(0);
    }
    
    private void drawRefractionTableInformation(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        for (int i = 0; i < this.locTextRitual.size(); ++i) {
            final FormattedCharSequence line = this.locTextRitual.get(i);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(this.guiLeft + 30), (double)(this.guiTop + 30 + i * 10), (double)this.getGuiZLevel());
            RenderingDrawUtils.renderStringAt(line, renderStack, this.field_230712_o_, -3355444, true);
            renderStack.func_227865_b_();
        }
        for (int i = 0; i < this.locTextRefraction.size(); ++i) {
            final FormattedCharSequence line = this.locTextRefraction.get(i);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(this.guiLeft + 220), (double)(this.guiTop + 30 + i * 10), (double)this.getGuiZLevel());
            RenderingDrawUtils.renderStringAt(line, renderStack, this.field_230712_o_, -3355444, true);
            renderStack.func_227865_b_();
        }
    }
    
    private void drawCapeInformationPages(final PoseStack renderStack, final int mouseX, final int mouseY, final float partialTicks) {
        for (int i = 0; i < this.locTextMantle.size(); ++i) {
            final FormattedCharSequence line = this.locTextMantle.get(i);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(this.guiLeft + 30), (double)(this.guiTop + 30 + i * 10), (double)this.getGuiZLevel());
            RenderingDrawUtils.renderStringAt(line, renderStack, this.field_230712_o_, -3355444, true);
            renderStack.func_227865_b_();
        }
        if (ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT)) {
            final SimpleAltarRecipe recipe = RecipeHelper.findAltarRecipeResult(stack -> stack.getItem() instanceof ItemMantle && this.constellation.equals(ItemsAS.MANTLE.getConstellation(stack)));
            if (recipe != null) {
                (this.lastFramePage = new RenderPageAltarRecipe(null, -1, recipe)).render(renderStack, (float)(this.guiLeft + 220), (float)(this.guiTop + 20), (float)this.getGuiZLevel(), partialTicks, (float)mouseX, (float)mouseY);
                this.lastFramePage.postRender(renderStack, (float)(this.guiLeft + 220), (float)(this.guiTop + 20), (float)this.getGuiZLevel(), partialTicks, (float)mouseX, (float)mouseY);
            }
        }
    }
    
    private void drawConstellationPaperRecipePage(final PoseStack renderStack, final int mouseX, final int mouseY, final float partialTicks) {
        if (ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT)) {
            final SimpleAltarRecipe recipe = RecipeHelper.findAltarRecipeResult(stack -> stack.getItem() instanceof ItemConstellationPaper && this.constellation.equals(ItemsAS.CONSTELLATION_PAPER.getConstellation(stack)));
            if (recipe != null) {
                (this.lastFramePage = new RenderPageAltarRecipe(null, -1, recipe)).render(renderStack, (float)(this.guiLeft + 30), (float)(this.guiTop + 20), (float)this.getGuiZLevel(), partialTicks, (float)mouseX, (float)mouseY);
                this.lastFramePage.postRender(renderStack, (float)(this.guiLeft + 30), (float)(this.guiTop + 20), (float)this.getGuiZLevel(), partialTicks, (float)mouseX, (float)mouseY);
            }
        }
    }
    
    private void drawPageExtendedInformation(final PoseStack renderStack) {
        ITextProperties info = (ITextProperties)this.getConstellation().getConstellationTag();
        if (!this.detailed) {
            info = (ITextProperties)new Component("astralsorcery.journal.constellation.unknown");
        }
        final int width = this.field_230712_o_.func_238414_a_(info);
        final float chX = 305.0f - width / 2.0f;
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(this.guiLeft + chX), (double)(this.guiTop + 44), (double)this.getGuiZLevel());
        RenderingDrawUtils.renderStringAt(this.field_230712_o_, renderStack, info, -3355444);
        renderStack.func_227865_b_();
        if (this.detailed && !this.locTextMain.isEmpty()) {
            final int offsetX = 220;
            final int offsetY = 77;
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(this.guiLeft + offsetX), (double)(this.guiTop + offsetY), (double)this.getGuiZLevel());
            for (final FormattedCharSequence line : this.locTextMain) {
                RenderingDrawUtils.renderStringAt(this.field_230712_o_, renderStack, line, -3355444);
                renderStack.func_227861_a_(0.0, 13.0, 0.0);
            }
            renderStack.func_227865_b_();
        }
    }
    
    private void drawPagePhaseInformation(final PoseStack renderStack) {
        if (this.activePhases == null) {
            this.testActivePhases();
            if (this.activePhases == null) {
                return;
            }
        }
        final List<MoonPhase> phases = this.activePhases;
        if (phases.isEmpty()) {
            final ITextProperties none = (ITextProperties)new Component("astralsorcery.journal.constellation.unknown");
            final float scale = 1.8f;
            final float length = this.field_230712_o_.func_238414_a_(none) * scale;
            final float offsetLeft = this.guiLeft + 296 - length / 2.0f;
            final int offsetTop = this.guiTop + 199;
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(offsetLeft + 10.0f), (double)offsetTop, (double)this.getGuiZLevel());
            renderStack.func_227862_a_(scale, scale, scale);
            RenderingDrawUtils.renderStringAt(none, renderStack, this.field_230712_o_, -857874979, true);
            renderStack.func_227865_b_();
        }
        else {
            final boolean known = ResearchHelper.getClientProgress().hasConstellationDiscovered(this.constellation);
            final int size = 19;
            final int offsetX = 95 + this.field_230708_k_ / 2 - MoonPhase.values().length * (size + 2) / 2;
            final int offsetY = 199 + this.guiTop;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            final MoonPhase[] mPhases = MoonPhase.values();
            for (int i = 0; i < mPhases.length; ++i) {
                final MoonPhase phase = mPhases[i];
                final int index = i;
                phase.getTexture().bindTexture();
                float brightness;
                if (known && this.activePhases.contains(phase)) {
                    Blending.PREALPHA.apply();
                    brightness = 1.0f;
                }
                else {
                    RenderSystem.defaultBlendFunc();
                    brightness = 0.7f;
                }
                RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(offsetX + index * (size + 2)), (float)offsetY, (float)this.getGuiZLevel(), (float)size, (float)size).color(brightness, brightness, brightness, brightness).draw());
            }
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }
    
    private void drawPageConstellation(final PoseStack renderStack, final float partial) {
        final ITextProperties cstName = (ITextProperties)this.constellation.getConstellationName();
        int width = this.field_230712_o_.func_238414_a_(cstName);
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(this.guiLeft + (305.0f - width * 1.8f / 2.0f)), (double)(this.guiTop + 26), (double)this.getGuiZLevel());
        renderStack.func_227862_a_(1.8f, 1.8f, 1.0f);
        RenderingDrawUtils.renderStringAt(cstName, renderStack, this.field_230712_o_, -3947581, true);
        renderStack.func_227865_b_();
        ITextProperties dstInfo = (ITextProperties)this.constellation.getConstellationTypeDescription();
        if (!this.detailed) {
            dstInfo = (ITextProperties)new Component("astralsorcery.journal.constellation.unknown");
        }
        width = this.field_230712_o_.func_238414_a_(dstInfo);
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(this.guiLeft + (305.0f - width / 2.0f)), (double)(this.guiTop + 219), (double)this.getGuiZLevel());
        RenderingDrawUtils.renderStringAt(dstInfo, renderStack, this.field_230712_o_, -2236963, true);
        renderStack.func_227865_b_();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final Random rand = new Random(4726142277924544921L);
        final boolean known = ResearchHelper.getClientProgress().hasConstellationDiscovered(this.constellation);
        RenderingConstellationUtils.renderConstellationIntoGUI(known ? this.constellation.getConstellationColor() : this.constellation.getTierRenderColor(), this.constellation, renderStack, (float)(this.guiLeft + 40), (float)(this.guiTop + 60), (float)this.getGuiZLevel(), 150.0f, 150.0f, 2.0, () -> 0.6f + 0.4f * RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), partial, 12 + rand.nextInt(10)), true, false);
        RenderSystem.disableBlend();
    }
    
    private void drawNavArrows(final PoseStack renderStack, final float partialTicks, final int mouseX, final int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        this.rectNext = null;
        this.rectPrev = null;
        this.rectBack = this.drawArrow(renderStack, this.guiLeft + 197, this.guiTop + 230, this.getGuiZLevel(), Type.LEFT, mouseX, mouseY, partialTicks);
        if (this.doublePageID - 1 >= 0) {
            this.rectPrev = this.drawArrow(renderStack, this.guiLeft + 25, this.guiTop + 220, this.getGuiZLevel(), Type.LEFT, mouseX, mouseY, partialTicks);
        }
        if (this.doublePageID + 1 <= this.doublePages) {
            this.rectNext = this.drawArrow(renderStack, this.guiLeft + 367, this.guiTop + 220, this.getGuiZLevel(), Type.RIGHT, mouseX, mouseY, partialTicks);
        }
        RenderSystem.disableBlend();
    }
    
    private void drawCstBackground(final PoseStack renderStack) {
        TexturesAS.TEX_BLACK.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> {
            final Matrix4f offset = renderStack.func_227866_c_().func_227870_a_();
            buf.func_227888_a_(offset, (float)(this.guiLeft + 15), (float)(this.guiTop + 240), (float)this.getGuiZLevel()).func_227885_a_(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(0.0f, 1.0f).func_181675_d();
            buf.func_227888_a_(offset, (float)(this.guiLeft + 200), (float)(this.guiTop + 240), (float)this.getGuiZLevel()).func_227885_a_(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(1.0f, 1.0f).func_181675_d();
            buf.func_227888_a_(offset, (float)(this.guiLeft + 200), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).func_227885_a_(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(1.0f, 0.0f).func_181675_d();
            buf.func_227888_a_(offset, (float)(this.guiLeft + 15), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).func_227885_a_(1.0f, 1.0f, 1.0f, 1.0f).func_225583_a_(0.0f, 0.0f).func_181675_d();
            return;
        });
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        TexturesAS.TEX_GUI_BACKGROUND_CONSTELLATIONS.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> {
            final Matrix4f offset2 = renderStack.func_227866_c_().func_227870_a_();
            buf.func_227888_a_(offset2, (float)(this.guiLeft + 15), (float)(this.guiTop + 240), (float)this.getGuiZLevel()).func_227885_a_(0.8f, 0.8f, 1.0f, 0.5f).func_225583_a_(0.3f, 0.9f).func_181675_d();
            buf.func_227888_a_(offset2, (float)(this.guiLeft + 200), (float)(this.guiTop + 240), (float)this.getGuiZLevel()).func_227885_a_(0.8f, 0.8f, 1.0f, 0.5f).func_225583_a_(0.7f, 0.9f).func_181675_d();
            buf.func_227888_a_(offset2, (float)(this.guiLeft + 200), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).func_227885_a_(0.8f, 0.8f, 1.0f, 0.5f).func_225583_a_(0.7f, 0.1f).func_181675_d();
            buf.func_227888_a_(offset2, (float)(this.guiLeft + 15), (float)(this.guiTop + 10), (float)this.getGuiZLevel()).func_227885_a_(0.8f, 0.8f, 1.0f, 0.5f).func_225583_a_(0.3f, 0.1f).func_181675_d();
            return;
        });
        RenderSystem.disableBlend();
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return true;
    }
    
    public void func_231175_as__() {
        Minecraft.getInstance().func_147108_a((Screen)this.origin);
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
        if (this.rectBack != null && this.rectBack.contains(mouseX, mouseY)) {
            Minecraft.getInstance().func_147108_a((Screen)this.origin);
            return true;
        }
        if (this.rectPrev != null && this.rectPrev.contains(mouseX, mouseY)) {
            if (this.doublePageID >= 1) {
                --this.doublePageID;
            }
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        if (this.rectNext != null && this.rectNext.contains(mouseX, mouseY)) {
            if (this.doublePageID <= this.doublePages - 1) {
                ++this.doublePageID;
            }
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            return true;
        }
        return this.doublePageID != 0 && this.lastFramePage != null && this.lastFramePage.propagateMouseClick(mouseX, mouseY);
    }
}
