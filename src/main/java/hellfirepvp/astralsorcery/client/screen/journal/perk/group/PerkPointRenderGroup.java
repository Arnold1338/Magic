package hellfirepvp.astralsorcery.client.screen.journal.perk.group;

import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkRenderGroup;

@OnlyIn(Dist.CLIENT)
public class PerkPointRenderGroup extends PerkRenderGroup
{
    public static final PerkPointRenderGroup INSTANCE;
    
    private PerkPointRenderGroup() {
        this.add(SpritesAS.SPR_PERK_INACTIVE, 100);
        this.add(SpritesAS.SPR_PERK_ACTIVE, 110);
        this.add(SpritesAS.SPR_PERK_ACTIVATEABLE, 120);
    }
    
    static {
        INSTANCE = new PerkPointRenderGroup();
    }
}
