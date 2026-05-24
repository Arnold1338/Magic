package hellfirepvp.astralsorcery.common.world.structure.feature;

import net.minecraft.world.level.levelgen.feature.FeatureConfiguration;
import hellfirepvp.astralsorcery.common.world.structure.AncientShrineStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.template.TemplateManager;
import net.minecraft.world.level.level.chunk.ChunkGenerator;
import net.minecraft.core.DynamicRegistries;
import net.minecraft.core.MutableBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.NoneFeatureConfiguration;
import net.minecraft.world.level.level.levelgen.structure.Structure;
import hellfirepvp.astralsorcery.common.world.TemplateStructureFeature;

public class FeatureAncientShrineStructure extends TemplateStructureFeature
{
    public Structure.IStartFactory<NoneFeatureConfiguration> func_214557_a() {
        return (Structure.IStartFactory<NoneFeatureConfiguration>)Start::new;
    }
    
    public static class Start extends StructureStart<NoneFeatureConfiguration>
    {
        public Start(final Structure<NoneFeatureConfiguration> config, final int chunkPosX, final int chunkPosZ, final MutableBoundingBox bounds, final int ref, final long seed) {
            super((Structure)config, chunkPosX, chunkPosZ, bounds, ref, seed);
        }
        
        public void func_230364_a_(final DynamicRegistries registries, final ChunkGenerator gen, final TemplateManager mgr, final int chunkX, final int chunkZ, final Biome biome, final NoneFeatureConfiguration cfg) {
            final int x = chunkX * 16 + this.field_214631_d.nextInt(16);
            final int z = chunkZ * 16 + this.field_214631_d.nextInt(16);
            final int y = gen.func_222529_a(x, z, Heightmap.Type.MOTION_BLOCKING);
            final AncientShrineStructure structure = new AncientShrineStructure(mgr, new BlockPos(x, y, z));
            this.field_75075_a.add(structure);
            this.func_202500_a();
        }
    }
}
