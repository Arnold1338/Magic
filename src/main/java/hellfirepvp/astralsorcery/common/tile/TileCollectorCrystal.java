package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.IndependentCrystalSource;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import net.minecraft.world.phys.AABB;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.source.FXSource;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import java.awt.Color;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.client.effect.source.orbital.FXOrbitalCollector;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.block.tile.crystal.CollectorCrystalType;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.constellation.ConstellationTile;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeTile;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionSourceNode;
import hellfirepvp.astralsorcery.common.tile.base.network.TileSourceBase;

public class TileCollectorCrystal extends TileSourceBase<SimpleTransmissionSourceNode> implements CrystalAttributeTile, ConstellationTile
{
    public static final BlockPos[] OFFSETS_LIQUID_STARLIGHT;
    private UUID playerUUID;
    private CrystalAttributes crystalAttributes;
    private CollectorCrystalType collectorType;
    private IWeakConstellation constellationType;
    private IMinorConstellation constellationTrait;
    private Object[] effectOrbitals;
    
    public TileCollectorCrystal() {
        super(TileEntityTypesAS.COLLECTOR_CRYSTAL);
        this.playerUUID = null;
        this.collectorType = CollectorCrystalType.ROCK_CRYSTAL;
        this.effectOrbitals = new Object[4];
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.getLevel().level()) {
            this.doesSeeSky();
            this.hasMultiblock();
        }
        else {
            this.playEffects();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        final Vector3 thisPos = new Vector3(this).add(0.5f, 0.5f, 0.5f);
        final Vector3 particlePos = thisPos.clone();
        MiscUtils.applyRandomOffset(particlePos, TileCollectorCrystal.rand, 0.75f);
        if (this.isEnhanced() && this.doesSeeSky() && this.getCollectorType() == CollectorCrystalType.CELESTIAL_CRYSTAL && this.getAttunedConstellation() != null) {
            final Color c = this.getAttunedConstellation().getConstellationColor();
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(particlePos).setScaleMultiplier(0.2f + TileCollectorCrystal.rand.nextFloat() * 0.1f).setAlphaMultiplier(0.8f).color(VFXColorFunction.constant(c)).setMaxAge(20 + TileCollectorCrystal.rand.nextInt(10));
            for (int i = 0; i < this.effectOrbitals.length; ++i) {
                final FXOrbitalCollector fxSource = (FXOrbitalCollector)this.effectOrbitals[i];
                if (fxSource == null) {
                    final FXSource<?, ?> src = new FXOrbitalCollector(new Vector3(this).add(0.5f, 0.5f, 0.5f), c).setOrbitAxis(Vector3.random()).setOrbitRadius(0.8f + TileCollectorCrystal.rand.nextFloat() * 0.5f).setTicksPerRotation(40 + TileCollectorCrystal.rand.nextInt(30));
                    EffectHelper.spawnSource(src);
                    this.effectOrbitals[i] = src;
                }
                else if (fxSource.canRemove() && fxSource.isRemoved()) {
                    this.effectOrbitals[i] = null;
                }
            }
            final BlockPos starlightSource = MiscUtils.getRandomEntry(TileCollectorCrystal.OFFSETS_LIQUID_STARLIGHT, TileCollectorCrystal.rand).func_177971_a((Vec3i)this.getBlockState());
            final Vector3 from = new Vector3((Vec3i)starlightSource).add(TileCollectorCrystal.rand.nextFloat(), 0.85f, TileCollectorCrystal.rand.nextFloat());
            final Vector3 motion = thisPos.clone().subtract(from).normalize().multiply(0.08f);
            final Color particleColor = MiscUtils.eitherOf(TileCollectorCrystal.rand, new Color[] { Color.WHITE, c, c.brighter() });
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(from).setMotion(motion).alpha(VFXAlphaFunction.proximity(thisPos::clone, 2.0f).andThen(VFXAlphaFunction.FADE_OUT)).setScaleMultiplier(0.2f + TileCollectorCrystal.rand.nextFloat() * 0.1f).color(VFXColorFunction.constant(particleColor)).setMaxAge(30 + TileCollectorCrystal.rand.nextInt(10));
            if (TileCollectorCrystal.rand.nextInt(80) == 0) {
                EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(thisPos).makeDefault(from).color(VFXColorFunction.constant(c));
            }
        }
        else if (TileCollectorCrystal.rand.nextBoolean()) {
            final Color c = this.getCollectorType().getDisplayColor();
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(particlePos).setScaleMultiplier(0.2f + TileCollectorCrystal.rand.nextFloat() * 0.1f).setAlphaMultiplier(0.8f).color(VFXColorFunction.constant(c)).setMaxAge(20 + TileCollectorCrystal.rand.nextInt(10));
        }
    }
    
