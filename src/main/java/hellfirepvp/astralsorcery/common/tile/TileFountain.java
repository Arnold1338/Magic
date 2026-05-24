package hellfirepvp.astralsorcery.common.tile;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.crafting.nojson.FountainEffectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrime;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.fluid.FluidLiquidStarlight;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.util.tile.SimpleSingleFluidTank;
import hellfirepvp.astralsorcery.common.util.tile.FluidTankAccess;
import hellfirepvp.astralsorcery.common.crafting.nojson.fountain.FountainEffect;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileFountain extends TileEntityTick
{
    private static final int TANK_SIZE = 16000;
    private static final int LIQUID_STARLIGHT_TANK_SIZE = 16000;
    private FountainEffect<?> currentEffect;
    private FountainEffect.EffectContext effectContext;
    private int tickDrawLiquidStarlight;
    private int tickActiveFountainEffect;
    private int mbLiquidStarlight;
    private final FluidTankAccess access;
    private final SimpleSingleFluidTank tank;
    
    public TileFountain() {
        super(TileEntityTypesAS.FOUNTAIN);
        this.tickDrawLiquidStarlight = 0;
        this.tickActiveFountainEffect = 0;
        this.mbLiquidStarlight = 0;
        (this.tank = new SimpleSingleFluidTank(16000)).addUpdateFunction(this::markForUpdate);
        (this.access = new FluidTankAccess()).putTank(0, (IFluidTank)this.tank, new Direction[0]);
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.func_145831_w().func_201670_d()) {
            if (this.hasMultiblock()) {
                this.updateFountainComponents();
                this.drawLiquidStarlight();
                final FountainEffect effect = this.getCurrentEffect();
                if (effect != null) {
                    final FountainEffect.EffectContext ctx = this.effectContext;
                    if (!this.consumeLiquidStarlight(1)) {
                        this.setCurrentEffect(null);
                        this.replaceCurrentEffect(effect, ctx, null);
                        return;
                    }
                    final FountainEffect.OperationSegment segment = this.getSegment();
                    if (segment != FountainEffect.OperationSegment.RUNNING) {
                        ++this.tickActiveFountainEffect;
                    }
                    final FountainEffect.OperationSegment nextSegment = this.getSegment();
                    if (segment != nextSegment) {
                        effect.transition(this, ctx, LogicalSide.SERVER, segment, nextSegment);
                        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.FOUNTAIN_TRANSITION_SEGMENT).addData(buf -> {
                            ByteBufUtils.writePos(buf, this.field_174879_c);
                            ByteBufUtils.writeEnumValue(buf, segment);
                            ByteBufUtils.writeEnumValue(buf, nextSegment);
                            return;
                        });
                        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.field_145850_b, (Vector3i)this.field_174879_c, 32.0));
                    }
                    effect.tick(this, ctx, this.tickActiveFountainEffect, LogicalSide.SERVER, this.getSegment());
                }
            }
        }
        else if (this.hasMultiblock()) {
            final FountainEffect effect = this.getCurrentEffect();
            if (effect != null) {
                effect.tick(this, this.effectContext, this.tickActiveFountainEffect, LogicalSide.CLIENT, this.getSegment());
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playTransitionEffect(final PktPlayEffect pktPlayEffect) {
        final BlockPos at = ByteBufUtils.readPos(pktPlayEffect.getExtraData());
        final FountainEffect.OperationSegment segment = ByteBufUtils.readEnumValue(pktPlayEffect.getExtraData(), FountainEffect.OperationSegment.class);
        final FountainEffect.OperationSegment nextSegment = ByteBufUtils.readEnumValue(pktPlayEffect.getExtraData(), FountainEffect.OperationSegment.class);
        final World world = (World)Minecraft.getInstance().field_71441_e;
        if (world == null) {
            return;
        }
        final TileFountain fountain = MiscUtils.getTileAt((IBlockReader)world, at, TileFountain.class, false);
        if (fountain == null) {
            return;
        }
        final FountainEffect effect = fountain.getCurrentEffect();
        if (effect == null) {
            return;
        }
        effect.transition(fountain, fountain.effectContext, LogicalSide.CLIENT, segment, nextSegment);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void replaceEffect(final PktPlayEffect pktPlayEffect) {
        final BlockPos at = ByteBufUtils.readPos(pktPlayEffect.getExtraData());
        final World world = (World)Minecraft.getInstance().field_71441_e;
        if (world == null) {
            return;
        }
        final TileFountain fountain = MiscUtils.getTileAt((IBlockReader)world, at, TileFountain.class, false);
        if (fountain == null) {
            return;
        }
        final FountainEffect effect = fountain.getCurrentEffect();
        if (effect == null) {
            return;
        }
        effect.onReplace(fountain, fountain.effectContext, null, LogicalSide.CLIENT);
    }
    
    private void drawLiquidStarlight() {
        --this.tickDrawLiquidStarlight;
        if (this.tickDrawLiquidStarlight <= 0) {
            this.tickDrawLiquidStarlight = 100;
            if (this.mbLiquidStarlight < 12800.0f && this.currentEffect != null) {
                final TileChalice chalice = MiscUtils.getTileAt((IBlockReader)this.field_145850_b, this.field_174879_c.above(), TileChalice.class, false);
                if (chalice != null) {
                    final FluidStack fluid = chalice.getTank().drain(400, IFluidHandler.FluidAction.SIMULATE);
                    if (!fluid.isEmpty() && fluid.getFluid() instanceof FluidLiquidStarlight) {
                        final FluidStack drained = chalice.getTank().drain(new FluidStack(fluid, 400), IFluidHandler.FluidAction.EXECUTE);
                        this.mbLiquidStarlight += drained.getAmount();
                        this.markForUpdate();
                    }
                }
            }
        }
    }
    
    private void updateFountainComponents() {
        final FountainEffect prevEffect = this.getCurrentEffect();
        final FountainEffect.EffectContext prevContext = this.effectContext;
        final BlockState primeState = this.field_145850_b.getBlockState(this.field_174879_c.func_177977_b());
        if (primeState.getBlock() instanceof BlockFountainPrime) {
            if (this.setCurrentEffect(((BlockFountainPrime)primeState.getBlock()).provideEffect()) && prevEffect != null) {
                this.replaceCurrentEffect(prevEffect, prevContext, this.getCurrentEffect());
            }
        }
        else if (this.setCurrentEffect(null) && prevEffect != null) {
            this.replaceCurrentEffect(prevEffect, prevContext, null);
        }
    }
    
    private void replaceCurrentEffect(final FountainEffect prevEffect, final FountainEffect.EffectContext prevContext, final FountainEffect newEffect) {
        prevEffect.onReplace(this, prevContext, newEffect, LogicalSide.SERVER);
        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.FOUNTAIN_REPLACE_EFFECT).addData(buf -> ByteBufUtils.writePos(buf, this.field_174879_c));
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.field_145850_b, (Vector3i)this.field_174879_c, 32.0));
    }
    
    private boolean setCurrentEffect(@Nullable final FountainEffect<?> effect) {
        if (!Objects.equals(this.currentEffect, effect)) {
            this.tickActiveFountainEffect = 0;
            this.currentEffect = effect;
            if (this.currentEffect == null) {
                this.effectContext = null;
            }
            else {
                this.effectContext = (FountainEffect.EffectContext)this.currentEffect.createContext(this);
            }
            this.markForUpdate();
            return true;
        }
        return false;
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        return StructureTypesAS.PTYPE_FOUNTAIN;
    }
    
    public boolean consumeLiquidStarlight(final int amount) {
        if (amount <= 0) {
            return this.mbLiquidStarlight > 0;
        }
        if (this.mbLiquidStarlight >= amount) {
            this.mbLiquidStarlight -= amount;
            this.markForUpdate();
            return true;
        }
        this.mbLiquidStarlight = 0;
        this.markForUpdate();
        return false;
    }
    
    public FountainEffect.OperationSegment getSegment() {
        if (this.getCurrentEffect() == null || this.tickActiveFountainEffect == 0) {
            return FountainEffect.OperationSegment.INACTIVE;
        }
        if (this.getCurrentEffect().isInTick(FountainEffect.OperationSegment.STARTUP, this.tickActiveFountainEffect)) {
            return FountainEffect.OperationSegment.STARTUP;
        }
        if (this.getCurrentEffect().isInTick(FountainEffect.OperationSegment.PREPARATION, this.tickActiveFountainEffect)) {
            return FountainEffect.OperationSegment.PREPARATION;
        }
        return FountainEffect.OperationSegment.RUNNING;
    }
    
    @Nullable
    public FountainEffect<?> getCurrentEffect() {
        return this.currentEffect;
    }
    
    public int getTickActiveFountainEffect() {
        return this.tickActiveFountainEffect;
    }
    
    @Nonnull
    public SimpleSingleFluidTank getTank() {
        return this.tank;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.tickActiveFountainEffect = compound.getInt("tickActiveFountainEffect");
        this.mbLiquidStarlight = compound.getInt("mbLiquidStarlight");
        this.tank.readNBT(compound.func_74775_l("tank"));
        if (compound.contains("currentEffect")) {
            final FountainEffect<?> prevEffect = this.getCurrentEffect();
            final ResourceLocation key = new ResourceLocation(compound.getString("currentEffect"));
            this.currentEffect = FountainEffectRegistry.getEffect(key);
            if (this.currentEffect != null) {
                if (!Objects.equals(this.currentEffect, prevEffect)) {
                    this.effectContext = (FountainEffect.EffectContext)this.currentEffect.createContext(this);
                }
                this.effectContext.readFromNBT(compound.func_74775_l("currentEffectData"));
            }
            else {
                this.effectContext = null;
            }
        }
        else {
            this.currentEffect = null;
            this.effectContext = null;
        }
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.putInt("tickActiveFountainEffect", this.tickActiveFountainEffect);
        compound.putInt("mbLiquidStarlight", this.mbLiquidStarlight);
        compound.put("tank", (Tag)this.tank.writeNBT());
        if (this.currentEffect != null) {
            compound.putString("currentEffect", this.currentEffect.getId().toString());
            if (this.effectContext != null) {
                final CompoundTag tag = new CompoundTag();
                this.effectContext.writeToNBT(tag);
                compound.put("currentEffectData", (Tag)tag);
            }
        }
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (this.access.hasCapability(cap, side)) {
            return (LazyOptional<T>)this.access.getCapability(side).cast();
        }
        return (LazyOptional<T>)super.getCapability((Capability)cap, side);
    }
}
