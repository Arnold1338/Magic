package hellfirepvp.astralsorcery.common.entity.item;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import javax.annotation.Nullable;
import java.awt.Color;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.reflection.ReflectionHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.datasync.DataParameter;

public class EntityItemHighlighted extends EntityCustomItemReplacement
{
    private static final DataParameter<Integer> DATA_COLOR;
    private static final int NO_COLOR = -16777216;
    
    public EntityItemHighlighted(final EntityType<? extends ItemEntity> type, final World world) {
        super(type, world);
        ReflectionHelper.setSkipItemPhysicsRender(this);
        this.func_213323_x_();
    }
    
    public EntityItemHighlighted(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z) {
        this(type, world);
        this.setPos(x, y, z);
        this.field_70177_z = this.field_70146_Z.nextFloat() * 360.0f;
        this.func_213293_j(this.field_70146_Z.nextDouble() * 0.2 - 0.1, 0.2, this.field_70146_Z.nextDouble() * 0.2 - 0.1);
    }
    
    public EntityItemHighlighted(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z, final ItemStack stack) {
        this(type, world, x, y, z);
        this.func_92058_a(stack);
        this.lifespan = (stack.isEmpty() ? 6000 : stack.getEntityLifespan(world));
    }
    
    public static EntityType.IFactory<EntityItemHighlighted> factoryHighlighted() {
        return (EntityType.IFactory<EntityItemHighlighted>)((spawnEntity, world) -> new EntityItemHighlighted(EntityTypesAS.ITEM_HIGHLIGHT, world));
    }
    
    protected void func_70088_a() {
        super.func_70088_a();
        this.func_184212_Q().func_187214_a((DataParameter)EntityItemHighlighted.DATA_COLOR, (Object)(-16777216));
    }
    
    public void applyColor(@Nullable final Color color) {
        this.func_184212_Q().func_187227_b((DataParameter)EntityItemHighlighted.DATA_COLOR, (Object)((color == null) ? -16777216 : (color.getRGB() & 0xFFFFFF)));
    }
    
    public boolean hasColor() {
        return (int)this.func_184212_Q().func_187225_a((DataParameter)EntityItemHighlighted.DATA_COLOR) != -16777216;
    }
    
    @Nullable
    public Color getHighlightColor() {
        if (!this.hasColor()) {
            return null;
        }
        final int colorInt = (int)this.func_184212_Q().func_187225_a((DataParameter)EntityItemHighlighted.DATA_COLOR);
        return new Color(colorInt, false);
    }
    
    @Override
    public void func_70071_h_() {
        final boolean onGround = this.func_233570_aj_();
        super.tick();
        if (this.func_233570_aj_() != onGround) {
            this.func_213323_x_();
        }
    }
    
    public void func_230245_c_(final boolean grounded) {
        final boolean updateSize = this.func_233570_aj_() != grounded;
        super.func_230245_c_(grounded);
        if (updateSize) {
            this.func_213323_x_();
        }
    }
    
    public EntitySize func_213305_a(final Pose poseIn) {
        if (!this.func_233570_aj_()) {
            return EntityType.field_200765_E.func_220334_j();
        }
        return this.level().func_220334_j();
    }
    
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
    
    static {
        DATA_COLOR = EntityDataManager.func_187226_a((Class)EntityItemHighlighted.class, DataSerializers.field_187192_b);
    }
}
