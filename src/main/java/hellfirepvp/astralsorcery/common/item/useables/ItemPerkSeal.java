package hellfirepvp.astralsorcery.common.item.useables;

import net.minecraftforge.items.IItemHandlerModifiable;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.core.Direction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemPerkSeal extends Item
{
    public ItemPerkSeal() {
        super(new Item.Properties().func_200918_c(0).func_200917_a(16).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public static int getPlayerSealCount(final Player player) {
        final LazyOptional<IItemHandler> cap = (LazyOptional<IItemHandler>)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (Direction)null);
        return getPlayerSealCount((IItemHandler)cap.orElse((Object)null));
    }
    
    public static int getPlayerSealCount(final IItemHandler inv) {
        if (inv == null) {
            return 0;
        }
        return ItemUtils.findItemsInInventory(inv, new ItemStack((ItemLike)ItemsAS.PERK_SEAL), false).stream().mapToInt(ItemStack::func_190916_E).sum();
    }
    
    public static boolean useSeal(final Player player, final boolean simulate) {
        return useSeal((IItemHandlerModifiable)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (Direction)null).orElse((Object)null), simulate);
    }
    
    public static boolean useSeal(final IItemHandlerModifiable inv, final boolean simulate) {
        return inv != null && ItemUtils.consumeFromInventory(inv, new ItemStack((ItemLike)ItemsAS.PERK_SEAL), simulate);
    }
}
