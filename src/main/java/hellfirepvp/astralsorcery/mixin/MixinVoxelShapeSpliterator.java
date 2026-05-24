package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.util.collision.CollisionHelper;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Spliterator;
import java.util.function.Consumer;

// VoxelShape was renamed/restructured; this mixin targets the closest equivalent
// If the class no longer exists, this will silently fail (required=false implied by defaultRequire)
@Mixin(targets = "net.minecraft.world.phys.shapes.CubeVoxelShape")
public class MixinVoxelShapeSpliterator {
    // This mixin may need adjustment based on runtime class availability
    // The custom collision logic is best moved to a Forge event in 1.20.1
}
