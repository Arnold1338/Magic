package hellfirepvp.astralsorcery.client.screen.journal.perk.group;

import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkRenderGroup;

@OnlyIn(Dist.CLIENT)
public class PerkPointHaloRenderGroup extends PerkRenderGroup
{
    public static final PerkPointHaloRenderGroup INSTANCE;
    
    private PerkPointHaloRenderGroup() {
        this.add(SpritesAS.SPR_PERK_HALO_INACTIVE, 150);
        this.add(SpritesAS.SPR_PERK_HALO_ACTIVE, 160);
        this.add(SpritesAS.SPR_PERK_HALO_ACTIVATEABLE, 170);
    }
    
    static {
        INSTANCE = new PerkPointHaloRenderGroup();
    }
}
