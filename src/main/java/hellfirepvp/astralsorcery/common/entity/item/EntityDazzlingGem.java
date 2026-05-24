package hellfirepvp.astralsorcery.common.entity.item;

import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;

public class EntityDazzlingGem extends EntityItemExplosionResistant
{
    public EntityDazzlingGem(final EntityType<? extends ItemEntity> type, final World world) {
        super(type, world);
    }
    
    public EntityDazzlingGem(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z) {
        super(type, world, x, y, z);
    }
    
    public EntityDazzlingGem(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z, final ItemStack stack) {
        super(type, world, x, y, z, stack);
    }
    
    public static EntityType.IFactory<EntityDazzlingGem> factoryGem() {
        return (EntityType.IFactory<EntityDazzlingGem>)((spawnEntity, world) -> new EntityDazzlingGem(EntityTypesAS.ITEM_CRYSTAL, world));
    }
    
    @Override
    public void func_70071_h_() {
        super.tick();
        if (!this.field_70170_p.func_201670_d() && this.field_70292_b + 10 >= this.lifespan) {
            this.field_70292_b = 0;
        }
    }
}
