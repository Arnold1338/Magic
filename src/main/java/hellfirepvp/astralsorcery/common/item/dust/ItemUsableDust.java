package hellfirepvp.astralsorcery.common.item.dust;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.item.ItemUseContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.dispenser.IBlockSource;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.world.item.Item;

public abstract class ItemUsableDust extends Item implements IDispenseItemBehavior
{
    public ItemUsableDust() {
        super(new Item.Properties().func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    abstract boolean dispense(final IBlockSource p0);
    
    abstract boolean rightClickAir(final World p0, final Player p1, final ItemStack p2);
    
    abstract boolean rightClickBlock(final ItemUseContext p0);
    
    public InteractionResult func_195939_a(final ItemUseContext ctx) {
        if (!ctx.func_195991_k().func_201670_d() && this.rightClickBlock(ctx) && !ctx.func_195999_j().func_184812_l_()) {
            ctx.func_195996_i().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
    
    public InteractionResult<ItemStack> func_77659_a(final World world, final Player player, final Hand hand) {
        final ItemStack held = player.func_184586_b(hand);
        if (!held.isEmpty() && !world.func_201670_d() && this.rightClickAir(world, player, held) && !player.func_184812_l_()) {
            held.shrink(1);
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
    }
    
    public ItemStack dispense(final IBlockSource src, final ItemStack stack) {
        if (this.dispense(src)) {
            stack.shrink(1);
        }
        return stack;
    }
}
