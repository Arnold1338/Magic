package hellfirepvp.astralsorcery.common.perk.source.provider.equipment;

import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.entity.EquipmentSlot;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSourceProvider;

public class EquipmentSourceProvider extends ModifierSourceProvider<EquipmentModifierSource>
{
    static final String KEY_MOD_IDENTIFIER = "modifier_identifier";
    
    public EquipmentSourceProvider() {
        super(ModifierManager.EQUIPMENT_PROVIDER_KEY);
    }
    
    @Override
    protected void update(final ServerPlayer playerEntity) {
        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot != EquipmentSlot.OFFHAND) {
                final ResourceLocation id = AstralSorcery.key("slot_" + slot.func_188450_d());
                final ItemStack stack = playerEntity.getItemBySlot(slot);
                final EquipmentModifierSource slotSource = new EquipmentModifierSource(slot, stack.copy());
                if (!stack.isEmpty()) {
                    final Collection<PerkAttributeModifier> modifiers = slotSource.getModifiers((Player)playerEntity, LogicalSide.SERVER, false);
                    if (!modifiers.isEmpty()) {
                        final CompoundTag nbt = NBTHelper.getPersistentData(stack);
                        if (!nbt.hasUUID("modifier_identifier")) {
                            nbt.putUUID("modifier_identifier", UUID.randomUUID());
                        }
                        this.updateSource(playerEntity, id, slotSource);
                    }
                    else {
                        this.updateSource(playerEntity, id, null);
                    }
                }
                else {
                    this.updateSource(playerEntity, id, null);
                }
            }
        }
    }
    
    @Override
    protected void removeModifiers(final ServerPlayer playerEntity) {
        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot != EquipmentSlot.OFFHAND) {
                final ResourceLocation id = AstralSorcery.key("slot_" + slot.func_188450_d());
                this.updateSource(playerEntity, id, null);
            }
        }
    }
    
    @Override
    public void serialize(final EquipmentModifierSource source, final FriendlyByteBuf buf) {
        ByteBufUtils.writeEnumValue(buf, source.slot);
        ByteBufUtils.writeItemStack(buf, source.itemStack);
    }
    
    @Override
    public EquipmentModifierSource deserialize(final FriendlyByteBuf buf) {
        final EquipmentSlot type = ByteBufUtils.readEnumValue(buf, EquipmentSlot.class);
        final ItemStack stack = ByteBufUtils.readItemStack(buf);
        return new EquipmentModifierSource(type, stack);
    }
}
