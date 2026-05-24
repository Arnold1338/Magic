package hellfirepvp.astralsorcery.common.crafting.nojson.freezing;

import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.IBlockDisplayReader;
import hellfirepvp.astralsorcery.AstralSorcery;

public class FluidFreezingRecipe extends BlockFreezingRecipe
{
    public FluidFreezingRecipe() {
        super(AstralSorcery.key("all_fluids_freezing"), (world, pos, state) -> state.getFluidState().func_206889_d() && state.getFluidState().func_206883_i().equals(state), (worldPos, state) -> {
            final FluidAttributes fAttr = state.getFluidState().func_206886_c().getAttributes();
            if (fAttr.getTemperature((IBlockDisplayReader)worldPos.getWorld(), (BlockPos)worldPos) <= 300) {
                return Blocks.field_150432_aD.defaultBlockState();
            }
            else if (fAttr.getTemperature((IBlockDisplayReader)worldPos.getWorld(), (BlockPos)worldPos) >= 500) {
                return Blocks.field_150343_Z.defaultBlockState();
            }
            else {
                return state;
            }
        });
    }
}
