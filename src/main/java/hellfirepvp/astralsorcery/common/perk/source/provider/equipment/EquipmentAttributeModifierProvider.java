package hellfirepvp.astralsorcery.common.perk.source.provider.equipment;

import net.minecraft.world.item.ItemStack;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.source.AttributeModifierProvider;

public interface EquipmentAttributeModifierProvider extends AttributeModifierProvider
{
    default Collection<PerkAttributeModifier> getModifiers(final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        return (Collection<PerkAttributeModifier>)Collections.emptyList();
    }
    
    Collection<PerkAttributeModifier> getModifiers(final ItemStack p0, final Player p1, final LogicalSide p2, final boolean p3);
}
