package hellfirepvp.astralsorcery.client.screen.base;

import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Rectangle;
import net.minecraft.network.chat.Component;

public class WidthHeightScreen extends InputScreen
{
    protected final int guiHeight;
    protected final int guiWidth;
    protected int guiLeft;
    protected int guiTop;
    protected boolean closeWithInventoryKey;
    
    protected WidthHeightScreen(final Component titleIn, final int guiHeight, final int guiWidth) {
        super(titleIn);
        this.closeWithInventoryKey = true;
        this.guiHeight = guiHeight;
        this.guiWidth = guiWidth;
    }
    
    protected void func_231160_c_() {
        super.func_231160_c_();
        this.initComponents();
    }
    
    public int getGuiLeft() {
        return this.guiLeft;
    }
    
    public int getGuiTop() {
        return this.guiTop;
    }
    
    public int getGuiZLevel() {
        return this.func_230927_p_();
    }
    
    public int getGuiWidth() {
        return this.guiWidth;
    }
    
    public int getGuiHeight() {
        return this.guiHeight;
    }
    
    public Rectangle getGuiBox() {
        return new Rectangle(this.getGuiLeft(), this.getGuiTop(), this.getGuiWidth(), this.getGuiHeight());
    }
    
    private void initComponents() {
        this.guiLeft = this.field_230708_k_ / 2 - this.guiWidth / 2;
        this.guiTop = this.field_230709_l_ / 2 - this.guiHeight / 2;
    }
    
    protected void drawWHRect(final PoseStack renderStack, final AbstractRenderableTexture resource) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        resource.bindTexture();
        RenderingGuiUtils.drawRect(renderStack, (float)this.guiLeft, (float)this.guiTop, (float)this.func_230927_p_(), (float)this.guiWidth, (float)this.guiHeight);
        RenderSystem.disableAlphaTest();
    }
    
    public boolean func_231042_a_(final char charCode, final int keyModifiers) {
        if (super.func_231042_a_(charCode, keyModifiers)) {
            return true;
        }
        if (this.closeWithInventoryKey && Minecraft.getInstance().field_71474_y.field_151445_Q.func_151470_d()) {
            this.func_231175_as__();
            if (Minecraft.getInstance().field_71462_r == null) {
                Minecraft.getInstance().field_71417_B.func_198034_i();
            }
        }
        return false;
    }
    
    @Override
    public boolean func_231044_a_(final double mouseX, final double mouseY, final int button) {
        if (super.func_231044_a_(mouseX, mouseY, button)) {
            return true;
        }
        if (button == 1 && this.shouldRightClickCloseScreen(mouseX, mouseY)) {
            this.func_231175_as__();
            if (Minecraft.getInstance().field_71462_r == null) {
                Minecraft.getInstance().field_71417_B.func_198034_i();
            }
            return true;
        }
        return false;
    }
    
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return false;
    }
}
