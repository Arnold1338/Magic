package hellfirepvp.astralsorcery.client.input;

import com.mojang.blaze3d.platform.InputConstants;

public abstract class KeyBindingWrapper
{
    private final KeyBinding keyBinding;
    
    protected KeyBindingWrapper(final KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }
    
    public KeyBinding getKeyBinding() {
        return this.keyBinding;
    }
    
    public void onKeyDown() {
    }
    
    public void onKeyUp() {
    }
}
