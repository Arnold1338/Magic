package hellfirepvp.astralsorcery.common.tile;

import net.minecraft.nbt.Tag;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.tile.network.StarlightTransmissionLens;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.astralsorcery.common.block.tile.BlockLens;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import java.util.LinkedList;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import java.util.List;
import hellfirepvp.astralsorcery.common.item.lens.LensColorType;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeTile;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.tile.base.network.TileTransmissionBase;

public class TileLens extends TileTransmissionBase<IPrismTransmissionNode> implements CrystalAttributeTile
{
    private CrystalAttributes attributes;
    private LensColorType colorType;
    private float accumulatedStarlight;
    private List<BlockPos> occupiedConnections;
    
    protected TileLens(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.attributes = null;
        this.colorType = null;
        this.accumulatedStarlight = 0.0f;
        this.occupiedConnections = new LinkedList<BlockPos>();
    }
    
    public TileLens() {
        super(TileEntityTypesAS.LENS);
        this.attributes = null;
        this.colorType = null;
        this.accumulatedStarlight = 0.0f;
        this.occupiedConnections = new LinkedList<BlockPos>();
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (this.colorType != null) {
            if (this.field_145850_b.func_201670_d()) {
                this.playColorEffects();
            }
            this.doColorEffects();
        }
    }
    
    @Override
    protected void onFirstTick() {
        super.onFirstTick();
        this.markForUpdate();
    }
    
    public void transmissionTick(final float starlightAmt, final IWeakConstellation type) {
        this.accumulatedStarlight += starlightAmt;
        final boolean networkSync = this.needsNetworkSync();
        this.markForUpdate();
        if (!networkSync) {
            this.preventNetworkSync();
        }
    }
    
    private void doColorEffects() {
        final World world = this.func_145831_w();
        if (!world.func_201670_d() && !this.occupiedConnections.isEmpty()) {
            this.occupiedConnections.clear();
            this.markForUpdate();
            this.preventNetworkSync();
        }
        if (this.accumulatedStarlight <= 0.0f) {
            return;
        }
        final float effectMultiplier = this.accumulatedStarlight * 1.4f;
        this.accumulatedStarlight = 0.0f;
        final List<BlockPos> linked = this.getLinkedPositions();
        if (linked.isEmpty()) {
            return;
        }
        final Vector3 thisVec = new Vector3(this).add(0.5, 0.5, 0.5);
        for (final BlockPos linkedTo : linked) {
            final PartialEffectExecutor exec = new PartialEffectExecutor(1.0f / linked.size() * effectMultiplier, TileLens.rand);
            final Vector3 to = new Vector3((Vector3i)linkedTo).add(0.5, 0.5, 0.5);
            final RaytraceAssist rta = new RaytraceAssist(thisVec, to).includeEndPoint();
            if (this.colorType.getType().doBlockInteraction()) {
                if (!rta.isClear(world) && rta.positionHit() != null) {
                    final BlockPos posHit = rta.positionHit();
                    final BlockState stateHit = world.getBlockState(posHit);
                    this.colorType.blockInBeam(world, posHit, stateHit, exec);
                    if (!world.func_201670_d()) {
                        this.occupiedConnections.add(posHit);
                    }
                }
                else if (!world.func_201670_d()) {
                    this.occupiedConnections.add(linkedTo);
                }
            }
            if (this.colorType.getType().doEntityInteraction()) {
                exec.reset();
                rta.setCollectEntities(0.5);
                rta.isClear(world);
                final List<Entity> found = rta.collectedEntities(world);
                found.forEach(e -> this.colorType.entityInBeam(world, thisVec, to, e, exec));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playColorEffects() {
        final Vector3 at = new Vector3(this).add(0.5, 0.5, 0.5);
        final Color lensColor = this.colorType.getColor();
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(this).add(0.2, 0.2, 0.2).add(TileLens.rand.nextFloat() * 0.6, TileLens.rand.nextFloat() * 0.6, TileLens.rand.nextFloat() * 0.6)).color(VFXColorFunction.constant(lensColor)).setScaleMultiplier(0.1f + TileLens.rand.nextFloat() * 0.15f);
        if (this.getTicksExisted() % 40 == 0) {
            for (final BlockPos connected : this.occupiedConnections) {
                final Vector3 to = new Vector3((Vector3i)connected).add(0.5, 0.5, 0.5);
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(at).setup(to, 0.6, 0.6).color(VFXColorFunction.constant(lensColor));
            }
        }
    }
    
    public LensColorType setColorType(@Nullable final LensColorType colorType) {
        if (this.getColorType() == colorType) {
            return colorType;
        }
        final LensColorType prev = this.getColorType();
        this.colorType = colorType;
        this.markForUpdate();
        return prev;
    }
    
    @Nullable
    public LensColorType getColorType() {
        return this.colorType;
    }
    
    public Direction getPlacedAgainst() {
        final BlockState state = this.field_145850_b.getBlockState(this.func_174877_v());
        if (!(state.getBlock() instanceof BlockLens)) {
            return Direction.DOWN;
        }
        return (Direction)state.getValue((Property)BlockLens.PLACED_AGAINST);
    }
    
    @Override
    public boolean isSingleLink() {
        return true;
    }
    
    @Nullable
    @Override
    public CrystalAttributes getAttributes() {
        return this.attributes;
    }
    
    @Override
    public void setAttributes(@Nullable final CrystalAttributes attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.attributes = CrystalAttributes.getCrystalAttributes(compound);
        if (compound.contains("colorType")) {
            this.colorType = LensColorType.byName(new ResourceLocation(compound.getString("colorType")));
        }
        else {
            this.colorType = null;
        }
        this.occupiedConnections = NBTHelper.readList(compound, "occupiedConnections", 10, nbt -> NBTHelper.readBlockPosFromNBT((CompoundTag)nbt));
    }
    
    @Override
    public void readNetNBT(final CompoundTag compound) {
        super.readNetNBT(compound);
        this.accumulatedStarlight = compound.getFloat("accumulatedStarlight");
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        if (this.attributes != null) {
            this.attributes.store(compound);
        }
        if (this.colorType != null) {
            compound.putString("colorType", this.colorType.getName().toString());
        }
        NBTHelper.writeList(compound, "occupiedConnections", (Collection<BlockPos>)this.occupiedConnections, pos -> NBTHelper.writeBlockPosToNBT(pos, new CompoundTag()));
    }
    
    @Override
    public void writeNetNBT(final CompoundTag compound) {
        super.writeNetNBT(compound);
        compound.func_74776_a("accumulatedStarlight", this.accumulatedStarlight);
    }
    
    @Nonnull
    @Override
    public IPrismTransmissionNode provideTransmissionNode(final BlockPos at) {
        return new StarlightTransmissionLens(at, this.attributes);
    }
}
