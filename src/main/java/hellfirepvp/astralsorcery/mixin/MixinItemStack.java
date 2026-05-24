package hellfirepvp.astralsorcery.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Inject(method = "isEnchanted", at = @At("HEAD"), cancellable = true)
    public void addPrismEnchantmentGlint(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (!EnchantmentHelper.getEnchantments(stack).isEmpty()) {
            cir.setReturnValue(true);
        }
    }
}
