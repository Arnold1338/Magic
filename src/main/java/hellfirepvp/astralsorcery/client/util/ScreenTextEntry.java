package hellfirepvp.astralsorcery.client.util;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// In 1.20.1, TextInputUtil was removed. This class provides the same interface
// but uses an internal string buffer with manual key handling.
public class ScreenTextEntry {
    private String text;
    private Runnable changeCallback;
    private int cursor;

    public ScreenTextEntry() {
        this.text = "";
        this.changeCallback = null;
        this.cursor = 0;
    }

    public void setChangeCallback(Runnable changeCallback) {
        this.changeCallback = changeCallback;
    }

    public void setText(@Nullable String newText) {
        if (newText == null) newText = "";
        String prevText = this.text;
        this.text = newText;
        this.cursor = Math.min(this.cursor, newText.length());
        if (!newText.equals(prevText) && this.changeCallback != null) {
            this.changeCallback.run();
        }
    }

    @Nonnull
    public String getText() { return this.text; }

    public boolean keyTyped(int key) {
        // 256=ESC, 257=ENTER, 335=numpad ENTER, 268=HOME, 269=END, 260=INSERT, 261=DELETE
        // 262-265=arrow keys
        if (key == 256 || key == 257 || key == 335 || key == 268 || key == 269 ||
                key == 260 || key == 261 || (key >= 262 && key <= 265)) {
            return false;
        }
        if (key == 259) { // BACKSPACE
            if (cursor > 0) {
                text = text.substring(0, cursor - 1) + text.substring(cursor);
                cursor--;
                if (changeCallback != null) changeCallback.run();
            }
            return true;
        }
        return false;
    }

    public boolean charTyped(char charCode) {
        if (text.length() >= 256) return false;
        text = text.substring(0, cursor) + charCode + text.substring(cursor);
        cursor++;
        if (changeCallback != null) changeCallback.run();
        return true;
    }
}
