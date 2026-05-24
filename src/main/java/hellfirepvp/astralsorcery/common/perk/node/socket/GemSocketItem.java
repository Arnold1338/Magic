package hellfirepvp.astralsorcery.common.perk.node.socket;

import net.minecraft.network.chat.MutableComponent;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import java.util.List;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface GemSocketItem
{
    default <T extends AbstractPerk & GemSocketPerk> void onInsert(final ItemStack stack, final T perk, final Player player, final PlayerProgress progress) {
    }
    
    default <T extends AbstractPerk & GemSocketPerk> void onExtract(final ItemStack stack, final T perk, final Player player, final PlayerProgress progress) {
    }
    
    default <T extends AbstractPerk & GemSocketPerk> boolean canBeInserted(final ItemStack stack, final T perk, final Player player, final PlayerProgress progress, final LogicalSide side) {
        return true;
    }
    
    default <T extends AbstractPerk & GemSocketPerk> List<DynamicAttributeModifier> getModifiers(final ItemStack stack, final T perk, final Player player, final LogicalSide side) {
        return new ArrayList<DynamicAttributeModifier>();
    }
    
    default <T extends AbstractPerk & GemSocketPerk> void addTooltip(final ItemStack stack, final T perk, final List<MutableComponent> toolTip) {
    }
}
