package hellfirepvp.astralsorcery.client.screen.journal.overlay;

import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPerkTree;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalProgression;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournal;

public abstract class ScreenJournalOverlay extends ScreenJournal
{
    private final ScreenJournal origin;
    
    protected ScreenJournalOverlay(final Component titleIn, final ScreenJournal origin) {
        super(titleIn, origin.getGuiHeight(), origin.getGuiWidth(), -1);
        this.origin = origin;
    }
    
    @Override
    public boolean func_231177_au__() {
        return this.origin.func_231177_au__();
    }
    
    public void func_231158_b_(final Minecraft mc, final int width, final int height) {
        super.func_231158_b_(mc, width, height);
        this.origin.func_231158_b_(mc, width, height);
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        this.origin.func_230430_a_(renderStack, 0, 0, pTicks);
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return true;
    }
    
    public void init() {
        Minecraft.getInstance().func_147108_a((Screen)this.origin);
    }
    
    public void func_231164_f_() {
        super.getTitle();
        if (this.origin instanceof ScreenJournalProgression) {
            ((ScreenJournalProgression)this.origin).expectReInit();
        }
        if (this.origin instanceof ScreenJournalPerkTree) {
            ((ScreenJournalPerkTree)this.origin).expectReinit = true;
        }
    }
    
    @Override
    public boolean func_231042_a_(final char charCode, final int keyModifiers) {
        if (super.func_231042_a_(charCode, keyModifiers)) {
            return true;
        }
        if (Minecraft.getInstance().gui != this && Minecraft.getInstance().gui != this.origin) {
            Minecraft.getInstance().func_147108_a((Screen)this.origin);
            return true;
        }
        return false;
    }
}
