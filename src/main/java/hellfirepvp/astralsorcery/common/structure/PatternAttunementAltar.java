package hellfirepvp.astralsorcery.common.structure;

import javax.annotation.Nonnull;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternAttunementAltar extends PatternBlockArray
{
    public PatternAttunementAltar() {
        super(StructureTypesAS.PTYPE_ATTUNEMENT_ALTAR.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState arch = BlocksAS.MARBLE_ARCH.defaultBlockState();
        final BlockState sooty = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
        this.addBlock((Block)BlocksAS.ATTUNEMENT_ALTAR, BlockPos.field_177992_a);
        this.addBlockCube(arch, -7, -1, -8, 7, -1, -8);
        this.addBlockCube(arch, -7, -1, 8, 7, -1, 8);
        this.addBlockCube(arch, -8, -1, -7, -8, -1, 7);
        this.addBlockCube(arch, 8, -1, -7, 8, -1, 7);
        this.addBlockCube(sooty, -7, -1, -7, 7, -1, 7);
        this.pillar(-8, 0, -8);
        this.pillar(-8, 0, 8);
        this.pillar(8, 0, -8);
        this.pillar(8, 0, 8);
        this.addBlock(arch, -9, -1, -9);
        this.addBlock(arch, -9, -1, -8);
        this.addBlock(arch, -9, -1, -7);
        this.addBlock(arch, -8, -1, -9);
        this.addBlock(arch, -7, -1, -9);
        this.addBlock(arch, -9, -1, 9);
        this.addBlock(arch, -9, -1, 8);
        this.addBlock(arch, -9, -1, 7);
        this.addBlock(arch, -8, -1, 9);
        this.addBlock(arch, -7, -1, 9);
        this.addBlock(arch, 9, -1, -9);
        this.addBlock(arch, 9, -1, -8);
        this.addBlock(arch, 9, -1, -7);
        this.addBlock(arch, 8, -1, -9);
        this.addBlock(arch, 7, -1, -9);
        this.addBlock(arch, 9, -1, 9);
        this.addBlock(arch, 9, -1, 8);
        this.addBlock(arch, 9, -1, 7);
        this.addBlock(arch, 8, -1, 9);
        this.addBlock(arch, 7, -1, 9);
    }
    
    private void pillar(final int x, final int y, final int z) {
        this.addBlock(BlocksAS.MARBLE_RUNED.defaultBlockState(), x, y, z);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), x, y + 1, z);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.MIDDLE), x, y + 2, z);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), x, y + 3, z);
        this.addBlock(BlocksAS.MARBLE_CHISELED.defaultBlockState(), x, y + 4, z);
    }
    
    private MatchableState getPillarState(final BlockMarblePillar.PillarType type) {
        return (MatchableState)new SimpleMatchableBlock(new Block[] { BlocksAS.MARBLE_PILLAR }) {
            @Nonnull
            public BlockState getDescriptiveState(final long tick) {
                return (BlockState)BlocksAS.MARBLE_PILLAR.defaultBlockState().func_206870_a((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)type);
            }
        };
    }
}
