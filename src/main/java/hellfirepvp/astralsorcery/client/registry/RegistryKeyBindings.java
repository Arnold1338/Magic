package hellfirepvp.astralsorcery.client.registry;

import java.util.HashSet;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import java.util.function.Consumer;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.client.lib.KeyBindingsAS;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.input.KeyDisablePerkAbilities;
import hellfirepvp.astralsorcery.client.input.KeyBindingWrapper;
import java.util.Set;

public class RegistryKeyBindings
{
    private static final Set<KeyBindingWrapper> watchedKeyBindings;
    private static final Set<KeyBindingWrapper> bindingsPressed;
    
    public static void init() {
        KeyBindingsAS.DISABLE_PERK_ABILITIES = register("disable_perk_abilities", 86, (Function<KeyBinding, KeyBindingWrapper>)KeyDisablePerkAbilities::new);
        MinecraftForge.EVENT_BUS.addListener((Consumer)RegistryKeyBindings::onKeyInput);
    }
    
    private static KeyBindingWrapper register(final String name, final int glfwKey) {
        return register(name, glfwKey, keyBinding -> new KeyBindingWrapper(keyBinding) {});
    }
    
    private static KeyBindingWrapper register(final String name, final int glfwKey, final Function<KeyBinding, KeyBindingWrapper> wrapperCreator) {
        final KeyBinding keyBinding = new KeyBinding(String.format("key.%s.%s", "astralsorcery", name), (IKeyConflictContext)KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, glfwKey, "Astral Sorcery");
        ClientRegistry.registerKeyBinding(keyBinding);
        final KeyBindingWrapper wrapper = wrapperCreator.apply(keyBinding);
        RegistryKeyBindings.watchedKeyBindings.add(wrapper);
        return wrapper;
    }
    
    private static void onKeyInput(final InputEvent.KeyInputEvent event) {
        final InputMappings.Input input = InputMappings.func_197954_a(event.getKey(), event.getScanCode());
        final KeyBindingWrapper eventKey = MiscUtils.iterativeSearch(RegistryKeyBindings.watchedKeyBindings, keyBinding -> keyBinding.getKeyBinding().getKey().equals((Object)input));
        if (eventKey != null) {
            final boolean isPressed = eventKey.getKeyBinding().func_151470_d();
            final boolean wasPressed = RegistryKeyBindings.bindingsPressed.contains(eventKey);
            if (isPressed != wasPressed) {
                if (isPressed) {
                    RegistryKeyBindings.bindingsPressed.add(eventKey);
                    eventKey.onKeyDown();
                }
                else {
                    RegistryKeyBindings.bindingsPressed.remove(eventKey);
                    eventKey.onKeyUp();
                }
            }
        }
    }
    
    static {
        watchedKeyBindings = new HashSet<KeyBindingWrapper>();
        bindingsPressed = new HashSet<KeyBindingWrapper>();
    }
}
