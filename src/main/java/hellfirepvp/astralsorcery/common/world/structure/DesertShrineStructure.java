package hellfirepvp.astralsorcery.common.world.structure;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.lib.WorldGenerationAS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.gen.feature.template.TemplateManager;
import hellfirepvp.astralsorcery.common.world.TemplateStructure;

public class DesertShrineStructure extends TemplateStructure
{
    public DesertShrineStructure(final TemplateManager mgr, final BlockPos templatePosition) {
        super(WorldGenerationAS.Structures.DESERT_SHRINE_PIECE, mgr, templatePosition);
        this.setYOffset(-11);
    }
    
    public DesertShrineStructure(final TemplateManager mgr, final CompoundTag nbt) {
        super(WorldGenerationAS.Structures.DESERT_SHRINE_PIECE, mgr, nbt);
        this.setYOffset(-11);
    }
    
    @Override
    public ResourceLocation getStructureName() {
        return WorldGenerationAS.Structures.KEY_DESERT_SHRINE;
    }
}
