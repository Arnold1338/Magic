package hellfirepvp.astralsorcery.client.screen;

import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import net.minecraft.network.chat.Component;
import java.util.Collections;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.util.Blending;
import net.minecraft.network.chat.MutableComponent;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.network.chat.ITextProperties;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import java.util.ArrayList;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;

public class ScreenConstellationPaper extends WidthHeightScreen
{
    private final IConstellation constellation;
    private List<MoonPhase> phases;
    
    public ScreenConstellationPaper(final IConstellation cst) {
        super((Component)cst.getConstellationName(), 344, 275);
        this.phases = null;
        this.constellation = cst;
        this.resolvePhases();
    }
    
    private void resolvePhases() {
        final WorldContext ctx = SkyHandler.getContext((World)Minecraft.getInstance().field_71441_e, LogicalSide.CLIENT);
        if (ctx != null) {
            this.phases = new ArrayList<MoonPhase>();
            for (final MoonPhase phase : MoonPhase.values()) {
                if (ctx.getConstellationHandler().isActiveInPhase(this.constellation, phase)) {
                    this.phases.add(phase);
                }
            }
        }
    }
    
    public void func_231164_f_() {
        super.func_231164_f_();
        SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_CLOSE, 1.0f, 1.0f);
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return true;
    }
    
    public boolean func_231177_au__() {
        return false;
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        RenderSystem.enableDepthTest();
        this.drawWHRect(renderStack, TexturesAS.TEX_GUI_CONSTELLATION_PAPER);
        this.drawHeader(renderStack);
        this.drawConstellation(renderStack);
        this.drawPhaseInformation(renderStack);
    }
    
    private void drawHeader(final PoseStack renderStack) {
        final MutableComponent name = this.constellation.getConstellationName();
        final float length = this.field_230712_o_.func_238414_a_((ITextProperties)name) * 1.8f;
        final double offsetLeft = (this.field_230708_k_ >> 1) - length / 2.0f;
        final int offsetTop = this.guiTop + 45;
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(offsetLeft + 2.0, (double)offsetTop, (double)this.getGuiZLevel());
        renderStack.func_227862_a_(1.8f, 1.8f, 1.0f);
        RenderingDrawUtils.renderStringAt((ITextProperties)name, renderStack, this.field_230712_o_, -1437774515, false);
        renderStack.func_227865_b_();
    }
    
    private void drawConstellation(final PoseStack renderStack) {
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderingConstellationUtils.renderConstellationIntoGUI(ColorsAS.CONSTELLATION_TYPE_BLANK, this.constellation, renderStack, this.field_230708_k_ / 2.0f - 72.5f, (float)(this.guiTop + 84), (float)this.getGuiZLevel(), 145.0f, 145.0f, 2.0, () -> 0.5f, true, false);
        RenderSystem.disableBlend();
    }
    
    private void drawPhaseInformation(final PoseStack renderStack) {
        if (this.phases == null) {
            this.resolvePhases();
        }
        final List<MoonPhase> phases = (this.phases == null) ? Collections.emptyList() : this.phases;
        if (phases.isEmpty()) {
            final ITextProperties text = (ITextProperties)new Component("astralsorcery.journal.constellation.unknown");
            RenderingDrawUtils.renderStringCentered(Minecraft.getInstance().field_71466_p, renderStack, text, this.guiLeft + this.guiWidth / 2 + 25, this.guiTop + 239, 1.8f, -1437774515);
        }
        else {
            final int size = 16;
            final int offsetX = this.field_230708_k_ / 2 - phases.size() * (size + 2) / 2;
            final int offsetY = this.guiTop + 237;
            for (int i = 0; i < phases.size(); ++i) {
                phases.get(i).getTexture().bindTexture();
                RenderSystem.enableBlend();
                Blending.DEFAULT.apply();
                RenderingGuiUtils.drawRect(renderStack, (float)(offsetX + i * (size + 2)), (float)offsetY, (float)this.getGuiZLevel(), (float)size, (float)size);
                RenderSystem.disableBlend();
            }
        }
    }
}
