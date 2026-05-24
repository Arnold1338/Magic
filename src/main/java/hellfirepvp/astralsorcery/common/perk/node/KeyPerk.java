package hellfirepvp.astralsorcery.common.perk.node;

import net.minecraft.resources.ResourceLocation;

public class KeyPerk extends MajorPerk
{
    public KeyPerk(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.setCategory(KeyPerk.CATEGORY_KEY);
    }
}
