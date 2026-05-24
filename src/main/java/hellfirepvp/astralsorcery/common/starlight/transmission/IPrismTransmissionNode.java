package hellfirepvp.astralsorcery.common.starlight.transmission;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import net.minecraft.core.Vec3i;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.astralsorcery.common.data.world.LightNetworkBuffer;
import net.minecraft.world.level.LevelAccessor;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import java.util.Random;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;

public interface IPrismTransmissionNode extends ILocatable
{
    public static final CrystalAttributes EMPTY = CrystalAttributes.Builder.newBuilder(false).build();
    public static final Random rand = new Random();
    
    BlockPos getLocationPos();
    
    default CrystalAttributes getTransmissionProperties() {
        return IPrismTransmissionNode.EMPTY;
    }
    
    default <T extends BlockEntity> boolean updateFromTileEntity(final T tile) {
        return true;
    }
    
    default float getTransmissionThroughputMultiplier() {
        return 1.0f - this.getTransmissionConsumptionMultiplier();
    }
    
    default float getTransmissionConsumptionMultiplier() {
        return 0.0f;
    }
    
    default boolean needsTransmissionUpdate() {
        return false;
    }
    
    default void onTransmissionTick(final World world, final float starlightAmt, final IWeakConstellation type) {
    }
    
    boolean notifyUnlink(final World p0, final BlockPos p1);
    
    void notifyLink(final World p0, final BlockPos p1);
    
    void notifySourceLink(final World p0, final BlockPos p1);
    
    void notifySourceUnlink(final World p0, final BlockPos p1);
    
    boolean notifyBlockChange(final World p0, final BlockPos p1);
    
    List<NodeConnection<IPrismTransmissionNode>> queryNext(final WorldNetworkHandler p0);
    
    List<BlockPos> getSources();
    
    default boolean needsUpdate() {
        return false;
    }
    
    default void update(final World world) {
    }
    
    default void postLoad(final IWorld world) {
    }
    
    default void markDirty(final World world) {
        ((LightNetworkBuffer)DataAS.DOMAIN_AS.getData(world, (WorldCacheDomain.SaveKey)DataAS.KEY_STARLIGHT_NETWORK)).markDirty((Vector3i)this.getLocationPos());
    }
    
    TransmissionProvider getProvider();
    
    void readFromNBT(final CompoundTag p0);
    
    void writeToNBT(final CompoundTag p0);
}
