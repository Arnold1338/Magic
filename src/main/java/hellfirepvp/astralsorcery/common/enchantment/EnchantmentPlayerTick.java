package hellfirepvp.astralsorcery.common.enchantment;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.item.enchantment.Enchantment;

public abstract class EnchantmentPlayerTick extends Enchantment
{
    protected EnchantmentPlayerTick(final Enchantment.Rarity rarityIn, final EnchantmentCategory typeIn, final EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }
    
    public abstract void tick(final Player p0, final LogicalSide p1, final int p2);
}
