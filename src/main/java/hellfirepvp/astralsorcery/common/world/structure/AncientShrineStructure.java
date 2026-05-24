package hellfirepvp.astralsorcery.common.world.structure;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.lib.WorldGenerationAS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.gen.feature.template.TemplateManager;
import hellfirepvp.astralsorcery.common.world.TemplateStructure;

public class AncientShrineStructure extends TemplateStructure
{
    public AncientShrineStructure(final TemplateManager mgr, final BlockPos templatePosition) {
        super(WorldGenerationAS.Structures.ANCIENT_SHRINE_PIECE, mgr, templatePosition);
        this.setYOffset(-7);
    }
    
    public AncientShrineStructure(final TemplateManager mgr, final CompoundTag nbt) {
        super(WorldGenerationAS.Structures.ANCIENT_SHRINE_PIECE, mgr, nbt);
        this.setYOffset(-7);
    }
    
    @Override
    public ResourceLocation getStructureName() {
        return WorldGenerationAS.Structures.KEY_ANCIENT_SHRINE;
    }
}
