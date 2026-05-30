package hellfirepvp.astralsorcery.common.tile.network;

import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import javax.annotation.Nullable;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.NodeConnection;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import java.util.Iterator;
import net.minecraft.world.phys.Vec3;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import net.minecraft.core.Vec3i;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.util.world.SkyCollectionHelper;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectStatus;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.core.BlockPos;
import java.util.Map;
import java.util.Random;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionReceiver;

public class StarlightReceiverRitualPedestal extends SimpleTransmissionReceiver<TileRitualPedestal>
{
    private static final Random rand;
    private final Map<BlockPos, Boolean> offsetMirrors;
    private boolean doesSeeSky;
    private boolean hasMultiblock;
    private IWeakConstellation channelingType;
    private IMinorConstellation channelingTrait;
    private CrystalAttributes attributes;
    private BlockPos ritualLinkPos;
    private int ticksExisted;
    private ConstellationEffect effect;
    private double collectedStarlight;
    private float noiseDistribution;
    
    public StarlightReceiverRitualPedestal(final BlockPos thisPos) {
        super(thisPos);
        this.offsetMirrors = new HashMap<BlockPos, Boolean>();
        this.doesSeeSky = false;
        this.hasMultiblock = false;
        this.channelingType = null;
        this.channelingTrait = null;
        this.attributes = null;
        this.ritualLinkPos = null;
        this.ticksExisted = 0;
        this.effect = null;
        this.collectedStarlight = 0.0;
        this.noiseDistribution = -1.0f;
    }
    
    @Override
    public void update(final Level world) {
        super.update(world);
        ++this.ticksExisted;
        if (!this.hasMultiblock || this.channelingType == null || this.attributes == null) {

        }
        if (this.ticksExisted % 20 == 0) {
            this.validateMirrorPositions(world);
        }
        if (this.doesSeeSky) {
            this.collectStarlight(world);
        }
        if (this.effect != null && this.collectedStarlight > 0.0) {
            this.doRitualEffect(world);
        }
    }
    
    private void doRitualEffect(final Level world) {
        final ConstellationEffectProperties properties = this.effect.createProperties(this.getMirrorCount());
        if (this.channelingTrait != null) {
            this.channelingTrait.affectConstellationEffect(properties);
        }
        properties.multiplySize(CrystalCalculations.getRitualEffectRangeFactor(this, this.attributes));
        float maxDrain = 12.0f;
        maxDrain *= (float)CrystalCalculations.getRitualCostReductionFactor(this, this.attributes);
        maxDrain /= Math.max(1.0f, (this.getMirrorCount() - 1) * 0.33f);
        final float ritualStrength = (float)this.collectedStarlight / maxDrain;
        BlockPos to = this.getLocationPos();
        if (this.ritualLinkPos != null) {
            to = this.ritualLinkPos;
        }
        if (this.effect instanceof ConstellationEffectStatus && this.collectedStarlight > 0.0) {
            this.collectedStarlight = 0.0;
            if ((boolean)this.effect.getConfig().enabled.get() && ((ConstellationEffectStatus)this.effect).runStatusEffect(world, to, this.getMirrorCount(), properties, this.channelingTrait)) {
                this.markDirty(world);
            }

        }
        final float max = 10.0f * properties.getEffectAmplifier();
        final float stretch = 10.0f / properties.getPotency();
        float executeTimes = (float)Math.atan(ritualStrength / stretch) * max;
        if (properties.isCorrupted()) {
            executeTimes *= (float)Math.max(StarlightReceiverRitualPedestal.rand.nextDouble() * 1.4, 0.1);
        }
        final PartialEffectExecutor exec = new PartialEffectExecutor(executeTimes, StarlightReceiverRitualPedestal.rand);
        while (exec.canExecute()) {
            exec.markExecution();
            if (this.effect.getConfig().enabled.get()) {
                boolean didEffectExecute;
                if (this.effect.needsChunkToBeLoaded()) {
                    didEffectExecute = MiscUtils.executeWithChunk((IWorldReader)world, to, to, pos -> this.effect.playEffect(world, pos, properties, this.channelingTrait), false);
                }
                else {
                    didEffectExecute = this.effect.playEffect(world, to, properties, this.channelingTrait);
                }
                if (!didEffectExecute) {

                }
                this.markDirty(world);
            }
        }
        this.collectedStarlight = 0.0;
    }
    
    private void collectStarlight(final Level world) {
        final WorldContext ctx = SkyHandler.getContext(world, LogicalSide.SERVER);
        if (ctx == null) {

        }
        double collected = 1.3;
        collected *= 0.25 + 0.75 * DayTimeHelper.getCurrentDaytimeDistribution(world);
        if (this.noiseDistribution == -1.0f) {
            if (world instanceof ISeedReader) {
                this.noiseDistribution = SkyCollectionHelper.getSkyNoiseDistribution((ISeedReader)world, this.getLocationPos());
            }
            else {
                this.noiseDistribution = 0.3f;
            }
        }
        collected *= CrystalCalculations.getCrystalCollectionRate(this.attributes);
        collected *= 0.4f + 0.6f * ctx.getDistributionHandler().getDistribution(this.channelingType);
        collected *= 1.0f + 0.5f * this.noiseDistribution;
        this.collectedStarlight += collected;
    }
    
