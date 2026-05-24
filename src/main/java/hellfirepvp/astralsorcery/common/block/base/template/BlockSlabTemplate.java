package hellfirepvp.astralsorcery.common.block.base.template;

import javax.annotation.Nullable;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.level.block.state.BlockBehaviour;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.SlabBlock;

public class BlockSlabTemplate extends SlabBlock implements CustomItemBlock
{
    private final BlockState baseState;
    
    public BlockSlabTemplate(final BlockState baseState, final AbstractBlock.Properties properties) {
        super(properties);
        this.baseState = baseState;
    }
    
    @Nullable
    public ToolType getHarvestTool(final BlockState tool) {
        return this.baseState.getHarvestTool();
    }
    
    public int getHarvestLevel(final BlockState state) {
        return this.baseState.getHarvestLevel();
    }
}
