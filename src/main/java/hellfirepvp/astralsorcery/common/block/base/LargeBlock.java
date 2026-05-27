package hellfirepvp.astralsorcery.common.block.base;

import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.phys.AABB;

public interface LargeBlock
{
    AABB getBlockSpace();
    
    default boolean canPlaceAt(final BlockItemUseContext ctx) {
        final BlockPos pos = ctx.func_195995_a();
        final Level world = ctx.func_195991_k();
        final AABB box = this.getBlockSpace();
        final BlockPos.Mutable mPos = new BlockPos.Mutable();
        for (int xx = (int)box.field_72340_a; xx <= box.field_72336_d; ++xx) {
            for (int yy = (int)box.field_72338_b; yy <= box.field_72337_e; ++yy) {
                for (int zz = (int)box.field_72339_c; zz <= box.field_72334_f; ++zz) {
                    mPos.func_181079_c(pos.getX() + xx, pos.getY() + yy, pos.getZ() + zz);
                    if (!world.isEmptyBlock((BlockPos)mPos) && !world.getBlockState((BlockPos)mPos).func_196953_a(BlockItemUseContext.func_221536_a(ctx, (BlockPos)mPos, Direction.DOWN))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
