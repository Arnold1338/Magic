package hellfirepvp.astralsorcery.mixin.client;

import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentHelper;
import hellfirepvp.astralsorcery.common.perk.DynamicModifierHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Inject(method = "getTooltipLines",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasTag()Z", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void addMissingEnchantmentTooltip(Player player, TooltipFlag advanced,
                                              CallbackInfoReturnable<List<Component>> cir,
                                              List<Component> tooltip) {
        ItemStack stack = (ItemStack)(Object)this;
        List<Component> addition = new ArrayList<>();
        try {
            DynamicModifierHelper.addModifierTooltip(stack, addition);
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            if (!stack.hasTag() && !enchantments.isEmpty()) {
                for (Enchantment e : enchantments.keySet()) {
                    addition.add(e.getFullname(enchantments.get(e)).copy().withStyle(ChatFormatting.GRAY));
                }
            }
        } catch (Exception exc) {
            addition.clear();
            tooltip.add(Component.translatable("astralsorcery.misc.tooltipError").withStyle(ChatFormatting.GRAY));
        }
        tooltip.addAll(addition);
    }

    @Redirect(method = "getTooltipLines",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentTags()Lnet/minecraft/nbt/ListTag;"))
    public ListTag enhanceEnchantmentTooltip(ItemStack stack) {
        return DynamicEnchantmentHelper.modifyEnchantmentTags(stack.getEnchantmentTags(), stack);
    }
}
