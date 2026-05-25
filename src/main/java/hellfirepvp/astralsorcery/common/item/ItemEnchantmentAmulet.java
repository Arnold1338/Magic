package hellfirepvp.astralsorcery.common.item;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import java.util.function.Function;
import java.util.Comparator;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantment;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import java.util.Optional;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.common.enchantment.amulet.AmuletRandomizeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import hellfirepvp.astralsorcery.common.enchantment.amulet.AmuletEnchantment;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import java.util.Random;
import hellfirepvp.astralsorcery.common.item.base.client.ItemDynamicColor;
import net.minecraft.world.item.Item;

public class ItemEnchantmentAmulet extends Item implements ItemDynamicColor
{
    private static final Random rand;
    
    public ItemEnchantmentAmulet() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        super.func_77624_a(stack, worldIn, (List)tooltip, flagIn);
        final List<AmuletEnchantment> enchantments = getAmuletEnchantments(stack);
        for (final AmuletEnchantment ench : enchantments) {
            tooltip.add((Component)ench.getDisplay().toString()ChatFormatting.BLUE));
        }
        if (getAmuletColor(stack).map(color -> color == -1).orElse(false)) {
            tooltip.add((Component)new Component("astralsorcery.amulet.color.colorless").toString()ChatFormatting.ITALIC).toString()ChatFormatting.GRAY));
        }
    }
    
    public void func_77663_a(final ItemStack stack, final Level worldIn, final Entity entityIn, final int itemSlot, final boolean isSelected) {
        if (!worldIn.level() && !getAmuletColor(stack).isPresent()) {
            freezeAmuletColor(stack);
        }
        if (!worldIn.level() && getAmuletEnchantments(stack).isEmpty()) {
            AmuletRandomizeHelper.rollAmulet(stack);
        }
        super.func_77663_a(stack, worldIn, entityIn, itemSlot, isSelected);
    }
    
    public int getColor(final ItemStack stack, final int tintIndex) {
        if (tintIndex != 1) {
            return -1;
        }
        final Optional<Integer> color = getAmuletColor(stack);
        if (color.isPresent()) {
            return color.get();
        }
        final int tick = (int)(ClientScheduler.getClientTick() % 500000L);
        final int c = Color.getHSBColor(tick / 500000.0f, 0.7f, 1.0f).getRGB();
        return c | 0xFF000000;
    }
    
    public static Optional<Integer> getAmuletColor(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemEnchantmentAmulet)) {
            return Optional.empty();
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (!tag.contains("amuletColor")) {
            return Optional.empty();
        }
        return Optional.of(tag.getInt("amuletColor"));
    }
    
    public static void freezeAmuletColor(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemEnchantmentAmulet)) {
            return;
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (tag.contains("amuletColor")) {
            return;
        }
        if (ItemEnchantmentAmulet.rand.nextInt(400) == 0) {
            tag.putInt("amuletColor", -1);
        }
        else {
            tag.putInt("amuletColor", Color.getHSBColor(ItemEnchantmentAmulet.rand.nextFloat(), 0.7f, 1.0f).getRGB() | 0xFF000000);
        }
    }
    
    public static List<AmuletEnchantment> getAmuletEnchantments(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemEnchantmentAmulet)) {
            return Lists.newArrayList();
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (!tag.contains("amuletEnchantments")) {
            return Lists.newArrayList();
        }
        final ListTag enchants = tag.getList("amuletEnchantments", 10);
        final List<AmuletEnchantment> enchantments = new ArrayList<AmuletEnchantment>(enchants.size());
        for (int i = 0; i < enchants.size(); ++i) {
            final AmuletEnchantment ench = AmuletEnchantment.deserialize(enchants.getCompound(i));
            if (ench != null) {
                enchantments.add(ench);
            }
        }
        enchantments.sort(Comparator.comparing((Function<? super AmuletEnchantment, ? extends Comparable>)DynamicEnchantment::getType));
        return enchantments;
    }
    
    public static void setAmuletEnchantments(final ItemStack stack, final List<AmuletEnchantment> enchantments) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemEnchantmentAmulet)) {
            return;
        }
        enchantments.sort(Comparator.comparing((Function<? super AmuletEnchantment, ? extends Comparable>)DynamicEnchantment::getType));
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        final ListTag enchants = new ListTag();
        for (final AmuletEnchantment enchant : enchantments) {
            enchants.add((Object)enchant.serialize());
        }
        tag.put("amuletEnchantments", (Tag)enchants);
    }
    
    static {
        rand = new Random();
    }
}
