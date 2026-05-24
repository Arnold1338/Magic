package hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TilePrism;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimplePrismTransmissionNode;

public class CrystalPrismTransmissionNode extends SimplePrismTransmissionNode
{
    private CrystalAttributes attributes;
    private float additionalLoss;
    
    public CrystalPrismTransmissionNode(final BlockPos thisPos, final CrystalAttributes attributes) {
        super(thisPos);
        this.additionalLoss = 1.0f;
        this.attributes = attributes;
    }
    
    public CrystalPrismTransmissionNode(final BlockPos thisPos) {
        super(thisPos);
        this.additionalLoss = 1.0f;
    }
    
    public boolean updateAdditionalLoss(final float loss) {
        final boolean didChange = this.additionalLoss != loss;
        this.additionalLoss = loss;
        return didChange;
    }
    
    @Override
    public void onTransmissionTick(final World world, final float starlightAmt, final IWeakConstellation type) {
        final TilePrism prism = MiscUtils.getTileAt((IBlockReader)world, this.getLocationPos(), TilePrism.class, false);
        if (prism != null) {
            prism.transmissionTick(starlightAmt, type);
        }
    }
    
    @Override
    public float getTransmissionConsumptionMultiplier() {
        return this.additionalLoss;
    }
    
    @Override
    public boolean needsTransmissionUpdate() {
        return true;
    }
    
    @Override
    public CrystalAttributes getTransmissionProperties() {
        return this.attributes;
    }
    
    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }
    
    @Override
    public void readFromNBT(final CompoundTag compound) {
        super.readFromNBT(compound);
        this.attributes = CrystalAttributes.getCrystalAttributes(compound);
        this.additionalLoss = compound.getFloat("lossMultiplier");
    }
    
    @Override
    public void writeToNBT(final CompoundTag compound) {
        super.writeToNBT(compound);
        if (this.attributes != null) {
            this.attributes.store(compound);
        }
        compound.func_74776_a("lossMultiplier", this.additionalLoss);
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public CrystalPrismTransmissionNode get() {
            return new CrystalPrismTransmissionNode(null);
        }
    }
}
