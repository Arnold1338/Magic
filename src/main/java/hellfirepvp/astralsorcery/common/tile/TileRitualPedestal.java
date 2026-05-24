package hellfirepvp.astralsorcery.common.tile;

import com.google.common.collect.ImmutableSet;
import java.util.function.Consumer;
import java.util.HashSet;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.util.MapStream;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import java.util.Collection;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXSpritePlane;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import java.awt.Color;
import javax.annotation.Nonnull;
import com.google.common.collect.Sets;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import net.minecraft.world.level.BlockGetter;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedCrystalBase;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import net.minecraft.core.Direction;
import java.util.HashMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import hellfirepvp.astralsorcery.common.util.EffectIncrementer;
import java.util.UUID;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;
import hellfirepvp.astralsorcery.common.util.tile.TileInventoryFiltered;
import java.util.Set;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.tile.base.TileAreaOfInfluence;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeTile;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverRitualPedestal;
import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;

public class TileRitualPedestal extends TileReceiverBase<StarlightReceiverRitualPedestal> implements CrystalAttributeTile, TileAreaOfInfluence
{
    public static final BlockPos RITUAL_ANCHOR_OFFEST;
    public static final Set<BlockPos> RITUAL_CIRCLE_OFFSETS;
    public static final int MAX_MIRROR_COUNT = 5;
    private TileInventoryFiltered inventory;
    private final Map<BlockPos, BlockState> offsetConfigurations;
    private UUID ownerUUID;
    private BlockPos ritualLinkTo;
    private boolean working;
    private Map<BlockPos, Boolean> offsetMirrors;
    private EffectIncrementer effectWork;
    private ConstellationEffect clientEffectInstance;
    private Object ritualHaloEffect;
    
