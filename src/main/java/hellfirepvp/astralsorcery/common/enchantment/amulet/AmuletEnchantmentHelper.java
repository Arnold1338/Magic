package hellfirepvp.astralsorcery.common.enchantment.amulet;

import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentHelper;
import hellfirepvp.astralsorcery.common.integration.IntegrationCurios;
import hellfirepvp.astralsorcery.common.item.ItemEnchantmentAmulet;
import hellfirepvp.astralsorcery.common.util.item.ItemComparator;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.entity.EquipmentSlot;
import net.minecraft.world.level.entity.player.Inventory;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class AmuletEnchantmentHelper {
    public static final String KEY_AS_OWNER = "AS_Amulet_Holder";

    public static void removeAmuletTagsAndCleanup(Player player, boolean keepEquipped) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.items.size(); i++) {
            if (i != inv.selected || !keepEquipped) {
                removeAmuletOwner(inv.items.get(i));
            }
        }
        removeAmuletOwner(inv.getSelected());
        if (!keepEquipped) {
            for (ItemStack s : inv.armor) removeAmuletOwner(s);
            for (ItemStack s : inv.offhand) removeAmuletOwner(s);
        }
    }

    @Nonnull
    private static UUID getWornPlayerUUID(ItemStack anyTool) {
        if (DynamicEnchantmentHelper.canHaveDynamicEnchantment(anyTool) && anyTool.hasTag()) {
            return NBTHelper.getUUID(anyTool.getTag(), "AS_Amulet_Holder", Util.NIL_UUID);
        }
        return Util.NIL_UUID;
    }

    public static void applyAmuletOwner(ItemStack tool, Player wearer) {
        if (DynamicEnchantmentHelper.canHaveDynamicEnchantment(tool)) {
            tool.getOrCreateTag().putUUID("AS_Amulet_Holder", wearer.getUUID());
        }
    }

    private static void removeAmuletOwner(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTag()) return;
        NBTHelper.removeUUID(stack.getTag(), "AS_Amulet_Holder");
        if (stack.getTag().isEmpty()) stack.setTag(null);
    }

    @Nullable
    public static Player getPlayerHavingTool(ItemStack anyTool) {
        UUID plUUID = getWornPlayerUUID(anyTool);
        if (plUUID.getLeastSignificantBits() == 0L && plUUID.getMostSignificantBits() == 0L) return null;
        Player player;
        if (EffectiveSide.get() == LogicalSide.CLIENT) {
            player = resolvePlayerClient(plUUID);
        } else {
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return null;
            player = server.getPlayerList().getPlayer(plUUID);
        }
        if (player == null) return null;
        int originalDamage = anyTool.getDamageValue();
        boolean foundTool = false;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            anyTool.setDamageValue(stack.getDamageValue());
            if (ItemComparator.compare(stack, anyTool, ItemComparator.Clause.Sets.ITEMSTACK_STRICT)) {
                foundTool = true;
                break;
            }
        }
        anyTool.setDamageValue(originalDamage);
        if (!foundTool) return null;
        return player;
    }

    @Nullable
    static Tuple<ItemStack, Player> getWornAmulet(ItemStack anyTool) {
        Player player = getPlayerHavingTool(anyTool);
        if (player == null) return null;
        // 1.20.1 Curios: returns Optional<SlotResult> instead of Optional<ImmutableTriple>
        Optional<SlotResult> curios = IntegrationCurios.getCurio(player,
            stack -> stack.getItem() instanceof ItemEnchantmentAmulet);
        return curios.map(slotResult -> new Tuple<>(slotResult.stack(), player)).orElse(null);
    }

    @OnlyIn(Dist.CLIENT)
    private static Player resolvePlayerClient(UUID plUUID) {
        if (Minecraft.getInstance().level == null) return null;
        return Minecraft.getInstance().level.getPlayerByUUID(plUUID);
    }
}
