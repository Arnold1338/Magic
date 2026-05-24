package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentHelper;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.Map;

@Mixin(ItemPredicate.class)
public class MixinItemPredicate {
    @Inject(method = "matches",
            at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;deserializeEnchantments(Lnet/minecraft/nbt/ListTag;)Ljava/util/Map;",
                ordinal = 0, shift = At.Shift.BY, by = 2),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void enhanceEnchantmentList(ItemStack item, CallbackInfoReturnable<Boolean> cir,
                                        Map<Enchantment, Integer> enchantments) {
        DynamicEnchantmentHelper.addNewLevels(enchantments, item);
    }
}