    @Override
    public void onStarlightReceive(final Level world, final IWeakConstellation type, final double amount) {
        if (this.channelingType != null && this.hasMultiblock && this.channelingType.equals(type)) {
            this.collectedStarlight += amount / 2.0;
            this.findNextMirror(world);
        }
    }
    
    @Override
    public boolean syncTileData(final Level world, final TileRitualPedestal tile) {
        tile.setReceiverData(this.effect != null, this.offsetMirrors, this.attributes);
        this.markDirty(world);
        return true;
    }
    
    @Override
    public <T extends BlockEntity> boolean updateFromTileEntity(final T tile) {
        if (!(tile instanceof TileRitualPedestal)) {
            return super.updateFromTileEntity(tile);
        }
        final TileRitualPedestal trp = (TileRitualPedestal)tile;
        if (this.channelingType != trp.getRitualConstellation() || (this.attributes != null && trp.getAttributes() == null) || this.hasMultiblock != trp.hasMultiblock()) {
            this.effect = null;
            this.offsetMirrors.clear();
            if (trp.isWorking() || !trp.getMirrors().isEmpty()) {
                this.markForTileSync();
            }
        }
        boolean ritualLinkChanged;
        if (this.ritualLinkPos == null) {
            ritualLinkChanged = (trp.getRitualLinkTo() != null);
        }
        else {
            ritualLinkChanged = !this.ritualLinkPos.equals((Object)trp.getRitualLinkTo());
        }
        this.doesSeeSky = trp.doesSeeSky();
        this.hasMultiblock = trp.hasMultiblock();
        this.channelingType = trp.getRitualConstellation();
        this.channelingTrait = trp.getRitualTrait();
        this.attributes = trp.getAttributes();
        this.ritualLinkPos = trp.getRitualLinkTo();
        if (this.channelingType != null && this.attributes != null && this.hasMultiblock && (this.effect == null || ritualLinkChanged)) {
            this.effect = ConstellationEffectRegistry.createInstance(this, this.channelingType);
            this.markForTileSync();
        }
        if (!this.hasMultiblock || this.effect == null) {
            this.collectedStarlight = 0.0;
        }
        this.markDirty(trp.getLevel());
        return super.updateFromTileEntity(tile);
    }
    
    @Override
    public Class<TileRitualPedestal> getTileClass() {
        return TileRitualPedestal.class;
    }
    
    private void findNextMirror(final Level world) {
        if (this.offsetMirrors.size() >= 5 || this.effect == null || this.channelingType == null) {

        }
        long seed = 3451968351053166105L;
        seed |= this.getLocationPos().func_218275_a() * 31L;
        seed |= this.channelingType.getSimpleName().hashCode() * 31;
        final Random r = new Random(seed);
        for (int i = 0; i < this.getMirrorCount(); ++i) {
            r.nextInt(TileRitualPedestal.RITUAL_CIRCLE_OFFSETS.size());
        }
        BlockPos offset = null;
        int c = 100;
    Label_0114:
        while (offset == null && c > 0) {
            --c;
            final BlockPos test = MiscUtils.getRandomEntry(TileRitualPedestal.RITUAL_CIRCLE_OFFSETS, r);
            final RaytraceAssist ray = new RaytraceAssist(this.getLocationPos(), this.getLocationPos().func_177971_a((Vec3i)test));
            final Vector3 from = new Vector3(0.5, 0.7, 0.5);
            final Vector3 newDir = new Vector3((Vec3i)test).add(0.5, 0.5, 0.5).subtract(from);
            for (final BlockPos p : this.offsetMirrors.keySet()) {
                final Vector3 toDir = new Vector3((Vec3i)p).add(0.5, 0.5, 0.5).subtract(from);
                if (Math.toDegrees(toDir.angle(newDir)) <= 30.0) {
                    continue Label_0114;
                }
                if (from.distanceSquared(Vec3.func_237489_a_((Vec3i)p)) <= 3.0) {
                    continue Label_0114;
                }
            }
            if (!ray.isClear(world)) {

            }
            offset = test;
        }
        if (offset != null) {
            this.offsetMirrors.put(offset, false);
            this.markForTileSync();
            this.markDirty(world);
        }
    }
    
