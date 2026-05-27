package hellfirepvp.astralsorcery.common.tile.base;

import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
        return this.getBlockState();
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
    
    public final ClientboundBlockEntityDataPacket getUpdatePacket() {
        final CompoundTag compound = new CompoundTag();
        super.func_189515_b(compound);
        this.writeCustomNBT(compound);
        this.writeNetNBT(compound);
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    public CompoundTag func_189517_E_() {
        final CompoundTag compound = new CompoundTag();
        super.func_189515_b(compound);
        this.writeCustomNBT(compound);
        return compound;
    }
    
    public final void onDataPacket(final net.minecraft.network.Connection manager, final ClientboundBlockEntityDataPacket packet) {
        this.handleTag(packet.getTag());
        this.readCustomNBT(packet.func_148857_g());
        this.readNetNBT(packet.func_148857_g());
        this.onDataReceived();
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void onDataReceived() {
    }
    
    public void markForUpdate() {
        if (this.getLevel() != null) {
            final BlockState thisState = this.func_195044_w();
            this.getLevel().func_184138_a(this.getBlockState(), thisState, thisState, 3);
        }
        this.setChanged();
    }
    
    public ItemEntity dropItemOnTop(final ItemStack stack) {
        return ItemUtils.dropItem(this.getLevel(), this.getBlockState().getX() + 0.5, this.getBlockState().getY() + 1.5, this.getBlockState().getZ() + 0.5, stack);
    }
    
    public boolean removeSelf() {
        return !this.getLevel().level() && this.getLevel().func_175656_a(this.getBlockState(), Blocks.AIR.defaultBlockState());
    }
    
    static {
        rand = new Random();
        BOX = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }
}
