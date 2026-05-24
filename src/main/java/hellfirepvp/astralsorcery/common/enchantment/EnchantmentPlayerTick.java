package hellfirepvp.astralsorcery.common.enchantment;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.world.item.enchantment.Enchantment;

public abstract class EnchantmentPlayerTick extends Enchantment
{
    protected EnchantmentPlayerTick(final Enchantment.Rarity rarityIn, final EnchantmentType typeIn, final EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }
    
    public abstract void tick(final Player p0, final LogicalSide p1, final int p2);
}
