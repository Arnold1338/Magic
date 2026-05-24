package hellfirepvp.astralsorcery;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {
    public void connect() {
        Mixins.addConfiguration(String.format("assets/%s/%s.mixins.json", "astralsorcery", "astralsorcery"));
    }
}
