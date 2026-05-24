package hellfirepvp.astralsorcery.common.block.base.template;

import javax.annotation.Nullable;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.block.StairsBlock;

public class BlockStairsTemplate extends StairsBlock implements CustomItemBlock
{
    private final BlockState baseState;
    
    public BlockStairsTemplate(final BlockState baseState, final AbstractBlock.Properties properties) {
        super(baseState, properties);
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
