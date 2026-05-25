package hellfirepvp.astralsorcery.common.item.dust;

import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.dispenser.BlockSource;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;

public abstract class ItemUsableDust extends Item implements DispenseItemBehavior
{
    public ItemUsableDust() {
        super(new Item.Properties().hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    abstract boolean dispense(final BlockSource p0);
    
    abstract boolean rightClickAir(final Level p0, final Player p1, final ItemStack p2);
    
    abstract boolean rightClickBlock(final ItemUseContext p0);
    
    public InteractionResult func_195939_a(final ItemUseContext ctx) {
        if (!ctx.func_195991_k().level() && this.rightClickBlock(ctx) && !ctx.func_195999_j().getVehicle()) {
            ctx.func_195996_i().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
    
    public InteractionResult<ItemStack> func_77659_a(final Level world, final Player player, final Hand hand) {
        final ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty() && !world.level() && this.rightClickAir(world, player, held) && !player.getVehicle()) {
            held.shrink(1);
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
    }
    
    public ItemStack dispense(final BlockSource src, final ItemStack stack) {
        if (this.dispense(src)) {
            stack.shrink(1);
        }
        return stack;
    }
}
