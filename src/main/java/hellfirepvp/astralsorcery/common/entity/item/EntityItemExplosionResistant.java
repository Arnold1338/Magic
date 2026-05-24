package hellfirepvp.astralsorcery.common.entity.item;

import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;

public class EntityItemExplosionResistant extends EntityItemHighlighted
{
    public EntityItemExplosionResistant(final EntityType<? extends ItemEntity> type, final World world) {
        super(type, world);
    }
    
    public EntityItemExplosionResistant(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z) {
        super(type, world, x, y, z);
    }
    
    public EntityItemExplosionResistant(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z, final ItemStack stack) {
        super(type, world, x, y, z, stack);
    }
    
    public static EntityType.IFactory<EntityItemExplosionResistant> factoryExplosionResistant() {
        return (EntityType.IFactory<EntityItemExplosionResistant>)((spawnEntity, world) -> new EntityItemExplosionResistant(EntityTypesAS.ITEM_EXPLOSION_RESISTANT, world));
    }
    
    public boolean func_70097_a(final DamageSource source, final float amount) {
        return !source.func_94541_c() && super.hurt(source, amount);
    }
    
    @Override
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
}
