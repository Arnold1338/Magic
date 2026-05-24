package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import net.minecraft.world.level.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttributeInstance.class)
public class MixinModifiableAttributeInstance {
    @Inject(method = "calculateValue", at = @At("RETURN"), cancellable = true)
    public void postProcessAttributeValue(CallbackInfoReturnable<Double> cir) {
        AttributeInstance attributeInstance = (AttributeInstance)(Object)this;
        cir.setReturnValue(AttributeEvent.postProcessVanilla(cir.getReturnValue(), attributeInstance));
    }
}
