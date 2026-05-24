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
    private final TransformReference<RegistryKey<World>, World> worldReference;
    
    private WorldBlockPos(final TransformReference<RegistryKey<World>, World> worldReference, final BlockPos pos) {
        super((Vector3i)pos);
        this.worldReference = worldReference;
    }
    
    private WorldBlockPos(final RegistryKey<World> type, final BlockPos pos, final Function<RegistryKey<World>, World> worldProvider) {
        super((Vector3i)pos);
        this.worldReference = new TransformReference<RegistryKey<World>, World>(type, worldProvider);
    }
    
    public static WorldBlockPos wrapServer(final World world, final BlockPos pos) {
        return new WorldBlockPos((RegistryKey<World>)world.dimension(), pos, type -> {
            final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
            return server.func_71218_a(type);
        });
    }
    
    public static WorldBlockPos wrapTileEntity(final BlockEntity tile) {
        return new WorldBlockPos((RegistryKey<World>)tile.func_145831_w().dimension(), tile.func_174877_v(), type -> tile.func_145831_w());
    }
    
    public RegistryKey<World> getWorldKey() {
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
    
    public WorldBlockPos add(final Vector3i vec) {
        return this.wrapInternal(super.func_177971_a(vec));
    }
    
    @Nullable
    public <T extends BlockEntity> T getTileAt(final Class<T> tileClass, final boolean forceChunkLoad) {
        final World world = this.worldReference.getValue();
        if (world != null) {
            return MiscUtils.getTileAt((IBlockReader)world, this, tileClass, forceChunkLoad);
        }
        return null;
    }
    
    @Nullable
    public World getWorld() {
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
