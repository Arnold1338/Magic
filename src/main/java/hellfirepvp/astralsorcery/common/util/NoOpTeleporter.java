package hellfirepvp.astralsorcery.common.util;

import java.util.function.Function;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Teleporter;

public class NoOpTeleporter extends Teleporter
{
    private final BlockPos targetPos;
    
    public NoOpTeleporter(final ServerLevel worldIn, final BlockPos targetPos) {
        super(worldIn);
        this.targetPos = targetPos;
    }
    
    public Entity placeEntity(final Entity entity, final ServerLevel currentWorld, final ServerLevel destWorld, final float yaw, final Function<Boolean, Entity> repositionEntity) {
        final Entity created = repositionEntity.apply(false);
        created.func_70634_a((double)this.targetPos.getX(), (double)this.targetPos.getY(), (double)this.targetPos.getZ());
        return created;
    }
}
