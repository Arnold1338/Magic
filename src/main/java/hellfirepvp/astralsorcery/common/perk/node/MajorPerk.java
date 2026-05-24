package hellfirepvp.astralsorcery.common.perk.node;

import hellfirepvp.astralsorcery.common.perk.tree.PerkTreeMajor;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierPerk;

public class MajorPerk extends AttributeModifierPerk
{
    public MajorPerk(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.setCategory(MajorPerk.CATEGORY_MAJOR);
    }
    
    @Override
    protected PerkTreePoint<? extends MajorPerk> initPerkTreePoint() {
        return new PerkTreeMajor<MajorPerk>((MajorPerk)this, this.getOffset());
    }
}
