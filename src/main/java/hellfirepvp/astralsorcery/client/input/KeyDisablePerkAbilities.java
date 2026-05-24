package hellfirepvp.astralsorcery.client.input;

import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.client.PktToggleClientOption;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;

public class KeyDisablePerkAbilities extends KeyBindingWrapper
{
    public KeyDisablePerkAbilities(final KeyBinding keyBinding) {
        super(keyBinding);
    }
    
    @Override
    public void onKeyDown() {
        if (!Minecraft.func_71410_x().func_147113_T()) {
            final PktToggleClientOption pkt = new PktToggleClientOption(PktToggleClientOption.Option.DISABLE_PERK_ABILITIES);
            PacketChannel.CHANNEL.sendToServer(pkt);
        }
    }
    
    @Override
    public void onKeyUp() {
    }
}
