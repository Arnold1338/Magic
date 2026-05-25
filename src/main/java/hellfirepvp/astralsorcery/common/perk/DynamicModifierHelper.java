package hellfirepvp.astralsorcery.common.perk;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.perk.source.provider.equipment.EquipmentAttributeModifierProvider;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.perk.source.AttributeModifierProvider;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.List;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import java.util.UUID;
import net.minecraft.world.item.ItemStack;

public class DynamicModifierHelper
{
    public static final String KEY_MODIFIERS = "attribute_modifiers";
    
    public static void addModifier(final ItemStack stack, final UUID uuid, final PerkAttributeType type, final ModifierType mode, final float value) {
        addModifier(stack, new DynamicAttributeModifier(uuid, type, mode, value));
    }
    
    public static void addModifier(final ItemStack stack, final DynamicAttributeModifier modifier) {
        addModifiers(stack, Collections.singletonList(modifier));
    }
    
    public static void addModifiers(final ItemStack stack, final Iterable<DynamicAttributeModifier> modifiers) {
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        final ListTag modifierList = tag.getList("attribute_modifiers", 10);
        modifiers.forEach(modifier -> modifierList.add((Object)modifier.serialize()));
        tag.put("attribute_modifiers", (Tag)modifierList);
    }
    
    public static List<PerkAttributeModifier> getDynamicModifiers(final ItemStack stack, final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        final List<PerkAttributeModifier> modifiers = Lists.newArrayList();
        if (stack.getItem() instanceof AttributeModifierProvider) {
            modifiers.addAll(((AttributeModifierProvider)stack.getItem()).getModifiers(player, side, ignoreRequirements));
        }
        if (stack.getItem() instanceof EquipmentAttributeModifierProvider) {
            modifiers.addAll(((EquipmentAttributeModifierProvider)stack.getItem()).getModifiers(stack.copy(), player, side, ignoreRequirements));
        }
        modifiers.addAll(getStaticModifiers(stack));
        return modifiers;
    }
    
    public static List<DynamicAttributeModifier> getStaticModifiers(final ItemStack stack) {
        final List<DynamicAttributeModifier> modifiers = Lists.newArrayList();
        if (NBTHelper.hasPersistentData(stack)) {
            final CompoundTag tag = NBTHelper.getPersistentData(stack);
            final ListTag modifierList = tag.getList("attribute_modifiers", 10);
            for (int i = 0; i < modifierList.size(); ++i) {
                final CompoundTag modifierTag = modifierList.getCompound(i);
                modifiers.add(DynamicAttributeModifier.deserialize(modifierTag));
            }
        }
        return modifiers;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void addModifierTooltip(final ItemStack stack, final List<Component> tooltip) {
        final Player clientPlayer = (Player)Minecraft.getInstance().player;
        if (clientPlayer == null) {
            return;
        }
        for (final PerkAttributeModifier mod : getDynamicModifiers(stack, (Player)Minecraft.getInstance().player, LogicalSide.CLIENT, false)) {
            if (mod.hasDisplayString()) {
                tooltip.add((Component)new Component(mod.getLocalizedDisplayString()).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.ITALIC)));
            }
        }
    }
}
