package hellfirepvp.astralsorcery.client.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.fonts.TextInputUtil;

public class ScreenTextEntry
{
    private String text;
    private Runnable changeCallback;
    private final TextInputUtil inputUtil;
    
    public ScreenTextEntry() {
        this.text = "";
        this.changeCallback = null;
        this.inputUtil = new TextInputUtil((Supplier)this::getText, (Consumer)this::setText, TextInputUtil.func_238570_a_(Minecraft.func_71410_x()), TextInputUtil.func_238582_c_(Minecraft.func_71410_x()), text -> text.length() < 256);
    }
    
    public void setChangeCallback(final Runnable changeCallback) {
        this.changeCallback = changeCallback;
    }
    
    public void setText(@Nullable String newText) {
        if (newText == null) {
            newText = "";
        }
        final String prevText = this.text;
        this.text = newText;
        if (!newText.equals(prevText) && this.changeCallback != null) {
            this.changeCallback.run();
        }
    }
    
    @Nonnull
    public String getText() {
        return this.text;
    }
    
    public boolean keyTyped(final int key) {
        return key != 256 && key != 257 && key != 335 && key != 268 && key != 269 && key != 260 && key != 261 && (key < 262 || key > 265) && this.inputUtil.func_216897_a(key);
    }
    
    public boolean charTyped(final char charCode) {
        return this.inputUtil.func_216894_a(charCode);
    }
}
