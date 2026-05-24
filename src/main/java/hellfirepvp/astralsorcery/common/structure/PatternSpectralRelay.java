package hellfirepvp.astralsorcery.common.structure;

import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternSpectralRelay extends PatternBlockArray
{
    public PatternSpectralRelay() {
        super(StructureTypesAS.PTYPE_SPECTRAL_RELAY.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        this.addBlock(BlocksAS.SPECTRAL_RELAY.defaultBlockState(), 0, 0, 0);
        final BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
        final BlockState arch = BlocksAS.MARBLE_ARCH.defaultBlockState();
        final BlockState sooty = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
        this.addBlock(sooty, 0, -1, 0);
        this.addBlock(chiseled, -1, -1, -1);
        this.addBlock(chiseled, 1, -1, -1);
        this.addBlock(chiseled, -1, -1, 1);
        this.addBlock(chiseled, 1, -1, 1);
        this.addBlock(arch, -1, -1, 0);
        this.addBlock(arch, 1, -1, 0);
        this.addBlock(arch, 0, -1, -1);
        this.addBlock(arch, 0, -1, 1);
    }
}
