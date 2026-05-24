package hellfirepvp.astralsorcery.common.constellation.engraving;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import net.minecraft.world.level.effect.MobEffectInstance;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.effect.PotionUtils;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.effect.MobEffect;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.item.Items;
import java.util.Map;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.UUID;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.item.base.TypeEnchantableItem;
import hellfirepvp.astralsorcery.common.perk.DynamicModifierHelper;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.List;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class EngravingEffect extends ForgeRegistryEntry<EngravingEffect>
{
    private final List<ApplicableEffect> effects;
    
    public EngravingEffect(final IConstellation cst) {
        this.effects = new ArrayList<ApplicableEffect>();
        this.setRegistryName(cst.getRegistryName());
    }
    
    public EngravingEffect addEffect(final ApplicableEffect potion) {
        this.effects.add(potion);
        return this;
    }
    
    public List<ApplicableEffect> getApplicableEffects(@Nonnull final ItemStack stack) {
        return this.effects.stream().filter(effects -> effects.supports(stack)).collect((Collector<? super Object, ?, List<ApplicableEffect>>)Collectors.toList());
    }
    
    public static class ModifierEffect implements ApplicableEffect
    {
        private final Supplier<PerkAttributeType> modifier;
        private final ModifierType type;
        private final float min;
        private final float max;
        private final List<EnchantmentCategory> applicableTypes;
        private boolean formatToInteger;
        
        public ModifierEffect(final Supplier<PerkAttributeType> modifier, final ModifierType type, final float min, final float max) {
            this.applicableTypes = new ArrayList<EnchantmentCategory>();
            this.formatToInteger = false;
            this.modifier = modifier;
            this.type = type;
            this.min = min;
            this.max = max;
        }
        
        public ModifierEffect addApplicableType(final EnchantmentCategory type) {
            this.applicableTypes.add(type);
            return this;
        }
        
        public ModifierEffect formatResultAsInteger() {
            this.formatToInteger = true;
            return this;
        }
        
        @Override
        public boolean supports(@Nonnull final ItemStack stack) {
            if (stack.isEmpty()) {
                return false;
            }
            if (!DynamicModifierHelper.getStaticModifiers(stack).isEmpty()) {
                return false;
            }
            final Item item = stack.getItem();
            if (this.applicableTypes.isEmpty()) {
                for (final EnchantmentCategory type : EnchantmentCategory.values()) {
                    if (type.func_77557_a(item)) {
                        return true;
                    }
                    if (item instanceof TypeEnchantableItem && ((TypeEnchantableItem)item).canEnchantItem(stack, type)) {
                        return true;
                    }
                }
            }
            for (final EnchantmentCategory type2 : this.applicableTypes) {
                if (type2.func_77557_a(item)) {
                    return true;
                }
                if (item instanceof TypeEnchantableItem && ((TypeEnchantableItem)item).canEnchantItem(stack, type2)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public ItemStack apply(@Nonnull final ItemStack stack, final float percent, final Random rand) {
            float rValue = percent * Math.max(0.0f, this.max - this.min);
            if (this.formatToInteger) {
                rValue = (float)Math.round(rValue);
            }
            DynamicModifierHelper.addModifier(stack, UUID.randomUUID(), this.modifier.get(), this.type, this.min + rValue);
            return stack;
        }
    }
    
    public static class EnchantmentEffect implements ApplicableEffect
    {
        private final Supplier<Enchantment> enchantment;
        private final int min;
        private final int max;
        private boolean ignoreCompat;
        
        public EnchantmentEffect(final Supplier<Enchantment> enchantment, final int min, final int max) {
            this.ignoreCompat = false;
            this.enchantment = enchantment;
            this.min = min;
            this.max = max;
        }
        
        public EnchantmentEffect setIgnoreCompatibility() {
            this.ignoreCompat = true;
            return this;
        }
        
        public boolean isIgnoreCompatibility() {
            return this.ignoreCompat;
        }
        
        @Override
        public boolean supports(@Nonnull final ItemStack stack) {
            if (stack.isEmpty()) {
                return false;
            }
            if (stack.getItem() instanceof BookItem) {
                return this.enchantment.get().isAllowedOnBooks();
            }
            if (!(stack.getItem() instanceof EnchantedBookItem) && !this.enchantment.get().func_92089_a(stack)) {
                return false;
            }
            final Map<Enchantment, Integer> enchantments = EnchantmentHelper.func_82781_a(stack);
            final Enchantment toApply = this.enchantment.get();
            for (final Enchantment applied : enchantments.keySet()) {
                if (toApply.equals(applied)) {
                    return false;
                }
                if (this.ignoreCompat) {
                    continue;
                }
                if (toApply.func_191560_c(applied) && !(stack.getItem() instanceof EnchantedBookItem)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public ItemStack apply(@Nonnull ItemStack stack, final float percent, final Random rand) {
            final int level = this.min + Math.round(percent * Math.max(0, this.max - this.min));
            if (stack.getItem() instanceof BookItem) {
                stack = ItemUtils.changeItem(stack, Items.field_151134_bR);
            }
            final Map<Enchantment, Integer> enchantments = EnchantmentHelper.func_82781_a(stack);
            final Enchantment newEnch = this.enchantment.get();
            if (!this.ignoreCompat) {
                boolean hasIncompat = false;
                for (final Enchantment e : enchantments.keySet()) {
                    if (e.equals(newEnch) || !e.func_191560_c(newEnch)) {
                        hasIncompat = true;
                        break;
                    }
                }
                if (hasIncompat) {
                    return stack;
                }
            }
            enchantments.put(newEnch, level);
            EnchantmentHelper.func_82782_a((Map)enchantments, stack);
            return stack;
        }
    }
    
    public static class PotionEffect implements ApplicableEffect
    {
        private final Supplier<Effect> effect;
        private final int min;
        private final int max;
        
        public PotionEffect(final Supplier<Effect> effect, final int min, final int max) {
            this.effect = effect;
            this.min = min;
            this.max = max;
        }
        
        @Override
        public boolean supports(@Nonnull final ItemStack stack) {
            return !stack.isEmpty() && stack.getItem() instanceof PotionItem && !MiscUtils.contains(PotionUtils.func_185189_a(stack), effInstance -> effInstance.func_188419_a().equals(this.effect.get()));
        }
        
        @Override
        public ItemStack apply(@Nonnull final ItemStack stack, final float percent, final Random rand) {
            final List<MobEffectInstance> existing = PotionUtils.func_185189_a(stack);
            MobEffectInstance effectInstance = null;
            if (!MiscUtils.contains(existing, effectInstance -> effectInstance.func_188419_a().equals(this.effect.get()))) {
                final int amp = this.min + Math.round(percent * Math.max(0, this.max - this.min));
                final int dur = 3600 + Math.round(rand.nextFloat() * 4.0f * 60.0f * 20.0f);
                effectInstance = new MobEffectInstance((Effect)this.effect.get(), dur, amp, true, false, true);
                existing.add(effectInstance);
            }
            if (!MiscUtils.contains(existing, effInstance -> effInstance.func_188419_a().equals(EffectsAS.EFFECT_CHEAT_DEATH)) && rand.nextInt(30) == 0) {
                existing.add(new MobEffectInstance((Effect)EffectsAS.EFFECT_CHEAT_DEATH, 3600 + Math.round(rand.nextFloat() * 4.0f * 60.0f * 20.0f), 0, true, false, true));
            }
            PotionUtils.func_185184_a(stack, (Collection)existing);
            stack.getTag().putInt("CustomPotionColor", ColorsAS.DYE_ORANGE.getRGB());
            stack.func_200302_a((Component)new Component("potion.astralsorcery.crafted.name").func_240699_a_(ChatFormatting.GOLD));
            return stack;
        }
    }
    
    public interface ApplicableEffect
    {
        boolean supports(@Nonnull final ItemStack p0);
        
        ItemStack apply(@Nonnull final ItemStack p0, final float p1, final Random p2);
    }
}
