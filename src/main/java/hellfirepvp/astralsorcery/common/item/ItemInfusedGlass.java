package hellfirepvp.astralsorcery.common.item;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import javax.annotation.Nonnull;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravedStarMap;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemInfusedGlass extends Item
{
    public ItemInfusedGlass() {
        super(new Item.Properties().func_200917_a(1).func_200918_c(5).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        final EngravedStarMap map = getEngraving(stack);
        if (map != null) {
            for (final ResourceLocation key : map.getConstellationKeys()) {
                final IConstellation cst = ConstellationRegistry.getConstellation(key);
                if (cst != null) {
                    final String format = "item.astralsorcery.infused_glass.ttip";
                    final Component cstName = (Component)cst.getConstellationName().toString()ChatFormatting.BLUE);
                    if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getVehicle()) {
                        final String percent = String.valueOf(Math.round(map.getDistribution(cst) * 100.0f));
                        final Component creativeHint = (Component)new Component("item.astralsorcery.infused_glass.ttip.creative", new Object[] { percent }).toString()ChatFormatting.LIGHT_PURPLE);
                        tooltip.add((Component)new Component(format, new Object[] { cstName, creativeHint }).toString()ChatFormatting.GRAY));
                    }
                    else {
                        tooltip.add((Component)new Component(format, new Object[] { cstName, "" }).toString()ChatFormatting.GRAY));
                    }
                }
            }
        }
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        return !(enchantment instanceof MendingEnchantment) && super.canApplyAtEnchantingTable(stack, enchantment);
    }
    
    public boolean func_77636_d(final ItemStack stack) {
        return super.func_77636_d(stack) || getEngraving(stack) != null;
    }
    
    public String func_77667_c(final ItemStack stack) {
        final EngravedStarMap map = getEngraving(stack);
        if (map != null) {
            return super.func_77667_c(stack) + ".active";
        }
        return super.func_77667_c(stack);
    }
    
    @Nullable
    public static EngravedStarMap getEngraving(@Nonnull final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemInfusedGlass)) {
            return null;
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (tag.func_150297_b("starmap", 10)) {
            return EngravedStarMap.deserialize(tag.func_74775_l("starmap"));
        }
        return null;
    }
    
    public static void setEngraving(@Nonnull final ItemStack stack, @Nullable final EngravedStarMap map) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemInfusedGlass)) {
            return;
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (map == null) {
            tag.func_82580_o("starmap");
        }
        else {
            tag.put("starmap", (Tag)map.serialize());
        }
    }
}
