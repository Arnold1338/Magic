package hellfirepvp.astralsorcery.common.structure;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternCelestialGateway extends PatternBlockArray
{
    public PatternCelestialGateway() {
        super(StructureTypesAS.PTYPE_CELESTIAL_GATEWAY.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState arch = BlocksAS.MARBLE_ARCH.defaultBlockState();
        final BlockState runed = BlocksAS.MARBLE_RUNED.defaultBlockState();
        final BlockState engraved = BlocksAS.MARBLE_ENGRAVED.defaultBlockState();
        final BlockState sooty = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
        this.addBlock((Block)BlocksAS.GATEWAY, BlockPos.field_177992_a);
        this.addBlockCube(arch, -3, -1, -3, 3, -1, 3);
        this.addBlockCube(sooty, -2, -1, -2, 2, -1, 2);
        this.addBlock(runed, -3, -1, -3);
        this.addBlock(runed, 3, -1, -3);
        this.addBlock(runed, 3, -1, 3);
        this.addBlock(runed, -3, -1, 3);
        this.addBlock(engraved, -3, 0, -3);
        this.addBlock(engraved, 3, 0, -3);
        this.addBlock(engraved, 3, 0, 3);
        this.addBlock(engraved, -3, 0, 3);
    }
}
