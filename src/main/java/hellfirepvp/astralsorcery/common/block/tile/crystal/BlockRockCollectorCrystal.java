package hellfirepvp.astralsorcery.common.block.tile.crystal;

import hellfirepvp.astralsorcery.common.item.block.ItemBlockRockCollectorCrystal;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCollectorCrystal;
import hellfirepvp.astralsorcery.common.block.tile.BlockCollectorCrystal;

public class BlockRockCollectorCrystal extends BlockCollectorCrystal
{
    public BlockRockCollectorCrystal() {
        super(CollectorCrystalType.ROCK_CRYSTAL);
    }
    
    @Override
    public Class<? extends ItemBlockCollectorCrystal> getItemBlockClass() {
        return ItemBlockRockCollectorCrystal.class;
    }
}
