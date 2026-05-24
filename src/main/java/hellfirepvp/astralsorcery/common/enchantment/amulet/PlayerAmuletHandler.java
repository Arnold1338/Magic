package hellfirepvp.astralsorcery.common.enchantment.amulet;

import java.util.EnumSet;
import net.minecraft.world.level.entity.EquipmentSlot;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantment;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.item.ItemEnchantmentAmulet;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentHelper;
import hellfirepvp.astralsorcery.common.event.DynamicEnchantmentEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class PlayerAmuletHandler implements ITickHandler
{
    public static final PlayerAmuletHandler INSTANCE;
    
    private PlayerAmuletHandler() {
    }
    
    public static void onEnchantmentAdd(final DynamicEnchantmentEvent.Add event) {
        if (!DynamicEnchantmentHelper.canHaveDynamicEnchantment(event.getEnchantedItemStack())) {
            return;
        }
        final Tuple<ItemStack, Player> linkedAmulet = AmuletEnchantmentHelper.getWornAmulet(event.getEnchantedItemStack());
        if (linkedAmulet == null || ((ItemStack)linkedAmulet.func_76341_a()).isEmpty() || linkedAmulet.func_76340_b() == null) {
            return;
        }
        event.getEnchantmentsToApply().addAll(ItemEnchantmentAmulet.getAmuletEnchantments((ItemStack)linkedAmulet.func_76341_a()));
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)context[0];
        this.applyAmuletTags(player);
        this.clearAmuletTags(player);
    }
    
    private void applyAmuletTags(final Player player) {
        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            AmuletEnchantmentHelper.applyAmuletOwner(player.getItemBySlot(slot), player);
        }
    }
    
    private void clearAmuletTags(final Player player) {
        AmuletEnchantmentHelper.removeAmuletTagsAndCleanup(player, true);
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "PlayerAmuletHandler";
    }
    
    static {
        INSTANCE = new PlayerAmuletHandler();
    }
}
