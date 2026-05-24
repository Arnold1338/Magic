package hellfirepvp.astralsorcery.common.item.quality;

import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemDazzlingGem extends Item
{
    public ItemDazzlingGem() {
        super(new Item.Properties().func_200917_a(1).func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final World worldIn, final List<Component> tooltip, final TooltipFlag flag) {
        getQuality(stack).ifPresent(quality -> tooltip.add(quality.getDisplayName()));
    }
    
    public boolean func_77636_d(final ItemStack stack) {
        return true;
    }
    
    public static boolean setQuality(final ItemStack stack, final GemQuality quality) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemDazzlingGem)) {
            return false;
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        tag.putInt("quality", quality.ordinal());
        return true;
    }
    
    public static Optional<GemQuality> getQuality(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemDazzlingGem)) {
            return Optional.empty();
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (!tag.func_150297_b("quality", 3)) {
            return Optional.empty();
        }
        final int qualityId = tag.getInt("quality");
        if (qualityId < 0 || qualityId >= GemQuality.values().length) {
            return Optional.empty();
        }
        return Optional.of(GemQuality.values()[qualityId]);
    }
}
