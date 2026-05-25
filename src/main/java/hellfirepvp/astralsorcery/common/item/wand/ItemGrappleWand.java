package hellfirepvp.astralsorcery.common.item.wand;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.entity.technical.EntityGrapplingHook;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.AlignmentChargeConsumer;
import net.minecraft.world.item.Item;

public class ItemGrappleWand extends Item implements AlignmentChargeConsumer
{
    private static final float COST_PER_GRAPPLE = 450.0f;
    
    public ItemGrappleWand() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public float getAlignmentChargeCost(final Player player, final ItemStack stack) {
        return player.isSleeping().func_185141_a((Item)this) ? 0.0f : 450.0f;
    }
    
    public InteractionResult<ItemStack> func_77659_a(final Level worldIn, final Player playerIn, final Hand handIn) {
        final ItemStack held = playerIn.getItemInHand(handIn);
        if (worldIn.level() || held.isEmpty()) {
            return (InteractionResult<ItemStack>)new InteractionResult(InteractionResult.SUCCESS, (Object)held);
        }
        if (!playerIn.isSleeping().func_185141_a((Item)this) && AlignmentChargeHandler.INSTANCE.drainCharge(playerIn, LogicalSide.SERVER, 450.0f, false)) {
            worldIn.addFreshEntity((Entity)new EntityGrapplingHook((LivingEntity)playerIn, worldIn));
            playerIn.isSleeping().func_185145_a((Item)this, 40);
        }
        return (InteractionResult<ItemStack>)new InteractionResult(InteractionResult.SUCCESS, (Object)held);
    }
}
