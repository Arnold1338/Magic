package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.event.CooldownSetEvent;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.Cooldowns.class)
public class MixinCooldownTracker {
    @ModifyVariable(method = "addCooldown", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public int fireCooldownEvent(int cooldownTicks) {
        Player.Cooldowns tracker = (Player.Cooldowns)(Object)this;
        // Access the player from the Cooldowns inner class
        Player player = ((net.minecraft.world.entity.player.Player.Cooldowns)(Object)this).player;
        if (player != null && !player.level().isClientSide()) {
            CooldownSetEvent event = new CooldownSetEvent(player, cooldownTicks);
            MinecraftForge.EVENT_BUS.post(event);
            cooldownTicks = Math.max(event.getResultCooldown(), 1);
        }
        return cooldownTicks;
    }
}
