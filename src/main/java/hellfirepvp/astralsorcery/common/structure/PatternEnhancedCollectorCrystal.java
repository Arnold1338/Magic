package hellfirepvp.astralsorcery.common.structure;

import javax.annotation.Nonnull;
import net.minecraft.state.Property;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternEnhancedCollectorCrystal extends PatternBlockArray
{
    public PatternEnhancedCollectorCrystal() {
        super(StructureTypesAS.PTYPE_ENHANCED_COLLECTOR_CRYSTAL.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
        final BlockState raw = BlocksAS.MARBLE_RAW.defaultBlockState();
        final BlockState runed = BlocksAS.MARBLE_RUNED.defaultBlockState();
        final BlockState engraved = BlocksAS.MARBLE_ENGRAVED.defaultBlockState();
        this.addBlockCube(raw, -1, -5, -1, 1, -5, 1);
        for (int xx = -1; xx <= 1; ++xx) {
            for (int zz = -1; zz <= 1; ++zz) {
                for (int yy = -1; yy <= 1; ++yy) {
                    this.addBlock(MatchableState.REQUIRES_AIR, xx, yy, zz);
                }
            }
        }
        for (final BlockPos offset : TileCollectorCrystal.OFFSETS_LIQUID_STARLIGHT) {
            this.addBlock(BlocksAS.FLUID_LIQUID_STARLIGHT.defaultBlockState(), offset.getX(), offset.getY(), offset.getZ());
        }
        this.addBlock(BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL.defaultBlockState(), 0, 0, 0);
        this.addBlock(chiseled, 0, -2, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.MIDDLE), 0, -3, 0);
        this.addBlock(engraved, 0, -4, 0);
        this.addBlock(chiseled, -2, -4, -2);
        this.addBlock(chiseled, -2, -4, 2);
        this.addBlock(chiseled, 2, -4, 2);
        this.addBlock(chiseled, 2, -4, -2);
        this.addBlock(engraved, -2, -3, -2);
        this.addBlock(engraved, -2, -3, 2);
        this.addBlock(engraved, 2, -3, 2);
        this.addBlock(engraved, 2, -3, -2);
        this.addBlock(runed, -2, -4, -1);
        this.addBlock(runed, -2, -4, 0);
        this.addBlock(runed, -2, -4, 1);
        this.addBlock(runed, 2, -4, -1);
        this.addBlock(runed, 2, -4, 0);
        this.addBlock(runed, 2, -4, 1);
        this.addBlock(runed, -1, -4, -2);
        this.addBlock(runed, 0, -4, -2);
        this.addBlock(runed, 1, -4, -2);
        this.addBlock(runed, -1, -4, 2);
        this.addBlock(runed, 0, -4, 2);
        this.addBlock(runed, 1, -4, 2);
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
