package hellfirepvp.astralsorcery.common.base.patreon.entity;

import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXPositionController;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingSprite;
import java.util.UUID;

public class PatreonFlare extends PatreonPartialEntity
{
    public Object clientSprite;
    
    public PatreonFlare(final UUID effectUUID, final UUID ownerUUID) {
        super(effectUUID, ownerUUID);
        this.clientSprite = null;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickClient() {
        super.tickClient();
        if (this.clientSprite != null) {
            final FXFacingSprite sprite = (FXFacingSprite)this.clientSprite;
            if (sprite.isRemoved() && (boolean)RenderingConfig.CONFIG.patreonEffects.get()) {
                this.clientSprite = null;
            }
        }
        if (this.clientSprite == null) {
            final SpriteQuery sprite2 = this.getSpriteQuery();
            if (sprite2 != null) {
                this.clientSprite = EffectHelper.of(EffectTemplatesAS.FACING_SPRITE).spawn(this.pos).setSprite(sprite2.resolveSprite()).setScaleMultiplier(0.35f).position(new VFXPositionController<EntityVisualFX>() {
                    @Nonnull
                    @Override
                    public Vector3 updatePosition(@Nonnull final EntityVisualFX fx, @Nonnull final Vector3 position, @Nonnull final Vector3 motionToBeMoved) {
                        return PatreonFlare.this.pos.clone();
                    }
                }).refresh(fx -> !this.removed && (boolean)RenderingConfig.CONFIG.patreonEffects.get());
            }
        }
    }
    
    @Nullable
    protected SpriteQuery getSpriteQuery() {
        final FlareColor color = this.getFlareColor();
        if (color != null) {
            return color.getSpriteQuery();
        }
        return null;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickEffects(final Level world) {
        super.tickEffects(world);
        if (!(boolean)RenderingConfig.CONFIG.patreonEffects.get() || PatreonFlare.rand.nextBoolean()) {
            return;
        }
        final Color c = this.getColor();
        if (c == null) {
            return;
        }
        final int age = 30 + PatreonFlare.rand.nextInt(15);
        final float scale = 0.1f + PatreonFlare.rand.nextFloat() * 0.1f;
        final Vector3 at = new Vector3(this.pos);
        at.add(PatreonFlare.rand.nextFloat() * 0.08 * (PatreonFlare.rand.nextBoolean() ? 1 : -1), PatreonFlare.rand.nextFloat() * 0.08 * (PatreonFlare.rand.nextBoolean() ? 1 : -1), PatreonFlare.rand.nextFloat() * 0.08 * (PatreonFlare.rand.nextBoolean() ? 1 : -1));
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(scale).color(VFXColorFunction.constant(c)).setMaxAge(age);
        if (PatreonFlare.rand.nextBoolean()) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(scale * 0.3f).setMaxAge(age - 10);
        }
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    protected Color getColor() {
        final FlareColor col = this.getFlareColor();
        if (col == null) {
            return null;
        }
        return (PatreonFlare.rand.nextInt(3) == 0) ? col.color2 : col.color1;
    }
    
    @Nullable
    private FlareColor getFlareColor() {
        final PatreonEffect effect = this.getEffect();
        if (effect != null) {
            return effect.getFlareColor();
        }
        return null;
    }
}
