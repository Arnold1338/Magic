package hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.phys.Vec3;
import java.util.Map;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.util.world.SkyCollectionHelper;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;

public class IndependentCrystalSource implements IIndependentStarlightSource
{
    public static final double MIN_DST = 16.0;
    private IWeakConstellation constellation;
    private CrystalAttributes crystalAttributes;
    private boolean doesSeeSky;
    private boolean doesAutoLink;
    private double collectionDstMultiplier;
    private BlockPos closestOtherCollector;
    private float posDistribution;
    private boolean enhanced;
    
    public IndependentCrystalSource() {
        this.constellation = null;
        this.crystalAttributes = null;
        this.doesSeeSky = false;
        this.doesAutoLink = false;
        this.collectionDstMultiplier = 1.0;
        this.closestOtherCollector = null;
        this.posDistribution = -1.0f;
        this.enhanced = false;
    }
    
    @Override
    public float produceStarlightTick(final ServerLevel world, final BlockPos pos) {
        if (!this.doesSeeSky || this.crystalAttributes == null) {
            return 0.0f;
        }
        final IWeakConstellation cst = this.getStarlightType();
        final WorldContext ctx = SkyHandler.getContext((World)world, LogicalSide.SERVER);
        if (ctx == null || cst == null) {
            return 0.0f;
        }
        if (this.posDistribution == -1.0f) {
            this.posDistribution = SkyCollectionHelper.getSkyNoiseDistribution((ISeedReader)world, pos);
        }
        if (this.closestOtherCollector != null && IndependentCrystalSource.rand.nextInt(40) == 0) {
            final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.LIGHTNING).addData(buf -> {
                ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5));
                ByteBufUtils.writeVector(buf, new Vector3((Vector3i)this.closestOtherCollector).add(0.5, 0.5, 0.5));
                buf.writeInt(this.constellation.getConstellationColor().darker().getRGB());
                return;
            });
            PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos((World)world, (Vector3i)pos, 32.0));
        }
        final Function<Float, Float> distrFunction = this.getDistributionFunc();
        float perc = CrystalCalculations.getCollectorCrystalCollectionRate(this);
        perc *= distrFunction.apply(0.3f + 0.7f * DayTimeHelper.getCurrentDaytimeDistribution((World)world));
        perc *= (float)this.collectionDstMultiplier;
        perc *= (float)(1.0 + 0.3 * this.posDistribution);
        perc *= (float)(0.4 + 0.6 * ctx.getDistributionHandler().getDistribution(cst));
        return perc;
    }
    
    private Function<Float, Float> getDistributionFunc() {
        if (this.enhanced) {
            return (Function<Float, Float>)(in -> 0.6f + 0.5f * in);
        }
        return (Function<Float, Float>)(in -> 0.2f + 0.8f * in);
    }
    
    @Override
    public boolean providesAutoLink() {
        return this.doesAutoLink;
    }
    
    @Override
    public <T extends BlockEntity> boolean updateFromTileEntity(final T tile) {
        if (!(tile instanceof TileCollectorCrystal)) {
            return true;
        }
        final TileCollectorCrystal tcc = (TileCollectorCrystal)tile;
        this.doesSeeSky = tcc.doesSeeSky();
        this.doesAutoLink = !tcc.isPlayerMade();
        this.enhanced = tcc.isEnhanced();
        this.constellation = tcc.getAttunedConstellation();
        this.crystalAttributes = tcc.getAttributes();
        return true;
    }
    
    @Override
    public void threadedUpdateProximity(final BlockPos thisPos, final Map<BlockPos, IIndependentStarlightSource> otherSources) {
        double minDstSq = Double.MAX_VALUE;
        BlockPos closest = null;
        for (final BlockPos other : otherSources.keySet()) {
            if (other.equals((Object)thisPos)) {
                continue;
            }
            final double dstSq = thisPos.func_218138_a((IPosition)Vec3.func_237491_b_((Vector3i)other), false);
            if (dstSq >= minDstSq) {
                continue;
            }
            minDstSq = dstSq;
            closest = other;
        }
        final double dst = Math.sqrt(minDstSq);
        if (dst <= 16.0) {
            this.collectionDstMultiplier = dst / 16.0;
            this.closestOtherCollector = closest;
        }
        else {
            this.collectionDstMultiplier = 1.0;
            this.closestOtherCollector = null;
        }
    }
    
    public CrystalAttributes getCrystalAttributes() {
        return this.crystalAttributes;
    }
    
    @Nullable
    @Override
    public IWeakConstellation getStarlightType() {
        return this.constellation;
    }
    
    @Override
    public SourceClassRegistry.SourceProvider getProvider() {
        return new Provider();
    }
    
    @Override
    public void readFromNBT(final CompoundTag compound) {
        this.constellation = NBTHelper.readOptional(compound, "constellation", nbt -> {
            final IConstellation cst = IConstellation.readFromNBT(nbt);
            if (cst instanceof IWeakConstellation) {
                return (IConstellation)cst;
            }
            else {
                return null;
            }
        });
        this.crystalAttributes = CrystalAttributes.getCrystalAttributes(compound);
        this.doesSeeSky = compound.getBoolean("doesSeeSky");
        this.doesAutoLink = compound.getBoolean("doesAutoLink");
        this.collectionDstMultiplier = compound.putDouble("collectionDstMultiplier");
        this.closestOtherCollector = NBTHelper.readOptional(compound, "closestOtherCollector", NBTHelper::readBlockPosFromNBT);
        this.enhanced = compound.getBoolean("enhanced");
    }
    
    @Override
    public void writeToNBT(final CompoundTag compound) {
        if (this.crystalAttributes != null) {
            this.crystalAttributes.store(compound);
        }
        NBTHelper.writeOptional(compound, "constellation", this.constellation, (nbt, cst) -> cst.writeToNBT(nbt));
        compound.putBoolean("doesSeeSky", this.doesSeeSky);
        compound.putBoolean("doesAutoLink", this.doesAutoLink);
        compound.func_74780_a("collectionDstMultiplier", this.collectionDstMultiplier);
        NBTHelper.writeOptional(compound, "closestOtherCollector", this.closestOtherCollector, (nbt, pos) -> NBTHelper.writeBlockPosToNBT(pos, nbt));
        compound.putBoolean("enhanced", this.enhanced);
    }
    
    public static class Provider implements SourceClassRegistry.SourceProvider
    {
        @Override
        public IIndependentStarlightSource provideEmptySource() {
            return new IndependentCrystalSource();
        }
        
        @Nonnull
        @Override
        public ResourceLocation getIdentifier() {
            return AstralSorcery.key("independent_crystal_source");
        }
    }
}
