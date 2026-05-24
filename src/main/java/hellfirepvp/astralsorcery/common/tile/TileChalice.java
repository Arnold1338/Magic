package hellfirepvp.astralsorcery.common.tile;

import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nullable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.effect.function.VFXScaleFunction;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.effect.vfx.FXCube;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.util.ColorizationHelper;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.fluid.FluidLiquidStarlight;
import net.minecraftforge.fluids.capability.IFluidHandler;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicates;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.block.Block;
import java.util.Iterator;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteractionContext;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.BlockPos;
import java.util.List;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.auxiliary.ChaliceHelper;
import hellfirepvp.astralsorcery.common.block.tile.BlockFountain;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.tile.FluidTankAccess;
import hellfirepvp.astralsorcery.common.util.tile.SimpleSingleFluidTank;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileChalice extends TileEntityTick
{
    private static final int TANK_SIZE = 64000;
    private final SimpleSingleFluidTank tank;
    private final FluidTankAccess access;
    private int nextInteraction;
    private Vector3 rotation;
    private Vector3 prevRotation;
    private Vector3 rotationVec;
    
    public TileChalice() {
        super(TileEntityTypesAS.CHALICE);
        this.nextInteraction = -1;
        this.rotation = new Vector3();
        this.prevRotation = new Vector3();
        this.rotationVec = null;
        (this.tank = new SimpleSingleFluidTank(64000)).addUpdateFunction(this::markForUpdate);
        (this.access = new FluidTankAccess()).putTank(0, (IFluidTank)this.tank, Direction.DOWN);
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (this.func_145831_w().func_201670_d()) {
            if (this.rotationVec == null) {
                this.rotationVec = Vector3.random().normalize().multiply(1.5f);
            }
            this.prevRotation = this.rotation.clone();
            this.rotation.add(this.rotationVec);
        }
        else {
            if (this.nextInteraction == -1) {
                this.nextInteraction = this.ticksExisted + 20 + TileChalice.rand.nextInt(40);
            }
            if (this.ticksExisted < this.nextInteraction) {
                return;
            }
            this.nextInteraction = this.ticksExisted + 20 + TileChalice.rand.nextInt(40);
            if (!this.tickLightwellDraw() && !this.tickFountainDraw()) {
                this.tickChaliceInteractions();
            }
        }
    }
    
    private void tickChaliceInteractions() {
        if (this.func_145831_w().func_175640_z(this.field_174879_c) || this.func_145831_w().getBlockState(this.func_174877_v().func_177977_b()).getBlock() instanceof BlockFountain) {
            return;
        }
        final FluidStack thisFluid = this.getTank().getFluid();
        if (thisFluid.isEmpty()) {
            return;
        }
        final List<BlockPos> chalicePositions = ChaliceHelper.findNearbyChalices(this.func_145831_w(), this.func_174877_v(), 16);
        Collections.shuffle(chalicePositions, TileChalice.rand);
        for (final BlockPos otherChalicePos : chalicePositions) {
            final TileChalice otherChalice = MiscUtils.getTileAt((IBlockReader)this.func_145831_w(), otherChalicePos, TileChalice.class, false);
            if (otherChalice == null) {
                continue;
            }
            final FluidStack otherFluid = otherChalice.getTank().getFluid();
            if (otherFluid.isEmpty()) {
                continue;
            }
            final LiquidInteractionContext ctx = new LiquidInteractionContext(thisFluid, otherFluid);
            final List<LiquidInteraction> recipes = RecipeTypesAS.TYPE_LIQUID_INTERACTION.findMatchingRecipes(ctx);
            for (LiquidInteraction recipe = LiquidInteraction.pickRecipe(recipes); recipe != null; recipe = LiquidInteraction.pickRecipe(recipes)) {
                if (recipe.consumeInputs(this, otherChalice)) {
                    final Vector3 thisChaliceV = new Vector3(this).add(0.5, 1.5, 0.5);
                    final Vector3 otherChaliceV = new Vector3((Vector3i)otherChalicePos).add(0.5, 1.5, 0.5);
                    final Vector3 target = thisChaliceV.getMidpoint(otherChaliceV);
                    recipe.getResult().doResult(this.func_145831_w(), target.clone());
                    final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.LIQUID_INTERACTION_LINE).addData(buf -> {
                        ByteBufUtils.writeVector(buf, thisChaliceV);
                        ByteBufUtils.writeVector(buf, target);
                        ByteBufUtils.writeFluidStack(buf, thisFluid);
                        ByteBufUtils.writeVector(buf, otherChaliceV);
                        ByteBufUtils.writeVector(buf, target);
                        ByteBufUtils.writeFluidStack(buf, otherFluid);
                        return;
                    });
                    PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.func_145831_w(), (Vector3i)target.toBlockPos(), 32.0));
                    return;
                }
                recipes.remove(recipe);
            }
        }
    }
    
    private boolean tickFountainDraw() {
        if (this.func_145831_w().func_175640_z(this.field_174879_c)) {
            return false;
        }
        final Vector3 thisVector = new Vector3(this).add(0.5, 1.5, 0.5);
        final List<BlockPos> fountains = BlockDiscoverer.searchForBlocksAround(this.field_145850_b, this.field_174879_c, 16, BlockPredicates.isBlock((Block)BlocksAS.FOUNTAIN));
        fountains.removeIf(pos -> {
            final Vector3 fountainVec = new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5);
            final RaytraceAssist assist = new RaytraceAssist(thisVector, fountainVec);
            return !assist.isClear(this.field_145850_b);
        });
        Collections.shuffle(fountains, TileChalice.rand);
        for (final BlockPos wellPos : fountains) {
            final TileFountain fountain = MiscUtils.getTileAt((IBlockReader)this.field_145850_b, wellPos, TileFountain.class, true);
            if (fountain != null) {
                final FluidStack drained = fountain.getTank().drain(400, IFluidHandler.FluidAction.SIMULATE);
                if (drained.getAmount() <= 100) {
                    continue;
                }
                final int maxFillable = this.getTank().fill(drained, IFluidHandler.FluidAction.SIMULATE);
                if (maxFillable > 0) {
                    final FluidStack actual = fountain.getTank().drain(new FluidStack(drained, maxFillable), IFluidHandler.FluidAction.EXECUTE);
                    this.getTank().fill(actual, IFluidHandler.FluidAction.EXECUTE);
                    final Vector3 wellVec = new Vector3((Vector3i)wellPos).add(0.5, 0.5, 0.5);
                    final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.LIQUID_INTERACTION_LINE).addData(buf -> {
                        ByteBufUtils.writeVector(buf, wellVec);
                        ByteBufUtils.writeVector(buf, thisVector);
                        ByteBufUtils.writeFluidStack(buf, actual);
                        return;
                    });
                    PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.func_145831_w(), (Vector3i)wellVec.toBlockPos(), 32.0));
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    private boolean tickLightwellDraw() {
        if (this.func_145831_w().func_175640_z(this.field_174879_c)) {
            return false;
        }
        final FluidStack thisFluid = this.getTank().getFluid();
        if (!thisFluid.isEmpty() && (!(thisFluid.getFluid() instanceof FluidLiquidStarlight) || thisFluid.getAmount() + 100 >= 64000)) {
            return false;
        }
        final Vector3 thisVector = new Vector3(this).add(0.5, 1.5, 0.5);
        final List<BlockPos> wellPositions = BlockDiscoverer.searchForBlocksAround(this.field_145850_b, this.field_174879_c, 16, BlockPredicates.isBlock((Block)BlocksAS.WELL));
        wellPositions.removeIf(pos -> {
            final Vector3 wellVec2 = new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5);
            final RaytraceAssist assist = new RaytraceAssist(thisVector, wellVec2);
            return !assist.isClear(this.field_145850_b);
        });
        Collections.shuffle(wellPositions, TileChalice.rand);
        for (final BlockPos wellPos : wellPositions) {
            final TileWell well = MiscUtils.getTileAt((IBlockReader)this.field_145850_b, wellPos, TileWell.class, true);
            if (well != null) {
                final FluidStack drained = well.getTank().drain(400, IFluidHandler.FluidAction.SIMULATE);
                if (!(drained.getFluid() instanceof FluidLiquidStarlight) || drained.getAmount() <= 100) {
                    continue;
                }
                final int maxFillable = this.getTank().getMaxAddable(drained.getAmount());
                if (maxFillable > 0) {
                    final FluidStack actual = well.getTank().drain(new FluidStack(drained, maxFillable), IFluidHandler.FluidAction.EXECUTE);
                    this.getTank().fill(actual, IFluidHandler.FluidAction.EXECUTE);
                    final Vector3 wellVec = new Vector3((Vector3i)wellPos).add(0.5, 0.5, 0.5);
                    final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.LIQUID_INTERACTION_LINE).addData(buf -> {
                        ByteBufUtils.writeVector(buf, wellVec);
                        ByteBufUtils.writeVector(buf, thisVector);
                        ByteBufUtils.writeFluidStack(buf, actual);
                        return;
                    });
                    PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.func_145831_w(), (Vector3i)wellVec.toBlockPos(), 32.0));
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void drawLiquidLine(final PktPlayEffect pktPlayEffect) {
        final FriendlyByteBuf buf = pktPlayEffect.getExtraData();
        while (buf.isReadable()) {
            final Vector3 from = ByteBufUtils.readVector(pktPlayEffect.getExtraData());
            final Vector3 to = ByteBufUtils.readVector(pktPlayEffect.getExtraData());
            final FluidStack fluid = ByteBufUtils.readFluidStack(pktPlayEffect.getExtraData());
            final VFXColorFunction<?> colorFn = VFXColorFunction.constant(ColorizationHelper.getColor(fluid).orElse(Color.WHITE).brighter());
            playLineGenericParticles(from, to, 0.1f + TileChalice.rand.nextFloat() * 0.2f, colorFn);
            playLineGenericParticles(from, to, 0.1f + TileChalice.rand.nextFloat() * 0.2f, colorFn);
            playLineFluidParticles(from, to, 0.25f + TileChalice.rand.nextFloat() * 0.2f, fluid);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void playLineGenericParticles(final Vector3 from, final Vector3 to, final float width, final VFXColorFunction<?> colorFn) {
        playLineParticles(from, to, width, at -> EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).color(colorFn).alpha(VFXAlphaFunction.FADE_OUT).setMotion(Vector3.random().multiply(0.01f)).setScaleMultiplier(0.15f + TileChalice.rand.nextFloat() * 0.25f).setMaxAge(20 + TileChalice.rand.nextInt(35)));
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void playLineFluidParticles(final Vector3 from, final Vector3 to, final float width, final FluidStack fluid) {
        final Color c = new Color(fluid.getFluid().getAttributes().getColor(fluid));
        playLineParticles(from, to, width, at -> EffectHelper.of(EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS).spawn(at).setTextureAtlasSprite(RenderingUtils.getParticleTexture(fluid)).tumble().color(VFXColorFunction.constant(c)).setMotion(Vector3.random().multiply(0.01f)).setScaleMultiplier(0.2f + TileChalice.rand.nextFloat() * 0.05f).scale(VFXScaleFunction.SHRINK).setMaxAge(10 + TileChalice.rand.nextInt(15)));
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void playLineParticles(final Vector3 from, final Vector3 to, final float width, final Function<Vector3, EntityVisualFX> pCreator) {
        to.clone().subtract(from).stepAlongVector(width, v -> {
            final EntityVisualFX entityVisualFX = pCreator.apply(from.clone().add(v));
        });
    }
    
    @Nonnull
    public SimpleSingleFluidTank getTank() {
        return this.tank;
    }
    
    @Nonnull
    public IFluidHandler getTankAccess() {
        return (IFluidHandler)this.access.getCapability(Direction.DOWN).orElse((Object)null);
    }
    
    @Nonnull
    public Vector3 getRotation() {
        return this.rotation;
    }
    
    @Nonnull
    public Vector3 getPrevRotation() {
        return this.prevRotation;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.tank.readNBT(compound.func_74775_l("tank"));
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.put("tank", (Tag)this.tank.writeNBT());
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (this.access.hasCapability(cap, side)) {
            return (LazyOptional<T>)this.access.getCapability(side).cast();
        }
        return (LazyOptional<T>)super.getCapability((Capability)cap, side);
    }
}
