package hellfirepvp.astralsorcery.common.item;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.entity.item.EntityStarmetal;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemStarmetalIngot extends Item
{
    public ItemStarmetalIngot() {
        super(new Item.Properties().func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    public boolean hasCustomEntity(final ItemStack stack) {
        return true;
    }
    
    @Nullable
    public Entity createEntity(final World world, final Entity location, final ItemStack itemstack) {
        final EntityStarmetal res = new EntityStarmetal(EntityTypesAS.ITEM_STARMETAL_INGOT, world, location.func_226277_ct_(), location.func_226278_cu_(), location.func_226281_cx_(), itemstack);
        res.func_70020_e(location.func_189511_e(new CompoundTag()));
        if (location instanceof ItemEntity) {
            res.setReplacedEntity((ItemEntity)location);
        }
        return (Entity)res;
    }
}