    public boolean isEnhanced() {
        return this.getCollectorType() == CollectorCrystalType.CELESTIAL_CRYSTAL && this.hasMultiblock();
    }
    
    public boolean isPlayerMade() {
        return this.getPlayerUUID() != null;
    }
    
    @Override
    public IWeakConstellation getAttunedConstellation() {
        return this.constellationType;
    }
    
    @Override
    public boolean setAttunedConstellation(@Nullable final IWeakConstellation cst) {
        if (cst != this.constellationType) {
            this.markForUpdate();
        }
        this.constellationType = cst;
        return true;
    }
    
    @Override
    public IMinorConstellation getTraitConstellation() {
        return this.constellationTrait;
    }
    
    @Override
    public boolean setTraitConstellation(@Nullable final IMinorConstellation cst) {
        if (cst != this.constellationTrait) {
            this.markForUpdate();
        }
        this.constellationTrait = cst;
        return true;
    }
    
    @Nullable
    @Override
    public CrystalAttributes getAttributes() {
        return this.crystalAttributes;
    }
    
    @Override
    public void setAttributes(@Nullable final CrystalAttributes attributes) {
        if (this.crystalAttributes == null && attributes == null) {

        }
        if (attributes == null || !attributes.equals(this.crystalAttributes)) {
            this.markForUpdate();
        }
        this.crystalAttributes = attributes;
    }
    
    public CollectorCrystalType getCollectorType() {
        return this.collectorType;
    }
    
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
    
    public void updateData(final UUID playerUUID, final CollectorCrystalType collectorType) {
        this.playerUUID = playerUUID;
        this.collectorType = collectorType;
        if (this.collectorType == null) {
            this.collectorType = CollectorCrystalType.ROCK_CRYSTAL;
        }
        this.markForUpdate();
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        if (this.collectorType == CollectorCrystalType.CELESTIAL_CRYSTAL) {
            return StructureTypesAS.PTYPE_ENHANCED_COLLECTOR_CRYSTAL;
        }
        return null;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.constellationType = NBTHelper.readOptional(compound, "constellationType", nbt -> {
            final IConstellation cst = IConstellation.readFromNBT(nbt);
            if (cst instanceof IWeakConstellation) {
                return (IConstellation)cst;
            }
            else {
                return null;
            }
        });
        this.constellationTrait = NBTHelper.readOptional(compound, "constellationTrait", nbt -> {
            final IConstellation cst2 = IConstellation.readFromNBT(nbt);
            if (cst2 instanceof IMinorConstellation) {
                return (IConstellation)cst2;
            }
            else {
                return null;
            }
        });
        this.setAttributes(CrystalAttributes.getCrystalAttributes(compound));
        this.crystalAttributes = CrystalAttributes.getCrystalAttributes(compound);
        this.collectorType = NBTHelper.readEnum(compound, "collectorType", CollectorCrystalType.class);
        this.playerUUID = NBTHelper.readOptional(compound, "playerUUID", nbt -> nbt.getUUID("playerUUID"));
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        if (this.getAttributes() != null) {
            this.getAttributes().store(compound);
        }
        NBTHelper.writeOptional(compound, "constellationType", this.constellationType, (nbt, cst) -> cst.writeToNBT(nbt));
        NBTHelper.writeOptional(compound, "constellationTrait", this.constellationTrait, (nbt, cst) -> cst.writeToNBT(nbt));
        NBTHelper.writeEnum(compound, "collectorType", this.collectorType);
        NBTHelper.writeOptional(compound, "playerUUID", this.playerUUID, (nbt, uuid) -> nbt.putUUID("playerUUID", uuid));
    }
    
    public AABB getRenderBoundingBox() {
        return TileCollectorCrystal.BOX.func_186662_g(1.0).func_186670_a(this.getBlockState());
    }
    
    @Nonnull
    @Override
    public IIndependentStarlightSource provideNewSourceNode() {
        return new IndependentCrystalSource();
    }
    
    @Nonnull
    @Override
    public SimpleTransmissionSourceNode provideSourceNode(final BlockPos at) {
        return new SimpleTransmissionSourceNode(at);
    }
    
    static {
        OFFSETS_LIQUID_STARLIGHT = new BlockPos[] { new BlockPos(-1, -4, -1), new BlockPos(0, -4, -1), new BlockPos(1, -4, -1), new BlockPos(1, -4, 0), new BlockPos(1, -4, 1), new BlockPos(0, -4, 1), new BlockPos(-1, -4, 1), new BlockPos(-1, -4, 0) };
    }
}
