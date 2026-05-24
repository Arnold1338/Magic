package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectOctans;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "getWaterSlowDown", at = @At("HEAD"), cancellable = true)
    public void preventWaterSlowdown(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (!entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                && MantleEffectOctans.shouldPreventWaterSlowdown(entity.getItemBySlot(EquipmentSlot.CHEST), entity)) {
            cir.setReturnValue(0.92f);
        }
    }
}
