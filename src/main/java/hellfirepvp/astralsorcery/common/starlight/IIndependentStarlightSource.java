package hellfirepvp.astralsorcery.common.starlight;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry;
import java.util.Map;
import net.minecraft.world.level.block.entity.BlockEntity;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;

public interface IIndependentStarlightSource
{
    public static final Random rand = new Random();
    
    float produceStarlightTick(final ServerLevel p0, final BlockPos p1);
    
    @Nullable
    IWeakConstellation getStarlightType();
    
    default boolean providesAutoLink() {
        return false;
    }
    
    default <T extends BlockEntity> boolean updateFromTileEntity(final T tile) {
        return true;
    }
    
    void threadedUpdateProximity(final BlockPos p0, final Map<BlockPos, IIndependentStarlightSource> p1);
    
    SourceClassRegistry.SourceProvider getProvider();
    
    void readFromNBT(final CompoundTag p0);
    
    void writeToNBT(final CompoundTag p0);
}
