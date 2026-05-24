package hellfirepvp.astralsorcery.common.base.patreon.entity;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.client.effect.vfx.FXCrystal;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import java.util.UUID;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.resource.query.TextureQuery;

public class PatreonCrystalFlare extends PatreonFlare
{
    private TextureQuery queryTexture;
    private Color colorTheme;
    private Object crystalEffect;
    
    public PatreonCrystalFlare(final UUID effectUUID, final UUID ownerUUID) {
        super(effectUUID, ownerUUID);
        this.queryTexture = new TextureQuery(AssetLoader.TextureLocation.MODEL, new String[] { "crystal_blue" });
        this.colorTheme = Color.WHITE;
        this.crystalEffect = null;
    }
    
    public PatreonCrystalFlare setQueryTexture(final TextureQuery queryTexture) {
        this.queryTexture = queryTexture;
        return this;
    }
    
    public PatreonCrystalFlare setColorTheme(final Color colorTheme) {
        this.colorTheme = colorTheme;
        return this;
    }
    
    @Override
    public void tickClient() {
        super.tickClient();
        if (this.crystalEffect != null) {
            final FXCrystal crystal = (FXCrystal)this.crystalEffect;
            if (crystal.isRemoved() && (boolean)RenderingConfig.CONFIG.patreonEffects.get()) {
                EffectHelper.refresh(crystal, EffectTemplatesAS.CRYSTAL);
            }
        }
        else {
            this.crystalEffect = EffectHelper.of(EffectTemplatesAS.CRYSTAL).spawn(this.getPos()).setTexture(this.queryTexture).setLightRayColor(this.colorTheme).setScaleMultiplier(0.03f).position((fx, position, motionToBeMoved) -> this.getPos().clone()).refresh(fx -> !this.removed && (boolean)RenderingConfig.CONFIG.patreonEffects.get());
        }
    }
}
