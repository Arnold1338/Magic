package hellfirepvp.astralsorcery.common.world.structure.feature;

import net.minecraft.world.gen.feature.IFeatureConfig;
import hellfirepvp.astralsorcery.common.world.structure.AncientShrineStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.level.levelgen.structure.Structure;
import hellfirepvp.astralsorcery.common.world.TemplateStructureFeature;

public class FeatureAncientShrineStructure extends TemplateStructureFeature
{
    public Structure.IStartFactory<NoFeatureConfig> func_214557_a() {
        return (Structure.IStartFactory<NoFeatureConfig>)Start::new;
    }
    
    public static class Start extends StructureStart<NoFeatureConfig>
    {
        public Start(final Structure<NoFeatureConfig> config, final int chunkPosX, final int chunkPosZ, final MutableBoundingBox bounds, final int ref, final long seed) {
            super((Structure)config, chunkPosX, chunkPosZ, bounds, ref, seed);
        }
        
        public void func_230364_a_(final DynamicRegistries registries, final ChunkGenerator gen, final TemplateManager mgr, final int chunkX, final int chunkZ, final Biome biome, final NoFeatureConfig cfg) {
            final int x = chunkX * 16 + this.field_214631_d.nextInt(16);
            final int z = chunkZ * 16 + this.field_214631_d.nextInt(16);
            final int y = gen.func_222529_a(x, z, Heightmap.Type.MOTION_BLOCKING);
            final AncientShrineStructure structure = new AncientShrineStructure(mgr, new BlockPos(x, y, z));
            this.field_75075_a.add(structure);
            this.func_202500_a();
        }
    }
}
