package hellfirepvp.astralsorcery.common.base.patreon.types;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import java.util.EnumSet;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeNebulaCloud extends PatreonEffect implements ITickHandler
{
    private final UUID playerUUID;
    
    public TypeNebulaCloud(final UUID effectUUID, @Nullable final FlareColor flareColor, final UUID playerUUID) {
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
            this.spawnCloudParticles(player);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnCloudParticles(final Player player) {
        final Vector3 playerPos = Vector3.atEntityCorner((Entity)player).addY(0.10000000149011612);
        for (int i = 0; i < 3; ++i) {
            final float oX = (TypeNebulaCloud.rand.nextFloat() - TypeNebulaCloud.rand.nextFloat()) * 2.0f;
            final float oZ = (TypeNebulaCloud.rand.nextFloat() - TypeNebulaCloud.rand.nextFloat()) * 2.0f;
            final Vector3 offset = new Vector3(oX, TypeNebulaCloud.rand.nextFloat() * 0.1f, oZ);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(playerPos.clone().add(offset)).setAlphaMultiplier(0.8f).alpha(((VFXAlphaFunction<?>)((fx, alphaIn, pTicks) -> {
                if (this.shouldDoEffect(player) && Minecraft.func_71410_x().field_71474_y.func_243230_g().func_243192_a() && player.field_70125_A > 40.0f) {
                    return Mth.func_76131_a(1.0f - (player.field_70125_A - 40.0f) / 20.0f, 0.0f, 1.0f) * alphaIn;
                }
                else {
                    return alphaIn;
                }
            })).andThen(VFXAlphaFunction.FADE_OUT)).color(VFXColorFunction.WHITE).setScaleMultiplier(0.2f + TypeNebulaCloud.rand.nextFloat() * 0.3f).setMaxAge(40 + TypeNebulaCloud.rand.nextInt(20));
        }
        if (TypeNebulaCloud.rand.nextInt(16) == 0) {
            final Vector3 from = Vector3.random().setY(0).normalize().multiply(TypeNebulaCloud.rand.nextFloat() * 2.0f).addY(TypeNebulaCloud.rand.nextFloat() * 0.1f);
            final Vector3 to = Vector3.random().setY(0).normalize().multiply(TypeNebulaCloud.rand.nextFloat() * 2.0f).addY(TypeNebulaCloud.rand.nextFloat() * 0.1f);
            EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(playerPos.clone().add(from)).makeDefault(playerPos.clone().add(to)).color(VFXColorFunction.WHITE).alpha((fx, alphaIn, pTicks) -> {
                if (this.shouldDoEffect(player) && Minecraft.func_71410_x().field_71474_y.func_243230_g().func_243192_a() && player.field_70125_A > 40.0f) {
                    return Mth.func_76131_a(1.0f - (Math.abs(player.field_70125_A) - 40.0f) / 20.0f, 0.0f, 1.0f) * alphaIn;
                }
                else {
                    return alphaIn;
                }
            });
        }
    }
    
    private boolean shouldDoEffect(final Player player) {
        return player.getUUID().equals(this.playerUUID) && (player.func_213283_Z() == Pose.STANDING || player.func_213283_Z() == Pose.CROUCHING) && !player.func_70644_a(Effects.field_76441_p);
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "PatreonEffect - Nebula Cloud " + this.playerUUID.toString();
    }
}
