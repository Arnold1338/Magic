package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.core.Direction;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import java.util.function.Function;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.util.object.TransformReference;
import net.minecraft.core.BlockPos;

public class WorldBlockPos extends BlockPos
{
    private final TransformReference<ResourceKey<Level>, Level> worldReference;
    
    private WorldBlockPos(final TransformReference<ResourceKey<Level>, Level> worldReference, final BlockPos pos) {
        super((Vec3i)pos);
        this.worldReference = worldReference;
    }
    
    private WorldBlockPos(final ResourceKey<Level> type, final BlockPos pos, final Function<ResourceKey<Level>, Level> worldProvider) {
        super((Vec3i)pos);
        this.worldReference = new TransformReference<ResourceKey<Level>, Level>(type, worldProvider);
    }
    
    public static WorldBlockPos wrapServer(final Level world, final BlockPos pos) {
        return new WorldBlockPos((ResourceKey<Level>)world.dimension(), pos, type -> {
            final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
            return server.getLevel(type);
        });
    }
    
    public static WorldBlockPos wrapTileEntity(final BlockEntity tile) {
        return new WorldBlockPos((ResourceKey<Level>)tile.getLevel().dimension(), tile.getBlockState(), type -> tile.getLevel());
    }
    
    public ResourceKey<Level> getWorldKey() {
        return this.worldReference.getReference();
    }
    
    private WorldBlockPos wrapInternal(final BlockPos pos) {
        return new WorldBlockPos(this.worldReference, pos);
    }
    
    public WorldBlockPos add(final int x, final int y, final int z) {
        return this.wrapInternal(super.offset(x, y, z));
    }
    
    public WorldBlockPos add(final double x, final double y, final double z) {
        return this.wrapInternal(super.func_177963_a(x, y, z));
    }
    
    public WorldBlockPos add(final Vec3i vec) {
        return this.wrapInternal(super.func_177971_a(vec));
    }
    
    @Nullable
    public <T extends BlockEntity> T getTileAt(final Class<T> tileClass, final boolean forceChunkLoad) {
        final Level world = this.worldReference.getValue();
        if (world != null) {
            return MiscUtils.getTileAt((IBlockReader)world, this, tileClass, forceChunkLoad);
        }
        return null;
    }
    
    @Nullable
    public Level getWorld() {
        return this.worldReference.getValue();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final WorldBlockPos that = (WorldBlockPos)o;
        return Objects.equals(this.getWorldKey(), that.getWorldKey());
    }
    
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.getWorldKey().hashCode();
        return result;
    }
}
