package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.item.enchantment.Enchantment;
import net.minecraft.world.level.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
    @Inject(method = "getItemEnchantmentLevel", at = @At("RETURN"), cancellable = true)
    private static void getEnhancedEnchantmentLevel(Enchantment enchID, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(DynamicEnchantmentHelper.getNewEnchantmentLevel(cir.getReturnValue(), enchID, stack, null));
    }

    @Redirect(method = "runIterationOnItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentTags()Lnet/minecraft/nbt/ListTag;"))
    private static ListTag applyEnhancedEnchantmentsTag(ItemStack stack) {
        return DynamicEnchantmentHelper.modifyEnchantmentTags(stack.getEnchantmentTags(), stack);
    }

    @Inject(method = "getEnchantments", at = @At("RETURN"))
    private static void applyDeserializedEnhancedEnchantments(ItemStack stack, CallbackInfoReturnable<Map<Enchantment, Integer>> cir) {
        DynamicEnchantmentHelper.addNewLevels(cir.getReturnValue(), stack);
    }
}
