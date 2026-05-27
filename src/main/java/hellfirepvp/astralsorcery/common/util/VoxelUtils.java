package hellfirepvp.astralsorcery.common.util;

import net.minecraft.world.phys.shapes.Shapes;
import java.util.List;
import java.util.Arrays;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.BooleanOp;

public class VoxelUtils
{
    public static VoxelShape combineAll(final BooleanOp fct, final VoxelShape... shapes) {
        return combineAll(fct, Arrays.asList(shapes));
    }
    
    public static VoxelShape combineAll(final BooleanOp fct, final List<VoxelShape> shapes) {
        if (shapes.isEmpty()) {
            return VoxelShapes.func_197880_a();
        }
        VoxelShape first = shapes.get(0);
        for (int i = 1; i < shapes.size(); ++i) {
            first = VoxelShapes.func_197882_b(first, (VoxelShape)shapes.get(i), fct);
        }
        return first;
    }
}
