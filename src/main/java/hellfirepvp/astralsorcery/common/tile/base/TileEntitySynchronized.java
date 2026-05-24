package hellfirepvp.astralsorcery.common.tile.base;

import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import java.util.Random;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TileEntitySynchronized extends BlockEntity implements ILocatable
{
    protected static final Random rand;
    protected static final AABB BOX;
    
    protected TileEntitySynchronized(final BlockEntityType<?> tileEntityTypeIn) {
        super((BlockEntityType)tileEntityTypeIn);
    }
    
    public BlockPos getLocationPos() {
        return this.func_174877_v();
    }
    
    public void func_230337_a_(final BlockState state, final CompoundTag nbt) {
        super.func_230337_a_(state, nbt);
        this.readCustomNBT(nbt);
        this.readSaveNBT(nbt);
    }
    
    public void readCustomNBT(final CompoundTag compound) {
    }
    
    public void readNetNBT(final CompoundTag compound) {
    }
    
    public void readSaveNBT(final CompoundTag compound) {
    }
    
    public final CompoundTag func_189515_b(CompoundTag compound) {
        compound = super.func_189515_b(compound);
        this.writeCustomNBT(compound);
        this.writeSaveNBT(compound);
        return compound;
    }
    
    public void writeCustomNBT(final CompoundTag compound) {
    }
    
    public void writeNetNBT(final CompoundTag compound) {
    }
    
    public void writeSaveNBT(final CompoundTag compound) {
    }
    
    public final SUpdateTileEntityPacket func_189518_D_() {
        final CompoundTag compound = new CompoundTag();
        super.func_189515_b(compound);
        this.writeCustomNBT(compound);
        this.writeNetNBT(compound);
        return new SUpdateTileEntityPacket(this.func_174877_v(), 255, compound);
    }
    
    public CompoundTag func_189517_E_() {
        final CompoundTag compound = new CompoundTag();
        super.func_189515_b(compound);
        this.writeCustomNBT(compound);
        return compound;
    }
    
    public final void onDataPacket(final NetworkManager manager, final SUpdateTileEntityPacket packet) {
        super.onDataPacket(manager, packet);
        this.readCustomNBT(packet.func_148857_g());
        this.readNetNBT(packet.func_148857_g());
        this.onDataReceived();
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void onDataReceived() {
    }
    
    public void markForUpdate() {
        if (this.func_145831_w() != null) {
            final BlockState thisState = this.func_195044_w();
            this.func_145831_w().func_184138_a(this.func_174877_v(), thisState, thisState, 3);
        }
        this.func_70296_d();
    }
    
    public ItemEntity dropItemOnTop(final ItemStack stack) {
        return ItemUtils.dropItem(this.func_145831_w(), this.func_174877_v().getX() + 0.5, this.func_174877_v().getY() + 1.5, this.func_174877_v().getZ() + 0.5, stack);
    }
    
    public boolean removeSelf() {
        return !this.func_145831_w().func_201670_d() && this.func_145831_w().func_175656_a(this.func_174877_v(), Blocks.field_150350_a.defaultBlockState());
    }
    
    static {
        rand = new Random();
        BOX = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }
}
