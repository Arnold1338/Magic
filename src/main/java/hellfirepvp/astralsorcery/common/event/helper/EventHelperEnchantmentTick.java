package hellfirepvp.astralsorcery.common.event.helper;

import java.util.EnumSet;
import java.util.Iterator;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.item.enchantment.EnchantmentHelper;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.world.level.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.astralsorcery.common.enchantment.EnchantmentPlayerTick;
import java.util.Collection;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class EventHelperEnchantmentTick implements ITickHandler
{
    public static final EventHelperEnchantmentTick INSTANCE;
    private Collection<EnchantmentPlayerTick> tickableEnchantments;
    
    private EventHelperEnchantmentTick() {
        this.tickableEnchantments = null;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        if (this.tickableEnchantments == null) {
            this.tickableEnchantments = ForgeRegistries.ENCHANTMENTS.getValues().stream().filter(enchantment -> enchantment instanceof EnchantmentPlayerTick).map(enchantment -> enchantment).collect((Collector<? super Object, ?, Collection<EnchantmentPlayerTick>>)Collectors.toList());
        }
        for (final EnchantmentPlayerTick ench : this.tickableEnchantments) {
            final int totalLevel = EnchantmentHelper.func_185284_a((Enchantment)ench, (LivingEntity)player);
            if (totalLevel > 0) {
                ench.tick(player, side, totalLevel);
            }
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "TickEnchantment Helper";
    }
    
    static {
        INSTANCE = new EventHelperEnchantmentTick();
    }
}