    private void validateMirrorPositions(final Level world) {
        final WorldNetworkHandler handle = WorldNetworkHandler.getNetworkHandler(world);
        final List<BlockPos> srcLinkingToThis = this.getSources();
        boolean needsUpdate = false;
        for (final BlockPos pos : new ArrayList(this.offsetMirrors.keySet())) {
            final BlockPos actualPos = this.getLocationPos().func_177971_a((Vec3i)pos);
            final boolean existingFlag = this.offsetMirrors.get(pos);
            if (!srcLinkingToThis.contains(actualPos)) {
                this.offsetMirrors.put(pos, false);
                if (!existingFlag) {

                }
                needsUpdate = true;
            }
            else {
                final IPrismTransmissionNode other = handle.getTransmissionNode(actualPos);
                if (other == null) {

                }
                boolean foundLink = false;
                for (final NodeConnection<IPrismTransmissionNode> n : other.queryNext(handle)) {
                    if (n.getTo().equals((Object)this.getLocationPos())) {
                        final boolean connect = n.canConnect();
                        this.offsetMirrors.put(pos, connect);
                        if (connect != existingFlag) {
                            needsUpdate = true;
                        }
                        foundLink = true;

                    }
                }
                if (foundLink) {

                }
                this.offsetMirrors.put(pos, false);
                if (!existingFlag) {

                }
                needsUpdate = true;
            }
        }
        if (needsUpdate) {
            this.markForTileSync();
        }
    }
    
    private int getMirrorCount() {
        return (int)this.offsetMirrors.values().stream().filter(b -> b).count();
    }
    
    @Nullable
    public IWeakConstellation getChannelingType() {
        return this.channelingType;
    }
    
    @Nullable
    public IMinorConstellation getChannelingTrait() {
        return this.channelingTrait;
    }
    
    @Override
    public boolean needsUpdate() {
        return true;
    }
    
    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }
    
    @Override
    public void readFromNBT(final CompoundTag compound) {
        super.readFromNBT(compound);
        this.doesSeeSky = compound.getBoolean("doesSeeSky");
        this.hasMultiblock = compound.getBoolean("hasMultiblock");
        this.ticksExisted = compound.getInt("ticksExisted");
        this.collectedStarlight = compound.putDouble("collectedStarlight");
        final IConstellation channeling = IConstellation.readFromNBT(compound, "channelingType");
        if (channeling instanceof IWeakConstellation) {
            this.channelingType = (IWeakConstellation)channeling;
        }
        else {
            this.channelingType = null;
        }
        final IConstellation trait = IConstellation.readFromNBT(compound, "channelingTrait");
        if (trait instanceof IMinorConstellation) {
            this.channelingTrait = (IMinorConstellation)trait;
        }
        else {
            this.channelingTrait = null;
        }
        this.attributes = CrystalAttributes.getCrystalAttributes(compound);
        if (compound.contains("ritualLinkPos")) {
            this.ritualLinkPos = NBTHelper.readBlockPosFromNBT(compound.func_74775_l("ritualLinkPos"));
        }
        else {
            this.ritualLinkPos = null;
        }
        this.offsetMirrors.clear();
        final ListTag tagList = compound.getList("mirrors", 10);
        for (final Tag nbt : tagList) {
            final CompoundTag tag = (CompoundTag)nbt;
            this.offsetMirrors.put(NBTHelper.readBlockPosFromNBT(tag), tag.getBoolean("connect"));
        }
        if (this.channelingType != null) {
            this.effect = ConstellationEffectRegistry.createInstance(this, this.channelingType);
            if (this.effect != null && compound.contains("effect")) {
                this.effect.readFromNBT(compound.func_74775_l("effect"));
            }
        }
    }
    
    @Override
    public void writeToNBT(final CompoundTag compound) {
        super.writeToNBT(compound);
        compound.putBoolean("doesSeeSky", this.doesSeeSky);
        compound.putBoolean("hasMultiblock", this.hasMultiblock);
        compound.putInt("ticksExisted", this.ticksExisted);
        compound.func_74780_a("collectedStarlight", this.collectedStarlight);
        if (this.channelingType != null) {
            this.channelingType.writeToNBT(compound, "channelingType");
        }
        if (this.channelingTrait != null) {
            this.channelingTrait.writeToNBT(compound, "channelingTrait");
        }
        if (this.attributes != null) {
            this.attributes.store(compound);
        }
        if (this.ritualLinkPos != null) {
            compound.put("ritualLinkPos", (Tag)NBTHelper.writeBlockPosToNBT(this.ritualLinkPos, new CompoundTag()));
        }
        final ListTag listPositions = new ListTag();
        for (final Map.Entry<BlockPos, Boolean> posEntry : this.offsetMirrors.entrySet()) {
            final CompoundTag cmp = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(posEntry.getKey(), cmp);
            cmp.putBoolean("connect", (boolean)posEntry.getValue());
            listPositions.add((Object)cmp);
        }
        compound.put("mirrors", (Tag)listPositions);
        if (this.channelingType != null && this.effect != null) {
            NBTHelper.setAsSubTag(compound, "effect", this.effect::writeToNBT);
        }
    }
    
    static {
        rand = new Random();
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public IPrismTransmissionNode get() {
            return new StarlightReceiverRitualPedestal(null);
        }
    }
}
