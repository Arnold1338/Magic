package hellfirepvp.astralsorcery.common.tile;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.tile.network.StarlightTransmissionPrism;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.astralsorcery.common.block.tile.BlockPrism;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.item.lens.LensColorType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;

public class TilePrism extends TileLens
{
    public TilePrism() {
        super(TileEntityTypesAS.PRISM);
    }
    
    @Override
    public boolean isSingleLink() {
        return false;
    }
    
    @Override
    public LensColorType setColorType(@Nullable final LensColorType colorType) {
        final LensColorType returned = super.setColorType(colorType);
        final BlockState thisState = this.func_145831_w().getBlockState(this.func_174877_v());
        if ((boolean)thisState.getValue((Property)BlockPrism.HAS_COLORED_LENS) && colorType == null && returned != null) {
            this.func_145831_w().func_180501_a(this.func_174877_v(), (BlockState)thisState.func_206870_a((Property)BlockPrism.HAS_COLORED_LENS, (Comparable)false), 11);
        }
        else if (!(boolean)thisState.getValue((Property)BlockPrism.HAS_COLORED_LENS) && colorType != null && returned == null) {
            this.func_145831_w().func_180501_a(this.func_174877_v(), (BlockState)thisState.func_206870_a((Property)BlockPrism.HAS_COLORED_LENS, (Comparable)true), 11);
        }
        return returned;
    }
    
    @Override
    public Direction getPlacedAgainst() {
        final BlockState state = this.field_145850_b.getBlockState(this.func_174877_v());
        if (!(state.getBlock() instanceof BlockPrism)) {
            return Direction.DOWN;
        }
        return (Direction)state.getValue((Property)BlockPrism.PLACED_AGAINST);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void onDataReceived() {
        super.onDataReceived();
        this.func_145831_w().func_184138_a(this.func_174877_v(), this.func_195044_w(), this.func_195044_w(), 11);
    }
    
    @Nonnull
    @Override
    public IPrismTransmissionNode provideTransmissionNode(final BlockPos at) {
        return new StarlightTransmissionPrism(at, this.getAttributes());
    }
}
