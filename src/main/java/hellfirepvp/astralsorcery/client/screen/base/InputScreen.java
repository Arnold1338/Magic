package hellfirepvp.astralsorcery.client.screen.base;

import java.util.function.Consumer;
import java.util.HashSet;
import net.minecraft.network.chat.Component;
import java.util.Set;
import net.minecraft.client.gui.screens.Screen;

public class InputScreen extends Screen
{
    private final Set<Integer> heldKeys;
    private double oMouseX;
    private double oMouseY;
    private boolean dragging;
    
    protected InputScreen(final Component name) {
        super(name);
        this.heldKeys = new HashSet<Integer>();
        this.dragging = false;
    }
    
    public void func_231023_e_() {
        this.heldKeys.forEach(this::keyPressedTick);
        super.func_231023_e_();
    }
    
    protected void keyPressedTick(final int key) {
    }
    
    protected void mouseDragStart(final double mouseX, final double mouseY) {
    }
    
    protected void mouseDragStop(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY) {
    }
    
    protected void mouseDragTick(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY, final double mouseOffsetX, final double mouseOffsetY) {
    }
    
    public boolean func_231046_a_(final int key, final int scanCode, final int modifiers) {
        this.heldKeys.add(key);
        return super.func_231046_a_(key, scanCode, modifiers);
    }
    
    public boolean func_223281_a_(final int key, final int scanCode, final int modifiers) {
        this.heldKeys.remove(key);
        return super.func_223281_a_(key, scanCode, modifiers);
    }
    
    public boolean isCurrentlyDragging() {
        return this.dragging;
    }
    
    protected void stopDragging(final double mouseX, final double mouseY) {
        if (this.dragging) {
            this.dragging = false;
            this.mouseDragStop(mouseX, mouseY, this.oMouseX, this.oMouseY);
        }
    }
    
    public boolean func_231044_a_(final double mouseX, final double mouseY, final int click) {
        if (click == 0) {
            this.dragging = true;
            this.mouseDragStart(this.oMouseX = mouseX, this.oMouseY = mouseY);
        }
        return super.func_231044_a_(mouseX, mouseY, click);
    }
    
    public boolean func_231048_c_(final double mouseX, final double mouseY, final int click) {
        if (click == 0) {
            this.stopDragging(mouseX, mouseY);
        }
        return super.func_231048_c_(mouseX, mouseY, click);
    }
    
    public boolean func_231045_a_(final double mouseX, final double mouseY, final int clickType, final double offsetX, final double offsetY) {
        if (clickType == 0 && this.dragging) {
            final double diffX = this.oMouseX - mouseX;
            final double diffY = this.oMouseY - mouseY;
            this.mouseDragTick(mouseX, mouseY, diffX, diffY, offsetX, offsetY);
        }
        return super.func_231045_a_(mouseX, mouseY, clickType, offsetX, offsetY);
    }
}
