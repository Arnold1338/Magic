package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.world.phys.AABB;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import hellfirepvp.astralsorcery.common.entity.technical.EntityObservatoryHelper;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.util.tile.NamedInventoryTile;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileObservatory extends TileEntityTick implements NamedInventoryTile
{
    private UUID entityHelperRef;
    private Integer entityIdServerRef;
    public float observatoryYaw;
    public float prevObservatoryYaw;
    public float observatoryPitch;
    public float prevObservatoryPitch;
    
    public TileObservatory() {
        super(TileEntityTypesAS.OBSERVATORY);
        this.entityIdServerRef = null;
        this.observatoryYaw = 0.0f;
        this.prevObservatoryYaw = 0.0f;
        this.observatoryPitch = -45.0f;
        this.prevObservatoryPitch = -45.0f;
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.astralsorcery.observatory");
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.getLevel().level()) {
            if (this.entityHelperRef == null) {
                this.createNewObservatoryEntity();
            }
            else {
                final Entity helper;
                if ((helper = this.resolveEntity(this.entityHelperRef)) == null || !helper.isAlive()) {
                    this.createNewObservatoryEntity();
                }
            }
        }
    }
    
    public boolean isUsable() {
        for (int xx = -1; xx <= 1; ++xx) {
            for (int zz = -1; zz <= 1; ++zz) {
                if (xx != 0 || zz != 0) {
                    final BlockPos other = this.field_174879_c.offset(xx, 0, zz);
                    if (!MiscUtils.canSeeSky(this.getLevel(), other, false, true)) {
                        return false;
                    }
                }
            }
        }
        return MiscUtils.canSeeSky(this.getLevel(), this.getBlockState().above(), true, false);
    }
    
    private Entity createNewObservatoryEntity() {
        this.setEntityHelperRef(null);
        this.entityIdServerRef = null;
        final EntityObservatoryHelper helper = (EntityObservatoryHelper)EntityTypesAS.OBSERVATORY_HELPER.func_200721_a(this.getLevel());
        helper.setFixedObservatoryPos(this.getBlockState());
        helper.func_70080_a(this.field_174879_c.getX() + 0.5, this.field_174879_c.getY() + 0.1, this.field_174879_c.getZ() + 0.5, 0.0f, 0.0f);
        this.getLevel().addFreshEntity((Entity)helper);
        this.setEntityHelperRef(helper.getUUID());
        this.entityIdServerRef = helper.func_145782_y();
        return helper;
    }
    
    @Nullable
    private Entity resolveEntity(final UUID entityUUID) {
        if (entityUUID == null) {
            return null;
        }
        for (final Entity e : this.level.func_217357_a((Class)Entity.class, new AABB(this.field_174879_c.offset(-3, -1, -3), this.field_174879_c.offset(3, 2, 3)))) {
            if (e.getUUID().equals(entityUUID)) {
                this.entityIdServerRef = e.func_145782_y();
                return e;
            }
        }
        return null;
    }
    
    @Nullable
    public Entity findRideableObservatoryEntity() {
        if (this.getEntityHelperRef() == null || this.entityIdServerRef == null) {
            return null;
        }
        return this.getLevel().getEntityById((int)this.entityIdServerRef);
    }
    
    @Nullable
    public UUID getEntityHelperRef() {
        return this.entityHelperRef;
    }
    
    public void setEntityHelperRef(final UUID entityHelperRef) {
        this.entityHelperRef = entityHelperRef;
        this.markForUpdate();
    }
    
    public void updatePitchYaw(final float pitch, final float prevPitch, final float yaw, final float prevYaw) {
        this.observatoryPitch = pitch;
        this.prevObservatoryPitch = prevPitch;
        this.observatoryYaw = yaw;
        this.prevObservatoryYaw = prevYaw;
    }
    
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return TileObservatory.INFINITE_EXTENT_AABB;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.entityHelperRef = NBTHelper.getUUID(compound, "entity", null);
        this.observatoryYaw = compound.getFloat("oYaw");
        this.observatoryPitch = compound.getFloat("oPitch");
        this.prevObservatoryYaw = compound.getFloat("oYawPrev");
        this.prevObservatoryPitch = compound.getFloat("oPitchPrev");
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        if (this.entityHelperRef != null) {
            compound.putUUID("entity", this.entityHelperRef);
        }
        compound.func_74776_a("oYaw", this.observatoryYaw);
        compound.func_74776_a("oPitch", this.observatoryPitch);
        compound.func_74776_a("oYawPrev", this.prevObservatoryYaw);
        compound.func_74776_a("oPitchPrev", this.prevObservatoryPitch);
    }
}