    public TileRitualPedestal() {
        super(TileEntityTypesAS.RITUAL_PEDESTAL);
        this.offsetConfigurations = new HashMap<BlockPos, BlockState>();
        this.ownerUUID = null;
        this.ritualLinkTo = null;
        this.working = false;
        this.offsetMirrors = new HashMap<BlockPos, Boolean>();
        this.effectWork = new EffectIncrementer(64);
        this.clientEffectInstance = null;
        this.ritualHaloEffect = null;
        (this.inventory = new TileInventoryFiltered(this, () -> 1, new Direction[] { Direction.DOWN })).canExtract((slot, amount, existing) -> !existing.isEmpty());
        this.inventory.canInsert((slot, toAdd, existing) -> existing.isEmpty() && toAdd.getItem() instanceof ItemAttunedCrystalBase && ((ItemAttunedCrystalBase)toAdd.getItem()).getFocusConstellation(toAdd) != null);
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.func_145831_w().func_201670_d()) {
            this.doesSeeSky();
            this.hasMultiblock();
            this.updateLinkTile();
            this.updateBlockConfigurations();
        }
        this.effectWork.update(this.working);
        if (this.func_145831_w().func_201670_d() && this.working) {
            this.playEffects();
        }
    }
    
    private void updateBlockConfigurations() {
        if (this.ticksExisted % 20 == 0) {
            for (final BlockPos offset : TileRitualPedestal.RITUAL_CIRCLE_OFFSETS) {
                final BlockPos pos = this.func_174877_v().func_177971_a((Vector3i)offset);
                MiscUtils.executeWithChunk((IWorldReader)this.func_145831_w(), pos, pos, at -> {
                    final BlockState savedState = this.offsetConfigurations.get(offset);
                    if (this.func_145831_w().isEmptyBlock(at)) {
                        if (savedState != null) {
                            this.offsetConfigurations.remove(offset);
                            this.markForUpdate();
                        }
                    }
                    else {
                        final BlockState actualState = this.func_145831_w().getBlockState(at);
                        if (savedState == null || !savedState.equals(actualState)) {
                            this.offsetConfigurations.put(offset, actualState);
                            this.markForUpdate();
                        }
                    }
                });
            }
        }
    }
    
    private void updateLinkTile() {
        final boolean hasLink = this.ritualLinkTo != null;
        final BlockPos link = this.func_174877_v().func_177971_a((Vector3i)TileRitualPedestal.RITUAL_ANCHOR_OFFEST);
        final TileRitualLink linkTile = MiscUtils.getTileAt((IBlockReader)this.field_145850_b, link, TileRitualLink.class, true);
        boolean hasLinkNow;
        if (linkTile != null) {
            this.ritualLinkTo = linkTile.getLinkedTo();
            hasLinkNow = (this.ritualLinkTo != null);
        }
        else {
            this.ritualLinkTo = null;
            hasLinkNow = false;
        }
        if (hasLink != hasLinkNow) {
            this.markForUpdate();
        }
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        return StructureTypesAS.PTYPE_RITUAL_PEDESTAL;
    }
    
    @Nonnull
    public Set<BlockState> getConfiguredBlockStates() {
        return Sets.newHashSet((Iterable)this.offsetConfigurations.values());
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public Color getEffectColor() {
        if (!this.providesEffect()) {
            return null;
        }
        final IWeakConstellation running = this.getRitualConstellation();
        if (running == null) {
            return null;
        }
        return running.getConstellationColor();
    }
    
    @Override
    public float getRadius() {
        if (!this.providesEffect()) {
            return 0.0f;
        }
        final IWeakConstellation running = this.getRitualConstellation();
        if (running == null) {
            return 0.0f;
        }
        final ConstellationEffect effect = ConstellationEffectRegistry.createInstance(this, running);
        if (effect == null) {
            return 0.0f;
        }
        final ConstellationEffectProperties properties = effect.createProperties(this.getMirrorCount());
        if (properties != null) {
            if (this.getRitualTrait() != null) {
                this.getRitualTrait().affectConstellationEffect(properties);
            }
            if (!this.getCurrentCrystal().isEmpty()) {
                final CrystalAttributes attributes = CrystalAttributes.getCrystalAttributes(this.getCurrentCrystal());
                if (attributes != null) {
                    properties.multiplySize(CrystalCalculations.getRitualEffectRangeFactor(this, attributes));
                }
            }
            return (float)properties.getSize() * 1.3f;
        }
        return 0.0f;
    }
    
    @Nonnull
    @Override
    public BlockPos getEffectOriginPosition() {
        return this.func_174877_v();
    }
    
    @Nonnull
    @Override
    public Vector3 getEffectPosition() {
        return new Vector3((Vector3i)this.getEffectOriginPosition()).add(0.5f, 0.5f, 0.5f);
    }
    
    @Nonnull
    @Override
    public RegistryKey<World> getDimension() {
        return (RegistryKey<World>)this.func_145831_w().dimension();
    }
    
    @Override
    public boolean providesEffect() {
        return this.isWorking() && !this.func_145837_r();
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        float alphaDaytime = DayTimeHelper.getCurrentDaytimeDistribution(this.func_145831_w());
        alphaDaytime *= 0.8f;
        final float percRunning = this.effectWork.getAsPercentage();
        final int chance = 15 + (int)((1.0f - percRunning) * 50.0f);
        if (TileRitualPedestal.rand.nextInt(chance) == 0) {
            final Vector3 from = new Vector3(this).add(0.5, 0.05, 0.5);
            MiscUtils.applyRandomOffset(from, TileRitualPedestal.rand, 0.05f);
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).setOwner(this.ownerUUID).spawn(from).setup(from.clone().addY(6.0), 1.5, 1.5).setAlphaMultiplier(0.5f + 0.5f * alphaDaytime).setMaxAge(64);
        }
        if (this.ritualLinkTo != null && TileRitualPedestal.rand.nextBoolean()) {
            final Vector3 at = new Vector3(this).add(0.0, 0.1, 0.0);
            at.add(TileRitualPedestal.rand.nextFloat() * 0.5 + 0.25, 0.0, TileRitualPedestal.rand.nextFloat() * 0.5 + 0.25);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).setOwner(this.ownerUUID).spawn(at).setAlphaMultiplier(0.7f).color(VFXColorFunction.WHITE).setMotion(Vector3.positiveYRandom(TileRitualPedestal.rand).addY(2.0).normalize().multiply(0.4f)).setScaleMultiplier(0.2f + TileRitualPedestal.rand.nextFloat() * 0.15f).motion(VFXMotionController.target(() -> new Vector3(this).add((Vector3i)TileRitualPedestal.RITUAL_ANCHOR_OFFEST).add(0.5, 0.5, 0.5), 0.1f)).setMaxAge(30 + TileRitualPedestal.rand.nextInt(50));
        }
        final List<BlockPos> activeMirrors = this.offsetMirrors.entrySet().stream().filter(Map.Entry::getValue).map((Function<? super Object, ?>)Map.Entry::getKey).collect((Collector<? super Object, ?, List<BlockPos>>)Collectors.toList());
        final IWeakConstellation ritualConstellation = this.getRitualConstellation();
        if (this.working && ritualConstellation != null) {
            if (!activeMirrors.isEmpty() && DayTimeHelper.isNight(this.func_145831_w()) && TileRitualPedestal.rand.nextInt(chance * 2) == 0) {
                final Vector3 from2 = new Vector3(this).add(0.5, 0.1, 0.5);
                MiscUtils.applyRandomOffset(from2, TileRitualPedestal.rand, 2.0f);
                from2.setY(this.func_174877_v().getY() - 0.6 + 1.0f * TileRitualPedestal.rand.nextFloat() * (TileRitualPedestal.rand.nextBoolean() ? 1 : -1));
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).setOwner(this.ownerUUID).spawn(from2).setup(from2.clone().addY(5 + TileRitualPedestal.rand.nextInt(3)), 1.2999999523162842, 1.2999999523162842).setAlphaMultiplier(alphaDaytime).color(VFXColorFunction.constant(ritualConstellation.getConstellationColor())).setMaxAge(64);
            }
            if (this.ritualHaloEffect == null) {
                this.ritualHaloEffect = EffectHelper.of(EffectTemplatesAS.TEXTURE_SPRITE).spawn(new Vector3(this).add(0.5, 0.05, 0.5)).setSprite(SpritesAS.SPR_HALO_RITUAL).setAxis(Vector3.RotAxis.Y_AXIS).setNoRotation(25.0f).setScaleMultiplier(6.5f).refresh(RefreshFunction.tileExistsAnd(this, (tile, effect) -> tile.isWorking() && !tile.getCurrentCrystal().isEmpty()));
            }
            if (this.ritualHaloEffect != null) {
                final FXSpritePlane effectPlane = (FXSpritePlane)this.ritualHaloEffect;
                EffectHelper.refresh(effectPlane, EffectTemplatesAS.TEXTURE_SPRITE);
                final float dayTimeMul = DayTimeHelper.getCurrentDaytimeDistribution(this.func_145831_w());
                effectPlane.setAlphaMultiplier(Math.max(0.05f, dayTimeMul * 0.75f));
            }
            final Vector3 offset = Vector3.random().setY(0).normalize().multiply(TileRitualPedestal.rand.nextFloat() * 4.0f * (TileRitualPedestal.rand.nextBoolean() ? 1 : -1));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).setOwner(this.ownerUUID).spawn(new Vector3(this).add(0.5, 0.02, 0.5).add(offset)).setAlphaMultiplier(1.0f).setGravityStrength(-0.001f).color(VFXColorFunction.constant(ritualConstellation.getConstellationColor())).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.3f + TileRitualPedestal.rand.nextFloat() * 0.15f).setMaxAge(25 + TileRitualPedestal.rand.nextInt(15));
            if (this.clientEffectInstance != null && !this.clientEffectInstance.getConstellation().equals(ritualConstellation)) {
                this.clientEffectInstance = null;
            }
            if (this.clientEffectInstance == null) {
                this.clientEffectInstance = ConstellationEffectRegistry.createInstance(ILocatable.fromPos(this.func_174877_v()), ritualConstellation);
            }
            if (this.clientEffectInstance != null) {
                this.clientEffectInstance.playClientEffect(this.func_145831_w(), this.func_174877_v(), this, percRunning, this.isFullyEnhanced());
                if (this.ritualLinkTo != null && this.func_145831_w().func_195588_v(this.ritualLinkTo)) {
                    this.clientEffectInstance.playClientEffect(this.func_145831_w(), this.ritualLinkTo, this, percRunning, this.isFullyEnhanced());
                }
            }
            final CrystalAttributes prop = this.getAttributes();
            if (prop != null && TileRitualPedestal.rand.nextInt(3) == 0) {
                for (int i = 0; i < 3; ++i) {
                    final Vector3 at2 = new Vector3(this).add(0.5, 1.35, 0.5).add(Vector3.random().multiply(0.6f));
                    final Vector3 motion = Vector3.random().multiply(0.02f);
                    EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).setOwner(this.ownerUUID).spawn(at2).setMotion(motion).setAlphaMultiplier(1.0f).color(VFXColorFunction.constant(ritualConstellation.getConstellationColor())).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.15f + TileRitualPedestal.rand.nextFloat() * 0.05f).setMaxAge(16 + TileRitualPedestal.rand.nextInt(15));
                }
                if (TileRitualPedestal.rand.nextInt(3) == 0) {
                    final Vector3 from3 = new Vector3(this).add(0.5, 1.2, 0.5);
                    Vector3 to;
                    if (activeMirrors.isEmpty()) {
                        to = new Vector3(this).add(0.5, 3.5 + TileRitualPedestal.rand.nextFloat() * 2.5, 0.5);
                    }
                    else {
                        final BlockPos mirror = MiscUtils.getRandomEntry(activeMirrors, TileRitualPedestal.rand).func_177971_a((Vector3i)this.func_174877_v());
                        to = new Vector3((Vector3i)mirror).add(0.5, 0.5, 0.5);
                    }
                    EffectHelper.of(EffectTemplatesAS.LIGHTNING).setOwner(this.ownerUUID).spawn(from3).makeDefault(to).color(VFXColorFunction.constant(ritualConstellation.getConstellationColor()));
                }
            }
        }
        for (final BlockPos mirror2 : this.offsetMirrors.keySet()) {
            if (this.ticksExisted % 32 == 0) {
                Vector3 source = new Vector3(this).add(0.5, 0.9, 0.5);
                final Vector3 to = new Vector3(this).add((Vector3i)mirror2).add(0.5, 0.5, 0.5);
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).setOwner(this.ownerUUID).spawn(source).setup(to, 0.800000011920929, 0.800000011920929);
                if (this.ritualLinkTo == null || !this.offsetMirrors.get(mirror2)) {
                    continue;
                }
                source = new Vector3(this).add((Vector3i)TileRitualPedestal.RITUAL_ANCHOR_OFFEST).add(0.5, 0.5, 0.5);
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).setOwner(this.ownerUUID).spawn(source).setup(to, 0.800000011920929, 0.800000011920929).color(VFXColorFunction.random());
            }
        }
    }
    
    public boolean isWorking() {
        return this.working;
    }
    
    public Map<BlockPos, Boolean> getMirrors() {
        return MapStream.of(this.offsetMirrors).mapKey(pos -> pos.func_177971_a((Vector3i)this.func_174877_v())).toMap();
    }
    
    public int getMirrorCount() {
        return (int)this.offsetMirrors.values().stream().filter(b -> b).count();
    }
    
    public boolean isFullyEnhanced() {
        return this.working && this.offsetMirrors.size() == 5;
    }
    
    @Nullable
    public Player getOwner() {
        if (this.ownerUUID == null || this.field_145850_b == null) {
            return null;
        }
        return this.field_145850_b.getPlayerByUUID(this.ownerUUID);
    }
    
    @Nonnull
    public ItemStack getCurrentCrystal() {
        final ItemStack crystal = this.inventory.getStackInSlot(0);
        return ItemUtils.copyStackWithSize(crystal, crystal.func_190916_E());
    }
    
    @Nullable
    public BlockPos getRitualLinkTo() {
        return this.ritualLinkTo;
    }
    
    @Nonnull
    public EffectIncrementer getWorkEffectTimer() {
        return this.effectWork;
    }
    
    @Nullable
    public IWeakConstellation getRitualConstellation() {
        final ItemStack crystal = this.inventory.getStackInSlot(0);
        if (!crystal.isEmpty() && crystal.getItem() instanceof ConstellationItem) {
            return ((ConstellationItem)crystal.getItem()).getAttunedConstellation(crystal);
        }
        return null;
    }
    
    @Nullable
    public IMinorConstellation getRitualTrait() {
        final ItemStack crystal = this.inventory.getStackInSlot(0);
        if (!crystal.isEmpty() && crystal.getItem() instanceof ConstellationItem) {
            return ((ConstellationItem)crystal.getItem()).getTraitConstellation(crystal);
        }
        return null;
    }
    
    @Nullable
    @Override
    public CrystalAttributes getAttributes() {
        final ItemStack crystal = this.inventory.getStackInSlot(0);
        if (!crystal.isEmpty() && crystal.getItem() instanceof CrystalAttributeItem) {
            return ((CrystalAttributeItem)crystal.getItem()).getAttributes(crystal);
        }
        return null;
    }
    
    @Override
    public void setAttributes(@Nullable final CrystalAttributes attributes) {
        final ItemStack crystal = this.inventory.getStackInSlot(0);
        if (!crystal.isEmpty() && crystal.getItem() instanceof CrystalAttributeItem) {
            ((CrystalAttributeItem)crystal.getItem()).setAttributes(crystal, attributes);
        }
    }
    
    public void setOwner(@Nullable final UUID playerUUID) {
        this.ownerUUID = playerUUID;
        this.markForUpdate();
    }
    
    @Nonnull
    public ItemStack tryPlaceCrystalInPedestal(@Nonnull final ItemStack crystal) {
        final ItemStack currentCatalyst = this.inventory.getStackInSlot(0);
        final ItemStack toInsert = ItemUtils.copyStackWithSize(crystal, Math.min(crystal.func_190916_E(), 1));
        if (toInsert.isEmpty()) {
            if (!this.inventory.canExtractItem(0, 1)) {
                return ItemStack.field_190927_a;
            }
            if (currentCatalyst.isEmpty()) {
                return ItemStack.field_190927_a;
            }
            this.inventory.setStackInSlot(0, ItemStack.field_190927_a);
            return currentCatalyst;
        }
        else {
            if (!this.inventory.canInsertItem(0, crystal)) {
                return crystal;
            }
            if (currentCatalyst.isEmpty()) {
                this.inventory.setStackInSlot(0, toInsert);
                return ItemUtils.copyStackWithSize(crystal, Math.max(0, crystal.func_190916_E() - 1));
            }
            return crystal;
        }
    }
    
    public void setReceiverData(final boolean working, final Map<BlockPos, Boolean> mirrorData, @Nullable final CrystalAttributes newAttributes) {
        this.working = working;
        this.offsetMirrors = new HashMap<BlockPos, Boolean>(mirrorData);
        final ItemStack crystal = this.getCurrentCrystal();
        if (!crystal.isEmpty() && crystal.getItem() instanceof CrystalAttributeItem) {
            if (newAttributes == null) {
                this.tryPlaceCrystalInPedestal(ItemStack.field_190927_a);
            }
            else {
                this.setAttributes(newAttributes.copy());
            }
        }
        this.markForUpdate();
        this.preventNetworkSync();
    }
    
    @Nonnull
    @Override
    public StarlightReceiverRitualPedestal provideEndpoint(final BlockPos at) {
        return new StarlightReceiverRitualPedestal(at);
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.inventory = this.inventory.deserialize(compound.func_74775_l("inventory"));
        this.ownerUUID = NBTHelper.getUUID(compound, "ownerUUID", null);
        this.ritualLinkTo = NBTHelper.readFromSubTag(compound, "ritualLinkTo", NBTHelper::readBlockPosFromNBT);
        this.working = compound.getBoolean("working");
        this.offsetMirrors.clear();
        final ListTag tagList = compound.getList("mirrors", 10);
        for (final Tag nbt : tagList) {
            final CompoundTag tag = (CompoundTag)nbt;
            this.offsetMirrors.put(NBTHelper.readBlockPosFromNBT(tag), tag.getBoolean("connect"));
        }
        this.offsetConfigurations.clear();
        final ListTag tagBlocks = compound.getList("blockConfiguration", 10);
        for (final Tag nbt2 : tagBlocks) {
            final CompoundTag tag2 = (CompoundTag)nbt2;
            this.offsetConfigurations.put(NBTHelper.readBlockPosFromNBT(tag2), NBTHelper.getBlockState(tag2, "state"));
        }
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.put("inventory", (Tag)this.inventory.serialize());
        if (this.ownerUUID != null) {
            compound.putUUID("ownerUUID", this.ownerUUID);
        }
        if (this.ritualLinkTo != null) {
            final CompoundTag cmp;
            NBTHelper.setAsSubTag(compound, "ritualLinkTo", cmp -> NBTHelper.writeBlockPosToNBT(this.ritualLinkTo, cmp));
        }
        compound.putBoolean("working", this.working);
        final ListTag listPositions = new ListTag();
        for (final Map.Entry<BlockPos, Boolean> posEntry : this.offsetMirrors.entrySet()) {
            final CompoundTag cmp = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(posEntry.getKey(), cmp);
            cmp.putBoolean("connect", (boolean)posEntry.getValue());
            listPositions.add((Object)cmp);
        }
        compound.put("mirrors", (Tag)listPositions);
        final ListTag listConfigurations = new ListTag();
        for (final Map.Entry<BlockPos, BlockState> posEntry2 : this.offsetConfigurations.entrySet()) {
            final CompoundTag cmp2 = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(posEntry2.getKey(), cmp2);
            NBTHelper.setBlockState(cmp2, "state", posEntry2.getValue());
            listConfigurations.add((Object)cmp2);
        }
        compound.put("blockConfiguration", (Tag)listConfigurations);
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (this.inventory.hasCapability(cap, side)) {
            return (LazyOptional<T>)this.inventory.getCapability().cast();
        }
        return (LazyOptional<T>)super.getCapability((Capability)cap, side);
    }
    
    static {
        RITUAL_ANCHOR_OFFEST = new BlockPos(0, 5, 0);
        final Set<BlockPos> circleOffsets = Sets.newHashSet((Object[])new BlockPos[] { new BlockPos(4, 0, 0), new BlockPos(4, 0, 1), new BlockPos(3, 0, 2), new BlockPos(2, 0, 3), new BlockPos(1, 0, 4), new BlockPos(0, 0, 4), new BlockPos(-1, 0, 4), new BlockPos(-2, 0, 3), new BlockPos(-3, 0, 2), new BlockPos(-4, 0, 1), new BlockPos(-4, 0, 0), new BlockPos(-4, 0, -1), new BlockPos(-3, 0, -2), new BlockPos(-2, 0, -3), new BlockPos(-1, 0, -4), new BlockPos(0, 0, -4), new BlockPos(1, 0, -4), new BlockPos(2, 0, -3), new BlockPos(3, 0, -2), new BlockPos(4, 0, -1) });
        final Set<BlockPos> ritualOffsets = new HashSet<BlockPos>(circleOffsets);
        circleOffsets.stream().map(pos -> pos.offset(0, 1, 0)).forEach(ritualOffsets::add);
        circleOffsets.stream().map(pos -> pos.offset(0, 2, 0)).forEach(ritualOffsets::add);
        RITUAL_CIRCLE_OFFSETS = (Set)ImmutableSet.copyOf((Collection)ritualOffsets);
    }
}
