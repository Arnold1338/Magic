package hellfirepvp.astralsorcery.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.auxiliary.link.LinkHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.auxiliary.link.IItemLinkingTool;
import net.minecraft.world.item.Item;

public class ItemLinkingTool extends Item implements IItemLinkingTool
{
    public ItemLinkingTool() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public boolean shouldInterceptBlockInteract(final LogicalSide side, final Player player, final Hand hand, final BlockPos pos, final Direction face) {
        return true;
    }
    
    public boolean shouldInterceptEntityInteract(final LogicalSide side, final Player player, final Hand hand, final Entity interacted) {
        return interacted instanceof Player;
    }
    
    public boolean doBlockInteract(final LogicalSide side, final Player player, final Hand hand, final BlockPos pos, final Direction face) {
        final Level world = player.level();
        if (!world.level()) {
            final LinkHandler.LinkSession session = LinkHandler.getActiveSession(player);
            if (session != null && session.getType() == LinkHandler.LinkType.ENTITY) {
                final LinkHandler.RightClickResult result = LinkHandler.onInteractBlock(player, world, pos, player.func_225608_bj_());
                if (result.shouldProcess()) {
                    LinkHandler.processInteraction(result, player, world, pos);
                    return true;
                }
            }
            final LinkHandler.RightClickResult result = LinkHandler.onInteractBlock(player, world, pos, player.func_225608_bj_());
            if (result.shouldProcess()) {
                LinkHandler.processInteraction(result, player, world, pos);
            }
        }
        else {
            player.func_184609_a(hand);
        }
        return true;
    }
    
    public boolean doEntityInteract(final LogicalSide side, final Player player, final Hand hand, final Entity interacted) {
        if (!(interacted instanceof LivingEntity)) {
            return false;
        }
        final LivingEntity target = (LivingEntity)interacted;
        final Level world = player.level();
        if (!world.level()) {
            final LinkHandler.LinkSession session = LinkHandler.getActiveSession(player);
            if (session == null || session.getType() == LinkHandler.LinkType.ENTITY) {
                final LinkHandler.RightClickResult result = LinkHandler.onInteractEntity(player, target);
                if (result.shouldProcess()) {
                    LinkHandler.processInteraction(result, player, world, BlockPos.field_177992_a);
                }
            }
        }
        else {
            player.func_184609_a(hand);
        }
        return true;
    }
}
