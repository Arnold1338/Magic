package hellfirepvp.astralsorcery.common.perk.source.provider.equipment;

import java.util.Objects;
import net.minecraft.Util;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.DynamicModifierHelper;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import hellfirepvp.astralsorcery.common.perk.source.AttributeModifierProvider;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;

public class EquipmentModifierSource implements ModifierSource, AttributeModifierProvider
{
    final EquipmentSlot slot;
    final ItemStack itemStack;
    
    EquipmentModifierSource(final EquipmentSlot slot, final ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }
    
    @Override
    public boolean canApplySource(final Player player, final LogicalSide dist) {
        return true;
    }
    
    @Override
    public void onRemove(final Player player, final LogicalSide dist) {
    }
    
    @Override
    public void onApply(final Player player, final LogicalSide dist) {
    }
    
    @Override
    public Collection<PerkAttributeModifier> getModifiers(final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        if (this.itemStack.isEmpty()) {
            return (Collection<PerkAttributeModifier>)Collections.emptyList();
        }
        return DynamicModifierHelper.getDynamicModifiers(this.itemStack, player, side, ignoreRequirements);
    }
    
    @Override
    public boolean isEqual(final ModifierSource other) {
        return this.equals(other);
    }
    
    @Override
    public ResourceLocation getProviderName() {
        return ModifierManager.EQUIPMENT_PROVIDER_KEY;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final EquipmentModifierSource that = (EquipmentModifierSource)o;
        return this.slot == that.slot && NBTHelper.getUUID(NBTHelper.getPersistentData(this.itemStack), "modifier_identifier", Util.NIL_UUID).equals(NBTHelper.getUUID(NBTHelper.getPersistentData(that.itemStack), "modifier_identifier", Util.NIL_UUID));
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.slot, NBTHelper.getUUID(NBTHelper.getPersistentData(this.itemStack), "modifier_identifier", Util.NIL_UUID));
    }
}
