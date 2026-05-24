package hellfirepvp.astralsorcery.common.event;

import java.util.LinkedList;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantment;
import java.util.List;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class DynamicEnchantmentEvent
{
    @Cancelable
    public static class Add extends Event
    {
        private final List<DynamicEnchantment> enchantmentsToApply;
        private final ItemStack itemStack;
        private final Player resolvedPlayer;
        
        public Add(final ItemStack itemStack, @Nonnull final Player player) {
            this.enchantmentsToApply = new LinkedList<DynamicEnchantment>();
            this.itemStack = itemStack;
            this.resolvedPlayer = player;
        }
        
        public ItemStack getEnchantedItemStack() {
            return this.itemStack;
        }
        
        @Nonnull
        public Player getResolvedPlayer() {
            return this.resolvedPlayer;
        }
        
        public List<DynamicEnchantment> getEnchantmentsToApply() {
            return this.enchantmentsToApply;
        }
    }
    
    @Cancelable
    public static class Modify extends Event
    {
        private final List<DynamicEnchantment> enchantmentsToApply;
        private final ItemStack itemStack;
        private final Player resolvedPlayer;
        
        public Modify(final ItemStack itemStack, final List<DynamicEnchantment> enchantmentsToApply, @Nonnull final Player resolvedPlayer) {
            this.itemStack = itemStack;
            this.enchantmentsToApply = enchantmentsToApply;
            this.resolvedPlayer = resolvedPlayer;
        }
        
        @Nonnull
        public Player getResolvedPlayer() {
            return this.resolvedPlayer;
        }
        
        public ItemStack getEnchantedItemStack() {
            return this.itemStack;
        }
        
        public List<DynamicEnchantment> getEnchantmentsToApply() {
            return this.enchantmentsToApply;
        }
    }
}
