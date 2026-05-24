package hellfirepvp.astralsorcery.common.base.patreon.entity;

import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeFlareColor;
import java.awt.Color;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import java.util.UUID;

public class PatreonFlareDynamicColor extends PatreonFlare
{
    public PatreonFlareDynamicColor(final UUID effectUUID, final UUID ownerUUID) {
        super(effectUUID, ownerUUID);
    }
    
    @Nullable
    @Override
    protected SpriteQuery getSpriteQuery() {
        return new SpriteQuery(AssetLoader.TextureLocation.EFFECT, 1, 48, new String[] { "patreonflares", "gray_mono" });
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected Color getColor() {
        final PatreonEffect effect = this.getEffect();
        if (!(effect instanceof TypeFlareColor)) {
            return Color.WHITE;
        }
        final Color color = ((TypeFlareColor)effect).getColorProvider().get();
        return (PatreonFlareDynamicColor.rand.nextInt(3) == 0) ? color : color.brighter();
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickClient() {
        if (this.clientSprite != null) {
            final FXFacingSprite p = (FXFacingSprite)this.clientSprite;
            if (!p.isRemoved() && (boolean)RenderingConfig.CONFIG.patreonEffects.get()) {
                final PatreonEffect effect = this.getEffect();
                if (effect instanceof TypeFlareColor) {
                    p.color(VFXColorFunction.constant(((TypeFlareColor)effect).getColorProvider().get()));
                }
            }
        }
        super.tickClient();
    }
}
