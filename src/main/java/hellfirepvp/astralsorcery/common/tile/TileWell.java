package hellfirepvp.astralsorcery.common.tile;

import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nullable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.world.SkyCollectionHelper;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.fluid.BlockLiquidStarlight;
import hellfirepvp.astralsorcery.common.fluid.FluidLiquidStarlight;
import java.awt.Color;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.level.material.Fluid;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefactionContext;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.util.tile.TileInventoryFiltered;
import hellfirepvp.astralsorcery.common.util.tile.PrecisionSingleFluidTank;
import hellfirepvp.astralsorcery.common.util.tile.FluidTankAccess;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverWell;
import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;

public class TileWell extends TileReceiverBase<StarlightReceiverWell>
{
    private static final int TANK_SIZE = 2000;
    private WellLiquefaction runningRecipe;
    private FluidTankAccess access;
    private PrecisionSingleFluidTank tank;
    private TileInventoryFiltered inventory;
    private double starlightBuffer;
    private float posDistribution;
    
    public TileWell() {
        super(TileEntityTypesAS.WELL);
        this.runningRecipe = null;
        this.starlightBuffer = 0.0;
        this.posDistribution = -1.0f;
        (this.tank = new PrecisionSingleFluidTank(2000)).setAllowInput(false);
        this.tank.addUpdateFunction(this::markForUpdate);
        (this.access = new FluidTankAccess()).putTank(0, (IFluidTank)this.tank, Direction.DOWN);
        (this.inventory = new TileInventoryFiltered(this, () -> 1, new Direction[] { Direction.DOWN })).filterMaxStackSize((slot, stack) -> 1);
        this.inventory.canExtract((slot, amount, existing) -> false);
        this.inventory.canInsert((slot, toAdd, existing) -> {
            if (toAdd.isEmpty()) {
                return true;
            }
            else {
                final boolean b;
                if (existing.isEmpty()) {
                    if (RecipeTypesAS.TYPE_WELL.findRecipe(new WellLiquefactionContext(toAdd)) != null) {
                        return b;
                    }
                }
                return b;
            }
        });
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.func_145831_w().func_201670_d()) {
            if (this.doesSeeSky()) {
                this.collectStarlight();
            }
            final ItemStack stack = this.getInventory().getStackInSlot(0);
            if (!stack.isEmpty()) {
                if (!this.func_145831_w().isEmptyBlock(this.func_174877_v().above())) {
                    this.breakCatalyst();
                }
                else {
                    if (this.runningRecipe == null) {
                        this.runningRecipe = RecipeTypesAS.TYPE_WELL.findRecipe(new WellLiquefactionContext(this));
                    }
                    if (this.runningRecipe != null) {
                        int statMultiplier = 1;
                        if (stack.getItem() instanceof CrystalAttributeItem) {
                            final CrystalAttributes attributes = ((CrystalAttributeItem)stack.getItem()).getAttributes(stack);
                            if (attributes != null) {
                                statMultiplier = attributes.getTotalTierLevel();
                            }
                        }
                        final double gain = Math.sqrt(this.starlightBuffer) * (statMultiplier * this.runningRecipe.getProductionMultiplier());
                        if (gain > 0.0 && this.tank.getFluidAmount() <= 2000) {
                            this.fillAndDiscardRest(this.runningRecipe, gain);
                            if (TileWell.rand.nextInt(750) == 0) {
                                EntityFlare.spawnAmbientFlare(this.func_145831_w(), this.func_174877_v().offset(-3 + TileWell.rand.nextInt(7), 1, -3 + TileWell.rand.nextInt(7)));
                            }
                        }
                        this.starlightBuffer = 0.0;
                        if (TileWell.rand.nextInt(1 + (int)(1000.0f * (statMultiplier * this.runningRecipe.getShatterMultiplier()))) == 0) {
                            this.breakCatalyst();
                            EntityFlare.spawnAmbientFlare(this.func_145831_w(), this.func_174877_v().offset(-3 + TileWell.rand.nextInt(7), 1, -3 + TileWell.rand.nextInt(7)));
                        }
                    }
                    else {
                        this.breakCatalyst();
                    }
                }
            }
            this.starlightBuffer = 0.0;
        }
        else {
            this.doClientEffects();
        }
    }
    
    private void fillAndDiscardRest(final WellLiquefaction recipe, final double gain) {
        final Fluid produced = recipe.getFluidOutput();
        if (produced == null) {
            return;
        }
        if (this.tank.getFluidAmount() < 10) {
            this.tank.setFluid(produced);
        }
        if (this.tank.getFluid().isEmpty()) {
            this.tank.setFluid(produced);
        }
        else if (!produced.equals(this.tank.getFluid().getFluid())) {
            return;
        }
        this.tank.addAmount(gain);
    }
    
    public void breakCatalyst() {
        this.inventory.setStackInSlot(0, ItemStack.EMPTY);
        this.runningRecipe = null;
        final PktPlayEffect effect = new PktPlayEffect(PktPlayEffect.Type.SMALL_CRYSTAL_BREAK).addData(buf -> ByteBufUtils.writeVector(buf, new Vector3(this).add(0.5, 1.3, 0.5)));
        PacketChannel.CHANNEL.sendToAllAround(effect, PacketChannel.pointFromPos(this.func_145831_w(), (Vector3i)this.func_174877_v(), 32.0));
        SoundHelper.playSoundAround(SoundEvents.field_187561_bM, this.func_145831_w(), (Vector3i)this.func_174877_v(), 1.0f, 1.0f);
        this.markForUpdate();
    }
    
    @Nonnull
    public ItemStack getCatalyst() {
        return this.getInventory().getStackInSlot(0);
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doClientEffects() {
        final ItemStack stack = this.inventory.getStackInSlot(0);
        if (!stack.isEmpty()) {
            this.runningRecipe = RecipeTypesAS.TYPE_WELL.findRecipe(new WellLiquefactionContext(this));
            if (this.runningRecipe != null) {
                Color color = Color.WHITE;
                if (this.runningRecipe.getCatalystColor() != null) {
                    color = this.runningRecipe.getCatalystColor();
                }
                this.doCatalystEffect(color);
            }
        }
        if (this.tank.getFluidAmount() > 0 && this.tank.getFluid().getFluid() instanceof FluidLiquidStarlight) {
            BlockLiquidStarlight.playLiquidStarlightBlockEffect(TileWell.rand, new Vector3(this).add(0.0, 0.4 + this.tank.getPercentageFilled() * 0.5, 0.0), 0.7f);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doCatalystEffect(final Color color) {
        if (TileWell.rand.nextInt(6) == 0) {
            final Vector3 at = new Vector3(this).add(0.5, 1.0, 0.5).add(TileWell.rand.nextFloat() * 0.15 * (TileWell.rand.nextBoolean() ? 1 : -1), TileWell.rand.nextFloat() * 0.2, TileWell.rand.nextFloat() * 0.15 * (TileWell.rand.nextBoolean() ? 1 : -1));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(color)).setMaxAge(25 + TileWell.rand.nextInt(20));
        }
    }
    
    private void collectStarlight() {
        double sbDayDistribution = DayTimeHelper.getCurrentDaytimeDistribution(this.field_145850_b);
        sbDayDistribution = 0.3 + 0.7 * sbDayDistribution;
        final int yLevel = this.func_174877_v().getY();
        float dstr;
        if (yLevel > 120) {
            dstr = 1.0f;
        }
        else {
            dstr = yLevel / 120.0f;
        }
        if (this.posDistribution == -1.0f) {
            if (this.field_145850_b instanceof ISeedReader) {
                this.posDistribution = SkyCollectionHelper.getSkyNoiseDistribution((ISeedReader)this.field_145850_b, this.func_174877_v());
            }
            else {
                this.posDistribution = 0.3f;
            }
        }
        sbDayDistribution *= dstr;
        sbDayDistribution *= 1.0 + 1.2 * this.posDistribution;
        this.starlightBuffer += Math.max(1.0E-4, sbDayDistribution);
    }
    
    public void receiveStarlight(final double amount) {
        this.starlightBuffer += amount;
        this.markForUpdate();
    }
    
    @Nonnull
    public PrecisionSingleFluidTank getTank() {
        return this.tank;
    }
    
    @Nonnull
    public TileInventoryFiltered getInventory() {
        return this.inventory;
    }
    
    @Nonnull
    @Override
    public StarlightReceiverWell provideEndpoint(final BlockPos at) {
        return new StarlightReceiverWell(at);
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.tank.readNBT(compound.func_74775_l("tank"));
        this.inventory = this.inventory.deserialize(compound.func_74775_l("inventory"));
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.put("tank", (Tag)this.tank.writeNBT());
        compound.put("inventory", (Tag)this.inventory.serialize());
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (this.access.hasCapability(cap, side)) {
            return (LazyOptional<T>)this.access.getCapability(side).cast();
        }
        if (this.inventory.hasCapability(cap, side)) {
            return (LazyOptional<T>)this.inventory.getCapability().cast();
        }
        return (LazyOptional<T>)super.getCapability((Capability)cap, side);
    }
}
