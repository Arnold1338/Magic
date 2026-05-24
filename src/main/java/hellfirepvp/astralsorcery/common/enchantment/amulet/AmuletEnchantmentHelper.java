package hellfirepvp.astralsorcery.common.enchantment.amulet;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotResult;
import java.util.Optional;
import hellfirepvp.astralsorcery.common.integration.IntegrationCurios;
import hellfirepvp.astralsorcery.common.item.ItemEnchantmentAmulet;
import net.minecraft.util.Tuple;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.item.ItemComparator;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.Util;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentHelper;
import java.util.UUID;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;

public class AmuletEnchantmentHelper
{
    public static final String KEY_AS_OWNER = "AS_Amulet_Holder";
    
    public static void removeAmuletTagsAndCleanup(final Player player, final boolean keepEquipped) {
        final Inventory inv = player.getInventory();
        for (int i = 0; i < inv.field_70462_a.size(); ++i) {
            if (i != inv.field_70461_c || !keepEquipped) {
                removeAmuletOwner((ItemStack)inv.field_70462_a.get(i));
            }
        }
        removeAmuletOwner(inv.func_70445_o());
        if (!keepEquipped) {
            for (int i = 0; i < inv.field_70460_b.size(); ++i) {
                removeAmuletOwner((ItemStack)inv.field_70460_b.get(i));
            }
            for (int i = 0; i < inv.field_184439_c.size(); ++i) {
                removeAmuletOwner((ItemStack)inv.field_184439_c.get(i));
            }
        }
    }
    
    @Nonnull
    private static UUID getWornPlayerUUID(final ItemStack anyTool) {
        if (DynamicEnchantmentHelper.canHaveDynamicEnchantment(anyTool) && anyTool.hasTag()) {
            return NBTHelper.getUUID(anyTool.getTag(), "AS_Amulet_Holder", Util.NIL_UUID);
        }
        return Util.NIL_UUID;
    }
    
    public static void applyAmuletOwner(final ItemStack tool, final Player wearer) {
        if (DynamicEnchantmentHelper.canHaveDynamicEnchantment(tool)) {
            tool.getOrCreateTag().putUUID("AS_Amulet_Holder", wearer.getUUID());
        }
    }
    
    private static void removeAmuletOwner(final ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTag()) {
            return;
        }
        NBTHelper.removeUUID(stack.getTag(), "AS_Amulet_Holder");
        if (stack.getTag().isEmpty()) {
            stack.setTag((CompoundTag)null);
        }
    }
    
    @Nullable
    public static Player getPlayerHavingTool(final ItemStack anyTool) {
        final UUID plUUID = getWornPlayerUUID(anyTool);
        if (plUUID.getLeastSignificantBits() == 0L && plUUID.getMostSignificantBits() == 0L) {
            return null;
        }
        Player player;
        if (EffectiveSide.get() == LogicalSide.CLIENT) {
            player = resolvePlayerClient(plUUID);
        }
        else {
            final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return null;
            }
            player = (Player)server.getPlayerList().getPlayer(plUUID);
        }
        if (player == null) {
            return null;
        }
        final int originalDamage = anyTool.getDamageValue();
        boolean foundTool = false;
        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            final ItemStack stack = player.getItemBySlot(slot);
            anyTool.setDamageValue(stack.getDamageValue());
            if (ItemComparator.compare(stack, anyTool, ItemComparator.Clause.Sets.ITEMSTACK_STRICT)) {
                foundTool = true;
                break;
            }
        }
        anyTool.setDamageValue(originalDamage);
        if (!foundTool) {
            return null;
        }
        return player;
    }
    
    @Nullable
    static Tuple<ItemStack, Player> getWornAmulet(final ItemStack anyTool) {
        final Player player = getPlayerHavingTool(anyTool);
        if (player == null) {
            return null;
        }
        final Optional<SlotResult> curios = IntegrationCurios.getCurio(player, stack -> stack.getItem() instanceof ItemEnchantmentAmulet);
        return (Tuple<ItemStack, Player>)curios.map(trpl -> new Tuple(trpl.stack(), (Object)player)).orElse(null);
    }
    
    @OnlyIn(Dist.CLIENT)
    private static Player resolvePlayerClient(final UUID plUUID) {
        final Optional<World> w = (Optional<World>)Optional.ofNullable(Minecraft.getInstance().level);
        return w.map(world -> world.getPlayerByUUID(plUUID)).orElse(null);
    }
}
