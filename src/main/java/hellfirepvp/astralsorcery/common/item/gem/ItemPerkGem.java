package hellfirepvp.astralsorcery.common.item.gem;

import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import java.util.List;
import hellfirepvp.astralsorcery.common.perk.node.socket.GemSocketPerk;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.DynamicModifierHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.perk.node.socket.GemSocketItem;
import net.minecraft.world.level.item.Item;

public abstract class ItemPerkGem extends Item implements GemSocketItem
{
    private final GemType type;
    
    public ItemPerkGem(final GemType type) {
        super(new Item.Properties().func_200917_a(1).func_200916_a(CommonProxy.ITEM_GROUP_AS));
        this.type = type;
    }
    
    public void func_77663_a(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (world.func_201670_d()) {
            return;
        }
        if (DynamicModifierHelper.getStaticModifiers(stack).isEmpty()) {
            GemAttributeHelper.rollGem(stack);
        }
    }
    
    @Nullable
    public static GemType getGemType(final ItemStack gem) {
        if (gem.isEmpty() || !(gem.getItem() instanceof ItemPerkGem)) {
            return null;
        }
        return ((ItemPerkGem)gem.getItem()).type;
    }
    
    public <T extends AbstractPerk & GemSocketPerk> boolean canBeInserted(final ItemStack stack, final T perk, final Player player, final PlayerProgress progress, final LogicalSide side) {
        return !this.getModifiers(stack, perk, player, side).isEmpty();
    }
    
    public <T extends AbstractPerk & GemSocketPerk> List<DynamicAttributeModifier> getModifiers(final ItemStack stack, final T perk, final Player player, final LogicalSide side) {
        return DynamicModifierHelper.getStaticModifiers(stack);
    }
}
