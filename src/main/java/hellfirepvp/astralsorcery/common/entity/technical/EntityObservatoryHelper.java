package hellfirepvp.astralsorcery.common.entity.technical;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.phys.HitResult;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.container.ContainerObservatory;
import net.minecraft.world.entity.player.Player;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import java.util.UUID;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public class EntityObservatoryHelper extends Entity
{
    private static final EntityDataAccessor<BlockPos> FIXED;
    
    public EntityObservatoryHelper(final World worldIn) {
        super((EntityType)EntityTypesAS.OBSERVATORY_HELPER, worldIn);
    }
    
    public static EntityType.IFactory<EntityObservatoryHelper> factory() {
        return (EntityType.IFactory<EntityObservatoryHelper>)((spawnEntity, world) -> new EntityObservatoryHelper(world));
    }
    
    protected void func_70088_a() {
        this.field_70180_af.func_187214_a((EntityDataAccessor)EntityObservatoryHelper.FIXED, (Object)BlockPos.field_177992_a);
    }
    
    public void setFixedObservatoryPos(final BlockPos pos) {
        this.field_70180_af.func_187227_b((EntityDataAccessor)EntityObservatoryHelper.FIXED, (Object)pos);
    }
    
    public BlockPos getFixedObservatoryPos() {
        return (BlockPos)this.field_70180_af.func_187225_a((EntityDataAccessor)EntityObservatoryHelper.FIXED);
    }
    
    @Nullable
    public TileObservatory getAssociatedObservatory() {
        final BlockPos at = this.getFixedObservatoryPos();
        final TileObservatory observatory = MiscUtils.getTileAt((IBlockReader)this.field_70170_p, at, TileObservatory.class, true);
        if (observatory == null) {
            return null;
        }
        final UUID helperRef = observatory.getEntityHelperRef();
        if (helperRef == null || !helperRef.equals(this.getUUID())) {
            return null;
        }
        return observatory;
    }
    
    public void func_70071_h_() {
        super.tick();
        this.field_70145_X = true;
        final TileObservatory observatory;
        if ((observatory = this.getAssociatedObservatory()) == null) {
            if (!this.field_70170_p.func_201670_d()) {
                this.func_70106_y();
            }
            return;
        }
        final Entity riding = (Entity)Iterables.getFirst((Iterable)this.func_184188_bt(), (Object)null);
        if (riding instanceof Player) {
            this.applyObservatoryRotationsFrom(observatory, (Player)riding, true);
        }
        else {
            this.field_70126_B = this.field_70177_z;
            this.field_70127_C = this.field_70125_A;
        }
        if (!observatory.isUsable()) {
            this.func_184226_ay();
        }
    }
    
    public void applyObservatoryRotationsFrom(final TileObservatory to, final Player riding, final boolean updateTile) {
        if (riding.field_71070_bA instanceof ContainerObservatory) {
            this.field_70177_z = riding.field_70759_as;
            this.field_70126_B = riding.field_70758_at;
            this.field_70125_A = riding.field_70125_A;
            this.field_70127_C = riding.field_70127_C;
        }
        else {
            this.field_70177_z = riding.field_70761_aq;
            this.field_70126_B = riding.field_70760_ar;
        }
        to.updatePitchYaw(this.field_70125_A, this.field_70127_C, this.field_70177_z, this.field_70126_B);
        if (updateTile) {
            to.markForUpdate();
        }
        final double xOffset = -0.85;
        final double zOffset = 0.15;
        final double yawRad = -Math.toRadians(to.observatoryYaw);
        final double xComp = 0.5 + Math.sin(yawRad) * xOffset - Math.cos(yawRad) * zOffset;
        final double zComp = 0.5 + Math.cos(yawRad) * xOffset + Math.sin(yawRad) * zOffset;
        final Vector3 pos = new Vector3((Vector3i)to.func_174877_v()).add(xComp, 0.4000000059604645, zComp);
        this.func_226286_f_(pos.getX(), pos.getY(), pos.getZ());
    }
    
    protected boolean func_184228_n(final Entity entityIn) {
        if (!super.func_184228_n(entityIn)) {
            return false;
        }
        final TileObservatory observatory = this.getAssociatedObservatory();
        return observatory != null && observatory.isUsable();
    }
    
    public boolean func_174814_R() {
        return true;
    }
    
    public boolean func_70027_ad() {
        return false;
    }
    
    public boolean func_225510_bt_() {
        return false;
    }
    
    public boolean func_96092_aw() {
        return false;
    }
    
    public boolean func_180427_aV() {
        return true;
    }
    
    protected boolean func_225502_at_() {
        return false;
    }
    
    public boolean func_184186_bw() {
        return false;
    }
    
    public ItemStack getPickedResult(final HitResult target) {
        return new ItemStack((ItemLike)BlocksAS.OBSERVATORY);
    }
    
    protected void func_70037_a(final CompoundTag compound) {
    }
    
    protected void func_213281_b(final CompoundTag compound) {
    }
    
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
    
    static {
        FIXED = SynchedEntityData.func_187226_a((Class)EntityObservatoryHelper.class, EntityDataSerializers.field_187200_j);
    }
}
