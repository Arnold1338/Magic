package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.entity.InteractableEntity;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {
    @Shadow public ServerPlayer player;

    @Inject(method = "handleInteract",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;disconnect(Lnet/minecraft/network/chat/Component;)V"),
            cancellable = true)
    public void allowInteractableEntity(ServerboundInteractPacket packet, CallbackInfo ci) {
        Entity interacted = packet.getTarget(player.serverLevel());
        if (interacted instanceof InteractableEntity) {
            player.attack(interacted);
            ci.cancel();
        }
    }
}
