package hellfirepvp.astralsorcery.common.data.config.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.util.block.BlockStateList;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.level.block.state.BlockState;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;

public class ConfiguredBlockStateList implements BlockPredicate, Predicate<BlockState>
{
    private final ForgeConfigSpec.ConfigValue<List<String>> configList;
    private BlockStateList resolvedConfiguration;
    
    public ConfiguredBlockStateList(final ForgeConfigSpec.ConfigValue<List<String>> configList) {
        this.resolvedConfiguration = null;
        this.configList = configList;
    }
    
    @Override
    public boolean test(final BlockState state) {
        if (this.resolvedConfiguration == null) {
            this.resolvedConfiguration = BlockStateList.fromConfig((List<String>)this.configList.get());
        }
        return this.resolvedConfiguration.test(state);
    }
    
    @Override
    public boolean test(final World world, final BlockPos pos, final BlockState state) {
        if (this.resolvedConfiguration == null) {
            this.resolvedConfiguration = BlockStateList.fromConfig((List<String>)this.configList.get());
        }
        return this.resolvedConfiguration.test(state);
    }
}
