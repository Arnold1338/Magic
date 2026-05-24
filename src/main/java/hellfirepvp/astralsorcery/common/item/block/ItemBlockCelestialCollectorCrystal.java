package hellfirepvp.astralsorcery.common.item.block;

import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.block.tile.crystal.CollectorCrystalType;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockCelestialCollectorCrystal extends ItemBlockCollectorCrystal
{
    public ItemBlockCelestialCollectorCrystal(final Block block, final Item.Properties itemProperties) {
        super(block, itemProperties.func_208103_a(CommonProxy.RARITY_CELESTIAL));
    }
    
    @Override
    public CollectorCrystalType getCollectorType() {
        return CollectorCrystalType.CELESTIAL_CRYSTAL;
    }
    
    @Override
    protected CrystalAttributes getCreativeTemplateAttributes() {
        return CrystalPropertiesAS.CREATIVE_CELESTIAL_COLLECTOR_ATTRIBUTES;
    }
}
