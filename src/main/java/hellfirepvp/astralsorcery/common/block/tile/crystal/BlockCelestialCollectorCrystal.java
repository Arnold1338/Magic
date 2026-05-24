package hellfirepvp.astralsorcery.common.block.tile.crystal;

import hellfirepvp.astralsorcery.common.item.block.ItemBlockCelestialCollectorCrystal;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCollectorCrystal;
import hellfirepvp.astralsorcery.common.block.tile.BlockCollectorCrystal;

public class BlockCelestialCollectorCrystal extends BlockCollectorCrystal
{
    public BlockCelestialCollectorCrystal() {
        super(CollectorCrystalType.CELESTIAL_CRYSTAL);
    }
    
    @Override
    public Class<? extends ItemBlockCollectorCrystal> getItemBlockClass() {
        return ItemBlockCelestialCollectorCrystal.class;
    }
}
