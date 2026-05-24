package hellfirepvp.astralsorcery.common.entity.item;

import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.world.level.entity.item.ItemEntity;

public class EntityCustomItemReplacement extends ItemEntity
{
    @Nullable
    private ItemEntity replacedEntity;
    
    public EntityCustomItemReplacement(final EntityType<? extends ItemEntity> type, final World world) {
        super((EntityType)type, world);
    }
    
    public EntityCustomItemReplacement(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityCustomItemReplacement(final World worldIn, final double x, final double y, final double z, final ItemStack stack) {
        super(worldIn, x, y, z, stack);
    }
    
    public void setReplacedEntity(@Nullable final ItemEntity replacedEntity) {
        this.replacedEntity = replacedEntity;
    }
    
    public void func_70071_h_() {
        super.tick();
        if (this.func_130014_f_().func_201670_d()) {
            return;
        }
        if (this.replacedEntity != null && this.field_70173_aa < 5 && !this.replacedEntity.isAlive() && this.replacedEntity.field_145804_b == 32767 && this.replacedEntity.field_70292_b == this.func_92059_d().getEntityLifespan(this.func_130014_f_()) - 1) {
            this.func_70106_y();
        }
    }
}
