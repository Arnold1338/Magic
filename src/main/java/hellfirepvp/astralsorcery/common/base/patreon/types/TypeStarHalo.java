package hellfirepvp.astralsorcery.common.base.patreon.types;

import java.util.EnumSet;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXPositionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeStarHalo extends PatreonEffect implements ITickHandler
{
    private final UUID playerUUID;
    
    public TypeStarHalo(final UUID effectUUID, @Nullable final FlareColor flareColor, final UUID playerUUID) {
        super(effectUUID, flareColor);
        this.playerUUID = playerUUID;
    }
    
    @Override
    public void attachTickListeners(final Consumer<ITickHandler> registrar) {
        super.attachTickListeners(registrar);
        registrar.accept((ITickHandler)this);
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        if (side.isClient() && this.shouldDoEffect(player)) {
            this.spawnHaloParticles(player);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnHaloParticles(final Player player) {
        final Vector3 headPos = Vector3.atEntityCorner((Entity)player).addY(player.func_213307_e(player.func_213283_Z()));
        for (int i = 0; i < 3; ++i) {
            final Vector3 offset = MiscUtils.getRandomCirclePosition(new Vector3(), Vector3.RotAxis.Y_AXIS, 0.30000001192092896);
            final float scale = 0.16f + TypeStarHalo.rand.nextFloat() * 0.12f;
            final int age = 20 + TypeStarHalo.rand.nextInt(10);
            MiscUtils.applyRandomOffset(offset, TypeStarHalo.rand, 0.02f);
            final FXFacingParticle particle = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(headPos.clone().addY(0.4000000059604645).add(offset)).setAlphaMultiplier(0.8f).alpha(((VFXAlphaFunction<?>)((fx, alphaIn, pTicks) -> {
                if (this.shouldDoEffect(player) && Minecraft.getInstance().options.func_243230_g().func_243192_a() && player.xRot < -30.0f) {
                    return Mth.canEnchant(1.0f - (Math.abs(player.xRot) - 30.0f) / 15.0f, 0.0f, 1.0f) * alphaIn;
                }
                else {
                    return alphaIn;
                }
            })).andThen(VFXAlphaFunction.PYRAMID)).color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_TYPE_WEAK)).setScaleMultiplier(scale).setMaxAge(age);
            if (TypeStarHalo.rand.nextInt(3) == 0) {
                particle.color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_VICIO));
            }
            FXFacingParticle starParticle = null;
            if (TypeStarHalo.rand.nextInt(5) == 0) {
                starParticle = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(headPos.clone().addY(0.4000000059604645).add(offset)).setAlphaMultiplier(0.8f).color(VFXColorFunction.WHITE).alpha(((VFXAlphaFunction<?>)((fx, alphaIn, pTicks) -> {
                    if (this.shouldDoEffect(player) && Minecraft.getInstance().options.func_243230_g().func_243192_a() && player.xRot < -30.0f) {
                        return Mth.canEnchant(1.0f - (Math.abs(player.xRot) - 30.0f) / 15.0f, 0.0f, 1.0f) * alphaIn;
                    }
                    else {
                        return alphaIn;
                    }
                })).andThen(VFXAlphaFunction.PYRAMID)).setScaleMultiplier(scale * 0.6f).setMaxAge(age);
            }
            if (TypeStarHalo.rand.nextInt(4) != 0) {
                particle.position(new VFXPositionController<EntityVisualFX>() {
                    @Nonnull
                    @Override
                    public Vector3 updatePosition(@Nonnull final EntityVisualFX fx, @Nonnull final Vector3 position, @Nonnull final Vector3 motionToBeMoved) {
                        if (TypeStarHalo.this.shouldDoEffect(player)) {
                            final Vector3 diff = new Vector3(player.field_70169_q - player.getX(), player.field_70167_r - player.getY(), player.field_70166_s - player.getZ());
                            diff.divide(4.0);
                            return Vector3.atEntityCorner((Entity)player).add(diff).addY(player.func_213307_e(player.func_213283_Z())).addY(0.4000000059604645).add(offset);
                        }
                        return new Vector3();
                    }
                });
                if (starParticle != null) {
                    starParticle.position(new VFXPositionController<EntityVisualFX>() {
                        @Nonnull
                        @Override
                        public Vector3 updatePosition(@Nonnull final EntityVisualFX fx, @Nonnull final Vector3 position, @Nonnull final Vector3 motionToBeMoved) {
                            if (TypeStarHalo.this.shouldDoEffect(player)) {
                                final Vector3 diff = new Vector3(player.field_70169_q - player.getX(), player.field_70167_r - player.getY(), player.field_70166_s - player.getZ());
                                diff.divide(4.0);
                                return Vector3.atEntityCorner((Entity)player).add(diff).addY(player.func_213307_e(player.func_213283_Z())).addY(0.4000000059604645).add(offset);
                            }
                            return new Vector3();
                        }
                    });
                }
            }
        }
    }
    
    private boolean shouldDoEffect(final Player player) {
        return player.getUUID().equals(this.playerUUID) && (player.func_213283_Z() == Pose.STANDING || player.func_213283_Z() == Pose.CROUCHING) && !player.hasEffect(Effects.field_76441_p);
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "PatreonEffect - Star halo " + this.playerUUID.toString();
    }
}
