package hellfirepvp.astralsorcery.common.world;

import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.world.marker.MarkerManagerAS;
import net.minecraft.core.Vec3i;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.ISeedReader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;

public abstract class TemplateStructure extends TemplateStructurePiece
{
    private int yOffset;
    
    public TemplateStructure(final IStructurePieceType structurePieceTypeIn, final TemplateManager mgr, final BlockPos templatePosition) {
        super(structurePieceTypeIn, 0);
        this.yOffset = 0;
        this.field_186178_c = templatePosition;
        this.loadTemplate(mgr);
    }
    
    public TemplateStructure(final IStructurePieceType structurePieceTypeIn, final TemplateManager mgr, final CompoundTag nbt) {
        super(structurePieceTypeIn, nbt);
        this.yOffset = 0;
        this.loadTemplate(mgr);
    }
    
    private void loadTemplate(final TemplateManager mgr) {
        final Template tpl = mgr.func_200220_a(this.getStructureName());
        final PlacementSettings settings = new PlacementSettings().func_186222_a(true).func_215222_a((StructureProcessor)BlockIgnoreStructureProcessor.field_215204_a);
        this.func_186173_a(tpl, this.field_186178_c, settings);
    }
    
    public <T extends TemplateStructure> T setYOffset(final int yOffset) {
        this.yOffset = yOffset;
        return (T)this;
    }
    
    public abstract ResourceLocation getStructureName();
    
    public boolean func_230383_a_(final ISeedReader world, final StructureManager mgr, final ChunkGenerator gen, final Random rand, final MutableBoundingBox box, final ChunkPos chunkPos, final BlockPos structCenter) {
        final MutableBoundingBox genBox = new MutableBoundingBox(box);
        genBox.func_78886_a(0, this.yOffset, 0);
        final BlockPos original = this.field_186178_c;
        this.field_186178_c = original.func_177981_b(this.yOffset);
        try {
            return super.func_230383_a_(world, mgr, gen, rand, genBox, chunkPos, structCenter.func_177981_b(this.yOffset));
        }
        finally {
            this.field_186178_c = original;
            this.field_186177_b.func_186223_a(box);
            this.field_74887_e = this.field_186176_a.func_215388_b(this.field_186177_b, this.field_186178_c);
        }
    }
    
    protected void func_186175_a(final String function, final BlockPos pos, final IServerWorld worldIn, final Random rand, final MutableBoundingBox sbb) {
        if (sbb.func_175898_b((Vector3i)pos)) {
            MarkerManagerAS.handleMarker(function, pos, (IWorld)worldIn, rand, this.field_74887_e);
        }
    }
}
