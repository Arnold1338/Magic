package hellfirepvp.astralsorcery.common.item.block;

import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.block.tile.crystal.CollectorCrystalType;
import net.minecraft.world.level.item.Item;
import net.minecraft.world.level.level.block.Block;

public class ItemBlockRockCollectorCrystal extends ItemBlockCollectorCrystal
{
    public ItemBlockRockCollectorCrystal(final Block block, final Item.Properties itemProperties) {
        super(block, itemProperties);
    }
    
    @Override
    public CollectorCrystalType getCollectorType() {
        return CollectorCrystalType.ROCK_CRYSTAL;
    }
    
    @Override
    protected CrystalAttributes getCreativeTemplateAttributes() {
        return CrystalPropertiesAS.CREATIVE_ROCK_COLLECTOR_ATTRIBUTES;
    }
}
