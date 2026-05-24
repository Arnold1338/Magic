package hellfirepvp.astralsorcery.common.fluid;

import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import java.util.function.Supplier;
import net.minecraft.world.item.BucketItem;

public class ItemLiquidStarlightBucket extends BucketItem
{
    public ItemLiquidStarlightBucket(final Supplier<? extends Fluid> fluidSupplier) {
        super((Supplier)fluidSupplier, new Item.Properties().func_200919_a(Items.field_151133_ar).func_200917_a(1).func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
        return (ICapabilityProvider)new FluidBucketWrapper(stack);
    }
}
