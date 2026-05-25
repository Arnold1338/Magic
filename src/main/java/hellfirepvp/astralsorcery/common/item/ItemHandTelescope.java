package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemHandTelescope extends Item
{
    public ItemHandTelescope() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public InteractionResult<ItemStack> use(final Level world, final Player player, final Hand hand) {
        final ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) {
            return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
        }
        if (world.level()) {
            AstralSorcery.getProxy().openGui(player, GuiType.HAND_TELESCOPE, new Object[0]);
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
    }
}
