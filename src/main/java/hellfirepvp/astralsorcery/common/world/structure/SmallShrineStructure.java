package hellfirepvp.astralsorcery.common.world.structure;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.lib.WorldGenerationAS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.gen.feature.template.TemplateManager;
import hellfirepvp.astralsorcery.common.world.TemplateStructure;

public class SmallShrineStructure extends TemplateStructure
{
    public SmallShrineStructure(final TemplateManager mgr, final BlockPos templatePosition) {
        super(WorldGenerationAS.Structures.SMALL_SHRINE_PIECE, mgr, templatePosition);
    }
    
    public SmallShrineStructure(final TemplateManager mgr, final CompoundTag nbt) {
        super(WorldGenerationAS.Structures.SMALL_SHRINE_PIECE, mgr, nbt);
    }
    
    @Override
    public ResourceLocation getStructureName() {
        return WorldGenerationAS.Structures.KEY_SMALL_SHRINE;
    }
}
