package hellfirepvp.astralsorcery.common.tile.base;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraft.world.level.level.block.entity.BlockEntityType;
import java.awt.Color;
import net.minecraft.world.level.level.block.state.BlockState;

public abstract class TileFakedState extends TileEntityTick
{
    private BlockState fakedState;
    private Color overlayColor;
    
    protected TileFakedState(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.fakedState = Blocks.field_150350_a.defaultBlockState();
        this.overlayColor = Color.WHITE;
    }
    
    public boolean revert() {
        return !this.func_145831_w().func_201670_d() && this.func_145831_w().func_180501_a(this.func_174877_v(), this.getFakedState(), 11);
    }
    
    @Nonnull
    public BlockState getFakedState() {
        return this.fakedState;
    }
    
    @Nonnull
    public Color getOverlayColor() {
        return this.overlayColor;
    }
    
    public void setFakedState(@Nonnull final BlockState fakedState) {
        this.fakedState = fakedState;
        this.markForUpdate();
    }
    
    public void setOverlayColor(@Nonnull final Color overlayColor) {
        this.overlayColor = overlayColor;
        this.markForUpdate();
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.fakedState = NBTHelper.getBlockStateFromTag(compound.func_74775_l("fakedState"), Blocks.field_150350_a.defaultBlockState());
        this.overlayColor = new Color(compound.getInt("color"), false);
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        NBTHelper.setBlockState(compound, "fakedState", this.fakedState);
        compound.putInt("color", this.overlayColor.getRGB());
    }
    
    @OnlyIn(Dist.CLIENT)
    public double func_145833_n() {
        return 65536.0;
    }
}
